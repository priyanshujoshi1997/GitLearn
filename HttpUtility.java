package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtility {
	static java.net.CookieManager msCookieManager = new java.net.CookieManager();
	static final String COOKIES_HEADER = "Set-Cookie";
	static final String USER_AGENT = "Mozilla/5.0";
	static LinkedHashMap<String, String> map_cookie = new LinkedHashMap<String, String>();

	public static String doGet(String prm_url, HashMap<String, String> params, HashMap<String, String> headers) {
		String inputLine = "";
		String cookieKey= null;
		StringBuffer response = new StringBuffer();
		try {
			URL url = new URL(prm_url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			Set<String> keys = headers.keySet();

			for (String itr : keys) {
				System.out.println(itr + " " + headers.get(itr));
				if(itr.equalsIgnoreCase("cookie")) {
					cookieKey = headers.get(itr).split(";")[0];
				}
			}
			System.out.println("GET :: "+cookieKey+"  :: "+map_cookie);
			
			if(!map_cookie.isEmpty() && map_cookie.get(cookieKey)!=null) {
				con.setRequestProperty("Cookie", map_cookie.get(cookieKey));
			}
			
			con.setRequestProperty("Content-type", "application/json");
			con.setRequestProperty("user-agent", USER_AGENT);
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}

	public static String sendPOST(String prm_url, String params, HashMap<String, String> headers) throws IOException {
		StringBuffer response = null;
		URL obj = new URL(prm_url);
		String cookieKey = null;
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		String cookieVal = null;
		Set<String> keys = headers.keySet();
		for (String itr : keys) {
			System.out.println(itr + " " + headers.get(itr));
			if(itr.equalsIgnoreCase("cookie")) {
				cookieKey = headers.get(itr).split(";")[0];
			}
		}
		con.setRequestProperty("Content-type", "application/json");
		con.setRequestProperty("user-agent", USER_AGENT);
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(params.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			Map<String, List<String>> headerFields = con.getHeaderFields();
			List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

			if (cookiesHeader != null) {
				for (String cookie : cookiesHeader) {
					cookieVal = cookie;
				}
			}
			map_cookie.put(cookieKey, cookieVal);
			System.out.println("POST:  "+cookieKey+"  :: "+map_cookie);
			in.close();
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
		return response.toString();
	}

}
