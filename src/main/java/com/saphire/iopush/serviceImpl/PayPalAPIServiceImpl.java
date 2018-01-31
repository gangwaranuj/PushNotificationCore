package com.saphire.iopush.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.saphire.iopush.bean.AmountBean;
import com.saphire.iopush.bean.CancelAgreementBean;
import com.saphire.iopush.bean.DetailsBean;
import com.saphire.iopush.bean.ExecutePaymentErrorBean;
import com.saphire.iopush.bean.ExecutePaymentResponseBean;
import com.saphire.iopush.bean.IoPushPaymentCancelledUserInfo;
import com.saphire.iopush.bean.IoPushPlanInfoBean;
import com.saphire.iopush.bean.IpnInfoBean;
import com.saphire.iopush.bean.ItemBean;
import com.saphire.iopush.bean.ItemListBean;
import com.saphire.iopush.bean.PayerBean;
import com.saphire.iopush.bean.PaymentExecuteMainBean;
import com.saphire.iopush.bean.PaymentOptions;
import com.saphire.iopush.bean.PaypalCreateAgreementRequestBean;
import com.saphire.iopush.bean.PaypalCreateAgreementRequestBean.Payer;
import com.saphire.iopush.bean.PaypalCreatePaymentBean;
import com.saphire.iopush.bean.PaypalCreatePlanBean;
import com.saphire.iopush.bean.PaypalMerchantPreferences;
import com.saphire.iopush.bean.PaypalMerchantPreferences.setupFee;
import com.saphire.iopush.bean.PaypalPaymentDefinitions;
import com.saphire.iopush.bean.PaypalPaymentDefinitions.Amount;
import com.saphire.iopush.bean.PaypalPlanActivateRequest;
import com.saphire.iopush.bean.PaypalPlanActivateRequest.Value;
import com.saphire.iopush.bean.PaypalUrl;
import com.saphire.iopush.bean.PlanDetailsBean;
import com.saphire.iopush.bean.RenewalFailedInfoBean;
import com.saphire.iopush.bean.TransactionBean;
import com.saphire.iopush.dao.IExternalAPIDao;
import com.saphire.iopush.dao.IPaypalDao;
import com.saphire.iopush.dao.IUserCategoryDao;
import com.saphire.iopush.model.IopushPackagePlan;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushPaypalTransactionLog;
import com.saphire.iopush.model.IopushPlan;
import com.saphire.iopush.model.IopushRenewalConfig;
import com.saphire.iopush.model.IopushRenewalCustomer;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.model.IopushUserplan;
import com.saphire.iopush.service.IPayPalAPIService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.PaypalResponse;
import com.saphire.iopush.utils.PaypalUtility;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.Utility;

@Service
@Transactional(readOnly = false)
public class PayPalAPIServiceImpl implements IPayPalAPIService {

	@Autowired	IPaypalDao iPaypalDaoImpl;
	@Autowired	Properties myProperties;
	@Autowired  IExternalAPIDao iExternalAPIDao;
	@Autowired  IPaypalDao iPaypalDao;
//	@Autowired 	JavaMailSender mailSender;
	@Autowired IUserCategoryDao iUserCategoryDao;

	@Autowired JavaMailSender mailSender_service;
	@Autowired JavaMailSender mailSender_support;

