package com.seniorproject.eventshowcase;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MediaFragment extends Fragment {
	private static final String TAG = "MediaFragment";
	private static final int REQUEST_PHOTO = 1;
	private static final int REQUEST_DATE = 0;
	public static final String EXTRA_MEDIA_ID = "com.example.eventshowcase.media_id";
	private static final String DIALOG_IMAGE = "image";
	private int favorite = 0;
	private Media mMedia;
	public Button createButton;
	public EditText nameText;
	public EditText dateText;
	public TextView timeText;
	public TextView picText;
	public TextView locCaution;
	
	
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private ImageButton mFavoriteButton;
	private TextView mFavoriteView;
	private Button mRegisterButton;
	
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	private static final String  REGISTER_URL = "http://event-showcase.org/webservice/registermedia.php";
	public static String EXTRA_PHOTO_FILENAME = "http://event-showcase.org/webservice/uploadedimages/";
	String name;
	String date;
	String image;
	String file = "";
	String event_id ="";
	String eventpostalcode = "";
	Location location;
	ImageLoader imageLoader = new ImageLoader(getActivity());
	LocationManager locationManager;
	GPSTracker gps;
	
	
	double latitude = 0;
	double longitude = 0;
	String postalCode = null;
	
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) getActivity());
		
		Intent i = getActivity().getIntent();
		// Get notes event_id
		event_id = i.getStringExtra("id");
		eventpostalcode = i.getStringExtra("zip");
		
		
		
		
		

		
		setHasOptionsMenu(true);
		
	}
	
	
	
	//View Creator: Sets .xml files to functionality
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_media, parent, false);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityIntent(getActivity()) !=null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		gps =new GPSTracker(getActivity());
		Geocoder geocoder;
		 List<Address> addresses = null;
	
	if(gps.canGetLocation()) {
		 latitude = gps.getLatitude();
		 longitude = gps.getLongitude();
		 
		 
		 
		 geocoder = new Geocoder(getActivity(), Locale.getDefault());

		 try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if(addresses!=null){
		if (addresses.size() > 0){
		 postalCode = addresses.get(0).getPostalCode();
		}
		 }
		 //Log.i("Postal Code",postalCode);
		 Log.i("Event postal code", eventpostalcode);
	} else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }
	
	
		
		nameText = (EditText)v.findViewById(R.id.media_title);
		
		nameText.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence e, int start, int before, int count){
				//mMedia.setTitle(e.toString());
			}

			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPhotoButton = (ImageButton)v.findViewById(R.id.media_image_button);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getActivity(),CameraActivity.class);
				startActivityForResult(i,REQUEST_PHOTO);
				
			}
		});
		
		//photo view
		mPhotoView = (ImageView)v.findViewById(R.id.media_imageView);
		imageLoader.DisplayImage(image,mPhotoView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v){
				Log.i(TAG,"image view was clicked");
				Photo p = mMedia.getPhoto();
				if (p == null){
					Log.i(TAG,"no image");
					return;
				}
				
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = CameraActivity.EXTRA_PHOTO_FILENAME;
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
			
		});
		
		
		//If camera is not available, disable camera
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || (Build.VERSION.SDK_INT >=Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
		if (!hasACamera) {
			mPhotoButton.setEnabled(false);
		}
		//Send information to mySQL database if ZIP Code and date match
		mRegisterButton = (Button)v.findViewById(R.id.submitButton);
		mRegisterButton.setOnClickListener(new View.OnClickListener() {
			//Add Code to restrict Location and Date of Post
				/*
				 * plan: get zip code, cross reference with event zip code
				 * 		check date range, possibly just before date
				 * 		if (location is out of range)
				 * 				Toast
				 * 			etc.
				 */
		
			//
			@SuppressLint("ShowToast") @Override
			public void onClick(View v) {
				
				if ( postalCode == null || postalCode.equals(eventpostalcode)){
				new CreateMedia().execute();
				} else{
					Toast.makeText(getActivity(), postalCode + " does not match event location!", Toast.LENGTH_LONG).show();
				}
				
				
			}
		});
		
		locCaution = (TextView)v.findViewById(R.id.locationCaution);
		if (postalCode != null && eventpostalcode.equals(postalCode)!=true){
			locCaution.setVisibility(View.VISIBLE);
		}
		
		return v;
	}
	
	
	
	// Takes information and sends to database
	class CreateMedia extends AsyncTask<String, String, String> {
	    
	    /**
	     * Before starting background thread Show Progress Dialog
	     * */
	    boolean failure = false;
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        pDialog = new ProgressDialog(getActivity());
	        pDialog.setMessage("Creating Media...");
	        pDialog.setIndeterminate(false);
	        pDialog.setCancelable(true);
	        pDialog.show();
	    }
	    
	    @Override
	    protected String doInBackground(String... args) {
	        // TODO Auto-generated method stub
	        // Check for success tag
	        int success;
	        String medianame = nameText.getText().toString();
	        String filename = file;
	        String id = event_id;
	        
	        Calendar c = Calendar.getInstance();
	        System.out.println("Current time => " + c.getTime());

	        SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy",Locale.US);
	        String mediadate = df.format(c.getTime());
	        System.out.println(mediadate);
			
	        try {
	            // Building Parameters
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("medianame", medianame));
	            params.add(new BasicNameValuePair("filename", filename));
	            params.add(new BasicNameValuePair("event_id", id));
	            params.add(new BasicNameValuePair("date",mediadate));
	            
	            
	            Log.d("request!", "starting");
	            
	            //Posting user data to script
	            JSONObject json = jsonParser.makeHttpRequest(
	                                                         REGISTER_URL, "POST", params);
	            
	            // full json response
	            Log.d("Login attempt", json.toString());
	            
	            // json success element
	            success = json.getInt(TAG_SUCCESS);
	            if (success == 1) {
	               	Log.d("Media Created!", json.toString());
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
	
	
	

		@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Context context = getActivity(); // "this" is the Activity used by the client
		
	}



		public static MediaFragment newInstance(UUID mediaId) {
			Bundle args = new Bundle();
			args.putSerializable(EXTRA_MEDIA_ID, mediaId);
			
			MediaFragment fragment = new MediaFragment();
			fragment.setArguments(args);
			
			return fragment;
		}
		// Go back button
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// TODO Auto-generated method stub
			switch(item.getItemId()) {
			case android.R.id.home:
				if(NavUtils.getParentActivityIntent(getActivity()) != null){
					NavUtils.navigateUpFromSameTask(getActivity());
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
			
		}
		
		//When returning from camera, save image
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			Log.i(TAG, "OnActivityResult called");
			//if (requestCode != Activity.RESULT_OK) return;
			//fix!!!
			Log.i(TAG, "Result ok");
			
			if (requestCode == REQUEST_DATE) {
				//Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			} else if (requestCode == REQUEST_PHOTO) {
				// Create new photo object and attach it to the event
				
				 file = data
						.getStringExtra(CameraActivity.EXTRA_PHOTO_FILENAME);
				if (file != null) {
					Log.i(TAG, "filename: " + file);
					
					imageLoader.DisplayImage(file, mPhotoView);
					
				}
				else{
					Log.i(TAG, "filename is null");
				}
			}
		}
		
		@Override
		public void onPause(){
			super.onPause();
			//MediaLab.get(getActivity()).saveMedia();
		}

		public int getFavorite() {
			return favorite;
		}

		public void setFavorite(int favorite) {
			this.favorite = favorite;
		}
		
		
		
		
		

		
		

}
