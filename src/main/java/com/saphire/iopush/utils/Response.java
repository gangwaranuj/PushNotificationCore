 package com.saphire.iopush.utils;
 
 import java.math.BigInteger;
import java.util.List;
 
 
 
 
 
 
 public class Response
 {
   private String message;
   private boolean status;
   private String errorMessage;
   private Exception exception;
   private int integerResult;
   private BigInteger bigInteger;
   private Long longResult;
   private List<?> data;
   private Object scalarResult;
   private int statusCode;
   
   public String getMessage()
   {
     return this.message;
   }
   
 
   public void setMessage(String message)
   {
     this.message = message;
   }
   
 
   public boolean getStatus()
   {
     return this.status;
   }
   
 
   public void setStatus(boolean status)
   {
     this.status = status;
   }
   
 
   public String getErrorMessage()
   {
     return this.errorMessage;
   }
   
 
   public void setErrorMessage(String errorMessage)
   {
     this.errorMessage = errorMessage;
   }
   
 
   public Exception getException()
   {
     return this.exception;
   }
   
 
   public void setException(Exception exception)
   {
     this.exception = exception;
   }
   
 
   public int getIntegerResult()
   {
     return this.integerResult;
   }
   
 
   public void setIntegerResult(int integerResult)
   {
     this.integerResult = integerResult;
   }
   
 
   public BigInteger getBigInteger()
   {
     return this.bigInteger;
   }
   
 
   public void setBigInteger(BigInteger bigInteger)
   {
     this.bigInteger = bigInteger;
   }
   
 
   public Long getLongResult()
   {
     return this.longResult;
   }
   
 
   public void setLongResult(Long longResult)
   {
     this.longResult = longResult;
   }
   
 
   public List<?> getData()
   {
     return this.data;
   }
   
 
   public void setData(List<?> data)
   {
     this.data = data;
   }
   
 
   public Object getScalarResult()
   {
     return this.scalarResult;
   }
   
 
   public void setScalarResult(Object scalarResult)
   {
     this.scalarResult = scalarResult;
   }


public int getStatusCode() {
	return statusCode;
}


public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}
 }

