package com.saphire.iopush.bean;

public class CancelAgreementBean {

	
	private String note;

	
	public CancelAgreementBean(String note) {
		super();
		this.note = note;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String toString() {
		return "CancelAgreementBean [note=" + note + "]";
	}
	
	
	
}
