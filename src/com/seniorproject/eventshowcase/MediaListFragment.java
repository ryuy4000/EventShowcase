//TO DO receive event_id. If media.event_id = event_id

package com.seniorproject.eventshowcase;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;





import android.annotation.TargetApi;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;



public class MediaListFragment extends ListFragment {
	private final String TAG = "MediaListFragment";
	public static final String EXTRA_EVENT_ID = "com.example.eventshowcase.event_id";
	//private ArrayList<Media> mMedia;
	private Media sMedia;
	private Event mEvent;
	private boolean mSubtitleVisible;
	public static final String EXTRA_MEDIA_ID = "com.example.eventshowcase.media_id";
	
	
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	
	JSONObject jsonobject;
	JSONArray jsonarray;
	ListView listview;
	ListViewAdapter adapter;
	ArrayList<HashMap<String, String>> arraylist;
	
	public static final String TAG_SUCCESS = "success";
     static final String TAG_TITLE = "medianame";
     static final String TAG_DATE = "date";
    public static final String TAG_MEDIA = "media";
    public static final String TAG_EVENT_ID = "event_id";
    public static final String TAG_MEDIA_ID = "media_id";
    public static final String TAG_FILE = "filename";
    public static final String TAG_VOTE = "vote";
    public static final String TAG_FLAG = "flag";
	
	private static final String  READ_MEDIA_URL = "http://event-showcase.org/webservice/media.php";
	private static final String DELETE_MEDIA_URL = "http://event-showcase.org/webservice/delete_media.php";
	public static String EXTRA_PHOTO_FILENAME = "http://event-showcase.org/webservice/uploadedimages/";
	
	//An array of all of our media
    private JSONArray mMedia = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mMediaList;
    public ImageLoader imageloader;
    String event_id;
    String postalcode;
	private String event_name;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//UUID eventId = (UUID)getArguments().getSerializable(EXTRA_EVENT_ID);
		//mEvent = EventLab.get(getActivity()).getEvent(eventId);
		//allow use of options menu
		setHasOptionsMenu(true);
		
		getActivity().setTitle(R.string.media_title);
		// = MediaLab.get(getActivity()).getMedia();
		
		//MediaAdapter adapter = new MediaAdapter(mMedia);
		
			//setListAdapter(adapter);
			setRetainInstance(true);
			mSubtitleVisible = false;
			Intent i = getActivity().getIntent();
			// Get notes event_id
			postalcode = i.getStringExtra("zip");
			event_id = i.getStringExtra("id");
			event_name = i.getStringExtra("name");
	}
	
	//Added CODE
	 /**
     * Retrieves json data of comments
     */
    public void updateJSONdata() {
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
                String title = c.getString(TAG_TITLE);
                String date = c.getString(TAG_DATE);
                String file = c.getString(TAG_FILE);
                String id = c.getString(TAG_MEDIA_ID);
                
                

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_TITLE, title);
                map.put(TAG_DATE, date);
                map.put(TAG_FILE, file);
                map.put(TAG_MEDIA_ID, id);
                ;
             
                // adding HashList to ArrayList
                mMediaList.add(map);
                
                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG_MEDIA, "No Events found");
        }
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
    			ListAdapter adapter = new SimpleAdapter(getActivity(), mMediaList,
    					R.layout.list_item_media, new String[] { TAG_TITLE, TAG_DATE
    							 }, new int[] { R.id.media_list_item_titleTextView, R.id.media_list_item_dateTextView
    							 });
    			
    			//add code to select photo
    			//ImageView mediaImageView = (ImageView).findViewById(R.id.medialist_imageView);
    				//imageloader=new ImageLoader(getActivity().getApplicationContext());
    				//imageloader.DisplayImage(EXTRA_PHOTO_FILENAME+ TAG_FILE,R.id.media_imageView);  
    			
    			
    			// I shouldn't have to comment on this one:
    			setListAdapter(adapter);
    			
    			// Optional: when the user clicks a list item we 
    			//could do something.  However, we will choose
    			//to do nothing...
    			ListView lv = getListView();
    			
    			
    			lv.setOnItemClickListener(new OnItemClickListener() {

    				@Override
    				public void onItemClick(AdapterView<?> parent, View view,
    						int position, long id) {

    					// This method is triggered if an item is click within our
    					// list. For our example we won't be using this, but
    					// it is useful to know in real life applications.
    					//DUMMY SEND, USE MEDIA PAGER ACTIVITY
    					String media_id = mMediaList.get(position).get(TAG_MEDIA_ID);
    					Intent i = new Intent(getActivity(), MediaActivity.class);
    					i.putExtra("media_id", media_id);
    					startActivity(i);
    				}
    			});
    }
	
	
	
	
	
