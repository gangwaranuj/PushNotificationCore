package com.saphire.iopush.dao;

import java.util.List;

import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSFeedResponseBean;
import com.saphire.iopush.model.IopushRSSStats;
import com.saphire.iopush.model.IopushRouteManager;
import com.saphire.iopush.utils.Response;

public interface IRSSFeedDao {

	Response statusrssfeed(int id, int active);

	Response updateRSSFeedConfig(RSSFeedBean rssfeedBean);

	Response rssfeed(int id);


	Response isExistRSSFeedName(String name);


	Response saveRSSFeedConfig(RSSFeedBean rssfeedBean);


	Response deleteRSSFeedConfig(int id);
	
	Response deleteRSSFeedSchedularConfig(int rssfeedid);
	
	Response saveDataSchedular(RSSFeedResponseBean data, int rssfeedid);


	 Response fetchRssFeedSchedule();


	Response updateDataSchedular(RSSFeedResponseBean data, int rssfeedid, int id);
	
	
	Response listRssfeed(int startIndex, int pageSize,int pid, String name, int id, String string2,String string3, Integer[] status);

	List<Object[]> listRssfeedNew( Integer[] status,String name,int startIndex,int pagesize,boolean flag, boolean analytics,String start_date, String end_date, int pid,  int id, String columnForOrdering,String requiredOrder);
	

	List<IopushRSSStats> listRSS(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid, String string, String string2);

//	List<Object[]> listRSSAnalyticnew(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid, String string, String string2);

	

	int countRSS(int startIndex, int pagesize, boolean analytics, String rssName, String rss_date1, String rss_date2,int pid);




	Response saveRouterInfo(IopushRouteManager routeManager);




	Response isRSSNameNew(String name, int productId);




	Response activateRSS(int id, int i);




	Response updateRoute(int id, int active);




	Response isRSSUrlUnique(String url, int productId);
	Response deleteRSSStat(String name, int intConverter);
	
	
	


}
