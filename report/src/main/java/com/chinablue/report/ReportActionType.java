package com.chinablue.report;

public enum ReportActionType {

    /**
     * 阅读
     */
    VIEW(1),
    /**
     * 点赞
     */
    LIKE(2),

    /**
     * 收藏
     */
    COLLECTION(3),

    /**
     * 评论
     */
    COMMENT(4),

    /**
     * 转发
     */
    REPOST(5),

    /**
     * 不喜欢
     */
    DISLIKE(6),

    /**
     * 报告
     */
    REPORT(7),

    /**
     * 播放
     */
    PLAY(8),

    /**
     * 拖拽
     */
    DRAG(9),

    /**
     * 加载
     */
    LOADING(10),

    /**
     * 缓冲
     */
    BUFFERING(11),

    /**
     * 暂停
     */
    PAUSE(12),

    /**
     * 停止
     */
    STOP(13),

    /**
     * 启动
     */
    LAUNCH(14);

    private int value;

    private ReportActionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
