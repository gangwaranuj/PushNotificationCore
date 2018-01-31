package com.saphire.iopush.bean;

public class ShippingAddressBean {


	private String recipient_name;
	private String line1;
	private String city;
	private String state;
	private String postal_code;
	private String country_code;




	public ShippingAddressBean(String recipient_name, String line1, String city, String state, String postal_code,
			String country_code) {
		super();
		this.recipient_name = recipient_name;
		this.line1 = line1;
		this.city = city;
		this.state = state;
		this.postal_code = postal_code;
		this.country_code = country_code;
	}



	public String getRecipient_name() {
		return recipient_name;
	}
	public void setRecipient_name(String recipient_name) {
		this.recipient_name = recipient_name;
	}
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this.line1 = line1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostal_code() {
		return postal_code;
	}
	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	@Override
	public String toString() {
		return "ShippingAddressBean [recipient_name=" + recipient_name + ", line1=" + line1 + ", city=" + city
				+ ", state=" + state + ", postal_code=" + postal_code + ", country_code=" + country_code + "]";
	}



}
