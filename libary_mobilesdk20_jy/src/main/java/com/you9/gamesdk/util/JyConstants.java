package com.you9.gamesdk.util;

import java.io.File;

public class JyConstants {
    public static final String SDK_INFO_PATH = ("jygamesdk" + File.separator + "config" + File.separator + "sdk_info");
    public static final String REQUEST_URL = "http://sdk.9you.net/api/";
    public static final String INIT_SDKINSTALL_REQUEST_URL = "http://click.9you.com/app/";

//    public static String LOGIN_DATA_SAVE_URL = "http://sdk.9you.net/api/";
    public static final String INIT = "0001";
    public static final String CREATE_ROLE = "0002";
    public static String GAME_LOADING = "0003";
    public static String GAMECURRENCY_CONSUME = "0011";
    public static String RMB_CONSUME = "0012";
    public static String GAME_CLOSE = "0009";
    public static String GAME_LOGIN = "0014";
    public static String GAME_LOGOUT = "0015";
    /** 通行证为空 */
    public static final String USERNAME_IS_EMPTY = "请输入用户名";
    /** 通行证正则 */
    public static final String USERNAME_REGRXP = "^[a-zA-Z0-9]{6,12}$";
    /** 通行证格式错误 */
    public static final String USERNAME_FORMERR = "用户名为6-12位字母和数字，首位不能为数字";
    /** 密码为空 */
    public static final String PWD_IS_EMPTY = "请输入密码";
    /** 登录密码正则 */
    public static final String PWD_REGRXP_LOGIN = "[^\\s]{6,20}";
    /** 登录ID */
    public static final String SSO_LOGIN_ID = "SSO_PAY";
    /** 登录KEY */
    public static final String SSO_LOGIN_KEY = "GZX67BF3Nsl6lgb9H";

    public static final String SSO_REQUEST_KEY = "JSDHhdfamxo1fa*fbapsfg%dncadfFY6";

    /** 默认IP */
    public static final String DEFAULT_LOC_ADDR = "10.0.0.1";
    /** 登录URL */
    public static final String SSO_LOGIN_URL = "https://login.passport.9you.com/";
    /** 登录成功Tips */
    public static final String LOGIN_SUCCESS_TIPS = "登录成功";
    public static final int TASK_LOGIN = 1;
    public static final int TASK_REGISTER = 2;
    /** 帐号注销WebView_Url */
    public static final String WEBVIEW_ZX_URL = "https://passport.9you.com/security.php";
    /** 支付类型Url */
    public static String GET_PAYTYPE_URL = "http://pay.9you.net/api/";
    /** 忘记密码WebView_Url */
    public static final String WEBVIEW_FORGET_PASSWORD_URL = "https://passport.9you.com/m/getpasswd.php";
    /** 用户协议书WebView_Path */
    public static final String WEBVIEW_JY_PROTOCOL_PATH = "file:///android_asset/agreement.html";
    /** 手机号码正则 */
    public static final String TELPHONE_REGRXP = "[0-9]{11}";
    /** 密码正则 */
    public static final String PASSWORD_REGRXP = "[^\\s]{6,20}";
    /** 身份证号正则 */
    public static final String ID_CARD_REGEX = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65|71|81|82|91)\\d{4})((((19|20)(([02468][048])|([13579][26]))0229))|((20[0-9][0-9])|(19[0-9][0-9]))((((0[1-9])|(1[0-2]))((0[1-9])|(1\\d)|(2[0-8])))|((((0[1,3-9])|(1[0-2]))(29|30))|(((0[13578])|(1[02]))31))))((\\d{3}(x|X))|(\\d{4}))";
    /** 手机号码格式错误 */
    public static final String TELPHONE_FORMERR = "手机号码为11位纯数字";
    /** 密码格式正则 */
    public static final String PWD_REGRXP = "^(?=.*\\d)(?=.*[a-z])[a-zA-Z\\d]{6,20}$";
    /** 密码格式错误 */
    public static final String PWD_FORMERR = "密码为6-20位包含字母和数字的字符(不含空格)，不和通行证相同";
    /** 真实姓名未填写 */
    public static final String REALNAME_FORMERR = "请填写真实姓名";
    /** 身份证格式错误 */
    public static final String ID_NUMBER_FORMERR = "身份证号格式不正确，请正确填写身份证号";
    /** 手机号码为空 */
    public static final String TELPHONE_IS_EMPTY = "请输入手机号";
    /** 查询用户名是否可用URL */
    public static final String REG_QUERY_URL = "http://register.9you.com/";
    /** SSO URL */
    public static final String SSO_REQUEST_URL = "http://sdk.9you.net/user/";
    /** 返回数据格式（原生数据） */
    public static final int RETURN_DATA_FORMAT_RAW = 0;
    /** 返回数据格式（JSON） */
    public static final int RETURN_DATA_FORMAT_JSON = 1;

    /** JyPayTypeActivity  startActivityForResult的resultCode */
    public static final int PAY_RES_CODE = 100;
