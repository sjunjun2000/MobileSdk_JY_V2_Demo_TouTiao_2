package com.you9.gamesdk.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.util.JyConstants;
import com.you9.gamesdk.util.ResourceUtil;


public class JyRegisterActivity extends TabActivity implements View.OnClickListener {


//    @BindView(R2.id.btn_title_back)
    private Button btnTitleBack;

    private Class<?> mTabActivityArray[];


    private String mTextArray[];
    private TabHost tabHost;
    private TabWidget tabWidget;
    private String[] tags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.jy_activity_register);

        setContentView(ResourceUtil.getLayoutId(this, "jy_activity_register"));
//        ButterKnife.bind(JyRegisterActivity.this);


        btnTitleBack = (Button) findViewById(ResourceUtil.getId(this, "btn_title_back"));
        btnTitleBack.setOnClickListener(this);
        tabHost = getTabHost();
        tabWidget = getTabWidget();
        tabWidget.setBackgroundColor(Color.BLACK);
        tabHost.setup();

        tags = new String[] {"Tel", "Uname"};
        mTextArray = new String[]{getString(ResourceUtil.getStringId(this, "jy_register_by_username")), getString(ResourceUtil.getStringId(this, "jy_register_by_telephone"))};

//        mTabActivityArray = new Class<?>[]{JyTelRegisterActivity.class, JyUserNameRegisterActivity.class};
        mTabActivityArray = new Class<?>[]{JyUserNameRegisterActivity.class, JyTelRegisterActivity.class};


        for (int i = 0; i < mTextArray.length; i++) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(ResourceUtil.getLayoutId(this, "jy_tab_register"), null);
            TextView textView = (TextView) view.findViewById(ResourceUtil.getId(this, "text_register_tab"));

            textView.setText(mTextArray[i]);
            TabHost.TabSpec spec = tabHost.newTabSpec(tags[i]);
            spec.setIndicator(view);
            spec.setContent(getTabItemIntent(i));
            tabHost.addTab(spec);

        }
        tabHost.setCurrentTab(0);


    }

    /*
     * 给tab设置每个选项的内容，每个内容就是一个actiity
     * */
    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent(JyRegisterActivity.this, mTabActivityArray[index]);
        return intent;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case JyConstants.REGISTER_TEL_RES_CODE:
                finish();

                break;
        }
    }

    @Override
    public void onClick(View v) {

        int btn_title_back = ResourceUtil.getId(this, "btn_title_back");
        if (v.getId() == btn_title_back){
            JyPlatform.mListener.register(JyGameSdkStatusCode.REGISTER_CANCEL, null);
            finish();
        }
    }
}
