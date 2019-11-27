package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanqi.verificationcodeinputview.VerificationInputCodeView;
import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyDialogCloseListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.PreferencesUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JySMSCountDownTimer;

import java.util.List;


public class JySmsVerifyDialog extends Dialog implements View.OnClickListener, VerificationInputCodeView.OnKeyEvent {
    private ImageView ivBack;
    private ImageView ivClose;
    private Context mContext;
    private TextView tvVerifyTel;
    private String smsVerifyTel;
    private String telPassword;
    private TextView tvResendTime;
    private PreferencesUtils preferencesUtils;
    private JySMSCountDownTimer timer;// 短信验证计时器
    private VerificationInputCodeView vcivSmsCode;
    private StringBuffer verifyCode;
    private JyDialogCloseListener dialogCloseListener;
    private TextView tvKf;
    private TextView tvUnameLogin;


    public JySmsVerifyDialog(Context context, String smsVerifyTel, String telPassword, JyDialogCloseListener dialogCloseListener) {
        super(context);
        this.mContext = context;
        this.dialogCloseListener = dialogCloseListener;
        this.smsVerifyTel = smsVerifyTel;
        this.telPassword = telPassword;

        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_sms_verify"), null);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();

        window.requestFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wAttrs = window.getAttributes();
        wAttrs.gravity = Gravity.CENTER_HORIZONTAL;
        window.setWindowAnimations(android.R.style.Animation_Dialog);
        window.setBackgroundDrawable(context.getResources().getDrawable(
                (android.R.color.transparent)));
        window.setContentView(view);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();


    }

    private void initView() {
        ivBack = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivBack"));
        ivClose = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivClose"));
        tvVerifyTel = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_verify_tel"));
        tvResendTime = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_resend_time"));
        vcivSmsCode = (VerificationInputCodeView) findViewById(ResourceUtil.getId(mContext, "vciv_sms_code"));
        tvKf = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_kf"));
        tvUnameLogin = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_uname_login"));

        ivBack.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvKf.setOnClickListener(this);
        tvUnameLogin.setOnClickListener(this);
        tvKf.setOnClickListener(this);
        tvUnameLogin.setOnClickListener(this);

        vcivSmsCode.setmOnKeyEvent(this);

        ivClose.setVisibility(View.VISIBLE);
        tvVerifyTel.setText(String.format(mContext.getResources().getString(ResourceUtil.getStringId(mContext, "jy_sms_verify_dialog_tel")), JyUtils.hidePhoneNum(smsVerifyTel)));




    }
    private void initData(){

        verifyCode = new StringBuffer();
        preferencesUtils = new PreferencesUtils(mContext);
        resend();
        preferencesUtils.saveSMSSendTime(System.currentTimeMillis());
        countdown(60000);
        tvResendTime.setEnabled(false);

    }


    /**
     * 短信发送倒计时
     *
     * @param millisInFuture 倒计时时间 单位毫秒
     */
    private void countdown(Integer millisInFuture) {
        if (millisInFuture == null) {
            long sendtime = preferencesUtils.getSMSSendTime();
            long interval = System.currentTimeMillis() - sendtime;
            if (0 < interval && interval < 60000)
                millisInFuture = (int) (60000 - interval);
            else
                return;
        }
        timer = new JySMSCountDownTimer(millisInFuture, 1000, mContext, tvResendTime);
        timer.start();
    }

    /**
     * 发送/重新发送验证码
     */
    private void resend() {
        tvResendTime.setEnabled(false);


        new JyAppRequest(mContext, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                preferencesUtils.saveSMSSendTime(System.currentTimeMillis());
                countdown(60000);
                tvResendTime.setEnabled(true);

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        }).smsSendRequest(smsVerifyTel);


    }


    @Override
    public void onClick(View v) {
        int iv_back = ResourceUtil.getId(mContext, "ivBack");
        int iv_close = ResourceUtil.getId(mContext, "ivClose");
        int tv_resend_time = ResourceUtil.getId(mContext, "tv_resend_time");
        int tv_kf = ResourceUtil.getId(mContext, "tv_kf");
        int tv_uname_login = ResourceUtil.getId(mContext, "tv_uname_login");
        if (iv_back == v.getId() || iv_close == v.getId()) {
            //返回按钮
            dismiss();
        }else if (tv_resend_time == v.getId()){
            resend();
        }else if (tv_kf == v.getId()){
            Uri uri = Uri.parse(JySdkConfigInfo.getInstance().getKfUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(it);
        }else if (tv_uname_login == v.getId()){
            dialogCloseListener.onClose(1);
            dismiss();
        }


    }


    @Override
    public void onCodeChange(int i, String s) {

    }

    @Override
    public void onFinishCode(List<String> list) {
        verifyCode.delete(0, verifyCode.length());

        Log.d("eeeee", "onFinishCode=");
        for (int i = 0; i < list.size(); i++){
            Log.d("eeeee", "finish=" + list.get(i));
            verifyCode.append(list.get(i));
        }
        Log.d("eeeee", "verifyCode=" + verifyCode.toString());
        smsVerify(verifyCode.toString());

    }

    /**
     * 验证码校验
     */
    private void smsVerify(String vCode) {
        ivBack.setEnabled(false);

        new JyAppRequest(mContext, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                register(smsVerifyTel, telPassword);

            }

            @Override
            public void onReqFailed(String errorMsg) {
                ivBack.setEnabled(true);
//                mJyNetWorkDialog.onFailed(TASK_SMS, errorMsg);

            }
        }).smsVerifyRequest(smsVerifyTel, vCode);

    }

    /**
     * 注册
     */
    private void register(final String cellphone, final String password) {

        new JyAppRequest(mContext, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyRegisterInfo jyRegisterInfo = new JyRegisterInfo();
                jyRegisterInfo.setUserName(cellphone);
                jyRegisterInfo.setPassword(password);
                JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_SUCCESS, jyRegisterInfo);
//                setResult(JyConstants.REGISTER_TEL_RES_CODE);
                dialogCloseListener.onClose(0);
                dismiss();


            }

            @Override
            public void onReqFailed(String errorMsg) {
//                btnSmsVerifySubmit.setEnabled(true);
                ivBack.setEnabled(true);

            }
        }).registerByTelRequest(cellphone, password);

    }
}
