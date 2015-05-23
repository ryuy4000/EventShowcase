package com.seniorproject.eventshowcase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.eventshowcase.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EventFragment extends Fragment {
	
	private static final String TAG = "CrimeFragment";
	private static final int REQUEST_DATE = 0;
	private static final int REQUEST_END_DATE = 1;
	private static final int REQUEST_CONTACT = 2;
	public static final String EXTRA_EVENT_ID = "com.example.eventshowcase.event_id";
	private static final String DIALOG_IMAGE = "image";
	private static final String DIALOG_DATE = "date";
	private static final String DEBUG_TAG = "debug";
	private Event mEvent;
	public Button createButton;
	public EditText nameText;
	public EditText dateText;
	public TextView timeText;
	public TextView locCaution;
	public TextView picText;
	public TextView vidText;
	public EditText locText;
	
	
	public Date mDate;
	public Date mEndDate;
	
	private ImageButton mPhotoButton;
	private ImageView mPhotoView;
	private ImageButton mInviteButton;
	private Button mRegisterButton;
	private Button mDateButton;
	private Button mEndDateButton;
	String invite;
	String invitename;
	
 
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
	private EditText numText;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	
	private static final String  REGISTER_URL = "http://event-showcase.org/webservice/register.php";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//enable options menu
		setHasOptionsMenu(true);
		mEvent = new Event();
		mDate = new Date();
		mEndDate = new Date();
		
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_event, parent, false);
		 
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if(NavUtils.getParentActivityIntent(getActivity()) !=null) {
			getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		nameText = (EditText)v.findViewById(R.id.event_title);
		nameText.addTextChangedListener(new TextWatcher(){
			public void onTextChanged(CharSequence e, int start, int before, int count){
				//mEvent.setTitle(e.toString());
			}

			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		locText = (EditText)v.findViewById(R.id.event_zip);
		locText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		numText = (EditText)v.findViewById(R.id.event_photo_count);
		numText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		
		mDateButton = (Button)v.findViewById(R.id.dateButton);
		mDateButton.setText(mEvent.getDate().toString());
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getDate());
				dialog.setTargetFragment(EventFragment.this,REQUEST_DATE);
				dialog.show(fm, DIALOG_DATE);
				mDate = mEvent.getDate();
			}
		});
		
		mEndDateButton = (Button)v.findViewById(R.id.dateButton2);
		mEndDateButton.setText(mEvent.getDate().toString());
		mEndDateButton.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentManager fm = getActivity().getSupportFragmentManager();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mEvent.getDate());
				dialog.setTargetFragment(EventFragment.this,REQUEST_END_DATE);
				dialog.show(fm, DIALOG_DATE);
				mEndDate = mEvent.getEndDate();
			}
		});
		
		
	
		
		
		//If camera is not available, disable camera
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || (Build.VERSION.SDK_INT >=Build.VERSION_CODES.GINGERBREAD && Camera.getNumberOfCameras() > 0);
		if (!hasACamera) {
			mPhotoButton.setEnabled(false);
		}
		
		ImageButton inviteButton = (ImageButton)v.findViewById(R.id.event_inviteButton);
		inviteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{invite});
				i.putExtra(Intent.EXTRA_TEXT, getInviteForm());
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invite_subject));
				i = Intent.createChooser(i, getString(R.string.send_invite));
				startActivity(i);
			}
		});
		mInviteButton = (ImageButton)v.findViewById(R.id.event_inviteChooser);
		mInviteButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i,REQUEST_CONTACT);
				
			}
		});
		/*mDatePicker = (DatePicker)v.findViewById(R.id.datePicker);
		mDatePicker.OnDateChangedListener(new View.OnDateChangedListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		*/
		
		mRegisterButton = (Button)v.findViewById(R.id.registerButton);
		mRegisterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new CreateEvent().execute();
				
			}
		});
		
		
		return v;
	}
	
	class CreateEvent extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(getActivity());
           pDialog.setMessage("Creating Event...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
           int success;
           String eventname = nameText.getText().toString();
           String eventzip = locText.getText().toString();
           String eventphotocount = numText.getText().toString();
           String dateFormat = "MM-d-yyyy";
   			String dateString = DateFormat.format(dateFormat, mDate).toString();
   			Log.i("Date",dateString);
   			String endDateString = DateFormat.format(dateFormat, mEndDate).toString();
           String eventdate = dateString;
           String endeventdate = endDateString;
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("eventname", eventname));
               params.add(new BasicNameValuePair("eventdate", eventdate));
               params.add(new BasicNameValuePair("enddate", endeventdate));
               params.add(new BasicNameValuePair("eventzip",eventzip));
               params.add(new BasicNameValuePair("photocount",eventphotocount));

               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
                      REGISTER_URL, "POST", params);

               // full json response
               Log.d("Login attempt", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("Event Created!", json.toString());
               	//finish();
               	return json.getString(TAG_MESSAGE);
               }else{
               	Log.d("Event Creation Failure!", json.getString(TAG_MESSAGE));
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
	//set image button's image to photo
	/*private void showPhoto() {
		Photo p = mEvent.getPhoto();
		BitmapDrawable b = null;
		if (p!=null) {
			String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	*/
	
	@Override
	public void onStart(){
		super.onStart();
		//showPhoto();
	}
	@Override
	public void onStop(){
		super.onStop();
		
	}
	public static EventFragment newInstance(UUID crimeId) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_EVENT_ID, crimeId);
		
		EventFragment fragment = new EventFragment();
		fragment.setArguments(args);
		
		return fragment;
	}

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
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//if (requestCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQUEST_DATE) {
			 mDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			 String dateFormat = "MM-d-yyyy";
	   		String dateString = DateFormat.format(dateFormat, mDate).toString();
			mDateButton.setText(dateString);
		} 
		else if (requestCode == REQUEST_END_DATE) {
			mEndDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			 String endDateFormat = "MM-d-yyyy";
	   		String dateString = DateFormat.format(endDateFormat, mEndDate).toString();
			mEndDateButton.setText(dateString);
		} 
		else if (requestCode == REQUEST_CONTACT){
			 Cursor cursor = null;
	            String email = "", name = "";
	            try {
	                Uri result = data.getData();
	                Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());

	                // get the contact id from the Uri
	                String id = result.getLastPathSegment();

	                // query for everything email
	                cursor = getActivity().getContentResolver().query(Email.CONTENT_URI,  null, Email._ID + "=" + id, null, null);

	                int nameId = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
	                int emailIdx = cursor.getColumnIndex(Email.DATA);

	                // let's just get the first email
	                if (cursor.moveToFirst()) {
	                    invite = cursor.getString(emailIdx);
	                    invitename = cursor.getString(nameId);
	                    Log.v(DEBUG_TAG, "Got email: " + invite);
	                } else {
	                    Log.w(DEBUG_TAG, "No results");
	                }
	            } catch (Exception e) {
	                Log.e(DEBUG_TAG, "Failed to get email data", e);
	            } finally {
	                if (cursor != null) {
	                    cursor.close();
	                }
			
            }}
		
		}
	


	@Override
	public void onPause(){
		super.onPause();
		
	}
	private String getInviteForm(){
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mEvent.getDate()).toString();
		String report = getString(R.string.invite_model,mEvent.getTitle(), invitename, dateString);
		return report;
	}
}
