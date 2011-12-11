package com.sheel.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author maged
 * @Warning Use at your own risk
 *
 */


//Responsible for sending HTTP requests and getting HTTP response
public abstract class HTTPClient {
	
	 final String SERVER  = "http://sheelmaaayaa.appspot.com";
     String PATH   = "";
     HttpResponse RESPONSE = null;
     Thread THREAD = null;
     String rspStr = "";
     StringBuilder builder = null;
	
	public HTTPClient(){
		
	}
	
	
    
    public void runHttpRequest(String path){
	   
	        PATH = path;
		    rspStr = "";
		    builder = new StringBuilder();
		 
		   
	        THREAD = new Thread(new Runnable()
	    
	    	{
	  //      @Override
	        public void run()
	        {
	            try
	            { 
	                 DefaultHttpClient CLIENT = new DefaultHttpClient();   
	                 RESPONSE = CLIENT.execute(new HttpGet(SERVER + PATH));
	 
		             if (RESPONSE.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		              {
		            	
		            	 HttpEntity entity = RESPONSE.getEntity();
		           
		                 InputStream content = entity.getContent();
		                 
		                 BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		                 String line;
		                 while ((line = reader.readLine()) != null){
		                       builder.append(line);
		                       
		                } 
	            }}
	            catch (Exception e)
	            { e.toString(); }
	            
	            doSomething();
	        }
	    });
	    
	    THREAD.start();
    }
    
    //To be implemented by according to the activity needs
    public abstract void doSomething();
    	
}

