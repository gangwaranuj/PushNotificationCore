package com.saphire.iopush.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "iopush_customnotification", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames={"fk_product_id","device_type"}))

//@Table(name = "iopush_customnotification", schema = "public", uniqueConstraints = @UniqueConstraint(columnNames={"type"}))
public class IopushCustomNotification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int customNotificationId;
	private String title;
	private String message;
	private String logoPath;
	private boolean optIn;
	//private int productId;
	private int product_id;
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
	private String allowText;
	private String DontAllowText;

	public IopushCustomNotification() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IopushCustomNotification(int customNotificationId, String title, String message, String logoPath,
			boolean optIn, int product_id, String websiteUrl, String popupBackgroundColor, String popupColor,
			String allowBtnBackgroundColor, String allowBtnColor, String dontAllowBtnBackgroundColor,
			String dontAllowBtnColor, int delayTime, boolean checkFlag, String type, String allowText,
			String dontAllowText,String buttonType) {
		super();
		this.customNotificationId = customNotificationId;
		this.title = title;
		this.message = message;
		this.logoPath = logoPath;
		this.optIn = optIn;
		//this.productId = productId;
		this.product_id =product_id;
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
		this.buttonType =  buttonType;
		this.allowText = allowText;
		DontAllowText = dontAllowText;
	}


	public IopushCustomNotification(int customNotificationId, String title, String message, String logoPath,
			boolean optIn, int product_id, String websiteUrl, String popupBackgroundColor, String popupColor,
			String allowBtnBackgroundColor, String allowBtnColor, String dontAllowBtnBackgroundColor,
			String dontAllowBtnColor, int delayTime, boolean checkFlag, String type, String buttonType,
			String deviceType, String allowText, String dontAllowText) {
		super();
		this.customNotificationId = customNotificationId;
		this.title = title;
		this.message = message;
		this.logoPath = logoPath;
		this.optIn = optIn;
		this.product_id = product_id;
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
		this.allowText = allowText;
		DontAllowText = dontAllowText;
	}

	@Id   
	@SequenceGenerator(name="iopush_customnotification_seq", sequenceName="iopush_customnotification_seq",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_customnotification_seq")
	@Column(name = "notification_id", unique = true, nullable = false,columnDefinition = "serial")
	public int getCustomNotificationId() {
		return customNotificationId;
	}

	@Column(length = 50)
	public String getTitle() {
		return title;
	}

	/**
	 * @return the buttonType
	 */
	@Column(name = "buttonType",length = 50)
	public String getButtonType() {
		return buttonType;
	}

	/**
	 * @param buttonType the buttonType to set
	 */
	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	@Column(length = 200)
	public String getMessage() {
		return message;
	}

	@Column(length = 100)
	public String getLogoPath() {
		return logoPath;
	}

	public boolean isOptIn() {
		return optIn;
	}


	@Column(length = 20)
	public String getAllowText() {
		return allowText;
	}

	@Column(length = 30)
	public String getDontAllowText() {
		return DontAllowText;
	}

	//	
	//	@Column(name="product_id")
	//	public int getProductId() {
	//		return productId;
	//	}

	//	 @Column(name = "fk_product_id", nullable = false)
	//	 public int getProductId() {
	//	 	return product_id;
	//	 }

	@Column(name = "pop_up_background", length = 30)
	public String getPopupBackgroundColor() {
		return popupBackgroundColor;
	}

	@Column(name = "pop_up_color",length = 30)
	public String getPopupColor() {
		return popupColor;
	}

	@Column(name = "allow_button_background",length = 30)
	public String getAllowBtnBackgroundColor() {
		return allowBtnBackgroundColor;
	}

	@Column(name = "allow_button_color",length = 30)
	public String getAllowBtnColor() {
		return allowBtnColor;
	}

	@Column(name = "dont_allow_button_background",length = 30)
	public String getDontAllowBtnBackgroundColor() {
		return dontAllowBtnBackgroundColor;
	}

	@Column(name = "dont_allow_button_color",length = 30)
	public String getDontAllowBtnColor() {
		return dontAllowBtnColor;
	}

	@Column(name = "delay_time")
	public int getDelayTime() {
		return delayTime;
	}

	@Column(name = "check_flag")
	public boolean isCheckFlag() {
		return checkFlag;
	}


	@Column(name = "type",length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	//	public void setProductId(int productId) {
	//		this.setProduct_id(productId);
	//	}


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
		DontAllowText = dontAllowText;
	}

	@Column(name = "website_url", length = 100)
	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	@Column(name = "fk_product_id", nullable = false)
	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	@Column(name = "device_type", length = 100)
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}



}