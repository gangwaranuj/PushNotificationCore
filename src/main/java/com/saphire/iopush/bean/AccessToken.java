package com.saphire.iopush.bean;

public class AccessToken {

	String scope;
	String nonce;
	String access_token;
	String token_type;
	String app_id;
	String expires_in;
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getAccess_token() {
		return access_token;
	}
	@Override
	public String toString() {
		return "Sample [scope=" + scope + ", nonce=" + nonce + ", access_token=" + access_token + ", token_type="
				+ token_type + ", app_id=" + app_id + ", expires_in=" + expires_in + "]";
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}
	
	
}
