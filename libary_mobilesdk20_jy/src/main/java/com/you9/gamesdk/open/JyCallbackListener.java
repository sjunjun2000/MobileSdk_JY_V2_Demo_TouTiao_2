package com.you9.gamesdk.open;

import com.you9.gamesdk.bean.JyRegisterInfo;
import com.you9.gamesdk.bean.JySdkConfigInfo;
import com.you9.gamesdk.bean.JyUser;

public interface JyCallbackListener {

    public void init(int statusCode, JySdkConfigInfo jySdkConfigInfo);
    public void login(int statusCode, JyUser jyUser);
    public void register(int statusCode, JyRegisterInfo jyRegisterInfo);
    public void pay(int statusCode);
    public void logout(int statusCode, boolean isSwitchAccount);
    public void dataUpload(int statusCode);
    public void accountRecord(int statusCode);


}
