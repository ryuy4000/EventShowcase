package com.seniorproject.eventshowcase;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.util.Log;

public class MediaLab {
	private static final String TAG = "MediaLab";
	private static final String FILENAME ="media.json";
	
	private ArrayList<Media> mMedia;
	//private EventShowcaseJSONSerializer mSerializer;
	
	private static MediaLab sMediaLab;
	private Context mAppContext;
	
	private MediaLab(Context appContext){
		mAppContext = appContext;
		//mSerializer = new EventShowcaseJSONSerializer(mAppContext, FILENAME);
		try{
			//mMedia = mSerializer.loadMedia();
		} catch (Exception e){
			mMedia = new ArrayList<Media>();
			Log.e(TAG, "Error Loading media: ", e);
		}
		
		
	}
	
	public static MediaLab get(Context c){
		if (sMediaLab == null){
			sMediaLab = new MediaLab(c.getApplicationContext());
		}
		return sMediaLab;
	}
	public void addMedia(Media m){
		mMedia.add(m);
	}
	public void deleteMedia(Media m){
		mMedia.remove(m);
	}
	
	public boolean saveMedia() {
		try{
			//mSerializer.saveMedia(mMedia);
			Log.d(TAG,"events saved to file");
			return true;
		} catch (Exception e) {
			Log.e(TAG,"Error saving event:", e);
			return false;
		}
	}
	public ArrayList<Media> getMedia(){
		return mMedia;
	}
	public Media getMedia(UUID id){
		for(Media e : mMedia) {
			if (e.getId().equals(id))
				return e;
		}
		return null;
	}
}
