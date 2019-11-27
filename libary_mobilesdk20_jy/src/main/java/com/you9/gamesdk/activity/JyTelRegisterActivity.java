package com.you9.gamesdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.enums.JyStateCode;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;

public class JyTelRegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView textCurrentCellphone;
    private CheckBox checkInputCellphone;
    private LinearLayout linearCurrentCellphone;
    private EditText editCellphone;
    private ImageButton clearEditCellphone;
    private LinearLayout linearCellphone;
    private EditText editPassword;
    private ImageButton clearEditPassword;
    private CheckBox checkShowPassword;
    private Button btnRegister;
    private CheckBox checkProtocol;
    private TextView textProtocol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jy_activity_register_tel);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_register_tel"));
        initView();

//        ButterKnife.bind(JyTelRegisterActivity.this);

        clearEditCellphone.setVisibility(TextUtils.isEmpty(editCellphone.getText().toString().trim()) ? View.GONE : View.VISIBLE);
        clearEditPassword.setVisibility(TextUtils.isEmpty(editPassword.getText().toString().trim()) ? View.GONE : View.VISIBLE);

        editCellphone.addTextChangedListener(new JyClearTextWatcher(
                clearEditCellphone));
        editPassword.addTextChangedListener(new JyClearTextWatcher(
                clearEditPassword));

        autoFill(JySdkConfigInfo.getInstance().getPhoneNum());

    }

    private void initView(){
        textCurrentCellphone = (TextView) findViewById(ResourceUtil.getId(this, "text_current_cellphone"));
        checkInputCellphone = (CheckBox) findViewById(ResourceUtil.getId(this, "check_input_cellphone"));
        linearCurrentCellphone = (LinearLayout) findViewById(ResourceUtil.getId(this, "linear_current_cellphone"));
        editCellphone = (EditText) findViewById(ResourceUtil.getId(this, "edit_cellphone"));
        clearEditCellphone = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_cellphone"));
        linearCellphone = (LinearLayout) findViewById(ResourceUtil.getId(this, "linear_cellphone"));
        editPassword = (EditText) findViewById(ResourceUtil.getId(this, "edit_password"));
        clearEditPassword = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_password"));
        checkShowPassword = (CheckBox) findViewById(ResourceUtil.getId(this, "check_show_password"));
        btnRegister = (Button) findViewById(ResourceUtil.getId(this, "btn_register"));
        checkProtocol = (CheckBox) findViewById(ResourceUtil.getId(this, "check_protocol"));
        textProtocol = (TextView) findViewById(ResourceUtil.getId(this, "text_protocol"));
        clearEditCellphone.setOnClickListener(this);
        clearEditPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        textProtocol.setOnClickListener(this);
        checkInputCellphone.setOnCheckedChangeListener(this);
        checkShowPassword.setOnCheckedChangeListener(this);
        checkProtocol.setOnCheckedChangeListener(this);
    }


    /**
     * 如果取得本机手机号则显示
     *
     * @param cellphone
     */
    private void autoFill(String cellphone) {
        boolean isEmpty = TextUtils.isEmpty(cellphone);
        linearCurrentCellphone.setVisibility(isEmpty ? View.GONE
                : View.VISIBLE);
        linearCellphone.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        textCurrentCellphone.setText(isEmpty ? "" : cellphone);
        checkInputCellphone.setChecked(isEmpty);
    }




    /**
     * 验证手机号 密码格式
     *
     * @param cellphone
     * @param password
     * @return
     */
    private boolean verify(String cellphone, String password) {
        editCellphone.setError(null);
        editPassword.setError(null);
        boolean pass = true;
        if (cellphone != null && !cellphone.matches(JyConstants.TELPHONE_REGRXP)) {
            editCellphone.setError(JyConstants.TELPHONE_FORMERR);
            editCellphone.requestFocus();
            pass = false;
        }
        if (!password.matches(JyConstants.PWD_REGRXP)) {
            editPassword.setError(JyConstants.PWD_FORMERR);
            editPassword.requestFocus();
            pass = false;
        }

        return pass;
    }


    @Override
    public void onClick(View v) {
//        int i = view.getId();
        int clear_edit_cellphone = ResourceUtil.getId(this, "clear_edit_cellphone");
        int clear_edit_password = ResourceUtil.getId(this, "clear_edit_password");
        int text_protocol = ResourceUtil.getId(this, "text_protocol");
        int btn_register = ResourceUtil.getId(this, "btn_register");
        if (clear_edit_cellphone == v.getId()){
            editCellphone.setText("");
            editCellphone.requestFocus();
        }else if (clear_edit_password == v.getId()){
            editPassword.setText("");
            editPassword.requestFocus();
        }else if (text_protocol == v.getId()){
            startActivity(new Intent(JyTelRegisterActivity.this, JyWebViewActivity.class).putExtra("gnType", JyStateCode.GN_TYPE_PROTOCOL.getCode()));

        }else if (btn_register == v.getId()) {
            if (checkInputCellphone.isChecked()) {// 使用用户输入的手机号注册
                final String cellphone = editCellphone.getText().toString().trim();
                final String password = editPassword.getText().toString().trim();
                if (verify(cellphone, password)) {
                    btnRegister.setEnabled(false);

//                    JyUtils.checkTelphone(cellphone);

                    new JyAppRequest(JyTelRegisterActivity.this, new JyAppRequestListener() {
                        @Override
                        public void onReqSuccess(Object obj) {
                            //注册手机号可以注册，跳转验证码验证页面
                            Intent intent = new Intent(JyTelRegisterActivity.this,
                                    JySmsVerifyActivity.class);
                            intent.putExtra("cellphone", cellphone);
                            intent.putExtra("password", password);
                            getParent().startActivityForResult(intent, JyConstants.REGISTER_TEL_RES_CODE);
                            btnRegister.setEnabled(true);

                        }

                        @Override
                        public void onReqFailed(String errorMsg) {

                        }
                    }).checkCellphone(cellphone);


                }
            } else {// 使用本地获取的手机号注册
                final String cellphone = JySdkConfigInfo.getInstance().getPhoneNum();
                final String password = editPassword.getText().toString().trim();
                if (verify(null, password)) {
                    btnRegister.setEnabled(false);
                    new JyAppRequest(JyTelRegisterActivity.this, new JyAppRequestListener() {
                        @Override
                        public void onReqSuccess(Object obj) {
                            btnRegister.setEnabled(true);
                            JyRegisterInfo jyRegisterInfo = new JyRegisterInfo();
                            jyRegisterInfo.setUserName(cellphone);
                            jyRegisterInfo.setPassword(password);
                            JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_SUCCESS, jyRegisterInfo);
                            getParent().finish();


                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            btnRegister.setEnabled(true);

                        }
                    }).registerByTelRequest(cellphone, password);
                }
            }
        }

        }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        int i = buttonView.getId();

        int check_input_cellphone = ResourceUtil.getId(this, "check_input_cellphone");
        int check_show_password = ResourceUtil.getId(this, "check_show_password");
        int check_protocol = ResourceUtil.getId(this, "check_protocol");

        if (check_input_cellphone == buttonView.getId()){
            linearCellphone.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        }else if (check_show_password == buttonView.getId()){
            int inputType = isChecked ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT
                    | InputType.TYPE_TEXT_VARIATION_PASSWORD;
            editPassword.setInputType(inputType);
            Editable etable = editPassword.getText();
            Selection.setSelection(etable, etable.length());
        }else if (check_protocol == buttonView.getId()){
            btnRegister.setEnabled(isChecked);
        }

    }
}

