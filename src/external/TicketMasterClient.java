package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TicketMasterClient {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = "";
	private static final String API_KEY = "Gtr12Dx04ZQtPOkhZ2qPqhodOwIGSa6A";
	
	public JSONArray search(double lat, double lon, String keyword) {
		if(keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		}catch(UnsupportedEncodingException e) {
			
		}
		String query = String.format("apikey=%s&latlong=%s,%s&keyword=%s&radius=%s", 
				API_KEY, lat, lon, keyword, 50);
		String url = URL+"?"+query;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			
			if(responseCode != 200) {
				return new JSONArray();
			}
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			while((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			
			JSONObject obj = new JSONObject(response.toString());
			
			if(!obj.isNull("_embedded")) {
				JSONObject embedded = obj.getJSONObject("_embedded");
				return embedded.getJSONArray("events");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new JSONArray();
	}
	
	private void queryAPI(double lat, double lon) {
		JSONArray events = search(lat, lon, null);
		try {
			for(int i=0; i<events.length(); i++) {
				JSONObject event = events.getJSONObject(i);
				System.out.println(event.toString(2));
			}
		}catch(Exception e) {
			
		}
	}
	
	public static void main(String[] args) {
		TicketMasterClient temp = new TicketMasterClient();
		temp.queryAPI(37.38, -122.08);
		temp.queryAPI(51.503364, -0.12);
	}

}