package com.you9.gamesdk.bean;

import android.graphics.drawable.Drawable;

public class JyPlatformSettings {

    private static JyPlatformSettings jyPlatformSettings;
    private int ScreenOrientation;
    private int gameType;
    private Drawable gameIcon;


    public static JyPlatformSettings getInstance() {
        synchronized (JyPlatformSettings.class) {
            if (jyPlatformSettings == null) {
                jyPlatformSettings = new JyPlatformSettings();
            }
        }
        return jyPlatformSettings;
    }

    public int getScreenOrientation() {
        return ScreenOrientation;
    }

    public void setScreenOrientation(int screenOrientation) {
        ScreenOrientation = screenOrientation;
    }

    public int getGameType() {
        return gameType;
    }

    public void setGameType(int gameType) {
        this.gameType = gameType;
    }

    public Drawable getGameIcon() {
        return gameIcon;
    }

    public void setGameIcon(Drawable gameIcon) {
        this.gameIcon = gameIcon;
    }
}
