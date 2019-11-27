package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyDialogCloseListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyCode;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;


public class JyRegisterUnameDialog extends Dialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView ivBack;
    private ImageView ivKf;
    private CheckBox checkProtocol;
    private TextView tvProtocol;
    private TextView tvPrivateProtocol;
    private Button btnUnameRegister;
    private EditText editRegisterAccount;
    private ImageButton clearEditAccount;
    private EditText editRegisterPassword;
    private ImageButton ibRegisterUnameShowPwd;
    private ImageButton clearEditUnamePassword;
    private ImageView ivCode;
    private String tempPwd;
    private EditText editRegisterUnameVcode;

    private Context mContext;
    private String realCode;
    private RelativeLayout rl_register_info;
    private JyDialogCloseListener dialogCloseListener;


    public JyRegisterUnameDialog(Context context, JyDialogCloseListener dialogCloseListener) {
        super(context);
        this.mContext = context;
        this.dialogCloseListener = dialogCloseListener;

        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_register_uname"), null);
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
        ivKf = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivKf"));
        checkProtocol = (CheckBox) findViewById(ResourceUtil.getId(mContext, "check_protocol"));
        tvProtocol = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_protocol"));
        tvPrivateProtocol = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_private_protocol"));
        btnUnameRegister = (Button) findViewById(ResourceUtil.getId(mContext, "btn_uname_register"));
        editRegisterAccount = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_register_account"));
        clearEditAccount = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_account"));
        editRegisterPassword = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_register_password"));
        ibRegisterUnameShowPwd = (ImageButton) findViewById(ResourceUtil.getId(mContext, "ib_register_uname_show_pwd"));
        clearEditUnamePassword = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_uname_password"));
        ivCode = (ImageView) findViewById(ResourceUtil.getId(mContext, "iv_code"));
        editRegisterUnameVcode = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_register_uname_vcode"));
        rl_register_info = (RelativeLayout) findViewById(ResourceUtil.getId(mContext, "rl_register_info"));

        ivBack.setOnClickListener(this);
        ivKf.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
        tvPrivateProtocol.setOnClickListener(this);
        btnUnameRegister.setOnClickListener(this);
        clearEditAccount.setOnClickListener(this);
        ibRegisterUnameShowPwd.setOnClickListener(this);
        clearEditUnamePassword.setOnClickListener(this);
        ivCode.setOnClickListener(this);
        btnUnameRegister.setOnClickListener(this);
        checkProtocol.setOnCheckedChangeListener(this);
        editRegisterAccount.addTextChangedListener(new JyClearTextWatcher(clearEditAccount));
        editRegisterPassword.addTextChangedListener(new JyClearTextWatcher(clearEditUnamePassword));
        editRegisterPassword.addTextChangedListener(new JyClearTextWatcher(ibRegisterUnameShowPwd));


        ivKf.setVisibility(View.VISIBLE);

    }

    private void initData(){
        do {
            tempPwd = JyUtils.getRandomPwd(6);



        }while (!tempPwd.matches(JyConstants.PWD_REGRXP));

        editRegisterAccount.setText(JyUtils.getUserName_12());
        editRegisterPassword.setText(tempPwd);
        createVerifyCode();
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
//        editRegisterAccount.setError(null);
//        editRegisterPassword.setError(null);
//        editRealname.setError(null);
//        editIdnumber.setError(null);
//        boolean pass = true;

        boolean pass = true;
//        editAccount.setError(null);
//        editPassword.setError(null);

        if (!JyUtils.checkUsername(mContext, userName)){
            pass = false;
//            Toast.makeText(mContext, JyConstants.USERNAME_FORMERR, Toast.LENGTH_SHORT).show();
        }else if (!JyUtils.checkPwd(mContext, password) && pass == true){
            pass = false;
//            Toast.makeText(mContext, JyConstants.PWD_FORMERR, Toast.LENGTH_SHORT).show();

        }
        return pass;

//        if (!userName.matches(JyConstants.USERNAME_REGRXP)) {
//            editRegisterAccount.setError(JyConstants.USERNAME_FORMERR);
//            editRegisterAccount.requestFocus();
//            pass = false;
//        }
//        if (!password.matches(JyConstants.PWD_REGRXP)) {
//            editRegisterPassword.setError(JyConstants.PWD_FORMERR);
//            editRegisterPassword.requestFocus();
//            pass = false;
//        }
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
//        return pass;
    }



    @Override
    public void onClick(View v) {
        int iv_back = ResourceUtil.getId(mContext, "ivBack");
        int iv_kf = ResourceUtil.getId(mContext, "ivKf");
        int tv_protocol = ResourceUtil.getId(mContext, "tv_protocol");
        int tv_private_protocol = ResourceUtil.getId(mContext, "tv_private_protocol");
        int btn_uname_register = ResourceUtil.getId(mContext, "btn_uname_register");
        int clear_edit_account = ResourceUtil.getId(mContext, "clear_edit_account");
        int ib_register_uname_show_pwd = ResourceUtil.getId(mContext, "ib_register_uname_show_pwd");
        int clear_edit_uname_password = ResourceUtil.getId(mContext, "clear_edit_uname_password");
        int iv_code = ResourceUtil.getId(mContext, "iv_code");
        if (iv_back == v.getId()){
            //返回按钮
            dismiss();
        }else if (iv_kf == v.getId()) {
            //跳转客服
//            mContext.startActivity(new Intent(mContext, JyWebViewActivity.class).putExtra("gnType", JyStateCode.GN_TYPE_KF.getCode()));
            Uri uri = Uri.parse(JySdkConfigInfo.getInstance().getKfUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(it);
        }else if (tv_protocol == v.getId()){
            new JyAgreementDialog(mContext, 0).show();
        }else if (tv_private_protocol == v.getId()){
            new JyAgreementDialog(mContext, 1).show();

        } else if (btn_uname_register == v.getId()){
            //先验证验证码后再进行用户名注册
//            String code = editRegisterUnameVcode.getText().toString().trim();
//            if (code.equals(realCode)){
                //验证码正确,继续注册
                final String userName = editRegisterAccount.getText().toString().trim();
                final String password = editRegisterPassword.getText().toString().trim();
                if (verify(userName, password, "", "")) {
//                    RegisterUserNameTask task = new RegisterUserNameTask();
//                    task.execute(userName, password, realName, idNumber);

                    btnUnameRegister.setEnabled(false);
                    new JyAppRequest(mContext, new JyAppRequestListener() {
                        @Override
                        public void onReqSuccess(Object obj) {
                            JyUtils.saveRegisterInfoPic(mContext, userName, password, rl_register_info);
                            btnUnameRegister.setEnabled(true);
                            JyRegisterInfo jyRegisterInfo = new JyRegisterInfo();
                            jyRegisterInfo.setUserName(userName);
                            jyRegisterInfo.setPassword(password);
                            JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_SUCCESS, jyRegisterInfo);
                            dialogCloseListener.onClose(0);
                            dismiss();

                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            btnUnameRegister.setEnabled(true);

                        }
                    }).registerByUnameRequest(userName, password, "", "");


                }
//            }
//            else{
//                Toast.makeText(mContext, mContext.getResources().getString(ResourceUtil.getStringId(mContext, "jy_uname_register_dialog_code_error")), Toast.LENGTH_SHORT).show();
//                editRegisterUnameVcode.setText("");
//                editRegisterUnameVcode.requestFocus();
//            }
        }else if (clear_edit_account == v.getId()) {
            //清除注册手机号
            editRegisterAccount.setText("");
            editRegisterAccount.requestFocus();
        }else if (ib_register_uname_show_pwd == v.getId()){
            int inputType = editRegisterPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                ibRegisterUnameShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes")));
            }else {
                ibRegisterUnameShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes_close")));
            }
            editRegisterPassword.setInputType(inputType);
            Editable etable = editRegisterPassword.getText();
            Selection.setSelection(etable, etable.length());

        }else if (clear_edit_uname_password == v.getId()){
            editRegisterPassword.setText("");
            editRegisterAccount.requestFocus();
        }else if (iv_code == v.getId()){
            createVerifyCode();
        }
        }

        private void createVerifyCode(){
            ivCode.setImageBitmap(JyCode.getInstance().createBitmap());
            realCode = JyCode.getInstance().getCode().toLowerCase();
            Log.d("eeeee", "code=" + realCode);
        }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d("eeeee", "check=" + isChecked);
        btnUnameRegister.setEnabled(isChecked);

    }
}



