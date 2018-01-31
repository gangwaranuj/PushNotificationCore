package com.saphire.iopush.bean;

public class CustomNotificationBean {

	private int customNotificationId = 0;
	private String title;
	private String message;
	private String logoPath;
	private boolean optIn;
	private String allowText;
	private String dontAllowText;
	private String websiteUrl;

	private String popupBackgroundColor;
	private String popupColor;
	private String allowBtnBackgroundColor;
	private String allowBtnColor;
	private String dontAllowBtnBackgroundColor;
	private String dontAllowBtnColor;
	private int delayTime;
	private boolean checkFlag;
	private String type;
	private String buttonType;
	private String deviceType;

	/**
	 * @return the buttonType
	 */
	public String getButtonType() {
		return buttonType;
	}



	/**
	 * @param buttonType the buttonType to set
	 */
	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}



	public CustomNotificationBean() {
		super();
	}



	public CustomNotificationBean(int customNotificationId, String title, String message, String logoPath,
			boolean optIn, String allowText, String dontAllowText, String websiteUrl, String popupBackgroundColor,
			String popupColor, String allowBtnBackgroundColor, String allowBtnColor, String dontAllowBtnBackgroundColor,
			String dontAllowBtnColor, int delayTime, boolean checkFlag, String type, String buttonType) {
		super();
		this.customNotificationId = customNotificationId;
		this.title = title;
		this.message = message;
		this.logoPath = logoPath;
		this.optIn = optIn;
		this.allowText = allowText;
		this.dontAllowText = dontAllowText;
		this.websiteUrl = websiteUrl;
		this.popupBackgroundColor = popupBackgroundColor;
		this.popupColor = popupColor;
		this.allowBtnBackgroundColor = allowBtnBackgroundColor;
		this.allowBtnColor = allowBtnColor;
		this.dontAllowBtnBackgroundColor = dontAllowBtnBackgroundColor;
		this.dontAllowBtnColor = dontAllowBtnColor;
		this.delayTime = delayTime;
		this.checkFlag = checkFlag;
		this.type = type;
		this.buttonType = buttonType;
	}


	
	
	public CustomNotificationBean(int customNotificationId, String title, String message, String logoPath,
			boolean optIn, String allowText, String dontAllowText, String websiteUrl, String popupBackgroundColor,
			String popupColor, String allowBtnBackgroundColor, String allowBtnColor, String dontAllowBtnBackgroundColor,
			String dontAllowBtnColor, int delayTime, boolean checkFlag, String type, String buttonType,
			String deviceType) {
		super();
		this.customNotificationId = customNotificationId;
		this.title = title;
		this.message = message;
		this.logoPath = logoPath;
		this.optIn = optIn;
		this.allowText = allowText;
		this.dontAllowText = dontAllowText;
		this.websiteUrl = websiteUrl;
		this.popupBackgroundColor = popupBackgroundColor;
		this.popupColor = popupColor;
		this.allowBtnBackgroundColor = allowBtnBackgroundColor;
		this.allowBtnColor = allowBtnColor;
		this.dontAllowBtnBackgroundColor = dontAllowBtnBackgroundColor;
		this.dontAllowBtnColor = dontAllowBtnColor;
		this.delayTime = delayTime;
		this.checkFlag = checkFlag;
		this.type = type;
		this.buttonType = buttonType;
		this.deviceType = deviceType;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}



	public int getCustomNotificationId() {
		return customNotificationId;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public boolean isOptIn() {
		return optIn;
	}


	public String getAllowText() {
		return allowText;
	}

	public String getDontAllowText() {
		return dontAllowText;
	}

	public void setCustomNotificationId(int customNotificationId) {
		this.customNotificationId = customNotificationId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
	}

	public void setOptIn(boolean optIn) {
		this.optIn = optIn;
	}

	public void setAllowText(String allowText) {
		this.allowText = allowText;
	}

	public void setDontAllowText(String dontAllowText) {
		this.dontAllowText = dontAllowText;
	}

	public String getPopupBackgroundColor() {
		return popupBackgroundColor;
	}

	public String getPopupColor() {
		return popupColor;
	}

	public String getAllowBtnBackgroundColor() {
		return allowBtnBackgroundColor;
	}

	public String getAllowBtnColor() {
		return allowBtnColor;
	}

	public String getDontAllowBtnBackgroundColor() {
		return dontAllowBtnBackgroundColor;
	}

	public String getDontAllowBtnColor() {
		return dontAllowBtnColor;
	}

	public int getDelayTime() {
		return delayTime;
	}

	public boolean isCheckFlag() {
		return checkFlag;
	}

	public void setPopupBackgroundColor(String popupBackgroundColor) {
		this.popupBackgroundColor = popupBackgroundColor;
	}

	public void setPopupColor(String popupColor) {
		this.popupColor = popupColor;
	}

	public void setAllowBtnBackgroundColor(String allowBtnBackgroundColor) {
		this.allowBtnBackgroundColor = allowBtnBackgroundColor;
	}

	public void setAllowBtnColor(String allowBtnColor) {
		this.allowBtnColor = allowBtnColor;
	}

	public void setDontAllowBtnBackgroundColor(String dontAllowBtnBackgroundColor) {
		this.dontAllowBtnBackgroundColor = dontAllowBtnBackgroundColor;
	}

	public void setDontAllowBtnColor(String dontAllowBtnColor) {
		this.dontAllowBtnColor = dontAllowBtnColor;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

	public void setCheckFlag(boolean checkFlag) {
		this.checkFlag = checkFlag;
	}



	public String getDeviceType() {
		return deviceType;
	}



	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}



	@Override
	public String toString() {
		return "CustomNotificationBean [customNotificationId=" + customNotificationId + ", title=" + title
				+ ", message=" + message + ", logoPath=" + logoPath + ", optIn=" + optIn + ", allowText=" + allowText
				+ ", dontAllowText=" + dontAllowText + ", websiteUrl=" + websiteUrl + ", popupBackgroundColor="
				+ popupBackgroundColor + ", popupColor=" + popupColor + ", allowBtnBackgroundColor="
				+ allowBtnBackgroundColor + ", allowBtnColor=" + allowBtnColor + ", dontAllowBtnBackgroundColor="
				+ dontAllowBtnBackgroundColor + ", dontAllowBtnColor=" + dontAllowBtnColor + ", delayTime=" + delayTime
				+ ", checkFlag=" + checkFlag + ", type=" + type + ", buttonType=" + buttonType + ", deviceType="
				+ deviceType + "]";
	}









}
