package com.you9.gamesdk.activity;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.enums.JyStateCode;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.ResourceUtil;


public class JyWebViewActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvBack;
    private WebView webView;
    private String gnType;
    private ProgressDialog progressBar;
    private String pageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_kf"));

        gnType = getIntent().getStringExtra("gnType");
        Log.d("eeeee", "gnType=" + gnType);

        initView();

        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        progressBar = ProgressDialog.show(JyWebViewActivity.this, getResources().getString(ResourceUtil.getStringId(JyWebViewActivity.this, "jy_kf_loading")),
                getResources().getString(ResourceUtil.getStringId(JyWebViewActivity.this, "jy_kf_loading")));

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("about:blank")) {
                    finish();
                    return false;
                } else {
                    view.loadUrl(url);
                }
                return true;

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });
        if (gnType.equals(JyStateCode.GN_TYPE_KF.getCode())) {
            pageUrl = JySdkConfigInfo.getInstance().getKfUrl();
        } else if (gnType.equals(JyStateCode.GN_TYPE_ZX.getCode())) {
            pageUrl = JyConstants.WEBVIEW_ZX_URL;
        } else if (gnType.equals(JyStateCode.GN_TYPE_FORGET_PASSWORD.getCode())) {
            pageUrl = JyConstants.WEBVIEW_FORGET_PASSWORD_URL;
        } else if (gnType.equals(JyStateCode.GN_TYPE_PROTOCOL.getCode())) {
            pageUrl = JyConstants.WEBVIEW_JY_PROTOCOL_PATH;
        } else if (gnType.equals(JyStateCode.GN_TYPE_YLH.getCode())) {
            pageUrl = JyConstants.WEBVIEW_YLH_URL + "ticket=" + JyUser.getInstance().getUserKey() + "&game=" + JySdkConfigInfo.getInstance().getClientId();

        }
        webView.loadUrl(pageUrl);


    }

    private void initView() {


        tvBack = (TextView) findViewById(ResourceUtil.getId(
                this, "tv_back"));
        webView = (WebView) findViewById(ResourceUtil.getId(
                this, "webView"));
        tvBack.setOnClickListener(this);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int tv_back = ResourceUtil.getId(this, "tv_back");
        if (v.getId() == tv_back) {
            finish();
        }

    }
}
