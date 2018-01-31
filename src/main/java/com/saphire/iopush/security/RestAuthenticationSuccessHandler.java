package com.saphire.iopush.security;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.utils.SecurityUtils;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	Properties myProperties;

	

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String imageurl = myProperties.getProperty(env + "IMAGEURL");
		IopushUser user = (IopushUser) authentication.getPrincipal() ;

		UserBean bean = new UserBean();
		bean.setUsername(user.getUsername());
		bean.setFirstName(user.getFirstName());
		bean.setCountryCode(user.getCountryCode());
		bean.setActive(user.isIsActive());
		// added for email id and phone number
		bean.setEmailId(user.getEmailId());
		bean.setPhoneNumber(user.getPhoneNumber());
		// added for user name and other details
		/*if (user.getImagePath() != null) {
			bean.setImagePath(imageurl + user.getImagePath());
		}*/
		
		bean.setCompany(user.getCompany());
		bean.setTitle(user.getTitle());
		bean.setSalutation(user.getSalutation());
		bean.setUserId(user.getUserId());
		bean.setFirstName(user.getFirstName());
		bean.setLastName(user.getLastName());
		bean.setWebsiteUrl(user.getWebsiteUrl());
		if (user.getImagePath().length() > 0) {
			bean.setImagePath(user.getImagePath());
		}
		bean.setSubDomain(user.getIopushProduct().getProductName());
		bean.setGrantedAuthorities(authentication.getAuthorities());
		request.getSession().setMaxInactiveInterval(60*60);
		request.getSession().setAttribute("productId", user.getIopushProduct().getProductID());
		bean.setPid(user.getIopushProduct().getHash());
		
		logger.info("onAuthenticationSuccess Login Successful!"); 
		SecurityUtils.sendResponse(response, HttpServletResponse.SC_OK, bean);
	}

}
