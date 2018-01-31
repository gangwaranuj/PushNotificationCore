package com.saphire.iopush.bean;

public class PaymentAgreementDetailsBean {

 private String cycles_remaining;
 private String cycles_completed;
 private String next_billing_date;
 private String last_payment_date;
 private String final_payment_date;
 private String failed_payment_count;
 private LastPaymentAmountBean last_payment_amount;
 private OutstandingBalanceBean outstanding_balance;
 /**
  * @return the cycles_remaining
  */
 public String getCycles_remaining() {
  return cycles_remaining;
 }
 /**
  * @param cycles_remaining the cycles_remaining to set
  */
 public void setCycles_remaining(String cycles_remaining) {
  this.cycles_remaining = cycles_remaining;
 }
 /**
  * @return the cycles_completed
  */
 public String getCycles_completed() {
  return cycles_completed;
 }
 /**
  * @param cycles_completed the cycles_completed to set
  */
 public void setCycles_completed(String cycles_completed) {
  this.cycles_completed = cycles_completed;
 }
 /**
  * @return the next_billing_date
  */
 public String getNext_billing_date() {
  return next_billing_date;
 }
 /**
  * @param next_billing_date the next_billing_date to set
  */
 public void setNext_billing_date(String next_billing_date) {
  this.next_billing_date = next_billing_date;
 }
 /**
  * @return the last_payment_date
  */
 public String getLast_payment_date() {
  return last_payment_date;
 }
 /**
  * @param last_payment_date the last_payment_date to set
  */
 public void setLast_payment_date(String last_payment_date) {
  this.last_payment_date = last_payment_date;
 }
 /**
  * @return the final_payment_date
  */
 public String getFinal_payment_date() {
  return final_payment_date;
 }
 /**
  * @param final_payment_date the final_payment_date to set
  */
 public void setFinal_payment_date(String final_payment_date) {
  this.final_payment_date = final_payment_date;
 }
 /**
  * @return the failed_payment_count
  */
 public String getFailed_payment_count() {
  return failed_payment_count;
 }
 /**
  * @param failed_payment_count the failed_payment_count to set
  */
 public void setFailed_payment_count(String failed_payment_count) {
  this.failed_payment_count = failed_payment_count;
 }
 /**
  * @return the lastPaymentAmountBean
  */
 public LastPaymentAmountBean getLastPaymentAmountBean() {
  return last_payment_amount;
 }
 /**
  * @param lastPaymentAmountBean the lastPaymentAmountBean to set
  */
 public void setLastPaymentAmountBean(LastPaymentAmountBean lastPaymentAmountBean) {
  last_payment_amount = lastPaymentAmountBean;
 }
 /**
  * @return the outstandingBalanceBean
  */
 public OutstandingBalanceBean getOutstandingBalanceBean() {
  return outstanding_balance;
 }
 /**
  * @param outstandingBalanceBean the outstandingBalanceBean to set
  */
 public void setOutstandingBalanceBean(OutstandingBalanceBean outstandingBalanceBean) {
  this.outstanding_balance = outstandingBalanceBean;
 }
 
 
 
}