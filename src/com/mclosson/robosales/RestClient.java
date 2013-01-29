package com.mclosson.robosales;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {
	
	private String baseUrl = "";
	
	public JSONObject get(String id) {
		String results = this.request(new HttpGet(), id);
		JSONObject json = null;
		
		try {
			json = new JSONObject(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	public JSONObject post(String id) {
		String results = this.request(new HttpPost(), id);
		JSONObject json = null;
		
		try {
			json = new JSONObject(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;		
	}

	public JSONObject put(String id) {
		String results = this.request(new HttpPut(), id);
		JSONObject json = null;
		
		try {
			json = new JSONObject(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;		
	}
	
	public JSONObject delete(String id) {
		String results = this.request(new HttpDelete(), id);
		JSONObject json = null;
		
		try {
			json = new JSONObject(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;		
	}
	
	private String request(HttpRequestBase request, String uri) {
		String results = "";
		//demo uri = "http://10.0.2.2:3000/api/v1/sku/123/122"
		
		try {
	      HttpClient client = new DefaultHttpClient();
	      //HttpGet request = new HttpGet();
	      request.setURI(new URI(baseUrl + uri));
	      results = client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		} 
		finally {}
		return results;
	}
	
}
