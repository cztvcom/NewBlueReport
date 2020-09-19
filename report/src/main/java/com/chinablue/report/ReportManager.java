package com.chinablue.report;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chinablue.report.http.MySubscriber;
import com.chinablue.report.http.RetrofitClient;
import com.chinablue.report.util.LogUtil;
import com.chinablue.report.util.NetworkUtil;
import com.chinablue.report.util.SPUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public final class ReportManager {

    private static ReportConfig config;
    private static String g_father_id = "", g_origin_id = "", origin_id = "";
    private static ReportAdreeEntity reportAdreeEntity;
    private static ReportActionEntity reportActionEntity;
    private static ReportUserEntity reportUserEntity;
    private static Subscription reportSubscription;
    private static int currentVideoTime = 0;// 当前视频时间
    private static Map extra;

    public static void setConfig(ReportConfig config) {
        ReportManager.config = config;
        new LogUtil(config.isLog);
        LogUtil.i("ReportManager init");
    }

    /**
     * 更新用户信息
     *
     * @param reportUserEntity1 用户信息
     */
    public static void setReportUserEntity(ReportUserEntity reportUserEntity1) {
        reportUserEntity = reportUserEntity1;
    }

    /**
     * 更新地址
     *
     * @param reportAdreeEntity1 用户地址信息
     */
    public static void setReportAdreeEntity(ReportAdreeEntity reportAdreeEntity1) {
        reportAdreeEntity = reportAdreeEntity1;
    }

    /**
     * 更新播放时间
     *
     * @param currentVideoTime1 当前视频视频
     */
    public static void setCurrentVideoTime(int currentVideoTime1) {
        currentVideoTime = currentVideoTime1;
    }

    /**
     * 授权，获取token
     *
     * @param origin_id1 用户ID，未登录传空
     */
    public static void authorizations(String origin_id1, Map extra) {
        getToken(origin_id1, extra, null);
    }

    public static void authorizations(String origin_id1, Map extra, AuthorizationsCallback callback1) {
        getToken(origin_id1, extra, callback1);
    }

    private static void getToken(String origin_id1, Map extra1, final AuthorizationsCallback callback) {
        if (config == null)
            return;

        HashMap<String, Object> map = new HashMap<>();
        map.put("customer_id", config.customer_id);
        map.put("product_id", config.product_id);
        map.put("device_id", config.device_Id);

        if (extra1 != null) {
            extra = extra1;
            map.put("extra", JSON.toJSON(extra));
        }

        if (!TextUtils.isEmpty(origin_id1)) {
            map.put("origin_id", origin_id1);
            origin_id = origin_id1;
        }

        if (reportUserEntity != null || reportAdreeEntity != null) {
            Map map1 = new HashMap();

            if (reportUserEntity != null)
                map1.putAll(JSON.parseObject(JSON.toJSONString(reportUserEntity)));
            if (reportAdreeEntity != null)
                map1.putAll(JSON.parseObject(JSON.toJSONString(reportAdreeEntity)));

            Set set = map1.keySet();
            for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                if (map1.get(key) instanceof String && !TextUtils.isEmpty((String) map1.get(key))) {
                    map.put((String) key, map1.get(key));
                }
            }
        }

        LogUtil.i("authorizations=" + JSON.toJSONString(map));
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(map));
        if (NetworkUtil.isNetworkAvailable(config.context)) {
            new RetrofitClient(config.url).post("/api/authorizations", requestBody, new MySubscriber() {
                @Override
                public void onNext(Object o) {
                    try {
                        LogUtil.i("code=" + ((Response) o).code());

                        if (o instanceof Response && ((Response) o).isSuccessful()) {
                            retrofit2.Response<String> response = (retrofit2.Response<String>) o;
                            JSONObject jsonpObject = JSON.parseObject(response.body());
                            if (jsonpObject.containsKey("access_token") && jsonpObject.containsKey("token_type")) {
                                SPUtils.getInstance().put("access_token", jsonpObject.getString("access_token"));
                                SPUtils.getInstance().put("token_type", jsonpObject.getString("token_type"));

                                if (callback != null)
                                    callback.onSuccess();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    /**
     * 取消授权，注销token
     */
    public static void cancelAuthorized() {
        if (config == null)
            return;

        if (NetworkUtil.isNetworkAvailable(config.context)) {

            LogUtil.i("cancelAuthorized");

            if (NetworkUtil.isNetworkAvailable(config.context)) {
                new RetrofitClient(config.url).delet("/api/authorizations", new HashMap<>(), new MySubscriber() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onNext(Object o) {
                        try {
                            LogUtil.i("code=" + ((Response) o).code());
                            if (o instanceof Response && ((Response) o).isSuccessful()) {
                                SPUtils.getInstance().clear();
                                origin_id = "";
                                authorizations(null, extra);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    /**
     * 上报动作信息
     *
     * @param entity 上报所需的参数，具体参考 ReportActionEntity 和 ReportActionType注释
     */
    public static void pushAction(final ReportActionEntity entity) {
        try {
            if (config == null)
                return;

            if (entity == null || TextUtils.isEmpty(JSON.toJSON(entity).toString()) || !NetworkUtil.isNetworkAvailable(config.context))
                return;

            entity.setProduct_id(config.product_id);

            final String uuId = UUID.randomUUID().toString();

            if (entity.getReportAdreeEntity() != null)
                reportAdreeEntity = entity.getReportAdreeEntity();
            else
                entity.setReportAdreeEntity(reportAdreeEntity);

            if (entity.getAction_type() == ReportActionType.PLAY.getValue())
                reportActionEntity = entity;  //记录下来 上报时用
            if (entity.getAction_type() == ReportActionType.VIEW.getValue())
                g_origin_id = UUID.randomUUID().toString();

            HashMap<String, Object> map = new HashMap<>();
            map.put("g_id", uuId);

            if (entity.getAction_type() == ReportActionType.LAUNCH.getValue())
                g_origin_id = uuId;  //g_origin_id 和g_id保持一致

            if (!TextUtils.isEmpty(g_father_id))
                map.put("g_father_id", g_father_id);
            if (!TextUtils.isEmpty(g_origin_id))
                map.put("g_origin_id", g_origin_id);


            // 需滤掉非空的 value 不然上报不成功
            Map maps = JSON.parseObject(JSON.toJSONString(entity));
            Set set = maps.keySet();
            for (Iterator iterator = set.iterator(); iterator.hasNext(); ) {
                Object key = iterator.next();
                if (maps.get(key) instanceof String && !TextUtils.isEmpty((String) maps.get(key)) || maps.get(key) instanceof Integer && (int) maps.get(key) >= 0)
                    map.put((String) key, maps.get(key));

                if (maps.get(key) instanceof Map)
                    map.put((String) key, JSON.toJSON(maps.get(key)));

                if (maps.get(key) instanceof JSONObject) {
                    Map maps2 = JSON.parseObject(JSON.toJSONString(maps.get(key)));
                    Set set2 = maps2.keySet();
                    for (Iterator iterator2 = set2.iterator(); iterator2.hasNext(); ) {
                        Object key2 = iterator2.next();
                        if (maps2.get(key2) instanceof String && !TextUtils.isEmpty((String) maps2.get(key2))) {
                            map.put((String) key2, maps2.get(key2));
                        }
                    }
                }
            }

            LogUtil.i("pushAction=" + JSON.toJSONString(map));
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(map));
            new RetrofitClient(config.url).post("/api/algo/pushAction", requestBody, new MySubscriber() {
                @Override
                public void onNext(Object o) {
                    try {
                        LogUtil.i("code=" + ((Response) o).code());
                        if (o instanceof Response && ((Response) o).isSuccessful()) {
                            g_father_id = uuId;

                            if (entity.getAction_type() == ReportActionType.PLAY.getValue())
                                regularReport();

                            if (entity.getAction_type() == ReportActionType.STOP.getValue()) {
//                                g_origin_id = "";
                                reportActionEntity = null;

                                if (reportSubscription != null && !reportSubscription.isUnsubscribed())
                                    reportSubscription.unsubscribe();
                            }
                        } else if (((Response) o).code() == 401) {
                            authorizations(origin_id, extra);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //视频播放时,定时上报
    private static void regularReport() {
        try {
            if (config == null)
                return;

            if (reportSubscription != null && !reportSubscription.isUnsubscribed())
                reportSubscription.unsubscribe();

            reportSubscription = Observable.interval(config.step, config.step, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .onBackpressureDrop()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MySubscriber() {
                        @Override
                        public void onNext(Object o) {
                            try {
                                LogUtil.i("regularReport");
                                if (reportActionEntity != null) {
                                    reportActionEntity.setAction_type(ReportActionType.REPORT.getValue());
                                    reportActionEntity.setAction_start(currentVideoTime);
                                    reportActionEntity.setStep(config.step);
                                    pushAction(reportActionEntity);
                                } else {
                                    reportSubscription.unsubscribe();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消上报订阅
     */
    public static void unSubscribeReport() {
        if (config == null)
            return;

        if (reportSubscription != null && !reportSubscription.isUnsubscribed())
            reportSubscription.unsubscribe();

        LogUtil.i("unSubscribeReport");
    }

    public interface AuthorizationsCallback {
        void onSuccess();
    }

}
