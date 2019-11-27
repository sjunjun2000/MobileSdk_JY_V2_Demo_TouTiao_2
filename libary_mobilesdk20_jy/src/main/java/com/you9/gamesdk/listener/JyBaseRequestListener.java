package com.you9.gamesdk.listener;

import com.you9.gamesdk.bean.JyResponse;

public interface JyBaseRequestListener {
    public void onBaseRequestSuccess(JyResponse response);

    public void onBaseRequestFailed(String errorMsg);
}