	private Logger logger = LoggerFactory.getLogger(PayPalAPIServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Response createPayment(int productID, int limit, int planID, int amount, int packageId) throws org.apache.http.auth.AuthenticationException 
	{

		logger.info("inside create plan method.");
		// Step 1: get the access token
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String clientID = myProperties.getProperty(env + "clientUserName");
		String secretPassword = myProperties.getProperty(env + "clientSecretPassword");
		String tokenURL = myProperties.getProperty(env + "tokenURL");
		String planURL = myProperties.getProperty(env + "planURL");
		String planActivateURL= myProperties.getProperty(env + "planActivateURL");
		String createAgreementURL= myProperties.getProperty(env + "createAgreementURL");
		String token = null;
		Response paymentAPIReponse = new Response();
		paymentAPIReponse.setStatus(false);
		List<String> redirectURL = new ArrayList<>();

		try {

			token = PaypalUtility.getAccesstoken(clientID, secretPassword, tokenURL);
			if (token != null) {

				logger.info("createPayment,Access token in retrieved, token id is {}",token);
				// after getting access token we need to create plan request to paypal
				Response response1 = iPaypalDaoImpl.findPlanDetail((planID));
				if (response1.getStatus()) {

					IopushPlan plan = (IopushPlan) response1.getData().get(0);
					String packageName= plan.getPlanName();
					// before creating the plan request we need to get the frequency and interval from the db
					Response response = iPaypalDaoImpl.getRenewConfig();
					Response userResponse = iExternalAPIDao.fetchUserByProdId(productID);
					if(response.getStatus())
					{

						IopushRenewalConfig renewalConfig=(IopushRenewalConfig) response.getData().get(0);
						// retries will be maximum fail attempts
						PaypalCreatePlanBean paypalCreatePlanBean = this.createPaypalPaymentRequest(packageName, amount,productID,""+renewalConfig.getFrequency(),renewalConfig.getFrequencyInterval(),renewalConfig.getRetries());
						PaypalResponse paypalResponse = PaypalUtility.createPlanRequest(clientID, secretPassword,planURL, paypalCreatePlanBean,token);
						if (paypalResponse.getStatus()) {

							PaypalCreatePlanBean paypalResponseObject = (PaypalCreatePlanBean) paypalResponse.getScalarResult();
							String planId=paypalResponseObject.getId();
							//Step 2: After creating plan request we need to activate the plan
							PaypalPlanActivateRequest[] reqArray= new PaypalPlanActivateRequest[1];
							PaypalPlanActivateRequest  req= new PaypalPlanActivateRequest();
							Value val=req.new Value();
							req.setValue(val);
							reqArray[0]=req;
							PaypalResponse paypalActivateResponse = PaypalUtility.activatePlan(planActivateURL+planId,token,reqArray);
							if(paypalActivateResponse.getStatus())
							{

								logger.info("createPayment,Paypal plan activated successfully, now agreement will be created");
								//Step 3: We need to create the agreement
								PaypalCreateAgreementRequestBean agreementRequestBean = this.createPaypalCreateAgreementRequestBean(packageName, amount,planId,renewalConfig.getFrequency(),renewalConfig.getFrequencyInterval());
								PaypalResponse createAgreementResponse=PaypalUtility.createAgreementRequest(clientID,secretPassword,createAgreementURL,token,agreementRequestBean);
								if(createAgreementResponse.getStatus())
								{
									PaypalCreateAgreementRequestBean agreementRequestBean2=(PaypalCreateAgreementRequestBean) createAgreementResponse.getScalarResult();
									DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
									Date date = new Date();
									// all the paypal step are done now save the plan and agreement details in payment table
									IopushPayment iopushPayment= new IopushPayment();
									// we will set the agreement id o since agreement is yet to be executed
									//iopushPayment.setAgreementId("");
									//	iopushPayment.setAmount((double) amount);
									if(userResponse.getStatus())
									{
										IopushUser user=(IopushUser) userResponse.getScalarResult();
										iopushPayment.setCreatedBy(user.getUsername());
										iopushPayment.setModifiedBy(user.getUsername());

									}
									else
									{
										iopushPayment.setCreatedBy("");
										iopushPayment.setModifiedBy("");
									}

									iopushPayment.setCreationDate(date);
									iopushPayment.setModifiedOn(date);
									//since now cycle has been completed
									//iopushPayment.setCycleCompleted(0);
									iopushPayment.setFkPackageId(packageId);
									iopushPayment.setFkPlanId(planID);
									iopushPayment.setFkProductId(productID);
									// here we will set the start date
									//iopushPayment.setNextPaymentdate("");
									//iopushPayment.setPaymentdate();
									iopushPayment.setPaymentMethod(Constants.PAYPAL_REQUEST_PAYMENT_METHOD);
									iopushPayment.setPaymentType(Constants.PAYPAL_TRANSACTION_TYPE_INSTANT);
									iopushPayment.setRenewAmount(amount);
									iopushPayment.setTpAcknowledgement(0);
									if(agreementRequestBean2!=null )
									{
										String url=agreementRequestBean2.getLinks()[1].getHref();
										String tp_token=url;
										tp_token=tp_token.substring(0, tp_token.lastIndexOf("/"));
										tp_token=tp_token.substring(tp_token.lastIndexOf("/")+1, tp_token.length());
										redirectURL.add(agreementRequestBean2.getLinks()[0].getHref());
										//iopushPayment.setTpPaymentId();
										iopushPayment.setAgreementStatus("Created");
										iopushPayment.setTpExecuteURL(url);
										iopushPayment.setTpToken(tp_token);
									}
									else
									{
										iopushPayment.setAgreementStatus("");
									}
									// going to save the

									logger.info("createPayment,going to save the payment details in paypal payment table");
									Response paymentSaveResponse = iPaypalDaoImpl.savePayment(iopushPayment);
									if (paymentSaveResponse.getStatus()) {

										logger.info("createPayment,payment details successfully saved");
										iopushPayment.setPaymentId(paymentSaveResponse.getIntegerResult());
										// need to save the transaction as well
										IopushPaypalTransactionLog iopushPaypalTransactionLog = new IopushPaypalTransactionLog();
										// hardcoding paypal payment for testing
										// transactional behaviour
										// iopushPayPalPayment.setPayment_id(250);
										iopushPaypalTransactionLog.setIopushPayPalPayment(iopushPayment.getPaymentId());
										iopushPaypalTransactionLog.setResp_log(paypalResponse.getJsonResponse());
										//logger.info("going to save the transaction in transaction log table, transaction object is {} ", iopushPaypalTransactionLog.toString());
										Response paymentTransactionSaveResponse = iPaypalDaoImpl.saveTransction(iopushPaypalTransactionLog);
										if (paymentTransactionSaveResponse.getStatus()) {
											logger.info("createPayment,Transaction log successfully saved");
											if (redirectURL.isEmpty()) {
												logger.info("createPayment,redirect URL is empty from paypal response");
												paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
												paymentAPIReponse.setMessage(
														"Unable to create paypal payment request,no return URL is fetched from paypal");
											} else {
												logger.info("createPayment,redirect URL is obtained from paypal response");
												paymentAPIReponse.setStatus(true);
												paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_SUCCESSCODE);
												paymentAPIReponse.setData(redirectURL);
												paymentAPIReponse.setMessage("create payment Request Successful");
												logger.info("createpayment request successfully completed");
											}

										} else {
											logger.info("createPayment,unable to save the transaction info to the transaction table");
											paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
											// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
											paymentAPIReponse.setMessage(
													"Unable to create paypal payment request,some exception while saving transaction details");
										}
									} else {
										logger.info("createPayment,unable to save the payment info to the payment table");
										paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
										// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
										paymentAPIReponse.setMessage("Unable to create paypal payment request,some exception while saving payment details");
									}
								}
								else
								{
									logger.info("createPayment,unable to create the agreement");
									paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
									// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
									paymentAPIReponse.setMessage(
											"Unable to complete paypal plan request,some error while creating the agreement");
								}
							} else {
								logger.info("unable to activate the plan");
								paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
								// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
								paymentAPIReponse.setMessage("Unable to complete paypal plan request,some error while activating the plan");
							}
						} 
						else {
							logger.info("unable to create the plan");
							paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
							// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
							paymentAPIReponse.setMessage("Unable to complete the paypal plan request, some error while creating plan");
						}
					}
					else {
						logger.info("unable to get the renew config details from the db");
						paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
						// paymentAPIReponse.setStatusCode(Constants.PAYPAL_REQUEST_UNAUTHORISED_REQUEST);
						paymentAPIReponse.setMessage("Unable to create paypal plan request, since no renew config details was retrieved from the db");
					}
				}
				else {
					logger.info("unable to get the plan details from the table");
					paymentAPIReponse.setStatusCode(Constants.PAYPAL_PAYMENT_UNAUTHORISED_REQUEST);
					paymentAPIReponse.setMessage("Unable to create paypal request,since no plan details was fetched for the given plan id ");
				}
			}
			else {
				logger.info("unable to get the access token from paypal API");
				paymentAPIReponse.setStatusCode(Constants.PAYPAL_PAYMENT_UNAUTHORISED_REQUEST);
				paymentAPIReponse.setMessage("Unable to create paypal payment request,since access token was not fetched ");
			}
		} catch (IOException e) {
			logger.error("Unable to create paypal payment,because of exception [" + e + "]");
			paymentAPIReponse.setStatusCode(Constants.PAYPAL_API_FAILED_REQUEST_CODE);
			paymentAPIReponse.setMessage("Unable to create paypal payment request");
			e.printStackTrace();
		}
		return paymentAPIReponse;
	}
	private PaypalCreatePaymentBean createPaypalPaymentRequest(IopushUserplan plan, Integer productID) {

		// this method is used to create request for paypal create payment API
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		PaypalCreatePaymentBean requestBean = new PaypalCreatePaymentBean();
		requestBean.setIntent(Constants.PAYPAL_REQUEST_INTENT);
		PayerBean payerBean = new PayerBean();
		payerBean.setPaymentMethod(Constants.PAYPAL_REQUEST_PAYMENT_METHOD);

		TransactionBean transactionBean = new TransactionBean();
		AmountBean amountBean = new AmountBean();
		amountBean.setCurrency(Constants.PAYPAL_REQUEST_CURRENCY);
		DetailsBean details = new DetailsBean();
		details.setHandling_fee(Constants.PAYPAL_REQUEST_BLANK);
		details.setInsurance(Constants.PAYPAL_REQUEST_BLANK);
		details.setShipping(Constants.PAYPAL_REQUEST_BLANK);
		details.setShipping_discount(Constants.PAYPAL_REQUEST_BLANK);
		details.setSubtotal("" + plan.getPricing());
		details.setTax(Constants.PAYPAL_REQUEST_BLANK);
		amountBean.setDetails(details);
		amountBean.setTotal(details.getSubtotal());
		transactionBean.setAmount(amountBean);
		transactionBean.setDescription(Constants.PAYPAL_REQUEST_DESCRIPTION);
		transactionBean.setCustom(Constants.PAYPAL_REQUEST_BLANK_STRING);
		transactionBean.setInvoice_number(Constants.PAYPAL_REQUEST_BLANK_STRING);
		PaymentOptions paymentOptions = new PaymentOptions();
		paymentOptions.setAllowed_payment_method(Constants.PAYPAL_REQUEST_ALLOWED_PAYMENT_METHOD);
		transactionBean.setPayment_options(paymentOptions);
		transactionBean.setSoft_descriptor(Constants.PAYPAL_REQUEST_BLANK_STRING);
		ItemListBean itemList = new ItemListBean();
		ItemBean item = new ItemBean(plan.getPlanName(), Constants.PAYPAL_REQUEST_BLANK_STRING,
				"" + Constants.PAYPAL_REQUEST_ITEM_QUANTITY, "" + plan.getPricing(),
				"" + Constants.PAYPAL_REQUEST_BLANK, "" + Constants.PAYPAL_REQUEST_BLANK,
				"" + Constants.PAYPAL_REQUEST_CURRENCY);
		Set<ItemBean> items = new HashSet<>();
		items.add(item);
		itemList.setItems(items);
		transactionBean.setItem_list(itemList);
		List<TransactionBean> transactionList = new ArrayList<>();
		transactionList.add(transactionBean);
		requestBean.setPayer(payerBean);
		requestBean.setTransactions(transactionList);
		requestBean.setNote_to_payer(Constants.PAYPAL_REQUEST_NOTE_TO_PAYER);
		PaypalUrl url = new PaypalUrl();
		// myProperties.getProperty("paypalReturnURL");
		String host = myProperties.getProperty(env + "IMAGEURL");
		url.setCancel_urls(host+myProperties.getProperty(env + "paypalCancelURL"));
		url.setReturn_url(host + myProperties.getProperty(env + "paypalReturnURL"));
		requestBean.setRedirect_urls(url);
		return requestBean;

	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseMessage paymentExecute(String reqUrl, int productId)
	{
		logger.info("inside paymentExecute.");
		ResponseMessage rm= null;

		double planCost = 0;
		String httpurl = "";
		Response response = null;
		String paypalResultJson = "";
		String paymentApprovalUrl = null;
		String accessToken=null;
		IopushPayment iopushPayment = null;
		String tptoken=null;
		Object username=null;
		String url ="";
		IopushUsercategory iopushUsercategory =null;	
		ExecutePaymentErrorBean executePaymentErrorBean = null;
		PaymentExecuteMainBean paymentExecuteMainBean =null;
		JsonResponse<ExecutePaymentResponseBean> jsonResponse = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		try {

			Date date= new Date();
			Gson gson = new Gson();
			Map params = PaypalUtility.getQueryMap(reqUrl);
			tptoken = (String) params.get("token");
			String userName = myProperties.getProperty(env+"clientUserName");
			String secretPassword = myProperties.getProperty(env+"clientSecretPassword");
			String tokenURL = myProperties.getProperty(env+"tokenURL");
			logger.info("paymentExecute going to fetch request param.");
			accessToken = PaypalUtility.getAccesstoken(userName, secretPassword, tokenURL);
			logger.debug("paymentExecute   request params [{}] and accessToken[{}]",params,accessToken);
			logger.info("Before thread.sleep");
			//Thread for testing
			Thread.sleep(Long.parseLong(myProperties.getProperty(env+"THREAD_TIME")));


			/************************************************************/
			//************Step 2. going to execute current payment
			/************************************************************/
			logger.info("After thread.sleep");
			response = this.iPaypalDaoImpl.findExecuteURL(tptoken, productId);
			if (response.getStatus()) {
				iopushPayment = (IopushPayment) response.getData().get(0);
				String executeUrl = iopushPayment.getTpExecuteURL();
				logger.info(" paypal  payment url is retreived :[{}]",executeUrl);
				PaypalResponse excuteResponse = PaypalUtility.executePayment(executeUrl, accessToken, "");
				if (excuteResponse.getStatus()) {
					if (excuteResponse.getStatusCode() == 200) {

						paymentExecuteMainBean =  gson.fromJson((String)excuteResponse.getScalarResult(), PaymentExecuteMainBean.class);
						paypalResultJson = gson.toJson(paymentExecuteMainBean);
						logger.info("PaymentExecute   executePayment  response bean Successfully retrieved  ["+ paymentExecuteMainBean.toString() + "].");

						//case 1. status 200 Ok  And (state = Actice)
						if((paymentExecuteMainBean.getState()).equalsIgnoreCase("Active")){

							logger.info("paymentExecute, going to fetch paypalpayment informaiton .");
							Response responsenew= this.iExternalAPIDao.fetchPaypalPaymentdetail(productId,Constants.PAYAL_AGREEMENT_STATUS_ACTIVE);

							//*************set values in iopushPayment after execute payment ***********
							iopushPayment.setRenewalPaymentStatus(false);
							iopushPayment.setOutstandingBalance(Utility.intConverter(paymentExecuteMainBean.getPaymentAgreementDetailsBean().getOutstandingBalanceBean().getValue()));
							iopushPayment.setNextPaymentdate(sdf.parse(paymentExecuteMainBean.getPaymentAgreementDetailsBean().getNext_billing_date()));
							iopushPayment.setAgreementId(paymentExecuteMainBean.getId());
							iopushPayment.setAgreementStatus(paymentExecuteMainBean.getState());
							iopushPayment.setTpAcknowledgement(1);
							iopushPayment.setPaymentdate(sdf.parse(paymentExecuteMainBean.getPaymentAgreementDetailsBean().getLast_payment_date()));
							Response userResponse = iExternalAPIDao.fetchUserByProdId(productId);
							if(userResponse.getStatus())
							{
								IopushUser user=(IopushUser) userResponse.getScalarResult();
								iopushPayment.setModifiedBy(user.getUsername());
								//iopushPayment.setModifiedBy(user.getUsername());

							}
							else
							{
								iopushPayment.setModifiedBy("");
							}
							iopushPayment.setModifiedOn(date);

							if((paymentExecuteMainBean.getPaymentAgreementDetailsBean().getLastPaymentAmountBean().getValue())!=null)
							{
								iopushPayment.setAmount((Double.parseDouble(paymentExecuteMainBean.getPaymentAgreementDetailsBean().getLastPaymentAmountBean().getValue())));
							}
							//								url="/iopush/#/plan/summary/?orderId=" + iopushPayment.getAgreementId();
							username=(Object)iopushPayment.getFkProductId();




							/************************************************************/
							//*************Step 1. going to cancel last paypal agreement with paypal and update status in DB
							/************************************************************/
							logger.info("paymentExecute, going to cancel last aggrement.");

							if(responsenew.getStatus()){

								IopushPayment iopushPaymentcancle =  (IopushPayment) responsenew.getData().get(0);
								String aggrementId=iopushPaymentcancle.getAgreementId();
								String cancelurl= myProperties.getProperty(env+"createAgreementURL")+aggrementId+"/cancel";
								CancelAgreementBean cancelAgreementBean = new CancelAgreementBean("Canceling the profile.");
								String jsonRequest=gson.toJson(cancelAgreementBean);

								PaypalResponse cancelResponse = PaypalUtility.executePayment(cancelurl, accessToken, jsonRequest);
								if(cancelResponse.getStatus()){
									if(cancelResponse.getStatusCode()==200){

										logger.info("paymentExecute, cancelPaypalAggrement,paypal agreement has SuccessFully cancelled.going to update IopushPayment detail.");
										iopushPaymentcancle.setAgreementStatus(Constants.PAYAL_AGREEMENT_STATUS_CANCEL);
										iopushPaymentcancle.setCancellationDate(new Date());
										iopushPaymentcancle.setModifiedBy("System");
										iopushPaymentcancle.setModifiedOn(new Date());
										Response resPaymentDetail= this.iExternalAPIDao.updatePaymentDetail(iopushPaymentcancle);
										logger.info("paymentExecute,cancelPaypalAggrement , paymentDetail SuccessFully updated. paymentDetail :[{}] ",iopushPaymentcancle.toString());
									}
									else{
										logger.info("paymentExecute,cancelPaypalAggrement, unable to cancel paypal .paypal return cancel status code : [{}]",cancelResponse.getStatusCode());	
									}
								}
							}else{
								logger.info("paymentExecute : pappayment_detail doesn't  exist for prodID=[{}] and status [{}]",productId,Constants.PAYAL_AGREEMENT_STATUS_ACTIVE);
							}
							/**********!!!!***********/


							//********************************************************************************************************************
							/***************Going to update user category when Paypal reponse 200 and status = active***************************/
							//********************************************************************************************************************
							logger.info("PaymentExecute going to update paypal payment[{}]",iopushPayment.toString());
							Response objResponse = this.iPaypalDaoImpl.updatePaypalPaymentDetails(iopushPayment);
							if(objResponse.getStatus())
							{
								objResponse = this.iPaypalDaoImpl.getPackageDetails(iopushPayment.getFkPackageId());

								if(objResponse.getStatus()){

									IopushPackagePlan iopushPackage = (IopushPackagePlan) objResponse.getScalarResult();
									logger.info("paymentExecute ,IopushPackagePlan successFully retrieved.IopushPackagePlan details:[{}]",iopushPackage.toString());
									Response resnew =iPaypalDao.findUsercategory(productId);
									if(resnew.getStatus()){

										iopushUsercategory= (IopushUsercategory) resnew.getData().get(0);
										logger.info("paymentExecute  Usercategory successFully retrieved for prodID = [{}] ",productId);
										iopushUsercategory.setPlanId(iopushPayment.getFkPlanId());
										iopushUsercategory.setTotal(iopushPackage.getSubscribersLimit());
//										int available = iopushPackage.getSubscribersLimit()-(iopushUsercategory.getUsed()+iopushUsercategory.getOverLimitSubscribersValue());
										int available = iopushPackage.getSubscribersLimit()-(iopushUsercategory.getUsed());
										if( available >0)
										{
											iopushUsercategory.setBalance(available);
											iopushUsercategory.setLimitExceed(Constants.LIMIT_NOT_EXCEED);
//											iopushUsercategory.setUsed(iopushUsercategory.getUsed()+iopushUsercategory.getOverLimitSubscribersValue());
											
											iopushUsercategory.setOverLimitSubscribersValue(0);
										}
										else{
											iopushUsercategory.setBalance(0);
											iopushUsercategory.setLimitExceed(Constants.LIMIT_EXCEED);
											iopushUsercategory.setOverLimitSubscribersValue(Math.abs(available));
//											iopushUsercategory.setUsed();
										}
										iopushUsercategory.setProduct_id(productId);
										//iopushUsercategory.setUsed(0);
										//iopushUsercategory.setLimitExceed(Constants.LIMIT_NOT_EXCEED);
										iopushUsercategory.setModifiedOn(new Date());
										logger.info("paymentExecute  going to updateUsercategory .");
										Response result = this.iPaypalDaoImpl.updateUsercategory(iopushUsercategory);
										if (result.getStatus())
											planCost = iopushPackage.getPricing();
										logger.info("paymentExecute  updateUsercategory Successfully updated.[ {}]",iopushUsercategory.toString());
									}
									else{
										logger.info("paymentExecute  Usercategory  doesn't exist for prodId[{}]",productId);
									}
								}
							} else {
								logger.info("paymentExecute :selectedUserPlan selected plan doesn't exist for planID=[ "+ iopushPayment.getFkPlanId() + " productID ="
										+ iopushPayment.getFkProductId() + "payment id"+ iopushPayment.getPaymentId());
							}
						}else {
							//********************************************************************************************************************
							/***************Going to update user category when Paypal reponse 200 and state = cancelled***************************/
							//********************************************************************************************************************
							//case 1. status 200 Ok from paypal ,  (state = Cancelled ) but unable to deduct money because card is removed before final transaction , state = Cancelled
							iopushPayment.setAgreementStatus(paymentExecuteMainBean.getState());
							iopushPayment.setTpAcknowledgement(1);
							Response objResponse = this.iPaypalDaoImpl.updatePaypalPaymentDetails(iopushPayment);
						}
					}
					else {
						executePaymentErrorBean = gson.fromJson((String) excuteResponse.getScalarResult(),ExecutePaymentErrorBean.class);
						paypalResultJson = gson.toJson(executePaymentErrorBean);
						logger.info("PaymentExecute : get paypalExecuteApi  response bean  [{}].",executePaymentErrorBean.toString());

						iopushPayment.setTpAcknowledgement(1);
						Response objResponse = this.iPaypalDaoImpl.updatePaypalPaymentDetails(iopushPayment);
					}
					IopushPaypalTransactionLog iopushTransactionLog = new IopushPaypalTransactionLog();
					iopushTransactionLog.setResp_log(paypalResultJson);
					iopushTransactionLog.setIopushPayPalPayment(iopushPayment.getPaymentId());
					logger.info("paymentExecute going to save Transaction Log . ["+ iopushTransactionLog + "]");
					response = this.iPaypalDaoImpl.saveTransaction(iopushTransactionLog);
					if (response.getStatus()) {

						logger.info("paymentExecute Transaction Log Successfully saved. transaction id is [{}]",response.getIntegerResult());
						String host = myProperties.getProperty(env + "IMAGEURL");
						httpurl = (excuteResponse.getStatusCode() == 200)	? host +((paymentExecuteMainBean.getState()).equalsIgnoreCase("Active")?"/iopush/#/plan/summary/?orderId=" + iopushPayment.getAgreementId():("/iopush/#/plan/error/?name= 'Error' &message= Initial Payment Transaction failed.'"))	: host + "/iopush/#/plan/error/?name=" + executePaymentErrorBean.getName() + "&message="+ executePaymentErrorBean.getMessage();
						rm=new ResponseMessage(Constants.SUCCESS_CODE,httpurl,username);
						logger.info("httpurl [{}]",httpurl);
					} else {
						logger.info("paymentExecute unable to save Transaction Log.");
					}
				} else {
					logger.info("paymentExecute : unable to execute Payment.exception occurred in executePayment method "+ excuteResponse.getException());
				}
			}else {
				logger.info("paymentExecute : pappayment_url doesn't  exist for paymentId=[{}]",tptoken);
			}
		}catch (Exception e) {
			logger.error("paymentExecute  Error ", e);
			e.printStackTrace();
			rm=new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,httpurl);
		}
		return rm;
	}

