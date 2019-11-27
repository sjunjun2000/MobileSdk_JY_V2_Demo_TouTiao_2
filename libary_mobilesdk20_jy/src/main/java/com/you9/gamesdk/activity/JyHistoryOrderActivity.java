package com.you9.gamesdk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.you9.gamesdk.R;
import com.you9.gamesdk.adapter.JyHistoryOrderAdapter;
import com.you9.gamesdk.bean.JyOrder;
import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.bean.JyResponse;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;

public class JyHistoryOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView ivIcon;
    private TextView tvGameName;
    private Button btnRefresh;
    private ListView lvHistoryOrder;
    private ImageButton ibBack;
    private TextView tvHistoryOrderNone;



    private View mMoreButton;
    private int pageIndex = 0;
    private JyHistoryOrderAdapter adapter;
    private List<JyOrder> orderList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.jy_activity_history_order);
        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_history_order"));
        initView();
        initData();
    }

    private void initView(){

        ivIcon = (ImageView) findViewById(ResourceUtil.getId(this, "ivIcon"));
        tvGameName = (TextView) findViewById(ResourceUtil.getId(this, "tvGameName"));
        btnRefresh = (Button) findViewById(ResourceUtil.getId(this, "btnRefresh"));
        lvHistoryOrder = (ListView) findViewById(ResourceUtil.getId(this, "lvHistoryOrder"));
        ibBack = (ImageButton) findViewById(ResourceUtil.getId(this, "ibBack"));
        tvHistoryOrderNone = (TextView) findViewById(ResourceUtil.getId(this, "tvHistoryOrderNone"));


        mMoreButton = LayoutInflater.from(JyHistoryOrderActivity.this).inflate(
                ResourceUtil.getLayoutId(this, "jy_item_pay_more"), null);
        mMoreButton.setVisibility(View.GONE);
        mMoreButton.setOnClickListener(this);
        lvHistoryOrder.addFooterView(mMoreButton);
        btnRefresh.setOnClickListener(this);
        ibBack.setOnClickListener(this);
    }

    private void initData(){
        ivIcon.setImageDrawable(JyPlatformSettings.getInstance().getGameIcon());
        tvGameName.setText(JySdkConfigInfo.getInstance().getGameName());

        orderList = new ArrayList<JyOrder>();
        pageIndex = 0;
        adapter = new JyHistoryOrderAdapter(JyHistoryOrderActivity.this);
//        lvHistoryOrder.setAdapter(adapter);
        getHistoryOrderRequest();



    }



//    @OnClick({R.id.btnRefresh, R.id.ibBack})
//    public void onViewClicked(View view) {
//        switch (view.getId()){
//            case R.id.btnRefresh:
//                pageIndex = 0;
//                getHistoryOrderRequest();
//
//                break;
//
//            case R.id.ibBack:
//                finish();
//                break;
//
//        }
//
//
//    }


    @Override
    public void onClick(View v) {
        int btn_refresh = ResourceUtil.getId(this, "btnRefresh");
        int ib_back = ResourceUtil.getId(this, "ibBack");

        if (btn_refresh == v.getId()){
            pageIndex = 0;
            getHistoryOrderRequest();
        }else if (ib_back == v.getId()){
            finish();
        }else if (v.equals(mMoreButton)){
            pageIndex++;
            getHistoryOrderRequest();
//            new LookupTask().execute((Void) null);

        }

    }

    private void getHistoryOrderRequest(){

        new JyAppRequest(JyHistoryOrderActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyResponse response = (JyResponse) obj;
                switch (response.getState()){
                    case "0":
                        if (pageIndex == 0){
                            mMoreButton.setVisibility(View.VISIBLE);
                            Log.d("eeeee", "getDesc=" + response.getDesc());
                            orderList = JSON.parseArray(response.getDesc(), JyOrder.class);
                            Log.d("eeeee", "orderList=" + orderList);
                            Log.d("eeeee", "orderListSize=" + orderList.size());

                        }else {
                            for (JyOrder o: JSON.parseArray(response.getDesc(), JyOrder.class)) {
                                orderList.add(o);
                            }

                        }
                        adapter.setData(orderList);
                        lvHistoryOrder.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

//                                List<Order> orderList = JSON.parseArray(response.getDesc(), Order.class);

                        break;

                    case "3":
                        if (pageIndex == 0){
                            tvHistoryOrderNone.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(
                                    JyHistoryOrderActivity.this,
                                    getResources().getString(ResourceUtil.getStringId(JyHistoryOrderActivity.this, "jy_pay_lookup_activity_no_more")),
                                    Toast.LENGTH_LONG).show();
                        }



                        break;
                }


            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        }).getHistoryOrderRequest(pageIndex);
    }
}
