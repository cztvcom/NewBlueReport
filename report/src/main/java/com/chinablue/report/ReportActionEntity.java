package com.chinablue.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ReportActionEntity {

    /**
     * g_id : 为当前动作生成的UUID
     * g_father_id : 如果有前置动作,为前置动作的g_id
     * item_type : 产品内部定义 传当前资讯对象的类型 在算法库内登记的ID 格式为int(6)
     * product_id : 频道ID
     * origin_item_id : 视频ID
     * action_time : 动作发生的时间 如 “2020-07-19 11:54:46”,
     * action_type : 参考ReportActionType类， 除7以外没有step参数
     * action_start : 进度条当前秒数 如 60(01:00) 其中action_type=123456 10没有action_start参数
     * step : 所报告g_id的动作新产生的时长增长秒数 如 30(该g_id动作持续时间增加30秒)
     *
     * <p> —-以下参数选填—-
     * country : 中国
     * province : 浙江
     * city : 杭州
     * district : 萧山
     * extra : {}  json {“参数1”:”参数1值”,“参数2”:”参数2值”}
     */

    private int item_type = 1;
    private String product_id = "1";
    private int origin_item_id = -1;
    private String action_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    private int action_type = -1;
    private int action_start = -1;
    private int step = -1;
    private ReportAdreeEntity reportAdreeEntity;
    private Map extra;

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public int getItem_type() {
        return item_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public int getOrigin_item_id() {
        return origin_item_id;
    }

    public void setOrigin_item_id(int origin_item_id) {
        this.origin_item_id = origin_item_id;
    }

    public String getAction_time() {
        return action_time;
    }

    public int getAction_type() {
        return action_type;
    }

    public void setAction_type(int action_type) {
        this.action_type = action_type;
    }

    public int getAction_start() {
        return action_start;
    }

    public void setAction_start(int action_start) {
        this.action_start = action_start;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public ReportAdreeEntity getReportAdreeEntity() {
        return reportAdreeEntity;
    }

    public void setReportAdreeEntity(ReportAdreeEntity reportAdreeEntity) {
        this.reportAdreeEntity = reportAdreeEntity;
    }

    public Map getExtra() {
        return extra;
    }

    public void setExtra(Map extra) {
        this.extra = extra;
    }
}
