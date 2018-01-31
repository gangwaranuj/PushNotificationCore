package com.saphire.iopush.bean;

public class CreatePaymentBean {

 private int limit;
 private int planId;
 private int amount;
 private int packageId;
 private String packageName;
 

 public String getPackageName() {
  return packageName;
 }
 public void setPackageName(String packageName) {
  this.packageName = packageName;
 }
 public int getPackageId() {
  return packageId;
 }
 public void setPackageId(int packageId) {
  this.packageId = packageId;
 }
 public int getLimit() {
  return limit;
 }
 public void setLimit(int limit) {
  this.limit = limit;
 }
 public int getPlanId() {
  return planId;
 }
 public void setPlanId(int planId) {
  this.planId = planId;
 }
 public int getAmount() {
  return amount;
 }
 public void setAmount(int amount) {
  this.amount = amount;
 }
@Override
public String toString() {
	return "CreatePaymentBean [limit=" + limit + ", planId=" + planId + ", amount=" + amount + ", packageId="
			+ packageId + ", " + (packageName != null ? "packageName=" + packageName : "") + "]";
}

 
}