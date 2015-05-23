package com.seniorproject.eventshowcase;

 
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.seniorproject.eventshowcase.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;



	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	new CheckInternet().execute();
            	//Log.i("response code", String.valueOf(responseCode));
            	//if (getResponseCode() == 200){
                Intent i = new Intent(SplashScreen.this, EventListActivity.class);
                startActivity(i);
            	//}
 
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    //To be implemented
    public class CheckInternet extends AsyncTask<Void, Void, Boolean> {
    	public int responseCode = 0;
		public int getResponseCode() {
			return responseCode;
		}
		public void setResponseCode(int responseCode) {
			this.responseCode = responseCode;
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			URL url = null;
			try {
				url = new URL("http://www.google.com");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpURLConnection conn = null;
			try {
				conn = (HttpURLConnection) url.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
			Log.i("RESPONSE",String.valueOf(conn.getResponseCode()));
			responseCode = conn.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
    }
 
}
