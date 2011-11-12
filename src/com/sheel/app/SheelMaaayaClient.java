package com.sheel.app;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @author mohsen
 * Use at your own risk
 *
 */


//Responsible for sending HTTP requests and getting HTTP response
public abstract class SheelMaaayaClient {
	
	 final String SERVER  = "http://sheelmaaayaa.appspot.com";
     String PATH   = "";
     HttpResponse RESPONSE = null;
     Thread THREAD = null;
     String rspStr = "";
	
	public SheelMaaayaClient(){
		
	}
	
	
    
    public void runHttpRequest(String path){
	   
	        PATH = path;
		    rspStr = "";
		   
	        THREAD = new Thread(new Runnable()
	    
	    	{
	        @Override
	        public void run()
	        {
	            try
	            {
	                 DefaultHttpClient CLIENT = new DefaultHttpClient();   
	                 RESPONSE = CLIENT.execute(new HttpGet(SERVER + "/test"));
	                
		             if (RESPONSE.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		                {
		                 rspStr = EntityUtils.toString(RESPONSE.getEntity());	
		                }
	                
	            }
	            catch (Exception e)
	            {
	            	 rspStr = e.toString();	
	            }
	            doSomething();
	        }
	    });
	    
	    THREAD.start();
    }
    
    //To be implemented by according to the activity needs
    public abstract void doSomething();
    	
}

