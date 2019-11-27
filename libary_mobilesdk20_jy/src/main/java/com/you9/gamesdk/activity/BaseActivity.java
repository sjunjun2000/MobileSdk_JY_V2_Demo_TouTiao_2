package com.you9.gamesdk.activity;

import android.app.Activity;

import com.you9.gamesdk.bean.JyPlatformSettings;
import com.you9.gamesdk.enums.JyPromotionApiUploadType;
import com.you9.gamesdk.util.JyUtils;

public class BaseActivity extends Activity {


    @Override
    protected void onResume() {
        super.onResume();

        setRequestedOrientation(JyPlatformSettings.getInstance().getScreenOrientation());

//        TeaAgent.onResume(this);

        JyUtils.touTiaoDataUpload(this, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONRESUME.getCode(), "", false, "");


    }

    @Override
    protected void onPause() {
        super.onPause();

//        TeaAgent.onPause(this);
        JyUtils.touTiaoDataUpload(this, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_ONPAUSE.getCode(), "", false, "");
    }
}
