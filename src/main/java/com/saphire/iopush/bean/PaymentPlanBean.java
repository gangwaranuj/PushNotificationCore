package com.saphire.iopush.bean;

public class PaymentPlanBean {

 public PaypalPaymentDefinitions[] payment_definitions;
 public  PaypalMerchantPreferences merchant_preferences;
 public String currency_code;
 public PaypalLinksBean[] links;
 /**
  * @return the payment_definitions
  */
 public PaypalPaymentDefinitions[] getPayment_definitions() {
  return payment_definitions;
 }
 /**
  * @param payment_definitions the payment_definitions to set
  */
 public void setPayment_definitions(PaypalPaymentDefinitions[] payment_definitions) {
  this.payment_definitions = payment_definitions;
 }
 /**
  * @return the merchant_preferences
  */
 public PaypalMerchantPreferences getMerchant_preferences() {
  return merchant_preferences;
 }
 /**
  * @param merchant_preferences the merchant_preferences to set
  */
 public void setMerchant_preferences(PaypalMerchantPreferences merchant_preferences) {
  this.merchant_preferences = merchant_preferences;
 }
 /**
  * @return the currency_code
  */
 public String getCurrency_code() {
  return currency_code;
 }
 /**
  * @param currency_code the currency_code to set
  */
 public void setCurrency_code(String currency_code) {
  this.currency_code = currency_code;
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
 
 
}