package com.saphire.iopush.bean;

public class IpnInfoBean {

		private long LogTime;
		private String ItemName;
		private String  ItemNumber;
		private String PaymentStatus;
		private String paymentAmount;
		private String TxnId;
		private String ReceiverEmail;
		private String PayerEmail;
		private String Response;
		private String RequestParams;
		private String transactionType;
		private String agreeementId;
		
		public String getAgreeementId() {
			return agreeementId;
		}
		public void setAgreeementId(String agreeementId) {
			this.agreeementId = agreeementId;
		}
		public String getTransactionType() {
			return transactionType;
		}
		public long getLogTime() {
			return LogTime;
		}
		public void setLogTime(long logTime) {
			LogTime = logTime;
		}
		public String getItemName() {
			return ItemName;
		}
		public void setItemName(String itemName) {
			ItemName = itemName;
		}
		public String getItemNumber() {
			return ItemNumber;
		}
		public void setItemNumber(String itemNumber) {
			ItemNumber = itemNumber;
		}
		public String getPaymentStatus() {
			return PaymentStatus;
		}
		public void setPaymentStatus(String paymentStatus) {
			PaymentStatus = paymentStatus;
		}
		public String getPaymentAmount() {
			return paymentAmount;
		}
		public void setPaymentAmount(String paymentAmount) {
			this.paymentAmount = paymentAmount;
		}
		public String getTxnId() {
			return TxnId;
		}
		public void setTxnId(String txnId) {
			TxnId = txnId;
		}
		public String getReceiverEmail() {
			return ReceiverEmail;
		}
		public void setReceiverEmail(String receiverEmail) {
			ReceiverEmail = receiverEmail;
		}
		public String getPayerEmail() {
			return PayerEmail;
		}
		public void setPayerEmail(String payerEmail) {
			PayerEmail = payerEmail;
		}
		public String getResponse() {
			return Response;
		}
		public void setResponse(String response) {
			Response = response;
		}
		public String getRequestParams() {
			return RequestParams;
		}
		public void setRequestParams(String requestParams) {
			RequestParams = requestParams;
		}
		public void setTransactionType(String parameter) {
			// TODO Auto-generated method stub
			transactionType=parameter;
			
		}
	
		
		
		
		

}
