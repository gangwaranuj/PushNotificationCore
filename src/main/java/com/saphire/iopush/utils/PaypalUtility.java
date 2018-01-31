package com.saphire.iopush.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.saphire.iopush.bean.AccessToken;
import com.saphire.iopush.bean.PaypalCreateAgreementRequestBean;
import com.saphire.iopush.bean.PaypalCreatePaymentBean;
import com.saphire.iopush.bean.PaypalCreatePlanBean;
import com.saphire.iopush.bean.PaypalPlanActivateRequest;

public class PaypalUtility {

	private static Logger logger = LoggerFactory.getLogger(PaypalUtility.class);

	public static PaypalResponse createPaymentRequest(String clientID, String secretPassword, String paymentURL,
			PaypalCreatePaymentBean beanObject) {


		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		CloseableHttpClient client = HttpClients.createDefault();
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(beanObject);
		logger.info("Paypal payment request created is [" + jsonRequest + "]");
		PaypalResponse finalresponse = new PaypalResponse();
		finalresponse.setStatus(false);
		try {
			HttpPost httpost = new HttpPost(paymentURL);
			httpost.addHeader("Content-Type", "application/json");
			List<NameValuePair> params = new ArrayList<>();
			// params.add(new BasicNameValuePair("grant_type",
			// "client_credentials"));
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(clientID, secretPassword);
			// httpost.setEntity(new UrlEncodedFormEntity(params));
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
			httpost.addHeader(new BasicScheme().authenticate(creds, httpost, null));
			httpost.setEntity(requestEntity);
			logger.info("Executing request " + httpost.getRequestLine());
			CloseableHttpResponse response = client.execute(httpost);
			try {

				int status = response.getStatusLine().getStatusCode();
				logger.info("paypal payment response status code is [" + status + "]");

				if (status==201) {
					//	Gson gson1 = new Gson();
					PaypalCreatePaymentBean Sample = gson.fromJson(EntityUtils.toString(response.getEntity()),
							PaypalCreatePaymentBean.class);
					String jsonResponse = gson.toJson(Sample);
					// String Sample1 =
					// gson1.fromJson(EntityUtils.toString(response.getEntity()),String.class);
					logger.info("paypal json Response is " + jsonResponse);
					logger.info("paypal payment respone object is [" + Sample.toString() + "]");
					finalresponse.setStatus(true);
					finalresponse.setJsonResponse(jsonResponse);
					finalresponse.setScalarResult(Sample);
				} else {
					logger.info("paypal payment request is not successfull");
					finalresponse.setStatus(true);
					String paypalFailedPaymentResponse=(EntityUtils.toString(response.getEntity()));

					finalresponse.setJsonResponse(paypalFailedPaymentResponse);
				}

			} finally {
				response.close();
			}
		} catch (AuthenticationException | IOException e) {
			e.printStackTrace();
			logger.error("error while paypal create payment api call, error is [" + e + "]");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("error while paypal create payment api call, error is [" + e + "]");
				e.printStackTrace();
			}
		}
		return finalresponse;

	}


	//getAccessToken method

	// this method is used to get the access token from paypal
	public static String getAccesstoken(String userName, String secretPassword, String tokenURL)
			throws IOException, AuthenticationException {

		// make http post call to paypal to get the access token
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		CloseableHttpClient client = HttpClients.createDefault();
		String result = null;
		try {
			HttpPost httpost = new HttpPost(tokenURL);
			httpost.addHeader("Content-Type", "application/x-www-form-urlencoded");
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("grant_type", "client_credentials"));
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(userName, secretPassword);
			httpost.setEntity(new UrlEncodedFormEntity(params));
			httpost.addHeader(new BasicScheme().authenticate(creds, httpost, null));

			CloseableHttpResponse response = client.execute(httpost);
			try {

				int status = response.getStatusLine().getStatusCode();

				if (status==200) {
					Gson gson = new Gson();
					AccessToken Sample = gson.fromJson(EntityUtils.toString(response.getEntity()), AccessToken.class);
					result = Sample.getAccess_token();

				} else {

				}

			} finally {
				response.close();
			}
		} finally {
			client.close();
		}

		return result;
	}




	// this method is used to get the access token from paypal
	public static PaypalResponse executePayment(String paymentApprovalUrl, String accessToken, String Bean)
	{
		//make http post call to paypal to get the access token
		CloseableHttpClient client = HttpClients.createDefault(); 
		PaypalResponse paypalResponse=null;
		CloseableHttpResponse response = null;
		Gson  gson = new Gson();
		try {
			HttpPost httpost = new HttpPost(paymentApprovalUrl);
			httpost.addHeader("Content-Type", "application/json");
			httpost.addHeader("Authorization", "Bearer" +" "+accessToken);

			httpost.setEntity(new StringEntity(Bean,ContentType.create("application/json")));
			response = client.execute(httpost);

			StatusLine status = response.getStatusLine() ;
			if(status.getStatusCode()==200  )
			{
				String successResponse = (EntityUtils.toString(response.getEntity()));
				paypalResponse= new PaypalResponse(true,successResponse, Constants.SUCCESS_CODE);
			}
			else if(status.getStatusCode()==204){

				paypalResponse= new PaypalResponse(true,"", Constants.SUCCESS_CODE);
			}
			else{

				String errorResponse =(EntityUtils.toString(response.getEntity()));
				if(errorResponse!=null){
					paypalResponse= new PaypalResponse(true,errorResponse,Constants.BAD_REQUEST);
				}

				else{
					String error=gson.toJson("Did not get any response from paypal.");
					paypalResponse= new PaypalResponse(true,error,Constants.BAD_REQUEST);
				}
			}
		}
		catch(Exception exception){
			exception.getMessage(); 
			paypalResponse= new PaypalResponse(false,Constants.ERROR,exception);
			exception.printStackTrace();
		}
		finally{
			if(client != null || response != null)
				try
			{
					client.close();
					response.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}

		}
		return paypalResponse;
	}


	public static Map<String, String> getQueryMap(String query)  
	{  
		String[] params = query.split("&");  
		Map<String, String> map = new HashMap<String, String>();  
		for (String param : params)  
		{  String [] p=param.split("=");
		String name = p[0];  
		if(p.length>1)  {String value = p[1];  
		map.put(name, value);
		}  
		}  
		return map;  
	}

	public static PaypalResponse createPlanRequest(String clientID, String secretPassword, String paymentURL,
			PaypalCreatePlanBean paypalCreatePlanBean, String token) {


		//CredentialsProvider credsProvider = new BasicCredentialsProvider();
		CloseableHttpClient client = HttpClients.createDefault();
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(paypalCreatePlanBean);
		logger.info("Paypal Plan request created is [" + jsonRequest + "]");
		PaypalResponse finalresponse = new PaypalResponse();
		finalresponse.setStatus(false);
		try {
			HttpPost httpost = new HttpPost(paymentURL);
			httpost.addHeader("Content-Type", "application/json");
			httpost.addHeader("Authorization", "Bearer" +" "+token);
			List<NameValuePair> params = new ArrayList<>();
			// params.add(new BasicNameValuePair("grant_type",
			// "client_credentials"));
			UsernamePasswordCredentials creds = new UsernamePasswordCredentials(clientID, secretPassword);
			// httpost.setEntity(new UrlEncodedFormEntity(params));
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
			httpost.addHeader(new BasicScheme().authenticate(creds, httpost, null));
			httpost.setEntity(requestEntity);
			logger.info("Executing request " + httpost.getRequestLine());
			CloseableHttpResponse response = client.execute(httpost);
			try {

				int status = response.getStatusLine().getStatusCode();
				logger.info("paypal create Plan response status code is [" + status + "]");

				if (status==201) {
					// Gson gson1 = new Gson();
					PaypalCreatePlanBean Sample = gson.fromJson(EntityUtils.toString(response.getEntity()),
							PaypalCreatePlanBean.class);
					String jsonResponse = gson.toJson(Sample);
					// String Sample1 =
					// gson1.fromJson(EntityUtils.toString(response.getEntity()),String.class);
					logger.info("paypal json Response is " + jsonResponse);
					logger.info("paypal payment respone object is [" + Sample.toString() + "]");
					finalresponse.setStatus(true);
					finalresponse.setJsonResponse(jsonResponse);
					finalresponse.setScalarResult(Sample);
				} else {
					logger.info("paypal create plan request is not successfull");
					finalresponse.setStatus(false);
					String paypalFailedPaymentResponse=(EntityUtils.toString(response.getEntity()));

					finalresponse.setJsonResponse(paypalFailedPaymentResponse);
				}

			} finally {
				response.close();
			}
		} catch (AuthenticationException | IOException e) {
			e.printStackTrace();
			logger.error("error while paypal create plan api call, error is [" + e + "]");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("error while paypal create plan api call, error is [" + e + "]");
				e.printStackTrace();
			}
		}
		return finalresponse;

	}


	public static PaypalResponse activatePlan(String url,String token, PaypalPlanActivateRequest[] reqArray) {


		//CredentialsProvider credsProvider = new BasicCredentialsProvider();
		CloseableHttpClient client = HttpClients.createDefault();
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(reqArray);
		logger.info("Paypal payment request created is [" + jsonRequest + "]");
		PaypalResponse finalresponse = new PaypalResponse();
		finalresponse.setStatus(false);
		try {
			HttpPatch httppatch= new HttpPatch(url);
			//HttpPost httpost = new HttpPost(url);
			httppatch.addHeader("Content-Type", "application/json");
			httppatch.addHeader("Authorization", "Bearer" +" "+token);
			List<NameValuePair> params = new ArrayList<>();
			// params.add(new BasicNameValuePair("grant_type",
			// "client_credentials"));
			// UsernamePasswordCredentials creds = new UsernamePasswordCredentials(clientID, secretPassword);
			// httpost.setEntity(new UrlEncodedFormEntity(params));
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
			//httpost.addHeader(new BasicScheme().authenticate(creds, httpost, null));
			httppatch.setEntity(requestEntity);
			logger.info("Executing request " + httppatch.getRequestLine());
			CloseableHttpResponse response = client.execute(httppatch);
			try {

				int status = response.getStatusLine().getStatusCode();
				logger.info("paypal payment response status code is [" + status + "]");

				if (status==200) {
					// Gson gson1 = new Gson();
					finalresponse.setStatus(true);
				} else {
					logger.info("paypal plan activate request is not successfull");
					finalresponse.setStatus(false);
				}

			} finally {
				response.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("error while paypal create payment api call, error is [" + e + "]");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("error while paypal create payment api call, error is [" + e + "]");
				e.printStackTrace();
			}
		}
		return finalresponse;

	}

	public static PaypalResponse createAgreementRequest(String clientID,String secretPassword,String string, String token, PaypalCreateAgreementRequestBean agreementRequestBean) {


		//CredentialsProvider credsProvider = new BasicCredentialsProvider();
		CloseableHttpClient client = HttpClients.createDefault();
		Gson gson = new Gson();
		String jsonRequest = gson.toJson(agreementRequestBean);
		logger.info("Paypal payment request created is [" + jsonRequest + "]");
		PaypalResponse finalresponse = new PaypalResponse();
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(clientID, secretPassword);
		finalresponse.setStatus(false);
		try {
			HttpPost httpost = new HttpPost(string);
			httpost.addHeader("Content-Type", "application/json");
			httpost.addHeader("Authorization", "Bearer" +" "+token);
			List<NameValuePair> params = new ArrayList<>();
			// params.add(new BasicNameValuePair("grant_type",
			// "client_credentials"));
			// httpost.setEntity(new UrlEncodedFormEntity(params));
			StringEntity requestEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
			//httpost.addHeader(new BasicScheme().authenticate(httpost, null));
			httpost.addHeader(new BasicScheme().authenticate(creds, httpost, null));
			httpost.setEntity(requestEntity);
			logger.info("Executing request " + httpost.getRequestLine());
			CloseableHttpResponse response = client.execute(httpost);
			try {

				int status = response.getStatusLine().getStatusCode();
				logger.info("paypal payment response status code is [" + status + "]");

				if (status==201) {
					// Gson gson1 = new Gson();
					PaypalCreateAgreementRequestBean Sample = gson.fromJson(EntityUtils.toString(response.getEntity()),
							PaypalCreateAgreementRequestBean.class);
					String jsonResponse = gson.toJson(Sample);
					// String Sample1 =
					// gson1.fromJson(EntityUtils.toString(response.getEntity()),String.class);
					logger.info("paypal json Response is " + jsonResponse);
					logger.info("paypal payment respone object is [" + Sample.toString() + "]");
					finalresponse.setStatus(true);
					finalresponse.setJsonResponse(jsonResponse);
					finalresponse.setScalarResult(Sample);
				} else {
					logger.info("paypal payment request is not successfull");
					finalresponse.setStatus(false);
					String paypalFailedPaymentResponse=(EntityUtils.toString(response.getEntity()));

					finalresponse.setJsonResponse(paypalFailedPaymentResponse);
				}

			} finally {
				response.close();
			}
		} catch (IOException | AuthenticationException e) {
			e.printStackTrace();
			logger.error("error while paypal create payment api call, error is [" + e + "]");
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				logger.error("error while paypal create payment api call, error is [" + e + "]");
				e.printStackTrace();
			}
		}

		return finalresponse;



	}
	//
	// this is the generic method used to make http get request to paypal

	public static PaypalResponse executeGETRequest(String paymentApprovalUrl, String accessToken)
	{
		//make http post call to paypal to get the access token
		CloseableHttpClient client = HttpClients.createDefault(); 
		PaypalResponse paypalResponse=null;
		CloseableHttpResponse response = null;
		Gson  gson = new Gson();
		try {
			HttpGet httpget = new HttpGet(paymentApprovalUrl);
			//HttpPost httpost = new HttpPost("https://api.sandbox.paypal.com/v1/payments/payment/PAY-3VY818518G269232WLFDW7IQ/execute");
			httpget.addHeader("Content-Type", "application/json");
			httpget.addHeader("Authorization", "Bearer" +" "+accessToken);
			//httpost.addHeader("Authorization", "Bearer" +" "+"asdfasdfasdfasdfasdfasdf");

			// httpget.setEntity(new StringEntity(Bean,ContentType.create("application/json")));
			response = client.execute(httpget);

			StatusLine status = response.getStatusLine() ;
			if(status.getStatusCode()==200)
			{
				String successResponse = (EntityUtils.toString(response.getEntity()));
				paypalResponse= new PaypalResponse(true,successResponse, Constants.SUCCESS_CODE);
			}
			else{

				String errorResponse = (EntityUtils.toString(response.getEntity()));
				if(errorResponse!=null){
					paypalResponse= new PaypalResponse(true,errorResponse,Constants.BAD_REQUEST);
				}

				else{
					String error=gson.toJson("Did not get any response from paypal.");
					paypalResponse= new PaypalResponse(true,error,Constants.BAD_REQUEST);
				}
			}
		}
		catch(Exception exception){
			exception.getMessage(); 
			paypalResponse= new PaypalResponse(false,Constants.ERROR,exception);
		}
		finally{
			if(client != null || response != null)
				try
			{
					client.close();
					response.close();
			}
			catch(Exception e)
			{
				e.getMessage();
			}

		}
		return paypalResponse;
	}







}
