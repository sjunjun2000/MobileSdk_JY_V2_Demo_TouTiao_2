package com.you9.gamesdk.enums;

/**
 * 状态码
 */
public enum JyPromotionChannelType {

    API_CHANNEL_TYPE_TOUTIAO("1"),                  //头条渠道API上报
    API_CHANNEL_TYPE_UC("2"),                  //UC渠道API上报
    API_CHANNEL_TYPE_KUAISHOU("3"),                  //快手渠道API上报
    API_CHANNEL_TYPE_BAIDU("4"),                  //百度渠道API上报
    SDK_CHANNEL_TYPE_TOUTIAO("100"),            //头条渠道SDK上报
    SDK_CHANNEL_TYPE_GDT("101");                  //广点通渠道SDK上报



    private JyPromotionChannelType(String code) {
        this.code = code;
    }

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
