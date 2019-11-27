package com.you9.gamesdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.ZSDK;
import com.caozaolin.mreactfloatview.listener.FvItemClickListener;
import com.ss.android.common.applog.TeaAgent;
import com.you9.gamesdk.activity.JyLogoutActivity;
import com.you9.gamesdk.activity.JyPayTypeActivity;
import com.you9.gamesdk.activity.JySwitchAccountActivity;
import com.you9.gamesdk.activity.JyWebViewActivity;
import com.you9.gamesdk.bean.DaoMaster;
import com.you9.gamesdk.bean.DaoSession;
import com.you9.gamesdk.bean.JyPaymentInfo;
import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.bean.JyYou9;
import com.you9.gamesdk.bean.JyYou9Dao;
import com.you9.gamesdk.dialog.JyLoginDialog;
import com.you9.gamesdk.enums.JyPromotionApiUploadType;
import com.you9.gamesdk.enums.JyStateCode;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyPermissionListener;
import com.you9.gamesdk.open.JyCallbackListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;

import org.greenrobot.greendao.database.Database;

import java.util.HashMap;

public class JyPlatform {
    private static JyPlatform mInstance;
    private static JyPlatformSettings mJyPlatformSettings;
    private JySdkConfigInfo sdkInfo;
    private static Context mContext;
    private static HashMap<String, String> params;
    public static JyCallbackListener mListener;
    private static DaoSession daoSession;
    private JyYou9Dao you9Dao;
    private JyYou9 jyYou9;


