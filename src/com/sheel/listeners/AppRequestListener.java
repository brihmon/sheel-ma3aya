package com.sheel.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

/**
 * Quick implementation for a commonly used interface
 * in facebook SDK with different requests to detect
 * different actions about it
 * 
 * @author Passant
 *
 */
public class AppRequestListener implements RequestListener{

	//_____________________ Constants ____________________________

	
	//_____________________ Instance parameters __________________
	
	/**
	 * Used for tracing purposes
	 */
	private String TAG_CLASS_PACKAGE;
	/**
	 * Used for tracing purposes
	 */
	private String METHOD_NAME;
	
	//_____________________ Constructor __________________________

	
	//_____________________ Different Actions _____________________
	
	public void onComplete(String response, Object state) {
		
	}// end onComplete

	public void onIOException(IOException e, Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onIOException");
		e.printStackTrace();					
	}// end onIOException

	public void onFileNotFoundException(FileNotFoundException e,Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onFileNotFoundException");
		e.printStackTrace();				
	}// end onFileNotFoundException

	public void onMalformedURLException(MalformedURLException e,
			Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onMalformedURLException");
		e.printStackTrace();				
	}// end onMalformedURLException

	public void onFacebookError(FacebookError e, Object state) {
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+":onFacebookError");
		e.printStackTrace();					
	}// end onFacebookError	
	
	/**
	 * Set values used for tracing purposes
	 * see @link {@link AppRequestListener#generateLogMessage(String)}
	 * @param classPackage
	 * 		className (packageName)
	 * @param methodName
	 * 		method name
	 */
	public void setTracingValues(String classPackage , String methodName){
		TAG_CLASS_PACKAGE = classPackage;
		METHOD_NAME = methodName;
	}// end setTracingValues
	
	/**
	 * Used to generate log messages with default format
	 * Message format is:<br>
	 * <ul>
	 * 		<li>Tag : className (packageName)</li>
	 * 		<li>Message : methodName: msg</li>
	 * </ul>
	 * @param msg
	 * 		message displayed in the log
	 */
	public void generateLogMessage(String msg){
		Log.e(TAG_CLASS_PACKAGE,METHOD_NAME+": "+msg);
	}// end generateLogMessage
}// end class
