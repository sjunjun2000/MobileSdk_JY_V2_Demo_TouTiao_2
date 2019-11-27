package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyDialogCloseListener;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;


public class JyRegisterTelDialog extends Dialog implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivKf;
    //    private EditText editAccount;
    private ImageButton clearEditTel;
    private EditText editTel;
    private Button btnTelRegisterNextStep;
    private LinearLayout linearQuickGame;
    private LinearLayout linearUnameLogin;
    private EditText editTelPassword;
    private ImageButton clearEditTelPassword;
//    private CheckBox checkRemember;
//    private CheckBox checkAutoLogin;
//    private TextView textForgot;
//    private Button btnRegister;
//    private Button btnLogin;
//    private LinearLayout llRegLog;
//    private TextView tvLogTips;
//    private LinearLayout linearAccount;

    private Context mContext;
    //    private JyNetWorkDialog mJyNetWorkDialog;
//    private JyYou9 jyYou9;
//    private String userName;
//    private String password;
//    private JyUserDao userDao;
//    private JyYou9Dao you9Dao;
//    private JyCallbackListener mListener;
//    private PreferencesUtils preferencesUtils;
//    private List<JyUser> userList;
    private ImageButton ibRegisterShowPwd;
    //    private ImageButton ibAccountMore;
