package com.saphire.iopush.service;

import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSStatsBean;
import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.utils.JsonResponse;
import com.saphire.iopush.utils.ResponseMessage;

public interface IRSSFeedService {


	ResponseMessage statusrssfeed(RSSFeedBean rssfeedBean);

	ResponseMessage rssfeedschedule();

	ResponseMessage urlValid(String uri);

	JsonResponse<IopushRssFeedConfig> updaterssfeed(RSSFeedBean rssfeedBean);

	JsonResponse<RSSFeedBean> listrssfeed(RSSFeedBean rssfeedBean);

	JsonResponse<IopushRssFeedConfig> rssfeed(RSSFeedBean rssfeedBean);

	JsonResponse<IopushRssFeedConfig> saveRSSFEED(RSSFeedBean rssfeedBean,int productId);

	ResponseMessage deleterssfeed(RSSFeedBean rssfeedBean);

	ResponseMessage sendNotification(RSSFeedBean rssfeedBean);
	
	ResponseMessage rssAnalytics(String rssname, String date);

	ResponseMessage welcomeAnalytics(String rssname, String date,int type,int product_id);

	JsonResponse<RSSStatsBean> listrssStats(RSSStatsBean rssStatsBean);

	ResponseMessage isUrlValid(String uri);

}