	@Override
	public JsonResponse<IoPushPlanInfoBean> fetchPaymenInfo(int prodId, String paymentId) {

		logger.info("inside fetchPaymentInfo method, agreement id is {}",paymentId);
		JsonResponse<IoPushPlanInfoBean> planInfo = null;
		Response response = this.iPaypalDaoImpl.fetchPlanInfo(prodId, paymentId);
		if (response.getStatus()) {
			IopushPayment iopushPayment = (IopushPayment) response.getScalarResult();
			int packageId=iopushPayment.getFkPackageId();
			int planId=iopushPayment.getFkPlanId();
			logger.info("package id is {} plan id is {}",packageId,planId);
			List<Object[]> Packageresponse = this.iPaypalDaoImpl.fetchPackageInfo(packageId,planId);

			String packageName=(String) Packageresponse.get(0)[0];
			int pricing=(int) Packageresponse.get(0)[1];
			int subscriberLimit=(int) Packageresponse.get(0)[2];

			IoPushPlanInfoBean bean = new IoPushPlanInfoBean();

			bean.setPlanName(packageName);
			bean.setAmount(pricing);
			bean.setSubscriberLimit(subscriberLimit);
			bean.setPaymentId(paymentId);
			planInfo = new JsonResponse<IoPushPlanInfoBean>("Success", bean);

		} else {
			planInfo = new JsonResponse<IoPushPlanInfoBean>("Error", "Something went wrong.");
		}

		return planInfo;
	}

