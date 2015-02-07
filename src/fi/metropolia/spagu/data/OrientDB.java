package fi.metropolia.spagu.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import fi.metropolia.spagu.business.BusinessLogic;
import fi.metropolia.spagu.business.LogParser;

public class OrientDB {

	private static String username = "admin";
	private static String password = "admin";

	public static void main(String[] args) {

		//System.out.println("Get: " +excuteGet());
	
		try {
			createData(BusinessLogic.summarizeVisitorList(LogParser.parseLineCreateVisitor(LogParser.FILE)));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String excutePost(JsonObject json) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.getCredentialsProvider().setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(username, password));
			HttpPost post = new HttpPost(
					"http://orientdb.spagu.metropolia.fi/document/location2/");

			post.addHeader("Content-type", "application/json");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			HttpResponse resp = httpclient.execute(post);

			//System.out.println(resp.getStatusLine());
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					resp.getEntity().getContent()));

			return reader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject excuteGet() {
		String urlStr = "http://orientdb.spagu.metropolia.fi/class/location2/visitors";
		try {
			DefaultHttpClient c = new DefaultHttpClient();
			c.getCredentialsProvider().setCredentials(AuthScope.ANY,
					new UsernamePasswordCredentials(username, password));
			HttpGet get = new HttpGet(urlStr);
			HttpResponse resp = c.execute(get);

			BufferedReader reader = new BufferedReader(new InputStreamReader(resp
					.getEntity().getContent()));

			JsonParser jParser = new JsonParser();
			
			return jParser.parse(reader).getAsJsonObject();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void createData(ArrayList<SummerizedVisitor> summarizedVisitorsList)
			throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("Now started ...");
		/**for (SummerizedVisitor sumVisitor : summarizedVisitorsList) {
			JsonObject json = new JsonObject();

			json.addProperty("@class", "events-24.5-10.6");
			json.addProperty("EventNo", sumVisitor.getEventNo());
			json.addProperty("TagID", sumVisitor.getTagId());
			json.addProperty("MuseumName", sumVisitor.getMuseumName());
			json.addProperty("PlaceName", sumVisitor.getPlaceName());
			json.addProperty("NodeName", sumVisitor.getNodeName());
			json.addProperty("StartTime", sumVisitor.getStartTime());
			json.addProperty("EndTime", sumVisitor.getEndTime());
			json.addProperty("Duration", sumVisitor.getDuration());
			
			excutePost(json);
		}*/
		long end = System.currentTimeMillis();
        System.out.println("Needs " + (end - start) + " ms to process");
		System.out.println("Done !");
	}

}
