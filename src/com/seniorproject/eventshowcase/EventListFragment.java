// TO DO:
//Select event_id from database and send to medialistfragment

package com.seniorproject.eventshowcase;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.location.Address;
import android.location.Geocoder;


public class EventListFragment extends ListFragment implements OnRefreshListener {
	
	private final String TAG = "EventListFragment";
	//private ArrayList<Event> mEvents;
	private boolean mSubtitleVisible;
	
	private boolean dontShow;
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	SwipeRefreshLayout swipeLayout;
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_TITLE = "eventname";
    private static final String TAG_EVENT = "events";
    private static final String TAG_EVENT_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_END_DATE = "enddate";
    private static final String TAG_ZIP = "postalcode";
    private static final String TAG_ZIP_SHOW ="showpostalcode";
    private static final String  TAG_PHOTO_COUNT="photocount";
    public static final String PREFS_NAME = "MyPrefsFile";
	
	private static final String  READ_EVENTS_URL = "http://event-showcase.org/webservice/event.php";
	
	
	//An array of all of our events
    private JSONArray mEvents = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mEventList;
	
    
    public void callToast(){
    	Toast.makeText(getActivity(), "Please make sure you are connected to the Internet", Toast.LENGTH_LONG).show();
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//allow use of options menu
		setHasOptionsMenu(true);
		
		
		
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		   boolean dialogShown = settings.getBoolean("dialogShown", false);

		   if (!dialogShown) {
		
		getActivity().setTitle(R.string.events_title);
		final SpannableString s = 
	               new SpannableString("This is a family friendly app. Therefore innapropriate or graphic submissions are prohibited. To see the full terms and conditions click here: https://event-showcase.org/termsandconditions");
	  Linkify.addLinks(s, Linkify.WEB_URLS);
		 new AlertDialog.Builder(getActivity())
	    .setTitle("Terms and Conditions")
	    .setMessage(s)
	    .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        }
	     })
	    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
		
		
		 SharedPreferences.Editor editor = settings.edit();
	     editor.putBoolean("dialogShown", true);
	     editor.commit();   
		   }
		   
		
			//setListAdapter(adapter);
			setRetainInstance(true);
			mSubtitleVisible = false;
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = super.onCreateView(inflater, container, savedInstanceState);
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible){
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		/*SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
		
	    swipeLayout.setOnRefreshListener(new OnRefreshListener() {

	        @Override
	        public void onRefresh() {
	            // TODO Auto-generated method stub
	        	new LoadEvents().execute();
	        }
	    });
	    */
        
		
	
	 
	 
	
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			registerForContextMenu(listView);
		} else {
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
			
				
				@Override
				public boolean onPrepareActionMode(
						android.view.ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(android.view.ActionMode mode) {
					//blank
				
				}
				@Override
				public boolean onCreateActionMode(android.view.ActionMode mode,
						Menu menu) {
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.event_list_item_context, menu);
					return true;
				}
				@Override
				public boolean onActionItemClicked(
						android.view.ActionMode mode, MenuItem item) {
					switch(item.getItemId()){
					case R.id.menu_item_delete_event:
						//EventAdapter adapter = (EventAdapter)getListAdapter();
						//EventLab eventLab = EventLab.get(getActivity());
						//for (int i = adapter.getCount() -1; i >=0; i--){
							//if (getListView().isItemChecked(i)) {
							//eventLab.deleteEvent(adapter.getItem(i));
						//}
					}
					mode.finish();
					//adapter.notifyDataSetChanged();
					return true;
				//default:
				//	return false;
				//}
				}
				
				@Override
				public void onItemCheckedStateChanged(
						android.view.ActionMode mode, int position, long id,
						boolean checked) {
					// TODO Auto-generated method stub
					
				}
			
		
		});
		
	}
	
		
		
		return v;
	}
	
    public boolean updateJSONdata() throws IOException {
    	 // Instantiate the arraylist to contain all the JSON data.
    	// we are going to use a bunch of key-value pairs, referring
    	// to the json element name, and the content, for example,
    	// message it the tag, and "I'm awesome" as the content..
    	
        mEventList = new ArrayList<HashMap<String, String>>();
        
        // Bro, it's time to power up the J parser 
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_EVENTS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {
            
        	//I know I said we would check if "Posts were Avail." (success==1)
        	//before we tried to read the individual posts, but I lied...
        	//mComments will tell us how many "posts" or comments are
        	//available
        	if(json!=null){
        	try{
        		
        			Log.i("Not", "null");
            mEvents = json.getJSONArray(TAG_EVENT);
        	}catch(JSONException e){
        		e.printStackTrace();
        		
        	}
        	

            // looping through all posts according to the json object returned
            for (int i = 0; i < mEvents.length(); i++) {
                JSONObject c = mEvents.getJSONObject(i);

                //gets the content of each tag
                String title = c.getString(TAG_TITLE);
                String date = c.getString(TAG_DATE);
                String event_id = c.getString(TAG_EVENT_ID);
                String postalcode = c.getString(TAG_ZIP);
                String enddate = c.getString(TAG_END_DATE);
                String photocount = c.getString(TAG_PHOTO_COUNT);
                
                 
                
                

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_DATE, date);
                map.put(TAG_END_DATE, enddate);
                map.put(TAG_EVENT_ID, event_id);
                map.put(TAG_ZIP_SHOW,"ZIP Code: " +postalcode);
                map.put(TAG_ZIP, postalcode);
                map.put(TAG_PHOTO_COUNT, photocount);
                
             
                // adding HashList to ArrayList
                mEventList.add(map);
                
                //annndddd, our JSON data is up to date same with our array list
            }
        	}
    		
    		else{
    			return false;
    			
    		}

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG_EVENT, "No Events found");
        }
        return true;
    }

    /**
     * Inserts the parsed data into our listview
     */
    private void updateList() {
    	// For a ListActivity we need to set the List Adapter, and in order to do
    			//that, we need to create a ListAdapter.  This SimpleAdapter,
    			//will utilize our updated Hashmapped ArrayList, 
    			//use our single_post xml template for each item in our list,
    			//and place the appropriate info from the list to the
    			//correct GUI id.  Order is important here.
    			ListAdapter adapter = new SimpleAdapter(getActivity(), mEventList,
    					R.layout.list_item_event, new String[] {  TAG_TITLE,
    				TAG_DATE,TAG_END_DATE,  TAG_ZIP_SHOW }, new int[] { R.id.event_list_item_titleTextView, R.id.event_list_item_dateTextView,R.id.event_list_item_enddateTextView,R.id.event_list_item_zipView
    							 });

    			// I shouldn't have to comment on this one:
    			setListAdapter(adapter);
    			
    			
    			// Optional: when the user clicks a list item we 
    			//could do something.  However, we will choose
    			//to do nothing...
    			ListView lv = getListView();	
    			
    			  

    			lv.setOnItemClickListener(new OnItemClickListener() {

    				

					@SuppressWarnings("deprecation")
					@Override
    				public void onItemClick(AdapterView<?> parent, View view,
    						int position, long id) {

    					// This method is triggered if an item is click within our
    					// list. For our example we won't be using this, but
    					// it is useful to know in real life applications.
    					//HashMap<String, String> blah = mEventList.get(position);
    					String event_id = mEventList.get(position).get(TAG_EVENT_ID);
    					String title = mEventList.get(position).get(TAG_TITLE);
    					String postalcode = mEventList.get(position).get(TAG_ZIP);
    					String enddate = mEventList.get(position).get(TAG_END_DATE);
    					String startdate = mEventList.get(position).get(TAG_DATE);
    					String photocount = mEventList.get(position).get(TAG_PHOTO_COUNT);
    					Date date = null;
    					Date sDate = null;
    					
    					SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    					try {
							 date = sdf.parse(enddate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					
    					
    					String dayInMonth = (String) android.text.format.DateFormat.format("dd", date);
    					String month = (String) android.text.format.DateFormat.format("MM", date);
    					String year = (String) android.text.format.DateFormat.format("yyyy", date);
    					int dayInMonthint = Integer.parseInt(dayInMonth);
    					int monthint = Integer.parseInt(month);
    					int yearint = Integer.parseInt(year);
    					
    					try {
							 sDate = sdf.parse(startdate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    					
    					String sdayInMonth = (String) android.text.format.DateFormat.format("dd", sDate);
    					String smonth = (String) android.text.format.DateFormat.format("MM", sDate);
    					String syear = (String) android.text.format.DateFormat.format("yyyy", sDate);
    					int sdayInMonthint = Integer.parseInt(sdayInMonth);
    					int smonthint = Integer.parseInt(smonth);
    					int syearint = Integer.parseInt(syear);
    					
    					
    					Calendar c = Calendar.getInstance();
    					
    					c.set(Calendar.HOUR, 0);
    					c.set(Calendar.MINUTE, 0);
    					c.set(Calendar.SECOND, 0);

    					// and get that as a Date
    					Date today = c.getTime();
    					Log.i("current date",c.getTime().toString());
    					
    					c.set(Calendar.YEAR, syearint);
    					c.set(Calendar.MONTH, smonthint-1);
    					c.set(Calendar.DAY_OF_MONTH, sdayInMonthint);
    					Date startDateSpecified = c.getTime();
    					Log.i("start date",c.getTime().toString());
    					
    					c.set(Calendar.YEAR, yearint);
    					c.set(Calendar.MONTH, monthint-1);
    					c.set(Calendar.DAY_OF_MONTH, dayInMonthint);
    					Date endDateSpecified = c.getTime();
    					Log.i("end date",c.getTime().toString());
    		   			
    					Intent i = null;
    					Log.i("End Date", enddate);
    					Log.i("Event Day",String.valueOf(dayInMonth));
    					Log.i("Event Month",String.valueOf(month));
    					Log.i("Event Year",String.valueOf(year));
    					Log.i("Event Start Day",String.valueOf(sdayInMonthint));
    					Log.i("Event Start Month",String.valueOf(smonthint));
    					Log.i("Event Start Year",String.valueOf(syearint));
    					
    					
    					if(startDateSpecified.after(today)){
    						Toast.makeText(getActivity(), "The event has not begun yet!", Toast.LENGTH_LONG).show();
    						dontShow = true;
    					}
    				
    					
    					
    					else if(endDateSpecified.equals(today) ||endDateSpecified.after(today)){
    						 i = new Intent(getActivity(), MediaListActivity.class);
    						 i.putExtra("id", event_id);
    	    				i.putExtra("zip",postalcode);
    	    				i.putExtra("name", title);
        					startActivity(i);
    						
   						

   					}
    					
    					
    					else{
    						
    						 i = new Intent(getActivity(), SlideshowActivity.class);
    	   						i.putExtra("id", event_id);
    	   						i.putExtra("photocount",photocount);
    	   						i.putExtra("name", title);
    	    					startActivity(i);
    					}
    					
    					
    				}
    			});
    }
	
	
    
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.event_list_item_context, menu);
	}
	
	@Override 
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		//EventAdapter adapter = (EventAdapter)getListAdapter();
		//Event event = adapter.getItem(position);
		
		switch(item.getItemId())  {
		case R.id.menu_item_delete_event:
			//EventLab.get(getActivity()).deleteEvent(event);
			//adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	//Set add event
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_event_list, menu);
		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
			switch(item.getItemId()) {
			case R.id.menu_item_new_event:
				
				Intent i = new Intent(getActivity(),EventActivity.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
				
			}
			
			}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id){
		
		HashMap<String, String> e = mEventList.get(position);
		//Log.d(TAG,e.getTitle() + "was clicked");
		//Start EventPagerActivity
		Intent i = new Intent(getActivity(), EventPagerActivity.class);
		//i.putExtra(EventFragment.EXTRA_EVENT_ID, e.getId());
		startActivity(i);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//((EventAdapter)getListAdapter()).notifyDataSetChanged();
		//loading the comments via AsyncTask
    	new LoadEvents().execute();
	}
	

	
	 public class LoadEvents extends AsyncTask<Void, Void, Boolean> {
		 	boolean works;
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
	        protected Boolean doInBackground(Void... arg0) {
	        	//we will develop this method in version 2
	            try {
					 works =updateJSONdata();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            return null;

	        }


	        @Override
	        protected void onPostExecute(Boolean result) {
	            super.onPostExecute(result);
	            pDialog.dismiss();
	          //we will develop this method in version 2
	            updateList();
	            if (works ==false){
					callToast();
				}
	        }
	    }



	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new LoadEvents().execute();
	}

	
}
