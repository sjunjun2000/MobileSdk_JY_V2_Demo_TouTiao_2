package com.you9.gamesdk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caozaolin.mreactfloatview.ZSDK;
import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.R;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.ResourceUtil;


public class JySwitchAccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView textTitle;
    private Button btnTitleBack;
    private TextView tvUsername;
    private ImageView ivExchange;
    private TextView tvKfTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jy_activity_switchaccount);

        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_switchaccount"));
        initView();
//        ButterKnife.bind(JySwitchAccountActivity.this);

        textTitle.setText(getString(ResourceUtil.getStringId(this, "jy_activity_switch_account_title")));

        tvUsername.setText(String.format(getString(ResourceUtil.getStringId(this, "jy_activity_passport")), JyUser.getInstance().getUsername()));
        tvKfTel.setText(String.format(getString(ResourceUtil.getStringId(this, "jy_activity_kf_tel")), JySdkConfigInfo.getInstance().getKfTel()));
    }

    private void initView(){

        textTitle = (TextView) findViewById(ResourceUtil.getId(this, "text_title"));
        btnTitleBack = (Button) findViewById(ResourceUtil.getId(this, "btn_title_back"));
        tvUsername = (TextView) findViewById(ResourceUtil.getId(this, "tv_username"));
        ivExchange = (ImageView) findViewById(ResourceUtil.getId(this, "iv_exchange"));
        tvKfTel = (TextView) findViewById(ResourceUtil.getId(this, "tv_kf_tel"));
        btnTitleBack.setOnClickListener(this);
        ivExchange.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int btn_title_back = ResourceUtil.getId(this, "btn_title_back");
        int iv_exchange = ResourceUtil.getId(this, "iv_exchange");
        if (btn_title_back == v.getId()){
            finish();
        }else if (iv_exchange == v.getId()){
            new JyAppRequest(JySwitchAccountActivity.this, new JyAppRequestListener() {
                @Override
                public void onReqSuccess(Object obj) {
                    JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_SUCCESS, true);
                    ZSDK.getInstance().onDestroy();
                    finish();

                }

                @Override
                public void onReqFailed(String errorMsg) {
                    JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_FAILED, true);

                }
            }).logoutRequest(JyUser.getInstance().getUsername());

        }

    }
}
