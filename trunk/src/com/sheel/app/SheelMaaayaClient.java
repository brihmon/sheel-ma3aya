package com.sheel.app;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//Responsible for sending HTTP requests and getting HTTP response
public abstract class SheelMaaayaClient {
	
	 final String SERVER  = "http://sheelmaaayaa.appspot.com";
	 //final String SERVER  = "http://192.168.1.3:9004";
     String PATH   = "";
     HttpResponse RESPONSE = null;
     Thread THREAD = null;
     String rspStr = "";
	
	public SheelMaaayaClient(){
		
	}
	
	static String JSON = "";
	
	/**
	 * 
	 * @param path is the path to the service
	 * @param json : JSON object to be sent
	 */
	public void runHttpPost(String path,String json){
	 
	 
		PATH = path;
		JSON = json;
	    rspStr = "";
	    
        THREAD = new Thread(new Runnable()
    
    	{
    //    @Override
        public void run()
        {
            try
            {
                DefaultHttpClient CLIENT = new DefaultHttpClient();   
             	HttpPost httpost = new HttpPost();
             	httpost.setURI(new URI(SERVER+PATH));
            	httpost.setEntity(new StringEntity(JSON));
            	
             	httpost.setHeader("Accept", "text/javascript");
        		httpost.setHeader("Content-type", "text/javascript");

                 RESPONSE = CLIENT.execute(httpost);
                
	             if (RESPONSE.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	                {
	                 rspStr += EntityUtils.toString(RESPONSE.getEntity());
	                 //rspStr+=RESPONSE.getStatusLine().getStatusCode();
	                }
                
            }
            catch (Exception e)
            {
            	 rspStr = e.toString()+"- CLIENT ERROR";	
            }
            doSomething();
        }
    });
    
    THREAD.start();
		JSON = "";
		PATH = "";
		
	}
    
    public void runHttpRequest(String path){
	   
	        PATH = path;
		    rspStr = "";
		   
	        THREAD = new Thread(new Runnable()
	    
	    	{
//	        @Override
	        public void run()
	        {
	            try
	            {
	                 DefaultHttpClient CLIENT = new DefaultHttpClient();   
	                 
	                 RESPONSE = CLIENT.execute(new HttpGet(SERVER + PATH));
	                
		             if (RESPONSE.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
		                {
		                 rspStr = EntityUtils.toString(RESPONSE.getEntity());	
		                }
	                
	            }
	            catch (Exception e)
	            {
	            	 rspStr = e.toString()+"- CLIENT ERROR";	
	            }
	            doSomething();
	        }
	    });
	    
	    THREAD.start();
    }
    
    //To be implemented according to the activity needs
    public abstract void doSomething();
    	
}

