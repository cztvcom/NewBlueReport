package com.chinablue.report.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.chinablue.report.interceptor.StringConverterFactory;
import com.chinablue.report.util.SPUtils;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.platform.Platform;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Part;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitClient {
    private String defaultUrl = "http://algo.cztv.com";
    private String url = "";
    private Map<String, String> headers;
    private OkHttpClient okHttpClient = null;
    private static final int DEFAULT_TIMEOUT = 20;
    public static final int CACHE_STALE_SHORT = 60;
    public static final int CACHE_STALE_LONG = 60 * 60 * 24 * 7;
    public static final String CACHE_CONTROL_AGE = "Cache-Control: public, max-age=";
//    private Cache cache = new Cache(new File(Utils.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

    private static class SingleonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient();
    }

    public static RetrofitClient getInstance() {
        return SingleonHolder.INSTANCE;
    }

    public RetrofitClient() {
        this.url = defaultUrl;
    }

    public RetrofitClient(String url) {
        this.url = url;
    }

    private Context mContext;

    public RetrofitClient setHeaders(Map<String, String> headers, Context context) {
        this.headers = headers;
        this.mContext = context;

        return this;
    }

    public Observable<Response<String>> getObservable(String path, Map<String, String> maps) {
        return createService(RetrofitApi.class, url, headers)
                .getObservableMap(path, maps != null ? maps : new HashMap<String, String>());
    }

    public Subscriber get(String path, Map<String, String> maps, Subscriber subscriber) {
        return (Subscriber) createService(RetrofitApi.class, url, headers)
                .getObservableMap(path, maps != null ? maps : new HashMap<String, String>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscriber post(String path, Object t, Subscriber subscriber) {
        if (t != null && t instanceof RequestBody)
            return (Subscriber) createService(RetrofitApi.class, url, headers)
                    .postObservableRequestBody(path, (RequestBody) t)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        if (t != null && t instanceof Map)
            return (Subscriber) createService(RetrofitApi.class, url, headers)
                    .postObservableMap(path, t != null ? (HashMap<String, String>) t : new HashMap<String, String>())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        return (Subscriber) createService(RetrofitApi.class, url, headers)
                .postObservableObject(path, t)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscriber put(String path, Object t, Subscriber subscriber) {
        if (t != null && t instanceof RequestBody)
            return (Subscriber) createService(RetrofitApi.class, url, headers)
                    .putObservableRequestBody(path, (RequestBody) t)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        return (Subscriber) createService(RetrofitApi.class, url, headers)
                .putObject(path, t)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscriber delet(String path, Object t, Subscriber subscriber) {
        if (t != null && t instanceof RequestBody)
            return (Subscriber) createService(RetrofitApi.class, url, headers)
                    .deletObservableRequestBody(path, (RequestBody) t)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        if (t != null && t instanceof Map)
            return (Subscriber) createService(RetrofitApi.class, url, headers)
                    .deleteObservableMap(path, t != null ? (HashMap<String, String>) t : new HashMap<String, String>())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        return (Subscriber) createService(RetrofitApi.class, url, headers)
                .deletObservableObject(path, t)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public Subscriber from(String path, List<MultipartBody.Part> partLis, Subscriber subscriber) {
        return (Subscriber) createService(RetrofitApi.class, url, headers)
                .fromObservableMap(path, partLis)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private <S> S createService(Class<S> serviceClass, final String url, final Map<String, String> headers) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);
//                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .addInterceptor(new LoggingInterceptor.Builder()
//                        .loggable(BuildConfig.DEBUG)
//                        .setLevel(Level.BASIC)
//                        .log(Platform.INFO)
//                        .request("Request")
//                        .response("Response")
//                        .build()
//                );
//                .cache(new Cache(new File(mContext.getCacheDir(), "HttpCache"), 1024 * 1024 * 10));

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request.Builder newrequest = chain.request().newBuilder();
                try {
                    newrequest.addHeader("Accept", "application/json");
                    newrequest.addHeader("Content-Type", "application/json");

                    if (!TextUtils.isEmpty(SPUtils.getInstance().getString("access_token")))
                        newrequest.addHeader("Authorization", SPUtils.getInstance().getString("token_type") + " " + SPUtils.getInstance().getString("access_token"));

                    if (headers != null && headers.size() > 0) {
                        Iterator i = headers.entrySet().iterator();
                        while (i.hasNext()) {
                            Map.Entry entry = (java.util.Map.Entry) i.next();
                            newrequest.addHeader(String.valueOf(entry.getKey()).trim(), String.valueOf(entry.getValue()).trim());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                okhttp3.Request request1 = newrequest.build();
                return chain.proceed(request1);
            }
        });

        okHttpClient = clientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(StringConverterFactory.create())
                .baseUrl(url)
                .build();

        return retrofit.create(serviceClass);
    }
}
