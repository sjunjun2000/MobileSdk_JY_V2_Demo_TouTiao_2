package com.you9.gamesdk.enums;

/**
 * 状态码
 */
public enum JyPayTypeCode {

    PAY_TYPE_ZFB("zfb9u"),                                  //支付宝支付
    PAY_TYPE_WX_GF("wxgf"),                                 //微信官方支付
    PAY_TYPE_WX_SZF("szfwx"),                               //神州付微信支付
    PAY_TYPE_NAME_ZFB("支付宝"),                             //支付类型名  支付宝
    PAY_YTPE_NAME_WX("微信");                                //支付类型名  微信


    private JyPayTypeCode(String code) {
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