//    /** JyPayTypeActivity  startActivityForResult的resultCode */
//    public static final int PAY_WXGF_RES_CODE = 101;
//    /** JyPayTypeActivity  startActivityForResult的resultCode */
//    public static final int PAY_ZFB_RES_CODE = 102;
//    /** JyPayTypeActivity  startActivityForResult的resultCode */
//    public static final int PAY_WX_H5_RES_CODE = 103;
//    /** JyPayTypeActivity  startActivityForResult的resultCode */
//    public static final int PAY_WX_RES_CODE = 104;
    /** JyTelRegisterActivity  startActivityForResult的resultCode */
    public static final int REGISTER_TEL_RES_CODE = 105;
    /** JyPayTypeActivity  startActivityForResult的resultCode */
    public static final int FLOAT_WINDOWS_RES_CODE = 1;

    /** 创建订单URL */
    public static final String CREATE_ORDER_URL = "http://pay.9you.net/order/";
    public static final String PAY_TYPE_ZFB = "zfb9u";
    public static final String PAY_TYPE_H5_WX = "szfwx";
    public static final String PAY_TYPE_WX = "wxgf";
    public static final String PAY_TYPE_CANCEL = "cancelPay";
    public static final int SDK_PAY_FLAG = 1;
    /** 微信包名 */
    public static final String PACKAGE_NAME_WX = "com.tencent.mm";
    /** 微信支付控件包名 */
    public static final String PACKAGE_NAME_WXPAY_JY = "com.you9.sdk";
    /** 微信支付控件跳转Activitiy路径 */
    public static final String ACTIVITY_PATH_WXPAY_JY = "com.you9.sdk.activity.JyPayTypeActivity";
    /** 微信支付控件文件名 */
    public static final String WXPAY_JY_NAME = "PaymentPlugin_JY.apk";
    public static final int READ_CONTACTS_REQUEST = 1;
    /** 防沉迷类型 未进行实名制 */
    public static final String IS_NOT_FCM = "2";


    /** WXPay_JY_VERSION_CODE 控件版本号ID */
    public static final int WXPay_JY_VERSION_CODE = 12;

    /** 防沉迷接口参数 */
    public static final String FCM_ID = "P_PHONE";
    public static final String FCM_TYPE = "resetFcm";
    public static final String FCM_KEY = "SW9HGW07ENGLAMEU";

    /** 实名认证URL */
    public static final String CERTIFICATION_URL = "http://mobilepasspod.9you.com/";

    /** 游乐会WebView_Url */
    public static final String WEBVIEW_YLH_URL = "http://uhg.9you.com/vip/index/gameAdd.html?show=1&";

    /**
     * JyPayTypeActivity  startActivityForResult的resultCode
     */
    public static final int PAYF_RES_CODE = 100;
    /**
     * JyPayTypeActivity  startActivityForResult的resultCode
     */
    public static final int PAY_WXGF_RES_CODE = 101;
    /**
     * JyPayTypeActivity  startActivityForResult的resultCode
     */
    public static final int PAY_ZFB_RES_CODE = 102;
    /**
     * JyPayTypeActivity  startActivityForResult的resultCode
     */
    public static final int PAY_WX_H5_RES_CODE = 103;
    /**
     * JyPayTypeActivity  startActivityForResult的resultCode
     */


    public static final String PAY_CANCEL_RES_CODE = "1000";

    /**
     * 支付相关请求Url
     */
    public static String PAY_URL = "http://pay.9you.net/api/";

    /**
     * 支付相关的key
     */
    public static final String REQUEST_KEY = "JSDHhdfamxo1fa*fbapsfg%dncadfFY6";

    /**
     * 微信支付Referer
     */
    public static final String WX_PAY_REFERER = "http://9you.com";

    /**
     * 头条API上报，久游请求
     */
    public static final String TOUTIAO_API_REQUEST_URL = "http://updata.9you.com/api/receive/";




}
