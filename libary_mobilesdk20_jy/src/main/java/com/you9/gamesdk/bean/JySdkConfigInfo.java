package com.you9.gamesdk.bean;

import java.io.Serializable;

public class JySdkConfigInfo extends JySystemInfo implements Serializable {

    private static JySdkConfigInfo sdkConfigInfo;
    private String clientId;
    private String appId;
    private String gameName;
    private String channelName;
    private String channelId;
    private String company;
    private String kfUrl;
    private String kfTel;
    private int touTiaoAppId;
    private String gdtActionSetId;
    private String gdtSecretKey;
    private String reportType;  //详细type类型见JyPromotionChannelType类
    private String e1;
    private String e2;
    private String e3;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getKfUrl() {
        return kfUrl;
    }

    public void setKfUrl(String kfUrl) {
        this.kfUrl = kfUrl;
    }

    public String getKfTel() {
        return kfTel;
    }

    public void setKfTel(String kfTel) {
        this.kfTel = kfTel;
    }

    public int getTouTiaoAppId() {
        return touTiaoAppId;
    }

    public void setTouTiaoAppId(int touTiaoAppId) {
        this.touTiaoAppId = touTiaoAppId;
    }

    public String getGdtActionSetId() {
        return gdtActionSetId;
    }

    public void setGdtActionSetId(String gdtActionSetId) {
        this.gdtActionSetId = gdtActionSetId;
    }

    public String getGdtSecretKey() {
        return gdtSecretKey;
    }

    public void setGdtSecretKey(String gdtSecretKey) {
        this.gdtSecretKey = gdtSecretKey;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getE1() {
        return e1;
    }

    public void setE1(String e1) {
        this.e1 = e1;
    }

    public String getE2() {
        return e2;
    }

    public void setE2(String e2) {
        this.e2 = e2;
    }

    public String getE3() {
        return e3;
    }

    public void setE3(String e3) {
        this.e3 = e3;
    }

    private JySdkConfigInfo() {
    }

    public static JySdkConfigInfo getInstance() {
        synchronized (JySdkConfigInfo.class) {
            if (sdkConfigInfo == null) {
                sdkConfigInfo = new JySdkConfigInfo();
            }
        }
        return sdkConfigInfo;
    }
}
