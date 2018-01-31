package com.saphire.iopush.bean;



public class PaypalPaymentDefinitions {

		private String id;
		private String name;
		private String type;
		private String frequency;
		public Amount amount;
		private String cycles;
		private String frequency_interval;
		
		public class Amount{

			private String currency;
			private String value;
			public String getCurrency() {
				return currency;
			}
			public void setCurrency(String currency) {
				this.currency = currency;
			}
			public String getValue() {
				return value;
			}
			public void setValue(String value) {
				this.value = value;
			}
			public Amount(String currency, String value) {
				super();
				this.currency = currency;
				this.value = value;
			}

		}
		
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getFrequency() {
			return frequency;
		}

		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}

		public Amount getAmount() {
			return amount;
		}

		public void setAmount(Amount amount) {
			this.amount = amount;
		}

		public String getFrequency_interval() {
			return frequency_interval;
		}

		public void setFrequency_interval(String frequency_interval) {
			this.frequency_interval = frequency_interval;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getCycles() {
			return cycles;
		}

		public void setCycles(String cycles) {
			this.cycles = cycles;
		}

		public PaypalPaymentDefinitions(String id, String name, String type, String frequency, Amount amount,
				String cycles, String frequency_interval) {
			super();
			this.id = id;
			this.name = name;
			this.type = type;
			this.frequency = frequency;
			this.amount = amount;
			this.cycles = cycles;
			this.frequency_interval = frequency_interval;
		}

		public PaypalPaymentDefinitions() {
			// TODO Auto-generated constructor stub
		}

			

}