//    private JyAccountsPopup accountsPopup;
//    private boolean moreAccount = false;
    private JyDialogCloseListener dialogCloseListener;


    public JyRegisterTelDialog(Context context, JyDialogCloseListener dialogCloseListener) {
        super(context);
        this.mContext = context;
        this.dialogCloseListener = dialogCloseListener;
//        this.mListener = listener;

        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_register_tel"), null);
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


//        mJyNetWorkDialog = new JyNetWorkDialog(mContext);
//        tvLogTips.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        userDao = JyPlatform.getDaoSession().getJyUserDao();
//        you9Dao = JyPlatform.getDaoSession().getJyYou9Dao();
//
//        jyYou9 = you9Dao.queryBuilder().build().unique();
//
//        if (jyYou9 != null) {
//            editAccount.setText(jyYou9.getJyUser().getUsername());
//            editPassword.setText(jyYou9.getJyUser().getPassword());
//            checkRemember.setChecked(true);
//        }
//
//        clearEditAccount.setVisibility(TextUtils.isEmpty(editAccount.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
//        clearEditPassword.setVisibility(TextUtils.isEmpty(editPassword.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
//
//        editAccount.addTextChangedListener(new JyClearTextWatcher(
//                clearEditAccount));
//        editPassword.addTextChangedListener(new JyClearTextWatcher(
//                clearEditPassword));


    }

    private void initView() {
        ivBack = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivBack"));
        ivKf = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivKf"));
        editTel = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_tel"));
        clearEditTel = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_tel"));
        btnTelRegisterNextStep = (Button) findViewById(ResourceUtil.getId(mContext, "btn_tel_register_next_step"));
        linearQuickGame = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "linear_quick_game"));
        linearUnameLogin = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "linear_uname_login"));
        editTelPassword = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_tel_password"));
        clearEditTelPassword = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_tel_password"));
        ibRegisterShowPwd = (ImageButton) findViewById(ResourceUtil.getId(mContext, "ib_register_show_pwd"));
        ivBack.setOnClickListener(this);
        ivKf.setOnClickListener(this);
        clearEditTel.setOnClickListener(this);
        ibRegisterShowPwd.setOnClickListener(this);
        btnTelRegisterNextStep.setOnClickListener(this);
        linearQuickGame.setOnClickListener(this);
        linearUnameLogin.setOnClickListener(this);
        editTel.addTextChangedListener(new JyClearTextWatcher(clearEditTel));
        editTelPassword.addTextChangedListener(new JyClearTextWatcher(clearEditTelPassword));
        editTelPassword.addTextChangedListener(new JyClearTextWatcher(ibRegisterShowPwd));

        clearEditTel.setVisibility(TextUtils.isEmpty(editTel.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
        clearEditTelPassword.setVisibility(TextUtils.isEmpty(editTelPassword.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
        ibRegisterShowPwd.setVisibility(TextUtils.isEmpty(editTelPassword.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);


        ivKf.setVisibility(View.VISIBLE);
        editTel.setText(TextUtils.isEmpty(JySdkConfigInfo.getInstance().getPhoneNum()) ? "" : JySdkConfigInfo.getInstance().getPhoneNum());


        clearEditTelPassword.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int iv_back = ResourceUtil.getId(mContext, "ivBack");
        int iv_kf = ResourceUtil.getId(mContext, "ivKf");
        int clear_edit_tel = ResourceUtil.getId(mContext, "clear_edit_tel");
        int btn_tel_register_next_step = ResourceUtil.getId(mContext, "btn_tel_register_next_step");
        int linear_quick_game = ResourceUtil.getId(mContext, "linear_quick_game");
        int linear_uname_login = ResourceUtil.getId(mContext, "linear_uname_login");
        int clear_edit_tel_password = ResourceUtil.getId(mContext, "clear_edit_tel_password");
        int ib_register_show_pwd = ResourceUtil.getId(mContext, "ib_register_show_pwd");
        if (iv_back == v.getId() || linear_uname_login == v.getId()) {
            //返回按钮
            dismiss();
        } else if (linear_quick_game == v.getId()) {
            new JyRegisterUnameDialog(mContext, new JyDialogCloseListener() {
                @Override
                public void onClose(int type) {
                    dialogCloseListener.onClose(type);
                    dismiss();
                }
            }).show();

        } else if (iv_kf == v.getId()) {
            //跳转客服
            Uri uri = Uri.parse(JySdkConfigInfo.getInstance().getKfUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(it);
        } else if (clear_edit_tel == v.getId()) {
            //清除注册手机号
            editTel.setText("");
            editTel.requestFocus();
        } else if (clear_edit_tel_password == v.getId()) {
            editTelPassword.setText("");
            editTelPassword.requestFocus();
        } else if (ib_register_show_pwd == v.getId()) {
            int inputType = editTelPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                ibRegisterShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes")));
            }else {
                ibRegisterShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes_close")));
            }
                editTelPassword.setInputType(inputType);
            Editable etable = editTelPassword.getText();
            Selection.setSelection(etable, etable.length());

        } else if (btn_tel_register_next_step == v.getId()) {
            //验证手机号，手机号注册跳转验证码页面
            final String cellphone = editTel.getText().toString().trim();
            final String password = editTelPassword.getText().toString().trim();
            if (verify(cellphone, password)) {
//                btnTelRegisterNextStep.setEnabled(false);


//                临时部分
//                new JySmsVerifyDialog(mContext, cellphone, password, new JyDialogCloseListener() {
//                    @Override
//                    public void onClose(int type) {
//                        dialogCloseListener.onClose(type);
//                                        dismiss();
//
//                    }
//                }).show();


                new JyAppRequest(mContext, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {
                        //注册手机号可以注册，跳转验证码验证页面

                        btnTelRegisterNextStep.setEnabled(true);
                        new JySmsVerifyDialog(mContext, cellphone, password, new JyDialogCloseListener() {
                            @Override
                            public void onClose(int type) {
                                dialogCloseListener.onClose(type);
                                dismiss();
                            }
                        }).show();


                        /**
                         * 发送验证码
                         */


//                        new JyAppRequest(mContext, new JyAppRequestListener() {
//                            @Override
//                            public void onReqSuccess(Object obj) {
//
//                                btnTelRegisterNextStep.setEnabled(true);
//                                new JySmsVerifyDialog(mContext, cellphone, password, new JyDialogCloseListener() {
//                                    @Override
//                                    public void onClose(int type) {
//                                        dialogCloseListener.onClose(type);
//                                        dismiss();
//                                    }
//                                }).show();
//
//                            }
//
//                            @Override
//                            public void onReqFailed(String errorMsg) {
//                                btnTelRegisterNextStep.setEnabled(true);
//                            }
//                        }).smsSendRequest(cellphone);


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        btnTelRegisterNextStep.setEnabled(true);
                    }
                }).checkCellphone(cellphone);


            }
        }

    }


    /**
     * 验证手机号 密码格式
     *
     * @param cellphone
     * @param password
     * @return
     */
    private boolean verify(String cellphone, String password) {

        boolean pass = true;
//        editAccount.setError(null);
//        editPassword.setError(null);

        if (!JyUtils.checkTelphone(mContext, cellphone)){
            pass = false;
//            Toast.makeText(mContext, JyConstants.TELPHONE_FORMERR, Toast.LENGTH_SHORT).show();
        }else if (!JyUtils.checkPwd(mContext, password) && pass == true){
            pass = false;
//            Toast.makeText(mContext, JyConstants.PWD_FORMERR, Toast.LENGTH_SHORT).show();

        }
        return pass;

//        editTel.setError(null);
//        editTelPassword.setError(null);
//        boolean pass = true;
//        if (cellphone != null && !cellphone.matches(JyConstants.TELPHONE_REGRXP)) {
//            editTel.setError(JyConstants.TELPHONE_FORMERR);
//            editTel.requestFocus();
//            pass = false;
//        }
//        if (!password.matches(JyConstants.PWD_REGRXP)) {
//            editTelPassword.setError(JyConstants.PWD_FORMERR);
//            editTelPassword.requestFocus();
//            pass = false;
//        }
//
//        return pass;
    }


}
