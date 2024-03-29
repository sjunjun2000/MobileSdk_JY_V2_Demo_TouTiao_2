package com.you9.gamesdk.bean;

public class JyWxInfo {

	private static JyWxInfo jyWxInfo;
	private String appId;
	private String nonceStr;
	private String packageValue;
	private String partnerId;
	private String prepayId;
	private String sign;
	private String timeStamp;


	public static JyWxInfo getInstance() {
		synchronized (JyWxInfo.class) {
			if (jyWxInfo == null) {
				jyWxInfo = new JyWxInfo();
			}
		}
		return jyWxInfo;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getPackageValue() {
		return packageValue;
	}

	public void setPackageValue(String packageValue) {
		this.packageValue = packageValue;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
