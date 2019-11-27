package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.you9.gamesdk.R;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyCallbackListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.ResourceUtil;
import com.you9.gamesdk.wigdet.JyClearTextWatcher;


public class JyCertificationDialog extends Dialog implements View.OnClickListener {
    private EditText editCertificationRealname;
    private ImageButton clearEditCertificationRealname;
    private EditText editCertificationIdnumber;
    private ImageButton clearEditCertificationIdnumber;
    private Button btnCertificationSubmit;
    Button btnCertificationCancel;
    private Context mContext;
    private JyCallbackListener mListener;

    public JyCertificationDialog(Context context, JyCallbackListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;


        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_certification"), null);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wAttrs = window.getAttributes();
        wAttrs.gravity = Gravity.CENTER | Gravity.FILL_HORIZONTAL;
        window.setWindowAnimations(android.R.style.Animation_Dialog);
        window.setBackgroundDrawable(context.getResources().getDrawable(
                (android.R.color.transparent)));
        window.setContentView(view);
//        ButterKnife.bind(this, view);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        clearEditCertificationRealname.setVisibility(TextUtils.isEmpty(editCertificationRealname.getText().toString().trim()) ? View.GONE : View.VISIBLE);
        clearEditCertificationIdnumber.setVisibility(TextUtils.isEmpty(editCertificationIdnumber.getText().toString().trim()) ? View.GONE : View.VISIBLE);

        editCertificationRealname.addTextChangedListener(new JyClearTextWatcher(
                clearEditCertificationRealname));
        editCertificationIdnumber.addTextChangedListener(new JyClearTextWatcher(
                clearEditCertificationIdnumber));
    }


    private void initView() {

        editCertificationRealname = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_certification_realname"));
        clearEditCertificationRealname = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_certification_realname"));
        editCertificationIdnumber = (EditText) findViewById(ResourceUtil.getId(mContext, "edit_certification_idnumber"));
        clearEditCertificationIdnumber = (ImageButton) findViewById(ResourceUtil.getId(mContext, "clear_edit_certification_idnumber"));
        btnCertificationSubmit = (Button) findViewById(ResourceUtil.getId(mContext, "btn_certification_submit"));
        btnCertificationCancel = (Button) findViewById(ResourceUtil.getId(mContext, "btn_certification_cancel"));
        clearEditCertificationRealname.setOnClickListener(this);
        clearEditCertificationIdnumber.setOnClickListener(this);
        btnCertificationSubmit.setOnClickListener(this);
        btnCertificationCancel.setOnClickListener(this);


    }


//    @SuppressLint("InvalidR2Usage")
//    @OnClick({R2.id.clear_edit_certification_realname, R2.id.clear_edit_certification_idnumber, R2.id.btn_certification_submit, R2.id.btn_certification_cancel})
//    public void onViewClicked(View view) {
//
//        int i = view.getId();
//        if (i == R.id.clear_edit_certification_realname) {
//            editCertificationRealname.setText("");
//            editCertificationRealname.requestFocus();
//        } else if (i == R.id.clear_edit_certification_idnumber) {
//            editCertificationIdnumber.setText("");
//            editCertificationIdnumber.requestFocus();
//        } else if (i == R.id.btn_certification_submit) {
//            String realName = editCertificationRealname.getText().toString().trim();
//            String idNumber = editCertificationIdnumber.getText().toString().trim();
//
//            if (verify(realName, idNumber)) {
//                new JyAppRequest(mContext, new JyAppRequestListener() {
//                    @Override
//                    public void onReqSuccess(Object obj) {
//                        mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
//                        dismiss();
//
//
//                    }
//
//                    @Override
//                    public void onReqFailed(String errorMsg) {
//                        Toast.makeText(mContext, mContext.getString(ResourceUtil.getStringId(mContent, "jy_dialog_certification_fcm_failed")), Toast.LENGTH_SHORT).show();
//
//                    }
//                }).fcmInfoRequest(realName, idNumber);
//            }
//        } else if (i == R.id.btn_certification_cancel) {
//            mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
//            dismiss();
//        }
//
//
//    }


    /**
     * 验证用户名 密码 真实姓名 身份证格式
     *
     * @param realName
     * @param idNumber
     * @return
     */
    private boolean verify(String realName,
                           String idNumber) {
        // TODO Auto-generated method stub
        editCertificationRealname.setError(null);
        editCertificationIdnumber.setError(null);
        boolean pass = true;

        if (realName.equals("")) {
            editCertificationRealname.setError(JyConstants.REALNAME_FORMERR);
            editCertificationRealname.requestFocus();
            pass = false;
        }

        if (!idNumber.matches(JyConstants.ID_CARD_REGEX)) {
            editCertificationIdnumber.setError(JyConstants.ID_NUMBER_FORMERR);
            editCertificationIdnumber.requestFocus();
        }
        return pass;
    }

    @Override
    public void onClick(View v) {

        int clear_edit_certification_realname = ResourceUtil.getId(mContext, "clear_edit_certification_realname");
        int clear_edit_certification_idnumber = ResourceUtil.getId(mContext, "clear_edit_certification_idnumber");
        int btn_certification_submit = ResourceUtil.getId(mContext, "btn_certification_submit");
        int btn_certification_cancel = ResourceUtil.getId(mContext, "btn_certification_cancel");
//        int i = view.getId();
        if (clear_edit_certification_realname == v.getId()) {
            editCertificationRealname.setText("");
            editCertificationRealname.requestFocus();
        } else if (clear_edit_certification_idnumber == v.getId()) {
            editCertificationIdnumber.setText("");
            editCertificationIdnumber.requestFocus();
        } else if (btn_certification_submit == v.getId()) {
            String realName = editCertificationRealname.getText().toString().trim();
            String idNumber = editCertificationIdnumber.getText().toString().trim();

            if (verify(realName, idNumber)) {
                new JyAppRequest(mContext, new JyAppRequestListener() {
                    @Override
                    public void onReqSuccess(Object obj) {
                        mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
                        dismiss();


                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        Toast.makeText(mContext, mContext.getString(ResourceUtil.getStringId(mContext, "jy_dialog_certification_fcm_failed")), Toast.LENGTH_SHORT).show();

                    }
                }).fcmInfoRequest(realName, idNumber);
            }
        } else if (btn_certification_cancel == v.getId()) {
            mListener.login(JyGameSdkStatusCode.LOGIN_SUCCESS, JyUser.getInstance());
            dismiss();
        }

    }


}

