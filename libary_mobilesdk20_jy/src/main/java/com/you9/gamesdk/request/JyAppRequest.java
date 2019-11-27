package com.you9.gamesdk.request;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.you9.gamesdk.bean.JyPayType;
import com.you9.gamesdk.bean.JyPaymentInfo;
import com.you9.gamesdk.bean.JyResponse;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.bean.JyYou9;
import com.you9.gamesdk.dialog.JyNetWorkDialog;
import com.you9.gamesdk.enums.JyPayTypeCode;
import com.you9.gamesdk.enums.JyPromotionApiUploadType;
import com.you9.gamesdk.enums.JyStateCode;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyBaseRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.PreferencesUtils;
import com.you9.gamesdk.util.ResourceUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class JyAppRequest extends JyBaseRequest {

    private Context mContext;
    private JyAppRequestListener mListener;
    private HashMap<String, String> params;
    private JyNetWorkDialog mJyNetWorkDialog;
    private final int TASK_LOGIN = 1;
    private final int TASK_QUERY = 2;
    private final int TASK_REGISTER = 3;
    private final int TASK_SMS = 4;
    private PreferencesUtils preferencesUtils;

    private Thread mThread;

    public JyAppRequest(Context context, JyAppRequestListener listener) {
        this.mContext = context;
        this.mListener = listener;
        params = new HashMap<String, String>();

        mJyNetWorkDialog = new JyNetWorkDialog(context);

    }


    /**
     * 初始化
     */


    public void initRequest() {
        params.put("g", JySdkConfigInfo.getInstance().getClientId());
        params.put("sim", JySdkConfigInfo.getInstance().getNetworkProvider());
        params.put("c", JySdkConfigInfo.getInstance().getChannelId());
        params.put("m", JySdkConfigInfo.getInstance().getPhoneNum());
        params.put("i", JySdkConfigInfo.getInstance().getiMei());
        params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
        params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
        params.put("o", JySdkConfigInfo.getInstance().getOs());
        params.put("gpu", JySdkConfigInfo.getInstance().getGpu());
        params.put("fm", JySdkConfigInfo.getInstance().getModel());
        params.put("cpu", JySdkConfigInfo.getInstance().getCpu());
        params.put("ghz", JySdkConfigInfo.getInstance().getHz());
        params.put("ram", JySdkConfigInfo.getInstance().getRam());
        params.put("rom", JySdkConfigInfo.getInstance().getRom());
        params.put("n", JySdkConfigInfo.getInstance().getNetworkType());
        params.put("f", JySdkConfigInfo.getInstance().getManufacturer());
        params.put("r", JySdkConfigInfo.getInstance().getRoot());
        params.put("dis", JySdkConfigInfo.getInstance().getResolvingPower());
        params.put("e1", JySdkConfigInfo.getInstance().getE1());
        params.put("e2", JySdkConfigInfo.getInstance().getE2());
        params.put("e3", JySdkConfigInfo.getInstance().getE3());
        params.put("ip", JyUtils.getLocalIp());
        params.put("e", JyConstants.INIT);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1
        Log.d("eeeee", "params=" + params.toString());
        post(mContext, params, JyConstants.REQUEST_URL, "gameLog", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {

                if (response.getDesc().equals(JyStateCode._SUCC.getCode())) {
//                    mListener.onReqSuccess(response);


                    sdkLogRequest("", "");

                } else {
                    mListener.onReqFailed(((JyResponse) response).getDesc());

                }
            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });


    }



    /**
     * 推广API上报请求久游服务器
     */
    public void promotionApiRequest(String apiType, String oaid){
        params.put("channel_id", JySdkConfigInfo.getInstance().getChannelId());
        Log.d("eeeee", "imei=" + JySdkConfigInfo.getInstance().getiMei());
        params.put("imei", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getiMei()));
        params.put("type", apiType);
        params.put("oaid", oaid);

        params.put("from", JySdkConfigInfo.getInstance().getReportType());

        get(mContext, params, JyConstants.TOUTIAO_API_REQUEST_URL, "from_sdk", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                Log.d("eeeee", "touTiaoApiRequestSuccess=" + response.toString());
            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                Log.d("eeeee", "touTiaoApiRequestError=" + errorMsg);

            }
        });

    }



    /**
     * 记录帐号
     */
    public void accountRecordRequest(String userId) {
        params.put("gameid", JySdkConfigInfo.getInstance().getClientId());
        params.put("channelid", JySdkConfigInfo.getInstance().getChannelId());
        params.put("userid", userId);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1
        params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getClientId() + JySdkConfigInfo.getInstance().getChannelId() + userId + JyConstants.SSO_REQUEST_KEY));
        post(mContext, params, JyConstants.SSO_REQUEST_URL, "bind", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.BIND_SUCC.getCode())) {
                    mListener.onReqSuccess(response);
                } else {
                    mListener.onReqFailed(response.getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);
            }
        });
    }

    /**
     * sdk日志上传
     */
    public void sdkLogRequest(final String cellphone, String uId) {
        final JyResponse[] resp = new JyResponse[1];

        mJyNetWorkDialog.setOnSuccessListener(new JyNetWorkDialog.OnSuccessListener() {
            @Override
            public void onSuccess() {

                loginDataRequest(cellphone, uId);
//                JyUtils.saveUserInfo(true, true);
                mListener.onReqSuccess(resp[0]);
            }
        });


        params.clear();
        params.put("username", cellphone);
//        params.put("uid", uId);
        params.put("idfa", JySdkConfigInfo.getInstance().getiMei());
        params.put("channel_id", JySdkConfigInfo.getInstance().getChannelId());
        params.put("app_type", "1");
        params.put("app_id", JySdkConfigInfo.getInstance().getAppId());

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        post(mContext, params, JyConstants.INIT_SDKINSTALL_REQUEST_URL, "sdkinstall.php", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                resp[0] = response;
                if (response.getDesc().equals(JyStateCode.SUCC_RAW_INIT_SDKINSTALL.getCode())) {
                    if (!TextUtils.isEmpty(cellphone)) {
//                        Intent i = new Intent(mContext, JyLoginService.class).putExtra("username", cellphone);
//                        mContext.startService(i);
//                        loginDataRequest(cellphone, uId);
                        mJyNetWorkDialog.onSuccess(cellphone + mContext.getString(ResourceUtil.getStringId(mContext, "jy_login_success")));


                    }else {
                        mListener.onReqSuccess(response);
                    }

                    if (JyUtils.isNumeric(cellphone)) {
                        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "mobile", true, JyUser.getInstance().getUsername());

                    } else if (!cellphone.equals("")){
                        Log.d("eeeee", "sdklogrequest");
                        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "username", true, JyUser.getInstance().getUsername());
                    }

                } else {
                    if (!TextUtils.isEmpty(cellphone)) {
                        mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    }
                    mListener.onReqFailed(((JyResponse) response).getDesc());
                    if (JyUtils.isNumeric(cellphone)) {
                        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "mobile", false, "");

                    } else if (!cellphone.equals("")) {
                        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "username", false, "");
                    }

                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                if (!TextUtils.isEmpty(cellphone)) {
                    mJyNetWorkDialog.onFailed(errorMsg);
                }
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    /**
     * 登录日志心跳上传
     */
    public void loginDataRequest(String cellphone, String uId) {


        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                params.put("g", JySdkConfigInfo.getInstance().getClientId());
                params.put("c", JySdkConfigInfo.getInstance().getChannelId());
                params.put("m", cellphone);
                params.put("uid", uId);
                params.put("i", JySdkConfigInfo.getInstance().getiMei());
                params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
                params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
                params.put("e1", JySdkConfigInfo.getInstance().getE1());
                params.put("e2", JySdkConfigInfo.getInstance().getE2());
                params.put("e3", JySdkConfigInfo.getInstance().getE3());
                params.put("ip", JyUtils.getLocalIp());


                params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
                params.put("iosid", "");
                params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
                params.put("extid", "");
                params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
                params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
                params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
                params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

                params.put("e", JyConstants.GAME_LOGIN);

                post(mContext, params, JyConstants.REQUEST_URL, "gameLog", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
                    @Override
                    public void onBaseRequestSuccess(JyResponse response) {
                        Log.d("eeeee", "heartBeatSuccess");
                    }

                    @Override
                    public void onBaseRequestFailed(String errorMsg) {
                        Log.d("eeeee", "heartBeatonBaseRequestFailed");

                    }
                });
                try {
                    Thread.sleep(30000);
                    loginDataRequest(cellphone, uId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

        );


        Log.d("eeeee", "loginMThread=" + mThread);
        mThread.start();
    }


    /**
     * 帐号登出
     */
    public void logoutRequest(String cellphone) {
        params.put("g", JySdkConfigInfo.getInstance().getClientId());
        params.put("c", JySdkConfigInfo.getInstance().getChannelId());
        params.put("m", cellphone);
        params.put("i", JySdkConfigInfo.getInstance().getiMei());
        params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
        params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
        params.put("e1", JySdkConfigInfo.getInstance().getE1());
        params.put("e2", JySdkConfigInfo.getInstance().getE2());
        params.put("e3", JySdkConfigInfo.getInstance().getE3());
        params.put("ip", JyUtils.getLocalIp());


        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("e", JyConstants.GAME_LOGOUT);
        post(mContext, params, JyConstants.REQUEST_URL, "gameLog", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                JyUtils.saveUserInfo(false, true);
                mListener.onReqSuccess(response);
            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                Log.d("eeeee", "LogoutRequestFailed");
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    public void checkCellphone(String cellphone) {
        params.put("type", "u");
        params.put("u", cellphone);
        mJyNetWorkDialog.setContent(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_register_verify_wait")));
        mJyNetWorkDialog.show(0);
        post(mContext, params, JyConstants.REG_QUERY_URL, "Query", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getDesc().equals(JyStateCode.TEL_CAN_REGISTER_QUERY.getCode())) {
                    mJyNetWorkDialog.dismiss();
                    mListener.onReqSuccess(response);
                } else {
                    mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    mListener.onReqFailed(((JyResponse) response).getDesc());

                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    /**
     * 登录
     */


    public void loginRequest(final String userName, final String password) {
        mJyNetWorkDialog.setContent(mContext
                .getString(ResourceUtil.getStringId(mContext,
                        "jy_dologin_dialog_login")));
//        mJyNetWorkDialog.setStyleTitle(makeTitleStyle(userName));
        mJyNetWorkDialog.show(1);

        String localIp = JyUtils.getLocalIp();
        params.put("id", JyConstants.SSO_LOGIN_ID);
        params.put("userName", userName);
        params.put("password", JyUtils.MD5Encode(password));
        params.put("userIp", localIp);

        params.put("s", JyUtils.MD5Encode(JyConstants.SSO_LOGIN_ID + userName + localIp + JyConstants.SSO_LOGIN_KEY));
        post(mContext, params, JyConstants.SSO_LOGIN_URL, "tel_u_login", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (!JyUtils.isBlankOrNull(response.getDesc())) {
                    JyUtils.getSdkLoginInfo(response.getDesc());
                    if (JyYou9.getInstance().getState().equals(JyStateCode.SUCC.getCode())) {

                        JyUser.getInstance().setLogin(true);
                        JyUser.getInstance().setPassword(password);
                        preferencesUtils = new PreferencesUtils(mContext);
                        preferencesUtils.saveAccounts(JyUser.getInstance());
                        sdkLogRequest(userName, JyUser.getInstance().getUserID());
                    } else {
                        mJyNetWorkDialog.onFailed(JyYou9.getInstance().getStateDesc());

                        mListener.onReqFailed(JyYou9.getInstance().getStateDesc());
                        if (JyUtils.isNumeric(userName)) {
                            JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "mobile", false, "");

                        } else {
                            JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_LOGIN.getCode(), "username", false, "");
                        }

//                        JyUtils.touTiaoDataUpload(JyTouTiaoDataTypeCode.TOUTIAO_DATA_TYPE_LOGIN.getCode(), "");

                    }


                } else {
                    //接口返回数据为空
                    mJyNetWorkDialog.onFailed(mContext.getString(ResourceUtil.getStringId(mContext, "jy_app_request")));
                    mListener.onReqFailed(mContext.getString(ResourceUtil.getStringId(mContext, "jy_app_request")));
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);
                mListener.onReqFailed(errorMsg);

            }
        });


    }


    /**
     * 短信验证码发送
     */

    public void smsSendRequest(String cellphone) {
        mJyNetWorkDialog.setContent(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_sms_sending")));
        mJyNetWorkDialog.show(0);

        params.put("username", cellphone);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        post(mContext, params, JyConstants.SSO_REQUEST_URL, "sendVcode", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.SUCC.getCode())) {
                    mJyNetWorkDialog.onSuccess(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_sms_verify_success")));
                    mListener.onReqSuccess(response);

//                    mJyNetWorkDialog.onSuccess(TASK_SMS, getString(ResourceUtil.getStringId(mContext, "jy_activity_sms_verify_success"));
//                    preferencesUtils.saveSMSSendTime(System.currentTimeMillis());
//                    countdown(60000);
                } else {
                    mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    mListener.onReqFailed(((JyResponse) response).getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    /**
     * 短信验证码验证
     */


    public void smsVerifyRequest(String cellphone, String vCode) {
        mJyNetWorkDialog.setContent(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_register_verify_wait")));
        mJyNetWorkDialog.show(0);
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("username", cellphone);
        params.put("vcode", vCode);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

//        JySmsVerifyActivity.this, params, JyConstants.SSO_REQUEST_URL, "checkVcode", JyConstants.RETURN_DATA_FORMAT_JSON, new JyAppRequestListener(
        post(mContext, params, JyConstants.SSO_REQUEST_URL, "checkVcode", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.SUCC.getCode())) {
                    mJyNetWorkDialog.onSuccess(mContext.getString(ResourceUtil.getStringId(mContext, "jy_register_vcode_success")));
                    mListener.onReqSuccess(response);
                } else {
                    mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    mListener.onReqFailed(((JyResponse) response).getDesc());
                }
            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);
                mListener.onReqFailed(errorMsg);

            }
        });

    }


    /**
     * 手机号注册
     */

    public void registerByTelRequest(String cellphone, String password) {
        mJyNetWorkDialog.setContent(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_register_wait")));
        mJyNetWorkDialog.show(0);
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("username", cellphone);
        params.put("password", password);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + cellphone + password + JyConstants.SSO_REQUEST_KEY).toLowerCase(Locale.getDefault()));
//        JySmsVerifyActivity.this, params, JyConstants.SSO_REQUEST_URL, "mRegist", JyConstants.RETURN_DATA_FORMAT_JSON, new JyAppRequestListener(
        post(mContext, params, JyConstants.SSO_REQUEST_URL, "mRegist", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.SUCC.getCode())) {
                    //注册成功
                    mJyNetWorkDialog.onSuccess(mContext.getString(ResourceUtil.getStringId(mContext, "jy_register_success")));
                    mListener.onReqSuccess(response);
                    JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode(), "mobile", true, "");


                } else {
                    mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    mListener.onReqFailed(((JyResponse) response).getDesc());
                    JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode(), "mobile", false, "");
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    /**
     * 用户名注册
     */
    public void registerByUnameRequest(String userName, String password, String realName, String idNumber) {

        mJyNetWorkDialog.setContent(mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_register_wait")));

        mJyNetWorkDialog.setOnSuccessListener(new JyNetWorkDialog.OnSuccessListener() {
            @Override
            public void onSuccess() {
                Log.d("eeeee", "registerByUnameRequestONSuccess");
                mListener.onReqSuccess(null);
                JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode(), "username", true, "");


            }
        });


        mJyNetWorkDialog.show(0);
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("username", userName);
        params.put("password", password);
        params.put("realname", realName);
        params.put("idnumber", idNumber);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + userName + password + JyConstants.SSO_REQUEST_KEY).toLowerCase(Locale.getDefault()));
        post(mContext, params, JyConstants.SSO_REQUEST_URL, "regist", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.SUCC.getCode())) {

                    mJyNetWorkDialog.onSuccess(mContext.getString(ResourceUtil.getStringId(mContext, "jy_register_success")));
//                    mListener.onReqSuccess(response);


                } else {
                    mListener.onReqFailed(((JyResponse) response).getDesc());
                    mJyNetWorkDialog.onFailed(((JyResponse) response).getDesc());
                    JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode(), "username", false, "");

                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mJyNetWorkDialog.onFailed(errorMsg);

            }
        });
    }


    /**
     * 获取支付类型
     */

    public void getPayTypeRequest(JyPaymentInfo jyPaymentInfo) {
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("server", jyPaymentInfo.getServer());
        params.put("price", jyPaymentInfo.getPrice() + "");


        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + jyPaymentInfo.getServer() + jyPaymentInfo.getPrice() + "" + JyConstants.REQUEST_KEY));
        post(mContext, params, JyConstants.PAY_URL, "getPayType", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode._SUCC.getCode())) {
                    List<JyPayType> payTypeList = JSON.parseArray(response.getDesc(), JyPayType.class);
                    mListener.onReqSuccess(payTypeList);

                } else {
                    mListener.onReqFailed(response.getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });

    }

    /**
     * 创建订单
     */
    public void createOrderRequest(JyPaymentInfo jyPaymentInfo) {
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("server", jyPaymentInfo.getServer());
        params.put("order_id", jyPaymentInfo.getOrderId());
        params.put("imei", JySdkConfigInfo.getInstance().getiMei());
        params.put("iccid", JySdkConfigInfo.getInstance().getIccid());

        params.put("pay_type", jyPaymentInfo.getPayType());
        params.put("subject", jyPaymentInfo.getSubject());
        params.put("body", jyPaymentInfo.getBody());
        params.put("price", jyPaymentInfo.getPrice() + "");
        params.put("e1", JySdkConfigInfo.getInstance().getE1());
        params.put("e2", JySdkConfigInfo.getInstance().getE2());
        params.put("e3", JySdkConfigInfo.getInstance().getE3());

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        if (jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode())) {
            params.put("web", "1");
        }

        if (JyUser.getInstance().isLogin()) {
            params.put("uid", JyUser.getInstance().getUsername());
            params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + jyPaymentInfo.getServer() + jyPaymentInfo.getOrderId() + JySdkConfigInfo.getInstance().getiMei() + JySdkConfigInfo.getInstance().getIccid() + JyUser.getInstance().getUsername() + jyPaymentInfo.getPayType() + jyPaymentInfo.getSubject() + jyPaymentInfo.getBody() + jyPaymentInfo.getPrice() + JyConstants.REQUEST_KEY));
        } else {
            params.put("uid", JyUser.getInstance().getUserID());
            params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + jyPaymentInfo.getServer() + jyPaymentInfo.getOrderId() + JySdkConfigInfo.getInstance().getiMei() + JySdkConfigInfo.getInstance().getIccid() + JyUser.getInstance().getUserID() + jyPaymentInfo.getPayType() + jyPaymentInfo.getSubject() + jyPaymentInfo.getBody() + jyPaymentInfo.getPrice() + JyConstants.REQUEST_KEY));
        }



        post(mContext, params, JyConstants.CREATE_ORDER_URL, "neworder", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode._SUCC.getCode()) || response.getState().equals(JyStateCode._SUCC_HISTORY_ORDER.getCode())) {

                    mListener.onReqSuccess(response);

                } else {
                    mListener.onReqFailed(response.getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });


    }

    /**
     * 防沉迷信息提交
     */
    public void fcmInfoRequest(String realName, String idNumber) {
//        JyUtils.checkUsername(JyUser.getInstance().getUsername());
        params.put("clientId", JySdkConfigInfo.getInstance().getClientId());
        params.put("type", JyConstants.FCM_TYPE);
        params.put("username", JyUser.getInstance().getUsername());
        params.put("userId", JyUser.getInstance().getUserID());
        params.put("idCard", idNumber);
        params.put("realName", URLEncoder.encode(realName));
        params.put("ip", JyUtils.getLocalIp());

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("s", JyUtils.MD5Encode(JyConstants.FCM_ID + JyUser.getInstance().getUsername() + JyConstants.FCM_KEY));
        post(mContext, params, JyConstants.CERTIFICATION_URL, "fillFcmInfo", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode.SUCC.getCode())) {
                    mListener.onReqSuccess(response);
                } else {
                    mListener.onReqFailed(response.getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });


    }

    /**
     * 游戏数据上传
     */
    public void dataUploadRequest(int dataType, String price) {
        params.put("g", JySdkConfigInfo.getInstance().getClientId());
        params.put("c", JySdkConfigInfo.getInstance().getChannelId());
        params.put("m", JyUser.getInstance().getUsername());
        params.put("o", JySdkConfigInfo.getInstance().getOs());
        params.put("i", JySdkConfigInfo.getInstance().getiMei());
        params.put("n", JySdkConfigInfo.getInstance().getNetworkType());
        params.put("e1", JySdkConfigInfo.getInstance().getE1());
        params.put("e2", JySdkConfigInfo.getInstance().getE2());
        params.put("e3", JySdkConfigInfo.getInstance().getE3());
        params.put("ip", JyUtils.getLocalIp());

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        switch (dataType) {
            case JyGameSdkStatusCode.GAME_CREATE_ROLE:
                //头条数据收集
                JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_CREATE_ROLE.getCode(), "create_gamerole", false, price);
                params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
                params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
                params.put("roleid", price);
                params.put("e", JyConstants.CREATE_ROLE);

                break;

            case JyGameSdkStatusCode.GAME_LOADING:
                params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
                params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
                params.put("e", JyConstants.GAME_LOADING);

                break;

            case JyGameSdkStatusCode.GAME_CURRENCY_CONSUME:
                params.put("v", price);
                params.put("e", JyConstants.GAMECURRENCY_CONSUME);

                break;

            case JyGameSdkStatusCode.GAME_RMB_CONSUME:
                params.put("v", price);
                params.put("e", JyConstants.RMB_CONSUME);

                break;

            case JyGameSdkStatusCode.GAME_CLOSE:
                params.put("imsi", JySdkConfigInfo.getInstance().getImsi());
                params.put("iccid", JySdkConfigInfo.getInstance().getIccid());
                params.put("e", JyConstants.GAME_CLOSE);

                break;

        }

        post(mContext, params, JyConstants.REQUEST_URL, "gameLog", JyConstants.RETURN_DATA_FORMAT_RAW, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getDesc().equals(JyStateCode._SUCC.getCode())) {
                    mListener.onReqSuccess(response);

                } else {
                    mListener.onReqFailed(response.getDesc());

                }


            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });

    }


    /**
     * 获取历史充值记录
     */
    public void getHistoryOrderRequest(int offset) {
        params.put("channel", JySdkConfigInfo.getInstance().getChannelId());
        params.put("offset", offset + "");
        params.put("game", JySdkConfigInfo.getInstance().getClientId());
        params.put("order_status", "");
        params.put("imei", JySdkConfigInfo.getInstance().getiMei());
//        params.put("uid", JyUser.getInstance().getUserID());

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        if (JyUser.getInstance().isLogin()) {
            params.put("uid", JyUser.getInstance().getUsername());
            params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + JySdkConfigInfo.getInstance().getiMei() + JyUser.getInstance().getUsername() + "" + JyConstants.REQUEST_KEY));
        } else {
            params.put("uid", JyUser.getInstance().getUserID());
            params.put("s", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getChannelId() + JySdkConfigInfo.getInstance().getClientId() + JySdkConfigInfo.getInstance().getiMei() + JyUser.getInstance().getUserID() + "" + JyConstants.REQUEST_KEY));
        }


        post(mContext, params, JyConstants.PAY_URL, "queryOrder", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode._SUCC.getCode())) {
//                     List<Order> orderList = JSON.parseArray(response.getDesc(), Order.class);
                    mListener.onReqSuccess(response);

                } else {
                    mListener.onReqFailed(response.getDesc());
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });
    }

    /**
     * 微信订单支付结果查询
     */
    public void wxPayResultRequest(String orderId) {
        params.put("orderId", orderId);

        params.put("androidid", JySdkConfigInfo.getInstance().getAndroidId());
        params.put("iosid", "");
        params.put("ua", JySdkConfigInfo.getInstance().getUserAgent());
        params.put("extid", "");
        params.put("eid1", JySdkConfigInfo.getInstance().getUuid());
        params.put("eid2", JySdkConfigInfo.getInstance().getOpenUdid());
        params.put("eid3", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac().replace(":", "")));     //MAC
        params.put("eid4", JyUtils.MD5Encode(JySdkConfigInfo.getInstance().getMac()));     //MAC1

        params.put("s", JyUtils.MD5Encode(orderId + JyConstants.REQUEST_KEY));
        post(mContext, params, JyConstants.PAY_URL, "queryOneOrder", JyConstants.RETURN_DATA_FORMAT_JSON, new JyBaseRequestListener() {
            @Override
            public void onBaseRequestSuccess(JyResponse response) {
                if (response.getState().equals(JyStateCode._PAY_RESULT_SUCC.getCode())) {
                    mListener.onReqSuccess(response);
                } else {
                    mListener.onReqFailed("");
                }

            }

            @Override
            public void onBaseRequestFailed(String errorMsg) {
                mListener.onReqFailed(errorMsg);

            }
        });
    }


    /**
     * 高亮用户名
     *
     * @param account 用户名
     * @return
     */
    private SpannableStringBuilder makeTitleStyle(String account) {
        String prefix = mContext.getString(ResourceUtil.getStringId(mContext, "jy_dologin_dialog_9you_user"));
        String suffix = mContext.getString(ResourceUtil.getStringId(mContext, "jy_dologin_dialog_welcome"));
        SpannableStringBuilder style = new SpannableStringBuilder(prefix
                + account + suffix);
        style.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(
                        ResourceUtil.getColorId(mContext,
                                "jy_color_9you_login_blue"))), prefix.length(),
                prefix.length() + account.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return style;
    }


}
