package com.you9.gamesdk.bean;

public class JyWxH5Info {

	private static JyWxH5Info jyWxInfo;
	private String appid;
	private String mweb_url;


//	public static JyWxH5Info getInstance() {
//		synchronized (JyWxH5Info.class) {
//			if (jyWxInfo == null) {
//				jyWxInfo = new JyWxH5Info();
//			}
//		}
//		return jyWxInfo;
//	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMweb_url() {
		return mweb_url;
	}

	public void setMweb_url(String mweb_url) {
		this.mweb_url = mweb_url;
	}
}
