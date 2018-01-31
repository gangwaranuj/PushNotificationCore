package com.saphire.iopush.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="iopush_paypal_transaction_logs" ,schema="public")

public class IopushPaypalTransactionLog {
 
 
 private int id;
 private int fk_payment_id ;
 private String resp_log;
 
 @Id 
 @SequenceGenerator(name="iopush_paypalTransaction_seq", sequenceName="iopush_paypalTransaction_seq",allocationSize=1)
 @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="iopush_paypalTransaction_seq")
 @Column(name = "id", unique = true, nullable = false,columnDefinition = "serial")
 public int getId() {
  return id;
 }
 public void setId(int id) {
  this.id = id;
 }
 


 @Column(name="fk_payment_id" )
 public int getIopushPayPalPayment() {
  return fk_payment_id;
 }
 public void setIopushPayPalPayment(int iopushPayPalPayment) {
  this.fk_payment_id = iopushPayPalPayment;
 }
 @Column(columnDefinition="text",name="json_response" )
 public String getResp_log() {
  return resp_log;
 }
 public void setResp_log(String resp_log) {
  this.resp_log = resp_log;
 }
 @Override
 public String toString() {
  return "IopushPaypalTransactionLog [id=" + id + ", iopushPayPalPayment=" + fk_payment_id + ", resp_log="
    + resp_log + "]";
 }
}