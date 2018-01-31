
package com.saphire.iopush.bean;


public class PaypalMerchantPreferences {

		public  setupFee setup_fee;
		private String return_url;
		private String cancel_url;
		private String auto_bill_amount;
		private String initial_fail_amount_action;
		private String max_fail_attempts;
		
		
		
		public setupFee getSetup_fee() {
			return setup_fee;
		}



		public void setSetup_fee(setupFee setup_fee) {
			this.setup_fee = setup_fee;
		}



		public String getReturn_url() {
			return return_url;
		}



		public void setReturn_url(String return_url) {
			this.return_url = return_url;
		}



		public String getCancel_url() {
			return cancel_url;
		}



		public void setCancel_url(String cancel_url) {
			this.cancel_url = cancel_url;
		}



		public String getAuto_bill_amount() {
			return auto_bill_amount;
		}



		public void setAuto_bill_amount(String auto_bill_amount) {
			this.auto_bill_amount = auto_bill_amount;
		}



		public String getInitial_fail_amount_action() {
			return initial_fail_amount_action;
		}



		public void setInitial_fail_amount_action(String initial_fail_amount_action) {
			this.initial_fail_amount_action = initial_fail_amount_action;
		}



		public String getMax_fail_attempts() {
			return max_fail_attempts;
		}



		public void setMax_fail_attempts(String max_fail_attempts) {
			this.max_fail_attempts = max_fail_attempts;
		}

		

		public PaypalMerchantPreferences(setupFee setup_fee, String return_url, String cancel_url, String auto_bill_amount,
				String initial_fail_amount_action, String max_fail_attempts) {
			super();
			this.setup_fee = setup_fee;
			this.return_url = return_url;
			this.cancel_url = cancel_url;
			this.auto_bill_amount = auto_bill_amount;
			this.initial_fail_amount_action = initial_fail_amount_action;
			this.max_fail_attempts = max_fail_attempts;
		}



		public PaypalMerchantPreferences() {
			// TODO Auto-generated constructor stub
		}



		public class setupFee{
				
				private String value;
				private String currency;
				
				
				public setupFee(String value, String currency) {
					super();
					this.value = value;
					this.currency = currency;
				}
				public String getValue() {
					return value;
				}
				public void setValue(String value) {
					this.value = value;
				}
				public String getCurrency() {
					return currency;
				}
				public void setCurrency(String currency) {
					this.currency = currency;
				}
				
			
		}
	}
	
