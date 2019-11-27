package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caozaolin.mreactfloatview.ZSDK;
import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.ResourceUtil;

/**
 * 网络任务提示框
 */
public class JyNetWorkDialog extends Dialog implements View.OnClickListener {

    private final int SUCCESS_WAITING_TIME = 2000;// 对话框延迟关闭时间 单位毫秒
    private TextView tvTitle;
    private Button btnSubmitNetwork;
    private Button btnSwitchAccount;
//    private TextView textTitle;
//    private ImageView icTrue;
//    private ProgressBar pbLoading;
//    private ImageView icWarning;
//    private TextView textContent;
//    private Button btnFailed;


    //    private SpannableStringBuilder styleTitle;// 标题样式
    private String title;// 标题内容
//    private String tips;// 提示内容

    private OnSuccessListener onSuccessListener;
//    private OnFailedListener onFailedListener;

//    private int taskId;

    private Context mContext;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    // this.taskId = taskId;
//                    pbLoading.setVisibility(View.GONE);
//                    icTrue.setVisibility(View.VISIBLE);
//                    icWarning.setVisibility(View.GONE);
//                    textTitle.setText(tips);
//                    btnFailed.setVisibility(View.GONE);
                    tvTitle.setText(title);

                    new CountDownTimer(SUCCESS_WAITING_TIME, SUCCESS_WAITING_TIME) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            // 短暂时间后关闭对话框并回调
                            Log.d("eeeee", "onFinish");
//                            if (btnSwitchAccount.isEnabled()){
//                                btnSwitchAccount.setEnabled(false);
//                            }
                            Log.d("eeeee", "onFinishFFFFF");
                            if (onSuccessListener != null && btnSwitchAccount.isEnabled()){
                                btnSwitchAccount.setEnabled(false);
                                onSuccessListener
                                        .onSuccess();
                            }

                            if (isShowing())
                                dismiss();

                        }
                    }.start();

                    break;

                case 1:
                    Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
                    dismiss();
//                    pbLoading.setVisibility(View.GONE);
//                    icTrue.setVisibility(View.GONE);
//                    icWarning.setVisibility(View.VISIBLE);
//                    tvTitle.setText(title);
//                    btnSubmitNetwork.setVisibility(View.VISIBLE);
//                    btnFailed.setVisibility(View.VISIBLE);

                    break;

                default:
                    break;
            }
        }
    };

    public JyNetWorkDialog(Context context) {
        super(context);
        this.mContext = context;
        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_waiting"), null);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wAttrs = window.getAttributes();

//        wAttrs.gravity = Gravity.TOP;
        wAttrs.x = 0;
        wAttrs.y = -500;
        window.setWindowAnimations(android.R.style.Animation_Dialog);
        window.setBackgroundDrawable(context.getResources().getDrawable(
                (android.R.color.transparent)));
        setCancelable(false);
        window.setContentView(view);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_title"));
        btnSubmitNetwork = (Button) findViewById(ResourceUtil.getId(mContext, "btn_submit_network"));
        btnSwitchAccount = (Button) findViewById(ResourceUtil.getId(mContext, "btn_switch_account"));
//        textTitle = (TextView) findViewById(ResourceUtil.getId(mContext, "text_title"));
//        icTrue = (ImageView) findViewById(ResourceUtil.getId(mContext, "ic_true"));
//        pbLoading = (ProgressBar) findViewById(ResourceUtil.getId(mContext, "pb_loading"));
//        icWarning = (ImageView) findViewById(ResourceUtil.getId(mContext, "ic_warning"));
//        textContent = (TextView) findViewById(ResourceUtil.getId(mContext, "text_content"));
//        btnFailed = (Button) findViewById(ResourceUtil.getId(mContext, "btn_failed"));
//        btnFailed.setOnClickListener(this);
        btnSubmitNetwork.setOnClickListener(this);
        btnSwitchAccount.setOnClickListener(this);

    }

    /**
     * 设置对话框内同
     *
     * @param title 标题
     *              //     * @param tips  提示
     */
    public void setContent(String title) {
        this.title = title;
//        this.tips = tips;
    }

    /**
     * 设置自定义标题
     * <p>
     * //     * @param style
     */
