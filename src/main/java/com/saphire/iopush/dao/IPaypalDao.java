package com.saphire.iopush.dao;

import java.util.Date;
import java.util.List;

import com.saphire.iopush.model.IopushPayPalPayment;
import com.saphire.iopush.model.IopushPayPalPlanSelected;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushPaypalTransactionLog;
import com.saphire.iopush.model.IopushRenewalCustomer;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Response;

public interface IPaypalDao {
	
	Response findPlanDetail(int planID);


	Response findExecuteURL(String token, int productId);


	Response updatePaypalPaymentDetail(IopushPayPalPayment iopushPayPalPayment);

	Response saveTransction(IopushPaypalTransactionLog iopushPaypalTransactionLog);

	Response findUserPlanById (int planId );

	Response findSelectedUserPlan(int paypalPaymentId);

	Response updateUsercategory(IopushUsercategory iopushUsercategory);
	
	Response findUsercategory(int productID);

	Response savePayment(IopushPayPalPayment iopushPayPalPayment);

	Response savePlanSelectInfo(IopushPayPalPlanSelected iopushPayPalPlanSelectedTable);

	Response fetchPlanInfo(int prodId, String paymentId);

	 List<Object[]> getIncompletePaymentUserInfo();

	Object[] getCurrentUserPlan(int productId);

	List<Integer> getPlanWithHighPreference(int planId);

	List<Object[]> getPackages(Integer[] planIds, int packageId);

	List<Object[]> getAllPlans();

	Response updatePaypalPaymentDetails(IopushPayment iopushPayment);

	Response getPackageDetails(int fkPackageId);

	
//	/Response findRenewalUserdetails(boolean renewStatus,String agreementStatus);
	
	Response findUserByProdID(List<Integer> prodId);

	Response saveTransaction(IopushPaypalTransactionLog iopushTransactionLog);

	Response getRenewConfig();

	Response savePayment(IopushPayment iopushPayment);

	Response getRenewalInfo(Date startDate, Date endDate);

	Response saveRenewInfo(IopushRenewalCustomer renew);

	Response getUserInfo(int prodID);

	void deleteRenewCostumer(IopushRenewalCustomer customer);

	Response findPackageDetail(int planId);

	Response findPaymentDetails(int paymentID);

	Response fetchRenewals(Date date);

	List<Object[]> fetchPackageInfo(int packageId, int planId);

	Response findRenewalUserdetails(String agreementStatus);

	Response updatePayment(IopushPayment paymentInfo);

	Response getPaymentInfo(String agreementID);


	List<Object[]> getUserInfoForAgreementId(String agreementID);


	List<Object[]> getUserInfoForAgreementIdArray(List<String> failedAgreementIDList);


	Response getUserInfo(Object[] array);


	Response getPaymentInfo(Object[] array);


	Response getPaymentInfo(int prodID);
}