	@Override
	public ResponseMessage sendEmailReport() {

		logger.info("inside sendEmailReport method, going to fetch the user details");
		ResponseMessage responsemessage =null;;
		// calling dao to get the details from paymentCancelledUserInfo table.
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.YYYY");
		String text = date.format(formatters);
		text=text.trim();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String path = myProperties.getProperty(env + "FOLDER");
		String fileName = (path + "/" + System.currentTimeMillis() + ".csv");
		List<com.saphire.iopush.bean.IoPushPaymentCancelledUserInfo> user = new ArrayList<>();
		// now we need to get the user details from paypal payment table for the
		// user who did not complete the payment
		List<Object[]> userDetails = iPaypalDaoImpl.getIncompletePaymentUserInfo();
		if (!(userDetails.isEmpty())) {

			for (Object[] userInfo : userDetails) {
				String userName = (String) userInfo[0];
				String email = (String) userInfo[1];
				String mobileNumer = (String) userInfo[2];
				String planInfo = String.format("%s :: €%d /MONTH -UP TO %d", (String) userInfo[3],
						(Integer) userInfo[4], (Integer) userInfo[5]);
				Date datePurchase = (Date) userInfo[6];
				IoPushPaymentCancelledUserInfo details = new IoPushPaymentCancelledUserInfo();
				details.setUserName(userName);
				details.setPackageName(planInfo);
				details.setPhoneNumber(mobileNumer);
				details.setEmail(email);
				user.add(details);
			}
			logger.info("got the user details, now CSV file will be written");
			if (Utility.purchaseCancellationCSVFile(user, fileName)) {

				logger.info("CSV file is successfully written, preparing to send the email");
				String errorMessage = "";
				try {

					String htmlMsg = String.format(myProperties.getProperty(env + "PURCHASE_CANCELLATION_MAIL_BODY"), text);
					MimeMessage mimeMessage = mailSender_support.createMimeMessage();
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
					helper.setTo(myProperties.getProperty(env + "PURCHASE_CANCELLATION_MAIL_TO").split(","));
					helper.setSubject("ioPush - purchases cancellation report");
					helper.setText(htmlMsg, true);
					FileSystemResource file = new FileSystemResource(fileName);
					helper.addAttachment(myProperties.getProperty(env + "PURCHASE_CANCELLATION_MAIL_ATTACHMENT_FILE_NAME"), file);
					mailSender_support.send(mimeMessage);
					//					response.setStatus(true);
					//					response.setMessage("EmailReport sent successfully");
					responsemessage=new ResponseMessage(Constants.SUCCESS_CODE,"EmailReport sent successfully !");
					logger.info("Email sent successfully");
				} catch (Exception e) {
					responsemessage=new ResponseMessage(Constants.ERROR_CODE_INVALID,"some error occured while sending email!");
					logger.error("some error occured while sending email.", e);
				}

			} else {
				logger.info("Some error while creating the csv file");
				responsemessage=new ResponseMessage(Constants.ERROR_CODE_INVALID,"Some error while creating the csv file!");

			}
		} else {
			logger.info("No user record found for the current date");
			responsemessage=new ResponseMessage(Constants.SUCCESS_CODE,"No user record found for the current date!");
		}

		/*
		 * else { logger.info("No user record found for the current date");
		 * response.setStatus(false);
		 * response.setMessage("No user record found for the current date"); }
		 */
		return responsemessage;
	}


