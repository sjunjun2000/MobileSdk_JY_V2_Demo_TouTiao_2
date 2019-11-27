package com.you9.gamesdk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.you9.gamesdk.R;
import com.you9.gamesdk.bean.JyPaymentInfo;
import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.enums.JyPayTypeCode;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.JyUtils;
import com.you9.gamesdk.util.ResourceUtil;

public class JyPayResultActivity extends BaseActivity implements View.OnClickListener {


    private ImageView ivIcon;
    private TextView tvGameName;
    private TextView tvPrice;
    private TextView tvPayGameName;
    private TextView tvPayType;
    private TextView tvCreateTime;
    private TextView tvOrderId;
    private TextView tvKfTel;
    private Button btnHistory;
    private Button btnPayFinish;
    private JyPaymentInfo jyPaymentInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jy_activity_payresult);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_payresult"));
        jyPaymentInfo = (JyPaymentInfo) getIntent().getSerializableExtra("jyPaymentInfo");



        initView();
        initData();
    }

    private void initView(){
        ivIcon = (ImageView) findViewById(ResourceUtil.getId(this, "ivIcon"));
        tvGameName = (TextView) findViewById(ResourceUtil.getId(this, "tvGameName"));
        tvPrice = (TextView) findViewById(ResourceUtil.getId(this, "tvPrice"));
        tvPayGameName = (TextView) findViewById(ResourceUtil.getId(this, "tvPayGameName"));
        tvPayType = (TextView) findViewById(ResourceUtil.getId(this, "tvPayType"));
        tvCreateTime = (TextView) findViewById(ResourceUtil.getId(this, "tvCreateTime"));
        tvOrderId = (TextView) findViewById(ResourceUtil.getId(this, "tvOrderId"));
        tvKfTel = (TextView) findViewById(ResourceUtil.getId(this, "tvKfTel"));
        btnHistory = (Button) findViewById(ResourceUtil.getId(this, "btnHistory"));
        btnPayFinish = (Button) findViewById(ResourceUtil.getId(this, "btnPayFinish"));
        btnHistory.setOnClickListener(this);
        btnPayFinish.setOnClickListener(this);
    }

    private void initData(){
        Log.d("eeeee", "initData");
        ivIcon.setImageDrawable(JyPlatformSettings.getInstance().getGameIcon());
        tvGameName.setText(JySdkConfigInfo.getInstance().getGameName());
        tvPrice.setText(String.format(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_pay_price")), JyUtils.convertFenToYuan(jyPaymentInfo.getPrice())));

        tvPayGameName.setText(jyPaymentInfo.getGameName());

        if (jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_ZFB.getCode())){
            tvPayType.setText(JyPayTypeCode.PAY_TYPE_NAME_ZFB.getCode());
        }else if (jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode()) || jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_SZF.getCode())){
            tvPayType.setText(JyPayTypeCode.PAY_YTPE_NAME_WX.getCode());
        }

        tvCreateTime.setText(jyPaymentInfo.getPayTime());
        tvOrderId.setText(jyPaymentInfo.getOrderId());
        tvKfTel.setText(String.format(getResources().getString(ResourceUtil.getStringId(this, "jy_activity_kftel")), jyPaymentInfo.getKfTel()));



    }

    @Override
    public void onClick(View v) {
        int btn_history = ResourceUtil.getId(this, "btnHistory");
        int btn_pay_finish = ResourceUtil.getId(this, "btnPayFinish");
        if (btn_history == v.getId()){
            startActivity(new Intent(JyPayResultActivity.this, JyHistoryOrderActivity.class));

        }else if (btn_pay_finish == v.getId()){
            finishPage();
        }

    }

//    @OnClick({R.id.btnHistory, R.id.btnPayFinish})
//    public void onViewClicked(View view) {
//        switch (view.getId()){
//            case R.id.btnHistory:
//                startActivity(new Intent(JyPayResultActivity.this, JyHistoryOrderActivity.class));
//
//                break;
//
//            case R.id.btnPayFinish:
//                finishPage();
//
//                break;
//        }
//
//    }

    //    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishPage();

        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishPage(){
        Bundle bundle = new Bundle();
        bundle.putString("payType", jyPaymentInfo.getPayType());
        if (jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_ZFB.getCode())){
            bundle.putString("resCode", "9000");
            setResult(JyConstants.PAY_ZFB_RES_CODE, getIntent().putExtra("payResult", bundle));
        }else if (jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_GF.getCode()) || jyPaymentInfo.getPayType().equals(JyPayTypeCode.PAY_TYPE_WX_SZF.getCode())){
            bundle.putString("resCode", "0");
            setResult(JyConstants.PAY_WXGF_RES_CODE, getIntent().putExtra("payResult", bundle));
        }
        finish();
    }


}
