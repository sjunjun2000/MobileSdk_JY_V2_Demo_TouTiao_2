package com.you9.gamesdk.activity;

import android.os.Bundle;

import com.caozaolin.mreactfloatview.ZSDK;
import com.you9.gamesdk.JyPlatform;
import com.you9.gamesdk.bean.JyUser;
import com.you9.gamesdk.listener.JyAppRequestListener;
import com.you9.gamesdk.open.JyGameSdkStatusCode;
import com.you9.gamesdk.request.JyAppRequest;
import com.you9.gamesdk.util.ResourceUtil;

public class JyLogoutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtil.getLayoutId(JyLogoutActivity.this, "jy_activity_logout"));


        new JyAppRequest(JyLogoutActivity.this, new JyAppRequestListener() {
            @Override
            public void onReqSuccess(Object obj) {
                JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_SUCCESS, false);
                ZSDK.getInstance().onDestroy();
                finish();

            }

            @Override
            public void onReqFailed(String errorMsg) {
                JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_FAILED, false);
                finish();

            }
        }).logoutRequest(JyUser.getInstance().getUsername());




//        JyPlatform.mListener.logout(JyGameSdkStatusCode.LOGOUT_SUCCESS, false);
//        ZSDK.getInstance().onDestroy();
//        finish();
    }
}