    public static synchronized JyPlatform getInstance(Context context, JyCallbackListener listener) {
        synchronized (JyPlatform.class) {
            if (mInstance == null) {
                mInstance = new JyPlatform();
                mContext = context;
                params = new HashMap<String, String>();
                mJyPlatformSettings = JyPlatformSettings.getInstance();
                mListener = listener;
                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "mc-db");
                Database db = helper.getWritableDb();
                daoSession = new DaoMaster(db).newSession();
            }
        }
        return mInstance;
    }


    /**
     * sdk初始化
     */
    public void init(final JyPlatformSettings jyPlatformSettings) {



        JyUtils.getPermissions(mContext, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, new JyPermissionListener() {
            @Override
            public void onPermissionSuccess() {


                mJyPlatformSettings.setScreenOrientation(jyPlatformSettings.getScreenOrientation());
                mJyPlatformSettings.setGameType(jyPlatformSettings.getGameType());
                mJyPlatformSettings.setGameIcon(jyPlatformSettings.getGameIcon());



                sdkInfo = JyUtils.getSdkConfigInfo(mContext);

                new JyAppRequest(mContext, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {
                        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ACTIVATION.getCode(), "", true, "");


                        mListener.init(JyGameSdkStatusCode.INIT_SUCCESS, JySdkConfigInfo.getInstance());

//                        if (JySdkConfigInfo.getInstance().getReportType().equals("SDK")) {
//                            TeaAgent.init(TeaConfigBuilder.create(mContext)
//                                    .setAppName(JySdkConfigInfo.getInstance().getGameName())
//                                    .setChannel(JySdkConfigInfo.getInstance().getChannelId())
//                                    .setAid(JySdkConfigInfo.getInstance().getTouTiaoAppId())
//                                    .createTeaConfig());
//
//                        }else if (JySdkConfigInfo.getInstance().getReportType().equals("API")){
//                            new JyAppRequest(mContext, new JyAppRequestListener() {
//                                @Override
//                                public void onReqSuccess(Object obj) {
//
//                                }
//
//                                @Override
//                                public void onReqFailed(String errorMsg) {
//
//                                }
//                            }).touTiaoApiRequest(JyPromotionApiUploadType.TOUTIAO_API_TYPE_ACTIVATION.getCode(), "oaid");
//                        }



                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        mListener.init(JyGameSdkStatusCode.INIT_FAILED, null);

                    }
                }).initRequest();


            }

            @Override
            public void onPermissionFailed() {
                Toast.makeText(mContext, mContext.getResources().getString(ResourceUtil.getStringId(mContext, "jy_platform_toast")), Toast.LENGTH_SHORT).show();

            }
        });


    }


    /**
     * sdk登录
     */
    public void login() {
        you9Dao = JyPlatform.getDaoSession().getJyYou9Dao();
        jyYou9 = you9Dao.queryBuilder().build().unique();
        if (jyYou9 == null || !jyYou9.getJyUser().isAutoLogin()) {
            new JyLoginDialog(mContext, mListener).show();
        } else {
            autoLogin(jyYou9.getJyUser().getUsername(), jyYou9.getJyUser().getPassword());
        }


    }

    /**
     * sdk支付
     */
    public void pay(final JyPaymentInfo jyPaymentInfo) {

        jyPaymentInfo.setKfTel(JySdkConfigInfo.getInstance().getKfTel());

        Intent intent = new Intent(mContext, JyPayTypeActivity.class).putExtra("jyPaymentInfo", jyPaymentInfo);
        ((Activity) mContext).startActivityForResult(intent, JyConstants.PAY_RES_CODE);

//        String orderId = JyUtils.getOrderId_20();

//        if (JyUtils.isAppAvilible(mContext, JyConstants.PACKAGE_NAME_WX, JyConstants.WXPay_JY_VERSION_CODE)) {
//            //判断是否安装了久游支付控件以及是否是最新版本
//            if (JyUtils.isAppAvilible(mContext, JyConstants.PACKAGE_NAME_WXPAY_JY, JyConstants.WXPay_JY_VERSION_CODE)) {
//
//                Intent intent = new Intent(mContext, JyPayTypeActivity.class).putExtra("jyPaymentInfo", jyPaymentInfo);
//                ((Activity) mContext).startActivityForResult(intent, JyConstants.PAY_RES_CODE);
//
//
//            } else {
//                Toast.makeText(mContext, mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_install_wxpay_jy_tips")),
//                        Toast.LENGTH_SHORT).show();
////                        JyUtils.copyAssets(JyPayTypeActivity.this, JyConstants.WXPAY_JY_NAME);
//                JyUtils.getPermission(mContext);
//            }
//
//
//        } else {
//            Toast.makeText(mContext, mContext.getString(ResourceUtil.getStringId(mContext, "jy_activity_install_wx_tips")), Toast.LENGTH_SHORT).show();
//        }

    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        Log.d("eeeee", "requestCode=" + requestCode);

        if (requestCode == JyConstants.READ_CONTACTS_REQUEST) {
            Log.d("eeeee", "grantResults[0]=" + grantResults[0]);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                JyUtils.copyAssets(mContext, JyConstants.WXPAY_JY_NAME);
            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "请同意相关权限后再进行安装久游插件", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * sdk切换帐号
     *
     * @param isSwitchAccount 是否切换帐号  true --切换帐号  false --注销帐号
     */
    public void logout(boolean isSwitchAccount) {
        JyUtils.saveUserInfo(false, true);
        if (isSwitchAccount) {
            mContext.startActivity(new Intent(mContext, JySwitchAccountActivity.class));
        } else {
            mContext.startActivity(new Intent(mContext, JyLogoutActivity.class));

        }


    }


    /**
     * 悬浮球
     */
    public void floatWindow() {

        ZSDK.getInstance().init((Activity) mContext, new FvItemClickListener() {
            @Override
            public void fvLogout() {
                logout(false);


            }

            @Override
            public void fvSwitchAccount() {
                logout(true);
            }

            @Override
            public void fvYlh() {
                mContext.startActivity(new Intent(mContext, JyWebViewActivity.class).putExtra("gnType", JyStateCode.GN_TYPE_YLH.getCode()));

            }

            @Override
            public void fvAutoLogin() {
            }
        });


    }

    public void dataUpload(int dataType, String param) {

        if (dataType == JyGameSdkStatusCode.GAME_UPDATE_LEVEL) {
            JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_CREATE_ROLE.getCode(), "", false, param);
            mListener.dataUpload(JyGameSdkStatusCode.DATA_UPLOAD_SUCCESS);

        } else {
            new JyAppRequest(mContext, new JyAppRequestListener() {
                @Override
                public void onReqSuccess(Object obj) {
                    mListener.dataUpload(JyGameSdkStatusCode.DATA_UPLOAD_SUCCESS);

                }

                @Override
                public void onReqFailed(String errorMsg) {
                    mListener.dataUpload(JyGameSdkStatusCode.DATA_UPLOAD_FAILED);

                }
            }).dataUploadRequest(dataType, param);

        }


    }

    public void touTiaoRegisterDataUpload(boolean isSuccess) {
        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_REGISTER.getCode(), "username", isSuccess, "");


    }

    /**
     * 自动登录
     */

    public void autoLogin(String userName, String password) {
        new JyAppRequest(mContext, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyUtils.saveUserInfo(true, true);

                mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());


                //判别防沉迷
//                Log.d("eeeee", "fcmType=" + JyUser.getInstance().getFcm());
//                if (JyUser.getInstance().getFcm().equals(JyConstants.IS_NOT_FCM)
//                        && !JyUser.getInstance().getUsername().matches(JyConstants.TELPHONE_REGRXP)) {
//                    //弹防沉迷框
//                    JyCertificationDialog dialog = new JyCertificationDialog(mContext, mListener);
//                    dialog.show();
//
//                } else {
//                    mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
//                }

            }

            @Override
            public void onReqFailed(String errorMsg) {
                mListener.login(JyGameSdkStatusCode.LOGIN_FAILED, null);

            }
        }).loginRequest(userName, password);
    }

    public static DaoSession getDaoSession() {

        return daoSession;
    }

    /**
     * 处理返回Activity状态码的情况
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("eeeee", "JyPlatformRequestCode=" + requestCode);
        switch (requestCode) {
            case JyConstants.FLOAT_WINDOWS_RES_CODE:
                ZSDK.getInstance().onActivityResult(requestCode, resultCode, data);
                break;

            case JyConstants.PAY_RES_CODE:
                Log.d("eeeee", "PAY_RES_CODE");

                if (data != null) {
                    Log.d("eeeee", "data!=null");
                    Bundle bundle = new Bundle();
                    bundle = data.getBundleExtra("payResult");
                    String payType = bundle.getString("payType");
                    String payStauts = bundle.getString("resCode");
                    Log.d("eeeee", "payType=" + payType + "=payStatus=" + payStauts);
                    if (payType.equals(JyConstants.PAY_TYPE_ZFB)) {
                        //支付宝支付结果
                        if (payStauts.equals("9000")) {
                            //支付宝支付成功
                            Log.d("eeeee", "payStauts=" + payStauts);
                            mListener.pay(JyGameSdkStatusCode.PAY_SUCCESS);
                            Log.d("eeeee", "ZFBPAY_SUCCESS=");
                        } else {
//                            支付宝支付失败
                            Log.d("eeeee", "payStauts=" + payStauts);
                            mListener.pay(JyGameSdkStatusCode.PAY_FAILED);
                            Log.d("eeeee", "ZFBPAY_Failed");
                        }
                    } else if (payType.equals(JyConstants.PAY_TYPE_WX) || payType.equals((JyConstants.PAY_TYPE_H5_WX))) {
                        //支付宝支付结果
                        Log.d("eeeee", "WXpaypayType=" + "=payStatus=" + payStauts);
                        if (payStauts.equals("0")) {
//                            微信支付陈功
                            Log.d("eeeee", "WWWWXpayStauts=" + payStauts);
                            mListener.pay(JyGameSdkStatusCode.PAY_SUCCESS);
                            Log.d("eeeee", "WXBPAY_SUCCESS=");
                        } else {
                            Log.d("eeeee", "ElseWWWWXpayStauts=" + payStauts);
                            mListener.pay(JyGameSdkStatusCode.PAY_FAILED);
                            Log.d("eeeee", "WXPAY_FAILEDS=");
                        }
                    } else if (payType.equals(JyConstants.PAY_TYPE_CANCEL)) {
                        Log.d("eeeee", "CancelPay=" + payType);
                        mListener.pay(JyGameSdkStatusCode.PAY_CANCEL);
                        Log.d("eeeee", "CancelPayComplete=");
                    }
                }


                break;

        }
    }

    public void accountRecord(final String userId, final String userName) {

        new JyAppRequest(mContext, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyUser.getInstance().setLogin(false);
                JyUser.getInstance().setUserID(userId);
                JyUser.getInstance().setUsername(userName);
                if (JySdkConfigInfo.getInstance().getReportType().equals("SDK")){
                    TeaAgent.setUserUniqueID(JyUtils.MD5Encode(userName));
                }

                mListener.accountRecord(JyGameSdkStatusCode.ACCOUNT_RECORD_SUCCESS);

            }

            @Override
            public void onReqFailed(String errorMsg) {
                mListener.accountRecord(JyGameSdkStatusCode.ACCOUNT_RECORD_FAILED);

            }
        }).accountRecordRequest(userId);
    }


    public void onPause() {
        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONPAUSE.getCode(), "", false, "");
        ZSDK.getInstance().onPause();


    }

    public void OnResume() {
        Log.d("eeeee", "onResume");
        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONRESUME.getCode(), "", false, "");

        ZSDK.getInstance().onResume();
    }


}

