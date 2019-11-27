package com.you9.gamesdk.enums;

/**
 * 状态码
 */
public enum JyStateCode {

    _SUCC("0"),                                     // 初始化接口成功
    _PAY_RESULT_SUCC("1"),                          //微信订单支付结果查询返回成功
    SUCC_RAW_INIT_SDKINSTALL("SUCCESS"),            //初始化sdkInstalljie接口成功
    SUCC("00"),                                     //返回json数据成功
    BIND_SUCC("10"),                                //记录帐号接口返回成功
    TEL_CAN_REGISTER_QUERY("1"),                    //手机号注册查询接口可以进行注册
    GN_TYPE_KF("01"),                               //跳转客服页面
    GN_TYPE_ZX("02"),                               //跳转注销帐号页面
    GN_TYPE_FORGET_PASSWORD("03"),                  //跳转忘记密码页面
    GN_TYPE_PROTOCOL("04"),                         //跳转久游用户协议书页面
    GN_TYPE_YLH("05"),                              //跳转游乐会页面
    _SUCC_HISTORY_ORDER("3");                       //historyOrder获取成功


    private JyStateCode(String code) {
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