	@Override
	@Transactional
	public JsonResponse<PlanDetailsBean> fetchUserPlan(int productId) {

		logger.info("inside fetchUserPlan.");
		JsonResponse<PlanDetailsBean> jsonResponse =null;
		List<PlanDetailsBean> listPlan = new ArrayList<PlanDetailsBean>();
		Response response = new Response();
		try{
			Object[] listPayment = iPaypalDaoImpl.getCurrentUserPlan(productId);
			if(listPayment != null)
			{
				int planId = Utility.intConverter(""+listPayment[1]);
				int packageId = Utility.intConverter(""+listPayment[0]);
				//			   select plan_ids having higher preferences
				List<Integer> listPlans = iPaypalDaoImpl.getPlanWithHighPreference(planId);
				Integer[] planIds= new Integer[listPlans.size()];
				int i =0;
				for(Integer object: listPlans)
				{
					planIds[i] = object;
					i++;
				}
				List<Object[]> listPackages = iPaypalDaoImpl.getPackages(planIds, packageId);
				for(Object[] object: listPackages)
				{
					listPlan.add(new PlanDetailsBean(Utility.intConverter(""+object[0]), Utility.intConverter(""+object[1]), Utility.intConverter(""+object[2]), Utility.intConverter(""+object[3]), ""+object[4]));
				}
			}
			else
			{
				List<Object[]> listAllPackages = iPaypalDaoImpl.getAllPlans();
				if(!listAllPackages.isEmpty())
				{
					for(Object[] object: listAllPackages)
					{
						listPlan.add(new PlanDetailsBean(Utility.intConverter(""+object[0]), Utility.intConverter(""+object[1]), Utility.intConverter(""+object[2]), Utility.intConverter(""+object[3]), ""+object[4]));
					}
				}
			}
			jsonResponse= new JsonResponse<PlanDetailsBean>(Constants.SUCCESS, listPlan, listPlan.size());
			logger.info("UserPlan Data successfully retrieved. ["+listPlan+ ", TotalRecordCount ::"+ listPlan.size() +" ]");
		}
		catch(Exception e){

			logger.error("fetchUserPlan some error occured." + e);
			jsonResponse = new JsonResponse<PlanDetailsBean>("ERROR", "An error occurred.");
			e.printStackTrace();
		}
		return jsonResponse;
	}



	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage sendRenewalStatusMail(Map<Integer, Boolean> renewalStatusMap) {

		logger.info("inside sendRenewalStatusMail");
		ResponseMessage resmessage= null;
		try
		{
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");	
			Calendar cal = Calendar.getInstance();
			String newdate=sdf.format(cal.getTime());
			String fileName=myProperties.getProperty(env +"FAIL_RENEWAL_CUSTOMER_REPORT")+"FailCustomerRenewal."+newdate+"_"  +System.currentTimeMillis()+".csv";
			if(!renewalStatusMap.isEmpty())
			{
				List <Integer> failedprodIDList=new ArrayList <Integer>();
				for (Map.Entry<Integer, Boolean> entry : renewalStatusMap.entrySet()) {

					logger.info("sendRenewalStatusMail, going to fetch renewalUserdetails.");
					Integer prodID=entry.getKey() ;
					Boolean paymentStatus=entry.getValue();
					logger.info("sendRenewalStatusMail,Renewal Payment Status for the prod Id {} is {}",prodID,paymentStatus);


					if(paymentStatus)
					{
						// if the payment is success then intimate the customer
						Response userResponse =new Response();
						userResponse=this.iPaypalDao.getUserInfo(prodID);
						IopushUser user= (IopushUser) userResponse.getData().get(0); 

						if(user.getEmailId()!=null)
						{
							logger.info("sendRenewalStatusMail preapring to send email to {} ",user.getEmailId());
							String htmlMsg = String.format(myProperties.getProperty(env +"RENEW_SUCCESS_MAIL_BODY"), user.getFirstName());
							MimeMessage mimeMessage = mailSender_service.createMimeMessage();
							MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
							mimeMessage.setContent(htmlMsg, "text/html");
							helper.setTo(user.getEmailId());
							helper.setSubject(myProperties.getProperty(env+"RENEW_SUCCESS_EMAIL_SUBJECT", "Subscription renewal"));
							mailSender_service.send(mimeMessage);
							logger.info("sendRenewalStatusMail ,message sent successfully");
						}
						else
						{
							logger.info("sendRenewalStatusMail ,email id was null for {}",user.getFirstName());
						}
					}
					else {
						// if the payment is failed then intimate the client
						failedprodIDList.add(prodID);
					}
				}
				if(!(failedprodIDList.isEmpty()))
				{
					Response userResponse =new Response();
					Response paymentResponse = new Response();
					List<RenewalFailedInfoBean> failedUserlist = new ArrayList<>();

					for(int prodID:failedprodIDList)
					{
						userResponse=this.iPaypalDao.getUserInfo(prodID);
						List <IopushUser>userList=(List<IopushUser>) userResponse.getData();
						for(IopushUser user :userList)
						{
							paymentResponse=this.iPaypalDao.getPaymentInfo(prodID);
							IopushPayment payment= (IopushPayment) paymentResponse.getScalarResult();
							RenewalFailedInfoBean bean= new RenewalFailedInfoBean () ;
							bean.setFirstName(user.getFirstName());
							bean.setDate(""+payment.getModifiedOn());
							bean.setAgreementStatus(payment.getAgreementStatus());
							bean.setEmailId(user.getEmailId());
							bean.setOutstandingBalance(""+payment.getOutstandingBalance());
							bean.setPhoneNumber(user.getPhoneNumber());
							bean.setRenewAmount(""+payment.getRenewAmount());
							failedUserlist.add(bean);
						}
					}
					if(!failedUserlist.isEmpty())
					{
						// create csv file for the user info
						String[] columns = new String[]{"First Name","Email Id","Phone Number","Renew Amount","Creation Date","Agreement Status","OutStanding Balance"};
						if(Utility.makeFailCustomerRenewalReportCsvFile(failedUserlist, fileName,columns))
						{
							logger.info("sendRenewalStatusMail, Status Fail, csv document is created with file name =  {}",fileName);
							String htmlMsg = String.format(myProperties.getProperty(env + "FAIL_RENEWAL_CUSTOMER_REPORT_MAIL_BODY"),newdate);
							MimeMessage mimeMessage = mailSender_service.createMimeMessage();
							MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
							helper.setTo(myProperties.getProperty(env+"FAIL_RENEWAL_CUSTOMER_MAIL_TO").split(","));
							helper.setSubject(myProperties.getProperty(env +"FAIL_RENEWAL_CUSTOMER_MAIL_SUBJECT"));
							helper.setText(htmlMsg, true);
							FileSystemResource file = new FileSystemResource(fileName);
							helper.addAttachment(String.format(myProperties.getProperty(env+"FAIL_RENEWAL_CUSTOMER_MAIL_ATTACHMENT_FILE"),newdate),file);
							mailSender_service.send(mimeMessage);
							logger.info("sendRenewalStatusMail,Status Fail,Renewal failed report successfully sent");
						}
						else
						{
							logger.info("sendRenewalStatusMail,Status Fail,Some error while creating csv file");
						}
					}
					else
					{
						// no response for user 
						logger.info("sendRenewalStatusMail ,No failed user Info ");
					}
				}
				else
				{
					// no failed user 
					logger.info("sendRenewalStatusMail ,No failed renewal");
				}
			}
			else
			{
				logger.info("sendRenewalStatusMail ,Renewal Status hashmap is empty");
			}
		}
		catch (Exception e){
			e.printStackTrace();
			logger.info("sendRenewalStatusMail, error occured.[{}]",e.getMessage());
			resmessage= new ResponseMessage(Constants.ERROR_CODE_INVALID,e.getMessage());
		}
		return resmessage;
	}



	private List<Object[]> getUserInfoForAgreementIdArray(List<String> failedAgreementIDList) {

		List<Object[]> response=this.iPaypalDao.getUserInfoForAgreementIdArray(failedAgreementIDList);
		return null;
	}
	private List<Object[]> getUserInfoForAgreementId(String agreementID) {

		List<Object[]> response=this.iPaypalDao.getUserInfoForAgreementId(agreementID);
		return response;

	}
	private void sendRenewMail(String body,String toMail,String subject) {
		logger.info("checkRenewals sending mail...");
		Response response = new Response();
		String errorMessage = "";
		try {
			MimeMessage mimeMessage = mailSender_service.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
			mimeMessage.setContent(body, "text/html");
			helper.setTo(toMail);
			//helper.setBcc(bcc);
			helper.setSubject(subject);
			mailSender_service.send(mimeMessage);
			response.setStatus(true);
			logger.info("checkRenewals,renew mail sent successfully To [{}]",toMail);
		} catch (Exception e) {
			response.setStatus(false);
			response.setMessage(errorMessage);
			e.printStackTrace();
			logger.error("checkRenewals,renew mail some error occured.", e);
		}
	}


	private PaypalCreatePlanBean createPaypalPaymentRequest(String packageName,int amount, Integer productID,String frequency,int frequencyInterval, int maxFailAttempt ) {

		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String host = myProperties.getProperty(env + "IMAGEURL");
		PaypalCreatePlanBean paypalCreatePlanBean = new PaypalCreatePlanBean ();
		PaypalMerchantPreferences merchant_preferences= new PaypalMerchantPreferences();
		setupFee fee=merchant_preferences.new setupFee(""+amount,Constants.PAYPAL_REQUEST_CURRENCY );
		merchant_preferences.setSetup_fee(fee);
		merchant_preferences.setAuto_bill_amount(Constants.PAYPAL_PLAN_YES);
		merchant_preferences.setInitial_fail_amount_action(Constants.PAYPAL_PLAN_CANCEL);
		merchant_preferences.setMax_fail_attempts(""+maxFailAttempt);
		merchant_preferences.setReturn_url(host +myProperties.getProperty(env + "paypalReturnURL"));
		merchant_preferences.setCancel_url(host+myProperties.getProperty(env + "paypalCancelURL"));
		paypalCreatePlanBean.setMerchant_preferences(merchant_preferences);
		paypalCreatePlanBean.setDescription(Constants.PAYPAL_REQUEST_DESCRIPTION);
		paypalCreatePlanBean.setName(packageName);
		paypalCreatePlanBean.setType(Constants.PAYPAL_PLAN_TYPE);
		PaypalPaymentDefinitions[] paymentDefinitionArray= new PaypalPaymentDefinitions[1];
		PaypalPaymentDefinitions paymentDefinition=new PaypalPaymentDefinitions();
		paymentDefinition.setCycles("0");
		paymentDefinition.setFrequency(frequency);
		paymentDefinition.setFrequency_interval(""+frequencyInterval);
		paymentDefinition.setName(Constants.PAYPAL_PAYMENT_DEFINETION);
		paymentDefinition.setType(Constants.PAYPAL_PAYMENT_TYPE);
		Amount amountobj= paymentDefinition.new Amount( Constants.PAYPAL_REQUEST_CURRENCY ,""+amount);
		paymentDefinition.setAmount(amountobj);
		paymentDefinitionArray[0]=paymentDefinition;
		paypalCreatePlanBean.setPayment_definitions(paymentDefinitionArray);
		return paypalCreatePlanBean;
	}



	private PaypalCreateAgreementRequestBean createPaypalCreateAgreementRequestBean(String packageName, int amount,String planID,String frequency,int frequencyInterval) {


		Calendar date = Calendar.getInstance();
		//date.setTimeInMillis(date_in_mil);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		if("day".equalsIgnoreCase(frequency))
		{
			date.add(Calendar.DATE, frequencyInterval);
		}
		else
		{
			if("month".equalsIgnoreCase(frequency))
			{
				date.add(Calendar.MONTH, frequencyInterval);
			}
		}
		logger.info("PaypalCreateAgreementRequestBean ,agreement start date is[{}]",date.getTime());
		PaypalCreateAgreementRequestBean agreementRequestBean= new PaypalCreateAgreementRequestBean();
		agreementRequestBean.setDescription(Constants.PAYPAL_REQUEST_DESCRIPTION);
		agreementRequestBean.setName(packageName);
		// hard code value

		agreementRequestBean.setStart_date(format.format(date.getTime()));
		PaypalCreatePlanBean plan= new PaypalCreatePlanBean();
		plan.setId(planID);
		agreementRequestBean.setPlanBean(plan);
		Payer payer=agreementRequestBean.new Payer(Constants.PAYPAL_REQUEST_PAYMENT_METHOD);
		agreementRequestBean.setPayer(payer);
		return agreementRequestBean;
	}




