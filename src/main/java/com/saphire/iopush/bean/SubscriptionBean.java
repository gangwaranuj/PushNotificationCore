package com.saphire.iopush.bean;

public class SubscriptionBean {

	String iopushToken = "";
	boolean isMobile ;
	String currentToken = "";
	String browser_name = "";
	String os_language = "";
	String os_name = "";
	String ip = "";
	String hostName = "";
	String pid = "";
	String segment_id ="";
	
	
	
	public SubscriptionBean(String iopushToken, boolean isMobile, String currentToken, String browser_name,
			String os_language, String os_name, String ip, String hostName, String pid,String segmentid) {
		super();
		this.iopushToken = iopushToken;
		this.isMobile = isMobile;
		this.currentToken = currentToken;
		this.browser_name = browser_name;
		this.os_language = os_language;
		this.os_name = os_name;
		this.ip = ip;
		this.hostName = hostName;
		this.pid = pid;
		this.segment_id = segmentid;
	}
	/**
	 * @return the segment_id
	 */
	
	/**
	 * @return the iopushToken
	 */
	public String getIopushToken() {
		return iopushToken;
	}
	/**
	 * @param iopushToken the iopushToken to set
	 */
	public void setIopushToken(String iopushToken) {
		this.iopushToken = iopushToken;
	}
	/**
	 * @return the isMobile
	 */
	public boolean isMobile() {
		return isMobile;
	}
	/**
	 * @param isMobile the isMobile to set
	 */
	public void setMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}
	/**
	 * @return the currentToken
	 */
	public String getCurrentToken() {
		return currentToken;
	}
	/**
	 * @param currentToken the currentToken to set
	 */
	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}
	/**
	 * @return the browser_name
	 */
	public String getBrowser_name() {
		return browser_name;
	}
	/**
	 * @param browser_name the browser_name to set
	 */
	public void setBrowser_name(String browser_name) {
		this.browser_name = browser_name;
	}
	/**
	 * @return the os_language
	 */
	public String getOs_language() {
		return os_language;
	}
	/**
	 * @param os_language the os_language to set
	 */
	public void setOs_language(String os_language) {
		this.os_language = os_language;
	}
	
	
	/**
	 * @return the os_name
	 */
	public String getOs_name() {
		return os_name;
	}
	/**
	 * @param os_name the os_name to set
	 */
	public void setOs_name(String os_name) {
		this.os_name = os_name;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}
	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getSegment_id() {
		return segment_id;
	}
	/**
	 * @param segment_id the segment_id to set
	 */
	public void setSegment_id(String segment_id) {
		this.segment_id = segment_id;
	}
	@Override
	public String toString() {
		return "SubscriptionBean [iopushToken=" + iopushToken + ", isMobile=" + isMobile + ", currentToken="
				+ currentToken + ", browser_name=" + browser_name + ", os_language=" + os_language + ", os_name="
				+ os_name + ", ip=" + ip + ", hostName=" + hostName + ", pid=" + pid +" , segment_id=" +segment_id+ "]";
	}
	
	
	
	
}
