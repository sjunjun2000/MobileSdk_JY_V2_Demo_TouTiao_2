package com.you9.gamesdk;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.enums.JyPromotionApiUploadType;
import com.you9.gamesdk.util.JyUtils;

public class BaseApp extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        JyUtils.getSdkConfigInfo(mContext);
        Log.d("eeeee", "appTYpe=" + JySdkConfigInfo.getInstance().getReportType());

        JyUtils.touTiaoDataUpload(mContext, JyPromotionApiUploadType.PROMOTION_API_UPLOAD_TYPE_INIT.getCode(), "", false, "");


    }
}
