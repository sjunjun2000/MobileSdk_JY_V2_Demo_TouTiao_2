package com.caozaolin.mreactfloatview.view;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.caozaolin.mreactfloatview.listener.FvItemClickListener;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class GameAssistApi {

    public final String TAG = "GameAssistApi";
    private Handler handler;
    private GameAssistWindowManager mGameAssistWindowManager;
    private Activity mActivity;
    private boolean isShowAssist = true;
    private FvItemClickListener mListener;

    // public static boolean isAllow = false; // 标志位，是否允许创建悬浮窗
    public GameAssistApi(Activity mActivity, FvItemClickListener listener) {
        this.mActivity = mActivity;
        this.mListener = listener;
        this.handler = new Handler(Looper.getMainLooper());
        // askforPermission();
        // Log.i(TAG, "________________在构造函数中1_________________ : isAllow : " + isAllow);
        if ((this.isShowAssist) && (this.mGameAssistWindowManager == null)) {
            // Log.i(TAG, "________________在构造函数中2_________________ : isAllow : " + isAllow);
            this.mGameAssistWindowManager = new GameAssistWindowManager(mActivity, mListener);
            this.mGameAssistWindowManager.createSmallWindow();
        }
    }


    public void onPause() {
        if (this.mGameAssistWindowManager != null) {
            this.mGameAssistWindowManager.removeBigWindow();
            this.mGameAssistWindowManager.removeSmallWindow();

            this.mGameAssistWindowManager.removeBrandWindow();
            this.mGameAssistWindowManager = null;
        }
        System.gc();
    }

    public void onResume() {
        if (this.isShowAssist) {
            if (this.mGameAssistWindowManager == null) {
                this.mGameAssistWindowManager = new GameAssistWindowManager(this.mActivity, mListener);
            }
            if (!this.mGameAssistWindowManager.isWindowShowing()) {
                this.mGameAssistWindowManager.createSmallWindow();
            }
        }
    }
}
