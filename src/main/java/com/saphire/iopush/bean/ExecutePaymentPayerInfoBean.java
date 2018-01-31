package com.saphire.iopush.bean;

public class ExecutePaymentPayerInfoBean {

	private String email;
	private String first_name;
	private String last_name;
	private String payer_id;
	private ShippingAddressBean shipping_address;
	private String country_code;





	public ExecutePaymentPayerInfoBean(String email, String first_name, String last_name, String payer_id,
			ShippingAddressBean shippingAddressBean, String country_code) {
		super();
		this.email = email;
		this.first_name = first_name;
		this.last_name = last_name;
		this.payer_id = payer_id;
		this.shipping_address = shippingAddressBean;
		this.country_code = country_code;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPayer_id() {
		return payer_id;
	}
	public void setPayer_id(String payer_id) {
		this.payer_id = payer_id;
	}
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public ShippingAddressBean getShippingAddressBean() {
		return shipping_address;
	}
	public void setShippingAddressBean(ShippingAddressBean shippingAddressBean) {
		this.shipping_address = shippingAddressBean;
	}
	@Override
	public String toString() {
		return "ExecutePaymentPayerInfoBean [email=" + email + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", payer_id=" + payer_id + ", country_code=" + country_code + ", shippingAddressBean="
				+ shipping_address + "]";
	}



}
