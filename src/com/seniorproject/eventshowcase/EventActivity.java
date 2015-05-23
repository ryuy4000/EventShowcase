package com.seniorproject.eventshowcase;


import java.util.UUID;

import android.support.v4.app.Fragment;


/*
 * 1. List of pictures (eventually taken from server)
 * 2. Button for uploading new picture (send to R.layout.activity_submission)
 * 3. Button to go back to Event list
 * 4. Takes Event info and id
 * 
 */
public class EventActivity extends SingleFragmentActivity {
	

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		UUID eventId = (UUID)getIntent().getSerializableExtra(EventFragment.EXTRA_EVENT_ID);
		return EventFragment.newInstance(eventId);
	}


}
