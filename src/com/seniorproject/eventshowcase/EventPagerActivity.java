package com.seniorproject.eventshowcase;

import java.util.ArrayList;
import java.util.UUID;

import com.seniorproject.eventshowcase.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class EventPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Event> mEvents;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		//get given events (Call to database)
		//mEvents = EventLab.get(this).getEvents();
		
		FragmentManager fm = getSupportFragmentManager();
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			@Override
			public int getCount() {
				return mEvents.size();
			}
			
			@Override
			public Fragment getItem(int pos) {
				Event event = mEvents.get(pos);
				//goes to EventFragment, change to MediaListFragment
				return MediaListFragment.newInstance(event.getId());
			}
				
		});
		//
		// Display Correct Event Name in Action Bar
		//
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			
			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				Event event = mEvents.get(pos);
				if (event.getTitle() != null){
					setTitle(event.getTitle());
				}
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// leave blank
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// leave blank
				
			}
		});
		//Set initial pager item
		UUID eventId = (UUID)getIntent().getSerializableExtra(EventFragment.EXTRA_EVENT_ID);
			for(int i = 0; i< mEvents.size();i++) {
				if (mEvents.get(i).getId().equals(eventId)) {
					mViewPager.setCurrentItem(i);
					break;
				}
			}
		
	}

}
