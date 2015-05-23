package com.seniorproject.eventshowcase;

import java.util.UUID;

import android.support.v4.app.Fragment;

public class MediaActivity extends SingleFragmentActivity {
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new MediaFragment();
	}
}