//    public void setStyleTitle(SpannableStringBuilder style) {
//        this.styleTitle = style;
//    }

public void show(int taskId){
    super.show();
//    this.taskId = taskId;
    if (title != null)
        tvTitle.setText(title);
    if (taskId == 1){
        btnSwitchAccount.setVisibility(View.VISIBLE);
    }

}

//    @Override
//    public void show() {
//        super.show();
//        {// 动态改变对话框内容
//            if (title != null)
//                tvTitle.setText(title);
////            if (styleTitle != null)
////                textTitle.setText(styleTitle);
////            if (tips != null)
////                textTitle.setText(tips);
////            pbLoading.setVisibility(View.VISIBLE);
////            icTrue.setVisibility(View.GONE);
////            icWarning.setVisibility(View.GONE);
////            btnFailed.setVisibility(View.GONE);
//        }
//    }

//    @SuppressLint("InvalidR2Usage")
//    @OnClick({R2.id.btn_failed})
//    public void onViewClicked(View view) {
//        int i = view.getId();
//        if (i == R.id.btn_failed){
//            dismiss();
//        }
////        switch (view.getId()) {
////            case R.id.btn_failed:
////                dismiss();
////                break;
////
////        }
//    }

    /**
     * 响应成功调用
     */
    public void onSuccess(String title) {
//        this.taskId = taskId;
        this.title = title;
        doMessage(0);

    }
//    public void onSuccess(JyResponse response, String tips){
//        this.resp = response;
//        doMessage(0);
//    }

    /**
     * 响应失败调用
     * <p>
     * //     * @param taskId 任务ID 用于判断对话框对应的任务
     * //     * @param tips   失败后的提示信息
     */
    public void onFailed(String title) {
        this.title = title;
        doMessage(1);
    }

    //
    private void doMessage(int i) {
        Message msg = new Message();

        msg.what = i;
        mHandler.sendMessage(msg);
    }

    public void setOnSuccessListener(OnSuccessListener listener) {
        onSuccessListener = listener;
    }
//
//    public void setOnFailedListener(OnFailedListener listener) {
//        onFailedListener = listener;
//    }

    @Override
    public void onClick(View v) {
        int btn_submit_network = ResourceUtil.getId(mContext, "btn_submit_network");
        int btn_switch_account = (ResourceUtil.getId(mContext, "btn_switch_account"));
        if (btn_submit_network == v.getId()){
            dismiss();
        }else if (btn_switch_account == v.getId()){
            Log.d("eeeee", "btn_switch_account==============");
            btnSwitchAccount.setEnabled(false);
            Log.d("eeeee", "btn_switch_account====￥￥4444444=========");
            cancel();
            dismiss();
            new JyAppRequest(mContext, new JyAppRequestListener() {
                @Override
                public void onReqSuccess(Object obj) {
                    JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_SUCCESS, true);
                    ZSDK.getInstance().onDestroy();
//                    finish();

                }

                @Override
                public void onReqFailed(String errorMsg) {
                    JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_FAILED, true);

                }
            }).logoutRequest(JyUser.getInstance().getUsername());
        }
//        int i = view.getId();
//        int btn_failed = ResourceUtil.getId(mContext, "btn_failed");
//        if (btn_failed == R.id.btn_failed){
//            dismiss();
    }

    /**
     * 响应成功回调接口
     *
     * @author guangqing_lu
     */
    public interface OnSuccessListener {
        public void onSuccess();
    }
}


//
//    /**
//     * 响应失败回调接口
//     *
//     * @author guangqing_lu
//     */
//    public interface OnFailedListener {
//        public void onFailed(int teskId);
//    }

//}
