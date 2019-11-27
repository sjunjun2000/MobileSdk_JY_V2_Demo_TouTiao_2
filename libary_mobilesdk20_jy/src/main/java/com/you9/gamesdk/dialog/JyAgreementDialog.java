package com.you9.gamesdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.you9.gamesdk.util.ResourceUtil;


public class JyAgreementDialog extends Dialog implements View.OnClickListener {
    private ImageView ivBack;
    private ImageView ivClose;
    private WebView webAgreement;

    private Context mContext;
    private int protocolType;
    private String protocolUrl;
    private TextView tvYou9;


    public JyAgreementDialog(Context context, int protocolType) {
        super(context);
        this.mContext = context;
        this.protocolType = protocolType;

        View view = View.inflate(context, ResourceUtil.getLayoutId(context, "jy_dialog_agreement"), null);
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


    }

    private void initView() {
        ivBack = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivBack"));
        ivClose = (ImageView) findViewById(ResourceUtil.getId(mContext, "ivClose"));
        tvYou9 = (TextView) findViewById(ResourceUtil.getId(mContext, "tv_you9"));
        webAgreement = (WebView) findViewById(ResourceUtil.getId(mContext, "web_agreement"));

        ivBack.setVisibility(View.INVISIBLE);
        ivClose.setVisibility(View.VISIBLE);
        ivClose.setOnClickListener(this);


        if (protocolType == 0) {
            //用户协议
            protocolUrl = "file:///android_asset/agreement.html";
        } else if (protocolType == 1) {
            //用户隐私协议
            tvYou9.setVisibility(View.GONE);
            protocolUrl = "file:///android_asset/privateProtocol.html";
        }
        webAgreement.loadUrl(protocolUrl);


    }


    @Override
    public void onClick(View v) {
        int iv_close = ResourceUtil.getId(mContext, "ivClose");

        if (iv_close == v.getId()) {
            dismiss();
        }

    }


}
