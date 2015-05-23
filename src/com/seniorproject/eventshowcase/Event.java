package com.seniorproject.eventshowcase;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	
private static final String JSON_ID = "id";
private static final String JSON_TITLE = "title";
private static final String JSON_SOLVED = "solved";
private static final String JSON_DATE = "date";
private static final String JSON_PHOTO = "photo";
private static final String JSON_INVITE = "invite";
 private UUID mId;
 
 private String mTitle;
 private boolean mSolved;
 private  Date mDate = new Date();
 private Date mEndDate = new Date();
 private Photo mPhoto;
 private String mInvite;
 
 public Event(){
	 mId = UUID.randomUUID();
 }
 
 public Event(JSONObject json) throws JSONException {
	 mId = UUID.fromString(json.getString(JSON_ID));
	 if(json.has(JSON_TITLE)) {
		 mTitle = json.getString(JSON_TITLE);
	 }
	 mSolved = json.getBoolean(JSON_SOLVED);
	 mDate = new Date(json.getLong(JSON_DATE));
	 if (json.has(JSON_PHOTO))
		 mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
	 if(json.has(JSON_INVITE))
		 mInvite =json.getString(JSON_INVITE);
 }
 
 public JSONObject toJSON() throws JSONException {
	 JSONObject json =  new JSONObject();
	 json.put(JSON_ID, mId.toString());
	 json.put(JSON_TITLE, mTitle);
	 json.put(JSON_SOLVED, mSolved);
	 json.put(JSON_DATE, mDate.getTime());
	 if(mPhoto != null)
		 json.put(JSON_PHOTO, mPhoto.toJSON());
	 json.put(JSON_INVITE, mInvite);
	 return json;
 }
 public Photo getPhoto(){
	 return mPhoto;
 }
 public void setPhoto(Photo p) {
	 mPhoto = p;
 }
 
 public String getInvite() {
	 return mInvite;
 }
 public void setInvite(String invite){
	 mInvite = invite;
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
 public boolean isSolved(){
	 return mSolved;
 }
 public void setSolved(boolean solved){
	 mSolved = solved;
 }
 public Date getDate(){
	 return mDate;
 }
 public void setDate(Date d){
	 mDate = d;
 }

public Date getEndDate() {
	// TODO Auto-generated method stub
	return mEndDate;
}
 

 
}
