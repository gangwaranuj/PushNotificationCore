package com.saphire.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
class Employee{
	
	
	
	int id;
	int age;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	
}

public class Main {

	
	public static void main(String[] args) throws IllegalArgumentException, IOException {

		System.out.println(StringEscapeUtils.unescapeJava(StringEscapeUtils.escapeJava("❤️😳😜😌😞😃emoticon_testingS_campaign")));
		
//System.out.println(StringEscapeUtils.escapeJava("❤️😳😜😌😞😃emoticon_testingS_campaign")) ;
		System.out.println(StringEscapeUtils.unescapeHtml3("<p>&#8222;Und ewig schläft das PUBERTIER&#8220; ist der dritte Streich von Jan Weiler. Ihr kennt das Pubertier noch nicht? Das ist Nick! Ein echtes Parade-Exemplar: Als männliches Pubertier besticht auch er durch faszinierende Einlassungen zu den Themen Mädchen, Umwelt und Politik sowie durch seine anhaltende Begeisterungsfähigkeit für ganz schlechtes Essen und seltsame Musik. Er wächst wie [&#8230;]</p><p>The post <a rel=\"nofollow\" href=\"https://www.daddylicious.de/pubertier-interview-mit-daddy-und-buchautor-jan-weiler/\">Pubertier? Interview mit Daddy und Buchautor Jan Weiler</a> appeared first on <a rel=\"nofollow\" href=\"https://www.daddylicious.de\">DADDYlicious</a>.</p>"));
		
	/*	URL url = new URL("http://navbharattimes.indiatimes.com/rssfeedsdefault.cms");
		HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
		// Reading the feed
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = input.build(new XmlReader(httpcon));
		List entries = feed.getEntries();
		Iterator itEntries = entries.iterator();

		while (itEntries.hasNext()) {
			SyndEntry entry = (SyndEntry) itEntries.next();
			System.out.println("Title: " + entry.getTitle());
			System.out.println("Link: " + entry.getLink());
			System.out.println("Author: " + entry.getAuthor());
			System.out.println("Publish Date: " + entry.getPublishedDate());
			System.out.println("Description: " + entry.getDescription().getValue());
			System.out.println(entry.getEnclosures().size());
		}*/

//		System.out.println(new String("emoji, m\u2764\uFE0Fn am\u2764\\uFE0Fur",Charset.forName("UTF-8")));
		/*String s = "emoji, m\\u2764\\uFE0Fn am\\u2764\\uFE0Fur" ;
		System.out.println(URLDecoder.decode(s,"UTF-8") );*/
		
//		System.out.println("emoji, m\\u2764\\uFE0Fn am\\u2764\\uFE0Fur");
/*		System.out.println(StringEscapeUtils.unescapeJava("emoji, m\\u2764\\uFE0Fn am\\u2764\\uFE0Fur"));
		sendNotification();*/
	}
	
	public static void sendNotification() throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=AIzaSyC5GNmBNZ99iuoiGQ9kWVbIgrAs6970RVk");

		JSONObject message = new JSONObject();
		message.put("to", "fb8iEdzxEGE:APA91bFmbZqBdaxm5YGhbH19c6MXtlRlyRCS-rpenKoGD7f63bauAXo_NDuqz5CGju44VfaYCHKFe7Br_LfYjAXa-zqfjCB3afkpXGIYVSany1N3Bsr5WBThb7hB_iQnTd4yYbPt6Lqv");

		JSONObject notification = new JSONObject();
		notification.put("status", "Here is a boy: \uD83D\uDC66\uD83C\uDFFF!");
		notification.put("title", "emoji, m\u2764\uFE0Fn am\u2764\uFE0Fur");
		notification.put("icon", "https://dev.iosistech.com/icmp/images/favicon.png") ;
		notification.put("tag", "hello");
		notification.put("url", "http://www.computerbild.de/artikel/cb-News-Internet-Amazon-Prime-Preis-Erhoehung-16668425.html");
		notification.put("token_id", "148394610383074134");
		notification.put("campaign_id", "123");

		message.put("data", notification);

		post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		System.out.println(message.toString());
		HttpResponse http_response = client.execute(post);
		StringBuilder response = new StringBuilder();
		BufferedReader rd = new BufferedReader
				(new InputStreamReader(
						http_response.getEntity().getContent()));

		String line = "";
		while ((line = rd.readLine()) != null) {
			response.append(line);
		}
		System.out.println(response.toString());

		
	}	
}
