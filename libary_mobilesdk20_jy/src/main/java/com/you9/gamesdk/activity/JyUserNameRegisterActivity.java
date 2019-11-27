package com.you9.gamesdk.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;


public class JyUserNameRegisterActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText editUsername;
    private ImageButton clearEditUsername;
    private EditText editPassword;
    private ImageButton clearEditPassword;
    private EditText editRealname;
    private ImageButton clearEditRealname;
    private EditText editIdnumber;
    private ImageButton clearEditIdnumber;
    private ImageButton showPassword;
    private Button btnRegister;
    private CheckBox checkProtocol;
    private TextView textProtocol;
    private CheckBox checkInformationPerfection;
    private LinearLayout llRealname;
    private LinearLayout llIdnumber;
    private RelativeLayout rl_register_info;
    private String tempPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_register_username"));

        initView();
        initData();


        clearEditUsername.setVisibility(TextUtils.isEmpty(editUsername
                .getText()) ? View.GONE : View.VISIBLE);
        clearEditPassword.setVisibility(TextUtils.isEmpty(editPassword
                .getText()) ? View.GONE : View.VISIBLE);
        showPassword.setVisibility(TextUtils.isEmpty(editPassword
                .getText()) ? View.GONE : View.VISIBLE);

        clearEditRealname.setVisibility(TextUtils.isEmpty(editRealname
                .getText()) ? View.GONE : View.VISIBLE);
        clearEditIdnumber.setVisibility(TextUtils.isEmpty(editIdnumber
                .getText()) ? View.GONE : View.VISIBLE);


        editUsername.addTextChangedListener(new JyClearTextWatcher(
                clearEditUsername));
        editPassword.addTextChangedListener(new JyClearTextWatcher(
                clearEditPassword));
        editPassword.addTextChangedListener(new JyClearTextWatcher(showPassword));

        editRealname.addTextChangedListener(new JyClearTextWatcher(
                clearEditRealname));
        editIdnumber.addTextChangedListener(new JyClearTextWatcher(
                clearEditIdnumber));
    }


    private void initView() {


        editUsername = (EditText) findViewById(ResourceUtil.getId(this, "edit_username"));
        clearEditUsername = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_username"));
        editPassword = (EditText) findViewById(ResourceUtil.getId(this, "edit_password"));
        clearEditPassword = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_password"));
        editRealname = (EditText) findViewById(ResourceUtil.getId(this, "edit_realname"));
        clearEditRealname = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_realname"));
        editIdnumber = (EditText) findViewById(ResourceUtil.getId(this, "edit_idnumber"));
        clearEditIdnumber = (ImageButton) findViewById(ResourceUtil.getId(this, "clear_edit_idnumber"));
        showPassword = (ImageButton) findViewById(ResourceUtil.getId(this, "show_password"));
        btnRegister = (Button) findViewById(ResourceUtil.getId(this, "btn_register"));
        checkProtocol = (CheckBox) findViewById(ResourceUtil.getId(this, "check_protocol"));
        textProtocol = (TextView) findViewById(ResourceUtil.getId(this, "text_protocol"));
        checkInformationPerfection = (CheckBox) findViewById(ResourceUtil.getId(this, "check_information_perfection"));
        llRealname = (LinearLayout) findViewById(ResourceUtil.getId(this, "ll_realname"));
        llIdnumber = (LinearLayout) findViewById(ResourceUtil.getId(this, "ll_idnumber"));
        rl_register_info = (RelativeLayout) findViewById(ResourceUtil.getId(this, "rl_register_info"));

        clearEditUsername.setOnClickListener(this);
        clearEditPassword.setOnClickListener(this);
        clearEditRealname.setOnClickListener(this);
        clearEditIdnumber.setOnClickListener(this);
        showPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        checkProtocol.setOnCheckedChangeListener(this);
        checkInformationPerfection.setOnCheckedChangeListener(this);


    }


    private void initData() {

        do {
            Log.d("eeeee", "do");
            tempPwd = JyUtils.getRandomPwd(6);
            Log.d("eeeee", "tempPwd=" + tempPwd);
            Log.d("eeeee", "matches=" + tempPwd.matches(JyConstants.USERNAME_REGRXP));



        }while (!tempPwd.matches(JyConstants.PWD_REGRXP));

        editUsername.setText(JyUtils.getUserName_12());
        Log.d("eeeee", "setText");
        editPassword.setText(tempPwd);


    }


    /**
     * 验证用户名 密码 真实姓名 身份证格式
     *
     * @param userName
     * @param password
     * @return
     */
    private boolean verify(String userName, String password, String realName,
                           String idNumber) {
        // TODO Auto-generated method stub
        editUsername.setError(null);
        editPassword.setError(null);
//        editRealname.setError(null);
//        editIdnumber.setError(null);
        boolean pass = true;
        if (!userName.matches(JyConstants.USERNAME_REGRXP)) {
            editUsername.setError(JyConstants.USERNAME_FORMERR);
            editUsername.requestFocus();
            pass = false;
        }
        if (!password.matches(JyConstants.PWD_REGRXP)) {
            editPassword.setError(JyConstants.PWD_FORMERR);
            editPassword.requestFocus();
            pass = false;
        }
//        if (realName.equals("")) {
//            editRealname.setError(JyConstants.REALNAME_FORMERR);
//            editRealname.requestFocus();
//            pass = false;
//        }
//
//        if (!idNumber.matches(JyConstants.ID_CARD_REGEX)) {
//            editIdnumber.setError(JyConstants.ID_NUMBER_FORMERR);
//            editIdnumber.requestFocus();
//        }
        return pass;
    }

    @Override
    public void onClick(View v) {

//        int i = view.getId();
        int clear_edit_username = ResourceUtil.getId(this, "clear_edit_username");
        int clear_edit_password = ResourceUtil.getId(this, "clear_edit_password");
        int clear_edit_realname = ResourceUtil.getId(this, "clear_edit_realname");
        int clear_edit_idnumber = ResourceUtil.getId(this, "clear_edit_idnumber");
        int btn_register = ResourceUtil.getId(this, "btn_register");
        int show_password = ResourceUtil.getId(this, "show_password");

        if (clear_edit_username == v.getId()) {
            editUsername.setText("");
            editUsername.requestFocus();
        } else if (clear_edit_password == v.getId()) {
            editPassword.setText("");
            editPassword.requestFocus();
        } else if (clear_edit_realname == v.getId()) {
            editRealname.setText("");
            editRealname.requestFocus();
        } else if (clear_edit_idnumber == v.getId()) {
            editIdnumber.setText("");
            editIdnumber.requestFocus();
        } else if (show_password == v.getId()) {

            int inputType = editPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editPassword.setInputType(inputType);
            Editable etable = editPassword.getText();
            Selection.setSelection(etable, etable.length());

        } else if (btn_register == v.getId()) {
            final String userName = editUsername.getText().toString();
            final String password = editPassword.getText().toString();
            String realName = editRealname.getText().toString();
            String idNumber = editIdnumber.getText().toString();
            if (verify(userName, password, realName, idNumber)) {
//                    RegisterUserNameTask task = new RegisterUserNameTask();
//                    task.execute(userName, password, realName, idNumber);

                btnRegister.setEnabled(false);
                new JyAppRequest(JyUserNameRegisterActivity.this, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {
                        JyUtils.saveRegisterInfoPic(JyUserNameRegisterActivity.this, userName, password, rl_register_info);
                        btnRegister.setEnabled(true);
                        JyRegisterInfo jyRegisterInfo = new JyRegisterInfo();
                        jyRegisterInfo.setUserName(userName);
                        jyRegisterInfo.setPassword(password);
                        JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_SUCCESS, jyRegisterInfo);
                        getParent().finish();

                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        btnRegister.setEnabled(true);

                    }
                }).registerByUnameRequest(userName, password, realName, idNumber);


            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int check_protocol = ResourceUtil.getId(this, "check_protocol");
        int check_information_perfection = ResourceUtil.getId(this, "check_information_perfection");
        if (check_protocol == buttonView.getId()) {
            btnRegister.setEnabled(isChecked);
        } else if (check_information_perfection == buttonView.getId()) {
            if (isChecked) {
                llRealname.setVisibility(View.VISIBLE);
                llIdnumber.setVisibility(View.VISIBLE);
            } else {
                llRealname.setVisibility(View.GONE);
                llIdnumber.setVisibility(View.GONE);
            }
        }
    }
}
