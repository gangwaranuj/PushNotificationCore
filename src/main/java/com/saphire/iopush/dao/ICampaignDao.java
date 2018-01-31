package com.saphire.iopush.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.saphire.iopush.model.IopushCampaign;
import com.saphire.iopush.model.IopushRules;
import com.saphire.iopush.model.IopushSubCampaign;
import com.saphire.iopush.model.IopushTimeZone;
import com.saphire.iopush.utils.Response;

public interface ICampaignDao {

	Response saveCampaign(IopushCampaign campaign);
	
	Response saveSubCampaign(IopushSubCampaign campaign);
	
	Response deleteCampaign(IopushCampaign iopushCampaign);
	
	Response listCampaign(int start,int limit);
	
	Response updateCampaign(IopushCampaign iopushCampaign);
	
	Response listCampaignByCriteria(DetachedCriteria criteria);
	
	Response fetch(IopushTimeZone iopushTimeZone, String[] excludeProperty);
	
	Response findCountryTimeZone(int c_id);
	
	Response saveRules(List<IopushRules> listIopushRules);
	
	Response findCampaignByID(int camapaign_id);
	
	Response deleteCampaignRule(int camapaign_id);
	
	Response deleteSubCampaign(int camapaign_id);
	
	Response listCampaigns_expire();
	
	int countCampaignsNew(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize, 
			boolean flag, boolean analytics, String object, String object2,int pid);
	
	/*List<Object[]> listCampaignsNew(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String object, String object2,int pid);
*/
	Response findSUBSCRIBERSCountByCriteira(String query);



	Response findCampaign(String campignname,int campignid, int product_id);



	Response changeCampaignStatus(int campaign_id, int pending);


	Response deleteNotification(int campignid);


	Response deleteViewsClick(int campignid);


	Response deleteCampaign(int campignid);


	Response ispList(Object[] countryId);


	Response listTimezone();


	Response listCities(int country_id);
	Response listCities();
	
	Response findActiveSubscribers(int campaign_id);

	Response getPlatforms(int campaignId);


	Response listplatform();


	Response executeQuery(String insertQuery);


	Response deleteActiveSubscribers(int campaign_id);


	Response findCampaign(int campignid, int product_id);


	Response findCountryCode(int c_id);
	
	Response deleteActiveSubscribers_subcampaign(int campaign_id,int geo_id);
	
	 Response changeSubCampaignStatus(int campignid, int status);
	 Response changeSubCampaignStatus(int campignid,int geo_id , int status);

	Response getSubCampaigns(int campignid);

	Response listSubCampaign(int campaignId);

	Response listCampaignAutoLaunch();


	Response listProduct();

	Response updateLiveCount(int campignid, int livecampaignsCount);
	
	List<IopushCampaign> listCampaignAnalytics(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid, String string, String string2);

	int countCampaignAnalytics(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid);
	Response campaignStatsCountry(int campaign_id);
	Response campaignCountryPlatformStats(int campaign_id, int geoid, HashMap<Integer, String> platform);

	
	Response fetchRules(int campaignId);

	List<Object[]> listCampaignsNew(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String object, String object2, int productId, String columnForOrdering,
			String requiredOrder);
	
	
	List<Object[]> campaignsAnalyticsList(Integer[] status,String campaign_name, int startIndex,int pageSize,boolean analytics,
			String campaign_date1,String campaign_date2,int pid, String columnForOrdering, String requiredOrder);



	List<Object[]> listCampaignsPlatforms(Integer[] campstatus, String searchCampignName, int startIndex, int pagesize,
			boolean flag, boolean analytics, String campaign_date1, String campaign_date2, int productId);

}
