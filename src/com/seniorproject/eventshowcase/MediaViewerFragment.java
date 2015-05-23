package com.seniorproject.eventshowcase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;

import android.R.menu;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MediaViewerFragment extends Fragment {

	public static Fragment newInstance(UUID eventId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static final String TAG = "MediaFragment";
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_DATE = 0;
	public static final String EXTRA_MEDIA_ID = "com.example.eventshowcase.media_id";
	private static final String DIALOG_IMAGE = "image";
	private static final String TAG_FAVORITE = "favorite";
	private int favorite = 0;
	private Media mMedia;
	public Button createButton;
	public TextView nameText;
	public MenuItem favText;
	public EditText dateText;
	public TextView timeText;
	public TextView picText;
	public TextView vidText;
	public TextView locText;
	
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private ImageButton mFavoriteButton;
	private TextView mFavoriteView;
	
	
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	private static final String  REGISTER_URL = "http://event-showcase.org/webservice/updatevotes.php";
	private static final String  REGISTER_FLAG_URL = "http://event-showcase.org/webservice/updateflags.php";
	public static String EXTRA_PHOTO_FILENAME = "http://event-showcase.org/webservice/uploadedimages/";
	
	String name;
	String date;
	String image;
	String vote;
	String sFavorite;
	String media_id;
	String flag;
	MenuItem item_u;
	ImageLoader imageLoader = new ImageLoader(getActivity());
	
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_media_view, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityIntent(getActivity()) != null){
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		Intent i = getActivity().getIntent();
		// Get the result of rank
		name = i.getStringExtra("title");
		// Get the result of country
		date = i.getStringExtra("date");
		// Get the result of flag
		image = i.getStringExtra("image");
		vote = i.getStringExtra("vote");
		sFavorite = i.getStringExtra("favorite");
		media_id = i.getStringExtra("media_id");
		flag = i.getStringExtra("flag");
		
		nameText = (TextView)v.findViewById(R.id.titleText_view);
		nameText.setText(name);
		
		
		
		
		
		mPhotoView = (ImageView)v.findViewById(R.id.media_imageView);
		
		imageLoader.DisplayImage(image,mPhotoView);
		
		
		
		return v;
	}
	// Takes information and sends to database
		class UpdateVotes extends AsyncTask<String, String, String> {
		    
		    /**
		     * Before starting background thread Show Progress Dialog
		     * */
		    boolean failure = false;
		    
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(getActivity());
		        pDialog.setMessage("Updating Votes...");
		        pDialog.setIndeterminate(false);
		        pDialog.setCancelable(true);
		        pDialog.show();
		    }
		    
		    @Override
		    protected String doInBackground(String... args) {
		        // TODO Auto-generated method stub
		        // Check for success tag
		        int success;
		        try{
		        	
					int favorite = Integer.parseInt(vote);
					favorite++;
					sFavorite = new Integer(favorite).toString();
					 
					//get media_id from MediaListFragment
					List<NameValuePair> params = new ArrayList<NameValuePair>();
		              params.add(new BasicNameValuePair("vote", sFavorite));
		              params.add(new BasicNameValuePair("id", media_id));
		              //Log.i("media id",media_id);
		              Log.i("vote",sFavorite);
		              
		            	  JSONObject json = jsonParser.makeHttpRequest(
		 	                     REGISTER_URL, "POST", params);
		            
		            // full json response
		            Log.d("Login attempt", json.toString());
		            
		            // json success element
		            success = json.getInt(TAG_SUCCESS);
		            if (success == 1) {
		               	Log.d("Vote Updated!", json.toString());
		               	
		               	//finish();
		               	return json.getString(TAG_MESSAGE);
		            }else{
		               	Log.d("Media Creation Failure!", json.getString(TAG_MESSAGE));
		               	return json.getString(TAG_MESSAGE);
		                
		            }
		        } catch (JSONException e) {
		            e.printStackTrace();
		        }
		        
		        return null;
		        
		    }
		    /**
		     * After completing background task Dismiss the progress dialog
		     * **/
		    protected void onPostExecute(String file_url) {
		        // dismiss the dialog once product deleted
		        pDialog.dismiss();
		        if (file_url != null){
		            Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
		        }
		    }
		}
		
		
		
		
		/// Update Flag #
		
		// Takes information and sends to database
				class UpdateFlag extends AsyncTask<String, String, String> {
				    
				    /**
				     * Before starting background thread Show Progress Dialog
				     * */
				    boolean failure = false;
				    
				    @Override
				    protected void onPreExecute() {
				        super.onPreExecute();
				        pDialog = new ProgressDialog(getActivity());
				        pDialog.setMessage("Sending Report...");
				        pDialog.setIndeterminate(false);
				        pDialog.setCancelable(true);
				        pDialog.show();
				    }
				    
				    @Override
				    protected String doInBackground(String... args) {
				        // TODO Auto-generated method stub
				        // Check for success tag
				        int success;
				        try{
				        	
							int flagInt = Integer.parseInt(flag);
							flagInt++;
							 String sFlag = new Integer(flagInt).toString();
							 
							//get media_id from MediaListFragment
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							Log.i("Media Id",media_id);
				              params.add(new BasicNameValuePair("flag", sFlag));
				              params.add(new BasicNameValuePair("id", media_id));
				              //Log.i("media id",media_id);
				              Log.i("flag",sFlag);
				              
				            	  JSONObject json = jsonParser.makeHttpRequest(
				 	                     REGISTER_FLAG_URL, "POST", params);
				            
				            // full json response
				            Log.d("Login attempt", json.toString());
				            
				            // json success element
				            success = json.getInt(TAG_SUCCESS);
				            if (success == 1) {
				               	Log.d("Flag Updated!", json.toString());
				               	
				               	//finish();
				               	return json.getString(TAG_MESSAGE);
				            }else{
				               	Log.d("Flag Update Failure!", json.getString(TAG_MESSAGE));
				               	return json.getString(TAG_MESSAGE);
				                
				            }
				        } catch (JSONException e) {
				            e.printStackTrace();
				        }
				        
				        return null;
				        
				    }
				    /**
				     * After completing background task Dismiss the progress dialog
				     * **/
				    protected void onPostExecute(String file_url) {
				        // dismiss the dialog once product deleted
				        pDialog.dismiss();
				        if (file_url != null){
				            Toast.makeText(getActivity(), file_url, Toast.LENGTH_LONG).show();
				        }
				    }
				}
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// TODO Auto-generated method stub
			super.onCreateOptionsMenu(menu, inflater);
			
			inflater.inflate(R.menu.fragment_media_view, menu);
			
			 item_u = menu.findItem(R.id.menu_item_upvote_num);
			item_u.setTitle("Votes: "+ vote);
			
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
			case android.R.id.home:
			
				if(NavUtils.getParentActivityIntent(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
				
			case R.id.menu_item_upvote:
				new UpdateVotes().execute();
			item_u.setTitle("Votes: "+vote);
			item.setEnabled(false);
			item.setIcon(R.drawable.disabled_arrow);
				return true;
			case R.id.menu_item_flag:
				String s = "Do you want to flag this item as inappropriate?";
				new AlertDialog.Builder(getActivity())
			    .setTitle("Report Image")
			    .setMessage(s)
			    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			        	new UpdateFlag().execute();
			        }
			     })
			    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        	
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();
			default:
			return super.onOptionsItemSelected(item);
		}

}
}

