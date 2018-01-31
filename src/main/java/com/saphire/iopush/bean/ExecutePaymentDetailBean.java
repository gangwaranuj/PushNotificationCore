package com.saphire.iopush.bean;

public class ExecutePaymentDetailBean {

	
	
	private String field;
	private String issue;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getIssue() {
		return issue;
	}
	public void setIssue(String issue) {
		this.issue = issue;
	}
	@Override
	public String toString() {
		return "ExecutePaymentDetailBean [" + (field != null ? "field=" + field + ", " : "")
				+ (issue != null ? "issue=" + issue : "") + "]";
	}
	
	
	
}
