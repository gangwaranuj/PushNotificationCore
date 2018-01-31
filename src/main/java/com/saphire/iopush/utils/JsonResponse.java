 package com.saphire.iopush.utils;
 
 import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
 
 
 
 
 
 public class JsonResponse<T>
 {
   private String Result;
   private List<T> Records;
   private T Record;
   private String Message;
   private List<T> Options;
   private int TotalRecordCount;
   private int statusCode;
   
   public int getStatusCode() {
	return statusCode;
}

public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}
   public JsonResponse() {}
   
   public JsonResponse(String Result)
   {
     this.Result = Result;
   }
   
   public JsonResponse(String Result, T Record) {
     this.Result = Result;
     this.Record = Record;
   }
   
   public JsonResponse(String Result, T Record, int TotalRecordCount) {
     this.Result = Result;
     this.Record = Record;
     setTotalRecordCount(TotalRecordCount);
   }
   
   public JsonResponse(String Result, String Message) {
     this.Result = Result;
     this.Message = Message;
   }
   
   public JsonResponse(String Result, List<T> Records, int TotalRecordCount) {
     this.Result = Result;
     this.Records = Records;
     setTotalRecordCount(TotalRecordCount);
   }
   
   public JsonResponse(String Result, List<T> Options) {
     this.Result = Result;
     this.Options = Options;
   }
   @JsonProperty("Options")
   public List<T> getOptions() {
     return this.Options;
   }
   
   public void setOptions(List<T> Options) {
     this.Options = Options;
   }
   
   @JsonProperty("Result")
   public String getResult() { return this.Result; }
   
   public void setResult(String Result)
   {
     this.Result = Result;
   }
   
   @JsonProperty("Record")
   public T getRecord() {
     return (T)this.Record;
   }
   
   public void setRecord(T Record) {
     this.Record = Record;
   }
   @JsonProperty("Message")
   public String getMessage() {
     return this.Message;
   }
   
   public void setMessage(String Message) {
     this.Message = Message;
   }
   
   @JsonProperty("Records")
   public List<T> getRecords() {
     return this.Records;
   }
   
   public void setRecords(List<T> Records) {
     this.Records = Records;
   }
   
   @JsonProperty("TotalRecordCount")
   public int getTotalRecordCount()
   {
     return this.TotalRecordCount;
   }
   
   public void setTotalRecordCount(int totalRecordCount) {
     this.TotalRecordCount = totalRecordCount;
   }
  
 }
