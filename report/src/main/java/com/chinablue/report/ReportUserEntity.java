package com.chinablue.report;

public class ReportUserEntity {
    private String name = "";
    private String nickname = "";
    private String birthday = "";   //格式 2020-01-05
    private String sex = "";
    private String phone = "";
    private String qq_openid = "";
    private String weixin_openid = "";
    private String weibo_openid = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq_openid() {
        return qq_openid;
    }

    public void setQq_openid(String qq_openid) {
        this.qq_openid = qq_openid;
    }

    public String getWeixin_openid() {
        return weixin_openid;
    }

    public void setWeixin_openid(String weixin_openid) {
        this.weixin_openid = weixin_openid;
    }

    public String getWeibo_openid() {
        return weibo_openid;
    }

    public void setWeibo_openid(String weibo_openid) {
        this.weibo_openid = weibo_openid;
    }
}
