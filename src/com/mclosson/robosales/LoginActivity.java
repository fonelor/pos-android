package com.mclosson.robosales;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
     
        Button buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(loginHandler);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	 
	View.OnClickListener loginHandler = new View.OnClickListener() {
		//192.168.32.128:3000
		public void onClick(View v) {
		String token, ticket, cash, visa, gift;
		JSONArray json;
		
		token = signin("mclosson", "mpass");
		clockin(token);
		getsku(token, "100");
		getsku(token, "101");
		getsku(token, "102");
		ticket = createticket(token);
		addsku(token, ticket, "100");
		addsku(token, ticket, "101");
		addsku(token, ticket, "102");

		json = getpaymenttypes(token);
	//	cash = ((JSONObject)json[0]).getString("id");
	//    visa = ((JSONObject)json[4]).getString("id");
	//    gift = ((JSONObject)json[3]).getString("id");
	//	addpayment(token, ticket, cash, "20.00");
	//	addpayment(token, ticket, visa, "30.35");
	//	addpayment(token, ticket, gift, "15.57");
		
		getreciept(token, ticket);
		clockout(token);
		logout(token);
	};
	
	private String signin(String username, String password)
	{
		String results = "";
		String token = "";
		
		try {
	      HttpClient client = new DefaultHttpClient();
	      HttpPost request = new HttpPost();
	      request.addHeader("Accept", "application/json");
	      request.addHeader("Content-type", "application/json");
	      String requestBody = "{\"username\":\"mclosson\",\"password\":\"mpass\"}";
	      request.setEntity(new ByteArrayEntity(requestBody.getBytes("UTF8")));
	      request.setURI(new URI("http://192.168.32.128:3000/api/v1/sessions"));
	      results = client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		} 
		finally {}
					
		if (results.contains("token"))
		{
			try {
				JSONObject json = new JSONObject(results);
				token = json.getString("token");
			} catch (JSONException e) {
				e.printStackTrace();
			}				
			finally {}
		}
		
		return token;
	}
	
	private void clockin(String token)
	{		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/sessions/clock"));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}
	}
	
	private void getsku(String token, String sku) 
	{		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/skus/" + sku));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();		
		}				
		finally {}
	}
	
	private String createticket(String token)
	{
		String results = "";
		String ticket = "";
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/tickets"));
			results = client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}

		if (results.contains("ticket_id"))
		{
			try {
				JSONObject json = new JSONObject(results);
				ticket = json.getString("ticket_id");
			} catch (JSONException e) {
				e.printStackTrace();
			}				
			finally {}
		}
		
		return ticket;
	}

	private void addsku(String token, String ticket, String sku)
	{
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			String requestBody = "{\"sku\":\"" + sku + "\"}";
		    request.setEntity(new ByteArrayEntity(requestBody.getBytes("UTF8")));
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/tickets/" + ticket + "/skus"));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}
	}
    
    private JSONArray getpaymenttypes(String token)
    {
    	String results = "";
    	
    	JSONArray json = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/paymenttypes"));
			results = client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}

		try {
			json = new JSONArray(results);
		} catch (JSONException e) {
			e.printStackTrace();
		}				
		finally {}
		
		return json;
    }

    private void addpayment(String token, String ticket, String paymenttype, String amount)
    {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			String requestBody = "{\"payment_type_id\":\"" + paymenttype + "\", \"amount\":\"" + amount + "\"}";
		    request.setEntity(new ByteArrayEntity(requestBody.getBytes("UTF8")));
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/tickets/" + ticket + "/payments"));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}
    }
    
    private void getreciept(String token, String ticket)
    {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/tickets/" + ticket + "/reciept"));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}
    }
    
    private void clockout(String token)
    {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpDelete request = new HttpDelete();
			request.addHeader("Accept", "application/json");
			request.addHeader("Content-type", "application/json");
			request.addHeader("Authorization", "Token token=\"" + token + "\"");
			request.setURI(new URI("http://192.168.32.128:3000/api/v1/sessions/clock"));
			client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		}				
		finally {}
    }
    
    private void logout(String token)
    {
		try {
	      HttpClient client = new DefaultHttpClient();
	      HttpDelete request = new HttpDelete();
	      request.addHeader("Accept", "application/json");
	      request.addHeader("Content-type", "application/json");
	      request.setURI(new URI("http://192.168.32.128:3000/api/v1/sessions"));
	      client.execute(request, new BasicResponseHandler());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {		
			e.printStackTrace();
		} catch (IOException e) {		
			e.printStackTrace();
		} 
		finally {}
    }  
};
}

