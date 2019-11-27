package com.you9.gamesdk.util;


import android.content.Context;
import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.you9.gamesdk.bean.JyUser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/***
 * 本地存储工具类
 *
 *
 */
public class PreferencesUtils {

    private final String PREFERENCES_NAME = "com_9you_login_preferences";
    private SharedPreferences pref;

    private Context mContext;

    public PreferencesUtils(Context context) {
        // TODO Auto-generated constructor stub
        pref = context.getSharedPreferences(PREFERENCES_NAME,
                Context.MODE_PRIVATE);

        mContext = context;
    }


    /**
     * 记录发送短信的时间点
     *
     * @param time
     */
    public void saveSMSSendTime(long time) {
        pref.edit().putLong("sms_send_time", time).commit();
    }

    /**
     * 取得最近一个发送短信的时间点
     *
     * @return
     */
    public long getSMSSendTime() {
        return pref.getLong("sms_send_time", 0L);
    }


    /**
     * 取出保存在本地的账户信息
     *
     * @return 所有账户信息
     */
    public List<JyUser> getAccounts() {
        String sUsers = pref.getString("remember_accounts", null);
        if (sUsers == null)
            return new ArrayList<JyUser>();
//        Type listType = new TypeToken<List<JyUser>>() {
//        }.getType();
//        List<JyUser> users = gson.fromJson(sUsers, listType);
        List<JyUser> users = JSON.parseArray(sUsers, JyUser.class);
        return users;
    }

    /**
     * 本地存储一个账户
     *
     * @param u
     *            账户
     */
    public void saveAccounts(JyUser u) {
        List<JyUser> users = getAccounts();
        Iterator<JyUser> iterator = users.iterator();
        while (iterator.hasNext()) {
            JyUser user = iterator.next();
            if (user.getUsername().equals(u.getUsername()))
                iterator.remove();
        }
        users.add(u);
        pref.edit().putString("remember_accounts", JSON.toJSONString(users)).commit();
    }

    /**
     * 将帐号从本地存储中删除
     *
     * @param u
     *            要删除的账户
     */
    public List<JyUser> removeAccount(JyUser u) {
        List<JyUser> users = getAccounts();
        Iterator<JyUser> iterator = users.iterator();
        while (iterator.hasNext()) {
            JyUser user = iterator.next();
            if (user.getUsername().equals(u.getUsername()))
                iterator.remove();
        }
        pref.edit().putString("remember_accounts", JSON.toJSONString(users)).commit();
        return users;
    }


}
