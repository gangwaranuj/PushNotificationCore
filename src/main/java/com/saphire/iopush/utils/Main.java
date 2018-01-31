
package com.saphire.iopush.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.security.crypto.codec.Base64;
import com.sun.mail.util.BASE64EncoderStream;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException {

/*	  int max = 100000;
	    int min = 0;
//	    var uniquee = new Date(now).getTime();
//	    if (uniqueno === null)
//	        uniqueno = uniquee + '' + Math.floor(Math.random() * (max - min));

		String query="INSERT INTO public.iopush_subscribers( subscribers_id, fcm_token, installation_date, iopush_token, os_language,os_name, fk_city_id, fk_geo_id, fk_isp_id, fk_platform_id, fk_category_id, fk_product_id) VALUES "; 
		String os_name[]={"ANDROID_OS","WINDOWS"};
		Random random = new Random();
		for(int i =0;i<10;i++){

			int os_name_index=random.nextInt(os_name.length);
			String values="("+"fcm_token"+","+"now()" +"," +"iopush_token " +"," +"ENG"+","+os_name[os_name_index]+","+"fk_city_id"+"," +"fk_geo_id"+","+"fk_platform_id"+","+"fk_category_id"+","+"fk_product_id"+")" ;
			System.out.println(query+values);*/

/*			
			
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
			   int beginIndex = random.nextInt(100);       //Begin index + length of your string < data length
			   int endIndex = beginIndex + 10;            //Length of string which you want

			   String yourID = data.substring(beginIndex, endIndex);
			   System.out.println(yourID);*/
		String text = "gautam.kanika@thinksys.com";
		  byte[] encodedBytes = Base64.encode(text.getBytes());
		  System.out.println(URLEncoder.encode(new String(encodedBytes), "UTF-8"));
		
		}
	

}
