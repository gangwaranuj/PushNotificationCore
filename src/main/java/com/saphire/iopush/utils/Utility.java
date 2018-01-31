package com.saphire.iopush.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Subdivision;
import com.opencsv.CSVWriter;
import com.opencsv.bean.BeanToCsv;
import com.saphire.iopush.bean.BrowserBean;
import com.saphire.iopush.bean.CampaignBean;
import com.saphire.iopush.bean.CityDashBoardBean;
import com.saphire.iopush.bean.CityModelBean;
import com.saphire.iopush.bean.GeoBean;
import com.saphire.iopush.bean.GeoCityBean;
import com.saphire.iopush.bean.ISPBean;
import com.saphire.iopush.bean.IoPushPaymentCancelledUserInfo;
import com.saphire.iopush.bean.PlatformBean;
import com.saphire.iopush.bean.PlatformStatBean;
import com.saphire.iopush.bean.RSSFeedBean;
import com.saphire.iopush.bean.RSSFeedResponseBean;
import com.saphire.iopush.bean.RenewalFailedInfoBean;
import com.saphire.iopush.bean.TotalClicksBean;
import com.saphire.iopush.bean.TrendsBean;
import com.saphire.iopush.bean.UserBean;
import com.saphire.iopush.bean.ViewsClicksBean;
import com.saphire.iopush.bean.WelcomeBean;
import com.saphire.iopush.model.IopushCampaign;

import com.saphire.iopush.model.IopushPackagePlan;
import com.saphire.iopush.model.IopushPlan;
import com.saphire.iopush.model.IopushPlanPreference;

import com.saphire.iopush.model.IopushRSSStats;

import com.saphire.iopush.model.IopushRssFeedConfig;
import com.saphire.iopush.model.IopushSubscribers;
import com.saphire.iopush.model.IopushUser;
import com.saphire.iopush.model.IopushWelcome;
import com.saphire.iopush.model.IopushWelcomeStats;
import com.sun.syndication.feed.synd.SyndEnclosure;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

public class Utility {

	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUM = "0123456789";
	private static final String SPL_CHARS = "!@";


	private static Logger logger = LoggerFactory.getLogger(Utility.class);

	private static final String key = "IOPUSHTECH"; // 128 bit key
	private static final String initVector = "RandomInitVector"; // 16 bytes IV


	public static String generateUniqueId() {
		UUID idOne = UUID.randomUUID();
		UUID idTwo = UUID.randomUUID();
		UUID idThree = UUID.randomUUID();
		UUID idFour = UUID.randomUUID();

		String time = idOne.toString().replace("-", "");
		String time2 = idTwo.toString().replace("-", "");
		String time3 = idThree.toString().replace("-", "");
		String time4 = idFour.toString().replace("-", "");

		StringBuffer data = new StringBuffer();
		data.append(time);
		data.append(time2);
		data.append(time3);
		data.append(time4);

		SecureRandom random = new SecureRandom();
		int beginIndex = random.nextInt(100); // Begin index + length of your
		// string < data length
		int endIndex = beginIndex + 10; // Length of string which you want

		String yourID = data.substring(beginIndex, endIndex);
		return yourID;
	}

	public static HashMap<String, TrendsBean> fillTrend(String startDate, String endDate, DateFormat format) {
		LinkedHashMap<String, TrendsBean> hm = new LinkedHashMap<String, TrendsBean>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		try {
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			Calendar c = Calendar.getInstance();
			long diffInMilli = end.getTime() - start.getTime();
			long diff = diffInMilli / (1000 * 60 * 60 * 24);
			for (long i = 0; i <= diff; i++) {
				hm.put(startDate, new TrendsBean(startDate, 0, 0, 0, 0, 0));
				c.setTime(start);
				c.add(Calendar.DATE, 1);
				startDate = sdf.format(c.getTime());
				start = sdf.parse(startDate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hm;

	}

	public static boolean containsBrowserId(List<PlatformStatBean> listPlatformStatBean, int browserid) {
		for (PlatformStatBean object : listPlatformStatBean) {
			if (object.getPlatformId() == browserid) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static List<ViewsClicksBean> mergeClickView(Response clicks, Response views, int action) {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		List<TotalClicksBean> totalclickbean = new ArrayList<TotalClicksBean>();
		List<Object[]> listTotalClicks = (List<Object[]>) clicks.getData();
		for (Object[] objects : listTotalClicks) {
			totalclickbean.add(new TotalClicksBean(action == 0 ? String.valueOf(objects[0]) : sdf.format(objects[0]),
					Integer.parseInt(String.valueOf(objects[1]))));
		}
		Map<String, Integer> mapTotalView = new HashMap<String, Integer>();
		List<Object[]> listTotalViews = (List<Object[]>) views.getData();
		for (Object[] objects : listTotalViews) {
			mapTotalView.put(action == 0 ? String.valueOf(objects[0]) : sdf.format(objects[0]),
					Integer.parseInt(String.valueOf(objects[1])));
		}

		List<ViewsClicksBean> finalViewClickBean = new ArrayList<ViewsClicksBean>();

		for (TotalClicksBean totalInstallClickBean : totalclickbean) {
			if (mapTotalView.containsKey(totalInstallClickBean.getDate())) {
				finalViewClickBean.add(new ViewsClicksBean(totalInstallClickBean.getDate(),
						mapTotalView.get(totalInstallClickBean.getDate()), totalInstallClickBean.getValue()));
				mapTotalView.remove(totalInstallClickBean.getDate());
			} else {
				finalViewClickBean
				.add(new ViewsClicksBean(totalInstallClickBean.getDate(), 0, totalInstallClickBean.getValue()));
			}
		}
		for (Map.Entry<String, Integer> entry : mapTotalView.entrySet()) {
			finalViewClickBean.add(new ViewsClicksBean(entry.getKey(), entry.getValue(), 0));
		}
		return finalViewClickBean;
	}

	public static char[] generatePswd(int minLen, int maxLen, int noOfCAPSAlpha, int noOfDigits, int noOfSplChars) {
		if (minLen > maxLen)
			throw new IllegalArgumentException("Min. Length > Max. Length!");
		if ((noOfCAPSAlpha + noOfDigits + noOfSplChars) > minLen)
			throw new IllegalArgumentException(
					"Min. Length should be atleast sum of (CAPS, DIGITS, SPL CHARS) Length!");
		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
		}
		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
		}
		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
			}
		}
		return pswd;
	}

	private static int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}

