package com.seniorproject.eventshowcase;

import android.support.v4.app.Fragment;

public class EventListActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new EventListFragment();
	}

}