	@Override
	public void checkRenewals() {

		logger.info("inside checkRenewals.. ");
		String email ="", date = "", firstName = "";
		try{
			//first we need to check the upcoming renewal
			Response responseRenewal=iPaypalDao.getRenewConfig();
			if(responseRenewal.getStatus())
			{
				IopushRenewalConfig config=(IopushRenewalConfig) responseRenewal.getData().get(0);
				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				String nodays=""+config.getMailDuration();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				int days=Integer.parseInt(nodays);
				Calendar c = Calendar.getInstance();
				Date startDate=dateFormat.parse(dateFormat.format(new Date()));
				c.add(Calendar.DATE, days);
				Date endDate=Utility.addDaysInDate(startDate, days, "yyyy/MM/dd");
				Response renewDetails=iPaypalDaoImpl.getRenewalInfo(startDate,endDate);

				if(renewDetails.getStatus())
				{
					List<IopushPayment> list=(List<IopushPayment>) renewDetails.getData();


					logger.info("no. of  renewals are {} and user list [{}]",list.size(),list);
					// we need to find the renewal for the current date and insert in the renew table
					for(IopushPayment payment :list)
					{
						IopushRenewalCustomer renew= new IopushRenewalCustomer(); 

						renew.setFkProductId(payment.getFkProductId());
						renew.setPaymentID(payment.getPaymentId());
						renew.setLastPaymentDate(payment.getPaymentdate());
						renew.setNextRenewDate(payment.getNextPaymentdate());
						renew.setTotalAmount(payment.getRenewAmount());
						renew.setOutstandingBalance(payment.getOutstandingBalance());
						renew.setAgreementID(payment.getAgreementId());
						Response latestRenewSaveResponse=iPaypalDaoImpl.saveRenewInfo(renew);
						if(latestRenewSaveResponse.getStatus())
						{

							logger.info("renew info successfully saved ");
							//after saving into db we need to send the email to all the users
							//							for(IopushPayment paymentInfo:list)
							//							{
							int prodID=payment.getFkProductId();


							// query the db to get the user details
							Response userResponse=iPaypalDaoImpl.getUserInfo(prodID);
							if(userResponse.getStatus())
							{
								IopushUser user=(IopushUser) userResponse.getData().get(0);
								logger.info("checkRenewals , userdetail of ProdId [{}] ",prodID);
								email=user.getEmailId();
								firstName=user.getFirstName();
								int renewAmount=payment.getRenewAmount();
								Double outstandingAmount=payment.getOutstandingBalance();
								Date renewDate=payment.getNextPaymentdate();
								date = new SimpleDateFormat("dd.MM.yyyy").format(renewDate);
								if(email!=null && !email.isEmpty())
								{
									String subject=String.format(myProperties.getProperty(env+ "RENEW_EMAIL_SUBJECT"),date);
									String htmlMsg = String.format(myProperties.getProperty(env + "RENEW_MAIL_BODY"),firstName,date);
									this.sendRenewMail(htmlMsg , email,subject);
								}
							}
							else
							{
								logger.info("no user details was fetched for the prodID {}" ,prodID);
							}
						}
						else
						{
							logger.info("error while saving renew info");
						}
					}

				}
				else
				{
					logger.info("no renewals founds");
				}
			}
			else
			{
				logger.info("no renewals config fetched from the table");
			}
		}
		catch(Exception e)
		{
			logger.info("An exception occurred ", e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Response  updateRenewalStatus() {

		logger.info(" inside updateRenewalStatus .");
		//step 1: Hit the db and fetch the agreements whose renewal date was yesterday
		Response finalResponse =new Response();
		try
		{

			Gson gson = new Gson();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			String env = myProperties.getProperty("ENVIORAMENT") + ".";
			//	String fetchAgreementURL=myProperties.getProperty(env+"checkRenewalURL");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Calendar c = Calendar.getInstance();
			Date date=dateFormat.parse(dateFormat.format(new Date()));

			logger.info(" updateRenewalStatus method,going to fetch the renewals for the previous day of {}",date);
			Response renewalResponse=iPaypalDaoImpl.fetchRenewals(date);

			// this agreement Success list will contains all the renewal payment success agreement id
			HashMap<Integer, Boolean > statusMap = new HashMap<Integer,Boolean>();
			List <String> successAgreementList= new ArrayList();
			String token=null;
			if(renewalResponse.getStatus())
			{
				List<IopushRenewalCustomer> list=(List<IopushRenewalCustomer>) renewalResponse.getData();
				// after creating url we need to hit the paypal to fetch the status
				String clientID = myProperties.getProperty(env + "clientUserName");
				String secretPassword = myProperties.getProperty(env + "clientSecretPassword");
				String tokenURL = myProperties.getProperty(env + "tokenURL");
				token = PaypalUtility.getAccesstoken(clientID, secretPassword, tokenURL);
				logger.info("updateRenewalStatus ,no of renewals fetched is {}",list.size());
				Set<Integer>deleteList= new HashSet<>();
				// step 2: fetch the renewal status for each agreement id
				for(IopushRenewalCustomer renew : list)
				{
					String agreementID=renew.getAgreementID();
					//int paymentID=renew.getPaymentID();
					//	String paymentID=renew.getAgreementID();
					int paymentID=renew.getPaymentID();
					String fetchAgreementURL=myProperties.getProperty(env+"checkRenewalURL");
					fetchAgreementURL=fetchAgreementURL+""+agreementID;
					logger.info("updateRenewalStatus ,going to check the renewal status for the agreement id {}",agreementID);
					PaypalResponse agreementResponse=PaypalUtility.executeGETRequest(fetchAgreementURL, token);
					Response responseInfo=this.iPaypalDaoImpl.findPaymentDetails(paymentID);
					IopushPayment paymentObj=(IopushPayment) responseInfo.getData().get(0);
					Boolean insertStatus=true;
					if(agreementResponse.getStatus())
					{
						/**
						 *	get the agreement reponse and fetch the values for business logic 
						 */
						IopushPayment paymentInfo=new IopushPayment();
						if(200==agreementResponse.getStatusCode())
						{
							Object jsonResponse=agreementResponse.getScalarResult();
							logger.debug("updateRenewalStatus ,json response for the agreement id {} is {}",agreementID,jsonResponse);
							PaymentExecuteMainBean createPaymentBean =null;
							//Convert the json response to object
							createPaymentBean= gson.fromJson((String) jsonResponse,PaymentExecuteMainBean.class) ;
							//	if("ACTIVE".equalsIgnoreCase(createPaymentBean.getState()))
							paymentInfo.setAgreementId(createPaymentBean.getId());
							paymentInfo.setAgreementStatus(createPaymentBean.getState());
							paymentInfo.setOutstandingBalance(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getOutstandingBalanceBean().getValue()));
							//paymentInfo.setAmount(paymentObj.getAmount());
							// new date
							Date dateNow= new Date();
							paymentInfo.setCreationDate(dateNow);
							paymentInfo.setFkPackageId(paymentObj.getFkPackageId());
							paymentInfo.setFkPlanId(paymentObj.getFkPlanId());
							paymentInfo.setFkProductId(paymentObj.getFkProductId());
							if(("Active".equalsIgnoreCase(createPaymentBean.getState())))
							{
								paymentInfo.setNextPaymentdate(format.parse(createPaymentBean.getPaymentAgreementDetailsBean().getNext_billing_date()));
							}
							else
							{
								if(("Cancelled").equalsIgnoreCase(createPaymentBean.getState()))
								{
									//delete the entry from renewal cunstomer if the status is cancelled
									deleteList.add(renew.getCustomer());
									paymentInfo.setCancellationDate(dateNow);
									paymentObj.setCancellationDate(dateNow);
								}
								paymentInfo.setNextPaymentdate(null);
							}
							Date lastBillingDate=null;
							Double lastPaymentAmount=null;
							if(createPaymentBean.getPaymentAgreementDetailsBean().getLast_payment_date() !=null) 
							{
								lastBillingDate=format.parse(createPaymentBean.getPaymentAgreementDetailsBean().getLast_payment_date());
								lastPaymentAmount=Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getLastPaymentAmountBean().getValue());
								//paymentInfo.setAmount();
							}
							paymentInfo.setPaymentMethod(Constants.PAYPAL_REQUEST_PAYMENT_METHOD);
							paymentInfo.setAmount(lastPaymentAmount);
							paymentInfo.setPaymentdate(lastBillingDate);
							paymentInfo.setPaymentType(Constants.PAYPAL_TRANSACTION_TYPE_RENEW);
							paymentInfo.setRenewAmount(paymentObj.getRenewAmount());
							paymentInfo.setTpAcknowledgement(1);
							//paymentInfo.setTpExecuteURL("");
							//paymentInfo.setAmount(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getLastPaymentAmountBean().getValue()));
							// business logic for setting the renewal payment status goes here
							//if(format.parse(createPaymentBean.getPaymentAgreementDetailsBean().getLast_payment_date())>renew.getLastPaymentDate())
							if(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getOutstandingBalanceBean().getValue())>renew.getOutstandingBalance() && Integer.parseInt(createPaymentBean.getPaymentAgreementDetailsBean().getFailed_payment_count())>paymentObj.getfailedPaymentCount())
							{
								// if the outstanding balance increases and no of failed payment increases means the renewal payment has failed
								// update the renew object and payment object as well
								paymentInfo.setRenewalPaymentStatus(false);

								renew.setOutstandingBalance(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getOutstandingBalanceBean().getValue()));
								renew.setNextRenewDate(paymentInfo.getNextPaymentdate());

							}
							else
							{
								// payment is done
								if("active".equalsIgnoreCase(paymentInfo.getAgreementStatus()))
								{
									paymentInfo.setRenewalPaymentStatus(true);

								}
								else
								{
									paymentInfo.setRenewalPaymentStatus(false);
								}
								deleteList.add(renew.getCustomer());
							}

							paymentInfo.setfailedPaymentCount(Integer.parseInt(createPaymentBean.getPaymentAgreementDetailsBean().getFailed_payment_count()));

							//paymentInfo.setOutstandingBalance(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getOutstandingBalanceBean().getValue()));

							// logic to check if the payment object needs to be persisted or not 
							/*
							 * check if the payment type is Renew
							 * payment status was false
							 *and
							 *again the payment got failed 
							 * 
							 * */
							Response response=null;
							if("renew".equalsIgnoreCase(paymentObj.getPaymentType()) && !(paymentObj.getRenewalPaymentStatus()) && !(paymentInfo.getRenewalPaymentStatus()))
							{

								logger.info("updateRenewalStatus ,since the payment type was renew, last payment status was false and current renewal payment status is false, so it wont persist in payment table");
								//paymentInfo.setPaymentId(paymentObj.getPaymentId());
								paymentObj.setAgreementStatus(paymentInfo.getAgreementStatus());
								//paymentObj.setCreationDate(paymentInfo.getCreationDate());
								paymentObj.setfailedPaymentCount(paymentInfo.getfailedPaymentCount());
								paymentObj.setNextPaymentdate(renew.getNextRenewDate());
								paymentObj.setOutstandingBalance(renew.getOutstandingBalance());
								paymentObj.setPaymentType(paymentInfo.getPaymentType());
								paymentObj.setRenewalPaymentStatus(paymentInfo.getRenewalPaymentStatus());
								// since this renew cycle is of failed payment hence the amount will be zero
								paymentObj.setAmount(paymentInfo.getAmount());
								paymentObj.setfailedPaymentCount(paymentInfo.getfailedPaymentCount());

								// newly addedd column
								paymentObj.setModifiedOn(dateNow);
								paymentObj.setModifiedBy(Constants.SYSTEM);



								response=this.iPaypalDaoImpl.updatePayment(paymentObj);
							}
							else
							{
								//paymentInfo.setAmount(Double.parseDouble(createPaymentBean.getPaymentAgreementDetailsBean().getLastPaymentAmountBean().getValue()));
								// newly added columns
								paymentInfo.setCreatedBy(Constants.SYSTEM);
								paymentInfo.setModifiedBy(Constants.SYSTEM);
								paymentInfo.setModifiedOn(dateNow);


								//								logger.info("going to save the payment details, payment object is {}",paymentInfo.toString());
								response=this.iPaypalDaoImpl.savePayment(paymentInfo);
							}


							if(response.getStatus())
							{
								logger.info("updateRenewalStatus ,payment details inserted successfully");
								paymentInfo.setPaymentId(response.getIntegerResult());
								// need to save the transaction as well
								IopushPaypalTransactionLog iopushPaypalTransactionLog = new IopushPaypalTransactionLog();
								// hardcoding paypal payment for testing
								// transactional behaviour
								// iopushPayPalPayment.setPayment_id(250);
								iopushPaypalTransactionLog.setIopushPayPalPayment((paymentObj.getPaymentId()));
								iopushPaypalTransactionLog.setResp_log((String) agreementResponse.getScalarResult());
								logger.debug("updateRenewalStatus ,going to save the transaction in transaction log table, transaction object is {} ", iopushPaypalTransactionLog.toString());
								Response paymentTransactionSaveResponse = iPaypalDaoImpl.saveTransction(iopushPaypalTransactionLog);
								if (paymentTransactionSaveResponse.getStatus()) {
									logger.info("Transaction log successfully saved");
								}

								// now update the user category table.
								Response planresponse=this.iPaypalDaoImpl.findPackageDetail(paymentObj.getFkPackageId());
								Response userResponse=this.iPaypalDaoImpl.findUsercategory(paymentObj.getFkProductId());
								IopushPackagePlan packagePlan=(IopushPackagePlan) planresponse.getData().get(0);
								IopushUsercategory userCategory=(IopushUsercategory) userResponse.getData().get(0);
								logger.info("updateRenewalStatus ,renewal payment for the id {} is {}",paymentInfo.getAgreementId(),paymentInfo.getRenewalPaymentStatus());
								if(paymentInfo.getRenewalPaymentStatus())
								{

									userCategory.setModifiedOn(new Date());
									//									userCategory.setBalance(userCategory.getBalance());
									//									userCategory.setUsed(0);
									//									userCategory.setTotal(packagePlan.getSubscribersLimit());
									userCategory.setLimitExceed(Constants.LIMIT_NOT_EXCEED);
									//since the renewal payment was successfull for this agreement id so this user needs to be informed about the successfull renewal
									statusMap.put(paymentInfo.getFkProductId(), true);

									//userCategory.setTotal(plan.get);
									//iopushUsercategory.setBalance(paymentObj.getFkPlanId());

								}
								//in case of failed payment 
								else
								{
									userCategory.setModifiedOn(new Date());
									//									userCategory.setBalance(0);
									//									userCategory.setTotal(0);
									//									userCategory.setUsed(0);
									userCategory.setLimitExceed(Constants.LIMIT_RENEWAL_FALSE);
									statusMap.put(paymentInfo.getFkProductId(), false);
								}
								// going to update the user category table 
								Response result = this.iPaypalDaoImpl.updateUsercategory(userCategory);
								if(result.getStatus())
								{
									logger.info("updateRenewalStatus ,user category table updated sucessfully");
								}
								else
								{
									logger.info("updateRenewalStatus ,error while updating user category");
								}
							}	else
							{
								logger.info("updateRenewalStatus ,some error while saving the payment details");
							}
						}
						else
						{
							//some error while fetching status from paypal
							logger.info("updateRenewalStatus ,did not get the 200 http response for the agreement id {},response will be written to paypal transaction table",renew.getAgreementID());
							String jsonResponse1=(String) agreementResponse.getScalarResult();
							IopushPaypalTransactionLog iopushPaypalTransactionLog = new IopushPaypalTransactionLog();
							iopushPaypalTransactionLog.setIopushPayPalPayment(paymentObj.getPaymentId());
							iopushPaypalTransactionLog.setResp_log(jsonResponse1);
							logger.debug("updateRenewalStatus ,going to save the transaction in transaction log table, transaction object is {} ", iopushPaypalTransactionLog.toString());
							Response paymentTransactionSaveResponse = iPaypalDaoImpl.saveTransction(iopushPaypalTransactionLog);
						}
					}
					else
					{
						logger.info("updateRenewalStatus ,some error while fetching the renewal status for the agreement id {} exception is {}",renew.getCustomer(),agreementResponse.getException());
						//set the error response
					}
				}

				// finally delete all the rows
				for(IopushRenewalCustomer customer: list)
				{
					logger.info("updateRenewalStatus ,going to delete the renew details for the id {}",customer.getCustomer());
					this.iPaypalDaoImpl.deleteRenewCostumer(customer);
				}
				if(!(statusMap.isEmpty()))
				{
					// if there are suceessful renewal payment than add it to final response 
					finalResponse.setScalarResult(statusMap);
				}
			}
			else
			{
				logger.info("updateRenewalStatus ,NO renewal found for the date{}",date);
			}
		}
		catch(Exception e)
		{
			logger.error("updateRenewalStatus ,Exception while updating renewal status,exception is {}",e);
		}