	public static Date addMinInDate(Date date, int min, String dateformat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.MINUTE, min);
		return format.parse(format.format(now.getTime()));
	}

	public static Date addDaysInDate(Date date, int days, String dateformat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.add(Calendar.DATE, days);
		return format.parse(format.format(now.getTime()));
	}

	public static int parseCountry(String countries) {
		int i = 0;
		if (countries.equals("")) {
			i = 0;
		} else if (countries.contains(",")) {
			i = 0;
		} else {
			i = intConverter(countries);
		}
		return i;
	}

	public static long diff(String clientdate, String clientsdate, String dateformat) {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		Date d1 = null;
		Date d2 = null;
		long diff = 0l;
		try {
			d1 = format.parse(clientdate);
			d2 = format.parse(clientsdate);
			diff = d2.getTime() - d1.getTime();
			diff = TimeUnit.MILLISECONDS.toMinutes(diff);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	public static Date addMin(int min, String dateformat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, min);
		return format.parse(format.format(now.getTime()));
	}

	// change selected date into timeZone wise country
	public static String changeDateIntotimeZone(Date date, String timezone, String dateFormat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String dateToReturn = format.format(date);
		// return format.parse(dateToReturn);
		return dateToReturn;
	}

	// change selected date into timeZone wise country
	public static String changeDateIntotimeZone(Date date, String timezone) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-d'T'HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String dateToReturn = format.format(date);
		// return format.parse(dateToReturn);
		return dateToReturn;
	}

	// change date into EDT Timezone
	public static String changeDateIntoEDT(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String timezone = "GMT-4";
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String dateToReturn = format.format(date);
		return dateToReturn;
	}

	public static String changeDateIntoEDTOnlyDate(Date date) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
		String timezone = "GMT-4";
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String dateToReturn = format.format(date);
		return dateToReturn;
	}

	public static Date changeOnlyDateInEDT(Date date, String dateFormat) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		String timezone = "GMT-4";
		format.setTimeZone(TimeZone.getTimeZone(timezone));
		String dateToReturn = format.format(date);
		return format.parse(dateToReturn);
	}

	// Valid URL
	public static ResponseMessage isURLValid(String url) {
		ResponseMessage rm = null;
		try {
			final URLConnection connection = new URL(url).openConnection();
			connection.connect();
			rm = new ResponseMessage(Constants.SUCCESS_CODE, Constants.SUCCESS);
		} catch (final MalformedURLException e) {
			rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, Constants.ERROR);
			throw new IllegalStateException("Bad URL: " + url, e);
		} catch (final IOException e) {
			rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, Constants.ERROR);
		} catch (Exception e) {
			rm = new ResponseMessage(Constants.ERROR_CODE_UNKNOWN, e.getMessage());
		}

		return rm;
	}

	public static int intConverter(String strValue) {
		int temp = 0;
		try {
			temp = Integer.parseInt(strValue);
		} catch (Exception e) {
			temp = 0;
		}
		return temp;
	}

	public static boolean checkRssfeedStatus(Integer[] rssStatus) {
		for (int i : rssStatus) {
			if (i != 0) {
				return false;
			}
		}

		return true;
	}
	public static LinkedHashMap<String, Integer> datesInRange(String startDate, String endDate) {
		LinkedHashMap<String, Integer> hm = new LinkedHashMap<String, Integer>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);

		try {
			// String startDateCopy=startDate;
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			Calendar c = Calendar.getInstance();
			long diffInMilli = end.getTime() - start.getTime();
			long diff = diffInMilli / (1000 * 60 * 60 * 24);
			// System.out.println("diff "+diff);
			for (long i = 0; i <= diff; i++) {
				// System.out.println("inside loop "+startDateCopy);
				hm.put(startDate, 0);
				c.setTime(start);
				c.add(Calendar.DATE, 1);
				startDate = sdf.format(c.getTime());
				start = sdf.parse(startDate);

			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return hm;
	}

	public static LinkedHashMap<String, ArrayList<Integer>> datesInRangeForViewsClicks(String startDate,
			String endDate) {
		LinkedHashMap<String, ArrayList<Integer>> hm = new LinkedHashMap<String, ArrayList<Integer>>();
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
		ArrayList<Integer> arr = new ArrayList<Integer>();
		try {
			// String startDateCopy=startDate;
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			Calendar c = Calendar.getInstance();
			long diffInMilli = end.getTime() - start.getTime();
			long diff = diffInMilli / (1000 * 60 * 60 * 24);
			for (long i = 0; i <= diff; i++) {
				hm.put(startDate, new ArrayList<Integer>(Arrays.asList(0, 0)));
				c.setTime(start);
				c.add(Calendar.DATE, 1);
				startDate = sdf.format(c.getTime());
				start = sdf.parse(startDate);
				arr.clear();
			}
		} catch (ParseException e) {
			e.printStackTrace();

		}
		return hm;
	}

	// for view click hour api
	public static LinkedHashMap<String, ArrayList<Integer>> hourWise() {
		LinkedHashMap<String, ArrayList<Integer>> hm = new LinkedHashMap<String, ArrayList<Integer>>();
		for (int i = 0; i < 24; i++) {
			String hour = (i) + "";
			hm.put(hour, new ArrayList<Integer>(Arrays.asList(0, 0)));

		}
		return hm;
	}

	public static HashMap<String, TrendsBean> hourWiseTrend() {
		LinkedHashMap<String, TrendsBean> hm = new LinkedHashMap<String, TrendsBean>();
		for (int i = 0; i < 24; i++) {
			String hour = (i) + "";
			hm.put(hour, new TrendsBean(hour, 0, 0, 0, 0, 0));

		}
		return hm;
	}

	public static long longConverter(String strValue) {
		long temp = 0L;
		try {
			temp = Long.parseLong(strValue);
		} catch (Exception e) {
			temp = 0L;
		}
		return temp;
	}

	public static int intConverter(String strValue, int defaultvalue) {
		int temp = defaultvalue;
		try {
			temp = Integer.parseInt(strValue);
		} catch (Exception e) {
			temp = defaultvalue;
		}
		return temp;
	}

	public static String getUserName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public static String randInt(int min, int max) {
		int randomNum = new Random().nextInt(max - min + 1) + min;
		long curtime = System.currentTimeMillis();
		return curtime + "" + randomNum;
	}

	public static boolean isThisDateValid(String dateToValidate, String dateFromat) {
		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {
			sdf.parse(dateToValidate);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static int getNoOfWeek(String dateToValidate, String dateFromat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);
		Calendar calendar = new GregorianCalendar();
		Date trialTime = sdf.parse(dateToValidate);
		calendar.setTime(trialTime);
		return calendar.get(3);
	}

	public static String getDateForTimeZone(String dateFormat, int noofday) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.add(5, noofday);

		return Utility.changeDateIntoEDT(cal.getTime());
	}

	public static String getDate(String dateFormat, int noofday) {
		Calendar cal = Calendar.getInstance();
		cal.add(5, noofday);
		return new SimpleDateFormat(dateFormat).format(cal.getTime());
	}

	public static String getFirstDayOfWeekDate(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		cal.set(7, cal.getActualMinimum(7));
		Date now = new Date();
		cal.setTime(now);
		int week = cal.get(7);
		Date date = new Date(now.getTime() - 86400000 * (week - 2));
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public static String getLastDayOfWeekDate(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		cal.set(7, cal.getActualMinimum(7));
		Date now = new Date();
		cal.setTime(now);
		int week = cal.get(7);
		Date date = new Date(now.getTime() - 86400000 * (week - 8));
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public static List<String> getDateOfCurrentWeek(String dateFormat) {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		List<String> days = new ArrayList<String>();
		int delta = -now.get(7) + 2;
		now.add(5, delta);
		for (int i = 0; i < 7; i++) {
			days.add(format.format(now.getTime()));
			now.add(5, 1);
		}
		return days;
	}

	public static Integer[] getRssfeedStatus(RSSFeedBean rSSFeedBean) {
		Integer[] rssfeedstatus = new Integer[2];
		if (rSSFeedBean.getActive() == 0) {
			rssfeedstatus[0] = 0;
		} else {
			rssfeedstatus[0] = 1;
		}
		if (rSSFeedBean.getInactive() == 0) {
			rssfeedstatus[1] = 0;
		} else {
			rssfeedstatus[1] = 1;
		}

		return rssfeedstatus;
	}

	public static Integer[] getCampignStatus(CampaignBean campignBean) {
		Integer[] campstatus = new Integer[4];
		if (campignBean.getPending() == 0) {
			campstatus[0] = 0;
		} else {
			campstatus[0] = 2;
		}
		if (campignBean.getLive() == 0) {
			campstatus[1] = 0;
		} else {
			campstatus[1] = 1;
		}
		if (campignBean.getExpired() == 0) {
			campstatus[2] = 0;
		} else {
			campstatus[2] = 3;
		}
		if (campignBean.getDraft() == 0) {
			campstatus[3] = 0;
		} else {
			campstatus[3] = 4;
		}
		return campstatus;
	}

	public static Integer[] getWelcomeStatus(WelcomeBean welcomeBean) {
		Integer[] welcomestatus = new Integer[4];
		if (welcomeBean.getPending() == 0) {
			welcomestatus[0] = 0;
		} else {
			welcomestatus[0] = 2;
		}
		if (welcomeBean.getLive() == 0) {
			welcomestatus[1] = 0;
		} else {
			welcomestatus[1] = 1;
		}
		if (welcomeBean.getExpired() == 0) {
			welcomestatus[2] = 0;
		} else {
			welcomestatus[2] = 3;
		}
		if (welcomeBean.getDraft() == 0) {
			welcomestatus[3] = 0;
		} else {
			welcomestatus[3] = 4;
		}
		return welcomestatus;
	}

	public static boolean checkCampStatus(Integer[] campstatus) {
		for (int i : campstatus) {
			if (i != 0) {
				return false;
			}
		}

		return true;
	}

	public static String getExtension(String data) {
		String ext = "png";
		try {
			ext = data.split(",")[0].split("/")[1].split(";")[0];
		} catch (Exception e) {
			ext = "png";
		}
		return ext;
	}

	public static GeoCityBean getDetails(String ip, Properties myProperties) {
		String county_name = "";
		String county_code = "";
		String city_name = "";
		String city_code = "";
		Double city_longitude = 00.00;
		Double city_latitude = 00.00;
		String time_zone = "";
		String isp = "";
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String maxminddb = myProperties.getProperty(env + "GEO_IP_CITY");
		String maxmindispdb = myProperties.getProperty(env + "GEO_IP_ISP");
		File database = new File(maxminddb);
		File databaseisp = new File(maxmindispdb);
		try {
			DatabaseReader reader = new DatabaseReader.Builder(database).build();
			InetAddress ipAddress = InetAddress.getByName(ip);
			CityResponse response = reader.city(ipAddress);

			Country country = response.getCountry();
			county_name = country.getName();
			county_code = country.getIsoCode();

			Subdivision subdivision = response.getMostSpecificSubdivision();
			City city = response.getCity();
				city_code = subdivision.getIsoCode() == null ? "OT" : subdivision.getIsoCode();
				city_name = city.getName() == null ? "Other" : city.getName();

			Location location = response.getLocation();
			city_longitude = location.getLongitude();
			city_latitude = location.getLatitude();
			time_zone = location.getTimeZone();

			DatabaseReader ispreader = new DatabaseReader.Builder(databaseisp).build();
			IspResponse response1 = ispreader.isp(ipAddress);
			isp = response1.getIsp()==null?"Other":response1.getIsp();

			return new GeoCityBean(county_name, county_code, city_name, city_code, city_longitude, city_latitude,
					time_zone, isp);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static boolean deleteResourceFromPath(String urlPath) {

		File file = new File(urlPath);
		if (file.exists()) {

			return (file.delete());
		} else {
			return false;
		}
	}

	public static Integer[] addInt(Integer[] series, int newInt) {
		// create a new array with extra index
		Integer[] newSeries = new Integer[series.length + 1];

		// copy the integers from series to newSeries
		for (int i = 0; i < series.length; i++) {
			newSeries[i] = series[i];
		}
		// add the new integer to the last index
		newSeries[newSeries.length - 1] = newInt;

		return newSeries;

	}

	/**
	 * This method is used to encrypt md5
	 * 
	 * @param String
	 *            key
	 * @return String
	 */
	public static String md5Encryption(String key) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(key.getBytes());
		byte byteData[] = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();

	}

	public static Date berlindateConversion() {
		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		df1.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		Date now = new Date();
		String dateTime = df1.format(now);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
		DateTime jodatime = dtf.parseDateTime(dateTime);
		return jodatime.toDate();

	}

	@SuppressWarnings("unchecked")
	public static List<ISPBean> populateISPData(Response response, Set<String> platforms) {
		List<Object[]> result = (List<Object[]>) response.getData();
		List<ISPBean> listGeoBean = new ArrayList<ISPBean>();
		Set<Integer> mapIspCode = new HashSet<Integer>();

		List<ISPBean> listGeoModelBean = new ArrayList<ISPBean>();
		for (Object[] objects : result) {

			listGeoModelBean.add(new ISPBean(String.valueOf(objects[0]), String.valueOf(objects[1]), 0,
					Integer.parseInt(String.valueOf(objects[4])), Integer.parseInt(String.valueOf(objects[3]))));

			mapIspCode.add(Integer.parseInt(String.valueOf(objects[3])));
		}

		String ispName = "";
		boolean flag = false;
		long count = 0L;

		for (int ispCode : mapIspCode) {
			count = 0L;
			List<PlatformBean> listPlatformBean = new ArrayList<PlatformBean>();
			for (ISPBean ispModelBean : listGeoModelBean) {
				if (ispCode == ispModelBean.getIspCode()) {
					ispName = ispModelBean.getIspName();
					count += ispModelBean.getTotalusers();
					listPlatformBean
					.add(new PlatformBean(ispModelBean.getPlatformName(), (int) ispModelBean.getTotalusers()));
				}
			}

			for (String platformName : platforms) {
				flag = containsPlatformName(listPlatformBean, platformName);
				if (!flag) {
					listPlatformBean.add(new PlatformBean(platformName, 0));
				}
			}
			Collections.sort(listPlatformBean);
			listGeoBean.add(new ISPBean(ispCode, ispName, count, listPlatformBean));
		}

		return listGeoBean;

	}

	@SuppressWarnings("unchecked")
	public static List<GeoBean> populateData(Response response, Set<String> platforms) {
		List<Object[]> result = (List<Object[]>) response.getData();
		String flagurl = "images/flag/%s.png";
		List<GeoBean> listGeoBean = new ArrayList<GeoBean>();
		Set<String> mapGeoCode = new HashSet<String>();

		List<GeoBean> listGeoModelBean = new ArrayList<GeoBean>();
		for (Object[] objects : result) {
			listGeoModelBean.add(new GeoBean(String.valueOf(objects[1]), String.valueOf(objects[0]),
					String.valueOf(objects[3]), String.valueOf(objects[2]), String.valueOf(objects[4]),
					Integer.parseInt(String.valueOf(objects[7]))));

			mapGeoCode.add(String.valueOf(objects[1]));
		}

		String geoname = "";
		String longitude = "";
		String latitude = "";
		boolean flag = false;
		long count = 0L;

		for (String geocode : mapGeoCode) {
			count = 0L;
			List<PlatformBean> listPlatformBean = new ArrayList<PlatformBean>();
			for (GeoBean geoModelBean : listGeoModelBean) {
				if (geocode.equalsIgnoreCase(geoModelBean.getGeoCode())) {
					geoname = geoModelBean.getGeoName();
					longitude = geoModelBean.getGeoLongitude();
					latitude = geoModelBean.getGeoLatitude();
					count += geoModelBean.getCount();
					listPlatformBean.add(new PlatformBean(geoModelBean.getPlatformName(), geoModelBean.getCount()));
				}
			}

			for (String platformName : platforms) {
				flag = containsPlatformName(listPlatformBean, platformName);
				if (!flag) {
					listPlatformBean.add(new PlatformBean(platformName, 0));
				}
			}
			Collections.sort(listPlatformBean);
			listGeoBean.add(new GeoBean(geocode, geoname, String.format(flagurl, geocode.toLowerCase()), longitude,
					latitude, count, listPlatformBean));
		}

		return listGeoBean;
	}

	public static boolean containsPlatformName(List<PlatformBean> listPlatformBean, String platformName) {
		for (PlatformBean object : listPlatformBean) {
			if (object.getPlatformName().equalsIgnoreCase(platformName)) {
				return true;
			}
		}
		return false;
	}

	public static String listCampaignQuery(Integer[] status, String campaign_name, int startIndex, int pageSize,
			boolean flag, boolean analytics, String campaign_date1, String campaign_date2, int pid,
			String columnForOrdering, String requiredOrder) {
		String hql = "Select " + "campaignId,campaignName," + "campaignScheduleDate,"
				+ "campaignCurrentDate,campaignStatus,iopushTimeZone.timezone, iopushTimeZone.timezoneID,campaignEndDate, segmented, modificationDate"
				+ " from " + IopushCampaign.class.getName() + " where iopushProduct.productID=" + pid + " and ";
		if (!flag || campaign_name.length() > 0 || analytics) {
			int campaign_id = Utility.intConverter(campaign_name);
			if (campaign_name.length() > 0) {
				if (campaign_id == 0)
					hql = hql + " LOWER(campaignName) LIKE '%" + campaign_name.toLowerCase() + "%' AND ";
				else
					hql = hql + " campaignId=" + campaign_id + " AND ";
			}


			if(analytics){
				hql=hql+" campaignStatus IN ("+Constants.LIVE+","+Constants.EXPIRE+")  AND "; 
				if(campaign_date1!=null){
					hql=hql+" campaignStartDate >= '"+campaign_date1+"'  AND ";
				}
				if(campaign_date2!=null){ 
					hql=hql+" campaignStartDate <= '"+campaign_date2+'T'+"23:59:59'  AND ";

				}
			} else {
				if (!flag)
					hql = hql + " campaignStatus IN (" + integerArraytoString(status) + ")";
			}
		}
		if (columnForOrdering != null && !columnForOrdering.isEmpty() && requiredOrder != null
				&& !requiredOrder.isEmpty()) {
			hql = hql + " ORDER BY " + columnForOrdering + " " + requiredOrder;
		} else {
			hql = hql + " ORDER BY campaignId DESC";
		}
		hql=hql+", campaignId DESC";
		return hql;
	}




	public static String listRssfeedQuery(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean flag,boolean analytics,String start_date,String end_date,int pid, String columnForOrdering, String requiredOrder){
		String hql = "Select id,campaign,"
				+ "cities,countries,createdBy,creationDate, generic,isps,modificationDate, modificedBy ,"
				+ "name,notification,  products ,  source , url ,  automatedImage , "
				+ " logo_path ,  segmentTypes ,  segments ,  subscribedFrom ,platform"
				+ " from " + IopushRssFeedConfig.class.getName() +" where products= '" +String.valueOf(pid) + "' and ";

		if(!flag || campaign_name.length()>0 || analytics)
		{
			int campaign_id =Utility.intConverter(campaign_name);
			if(campaign_name.length()>0){
				if(campaign_id==0)
					hql=hql+" LOWER(name) LIKE '%"+campaign_name.toLowerCase()+"%' AND "; 
				else
					hql=hql+" id="+campaign_id +" AND "; 
			}

			if(analytics){
				hql=hql+" campaignStatus IN ("+Constants.LIVE+","+Constants.EXPIRE+")  AND "; 
				if(start_date!=null){
					hql=hql+" campaignStartDate >= '"+start_date+"'  AND ";
				}
				if(end_date!=null){ 
					hql=hql+" campaignStartDate <= '"+end_date+'T'+"23:59:59'  AND ";
				}
			}else{
				if(!flag)
					hql=hql+" notification IN ("+integerArraytoString(status)+")";
			}
		}

		hql=hql+" Order by "+  columnForOrdering +" "+requiredOrder.toLowerCase()+" , id desc ";

		return hql;
	}


	public static String listAnalyticsCampaignQuery(String campaign_name, boolean analytics
			,String campaign_date1,String campaign_date2,int pid, String columnForOrdering, String requiredOrder){
		String hql = "select campaignId , campaignName ,campaignStatus,campaignScheduleDate,"
				+ " campaignsent,campaignopen,campaignclick  from "
				+ IopushCampaign.class.getName() ; 
		hql += " WHERE iopushProduct.productID = "+pid+ " AND ";

		int campaign_id =Utility.intConverter(campaign_name);
		//   for search box
		if(campaign_name.length()>0){
			if(campaign_id==0)
				hql=hql+" LOWER(campaignName) LIKE '%"+campaign_name.toLowerCase()+"%' AND "; 
			else
				hql=hql+" campaignId="+campaign_id +" AND "; 
		}
		//   for selecting calendar
		if(campaign_date1!=null){
			hql=hql+" campaignScheduleDate >= '"+campaign_date1+"'  AND ";
		}
		if(campaign_date2!=null){ 
			hql=hql+" campaignScheduleDate <= '"+campaign_date2+'T'+"23:59:59'  AND ";
		}

		hql=hql+" campaignStatus in (1,3) ORDER BY "+columnForOrdering+" "+requiredOrder+" , campaignId DESC";
		return hql;
	}

	// <<<<<<< HEAD
	// 	public static String countAnalyticsCampaignQuery(String campaign_name, int startIndex, int pageSize, boolean flag,
	// 			String username, boolean analytics, String campaign_date1, String campaign_date2) {
	// 		String hql = "Select " + "count(*) " + "from iopush_campaign_stats , iopush_campaign ";
	// 		if (!flag || campaign_name.length() > 0 || analytics) {
	// =======
	// 		int campaign_id =Utility.intConverter(campaign_name);
	// 		//			for search box
	// 		if(campaign_name.length()>0){
	// 			if(campaign_id==0)
	// 				hql=hql+" LOWER(campaignName) LIKE '%"+campaign_name.toLowerCase()+"%' AND "; 
	// 			else
	// 				hql=hql+" campaignId="+campaign_id +" AND "; 
	// 		}
	// 		//			for selecting calendar
	// 		if(campaign_date1!=null){
	// 			hql=hql+" campaignScheduleDate >= '"+campaign_date1+"'  AND ";
	// 		}
	// 		if(campaign_date2!=null){ 
	// 			hql=hql+" campaignScheduleDate <= '"+campaign_date2+'T'+"23:59:59'  AND ";
	// 		}

	// 		hql=hql+" campaignStatus in (1,3) ORDER BY "+columnForOrdering+" "+requiredOrder+" , campaignId DESC";
	// 		return hql;
	// 	}

	public static String countAnalyticsCampaignQuery(String campaign_name,int startIndex,int pageSize, boolean flag,String username,boolean analytics,String campaign_date1,String campaign_date2){
		String hql = "Select "
				+ "count(*) "
				+ "from iopush_campaign_stats , iopush_campaign "  ;
		if(!flag || campaign_name.length()>0 || analytics)
		{

			hql += " WHERE ";
			int campaign_id = Utility.intConverter(campaign_name);
			if (campaign_name.length() > 0) {
				if (campaign_id == 0)
					hql = hql + " LOWER(campaignName) LIKE '%" + campaign_name.toLowerCase() + "%' AND";
				else
					hql = hql + " campaignId=" + campaign_id + " AND";
			}


			if(analytics){
				if(campaign_date1!=null){
					hql=hql+"  campaignStartDate >= '"+campaign_date1+"' AND";
				}
				if(campaign_date2!=null){ 
					hql=hql+" campaignStartDate <= '"+campaign_date2+'T'+"23:59:59'";

				}
			}

			hql = hql + "iopush_campaign.campaign_id = iopush_campaign_stats.fk_campaign_id    ";

		}
		return hql;
	}

	// <<<<<<< HEAD
	// 	public static String countCampaignQuery(Integer[] status, String campaign_name, int startIndex, int pageSize,
	// 			boolean flag, boolean analytics, String campaign_date1, String campaign_date2, int pid) {
	// 		String hql = "Select " + "count(*) " + "from " + IopushCampaign.class.getName()
	// 				+ " where iopushProduct.productID=" + pid + " and ";
	// 		if (!flag || campaign_name.length() > 0 || analytics) {
	// 			int campaign_id = Utility.intConverter(campaign_name);
	// 			if (campaign_name.length() > 0) {
	// 				if (campaign_id == 0)
	// 					hql = hql + " LOWER(campaignName) LIKE '%" + campaign_name.toLowerCase() + "%' AND";
	// =======
	/************************************/
	/************************************/
	public static String welcomeAnalyticsQuery(String columnForOrdering,String requiredOrder,String welcomename,int startIndex,int pageSize, boolean flag,boolean analytics,String welcome_startdate,String welcome_enddate,int pid)
	{
		String hql = " SELECT "
				+ "welcomeName, "
				+ " iopushWelcome.welcomeId ,"
				+ "click,"
				+ " date,"
				+ " open,"
				+ " productid,"
				+ " sent, "
				+ "welcomeStatsId ,iopushWelcome.welcomeStatus"
				+"  FROM " +IopushWelcomeStats.class.getName() + " where productid= '" +String.valueOf(pid) + "' and ";
		if(!flag || welcomename.length()>0 || analytics)
		{
			int welcomeId =Utility.intConverter(welcomename);
			if(welcomename.length()>0){
				if(welcomeId==0)
					hql=hql+" LOWER(welcomeName) LIKE '%"+welcomename.toLowerCase()+"%' AND"; 
				else
					hql=hql+" iopushWelcome.welcomeId="+welcomeId +" AND"; 
			}

			if(analytics){
				if(welcome_startdate!=null){
					hql=hql+"  date >= '"+welcome_startdate+"' AND";
				}
				if(welcome_enddate!=null){ 
					hql=hql+" date <= '"+welcome_enddate+'T'+"23:59:59'" +" AND";
				}
			}

			if(columnForOrdering.equals("iopushWelcome.welcomeId")){

				hql=hql+" iopushWelcome.welcomeStatus in (1,3) ORDER BY "+columnForOrdering+" "+requiredOrder;

			}else{
				hql=hql+" iopushWelcome.welcomeStatus in (1,3) ORDER BY "+columnForOrdering+" "+requiredOrder+" ,  iopushWelcome.welcomeId DESC";
			}
		}
		return hql;

	}

	/************************************/
	/************************************/

	public static String countCampaignQuery(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean flag,boolean analytics,String campaign_date1,String campaign_date2,int pid){
		String hql = "Select "
				+ "count(*) "
				+ "from " + IopushCampaign.class.getName() + " where iopushProduct.productID=" + pid + " and ";
		if(!flag || campaign_name.length()>0 || analytics)
		{
			int campaign_id =Utility.intConverter(campaign_name);
			if(campaign_name.length()>0){
				if(campaign_id==0)
					hql=hql+" LOWER(campaignName) LIKE '%"+campaign_name.toLowerCase()+"%' AND"; 

				else
					hql = hql + " campaignId=" + campaign_id + " AND";
			}


			if(analytics){
				hql=hql+" campaignStatus IN ("+Constants.LIVE+","+Constants.EXPIRE+") AND"; 
				if(campaign_date1!=null){
					hql=hql+"  campaignStartDate >= '"+campaign_date1+"' AND";
				}
				if(campaign_date2!=null){ 
					hql=hql+" campaignStartDate <= '"+campaign_date2+'T'+"23:59:59' AND ";

				}
			} else {
				if (!flag)
					hql = hql + " campaignStatus IN (" + integerArraytoString(status) + ")  ";
			}

		}
		return hql;
	}

	public static String integerArraytoString(Integer[] status) {
		String result = "";
		for (Integer a : status) {
			result = result + a + ',';
		}
		return result.endsWith(",") ? result.substring(0, result.length() - 1) : result;
	}

	@SuppressWarnings("unchecked")
	public static List<CityDashBoardBean> populateCityData(Response response, Set<String> browsers, boolean cflag) {
		List<Object[]> result = (List<Object[]>) response.getData();
		List<CityDashBoardBean> cityDashBoardBean = new ArrayList<CityDashBoardBean>();
		Set<Integer> mapCityId = new HashSet<Integer>();
		List<CityModelBean> listCityModelBean = new ArrayList<CityModelBean>();
		for (Object[] objects : result) {
			listCityModelBean.add(new CityModelBean(Utility.intConverter(String.valueOf(objects[4])),
					String.valueOf(objects[3]), String.valueOf(objects[2]), String.valueOf(objects[1]),
					String.valueOf(objects[0]), Utility.intConverter(String.valueOf(objects[6]))));
			mapCityId.add(Utility.intConverter(String.valueOf(objects[4])));
		}
		boolean flag = false;
		long count = 0L;
		String city_name = "";
		String longitude = "";
		String latitude = "";
		for (int cityid : mapCityId) {
			count = 0L;
			List<BrowserBean> listBrowserBean = new ArrayList<BrowserBean>();
			for (CityModelBean cityModelBean : listCityModelBean) {
				if (cityid == cityModelBean.getCity_id()) {
					city_name = cityModelBean.getCity_name();
					longitude = cityModelBean.getCity_longitude();
					latitude = cityModelBean.getCity_latitude();
					count += cityModelBean.getCount();
					listBrowserBean.add(new BrowserBean(cityModelBean.getBrowserName(), cityModelBean.getCount()));
				}
			}
			for (String browserName : browsers) {
				flag = containsBrowserName(listBrowserBean, browserName);
				if (!flag) {
					listBrowserBean.add(new BrowserBean(browserName, 0));
				}
			}
			Collections.sort(listBrowserBean);
			cityDashBoardBean
			.add(new CityDashBoardBean(cityid, city_name, latitude, longitude, listBrowserBean, count));
		}
		return cityDashBoardBean;
	}

	public static boolean containsBrowserName(List<BrowserBean> listBrowserBean, String platformName) {
		for (BrowserBean object : listBrowserBean) {
			if (object.getPlatformName().equalsIgnoreCase(platformName)) {
				return true;
			}
		}
		return false;
	}

	public static int sendNotification(String title, String description, String icon, String click_action, String token,
			Properties myProperties) throws ClientProtocolException, IOException {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(myProperties.getProperty("firebase.url"));
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=" + myProperties.getProperty("firebase.key"));

		JSONObject message = new JSONObject();
		message.put("to", token);

		JSONObject notification = new JSONObject();
		notification.put("status", URLDecoder.decode(description, "UTF-8"));
		notification.put("title", URLDecoder.decode(title, "UTF-8"));
		notification.put("icon", icon);
		notification.put("tag", title);
		notification.put("url", click_action);
		notification.put("token_id", "148394610383074134");
		notification.put("campaign_id", "123");

		message.put("data", notification);

		post.setEntity(new StringEntity(StringEscapeUtils.unescapeJava(message.toString()), "UTF-8"));
		HttpResponse http_response = client.execute(post);
		StringBuilder response = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(http_response.getEntity().getContent()));

		String line = "";
		while ((line = rd.readLine()) != null) {
			response.append(line);
		}

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(response.toString());
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		logger.info("Unescape java " + StringEscapeUtils.unescapeJava(message.toString()));
		logger.info("Message To be sent " + message);
		logger.info("status code: " + http_response.getStatusLine());
		logger.info("response : " + response);
		return jsonObject.get("success").getAsInt();
	}

	public static String insertQueryBasedOnCriteria(CampaignBean campaignBean, Boolean segmented, int product_id) {
		HashMap<String, String> criteriaList = new HashMap<String, String>();
		StringBuilder appendList = new StringBuilder();
		if (segmented) {

			if (!campaignBean.getPlatform().isEmpty()) {
				if (campaignBean.getPlatform().contains(",")) {
					for (String browser : campaignBean.getPlatform().split(","))
						appendList.append("'" + browser + "',");
					criteriaList.put(Constants.PLATFORM_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.PLATFORM_ID, "'" + campaignBean.getPlatform() + "'");
				}
			}

			if (!campaignBean.getSubscribed().isEmpty()) {
				String[] not_clicked_dates = campaignBean.getSubscribed().split(",");
				criteriaList.put(Constants.SUBSCRIPTION_RANGE, "'" + not_clicked_dates[0] + "'");
			}

			if (!campaignBean.getIsps().isEmpty()) {
				if (campaignBean.getIsps().contains(",")) {
					appendList = new StringBuilder();
					for (String isp : campaignBean.getIsps().split(","))
						appendList.append("'" + isp + "',");
					criteriaList.put(Constants.ISP_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.ISP_ID, "'" + campaignBean.getIsps() + "'");
				}
			}

			if (!campaignBean.getCountries().isEmpty()) {
				if (campaignBean.getCountries().contains(",")) {
					appendList = new StringBuilder();
					for (String country : campaignBean.getCountries().split(","))
						appendList.append("'" + country + "',");
					criteriaList.put(Constants.COUNTRY_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.COUNTRY_ID, "'" + campaignBean.getCountries() + "'");
				}
			}

			if (!campaignBean.getCities().isEmpty()) {
				if (campaignBean.getCities().contains(",")) {
					appendList = new StringBuilder();
					for (String city : campaignBean.getCities().split(","))
						appendList.append("'" + city + "',");
					criteriaList.put(Constants.CITY_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.CITY_ID, "'" + campaignBean.getCities() + "'");
				}
			}
			if (!campaignBean.getSegments().isEmpty()) {
				if (campaignBean.getSegments().contains(",")) {
					appendList = new StringBuilder();
					for (String segments : campaignBean.getSegments().split(","))
						appendList.append("'" + segments + "',");
					criteriaList.put(Constants.SEGMENTS, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.SEGMENTS, "'" + campaignBean.getSegments() + "'");
				}
			}
		}

		return createQueryWithCriteria(criteriaList, campaignBean, segmented, product_id);
	}

	private static String createQueryWithCriteria(Map<String, String> criteriaList, CampaignBean campaignBean,
			boolean segmented, int product_id) {
		String subSelectQuery = "";
		String masterQuery = "";
		boolean isSubQueryConditional = false;
		boolean isMasterQueryConditional = false;

		if (segmented) {
			if (criteriaList.containsKey(Constants.SUBSCRIPTION_RANGE)
					|| criteriaList.containsKey(Constants.PLATFORM_ID) || criteriaList.containsKey(Constants.COUNTRY_ID)
					|| criteriaList.containsKey(Constants.ISP_ID) || criteriaList.containsKey(Constants.CITY_ID)
					|| criteriaList.containsKey(Constants.SEGMENTS)) {
				subSelectQuery = "select subscribers_id from " + Constants.IOPUSH_SUBSCRIBERS;
			}

			if (criteriaList.getOrDefault(Constants.PLATFORM_ID, "").length() > 0) {
				/*
				 * if(isSubQueryConditional) subSelectQuery +=
				 * " and fk_platform_id in(" +
				 * criteriaList.get(Constants.PLATFORM_ID) +")"; else{
				 */
				isSubQueryConditional = true;
				subSelectQuery += " where fk_platform_id in(" + criteriaList.get(Constants.PLATFORM_ID) + ")";

			}

			if (product_id > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_product_id in(" + product_id + ")";
				else {
					subSelectQuery += " where fk_product_id in(" + product_id + ")";
					isSubQueryConditional = true;
				}

			}

			if (criteriaList.getOrDefault(Constants.SUBSCRIPTION_RANGE, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and date(installation_date) >= "
							+ criteriaList.get(Constants.SUBSCRIPTION_RANGE);
				else {
					subSelectQuery += " where date(installation_date) >= "
							+ criteriaList.get(Constants.SUBSCRIPTION_RANGE);
					isSubQueryConditional = true;
				}
			}

			if (criteriaList.getOrDefault(Constants.COUNTRY_ID, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
				else {
					subSelectQuery += " where fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
					isSubQueryConditional = true;
				}

			}

			if (criteriaList.getOrDefault(Constants.CITY_ID, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_city_id in(" + criteriaList.get(Constants.CITY_ID) + ")";
				else {
					subSelectQuery += " where fk_city_id in(" + criteriaList.get(Constants.CITY_ID) + ")";
					isSubQueryConditional = true;
				}

			}

			if (criteriaList.getOrDefault(Constants.ISP_ID, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
				else {
					subSelectQuery += " where fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
					isSubQueryConditional = true;
				}
			}

			if (criteriaList.getOrDefault(Constants.SEGMENTS, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_segmentId in(" + criteriaList.get(Constants.SEGMENTS) + ")";
				else {
					subSelectQuery += " where fk_segmentId in(" + criteriaList.get(Constants.SEGMENTS) + ")";
					isSubQueryConditional = true;
				}
			}

			return (String.format(Constants.SELECT_SUBSCRIBERS, +campaignBean.getCampaign_id())
					+ (masterQuery.length() == 0 ? subSelectQuery : masterQuery) + ")");
		} else {

			subSelectQuery = "select subscribers_id from " + Constants.IOPUSH_SUBSCRIBERS;
			if (product_id > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_product_id in(" + product_id + ")";
				else {
					subSelectQuery += " where fk_product_id in(" + product_id + ")";
					isSubQueryConditional = true;
				}

			}
			return (String.format(Constants.SELECT_SUBSCRIBERS, +campaignBean.getCampaign_id())
					+ (masterQuery.length() == 0 ? subSelectQuery : masterQuery) + ")");
		}

		// change done by Sachin for DB optimization

		// return Constants.SELECT_EXTENSION_UPN +
		// (masterQuery.length()==0?subSelectQuery:masterQuery) + ")" ;
	}

	public static HttpResponse httpPost(String jsonObject, String url) throws UnsupportedEncodingException {

		HttpPost request = new HttpPost(url);
		StringEntity params = new StringEntity(jsonObject.toString(), "UTF-8");
		try {
			request.addHeader("content-type", "application/json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Error occurred while posting request ", e);
		}
		logger.info("URL to post the request[" + url + "]");


		if (logger.isDebugEnabled())
			logger.debug("jsonObject[" + jsonObject + "]");


		request.setEntity(params);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpResponse http_response = null;
		try {
			http_response = httpClient.execute(request);
			StringBuilder response = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(http_response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			logger.info("HTTP Response: [" + response + "]");

		} catch (ClientProtocolException e) {
			logger.error("Error occurred while posting request ", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error occurred while posting request ", e);
		}
		return http_response;
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	public static RSSFeedResponseBean checkRssFeeds(String rssUrl, int rssfeedid, Properties myProperties,
			String articleid, String logo_path, String duplicateLink)
					throws ClientProtocolException, IOException, IllegalArgumentException, FeedException {
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		RSSFeedResponseBean data = new RSSFeedResponseBean();
		String writerssfeedPath = myProperties.getProperty(env + "RSSFEED_FOLDER");
		String readrssfeedPath = Constants.RSSFEED_FOLDER_READ;
		String imageurl = myProperties.getProperty(env + "IMAGEURL");
		URL url = new URL(rssUrl);
		// Reading the feed
		SyndFeedInput input = new SyndFeedInput();
		XmlReader xmlReader = new XmlReader(url);
		xmlReader.setDefaultEncoding("UTF-8");
		SyndFeed feed = input.build(xmlReader);
		Iterator<SyndEntry> itEntries = feed.getEntries().iterator();
		while (itEntries.hasNext()) {
			SyndEntry entry = itEntries.next();

			data.setTitle(entry.getTitle());
			if (entry.getEnclosures().size() > 0) {
				data.setDescription(entry.getDescription().getValue());
				data.setEnclosure(((SyndEnclosure) entry.getEnclosures().get(0)).getUrl());
			} else {
				data.setDescription(entry.getDescription().getValue().replaceAll("\\<.*?\\>", ""));
				data.setEnclosure(extractImage(entry.getDescription().getValue()));
			}
			if (entry.getLink().trim().equalsIgnoreCase(duplicateLink.trim())) {
				data.setStatus(Constants.DUPLICATE_LINK);
				return data;
			}
			data.setLink(entry.getLink());

			data.setArticleid(articleid + "" + rssfeedid);
			data.setPubDate(entry.getPublishedDate() + "");
			data.setGuid("");
			if (isValidImage(data.getEnclosure()) == Constants.SUCCESS_CODE) {
				String imageName = getImageName(data.getEnclosure(), articleid + "" + rssfeedid);
				saveImage(data.getEnclosure(), writerssfeedPath, imageName);
				data.setEnclosure(imageurl + readrssfeedPath + imageName);
			} else
				data.setEnclosure(logo_path);
			data.setThumbnail("");
			data.setStatus(200);
			data.setContent("");
			data.setResult("Success");
			return data;

		}
		data.setStatus(Constants.ERROR_CODE_UNKNOWN);
		return data;

	}

	public static int isValidImage(String imageUrl) throws IOException {
		URL u = new URL(imageUrl);
		HttpURLConnection huc = (HttpURLConnection) u.openConnection();
		huc.setRequestMethod("GET"); // OR huc.setRequestMethod ("HEAD");
		huc.connect();
		return huc.getResponseCode();
	}

	public static RSSFeedResponseBean hitURL(String url, int rssfeedid, Properties myProperties, String articleid)
			throws ClientProtocolException, IOException {
		RSSFeedResponseBean data = new RSSFeedResponseBean();

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		int code = response.getStatusLine().getStatusCode();
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		if (code == 200) {
			data = xmlParser(result.toString(), rssfeedid, myProperties, articleid);
		} else {
			data.setStatus(code);
			data.setResult(line);
		}
		return data;
	}

	public static RSSFeedResponseBean xmlParser(String xml, int rssfeedid, Properties myProperties, String articleid) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		RSSFeedResponseBean data = new RSSFeedResponseBean();
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		String writerssfeedPath = myProperties.getProperty(env + "RSSFEED_FOLDER");
		String readrssfeedPath = Constants.RSSFEED_FOLDER_READ;
		String imageurl = myProperties.getProperty(env + "IMAGEURL");
		try {
			db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			Document doc = db.parse(is);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("item");
			Node nNode = nList.item(0);
			String link = "";
			String url = "";
			String imagename = "";

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				data.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
				data.setDescription(eElement.getElementsByTagName("description").item(0).getTextContent());
				link = eElement.getElementsByTagName("link").item(0).getTextContent();
				data.setLink(link);
				data.setArticleid(articleid + "" + rssfeedid);
				data.setPubDate(eElement.getElementsByTagName("pubDate").item(0).getTextContent());
				data.setGuid(eElement.getElementsByTagName("guid").item(0).getTextContent());
				// Element content =
				// (Element)eElement.getElementsByTagName("media:content").item(0);
				// data.setContent(content.getAttribute("url"));
				// Element thumbnail =
				// (Element)eElement.getElementsByTagName("media:thumbnail").item(0);
				// data.setThumbnail(thumbnail.getAttribute("url"));

				Element enclosure = (Element) eElement.getElementsByTagName("enclosure").item(0);
				url = enclosure.getAttribute("url");
				imagename = getImageName(url, articleid + "" + rssfeedid);
				saveImage(url, writerssfeedPath, imagename);
				data.setEnclosure(imageurl + readrssfeedPath + imagename);
				data.setThumbnail("");
				data.setContent(writerssfeedPath + imagename);
				data.setStatus(200);
				data.setResult("Success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String fetchArticleID(String link) {
		String article = new String(link);
		article = article.substring(article.lastIndexOf('-') + 1, article.lastIndexOf('.'));
		return article;
	}

	public static String insertQueryBasedOnCriteria(IopushRssFeedConfig iopushRssFeedConfig, String articleid) {
		HashMap<String, String> criteriaList = new HashMap<String, String>();
		StringBuilder appendList = new StringBuilder();

		if (!iopushRssFeedConfig.getPlatform().isEmpty()) {
			if (iopushRssFeedConfig.getPlatform().contains(",")) {
				for (String browser : iopushRssFeedConfig.getPlatform().split(","))
					appendList.append("'" + browser + "',");
				criteriaList.put(Constants.PLATFORM_ID, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.PLATFORM_ID, "'" + iopushRssFeedConfig.getPlatform() + "'");
			}
		}

		if (!iopushRssFeedConfig.getIsps().isEmpty()) {
			if (iopushRssFeedConfig.getIsps().contains(",")) {
				appendList = new StringBuilder();
				for (String isp : iopushRssFeedConfig.getIsps().split(","))
					appendList.append("'" + isp + "',");
				criteriaList.put(Constants.ISP_ID, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.ISP_ID, "'" + iopushRssFeedConfig.getIsps() + "'");
			}
		}

		if (!iopushRssFeedConfig.getSegments().isEmpty()) {
			if (iopushRssFeedConfig.getSegments().contains(",")) {
				appendList = new StringBuilder();
				for (String segments : iopushRssFeedConfig.getSegments().split(","))
					appendList.append("'" + segments + "',");
				criteriaList.put(Constants.SEGMENTS, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.SEGMENTS, "'" + iopushRssFeedConfig.getSegments() + "'");
			}
		}

		if (!iopushRssFeedConfig.getProducts().isEmpty()) {
			if (iopushRssFeedConfig.getProducts().contains(",")) {
				appendList = new StringBuilder();
				for (String productid : iopushRssFeedConfig.getProducts().split(","))
					appendList.append("'" + productid + "',");
				criteriaList.put(Constants.PRODUCT_ID, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.PRODUCT_ID, "'" + iopushRssFeedConfig.getProducts() + "'");
			}
		}

		if (!iopushRssFeedConfig.getCountries().isEmpty()) {
			if (iopushRssFeedConfig.getCountries().contains(",")) {
				appendList = new StringBuilder();
				for (String country : iopushRssFeedConfig.getCountries().split(","))
					appendList.append("'" + country + "',");
				criteriaList.put(Constants.COUNTRY_ID, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.COUNTRY_ID, "'" + iopushRssFeedConfig.getCountries() + "'");
			}
		}

		if (!iopushRssFeedConfig.getCities().isEmpty()) {
			if (iopushRssFeedConfig.getCities().contains(",")) {
				appendList = new StringBuilder();
				for (String city : iopushRssFeedConfig.getCities().split(","))
					appendList.append("'" + city + "',");
				criteriaList.put(Constants.CITY_ID, appendList.substring(0, appendList.length() - 1));
			} else {
				criteriaList.put(Constants.CITY_ID, "'" + iopushRssFeedConfig.getCities() + "'");
			}
		}
		return createQueryWithCriteria(criteriaList, articleid);
	}

	private static String createQueryWithCriteria(Map<String, String> criteriaList, String articleid) {
		String subSelectQuery = "";
		String masterQuery = "";
		boolean isSubQueryConditional = false;

		if (criteriaList.containsKey(Constants.PLATFORM_ID) || criteriaList.containsKey(Constants.PRODUCT_ID)
				|| criteriaList.containsKey(Constants.COUNTRY_ID) || criteriaList.containsKey(Constants.ISP_ID)
				|| criteriaList.containsKey(Constants.CITY_ID) || criteriaList.containsKey(Constants.SEGMENTS)) {
			subSelectQuery = "select subscribers_id from " + Constants.IOPUSH_SUBSCRIBERS;
		}

		if (criteriaList.getOrDefault(Constants.PLATFORM_ID, "").length() > 0) {
			isSubQueryConditional = true;
			subSelectQuery += " where fk_platform_id in(" + criteriaList.get(Constants.PLATFORM_ID) + ")";
		}

		if (criteriaList.getOrDefault(Constants.PRODUCT_ID, "").length() > 0) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_product_id in(" + criteriaList.get(Constants.PRODUCT_ID) + ")";
			else {
				subSelectQuery += " where fk_product_id in(" + criteriaList.get(Constants.PRODUCT_ID) + ")";
				isSubQueryConditional = true;
			}

		}

		if (criteriaList.getOrDefault(Constants.COUNTRY_ID, "").length() > 0) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
			else {
				subSelectQuery += " where fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
				isSubQueryConditional = true;
			}

		}

		if (criteriaList.getOrDefault(Constants.CITY_ID, "").length() > 0) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_city_id in(" + criteriaList.get(Constants.CITY_ID) + ")";
			else {
				subSelectQuery += " where fk_city_id in(" + criteriaList.get(Constants.CITY_ID) + ")";
				isSubQueryConditional = true;
			}

		}

		if (criteriaList.getOrDefault(Constants.ISP_ID, "").length() > 0) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
			else {
				subSelectQuery += " where fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
				isSubQueryConditional = true;
			}
		}

		if (criteriaList.getOrDefault(Constants.SEGMENTS, "").length() > 0) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_segmentid in(" + criteriaList.get(Constants.SEGMENTS) + ")";
			else {
				subSelectQuery += " where fk_segmentid in(" + criteriaList.get(Constants.SEGMENTS) + ")";
				isSubQueryConditional = true;
			}
		}

		return (String.format(Constants.SELECT_SUBSCRIBERS, articleid)
				+ (masterQuery.length() == 0 ? subSelectQuery : masterQuery) + ")");

	}

	public static void saveImage(String imageUrl, String destinationFolder, String filename) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFolder + filename);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}

	public static String insertQueryBasedOnCriteriaForSubCampaign(CampaignBean campaignBean, Boolean segmented,
			Integer product_id, int country_id) {
		HashMap<String, String> criteriaList = new HashMap<String, String>();
		StringBuilder appendList = new StringBuilder();
		if (segmented) {

			if (!campaignBean.getPlatform().isEmpty()) {
				if (campaignBean.getPlatform().contains(",")) {
					for (String browser : campaignBean.getPlatform().split(","))
						appendList.append("'" + browser + "',");
					criteriaList.put(Constants.PLATFORM_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.PLATFORM_ID, "'" + campaignBean.getPlatform() + "'");
				}
			}

			if (!campaignBean.getSubscribed().isEmpty()) {
				String[] not_clicked_dates = campaignBean.getSubscribed().split(",");
				criteriaList.put(Constants.SUBSCRIPTION_RANGE, "'" + not_clicked_dates[0] + "'");
			}

			if (!campaignBean.getIsps().isEmpty()) {
				if (campaignBean.getIsps().contains(",")) {
					appendList = new StringBuilder();
					for (String isp : campaignBean.getIsps().split(","))
						appendList.append("'" + isp + "',");
					criteriaList.put(Constants.ISP_ID, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.ISP_ID, "'" + campaignBean.getIsps() + "'");
				}
			}
			if (country_id > 0) {
				criteriaList.put(Constants.COUNTRY_ID, "'" + country_id + "'");
			}
			if (!campaignBean.getSegments().isEmpty()) {
				if (campaignBean.getSegments().contains(",")) {
					for (String segments : campaignBean.getSegments().split(","))
						appendList.append("'" + segments + "',");
					criteriaList.put(Constants.SEGMENTS, appendList.substring(0, appendList.length() - 1));
				} else {
					criteriaList.put(Constants.SEGMENTS, "'" + campaignBean.getSegments() + "'");
				}
			}

		}

		return createQueryWithCriteriaForSubCampaign(criteriaList, campaignBean, segmented, product_id, country_id);

	}

	private static String createQueryWithCriteriaForSubCampaign(HashMap<String, String> criteriaList,
			CampaignBean campaignBean, Boolean segmented, Integer product_id, int country_id) {
		String subSelectQuery = "";
		String masterQuery = "";
		boolean isSubQueryConditional = false;
		boolean isMasterQueryConditional = false;

		if (segmented) {
			if (criteriaList.containsKey(Constants.SUBSCRIPTION_RANGE)
					|| criteriaList.containsKey(Constants.PLATFORM_ID) || criteriaList.containsKey(Constants.COUNTRY_ID)
					|| criteriaList.containsKey(Constants.ISP_ID) || criteriaList.containsKey(Constants.CITY_ID)
					|| criteriaList.containsKey(Constants.SEGMENTS)) {
				subSelectQuery = "select subscribers_id from " + Constants.IOPUSH_SUBSCRIBERS;
			}

			if (criteriaList.getOrDefault(Constants.PLATFORM_ID, "").length() > 0) {
				/*
				 * if(isSubQueryConditional) subSelectQuery +=
				 * " and fk_platform_id in(" +
				 * criteriaList.get(Constants.PLATFORM_ID) +")"; else{
				 */
				isSubQueryConditional = true;
				subSelectQuery += " where fk_platform_id in(" + criteriaList.get(Constants.PLATFORM_ID) + ")";

			}

			if (product_id > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_product_id in(" + product_id + ")";
				else {
					subSelectQuery += " where fk_product_id in(" + product_id + ")";
					isSubQueryConditional = true;
				}

			}

			if (criteriaList.getOrDefault(Constants.SUBSCRIPTION_RANGE, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and date(installation_date) >= "
							+ criteriaList.get(Constants.SUBSCRIPTION_RANGE);
				else {
					subSelectQuery += " where date(installation_date) >= "
							+ criteriaList.get(Constants.SUBSCRIPTION_RANGE);
					isSubQueryConditional = true;
				}
			}

			if (criteriaList.getOrDefault(Constants.COUNTRY_ID, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
				else {
					subSelectQuery += " where fk_geo_id in(" + criteriaList.get(Constants.COUNTRY_ID) + ")";
					isSubQueryConditional = true;
				}

			}

			if (criteriaList.getOrDefault(Constants.ISP_ID, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
				else {
					subSelectQuery += " where fk_isp_id in(" + criteriaList.get(Constants.ISP_ID) + ")";
					isSubQueryConditional = true;
				}
			}
			if (criteriaList.getOrDefault(Constants.SEGMENTS, "").length() > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_segmentid in(" + criteriaList.get(Constants.SEGMENTS) + ")";
				else {
					subSelectQuery += " where fk_segmentid in(" + criteriaList.get(Constants.SEGMENTS) + ")";
					isSubQueryConditional = true;
				}
			}

			return (String.format(Constants.SELECT_SUBSCRIBERS, +campaignBean.getCampaign_id())
					+ (masterQuery.length() == 0 ? subSelectQuery : masterQuery) + ")");
		} else {

			subSelectQuery = "select subscribers_id from " + Constants.IOPUSH_SUBSCRIBERS;
			if (product_id > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_product_id in(" + product_id + ")";
				else {
					subSelectQuery += " where fk_product_id in(" + product_id + ")";
					isSubQueryConditional = true;
				}

			}
			if (country_id > 0) {
				if (isSubQueryConditional)
					subSelectQuery += " and fk_geo_id in(" + country_id + ")";
				else {
					subSelectQuery += " where fk_geo_id in(" + country_id + ")";
					isSubQueryConditional = true;
				}

			}

			return (String.format(Constants.SELECT_SUBSCRIBERS, +campaignBean.getCampaign_id())
					+ (masterQuery.length() == 0 ? subSelectQuery : masterQuery) + ")");
		}
	}

	public static String getImageName(String url, String filename) {
		String imagename = filename + "." + url.substring(url.lastIndexOf('.') + 1, url.length());
		return imagename;
	}

	public static String extractImage(String imgurl) {
		final String regex = "src=\"(.*?)\"";
		final Pattern p = Pattern.compile(regex);
		final Matcher m = p.matcher(imgurl);
		if (m.find()) {
			String s = m.group();
			if (s.contains("src")) {
				String quotes = s.split("=")[1];
				return quotes.substring(1, quotes.length() - 1);
			}
			return m.group();
		} else
			return "";
	}

	public static String getRSSLogo(String rssUrl) throws IllegalArgumentException, FeedException, IOException {
		URL url = new URL(rssUrl);
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(url.openStream());
			NodeList imageList = doc.getElementsByTagName("image");
			Element z = (Element) imageList.item(0);
			String mainImage = getValue(z, "url");
			NodeList items = doc.getElementsByTagName("item");
			// for (int j = 0; j < items.getLength(); j++) {
			Element item = (Element) items.item(0);
			if (getValue(item, "image").isEmpty()) {
				if (!getValue(item, "description").isEmpty() && getValue(item, "description").contains("img src")) {
					String image = getValue(item, "description").replaceAll("\"", "'");
					return image.substring(image.indexOf("img src") + 9,
							image.substring(0, image.indexOf("img src") + 11).length()
							+ image.substring(image.indexOf("img src") + 11).indexOf("'"));
				} else {
					return mainImage;
				}
			} else {
				return getValue(item, "image");
			}
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getValue(Element parent, String nodeName) {
		if (parent.getElementsByTagName(nodeName).item(0) == null) {
			return "";
		}
		if (parent.getElementsByTagName(nodeName).item(0).getFirstChild() == null) {
			return "";
		}
		return parent.getElementsByTagName(nodeName).item(0).getFirstChild().getNodeValue();
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	public static RSSFeedResponseBean checkRssUrl(String urlToCheck, Properties myProperties)
			throws IOException, IllegalArgumentException, FeedException {
		String env = myProperties.getProperty("ENVIORAMENT") + ".";
		RSSFeedResponseBean data = new RSSFeedResponseBean();
		String writerssfeedPath = myProperties.getProperty(env + "RSSFEED_FOLDER");
		String readrssfeedPath = Constants.RSSFEED_FOLDER_READ;
		URL url = new URL(urlToCheck);
		// Reading the feed
		SyndFeedInput input = new SyndFeedInput();
		XmlReader xmlReader = new XmlReader(url);
		xmlReader.setDefaultEncoding("UTF-8");
		SyndFeed feed = input.build(xmlReader);
		Iterator<SyndEntry> itEntries = feed.getEntries().iterator();
		if (itEntries.hasNext()) {
			data.setStatus(Constants.SUCCESS_CODE);
			return data;
		}
		data.setStatus(Constants.ERROR_CODE_INVALID);
		return data;
	}


	public static String insertQueryBasedOnCriteriaForRSS(RSSFeedBean rssfeedBean) throws ParseException {

		String subSelectQuery="";
		boolean isSubQueryConditional=false;

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT_YYYY_MM_DD_HH_MM_SS);

		/*if(criteriaList.containsKey("platform_id")
				||criteriaList.containsKey("geo_id")
				||criteriaList.containsKey("isp_id") 
				|| criteriaList.containsKey("city_id")){
			subSelectQuery = "select subscribers_id from iopush_subscribers ";
		}*/
		if(rssfeedBean.getProductId()>0){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_product_id in(" + rssfeedBean.getProductId() + ")"  ;
			else{
				subSelectQuery += " where fk_product_id in(" + rssfeedBean.getProductId() + ")"  ;
				isSubQueryConditional=true ;
			}

		}

		if((rssfeedBean.getPlatform() != null && !rssfeedBean.getPlatform().isEmpty()) ){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_platform_id in(" + rssfeedBean.getPlatform() +")";
			else
			{

				isSubQueryConditional = true;
			}

		}

		if (!rssfeedBean.getPlatform().isEmpty()) {
			if (isSubQueryConditional)
				subSelectQuery += " and fk_platform_id in(" + rssfeedBean.getPlatform() + ")";
			else {
				isSubQueryConditional = true;
				subSelectQuery += " where fk_platform_id in(" + rssfeedBean.getPlatform() + ")";
			}
		}


		if((rssfeedBean.getCountries()!=null &&  !rssfeedBean.getCountries().isEmpty() )){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_geo_id in(" + rssfeedBean.getCountries() + ")"  ;
			else{
				subSelectQuery += " where fk_geo_id in(" + rssfeedBean.getCountries() + ")"  ;
				isSubQueryConditional=true ;

			}

		}


		if((rssfeedBean.getCities() !=null &&  !rssfeedBean.getCities().isEmpty())){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_city_id in(" + rssfeedBean.getCities() + ")"  ;
			else{
				subSelectQuery += " where fk_city_id in(" + rssfeedBean.getCities() + ")"  ;
				isSubQueryConditional=true ;

			}

		}


		if(( rssfeedBean.getIsps() !=null && !rssfeedBean.getIsps().isEmpty() )){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_isp_id in(" + rssfeedBean.getIsps() + ")"  ;
			else{
				subSelectQuery += " where fk_isp_id in(" + rssfeedBean.getIsps() + ")"  ;
				isSubQueryConditional=true;
			}
		}
		if((rssfeedBean.getSegments() !=null && !rssfeedBean.getSegments().isEmpty() )){
			if(isSubQueryConditional)
				subSelectQuery += " and fk_segmentId in(" + rssfeedBean.getSegments() + ")"  ;
			else{
				subSelectQuery += " where fk_segmentId in(" + rssfeedBean.getSegments() + ")"  ;
				isSubQueryConditional=true;

			}
		}

		if((rssfeedBean.getSubscribedFrom() !=null && !rssfeedBean.getSubscribedFrom().isEmpty() )){
			if(isSubQueryConditional)
				subSelectQuery += " and date(installation_date) >= date('" + rssfeedBean.getSubscribedFrom().replace('T', ' ') + "') "  ;
			else{
				subSelectQuery += " where date(installation_date) >= date('" + rssfeedBean.getSubscribedFrom().replace('T', ' ') + "')"  ;
				isSubQueryConditional=true;
			}
		}
		subSelectQuery += "?dataSource=#dataSource";
		return "sql:select fcm_token, iopush_token, fk_city_id, fk_geo_id, fk_isp_id, fk_platform_id, fk_product_id , fk_segmentId from iopush_subscribers "
		+ subSelectQuery;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean makeCsvFile(List<UserBean> user, String fileName, String[] columns) {

		Boolean result = false;
		;
		com.opencsv.CSVWriter csvWriter = null;
		try {
			csvWriter = new CSVWriter(new FileWriter(fileName));
			// csvWriter.writeNext(columns);
			com.opencsv.bean.ColumnPositionMappingStrategy mappingStrategy = new com.opencsv.bean.ColumnPositionMappingStrategy();
			mappingStrategy.setType(UserBean.class);
			BeanToCsv bc = new BeanToCsv();
			mappingStrategy.setColumnMapping(columns);

			bc.write(mappingStrategy, csvWriter, user);
			result = true;
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}





	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean purchaseCancellationCSVFile(List<IoPushPaymentCancelledUserInfo> user,
			String fileName) {
		String[] columns = new String[] { "User Name", "Email Id", "Phone Number", "Package Name" };
		Boolean result=false;;
		com.opencsv.CSVWriter csvWriter=null;
		try {
			String [] record = null;
			csvWriter = new CSVWriter(new FileWriter(fileName));
			csvWriter.writeNext(columns);
			for(IoPushPaymentCancelledUserInfo object : user){
				record = new String []{ object.getUserName(),object.getEmail(),object.getPhoneNumber(),object.getPackageName()};
				csvWriter.writeNext(record);
			}
			result=true;
		} catch (IOException e) {

			e.printStackTrace();
		} finally {
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


	// 	@SuppressWarnings({ "rawtypes", "unchecked" })
	// 	public static boolean makeResendRegistrationMailCsvFile(List<UserBean> user, String fileName, String [] columns) {

	// 		Boolean result=false;;
	// 		com.opencsv.CSVWriter csvWriter=null;
	// 		try {
	// //			"Full name","Email","Phone","Domain","User name","Password","Date and time of registration"}
	// 			String [] record = null;
	// 			csvWriter = new CSVWriter(new FileWriter(fileName));
	// 			csvWriter.writeNext(columns);
	// 			for(UserBean userbean : user){
	// 				record = new String []{ userbean.getFullName(),userbean.getEmailId(),userbean.getPhoneNumber(),userbean.getSubDomain(),userbean.getUsername(),userbean.getPassword(),userbean.getRegistrationDate()};
	// 				csvWriter.writeNext(record);
	// 			}
	// 			result=true;
	// 		}
	// 		catch (IOException e) {

	// 			e.printStackTrace();
	// 		}
	// 		finally
	// 		{

	// 			try {
	// 				csvWriter.close();
	// 			} catch (IOException e) {
	// 				e.printStackTrace();
	// 			}
	// 		}
	// <<<<<<< HEAD
	// 		return result;
	// 	}

	public static String findUserPlanInfo(int prodId) {

		String hql = "select userplan_id,plan_name,pricing,subscriber_limit from iopush_plan  where userplan_id  not in (select fk_plan_id from iopush_paypal_payment  where fk_product_id ="
				+ prodId + ") and userplan_id !=6";

		return hql;
	}

	public static String createIncompletePaymentQuery() throws ParseException {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		String hql="";

		date = dateFormat.parse(dateFormat.format(new Date()));

		hql = "  select user1.first_name,user1.email_id,user1.phone_number,plan.plan_name,package1.pricing,package1.subscribers_limit, payment.creation_date from iopush_payment payment"
				+" join  iopush_user user1 on payment.fk_product_id=user1.fk_product_id"
				+" join  iopush_user_plan plan on payment.fk_plan_id=plan.plan_id "
				+" join   iopush_package_plan package1 on package1.package_id=payment.fk_package_id"
				+" where payment.tp_acknowledgement=0  and  payment.creation_date>'"+date+"'";
		return hql;
	}

	// =======
	// 		return result; 
	// 	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean makeImplementedUserReportCsvFile(List<UserBean> user, String fileName, String [] columns) {

		Boolean result=false;;
		com.opencsv.CSVWriter csvWriter=null;
		try {
			//			"Name","Email","Phone number","Website URL","Registration date"

			String [] record = null;
			csvWriter = new CSVWriter(new FileWriter(fileName));
			csvWriter.writeNext(columns);
			for(UserBean userbean : user){
				record = new String []{userbean.getUsername(),userbean.getEmailId(),userbean.getPhoneNumber(),userbean.getWebsiteUrl(),userbean.getRegistrationDate()};
				csvWriter.writeNext(record);
			}
			result=true;
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		finally
		{
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result; 
	}










	public static String QueryForLastNotification(int productID) {


		return "select creationDate, forwardUrl, campaignopen, campaignclick, campaignsent from "
				+ IopushCampaign.class.getName() + " where iopushProduct.productID=" + productID
				+ " and campaignStatus in (1, 3) order by campaignStatus ASC, campaignId DESC";

	}

	public static String QueryForPreviewLastNotification(int productID) {

		return "select title, description, forwardUrl, imagePath, campaignId from " + IopushCampaign.class.getName()
				+ " where iopushProduct.productID=" + productID
				+ " and campaignStatus in (1, 3) order by campaignStatus ASC, campaignId DESC";

	}


	/*public static String QueryForFindUserdetail(){
		return "select user_id,email_id,username,website_url ,phone_number ,created_on from iopush_user  where fk_product_id  not in"
				+ " (select fk_product_id from iopush_subscribers )";
	}*/
	public static String packagesList(Integer[] fkPlanId, int fkPackageId) {

		return "select package.packageId, package.fkPlanId, package.subscribersLimit, package.pricing, (select planName from "+ IopushPlan.class.getName() +" where planId=package.fkPlanId) as planName"
				+ " from "+IopushPackagePlan.class.getName()
				+ " package where package.fkPlanId in ("+integerArraytoString(fkPlanId)+") and package.packageId > "+fkPackageId+" order by package.fkPlanId";
	}
	public static String planList(int fkPlanId) {
		return "select fkPlanId from "+IopushPlanPreference.class.getName() +" where preference >= "
				+ "(select preference from "+ IopushPlanPreference.class.getName()+"  where fkPlanId = "+fkPlanId+")";
	}
	public static String allPackagesList() {
		return "select package.packageId,package.fkPlanId, package.subscribersLimit, package.pricing, (select planName from "+ IopushPlan.class.getName() +" where planId=package.fkPlanId) as planName"
				+ " from "+IopushPackagePlan.class.getName()+" package";
	}

	public static String createPackageInfoQuery(int planId, int packageId) {

		String query="select (select plan_name from iopush_user_plan where plan_id="+planId+"),b.pricing, b.subscribers_limit from iopush_package_plan b where b.package_id ="+packageId+"";
		return query;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean makeFailCustomerRenewalReportCsvFile(List<RenewalFailedInfoBean> failedUserlist, String fileName, String [] columns) {

		Boolean result=false;;
		com.opencsv.CSVWriter csvWriter=null;
		try {
			String [] record = null;
			csvWriter = new CSVWriter(new FileWriter(fileName));
			csvWriter.writeNext(columns);
			for(RenewalFailedInfoBean iopushUser : failedUserlist){
				record = new String []{ iopushUser.getFirstName() ,iopushUser.getEmailId(),iopushUser.getPhoneNumber(),iopushUser.getRenewAmount(),iopushUser.getDate(),iopushUser.getAgreementStatus(),iopushUser.getOutstandingBalance() };
				csvWriter.writeNext(record);
			}
			if(csvWriter!=null)
				csvWriter.close();
			result=true;

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return result; 
	}

	public static String queryForFindRenewalFailCustomerInfo(String d1,String d2){


		String query="select a.first_name,a.email_id,a.phone_number,b.renew_amount,b.creation_date,b.agreement_status ,b.outstanding_balance from iopush_user a,iopush_payment b where a.fk_product_id in (select b.fk_product_id from iopush_payment where b.payment_type='Renew' and b.renew_payment_status='false' and b.modified_on between '"+d2+"'and '"+d1+"')";
		return query;

	}


	public static String QueryForFindUserdetail(){
		return "select user_id,email_id,username,website_url ,phone_number ,created_on from iopush_user  where fk_product_id  not in"
				+ " (select fk_product_id from iopush_subscribers )";
	}


	public static String manageWelcomeQuery(Integer[] status, String welcomeName, boolean flag,
			int pid, String columnForOrdering, String requiredOrder) {
		String query= "select welcomeId, "
				+ "welcomeName, "
				+ "welcomeScheduleDate,"
				+ " welcomeEndDate, "
				+ "modificationDate,"
				+ " welcomeStatus, "
				+ "title,"
				+ " description,"
				+ "forwardUrl,"
				+ " imagePath, "
				+ "cities,"
				+ " segmented,"
				+ " countries,"
				+ " platform,"
				+ " active,"
				+ " segment_id,"
				+ " segmentType_id, "
				+ "iopushProduct.productID"
				+ " from "+IopushWelcome.class.getName() +" where iopushProduct.productID = "+pid +" AND ";
		if(!welcomeName.isEmpty())
		{
			if(Utility.intConverter(welcomeName)==0)
			{
				query += " welcomeName LIKE '%"+welcomeName+"%' AND";
			}
			else
			{
				query += " welcomeId = "+welcomeName+" AND ";
			}
		}
		if(!flag)
		{
			query += " welcomeStatus in ("+integerArraytoString(status)+")";
		}
		if(!columnForOrdering.equalsIgnoreCase("welcomeId"))
			query += " order by "+columnForOrdering+" "+requiredOrder+", welcomeId DESC";
		else
			query += " order by "+columnForOrdering+" "+requiredOrder;


		return query;
	}


	public static String updateActiveStatusForUser(String username){
		return "update "+IopushUser.class.getName()+" set Active=true , linkRoute = true where username = '"+username+"'";
	}


	public static String listCampaignQueryPlatform(Integer[] status,String campaign_name,int startIndex,int pageSize, boolean flag,boolean analytics,String campaign_date1,String campaign_date2,int pid){
		String hql = "Select "
				+ "campaignId,campaignName,"
				+ "campaignScheduleDate,"
				+ "campaignCurrentDate,campaignStatus,iopushTimeZone.timezone, iopushTimeZone.timezoneID,campaignEndDate, segmented, modificationDate"
				+ " from " + IopushCampaign.class.getName() +" where iopushProduct.productID=" + pid + " and ";
		if(!flag || campaign_name.length()>0 || analytics)
		{
			int campaign_id =Utility.intConverter(campaign_name);
			if(campaign_name.length()>0){
				if(campaign_id==0)
					hql=hql+" LOWER(campaignName) LIKE '%"+campaign_name.toLowerCase()+"%' AND "; 
				else
					hql=hql+" campaignId="+campaign_id +" AND "; 
			}

			if(analytics){
				hql=hql+" campaignStatus IN ("+Constants.LIVE+","+Constants.EXPIRE+")  AND "; 
				if(campaign_date1!=null){
					hql=hql+" campaignStartDate >= '"+campaign_date1+"'  AND ";
				}
				if(campaign_date2!=null){ 
					hql=hql+" campaignStartDate <= '"+campaign_date2+'T'+"23:59:59'  AND ";
				}
			}else{
				if(!flag)
					hql=hql+" campaignStatus IN ("+integerArraytoString(status)+")";
			}
		}

		return hql;
	}




	public static String manageWelcomeQueryPlatform(Integer[] status, String welcomeName, boolean flag,
			int pid) {
		String query= "select welcomeId, "
				+ "welcomeName, "
				+ "welcomeScheduleDate,"
				+ " welcomeEndDate, "
				+ "modificationDate,"
				+ " welcomeStatus, "
				+ "title,"
				+ " description,"
				+ "forwardUrl,"
				+ " imagePath, "
				+ "cities,"
				+ " segmented,"
				+ " countries,"
				+ " platform,"
				+ " active,"
				+ " segment_id,"
				+ " segmentType_id, "
				+ "iopushProduct.productID"
				+ " from "+IopushWelcome.class.getName() +" where iopushProduct.productID = "+pid +" AND ";
		if(!welcomeName.isEmpty())
		{
			if(Utility.intConverter(welcomeName)==0)
			{
				query += " welcomeName LIKE '%"+welcomeName+"%' AND";
			}
			else
			{
				query += " welcomeId = "+welcomeName+" AND ";
			}
		}
		if(!flag)
		{
			query += "welcomeStatus in ("+integerArraytoString(status)+")";
		}

		return query;
	}


	public static  String convertIntToStr(String platform) {
		String test[] = platform.split(",");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < test.length; i++)

		{

			switch (test[i]) {

			case "1":
				if (i == (test.length - 1))
					sb.append("Chrome");
				else
					sb.append("Chrome,");
				break;
			case "2":
				if (i == (test.length - 1))
					sb.append("Firefox");
				else
					sb.append("Firefox,");
				break;
			case "3":
				if (i == (test.length - 1))
					sb.append("Opera");
				else
					sb.append("Opera,");
				break;
			case "4":
				if (i == (test.length - 1))
					sb.append("Safari");
				else
					sb.append("Safari,");
				break;
			case "5":
				if (i == (test.length - 1))
					sb.append("Android");
				else
					sb.append("Android,");
				break;
			default:
				break;

			}

			// combined string
			platform = sb.toString();
			// now sort the combined string
			// System.out.println("value at platform ::" + platform);

		}
		return platform;
	}

	public static  String convertStrToSortedInt(String platform) {
		String sorted = null;
		String test[] = platform.split(",");
		StringBuilder sb = new StringBuilder();
		StringBuilder sortedDotSeparatedString = null;
		for (int i = 0; i < test.length; i++) {
			switch (test[i]) {

			case "Android":
				sb.append("1");
				break;
			case "Chrome":
				sb.append("2");
				break;
			case "Firefox":
				sb.append("3");
				break;
			case "Opera":
				sb.append("4");
				break;
			case "Safari":
				sb.append("5");
				break;
			default:
				break;

			}

			// 2.3.4.5.1.
			platform = sb.toString();

			char[] chars = platform.toCharArray();

			Arrays.sort(chars);
			sorted = new String(chars);

			char[] char2 = sorted.toCharArray();
			sortedDotSeparatedString = new StringBuilder();

			for (int j = 0; j < char2.length; j++) {
				if (j == (char2.length - 1))
					sortedDotSeparatedString.append(char2[j] + "");
				else
					sortedDotSeparatedString.append(char2[j] + ".");
			}

			// System.out.println("value at sortedDotSeparatedString ::" +
			// sortedDotSeparatedString.toString());
			// sorted string

		}

		return sortedDotSeparatedString.toString();
	}

	public static String convertStringPlatformSorted(String platform) {
		String test[] = platform.split("\\.");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < test.length; i++)

		{

			// System.out.println("value :::"+ test[i] + " value of i "+ i + "
			// lenght:"+test.length);
			switch (test[i]) {

			case "1":
				if (i == (test.length - 1))
					sb.append("Android");
				else
					sb.append("Android/");
				break;
			case "2":
				if (i == (test.length - 1))
					sb.append("Chrome");
				else
					sb.append("Chrome/");
				break;
			case "3":
				if (i == (test.length - 1))
					sb.append("Firefox");
				else
					sb.append("Firefox/");
				break;
			case "4":
				if (i == (test.length - 1))
					sb.append("Opera");
				else
					sb.append("Opera/");
				break;
			case "5":
				if (i == (test.length - 1))
					sb.append("Safari");
				else
					sb.append("Safari/");
				break;
			default:
				break;

			}

		}
		return sb.toString();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean makeResendRegistrationMailCsvFile(List<IopushUser> user, String fileName, String [] columns) {

		Boolean result=false;;
		com.opencsv.CSVWriter csvWriter=null;
		try {
			String [] record = null;
			csvWriter = new CSVWriter(new FileWriter(fileName));
			csvWriter.writeNext(columns);

			for(IopushUser userbean : user){
				String registrationDate = userbean.getCreatedOn()==null?"":" " +userbean.getCreatedOn();
				String firstname=userbean.getFirstName()==null?"":userbean.getFirstName();
				String lastname=userbean.getLastName()==null?"":userbean.getLastName();
				String username=firstname+ " "+lastname;
				record = new String []{ username,userbean.getEmailId(),userbean.getPhoneNumber(),userbean.getIopushProduct().getProductName(),userbean.getUsername(),userbean.getPassword(),registrationDate};
				csvWriter.writeNext(record);
			}
			result=true;
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		finally
		{
			try {
				csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result; 
	}

	public static String getUserInfoQuery(String agreementID) {

		String query="select a.first_name,a.email_id,a.phone_number,b.renew_amount,b.modified_on,b.agreement_status ,b.outstanding_balance from iopush_user a,iopush_payment b where a.fk_product_id in (select distinct(b.fk_product_id) from iopush_payment where b.agreement_id='"+agreementID+"')";
		return query;
	}

}
