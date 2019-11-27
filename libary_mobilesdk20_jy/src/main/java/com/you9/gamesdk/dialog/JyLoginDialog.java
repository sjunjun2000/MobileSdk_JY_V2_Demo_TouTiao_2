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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.activity.JyWebViewActivity;
import com.you9.gamesdk.adapter.JyAccountsPopupAdapter;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.bean.JyUserDao;
import com.you9.gamesdk.bean.JyYou9;
import com.you9.gamesdk.bean.JyYou9Dao;
import com.you9.gamesdk.enums.JyStateCode;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.listener.JyDialogCloseListener;
import com.you9.gamesdk.open.JyCallbackListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.PreferencesUtils;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyAccountsPopup;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;

import java.util.List;


public class JyLoginDialog extends Dialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, JyAccountsPopup.OnShowListener, JyAccountsPopup.OnDismissListener, JyAccountsPopupAdapter.OnAccountSelectedListener, JyAccountsPopupAdapter.OnAccountDeleteListener {
    private ImageView ivBack;
    private ImageView ivKf;
    private EditText editAccount;
    private ImageButton clearEditAccount;
    private EditText editPassword;
    private ImageButton clearEditPassword;
    private CheckBox checkRemember;
    private CheckBox checkAutoLogin;
    private TextView textForgot;
    //    private Button btnRegister;
    private Button btnLogin;
    private LinearLayout llRegLog;
    //    private TextView tvLogTips;
    private LinearLayout linearAccount;

    private Context mContext;
    private JyNetWorkDialog mJyNetWorkDialog;
    private JyYou9 jyYou9;
    private String userName;
    private String password;
    private JyUserDao userDao;
    private JyYou9Dao you9Dao;
    private JyCallbackListener mListener;
    private PreferencesUtils preferencesUtils;
    private List<JyUser> userList;
    private ImageButton ibLoginShowPwd;
    private ImageButton ibAccountMore;
    private JyAccountsPopup accountsPopup;
    private boolean moreAccount = false;
    private TextView tvTelRegister;
    private TextView tvQuickRegister;


    public JyLoginDialog(Context context, JyCallbackListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;

        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_login"), null);
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


        mJyNetWorkDialog = new JyNetWorkDialog(mContext);
//        tvLogTips.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
//        preferencesUtils = new PreferencesUtils(mContext);
//        userList = preferencesUtils.getAccounts();
//        Log.d("eeeee", "size=" + userList.size());
        userDao = JyPlatform.getDaoSession().getJyUserDao();
        you9Dao = JyPlatform.getDaoSession().getJyYou9Dao();

        jyYou9 = you9Dao.queryBuilder().build().unique();

        if (jyYou9 != null) {
            editAccount.setText(jyYou9.getJyUser().getUsername());
            editPassword.setText(jyYou9.getJyUser().getPassword());
            checkRemember.setChecked(true);
        }

        clearEditAccount.setVisibility(TextUtils.isEmpty(editAccount.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);
        clearEditPassword.setVisibility(TextUtils.isEmpty(editPassword.getText().toString().trim()) ? View.INVISIBLE : View.VISIBLE);

        editAccount.addTextChangedListener(new JyClearTextWatcher(
                clearEditAccount));
        editPassword.addTextChangedListener(new JyClearTextWatcher(
                clearEditPassword));


    }

    private void initView() {
        ivBack = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivBack"));
        ivKf = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivKf"));
        editAccount = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_account"));
        clearEditAccount = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_account"));
        editPassword = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_password"));
        clearEditPassword = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_password"));
        checkRemember = (CheckBox) findViewById(ResourceUtil.getId(mContext, "check_remember"));
        checkAutoLogin = (CheckBox) findViewById(ResourceUtil.getId(mContext, "check_autoLogin"));
        textForgot = (TextView) findViewById(ResourceUtil.getId(mContext, "text_forgot"));
//        btnRegister = (Button) findViewById(ResourceUtil.getId(mContext, "btn_register"));
        btnLogin = (Button) findViewById(ResourceUtil.getId(mContext, "btn_login"));
        llRegLog = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "ll_reg_log"));
//        tvLogTips = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_log_tips"));
        ibLoginShowPwd = (ImageButton) findViewById(ResourceUtil.getId(mContext, "ib_login_show_pwd"));
        ibAccountMore = (ImageButton) findViewById(ResourceUtil.getId(mContext, "ib_account_more"));
        linearAccount = (LinearLayout) findViewById(ResourceUtil.getId(mContext, "linear_account"));

