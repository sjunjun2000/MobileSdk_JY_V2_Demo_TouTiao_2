package com.you9.gamesdk.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.you9.gamesdk.bean.JyAlipayResult;
import com.you9.gamesdk.bean.JyPayType;
import com.you9.gamesdk.bean.JyPaymentInfo;
import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.bean.JyResponse;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyWxH5Info;
import com.you9.gamesdk.dialog.JyLoadingDialog;
import com.you9.gamesdk.enums.JyPayTypeCode;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JyPayTypeActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageView ivIcon;
    private TextView tvGameName;
    private TextView tvTradeName;
    private TextView tvPrice;
    private RadioButton rbAlipay;
    private RadioButton rbWxGf;
    private RadioButton rbWxH5;
    private RadioGroup rgPayType;
    private Button btnPay;
    private Button btnHistory;
    private Button btnBack;

    private Bundle bundle;


    private long mExitTime;
    //    private BaseResp resp;
    private WebView wbWxpay;
    private Map<String, String> extraHeaders;


    private JyPaymentInfo jyPaymentInfo;
    private String payType;
    //    public static JyPayResultListener mListener;
    private JyAlipayResult payResult;
    private Dialog loadingDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case JyConstants.SDK_PAY_FLAG:
                    payResult = new JyAlipayResult((Map<String, String>) msg.obj);
                    jyPaymentInfo.setPayStatus(payResult.getResultStatus());
                    if (!payResult.getResultStatus().equals("9000")) {
//                        支付失败

                        payFailedDialog(payResult.getResultStatus());

                    } else {

//                        goPayResultPage(JyConstants.PAY_ZFB_RES_CODE);

                        startActivity(new Intent(JyPayTypeActivity.this, JyAlipayResultActivity.class));
                    }


                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_paytype"));
        jyPaymentInfo = (JyPaymentInfo) getIntent().getSerializableExtra("jyPaymentInfo");

        initView();

        bundle = new Bundle();
        initData();

        new JyAppRequest(JyPayTypeActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                List<JyPayType> payTypeList = (List<JyPayType>) obj;
                for (JyPayType payType : payTypeList) {
                    if (payType.getPay_type().equals(JyPayTypeCode.PAY_TYPE_ZFB.getCode())) {
                        //支付宝支付
                        rbAlipay.setVisibility(View.VISIBLE);

                    } else if (payType.getPay_type().equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode())) {
                        //微信官方支付
                        rbWxGf.setVisibility(View.VISIBLE);

                    } else if (payType.getPay_type().equals(JyPayTypeCode.PAY_TYPE_WX_SZF.getCode())) {
                        //微信神州付支付
                        rbWxH5.setVisibility(View.VISIBLE);

                    }


                }

                if (rbWxGf.getVisibility() == View.VISIBLE) {
                    rbWxGf.setChecked(true);
                    payType = JyPayTypeCode.PAY_TYPE_WX_GF.getCode();
                } else if (rbAlipay.getVisibility() == View.VISIBLE) {
                    rbAlipay.setChecked(true);
                    payType = JyPayTypeCode.PAY_TYPE_ZFB.getCode();
                }

            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        }).getPayTypeRequest(jyPaymentInfo);


    }

    private void initView() {
        ivIcon = (ImageView) findViewById(ResourceUtil.getId(this, "ivIcon"));
        tvGameName = (TextView) findViewById(ResourceUtil.getId(this, "tvGameName"));
        tvTradeName = (TextView) findViewById(ResourceUtil.getId(this, "tvTradeName"));
        tvPrice = (TextView) findViewById(ResourceUtil.getId(this, "tvPrice"));
        rbAlipay = (RadioButton) findViewById(ResourceUtil.getId(this, "rbAlipay"));
        rbWxGf = (RadioButton) findViewById(ResourceUtil.getId(this, "rbWxGf"));
        rbWxH5 = (RadioButton) findViewById(ResourceUtil.getId(this, "rbWxH5"));
        rgPayType = (RadioGroup) findViewById(ResourceUtil.getId(this, "rgPayType"));
        btnPay = (Button) findViewById(ResourceUtil.getId(this, "btnPay"));
        btnHistory = (Button) findViewById(ResourceUtil.getId(this, "btnHistory"));
        btnBack = (Button) findViewById(ResourceUtil.getId(this, "btnBack"));
        wbWxpay = (WebView) findViewById(ResourceUtil.getId(this, "wb_wxpay"));
        btnPay.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        rgPayType.setOnCheckedChangeListener(this);
        extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Referer", JyConstants.WX_PAY_REFERER);
        wbWxpay.getSettings().setJavaScriptEnabled(true);
        wbWxpay.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings webSettings = wbWxpay.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        wbWxpay.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("eeeee", "shouleUrl=" + url);

                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } else {
                    view.loadUrl(url, extraHeaders);

                }

                return true;


            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    private void initData() {
        ivIcon.setImageDrawable(JyPlatformSettings.getInstance().getGameIcon());
//        ivIcon.setImageBitmap(JyUtils.bytesToBitmap(JyPlatformSettings.getInstance().getGameIcon()));
        tvGameName.setText(JySdkConfigInfo.getInstance().getGameName());
        tvTradeName.setText(jyPaymentInfo.getSubject());
        tvPrice.setText(String.format(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_amount")), JyUtils.convertFenToYuan(jyPaymentInfo.getPrice())));
        btnPay.setText(String.format(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_pay")), JyUtils.convertFenToYuan(jyPaymentInfo.getPrice())));


    }

    @Override
    public void onClick(View v) {
        int btn_Pay = ResourceUtil.getId(this, "btnPay");
        int btn_History = ResourceUtil.getId(this, "btnHistory");
        int btn_Back = ResourceUtil.getId(this, "btnBack");
        if (btn_Pay == v.getId()) {
            btnPay.setEnabled(false);

            jyPaymentInfo.setOrderId(JyUtils.getOrderId_20());
            jyPaymentInfo.setPayType(payType);
            jyPaymentInfo.setPayTime(JyUtils.getNowTime(1));

            new JyAppRequest(JyPayTypeActivity.this, new JyAppRequestListener() {
                @Override
                public void onReqSuccess(Object obj) {
                    JyResponse resp = (JyResponse) obj;
                    if (payType.equals(JyPayTypeCode.PAY_TYPE_ZFB.getCode())) {
                        Log.d("eeeee", "创建订单成功，准备支付，res:" + resp.getState());
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                // 构造PayTask 对象
                                PayTask alipay = new PayTask(JyPayTypeActivity.this);
                                // 调用支付接口，获取支付结果
                                Map<String, String> result = alipay.payV2(resp.getDesc(), true);
                                Message msg = new Message();
                                msg.what = JyConstants.SDK_PAY_FLAG;
                                msg.obj = result;
                                mHandler.sendMessage(msg);


                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    } else if (payType.equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode())) {
                        if (JyUtils.isAppAvilible(JyPayTypeActivity.this, JyConstants.PACKAGE_NAME_WX, JyConstants.WXPay_JY_VERSION_CODE)) {
                            JyResponse response = (JyResponse) obj;
                            Log.d("eeeee", "desc=" + response.getDesc());
//                        JyWxH5Info info = JyWxH5Info.getInstance();
                            JyWxH5Info info = JSON.parseObject(response.getDesc(), JyWxH5Info.class);
                            Log.d("eeeee", "url=" + info.getMweb_url());

                            wbWxpay.loadUrl(info.getMweb_url(), extraHeaders);

                        } else {
                            Toast.makeText(JyPayTypeActivity.this, getResources().getString(ResourceUtil.getStringId(JyPayTypeActivity.this, "jy_activity_install_wx_tips")), Toast.LENGTH_SHORT).show();
                            btnPay.setEnabled(true);
                        }

                    }

                }

                @Override
                public void onReqFailed(String errorMsg) {
                    Toast.makeText(JyPayTypeActivity.this, getString(ResourceUtil.getStringId(JyPayTypeActivity.this, "jy_activity_create_order_error")), Toast.LENGTH_SHORT).show();
//                        App.getInstance().unregisterActivityLifecycleCallbacks(App.activityLifecycleCallbacks);
                    finishPage();

                }
            }).createOrderRequest(jyPaymentInfo);
        } else if (btn_History == v.getId()) {
            startActivity(new Intent(JyPayTypeActivity.this, JyHistoryOrderActivity.class));
        } else if (btn_Back == v.getId()) {
            cancelPay();
        }

    }

    private void goPayResultPage(int payType) {
        Log.d("eeeee", "goPayResultPage");
        JyLoadingDialog.closeDialog(loadingDialog);
        btnPay.setEnabled(true);
        startActivityForResult(new Intent(JyPayTypeActivity.this, JyPayResultActivity.class).putExtra("jyPaymentInfo", jyPaymentInfo), payType);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(JyConstants.PAYF_RES_CODE, data);
        Log.d("eeeee", "onActivityResult");
        finishPage();
    }

    private void payFailedDialog(String errorCode) {
        Log.d("eeeee", "payFailedDialog");
        JyLoadingDialog.closeDialog(loadingDialog);
        btnPay.setEnabled(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(JyPayTypeActivity.this);
        builder.setTitle(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_tips")));
        builder.setMessage(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_pay_failed")));
        builder.setPositiveButton(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_pay_sure")), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                bundle.putString("payType", jyPaymentInfo.getPayType());
                bundle.putString("resCode", String.valueOf(errorCode));

                setResult(JyConstants.PAYF_RES_CODE, getIntent().putExtra("payResult", bundle));
                Log.d("eeeee", "payFailedDialog");
                finishPage();
            }
        });
        builder.setCancelable(false);
        builder.show();




    }


    //    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(JyPayTypeActivity.this, getResources().getString(ResourceUtil.getStringId(this, "jy_activity_pay_exit")), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            cancelPay();
        }
    }

    private void cancelPay() {
        bundle.putString("payType", "cancelPay");
        bundle.putString("resCode", JyConstants.PAY_CANCEL_RES_CODE);
        setResult(JyConstants.PAYF_RES_CODE, getIntent().putExtra("payResult", bundle));
        finishPage();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("eeeee", "onResume");
        Log.d("eeeee", "payType=" + jyPaymentInfo.getPayType());
//        Log.d("eeeee", "OnResumeApp.isForeground=" + App.isForeground);

        if (!btnPay.isEnabled() && jyPaymentInfo.getPayType() != null && jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode())) {
            Log.d("eeeee", "进入过后台");
            loadingDialog = JyLoadingDialog.JyLoadingDialog(this, getResources().getString(ResourceUtil.getStringId(JyPayTypeActivity.this, "jy_activity_loading_text")));
            //查询订单
            //通知订单状态
            new JyAppRequest(JyPayTypeActivity.this, new JyAppRequestListener() {
                @Override
                public void onReqSuccess(Object obj) {

                    goPayResultPage(JyConstants.PAY_WXGF_RES_CODE);

//                    resp.errCode = 0;
//                    JyPayTypeActivity.mListener.wxPayResult(resp);

                }

                @Override
                public void onReqFailed(String errorMsg) {
//                    resp.errCode = 1;
//                    JyPayTypeActivity.mListener.wxPayResult(resp);
                    payFailedDialog(1 + "");

                }
            }).wxPayResultRequest(jyPaymentInfo.getOrderId());

        } else if (payResult != null && payResult.getResultStatus().equals("9000") && !btnPay.isEnabled() && jyPaymentInfo.getPayType() != null && jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_ZFB.getCode())) {
            loadingDialog = JyLoadingDialog.JyLoadingDialog(this, getResources().getString(ResourceUtil.getStringId(JyPayTypeActivity.this, "jy_activity_loading_text")));
            goPayResultPage(JyConstants.PAY_ZFB_RES_CODE);
        }
    }

    private void finishPage() {
        Log.d("eeeee", "finishPage");
        jyPaymentInfo = null;
        Log.d("eeeee", "clearJyPaymentInfo");
        finish();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int rb_alipay = ResourceUtil.getId(this, "rbAlipay");
        int rb_wxgf = ResourceUtil.getId(this, "rbWxGf");
        int rb_wxh5 = ResourceUtil.getId(this, "rbWxH5");
        Log.d("eeeee", "rb_alipay=" + rb_alipay);
        Log.d("eeeee", "rb_wxgf=" + rb_wxgf);
        Log.d("eeeee", "rb_wxh5=" + rb_wxh5);
        Log.d("eeeee", "buttonViewId=" + group.getId());
        Log.d("eeeee", "checkedId=" + checkedId);


        if (rb_alipay == checkedId) {
//            if (ischanged) {
            Log.d("eeeee", "PAY_TYPE_ZFB");
            payType = JyPayTypeCode.PAY_TYPE_ZFB.getCode();
//            }
        } else if (rb_wxgf == checkedId) {
//            if (ischanged) {
            Log.d("eeeee", "PAY_TYPE_WX_GF");
            payType = JyPayTypeCode.PAY_TYPE_WX_GF.getCode();
//            }
        } else if (rb_wxh5 == checkedId) {
//            if (ischanged) {
            Log.d("eeeee", "PAY_TYPE_WX_SZF");
            payType = JyPayTypeCode.PAY_TYPE_WX_SZF.getCode();
//            }
        }
    }
}
