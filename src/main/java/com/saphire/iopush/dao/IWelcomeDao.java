
package com.saphire.iopush.dao;

import java.text.ParseException;
import java.util.List;

import com.saphire.iopush.bean.WelcomeStatsBean;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.utils.Response;

public interface IWelcomeDao {

	Response save(IopushWelcome iopushWelcome);


	Response updateWelcomeEntity(IopushWelcome iopushWelcome);

	Response getWelcomeEntity(Integer productId);

	Response listCountries(List<String> listCountries);

	Response deleteWelcome(int welcomeId, int productId);

	Response welcomeStatusChange(int welcomeId, int status, int productId );
	
	int countWelcome(Integer[] welcomestatus, String searchWelcomeName, int startIndex, int pagesize, boolean flag, boolean analytics, String date1, String date2,int pid);
	
	List<IopushWelcome> listWelcome(Integer[] welcomestatus, String searchWelcomeName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String date1, String date2,int pid, String string, String string2);


	Response getWelcomeData(int welcomeId, int productId);

	Response findWelcome(String welcomename,int welcomeId, int product_id);

	Response update(IopushWelcome iopushWelcome);

	Response listEligibleForExpireWelcome();
	Response listEligibleForLaunchWelcome();


	Response listEligibleForCacheWelcome();


	Response welcomeStatusAndFlag(int welcomeId, int status, boolean active, int productId);


//	List<WelcomeStatsBean> listWelcomeStatsNew(int startIndex, int pagesize, boolean analytics, String welcomeName, String welcome_date1, String welcome_date2,int pid, String string, String string2);

	List<Object[]> listWelcomeStats(int startIndex, int pagesize,boolean flag, boolean analytics, String welcomeName, String welcome_date1, String welcome_date2,int pid, String string, String string2) throws ParseException;



	int countWelcome(int startIndex, int pagesize, boolean analytics, String welcomeName, String welcome_date1, String welcome_date2,int pid);


	Response fetchIsps(Integer[] country);


	List<Object[]> listManageWelcome(Integer[] welcomestatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, int productId,
			String columnForOrdering, String requiredOrder);





	List<Object[]> listManageWelcomePlatform(Integer[] status, String welcomeName, int startIndex, int pageSize,
			boolean flag, int pid);


	

}

