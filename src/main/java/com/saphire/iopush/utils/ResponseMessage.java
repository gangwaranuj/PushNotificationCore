package com.saphire.iopush.utils;

public class ResponseMessage {
	int responseCode;
	String responseDescription;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	Object data;
	/**
	 * @param responseCode
	 * @param responseDescription
	 */
	public ResponseMessage(int responseCode, String responseDescription) {
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
	}
	public ResponseMessage() {
		// TODO Auto-generated constructor stub
	}


	public ResponseMessage(int responseCode, String responseDescription, Object data) {
		super();
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.data = data;
	}
	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}
	/**
	 * @return the responseDescription
	 */
	public String getResponseDescription() {
		return responseDescription;
	}
	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	/**
	 * @param responseDescription the responseDescription to set
	 */
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ResponseMessage [responseCode=" + responseCode + ", responseDescription=" + responseDescription + "]";
	}

}
