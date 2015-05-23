package com.seniorproject.eventshowcase;

import java.util.ArrayList;
import java.util.HashMap;

import com.seniorproject.eventshowcase.R;
 

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListViewAdapter extends BaseAdapter {
 
	// Declare Variables
	Context context;
	LayoutInflater inflater;
	ArrayList<HashMap<String, String>> data;
	ImageLoader imageLoader;
	HashMap<String, String> resultp = new HashMap<String, String>();
 
	public ListViewAdapter(Context context,
			ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		imageLoader = new ImageLoader(context);
	}
 
	@Override
	public int getCount() {
		return data.size();
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	 
	public View getView(final int position, View convertView, ViewGroup parent) {
		// Declare Variables
		TextView title;
		TextView date;
		TextView votes;
		ImageView image;
 
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View itemView = inflater.inflate(R.layout.list_item_media, parent, false);
		
		
		// Get the position
		resultp = data.get(position);
 
		// Locate the TextViews in listview_item.xml
		title = (TextView) itemView.findViewById(R.id.media_list_item_titleTextView);
		date = (TextView) itemView.findViewById(R.id.media_list_item_dateTextView);
		votes = (TextView) itemView.findViewById(R.id.media_list_item_voteTextView);
 
		// Locate the ImageView in listview_item.xml
		image = (ImageView) itemView.findViewById(R.id.medialist_imageView);
 
		// Capture position and set results to the TextViews
		title.setText(resultp.get(MediaListFragment.TAG_TITLE));
		date.setText(resultp.get(MediaListFragment.TAG_DATE));
		votes.setText(resultp.get(MediaListFragment.TAG_VOTE));
		// Capture position and set results to the ImageView
		// Passes flag images URL into ImageLoader.class
		imageLoader.DisplayImage(resultp.get(MediaListFragment.TAG_FILE), image);
		// Capture ListView item click
		itemView.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				// Get the position
				resultp = data.get(position);
				Intent intent = new Intent(context, MediaViewerActivity.class);
				// Pass all data rank
				intent.putExtra("title", resultp.get(MediaListFragment.TAG_TITLE));
				// Pass all data country
				intent.putExtra("date", resultp.get(MediaListFragment.TAG_DATE));
				// Pass all data country
				intent.putExtra("vote", resultp.get(MediaListFragment.TAG_VOTE));
				// Pass all data flag
				intent.putExtra("image", resultp.get(MediaListFragment.TAG_FILE));
				intent.putExtra("media_id", resultp.get(MediaListFragment.TAG_MEDIA_ID));
				intent.putExtra("flag", resultp.get(MediaListFragment.TAG_FLAG));
				//Log.i("Item ID",(resultp.get(MediaListFragment.EXTRA_MEDIA_ID)).toString());
				// Start SingleItemView Class
				context.startActivity(intent);
 
			}
			
		});
		return itemView;
	}
}