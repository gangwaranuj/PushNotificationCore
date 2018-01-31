package com.saphire.iopush.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComboBoxOption {
	
	/**
	 * @param dropdownID
	 * @param displayText
	 * @param value
	 */
	public ComboBoxOption(int dropdownID, String value,String DisplayText) {
		this.DropdownID = dropdownID;
		this.DisplayText = DisplayText;
		this.Value = value ;
	}

	public ComboBoxOption(){
		
	}
	
	public ComboBoxOption(String value,String DisplayText) {
		this.DisplayText = DisplayText;
		this.Value = value ;
	}
 private int DropdownID; 	
 private String DisplayText; 
 private String Value;
/**
 * @return the displayText
 */
 @JsonProperty("DisplayText")
public String getDisplayText() {
	return DisplayText;
}
/**
 * @param displayText the displayText to set
 */
public void setDisplayText(String displayText) {
	this.DisplayText = displayText;
}
/**
 * @return the value
 */
@JsonProperty("Value")
public String getValue() {
	return Value;
}
/**
 * @param value the value to set
 */
public void setValue(String value) {
	Value = value;
}

/**
 * @return the dropdownID
 */
@JsonProperty("DropdownID")
public int getDropdownID() {
	return DropdownID;
}

/**
 * @param dropdownID the dropdownID to set
 */
public void setDropdownID(int dropdownID) {
	DropdownID = dropdownID;
}

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Override
public String toString() {
	return "DisplayOption [DropdownID=" + DropdownID + ", DisplayText=" + DisplayText + ", Value=" + Value + "]";
}

 
}
