package com.saphire.iopush.bean;

import java.util.Arrays;

public class PaymentExecuteMainBean {

 private String id;
 private String state;
 private String description;
 private PaymentPlanBean plan;
 public PaypalLinksBean[] links;
 private String start_date;
 private PaymentAgreementDetailsBean agreement_details;
 /**
  * @return the id
  */
 public String getId() {
  return id;
 }
 /**
  * @param id the id to set
  */
 public void setId(String id) {
  this.id = id;
 }
 /**
  * @return the state
  */
 public String getState() {
  return state;
 }
 /**
  * @param state the state to set
  */
 public void setState(String state) {
  this.state = state;
 }
 /**
  * @return the description
  */
 public String getDescription() {
  return description;
 }
 /**
  * @param description the description to set
  */
 public void setDescription(String description) {
  this.description = description;
 }
 /**
  * @return the paymentPlanBean
  */
 public PaymentPlanBean getPaymentPlanBean() {
  return plan;
 }
 /**
  * @param paymentPlanBean the paymentPlanBean to set
  */
 public void setPaymentPlanBean(PaymentPlanBean paymentPlanBean) {
  this.plan = paymentPlanBean;
 }
 /**
  * @return the link
  */
 public PaypalLinksBean[] getLink() {
  return links;
 }
 /**
  * @param link the link to set
  */
 public void setLink(PaypalLinksBean[] link) {
  this.links = link;
 }
 /**
  * @return the start_date
  */
 public String getStart_date() {
  return start_date;
 }
 /**
  * @param start_date the start_date to set
  */
 public void setStart_date(String start_date) {
  this.start_date = start_date;
 }
 /**
  * @return the paymentAgreementDetailsBean
  */
 public PaymentAgreementDetailsBean getPaymentAgreementDetailsBean() {
  return agreement_details;
 }
 /**
  * @param paymentAgreementDetailsBean the paymentAgreementDetailsBean to set
  */
 public void setPaymentAgreementDetailsBean(PaymentAgreementDetailsBean paymentAgreementDetailsBean) {
  this.agreement_details = paymentAgreementDetailsBean;
 }
 /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
 @Override
 public String toString() {
  return "PaymentExecuteMainBean [id=" + id + ", state=" + state + ", description=" + description
    + ", paymentPlanBean=" + plan + ", link=" + Arrays.toString(links) + ", start_date="
    + start_date + ", paymentAgreementDetailsBean=" + agreement_details + "]";
 }
 
 
}