		return finalResponse;
	}



	@Override
	public void checkIPN(HttpServletRequest request) {

		logger.info("Inside the ipn");
		try {
			String requestParams = this.getAllRequestParams(request);
			IpnInfoBean ipnInfo = new IpnInfoBean();
			Enumeration en = request.getParameterNames();
			String str = "cmd=_notify-validate";
			while(en.hasMoreElements()){
				String paramName = (String)en.nextElement();
				String paramValue = request.getParameter(paramName);

				str = str + "&" + paramName + "=" + URLEncoder.encode(paramValue, "UTF-8");

			}

			// post back to PayPal system to validate
			// NOTE: change http: to https: in the following URL to verify using SSL (for increased security).
			// using HTTPS requires either Java 1.4 or greater, or Java Secure Socket Extension (JSSE)
			// and configured for older versions.
			String url=myProperties.getProperty("PAYPAL_IPN_URL");

			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			uc.setDoOutput(true);
			uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			PrintWriter pw = new PrintWriter(uc.getOutputStream());
			pw.println(str);
			pw.close();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(uc.getInputStream()));
			String res = in.readLine();
			in.close();

			// assign posted variables to local variables
			ipnInfo.setLogTime(System.currentTimeMillis());
			ipnInfo.setItemName(request.getParameter("item_name"));
			ipnInfo.setItemNumber(request.getParameter("item_number"));
			ipnInfo.setPaymentStatus(request.getParameter("payment_status"));
			ipnInfo.setPaymentAmount(request.getParameter("mc_gross"));
			// ipnInfo.setPaymentCurrency(request.getParameter("mc_currency"));
			ipnInfo.setTxnId(request.getParameter("txn_id"));
			ipnInfo.setReceiverEmail(request.getParameter("receiver_email"));
			ipnInfo.setPayerEmail(request.getParameter("payer_email"));
			ipnInfo.setResponse(res);
			ipnInfo.setRequestParams(requestParams);
			// agreement cancel
			ipnInfo.setTransactionType(request.getParameter("txn_type"));
			ipnInfo.setAgreeementId(request.getParameter("recurring_payment_id"));

			if ("VERIFIED".equalsIgnoreCase(res)) {

				//check if the transaction type is agreementCancelled

				if(ipnInfo.getTransactionType().equalsIgnoreCase("recurring_payment_profile_cancel"))
				{
					logger.info("ipn received is of type agreement cancelled");
					//check if we have agreement id for this
					String agreementID=ipnInfo.getAgreeementId();
					Response response=this.iPaypalDao.getPaymentInfo(agreementID);
					String env = myProperties.getProperty("ENVIORAMENT") + ".";
					if(response.getStatus()){
						logger.info("agreement id is found in db");
						IopushPayment payment=(IopushPayment) response.getData().get(0);
						if(payment.getAgreementStatus().equalsIgnoreCase(Constants.PAYAL_AGREEMENT_STATUS_ACTIVE))
						{
							payment.setAgreementStatus(Constants.PAYAL_AGREEMENT_STATUS_CANCEL);
							payment.setCancellationDate(new Date());
							payment.setModifiedBy(Constants.SYSTEM);
							Date date= new Date();
							payment.setModifiedOn(date);
							//payment.setCreationDate(new Date());
							this.iPaypalDao.updatePayment(payment);

							/*Response resnew =iPaypalDao.findUsercategory(payment.getFkProductId());
							if(resnew.getStatus()){

							IopushUsercategory iopushUsercategory= (IopushUsercategory) resnew.getData().get(0);
							int balance = 0;
							iopushUsercategory.setTotal(0);
							iopushUsercategory.setUsed(0);
							iopushUsercategory.setBalance(balance);
							iopushUsercategory.setLimitExceed(Constants.LIMIT_CANCEL);
							iopushUsercategory.setModifiedOn(new Date());

								logger.info("paymentExecute  going to updateUsercategory .");
								this.iPaypalDaoImpl.updateUsercategory(iopushUsercategory);*/

								Response userResponse=iPaypalDaoImpl.getUserInfo(payment.getFkProductId());
								IopushUser user=(IopushUser) userResponse.getData().get(0);
								// need to get the user details.
								String email=user.getEmailId();
								String firstName=user.getFirstName();
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");	
								Calendar cal = Calendar.getInstance();
								String newdate = simpleDateFormat.format(cal.getTime());
								String subject=myProperties.getProperty(env+"TERMINATION_MAIL_SUBJECT");
								String htmlMsg = String.format(myProperties.getProperty(env+"TERMINATION_MAIL_BODY"),firstName,newdate);
								logger.info("Termination sending mail...");
								this.sendRenewMail(htmlMsg , email,subject);
							}
						}
					}


				}

			}

			//check notification validation
			//res.equalsIgnoreCase("");
