package com.you9.gamesdk.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class JyUser {

    @Transient
    private static JyUser jyUser;
    @Transient
    private boolean isLogin;        //是否走久游登录体系     true 走久游登录体系  false 不走久游登录体系，调用绑定接口

    @Id
    private Long id;
    private String userID;
    private String username;
    private String nickname;
    private String passwordHash;
    private String gendar;
    private String regDate;
    private String regTime;
    private String sID;
    private String pwdTension;
    private String isMain;
    private String isToken;
    private String fcm;
    private String idcard;
    private String isTrialUser;
    private String isVIPUser;
    private String userKey;
    private String password;

    private boolean isAutoLogin;    //是否自动登录  true-自动登录  false-不自动登录


    @Generated(hash = 911463408)
    public JyUser(Long id, String userID, String username, String nickname,
            String passwordHash, String gendar, String regDate, String regTime, String sID,
            String pwdTension, String isMain, String isToken, String fcm, String idcard,
            String isTrialUser, String isVIPUser, String userKey, String password,
            boolean isAutoLogin) {
        this.id = id;
        this.userID = userID;
        this.username = username;
        this.nickname = nickname;
        this.passwordHash = passwordHash;
        this.gendar = gendar;
        this.regDate = regDate;
        this.regTime = regTime;
        this.sID = sID;
        this.pwdTension = pwdTension;
        this.isMain = isMain;
        this.isToken = isToken;
        this.fcm = fcm;
        this.idcard = idcard;
        this.isTrialUser = isTrialUser;
        this.isVIPUser = isVIPUser;
        this.userKey = userKey;
        this.password = password;
        this.isAutoLogin = isAutoLogin;
    }


    @Generated(hash = 1093068585)
    public JyUser() {
    }


    public static JyUser getInstance() {
        synchronized (JyUser.class) {
            if (jyUser == null) {
                jyUser = new JyUser();
            }
        }
        return jyUser;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getUserID() {
        return this.userID;
    }


    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getUsername() {
        return this.username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getNickname() {
        return this.nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getPasswordHash() {
        return this.passwordHash;
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }


    public String getGendar() {
        return this.gendar;
    }


    public void setGendar(String gendar) {
        this.gendar = gendar;
    }


    public String getRegDate() {
        return this.regDate;
    }


    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }


    public String getRegTime() {
        return this.regTime;
    }


    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }


    public String getSID() {
        return this.sID;
    }


    public void setSID(String sID) {
        this.sID = sID;
    }


    public String getPwdTension() {
        return this.pwdTension;
    }


    public void setPwdTension(String pwdTension) {
        this.pwdTension = pwdTension;
    }


    public String getIsMain() {
        return this.isMain;
    }


    public void setIsMain(String isMain) {
        this.isMain = isMain;
    }


    public String getIsToken() {
        return this.isToken;
    }


    public void setIsToken(String isToken) {
        this.isToken = isToken;
    }


    public String getFcm() {
        return this.fcm;
    }


    public void setFcm(String fcm) {
        this.fcm = fcm;
    }


    public String getIdcard() {
        return this.idcard;
    }


    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }


    public String getIsTrialUser() {
        return this.isTrialUser;
    }


    public void setIsTrialUser(String isTrialUser) {
        this.isTrialUser = isTrialUser;
    }


    public String getIsVIPUser() {
        return this.isVIPUser;
    }


    public void setIsVIPUser(String isVIPUser) {
        this.isVIPUser = isVIPUser;
    }


    public String getUserKey() {
        return this.userKey;
    }


    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }


    public String getPassword() {
        return this.password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAutoLogin() {
        return isAutoLogin;
    }

    public void setAutoLogin(boolean autoLogin) {
        isAutoLogin = autoLogin;
    }


    public boolean getIsAutoLogin() {
        return this.isAutoLogin;
    }


    public void setIsAutoLogin(boolean isAutoLogin) {
        this.isAutoLogin = isAutoLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }


    public boolean getIsLogin() {
        return this.isLogin;
    }


    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
