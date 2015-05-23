package com.seniorproject.eventshowcase;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;



public class SlideshowFragment extends Fragment {
	
	
	private ProgressDialog pDialog;
	public static final String TAG_SUCCESS = "success";
    static final String TAG_TITLE = "medianame";
    static final String TAG_DATE = "date";
   public static final String TAG_MEDIA = "media";
   public static final String TAG_EVENT_ID = "event_id";
   private static final String TAG_MEDIA_ID = "media_id";
   public static final String TAG_FILE = "filename";
   public static final String TAG_VOTE = "vote";
	
	private static final String  READ_MEDIA_URL = "http://event-showcase.org/webservice/media.php";
	
	//An array of all of our media
    private JSONArray mMedia = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mMediaList;
     public ImageLoader imageloader = new ImageLoader(getActivity());
    
	
	ImageView finalImageView;
	ViewPager mViewPager;
	SlideShowPagerAdapter mSlideShowPagerAdapter;
	
	
	 ArrayList<String> list = new ArrayList<String>();
	private String event_id;
	private String photocount;
	private int photocountint;
	 
	 public void setList(ArrayList<String> list){
	      this.list=list;
	   }
	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getActivity().getIntent();
		event_id = i.getStringExtra("id");
		photocount = i.getStringExtra("photocount");
		photocountint = Integer.parseInt(photocount);
		
		
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//loading the comments via AsyncTask
		new LoadMedia().execute();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		Log.i("working",((Integer)list.size()).toString());
		
		
		View v = inflater.inflate(R.layout.fragment_image_slideshow, parent, false);
	       ImageView finalImageView = (ImageView) v.findViewById(R.id.slideshow_view);
	       
	       /*mViewPager = (ViewPager) v.findViewById(R.id.pager);      
	        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getActivity());
	        mViewPager.setAdapter(mSlideShowPagerAdapter);
	        */
		
		
		
		return v;
	}
	
	public ArrayList<String> updateJSONdata() {
   	 // Instantiate the arraylist to contain all the JSON data.
   	// we are going to use a bunch of key-value pairs, referring
   	// to the json element name, and the content, for example,
   	// message it the tag, and "I'm awesome" as the content..
   	
       mMediaList = new ArrayList<HashMap<String, String>>();
       
       // Bro, it's time to power up the J parser 
       JSONParser jParser = new JSONParser();
       // Feed the beast our comments url, and it spits us
       //back a JSON object.  Boo-yeah Jerome.
       JSONObject json = jParser.getJSONFromUrl(READ_MEDIA_URL);

       //when parsing JSON stuff, we should probably
       //try to catch any exceptions:
       try {
           
       	//I know I said we would check if "Posts were Avail." (success==1)
       	//before we tried to read the individual posts, but I lied...
       	//mComments will tell us how many "posts" or comments are
       	//available
           mMedia = json.getJSONArray(TAG_MEDIA);

           // looping through all posts according to the json object returned
          
           for (int i = 0; i < mMedia.length(); i++) {
               JSONObject c = mMedia.getJSONObject(i);

               //gets the content of each tag
               String file = c.getString(TAG_FILE);
               String vote = c.getString(TAG_VOTE);
               String current_event_id = c.getString(TAG_EVENT_ID);
               Log.i("file", file);
               Log.i("current event ID", current_event_id);
               Log.i("event_id",event_id);
               
               

               // creating new HashMap
               if (current_event_id.equals(event_id) && photocountint >0){
               list.add(file);
               photocountint--;
               }
               

              
           }
           if (list.isEmpty()){
        	   Log.e("List","is empty!!");
           }

       } catch (JSONException e) {
           e.printStackTrace();
           Log.e(TAG_MEDIA, "No Media found");
       }
	return list;
   }
	
	 private void setImage() {
		 finalImageView = (ImageView) getView().findViewById(R.id.slideshow_view);
		 new Runnable() {
			
 		    int currentIndex = 0;
 		    int updateInterval = 3000; //=one second
 		   
 		    @Override
 		    public void run() {
 		    	String item= "";
 		        currentIndex += 1;
 		        if(currentIndex == list.size()){
 		            currentIndex = 0;
 		        }
 		        if (list.size()>0){
 		        item = list.get(currentIndex); 
 		        Log.i("List item", item);
 		       
 		       
 		       imageloader.DisplayImage(item, finalImageView);          

 		        finalImageView.postDelayed(this, updateInterval);
 		        }
 		    }
 		}.run();
	    			
	    }
	
	
	public class LoadMedia extends AsyncTask<ArrayList<String>, ArrayList<String>, ArrayList<String>> {
		ArrayList<String> list;
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading Events...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        
    	@Override
		protected ArrayList<String> doInBackground(ArrayList<String>... params) {
			// TODO Auto-generated method stub
    	list = updateJSONdata();
			return list;
		}

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            super.onPostExecute(result);
            pDialog.dismiss();
          //we will develop this method in version 2
            setImage();
        }
		
    }
	
	
	class SlideShowPagerAdapter  extends PagerAdapter {
		 
	    Context mContext;
	    LayoutInflater mLayoutInflater;
	 
	    public SlideShowPagerAdapter(Context context) {
	        mContext = context;
	        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	 
	    @Override
	    public int getCount() {
	        return list.size();
	    }
	 
	    @Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((LinearLayout) object);
	    }
	 
	    @Override
	    public Object instantiateItem(ViewGroup container, int position) {
	        View itemView = mLayoutInflater.inflate(R.layout.fragment_image_slideshow, container, false);
	 
	        ImageView finalImageView = (ImageView) itemView.findViewById(R.id.slideshow_view);
	        String item = list.get(position);
	        imageloader.DisplayImage(item, finalImageView); 
	        
	 
	        container.addView(itemView);
	 
	        return itemView;
	    }
	 
	    @Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        container.removeView((LinearLayout) object);
	    }
	}


}