//		}

		catch (Exception e) {
			logger.error("Some error while processing ipn, error is {}",e.getMessage());
		}
	}

	private String getAllRequestParams(HttpServletRequest request) {

		Map map = request.getParameterMap();
		StringBuilder sb = new StringBuilder("\nREQUEST PARAMETERS\n");
		for (Iterator it = map.keySet().iterator(); it.hasNext();)
		{
			String pn = (String)it.next();
			sb.append(pn).append("\n");
			String[] pvs = (String[]) map.get(pn);
			for (int i = 0; i < pvs.length; i++) {
				String pv = pvs[i];
				sb.append("\t").append(pv).append("\n");
			}
		}
		return sb.toString();
	}
	@Override
	public Response sendupgradepackageMail(int prodID){

		logger.info("sendupgradepackageMail sending mail...");
		Response response = new Response();
		String errorMessage = "";

		Response res =this.iPaypalDao.getUserInfo(prodID);
		if(res.getStatus()){

			IopushUser iopushUser=(IopushUser) res.getData().get(0);    

			try {

				String env = myProperties.getProperty("ENVIORAMENT") + ".";

				String lastname=iopushUser.getLastName()==null?"":iopushUser.getLastName();
				String firstname=iopushUser.getFirstName()==null?"":iopushUser.getFirstName();
				
				String htmlMsg = String.format(myProperties.getProperty(env + "UPGRADE_PACKAGE_MAIL_BODY"),firstname +" "+lastname);

				MimeMessage mimeMessage = mailSender_service.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
				mimeMessage.setContent(htmlMsg, "text/html");
				helper.setTo(iopushUser.getEmailId());
				//		    helper.setTo(userBean.getEmailId());
				//helper.setBcc(myProperties.getProperty(env + "UPGRADE_PACKAGE_MAIL_TO").split(","));
				//		    errorMessage = myProperties.getProperty(env + "EMAIL_ERROR","User Registration is successful, some error while sending mail");
				helper.setSubject(myProperties.getProperty(env+"UPGRADE_PACKAGE_MAIL_SUBJECT", "ioPUSH package upgrade!"));
				mailSender_service.send(mimeMessage);
				response.setStatus(true);
				logger.info("sendupgradepackageMail message sent successfully");
			} catch (Exception e) {
				response.setStatus(false);
				response.setMessage(errorMessage);
				logger.error("sendupgradepackageMail some error occured.", e);
			}
		}
		else{

			logger.error("sendupgradepackageMail No user found");

		}
		return response;

	}
	@Override
	public Response sendPurchaseConfirmationMail(int prod) {


		logger.info("sendPurchaseConfirmationMail sending mail...");
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		Response response = new Response();
		String errorMessage = "";
		int planId=0; 
		String packagename="";

		Response res =this.iPaypalDao.getUserInfo(prod);


		if(res.getStatus()){

			logger.info("sendPurchaseConfirmationMail userINfo Successfully retrieved .going fetch userCategory" );	
			Response userCategoryRes = this.iPaypalDao.findUsercategory(prod);
			if(userCategoryRes.getStatus()){

				planId=((IopushUsercategory) userCategoryRes.getData().get(0)).getPlanId();

				logger.info("sendPurchaseConfirmationMail usercategory retrieved ...Plan ID: [{}]" ,planId);
				Response planres=this.iPaypalDaoImpl.findPlanDetail(planId);
				if(planres.getStatus()){
					packagename=( (IopushPlan) planres.getData().get(0)).getPlanName();


					IopushUser iopushUser=(IopushUser) res.getData().get(0); 
					try {

						
						String lastname=iopushUser.getLastName()==null?"":iopushUser.getLastName();
						String firstname=iopushUser.getFirstName()==null?"":iopushUser.getFirstName();
						
						String htmlMsg = String.format(myProperties.getProperty(env +"PURCHASE_CONFORM_MAIL_BODY"), firstname+" "+lastname,packagename);

						MimeMessage mimeMessage = mailSender_service.createMimeMessage();
						MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
						mimeMessage.setContent(htmlMsg, "text/html");
						helper.setTo(iopushUser.getEmailId());
						//			    helper.setTo(userBean.getEmailId());
						//helper.setBcc(myProperties.getProperty(env + "PURCHASE_CONFORM__MAIL_TO").split(","));
						//			    errorMessage = myProperties.getProperty(env + "EMAIL_ERROR","User Registration is successful, some error while sending mail");
						helper.setSubject(myProperties.getProperty(env+"PURCHASE_CONFORM_MAIL_SUBJECT", "Confirmation of Purchase"));
						mailSender_service.send(mimeMessage);
						response.setStatus(true);
						logger.info("sendPurchaseConfirmationMail message sent successfully");
					} catch (Exception e) {
						response.setStatus(false);
						response.setMessage(errorMessage);
						logger.error("sendPurchaseConfirmationMail some error occured.", e);
					}
				}
				else{

					logger.error("sendPurchaseConfirmationMail No record  in user_plan related to plan ID[{}] ",planId);
				}
			}
			else{

				logger.error("sendPurchaseConfirmationMail No userCategory  record");
			}
		}
		else{

			logger.error("sendPurchaseConfirmationMail No user found");

		}
		return response;
	}
	@Override
	public Response findCustomPaypalResponse(String paymentId) {

		logger.info("inside findCustomPaypalResponse..");
		Response response = new Response();
		String content;
		try {
			content = new String(Files.readAllBytes(Paths.get("/var/www/html/paypalcustom/" + paymentId)));
			response.setMessage(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response ;
	}
}
