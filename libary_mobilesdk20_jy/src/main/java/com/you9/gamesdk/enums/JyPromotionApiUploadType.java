package com.you9.gamesdk.enums;

/**
 * 状态码
 */
public enum JyPromotionApiUploadType {

    PROMOTION_API_UPLOAD_TYPE_ACTIVATION("0"),                          //API上报激活
    PROMOTION_API_UPLOAD_TYPE_REGISTER("1"),                            //API上报注册
    PROMOTION_API_UPLOAD_TYPE_PAY("2"),                                 //API上报支付
    PROMOTION_API_UPLOAD_TYPE_RETAIN("3"),                              //API上报留存
    PROMOTION_API_UPLOAD_TYPE_LOGIN("50"),                              //API上报登录
    PROMOTION_API_UPLOAD_TYPE_UPDATE_LEVEL("51"),                       //API上报角色升级
    PROMOTION_API_UPLOAD_TYPE_CREATE_ROLE("52"),                        //API上报创建角色
    PROMOTION_API_UPLOAD_TYPE_ONRESUME("53"),                           //API上报onResume
    PROMOTION_API_UPLOAD_TYPE_ONPAUSE("54"),                           //API上报onPause
    PROMOTION_API_UPLOAD_TYPE_INIT("55");                              //API上报初始化


    private JyPromotionApiUploadType(String code) {
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
