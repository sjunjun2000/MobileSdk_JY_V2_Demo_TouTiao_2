package com.you9.gamesdk.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.R;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.PreferencesUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;

import java.util.HashMap;


public class JySmsVerifyActivity extends BaseActivity implements View.OnClickListener {

    private TextView textTitle;
    private Button btnTitleBack;
    private TextView textSmsVerifyTips;
    private EditText editSmsVerifySms;
    private ImageButton clearEditSms;
    private Button btnSmsVerifySubmit;
    private Button btnSmsVerifyResend;

    private String cellphone;
    private String password;
    private PreferencesUtils preferencesUtils;
//    private JySMSCountDownTimer timer;// 短信验证计时器
    //    private JyNetWorkDialog mJyNetWorkDialog;
    private HashMap<String, String> params;
    private final int TASK_SMS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jy_activity_sms_verify);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_sms_verify"));
        initView();

//        ButterKnife.bind(JySmsVerifyActivity.this);
        cellphone = getIntent().getStringExtra("cellphone");
        password = getIntent().getStringExtra("password");
        preferencesUtils = new PreferencesUtils(JySmsVerifyActivity.this);
//        mJyNetWorkDialog = new JyNetWorkDialog(this);
        params = new HashMap<String, String>();


        clearEditSms
                .setVisibility(TextUtils.isEmpty(editSmsVerifySms.getText().toString().trim()) ? View.GONE
                        : View.VISIBLE);

        editSmsVerifySms.addTextChangedListener(new JyClearTextWatcher(clearEditSms));

        countdown(null);
//        resend();
    }

    private void initView() {

        textTitle = (TextView) findViewById(ResourceUtil.getId(this, "text_title"));
        btnTitleBack = (Button) findViewById(ResourceUtil.getId(this, "btn_title_back"));
        textSmsVerifyTips = (TextView) findViewById(ResourceUtil.getId(this, "text_sms_verify_tips"));
        editSmsVerifySms = (EditText) findViewById(ResourceUtil.getId(this, "edit_sms_verify_sms"));
        clearEditSms = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_sms"));
        btnSmsVerifySubmit = (Button) findViewById(ResourceUtil.getId(this, "btn_sms_verify_submit"));
        btnSmsVerifyResend = (Button) findViewById(ResourceUtil.getId(this, "btn_sms_verify_resend"));
        btnTitleBack.setOnClickListener(this);
        clearEditSms.setOnClickListener(this);
        btnSmsVerifySubmit.setOnClickListener(this);
        btnSmsVerifyResend.setOnClickListener(this);

    }

//    @SuppressLint("InvalidR2Usage")
//    @OnClick({R2.id.btn_title_back, R2.id.btn_sms_verify_submit, R2.id.btn_sms_verify_resend, R2.id.clear_edit_sms})
//    public void onViewClicked(View view) {
//
//        int i = view.getId();
//        if (i == R.id.btn_title_back){
//            finish();
//        }else if (i == R.id.btn_sms_verify_submit){
//            registerSubmit();// 注册
//        }else if (i == R.id.btn_sms_verify_resend){
//            resend();
//        }else if (i == R.id.clear_edit_sms){
//            editSmsVerifySms.setText("");
////                editSmsVerifySms.requestFocus();
//        }
//    }


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
//        timer = new JySMSCountDownTimer(millisInFuture, 1000, this, btnSmsVerifyResend);
//        timer.start();
    }


    /**
     * 注册提交
     */
    private void registerSubmit() {
//        String cellphone = getIntent().getStringExtra("cellphone");
        String code = editSmsVerifySms.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            editSmsVerifySms.setError(getString(ResourceUtil.getStringId(this, "jy_activity_sms_verify_empty")));
            editSmsVerifySms.requestFocus();
            return;
        }
        //验证码校验
        smsVerify(code);


    }


    /**
     * 验证码校验
     */
    private void smsVerify(String vCode) {
        btnSmsVerifySubmit.setEnabled(false);
        btnTitleBack.setEnabled(false);

        new JyAppRequest(JySmsVerifyActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                register(cellphone, password);

            }

            @Override
            public void onReqFailed(String errorMsg) {
                btnSmsVerifySubmit.setEnabled(true);
                btnTitleBack.setEnabled(true);
//                mJyNetWorkDialog.onFailed(TASK_SMS, errorMsg);

            }
        }).smsVerifyRequest(cellphone, vCode);

    }

    /**
     * 注册
     */
    private void register(final String cellphone, final String password) {

        new JyAppRequest(JySmsVerifyActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyRegisterInfo jyRegisterInfo = new JyRegisterInfo();
                jyRegisterInfo.setUserName(cellphone);
                jyRegisterInfo.setPassword(password);
                JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_SUCCESS, jyRegisterInfo);
                setResult(JyConstants.REGISTER_TEL_RES_CODE);
                finish();


            }

            @Override
            public void onReqFailed(String errorMsg) {
                btnSmsVerifySubmit.setEnabled(true);
                btnTitleBack.setEnabled(true);

            }
        }).registerByTelRequest(cellphone, password);

    }


    /**
     * 发送/重新发送验证码
     */
    private void resend() {
        btnSmsVerifySubmit.setEnabled(false);
        btnTitleBack.setEnabled(false);
        btnSmsVerifyResend.setEnabled(false);


        new JyAppRequest(JySmsVerifyActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                preferencesUtils.saveSMSSendTime(System.currentTimeMillis());
                countdown(60000);
                btnSmsVerifySubmit.setEnabled(true);

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        }).smsSendRequest(cellphone);


    }

    @Override
    public void onClick(View v) {
        int btn_title_back = ResourceUtil.getId(this, "btn_title_back");
        int btn_sms_verify_submit = ResourceUtil.getId(this, "btn_sms_verify_submit");
        int btn_sms_verify_resend = ResourceUtil.getId(this, "btn_sms_verify_resend");
        int clear_edit_sms = ResourceUtil.getId(this, "clear_edit_sms");

        if (btn_title_back == v.getId()) {
            finish();
        } else if (btn_sms_verify_submit == v.getId()) {
            registerSubmit();// 注册
        } else if (btn_sms_verify_resend == v.getId()) {
            resend();
        } else if (clear_edit_sms == v.getId()) {
            editSmsVerifySms.setText("");
//                editSmsVerifySms.requestFocus();
        }

    }
}
