package com.you9.mobilesdk_jy_v2_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.JySettingsCode;
import com.you9.gamesdk.bean.JyPaymentInfo;
import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.open.JyCallbackListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button bInit;
    private Button bLogin;
    private Button bPay;
    private Button bLogout;
    private Button bSwitchAccount;
    private Button bFloatView;
    private Button bCreateRole;
    private Button bGameLoading;
    private Button bGameCurrency;
    private Button bRmbConsume;
    private Button bUpdateLevel;
    private Button bGameClose;
    private Button bAutoLogin;
    private Button baccountRecord;

    private JyPlatform mJyPlatform;
    private JyCallbackListener mListener = new JyCallbackListener() {
        @Override
        public void init(int statusCode, JySdkConfigInfo jySdkConfigInfo) {

            switch (statusCode) {
                case JyGameSdkStatusCode.INIT_SUCCESS:
                    Log.d("eeeee", "初始化成功");
                    Log.d("eeeee", "e1=" + jySdkConfigInfo.getE1());
                    Log.d("eeeee", "e2=" + jySdkConfigInfo.getE2());
                    Log.d("eeeee", "e3=" + jySdkConfigInfo.getE3());
                    break;

                case JyGameSdkStatusCode.INIT_FAILED:
                    Log.d("eeeee", "初始化失败");
                    break;
            }

        }

        @Override
        public void login(int statusCode, JyUser jyUser) {
            switch (statusCode) {
                case JyGameSdkStatusCode.LOGIN_SUCCESS:
                    Log.d("eeeee", "登录成功");
                    Log.d("eeeee", "uname=====" + jyUser.getUsername());
//                    服务端进行登录票据验证，详细接口请内容请参考 久游sdk登录服务端验证接口.txt

                    break;

                case JyGameSdkStatusCode.LOGIN_FAILED:
                    Log.d("eeeee", "登录失败");
                    break;
            }

        }

        @Override
        public void register(int statusCode, JyRegisterInfo jyRegisterInfo) {
            switch (statusCode) {
                case JyGameSdkStatusCode.REGISTER_SUCCESS:
                    Log.d("eeeee", "注册成功");
                    //注册成功后有2种登录方式可供选择，1.自动登录  2.登录框登录

                    //1.自动登录  登录成功后回调login
                    mJyPlatform.autoLogin(jyRegisterInfo.getUserName(), jyRegisterInfo.getPassword());

                    //2.登录框登录
//                    mJyPlatform.login();

                    break;
                case JyGameSdkStatusCode.REGISTER_CANCEL:
                    Log.d("eeeee", "取消注册");

                    break;
            }

        }

        @Override
        public void pay(int statusCode) {
            switch (statusCode) {
                case JyGameSdkStatusCode.PAY_CANCEL:
                    Log.d("eeeee", "取消支付");
                    break;

                case JyGameSdkStatusCode.PAY_SUCCESS:
                    Log.d("eeeee", "支付成功");
                    break;

                case JyGameSdkStatusCode.PAY_FAILED:
                    Log.d("eeeee", "支付失败");
                    break;
            }

        }

        @Override
        public void logout(int statusCode, boolean isSwitchAccount) {
            if (isSwitchAccount) {
//                切换帐号
                switch (statusCode) {
                    case JyGameSdkStatusCode.LOGOUT_SUCCESS:
                        Log.d("eeeee", "切换帐号成功");

//                        调用login方法
                        mJyPlatform.login();

                        break;

                    case JyGameSdkStatusCode.LOGOUT_FAILED:
                        Log.d("eeeee", "切换帐号失败");

                        break;
                }
            } else {
//                注销
                switch (statusCode) {
                    case JyGameSdkStatusCode.LOGOUT_SUCCESS:
                        Log.d("eeeee", "注销帐号成功");

                        break;

                    case JyGameSdkStatusCode.LOGOUT_FAILED:
                        Log.d("eeeee", "注销帐号失败");

                        break;

                }
            }

        }

        @Override
        public void dataUpload(int statusCode) {
            switch (statusCode) {
                case JyGameSdkStatusCode.DATA_UPLOAD_SUCCESS:
                    Log.d("eeeee", "游戏数据上传成功");
                    break;

                case JyGameSdkStatusCode.DATA_UPLOAD_FAILED:
                    Log.d("eeeee", "游戏数据上传失败");

                    break;
            }
        }

        @Override
        public void accountRecord(int statusCode) {
            switch (statusCode) {
                case JyGameSdkStatusCode.ACCOUNT_RECORD_SUCCESS:
                    Log.d("eeeee", "帐号记录成功");
                    break;

                case JyGameSdkStatusCode.ACCOUNT_RECORD_FAILED:
                    Log.d("eeeee", "帐号记录失败");
                    break;
            }

        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bInit = (Button) findViewById(R.id.bInit);
        bLogin = (Button) findViewById(R.id.bLogin);
        bPay = (Button) findViewById(R.id.bPay);
        bLogout = (Button) findViewById(R.id.bLogout);
        bSwitchAccount = (Button) findViewById(R.id.bSwitchAccount);
        bFloatView = (Button) findViewById(R.id.bFloatView);
        bCreateRole = (Button) findViewById(R.id.bCreateRole);
        bGameLoading = (Button) findViewById(R.id.bGameLoading);
        bGameCurrency = (Button) findViewById(R.id.bGameCurrency);
        bRmbConsume = (Button) findViewById(R.id.bRmbConsume);
        bUpdateLevel = (Button) findViewById(R.id.bUpdateLevel);
        bGameClose = (Button) findViewById(R.id.bGameClose);
        bAutoLogin = (Button) findViewById(R.id.bAutoLogin);
        baccountRecord = (Button) findViewById(R.id.baccountRecord);

        bInit.setOnClickListener(this);
        bLogin.setOnClickListener(this);


        bPay.setOnClickListener(this);
        bLogout.setOnClickListener(this);
        bSwitchAccount.setOnClickListener(this);
        bFloatView.setOnClickListener(this);
        bCreateRole.setOnClickListener(this);
        bGameLoading.setOnClickListener(this);
        bGameCurrency.setOnClickListener(this);
        bRmbConsume.setOnClickListener(this);
        bUpdateLevel.setOnClickListener(this);
        bGameClose.setOnClickListener(this);
        bAutoLogin.setOnClickListener(this);
        baccountRecord.setOnClickListener(this);
        mJyPlatform = JyPlatform.getInstance(MainActivity.this, mListener);

        


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bInit:
                JyPlatformSettings mJyPlatformSettings = new JyPlatformSettings();
                mJyPlatformSettings.setScreenOrientation(JySettingsCode.SCREEN_ORIENTATION_PORTRAIT);       //游戏横竖屏设置
                mJyPlatformSettings.setGameType(JySettingsCode.ONLINE_GAME);                                //游戏性质设置
                mJyPlatformSettings.setGameIcon(getResources().getDrawable(R.drawable.ic_launcher));        //游戏icon
//                mJyPlatformSettings.setGameIcon(getResources().getDrawable(ResourceUtil.getDrawableId(this, "ic_launcher")));
                mJyPlatform.init(mJyPlatformSettings);                                                      //sdk初始化

                break;

            case R.id.bLogin:
                mJyPlatform.login();

                break;
            case R.id.bPay:
                JyPaymentInfo paymentInfo = new JyPaymentInfo();
                paymentInfo.setBody("1111");
                paymentInfo.setPrice(1);
                paymentInfo.setSubject("钻石");
                paymentInfo.setServer("99");
                mJyPlatform.pay(paymentInfo);
                break;

            case R.id.bLogout:
//                注销 传false. sdk会回调 JyCallbackListener 的logout方法,回调参数为false.等待收到回调后再退出游戏场景即可
                mJyPlatform.logout(false);
                break;

            case R.id.bSwitchAccount:
                // 切换帐号 传true. sdk会回调 JyCallbackListener 的logout方法,回调参数为true.等待收到回调后再退出游戏场景, 然后再重新调用login
                mJyPlatform.logout(true);

                break;

            case R.id.bFloatView:
                mJyPlatform.floatWindow();

                break;

            case R.id.bCreateRole:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_CREATE_ROLE, "123124"); //param为角色Id

                break;

            case R.id.bGameLoading:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_LOADING, "");

                break;

            case R.id.bGameCurrency:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_CURRENCY_CONSUME, "1");     //param为游戏币消费数量

                break;

            case R.id.bRmbConsume:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_RMB_CONSUME, "1");          //param为人名币消费金额

                break;

            case R.id.bUpdateLevel:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_UPDATE_LEVEL, "15");        //param为角色等级
                break;

            case R.id.bGameClose:
                mJyPlatform.dataUpload(JyGameSdkStatusCode.GAME_CLOSE, "");

                break;

            case R.id.bAutoLogin:
                mJyPlatform.autoLogin("13761091371", "sjj19831031");
                break;

            case R.id.baccountRecord:
                mJyPlatform.accountRecord("1111111", "222222222");

                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("eeeee", "onActivityResult");
        mJyPlatform.onActivityResult(requestCode, resultCode, data);
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("eeeee", "onRequestPermissionsResult");
        mJyPlatform.onRequestPermissionsResult(requestCode, grantResults);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //接入悬浮窗时必须实现
        mJyPlatform.OnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //接入悬浮窗时必须实现
        mJyPlatform.onPause();
    }
}