//---------------------	
	
	public static MediaListFragment newInstance(UUID eventId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_EVENT_ID, eventId);
	
		
		MediaListFragment fragment = new MediaListFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityIntent(getActivity()) !=null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		// TODO Auto-generated method stub
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		
		
		
		
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
					inflater.inflate(R.menu.media_list_item_context, menu);
					return true;
				}
				@Override
				public boolean onActionItemClicked(
						android.view.ActionMode mode, MenuItem item) {
					switch(item.getItemId()){
					case R.id.menu_item_delete_media:
						
						//add code to delete media from server
						
						
						for (int i = adapter.getCount() -1; i >=0; i--){
							//add code to delete
					}
					mode.finish();
					adapter.notifyDataSetChanged();
					return true;
				default:
					return false;
				}
				
				}
				
				@Override
				public void onItemCheckedStateChanged(
						android.view.ActionMode mode, int position, long id,
						boolean checked) {
					// TODO Auto-generated method stub
					
				}
			
		
		});
		
	}
	
		//return super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
			switch(item.getItemId()) {
			case R.id.menu_item_new_media:
				
				Intent i = new Intent(getActivity(),MediaActivity.class);
				i.putExtra("id", event_id);
				i.putExtra("zip", postalcode);
				startActivity(i);
				return true;
			
			case android.R.id.home:
				if(NavUtils.getParentActivityIntent(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
				default:
					return super.onOptionsItemSelected(item);
			}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		getActivity().getMenuInflater().inflate(R.menu.media_list_item_context, menu);
	}
	
	@Override 
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		//MediaAdapter adapter = (MediaAdapter)getListAdapter();
		//Media media = adapter.getItem(position);
		
		switch(item.getItemId())  {
		case R.id.menu_item_delete_media:
			//MediaLab.get(getActivity()).deleteMedia(media);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	//Set add event
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// TODO Auto-generated method stub
			super.onCreateOptionsMenu(menu, inflater);
			inflater.inflate(R.menu.fragment_media_list, menu);
			getActivity().setTitle("Event: "+ event_name);
			
		}
		
	

		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//((EventAdapter)getListAdapter()).notifyDataSetChanged();
			//loading the comments via AsyncTask
	    	new LoadMedia().execute();
		}
		
		 public class LoadMedia extends AsyncTask<Void, Void, Boolean> {

		    	@Override
				protected void onPreExecute() {
					super.onPreExecute();
					pDialog = new ProgressDialog(getActivity());
					pDialog.setMessage("Loading Media...");
					pDialog.setIndeterminate(false);
					pDialog.setCancelable(true);
					pDialog.show();
				}
		        @Override
		        protected Boolean doInBackground(Void... arg0) {
		        	//we will develop this method in version 2
		            //updateJSONdata();
		        	// Create an array
		        
					arraylist = new ArrayList<HashMap<String, String>>();
					// Retrieve JSON Objects from the given URL address
					jsonobject = JSONfunctions
							.getJSONfromURL("http://event-showcase.org/webservice/media.php");
		        	
		 
					try {
						// Locate the array name in JSON
						jsonarray = jsonobject.getJSONArray("media");
		 
						for (int i = 0; i < jsonarray.length(); i++) {
							
							//if media.event_id == event_id passed from eventListFragment DO:
							
							HashMap<String, String> map = new HashMap<String, String>();
							jsonobject = jsonarray.getJSONObject(i);
							// Retrive JSON Objects
							if (event_id.equals(jsonobject.getString("event_id"))){
								Log.i(event_id," matches");
							map.put("medianame", jsonobject.getString("medianame"));
							map.put("date", jsonobject.getString("date"));
							map.put("filename", jsonobject.getString("filename"));
							map.put("vote", jsonobject.getString("vote"));
							map.put("media_id",jsonobject.getString("media_id"));
							map.put("flag", jsonobject.getString("flag"));
						
							// Set the JSON Objects into the array
							arraylist.add(map);
							}
							
							
						}
					} catch (JSONException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();
					}
		            return null;

		        }


		        @Override
		        protected void onPostExecute(Boolean result) {
		            super.onPostExecute(result);
		            pDialog.dismiss();
		          //we will develop this method in version 2
		            //updateList();
		         // Locate the listview in listview_main.xml
		            
					// Pass the results into ListViewAdapter.java
					adapter = new ListViewAdapter(getActivity(), arraylist);
				
					// Set the adapter to the ListView
					setListAdapter(adapter);
					
					
		        }
		    }
		 
		
}

