package com.saphire.iopush.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.dao.IUserDao;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.service.IUserService;
import com.saphire.iopush.utils.Constants;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;
import com.saphire.iopush.utils.SecurityUtils;
import com.saphire.iopush.utils.Utility;



public class UserDetailsServiceImpl
implements UserDetailsService,IUserService
{
	private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class); 
	@Autowired IUserDao userDao;

	@Autowired
	Properties myProperties;
//	@Autowired 	JavaMailSender mailSender;
	@Autowired JavaMailSender mailSender_service;
	@Autowired JavaMailSender mailSender_support;


	public void setUserDao(IUserDao userDao)
	{
		this.userDao = userDao;
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		return this.userDao.findUserByName(username);
	}



	public ResponseMessage changePassword(String currentPassword,String newPassword)
	{	
		logger.info("changePassword currentPassword is [ "+currentPassword+" ] newPassword is [ "+newPassword+" ]");	
		String user=SecurityUtils.getCurrentLogin(); 
		logger.info("Inside changePassword method, current user is [ " +user+"]");

		logger.debug("going to check the user credentials");
		IopushUser iopushUser = (IopushUser)this.userDao.findUserByName(user);


		String error = "";
		ResponseMessage responseMessage = new ResponseMessage();

		try {
			//if(Utility.md5Encryption(currentPassword).equalsIgnoreCase(iopushUser.getPassword()))
			if(currentPassword.equalsIgnoreCase(iopushUser.getPassword()))
			{
				logger.debug("Entered password matched with the current password.");
				try {

					//if(((Utility.md5Encryption(newPassword).equalsIgnoreCase(iopushUser.getLastPassword()))))
					if(((newPassword.equalsIgnoreCase(iopushUser.getLastPassword()))))
					{
						error= "Password should not same as previous password.";
					}
					//else if((newPassword.equalsIgnoreCase(iopushUser.getUsername())))
					else if((newPassword.equalsIgnoreCase(iopushUser.getUsername())))
					{
						error= "Password should not be same as username.";   
					}
					else
					{
						iopushUser.setModifiedOn(new Date());


				//		iopushUser.setLastPassword(Utility.md5Encryption(currentPassword));
						iopushUser.setLastPassword(currentPassword);
					//	iopushUser.setPassword(Utility.md5Encryption(newPassword));


						iopushUser.setPassword(newPassword);
					}

				} catch (Exception e) {

					error= e.getMessage();
					e.printStackTrace();

				}

				if(!(error.length()>0))
				{
					logger.debug("going to save the new password in db");
					Response response=this.userDao.updateUser(iopushUser);
					if(response.getStatus())
					{
						logger.info("Password updated successfully.");
						responseMessage= new ResponseMessage(Constants.SUCCESS_CODE, "Password updated successfully.");
					}

				}
				else
				{
					responseMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID,error);
				}
			}
			else
			{
				logger.info("password did not match with the existing password");
				responseMessage= new ResponseMessage(Constants.ERROR_CODE_INVALID,  "Password entered did not match with the existing password.");
			}
		} catch (Exception e) {
			logger.error("failed to update the password with error [" +e+"]");
			responseMessage= new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
		}


		return responseMessage;
	}



	//************************Upload User Photo************************//

	public JsonResponse<UserBean> uploadImage(String imageData,int userId)
	{
		logger.info("Inside the upload image method for userId[" +userId+"]");
		JsonResponse<UserBean> responsebean = new JsonResponse<UserBean>();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String imagePath = myProperties.getProperty(env +"USER_IMG_FOLDER");
		String imageurl = myProperties.getProperty(env + "IMAGEURL");
		String fileExtension = Utility.getExtension(imageData);
		String fileName= "" + userId + "." + fileExtension;
		String folderPath = Constants.USER_IMAGE_FOLDER;
		String base64Image = imageData.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(new File(imagePath + fileName));
			file.write(imageBytes);
			if(logger.isDebugEnabled())
				logger.debug("going to save the image in path[" +folderPath+"] with file name [" +fileName+"]");

			IopushUser iopushUser =	(IopushUser)this.userDao.findUserById(userId);
			iopushUser.setImagePath(folderPath + fileName);

			this.userDao.updateUser(iopushUser) ;

			logger.info("image saved successfully [" + imageurl + folderPath + fileName + "]");
			UserBean userBean = new UserBean() ;
			userBean.setImagePath(imageurl + folderPath + fileName);
			responsebean= new JsonResponse<UserBean>(Constants.SUCCESS, userBean);
		}    
		catch(Exception e)
		{
			logger.error("error saving image with error[" + e.getMessage() + "]");
			responsebean = new JsonResponse<UserBean>(Constants.ERROR, "Failed to upload the image with exception[" + e.getMessage() +"]");
		}

		finally{
			try {
				file.close();
			} catch (IOException e) {
				responsebean = new JsonResponse<UserBean>(Constants.ERROR, "Failed to upload the image with exception[" +e.getMessage()+"]");
			}
		}
		return responsebean;
	}

	//*****************************Update Profile*****************************//
	public ResponseMessage updateProfile(UserBean user)
	{
		logger.info("Inside update Profile method userID is [ " +user.getUserId()+"]");
		ResponseMessage responseMessage=null;
		//IcmpUser icmpUser=this.getUserDetails(user);
		try{
			if(logger.isDebugEnabled())
				logger.debug("going to check the user details in  db");
			IopushUser iopushUser = (IopushUser) this.userDao.findUserById(user.getUserId());

			if(iopushUser!=null)
			{
				if(logger.isDebugEnabled())
					logger.debug("user is available ");

				if(user.getSalutation()!= null && ! user.getSalutation().isEmpty())
				{
					logger.debug("salutation is [" +user.getSalutation()+"]" );
					iopushUser.setSalutation(user.getSalutation());
				}

				if(user.getTitle()!= null && ! user.getTitle().isEmpty())
				{
					logger.debug("title is [" +user.getTitle()+"]" );
					iopushUser.setTitle(user.getTitle());
				}

				if(user.getCompany()!= null && ! user.getCompany().isEmpty())
				{
					logger.debug("company is [" +user.getCompany()+"]" );
					iopushUser.setCompany(user.getCompany());
				}

				if(user.getPhoneNumber()!= null && ! user.getPhoneNumber().isEmpty())
				{
					logger.debug("phone number is [" +user.getPhoneNumber()+"]" );
					iopushUser.setPhoneNumber(user.getPhoneNumber());
				}

				if(user.getMiddleName()!= null && ! user.getMiddleName().isEmpty())
				{
					logger.debug("middleName is [" +user.getMiddleName()+"]" );
					iopushUser.setMiddleName(user.getMiddleName());
				}

				if(user.getEmailId()!= null && ! user.getEmailId().isEmpty())
				{
					logger.debug("emailId is [" +user.getEmailId()+"]" );
					iopushUser.setEmailId(user.getEmailId());
				}

				if(user.getFirstName()!=null && user.getFirstName().isEmpty()){
					iopushUser.setEmailId(user.getFirstName());
				}

				logger.debug("going to save the user details in db");
				Response response = this.userDao.updateUser(iopushUser);

				if(response.getStatus())
				{
					logger.info("User updated successfully");
					responseMessage= new ResponseMessage(Constants.SUCCESS_CODE, "User updated successfully.");
				}

			}
			else
			{
				logger.info("User does not exist");
				responseMessage = new ResponseMessage(Constants.ERROR_CODE_INVALID, "User does not exist.");
			}
		}
		catch(Exception e)
		{
			logger.error("failed to update the user:",e);
			responseMessage= new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to update the user with error [" + e.getMessage() + "]");
		}

		return responseMessage;

	}

	@Override
	public ResponseMessage deleteImage(int userId) {
		logger.info("Inside remove image method");
		IopushUser iopushUser = (IopushUser) this.userDao.findUserById(userId);
		ResponseMessage message=  new ResponseMessage();
		if(iopushUser!=null)
		{
			try{
				String env=myProperties.getProperty("ENVIORAMENT");
				String folder=myProperties.getProperty(env+"."+"FOLDER");
				File userfolder=new File(myProperties.getProperty(env+"."+"USER_IMG_FOLDER"));
				if(iopushUser.getImagePath().length()>0)
				{
					if(Utility.deleteResourceFromPath(folder+iopushUser.getImagePath())){
						if(logger.isDebugEnabled())
							logger.debug("File deleted successfully");

						for(File f: userfolder.listFiles()){
							if(f.getName().startsWith(""+userId))
								f.delete();
						}

						iopushUser.setImagePath("");
						this.userDao.updateUser(iopushUser);
						message= new ResponseMessage(Constants.SUCCESS_CODE, "Image deleted. ");
					}else{
						if(logger.isDebugEnabled())
							logger.debug("Delete operation failed.");
						message = new ResponseMessage(Constants.ERROR_CODE_INVALID, "Failed to delete the file.");
					}
				}
				else
				{
					message=new ResponseMessage(Constants.ERROR_CODE_INVALID, "No image path found in db.");
				}
			}catch(Exception e){
				message = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, "Failed to delete the file with exception [" +e+"]");

			}

		}
		else
		{
			logger.info("user does not exist");
			message = new ResponseMessage(Constants.ERROR_CODE_INVALID, "User does not exist.");
		}

		return message;
	}

	@Override
	public ResponseMessage forgetpassword(String email) {
		// Username Detail
		ResponseMessage rm = null;
		try {
			logger.info("forgetPassword email " + email);
			Response response = userDao.findUserByMail(email);
			if (response.getStatus()) {
				String env = myProperties.getProperty("ENVIORAMENT") + ".";
				String subject = myProperties.getProperty(env + "FORGET_PASSWORD_SUBJECT");
				int noOfCAPSAlpha = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_CAPS_ALPHA", "1"));
				int noOfDigits = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_DIGITS", "1"));
				int noOfSplChars = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_NO_OF_SPL_CHARACTER", "1"));
				int minLen = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_MIN_LENGTH", "8"));
				int maxLen = Utility.intConverter(myProperties.getProperty(env + "FORGET_PASSWORD_MAX_LENGTH", "12"));
				String  bccMail = myProperties.getProperty(env + "FORGET_PASSWORD_BCC_MAIL","salgotra.jagdish@thinksys.com");
				String password = new String(Utility.generatePswd(minLen, maxLen, noOfCAPSAlpha, noOfDigits, noOfSplChars));

				IopushUser iopushUser = (IopushUser) response.getScalarResult() ;
				//				String fname=iopushUser.getFirstName();
				//				 String lname=iopushUser.getLastName();

				iopushUser.setPassword(password);
				String htmlMsg = String.format(myProperties.getProperty(env + "FORGET_PASSWORD_BODY"), iopushUser.getFirstName() + "" + (iopushUser.getLastName() == null?"":" "+iopushUser.getLastName()),iopushUser.getUsername(), password);

				if(logger.isDebugEnabled())
					logger.debug("Forget Password username[ " + email + "], htmlMsg[" + htmlMsg+"]");

				htmlMsg = String.format(htmlMsg, iopushUser.getFirstName() + "" + (iopushUser.getLastName() == null?"":" "+iopushUser.getLastName()),password);
				htmlMsg = htmlMsg.replaceAll("USERNAME", iopushUser.getFirstName() + "" + (iopushUser.getLastName() == null?"":" "+iopushUser.getLastName())).replaceAll("USERPASSWORD", password);

				if(logger.isDebugEnabled())
					logger.debug("Forget Password username [" + email + "], htmlMsg:" + htmlMsg + ",subject[" + subject
							+ "],emailid[" + iopushUser.getEmailId() + "]");

				response = userDao.updateUser(iopushUser);

				logger.info("Forget Password email: " + email + " Change Password  " + response.getStatus());
				logger.info("Forget Password email: " + email + " Send Mail  to " + iopushUser.getEmailId());

				MimeMessage mimeMessage = mailSender_support.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
				mimeMessage.setContent(htmlMsg, "text/html");
				helper.setTo(email);
				//helper.setBcc("salgotra.jagdish@thinksys.com");
				helper.setBcc(bccMail);
				helper.setSubject(subject);
				mailSender_support.send(mimeMessage);

				logger.info("Successfully Send Mail  to "+iopushUser.getEmailId());
				rm=new ResponseMessage(Constants.SUCCESS_CODE, " We have sent an email to "+ iopushUser.getEmailId() +" with your new password.");

			}else{
				rm=new ResponseMessage(Constants.ERROR_CODE_INVALID, "This Email Id doesn't exist, please enter valid Id.");
				logger.info("Forget Password emailid: "+email+" does not exist");
			}
		}catch(Exception e){
			rm=new ResponseMessage(Constants.ERROR_CODE_UNKNOWN,e.getMessage());
			logger.error("Forget Password Exception ",e);
		}
		return rm; 
	}

}