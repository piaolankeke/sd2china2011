package net.csdn.client.sd.utils;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class BaseAuthenicationHttpClient {
	private static final String TAG = "BaseAuthenicationHttpClient";
    public static final int REGISTRATION_TIMEOUT = 30 * 1000; 
    public static final String USER_AGENT = "API Client";

    public String doGet(String url)
    {
    	return doGet(url,"","");
    }
	public String doGet(String url,String name,String password)
	{
		String ret="";
		HttpGet httpRequest = new HttpGet(url);
		if(!name.equals("") || !password.equals(""))
		{
	        String userPassword = name+":"+password;    	
	        String encoding = null;
			try {
				encoding = Base64.encodeObject(userPassword).trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        httpRequest.addHeader("Authorization", "Basic " + encoding);
		}
        httpRequest.setHeader("User-agent", USER_AGENT);
		try
		{
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				ret = EntityUtils.toString(httpResponse.getEntity());
				
			}else {
				Log.e(TAG,"Fail: Code: " + httpResponse.getStatusLine().getStatusCode());
            }
			httpRequest.abort();

		}catch(IOException  ex)
		{
			Log.e(TAG,ex.toString());
		}
		return ret;
	}
	
	public String doPost(String url,String name,String password,ArrayList<NameValuePair> params) throws IOException 
	{
		String ret = "";
		HttpEntity entity = null;
		

		entity = new UrlEncodedFormEntity(params,"UTF-8");
		HttpPost httpRequest = new HttpPost(url);
		httpRequest.addHeader("Content-Type","application/x-www-form-urlencoded");
		if(!name.equals("") || !password.equals(""))
		{
	        String userPassword = name+":"+password;    	
	        String encoding = null;
			try {
				encoding = Base64.encodeObject(userPassword).trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        httpRequest.addHeader("Authorization", "Basic " + encoding);
		}		
        httpRequest.setHeader("User-agent", USER_AGENT);
		httpRequest.setEntity(entity);
		
		HttpClient client = new DefaultHttpClient();
		HttpParams http_params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(http_params,REGISTRATION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(http_params, REGISTRATION_TIMEOUT);
		ConnManagerParams.setTimeout(http_params, REGISTRATION_TIMEOUT);
		http_params.setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

		try {
			HttpResponse httpResponse = client.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ret = EntityUtils.toString(httpResponse.getEntity());
			} else {
				Log.e(TAG,"Fail: Code: " + httpResponse.getStatusLine().getStatusCode());
			}
			httpRequest.abort();
		} catch (IOException ex) {
			Log.e(TAG,ex.toString());
		}

		return ret;
	}
}
