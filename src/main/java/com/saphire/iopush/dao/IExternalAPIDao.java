package com.saphire.iopush.dao;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.model.IopushCityDetails;
import com.saphire.iopush.model.IopushGeoDetails;
import com.saphire.iopush.model.IopushGetInTouch;
import com.saphire.iopush.model.IopushIsp;
import com.saphire.iopush.model.IopushPayment;
import com.saphire.iopush.model.IopushProduct;
import com.saphire.iopush.model.IopushSubscribers;

import com.saphire.iopush.model.IopushTimeZone;

import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushUsercategory;
import com.saphire.iopush.utils.Response;
import com.saphire.iopush.utils.ResponseMessage;

public interface IExternalAPIDao {

	Response findPlatformName(String browser_name);

	Response findIspID(String isp, int geo_id);

	Response findGeoCode(String lowerCase);

	Response saveGeoDetail(IopushGeoDetails iopushGeoDetails);

	Response findCityCode(String city_code, int geo_id);

	Response saveCityCode(IopushCityDetails iopushCityDetails);

	Response findProductID(String hostName);

	Response findSegmentID(String segmentId);

	Response saveSubscriber(IopushSubscribers iopushSubscribers);

	Response listSubscribers();

	Response saveIsp(IopushIsp isp);

	Response updateActivateSubscriberStat(String iopush_token, int campaign_id, int action);

	Response updateUserCategory(IopushUsercategory iopushUserCategory);

	Response saveRegistrationDetails(IopushUser iopushUser);

	Response saveProduct(IopushProduct iopushProduct);

	Response saveTimezone(IopushTimeZone iopushTimeZone);

	Response findUserPlan();

	List<Object[]> findUnsubscribedUserdetail();

	Response checkEmailandDomain(String subDomain);

	Response updateEmailFlag(IopushUser iopushUser);

	Response checkResendregistrationEmailUser();

	UserDetails findUserByName(String paramString)throws UsernameNotFoundException;

	ResponseMessage updateActiveStatus( String username);

	Response updatePaymentDetail(IopushPayment iopushPayment);
	
	Response fetchPaypalPaymentdetail(int pid,String status);

	List<Object[]> fetchFailCustomerRenewalInfo(String date1,String date2);
	
	Response fetchUserById(int userD);
	
	Response saveGetInTouch(IopushGetInTouch iopushgetintouch);
	
	Response findUserByEmailId(String email);

	Response fetchUserByProdId(int prodId);
	
	Response fetchLimitExceedUserDetail();
	
	Response updateIsLimitExceedFlag(int prodId);
}
