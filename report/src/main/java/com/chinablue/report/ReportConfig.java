package com.chinablue.report;

import android.content.Context;
import android.text.TextUtils;

import com.chinablue.report.util.SPUtils;
import com.chinablue.report.util.Utils;

public class ReportConfig {

    public Context context;

    // 租户ID， 产品ID
    public String customer_id = "2", product_id = "2";
    // 设备Id
    public String device_Id = "";
    // 请求域名
    public String url = "http://algo.cztv.com";//"http://10.30.129.68";//"http://algo.cztv.com";
    // 上报时间间隔
    public int step = 30;
    // 是否开启日志打印
    public boolean isLog = false;

    public static class Builder {
        private ReportConfig config;

        public static Builder init(Context context) {
            Utils.init(context);

            Builder builder = new Builder();
            ReportConfig config = new ReportConfig();
            config.context = context;
            config.device_Id = context != null && !TextUtils.isEmpty(SPUtils.getInstance().getString("deviceId")) ? SPUtils.getInstance().getString("deviceId") : Utils.getDeviceID(null);
            builder.config = config;
            return builder;
        }

        public Builder setCustomerId(String customer_id) {
            config.customer_id = customer_id;
            return this;
        }

        public Builder setProductId(String product_id) {
            config.product_id = product_id;
            return this;
        }

        public Builder setDeviceId(String device_Id) {
            config.device_Id = device_Id;

            if (Utils.getContext() != null)
                SPUtils.getInstance().put("deviceId", device_Id);
            return this;
        }


        public Builder setStep(int step) {
            if (step < 0)
                step = 0;

            config.step = step;
            return this;
        }

        public Builder setUrl(String url) {
            config.url = url;
            return this;
        }

        public Builder setLogEnabled(boolean isLog) {
            config.isLog = isLog;
            return this;
        }

        public ReportConfig get() {
            return config;
        }

        public void apply() {
            ReportManager.setConfig(config);
        }
    }

}
