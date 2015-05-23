package com.seniorproject.eventshowcase;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Media {
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	 private UUID mId;
	 
	 private String mTitle;
	 private  Date mDate = new Date();
	 private Photo mPhoto;
	 
	 public Media(){
		 mId = UUID.randomUUID();
	 }
	 
	 public Media(JSONObject json) throws JSONException {
		 mId = UUID.fromString(json.getString(JSON_ID));
		 if(json.has(JSON_TITLE)) {
			 mTitle = json.getString(JSON_TITLE);
		 }
		
		 mDate = new Date(json.getLong(JSON_DATE));
		 if (json.has(JSON_PHOTO))
			 mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
	 }
	 
	 public JSONObject toJSON() throws JSONException {
		 JSONObject json =  new JSONObject();
		 json.put(JSON_ID, mId.toString());
		 json.put(JSON_TITLE, mTitle);
		 json.put(JSON_DATE, mDate.getTime());
		 if(mPhoto != null)
			 json.put(JSON_PHOTO, mPhoto.toJSON());
		 return json;
	 }
	 
	 public Photo getPhoto(){
		 return mPhoto;
	 }
	 public void setPhoto(Photo p) {
		 mPhoto = p;
	 }
	 @Override
	 public String toString() {
		 return mTitle;
	 }
	 
	 public UUID getId(){
		 return mId;
	 }
	 
	 public String getTitle(){
		 return mTitle;
	 }
	 
	 public void setTitle(String title){
		 mTitle = title;
	 }
	 public Date getDate(){
		 return mDate;
	 }
	 public void setDate(Date d){
		 mDate = d;
	 }
}
