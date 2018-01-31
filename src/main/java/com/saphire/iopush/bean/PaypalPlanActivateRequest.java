package com.saphire.iopush.bean;

public class PaypalPlanActivateRequest {
	
	private String path="/";
	private String op="replace";
	public Value value;
	
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public class Value{
		
		private String state="ACTIVE";
	}

}