        tvTelRegister = (TextView) findViewById(ResourceUtil.getId(mContext, "tvTelRegister"));
        tvQuickRegister = (TextView) findViewById(ResourceUtil.getId(mContext, "tvQuickRegister"));

        ivKf.setOnClickListener(this);
        clearEditAccount.setOnClickListener(this);
        clearEditPassword.setOnClickListener(this);
//        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
//        tvLogTips.setOnClickListener(this);
        textForgot.setOnClickListener(this);
        checkAutoLogin.setOnCheckedChangeListener(this);
        ibLoginShowPwd.setOnClickListener(this);
        ibAccountMore.setOnClickListener(this);
        tvTelRegister.setOnClickListener(this);
        tvQuickRegister.setOnClickListener(this);

        ivKf.setVisibility(View.VISIBLE);
        ivBack.setVisibility(View.INVISIBLE);
        ibLoginShowPwd.setVisibility(TextUtils.isEmpty(editPassword
                .getText()) ? View.INVISIBLE : View.VISIBLE);

        editPassword.addTextChangedListener(new JyClearTextWatcher(ibLoginShowPwd));
        preferencesUtils = new PreferencesUtils(mContext);
        accountsPopup = new JyAccountsPopup(mContext, preferencesUtils.getAccounts());
        accountsPopup.setOnShowListener(this);
        accountsPopup.setOnDismissListener(this);
        accountsPopup.setOnAccountDeleteListener(this);
        accountsPopup.setOnAccountSelectedListener(this);


    }


    /**
     * 验证用户输入的用户名密码
     *
     * @param account  用户名
     * @param password 密码
     * @return
     */
    private boolean verify(String account, String password) {
        boolean pass = true;
//        editAccount.setError(null);
//        editPassword.setError(null);

        if (!JyUtils.checkUsername(mContext, account)){
            pass = false;
//            Toast.makeText(mContext, JyConstants.USERNAME_FORMERR, Toast.LENGTH_SHORT).show();
        }else if (!JyUtils.checkPwd(mContext, password) && pass == true){
            pass = false;

        }
        return pass;

//        pass = JyUtils.checkUsername(account) && JyUtils.checkUsername(password);
//        try {
//            JyUtils.checkUsername(account);
//        } catch (IllegalArgumentException e) {
//            editAccount.setError(e.getMessage());
//            editAccount.requestFocus();
//            pass = false;
//        }
//        try {
//            JyUtils.checkPwd(password);
//        } catch (IllegalArgumentException e) {
//            editPassword.setError(e.getMessage());
//            editPassword.requestFocus();
//            pass = false;
//        }

    }

    @Override
    public void onClick(View v) {
        int iv_kf = ResourceUtil.getId(mContext, "ivKf");
        int clear_edit_account = ResourceUtil.getId(mContext, "clear_edit_account");
        int clear_edit_password = ResourceUtil.getId(mContext, "clear_edit_password");
        int text_forgot = ResourceUtil.getId(mContext, "text_forgot");
//        int btn_register = ResourceUtil.getId(mContext, "btn_register");
        int btn_login = ResourceUtil.getId(mContext, "btn_login");
//        int tv_log_tips = ResourceUtil.getId(mContext, "tv_log_tips");
        int ib_login_show_pwd = ResourceUtil.getId(mContext, "ib_login_show_pwd");
        int ib_account_more = ResourceUtil.getId(mContext, "ib_account_more");
        int tv_tel_register = ResourceUtil.getId(mContext, "tvTelRegister");
        int tv_quick_register = ResourceUtil.getId(mContext, "tvQuickRegister");

        if (iv_kf == v.getId()) {
//            mContext.startActivity(new Intent(mContext, JyWebViewActivity.class).putExtra("gnType", JyStateCode.GN_TYPE_KF.getCode()));
            Uri uri = Uri.parse(JySdkConfigInfo.getInstance().getKfUrl());
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(it);
        } else if (clear_edit_account == v.getId()) {
            editAccount.setText("");
            editAccount.requestFocus();
        } else if (clear_edit_password == v.getId()) {
            editPassword.setText("");
            editPassword.requestFocus();
        } else if (text_forgot == v.getId()) {
            mContext.startActivity(new Intent(mContext, JyWebViewActivity.class).putExtra("gnType", JyStateCode.GN_TYPE_FORGET_PASSWORD.getCode()));

        } else if (ib_account_more == v.getId()) {
            if (moreAccount) {
                accountsPopup.dismiss();
            } else {
                accountsPopup.setWidth(linearAccount.getWidth());
                accountsPopup.showAsDropDown(linearAccount, 0, -20);
            }


        } else if (ib_login_show_pwd == v.getId()) {
            int inputType = editPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (inputType == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){
                ibLoginShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes")));
            }else {
                ibLoginShowPwd.setImageDrawable(mContext.getResources().getDrawable(ResourceUtil.getMipmapId(mContext, "jy_eyes_close")));
            }
            editPassword.setInputType(inputType);
            Editable etable = editPassword.getText();
            Selection.setSelection(etable, etable.length());

        } else if (btn_login == v.getId()) {
            userName = editAccount.getText().toString().trim();
            password = editPassword.getText().toString().trim();
            if (verify(userName, password)) {
//                     开始登录任务
//                dismiss();


                new JyAppRequest(mContext, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {


                        if (checkAutoLogin.isChecked()) {
                            JyUtils.saveUserInfo(true, true);

                        } else {
                            if (checkRemember.isChecked()) {
                                JyUtils.saveUserInfo(false, true);
                            } else {
                                JyUtils.saveUserInfo(false, false);
                            }
                        }


                        mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
                        dismiss();


                        //判别防沉迷
//                        Log.d("eeeee", "fcmType=" + JyUser.getInstance().getFcm());
//                        if (JyUser.getInstance().getFcm().equals(JyConstants.IS_NOT_FCM)
//                                && !JyUser.getInstance().getUsername().matches(JyConstants.TELPHONE_REGRXP)) {
//                            //弹防沉迷框
//                            JyCertificationDialog dialog = new JyCertificationDialog(mContext, mListener);
//                            dialog.show();
//
//                        } else {
//                            mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
//                        }


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        mListener.login(JyGameSdkStatusCode.LOGIN_FAILED, null);

                    }
                }).loginRequest(userName, password);

            }
        } else if (tv_tel_register == v.getId()) {
            new JyRegisterTelDialog(mContext, new JyDialogCloseListener() {
                @Override
                public void onClose(int type) {
                    if (type == 0) {
                        dismiss();

                }

            }
        }).show();
    }else if(tv_quick_register ==v.getId())

    {
        new JyRegisterUnameDialog(mContext, new JyDialogCloseListener() {
            @Override
            public void onClose(int type) {
                dismiss();
            }
        }).show();
    }

}

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int ck_auto_login = ResourceUtil.getId(mContext, "check_autoLogin");
        if (ck_auto_login == buttonView.getId() && isChecked) {
            if (!checkRemember.isChecked()) {
                checkRemember.setChecked(true);
            }

        }

    }

    @Override
    public void onAccountSelected(int position) {
        List<JyUser> accounts = preferencesUtils.getAccounts();
        JyUser u = accounts.get(position);
        editAccount.setText(u.getUsername());
        editPassword.setText(u.getPassword());
        accountsPopup.dismiss();

    }

    @Override
    public void onAccountdelete(int position) {
        List<JyUser> accounts = preferencesUtils.getAccounts();
        JyUser u = accounts.get(position);
        accounts = preferencesUtils.removeAccount(u);
        if (accounts.isEmpty()) {
            accountsPopup.dismiss();
            ibAccountMore.setVisibility(View.INVISIBLE);
        }
        accountsPopup.refreshView(accounts);

    }

    @Override
    public void onAccountsPopupShow() {
        moreAccount = true;
        ibAccountMore.setImageResource(ResourceUtil.getMipmapId(
                mContext, "jy_login_more_up"));

    }

    @Override
    public void onAccountsPopupDismiss() {
        moreAccount = false;
        ibAccountMore.setImageResource(ResourceUtil.getMipmapId(
                mContext, "jy_login_more"));

    }
}
