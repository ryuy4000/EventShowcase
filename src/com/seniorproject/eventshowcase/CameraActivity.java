package com.seniorproject.eventshowcase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;
 
import com.seniorproject.eventshowcase.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

	 
	@SuppressLint("NewApi")
	public class CameraActivity extends Activity {
	    ProgressDialog prgDialog;
	    String encodedString;
	    RequestParams params = new RequestParams();
	    String imgPath, fileName;
	    Bitmap bitmap;
		private Uri imageUri;
		private Uri mCapturedImageURI;
	    private static int RESULT_LOAD_IMG = 1;
	    static final int REQUEST_IMAGE_CAPTURE = 2;
	    public static String EXTRA_PHOTO_FILENAME = "http://event-showcase.org/webservice/uploadedimages/";
	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fragment_upload);
	        prgDialog = new ProgressDialog(this);
	        // Set Cancelable as False
	        prgDialog.setCancelable(false);
	    }
	    
	    public void dispatchTakePictureIntent(View view) {
	    	//define the file-name to save photo taken by Camera activity
	    	String fileName = "temp.jpg";  
	        ContentValues values = new ContentValues();  
	        values.put(MediaStore.Images.Media.TITLE, fileName);  
	        mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);  

	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  
	        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
	        
	       // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	       // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	          //  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	        //}
	    }
	 
	    public void loadImagefromGallery(View view) {
	        // Create intent to Open Image applications like Gallery, Google Photos
	        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
	                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	        // Start the Intent
	        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
	    }
	    
	    public static Bitmap rotateImage(Bitmap source, float angle)
	    {
	          Matrix matrix = new Matrix();
	          matrix.postRotate(angle);
	          return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	    }
	    
	    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

	        ExifInterface ei;
	        try {
	            ei = new ExifInterface(path);
	            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
	                    ExifInterface.ORIENTATION_NORMAL);
	            switch (orientation) {
	            case ExifInterface.ORIENTATION_ROTATE_90:
	                bitmap = rotateImage(bitmap, 90);
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_180:
	                bitmap = rotateImage(bitmap, 180);
	                break;
	            case ExifInterface.ORIENTATION_ROTATE_270:
	                bitmap = rotateImage(bitmap, 270);
	                break;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return bitmap;
	    }
	 
	    // When Image is selected from Gallery
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        		
	        	//When an Image is taken
	        	
	        	if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        	 
	        		String[] projection = { MediaStore.Images.Media.DATA}; 
	                Cursor cursor = getContentResolver().query(mCapturedImageURI, projection, null, null, null); 
	                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
	                cursor.moveToFirst(); 
	                 imgPath = cursor.getString(column_index_data);
	                
	        		 ImageView imgView = (ImageView) findViewById(R.id.imgView);
	        		
		                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
		                
		               bitmap = imageOreintationValidator(bitmap,imgPath);
		              // Set the Image in ImageView
			                imgView.setImageBitmap(bitmap);
		                // Get the Image's file name
		                String fileNameSegments[] = imgPath.split("/");
		                fileName = fileNameSegments[fileNameSegments.length - 1];
		                // Put file name in Async Http Post Param which will used in Php web app
		                params.put("filename", fileName);
		                Log.e("Filename: ", fileName);
		                Intent i = new Intent();
		                i.putExtra(EXTRA_PHOTO_FILENAME, EXTRA_PHOTO_FILENAME + fileName);
		                this.setResult(Activity.RESULT_OK, i);
	        		}
	                    
	        	
	        	
	            // When an Image is picked
	        	else if (requestCode == RESULT_LOAD_IMG   && resultCode == RESULT_OK
	                    && null != data) {
	                // Get the Image from data
	        		Log.i("Loaded", "Image");
	                Uri selectedImage = data.getData();
	                String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	                // Get the cursor
	                Cursor cursor = getContentResolver().query(selectedImage,
	                        filePathColumn, null, null, null);
	                // Move to first row
	                cursor.moveToFirst();
	 
	                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	                imgPath = cursor.getString(columnIndex);
	                cursor.close();
	                ImageView imgView = (ImageView) findViewById(R.id.imgView);
	                // Set the Image in ImageView
	                imgView.setImageBitmap(BitmapFactory
	                        .decodeFile(imgPath));
	                // Get the Image's file name
	                String fileNameSegments[] = imgPath.split("/");
	                fileName = fileNameSegments[fileNameSegments.length - 1];
	                // Put file name in Async Http Post Param which will used in Php web app
	                params.put("filename", fileName);
	                Log.e("Filename: ", fileName);
	                Intent i = new Intent();
	                i.putExtra(EXTRA_PHOTO_FILENAME, EXTRA_PHOTO_FILENAME + fileName);
	                this.setResult(Activity.RESULT_OK, i);
	 
	            } else {
	                Toast.makeText(this, "You haven't picked Image",
	                        Toast.LENGTH_LONG).show();
	                this.setResult(RESULT_CANCELED);
	            }
	       
	 
	    }
	     
	 // When Upload button is clicked
	    public void uploadImage(View v) {
	        // When Image is selected from Gallery
	        if (imgPath != null && !imgPath.isEmpty()) {
	            prgDialog.setMessage("Converting Image to Binary Data");
	            prgDialog.show();
	            // Convert image to String using Base64
	            encodeImagetoString();
	        // When Image is not selected from Gallery
	        } else {
	            Toast.makeText(
	                    getApplicationContext(),
	                    "You must select image from gallery before you try to upload",
	                    Toast.LENGTH_LONG).show();
	        }
	    }
	 
	    // AsyncTask - To convert Image to String
	    public void encodeImagetoString() {
	        new AsyncTask<Void, Void, String>() {
	 
	            protected void onPreExecute() {
	 
	            };
	 
	            @Override
	            protected String doInBackground(Void... params) {
	                BitmapFactory.Options options = null;
	                options = new BitmapFactory.Options();
	                options.inSampleSize = 3;
	                bitmap = BitmapFactory.decodeFile(imgPath,
	                        options);
	                ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                // Must compress the Image to reduce image size to make upload easy
	                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream); 
	                byte[] byte_arr = stream.toByteArray();
	                // Encode Image to String
	                encodedString = Base64.encodeToString(byte_arr, 0);
	                return "";
	            }
	 
	            @Override
	            protected void onPostExecute(String msg) {
	                prgDialog.setMessage("Calling Upload");
	                // Put converted Image string into Async Http Post param
	                params.put("image", encodedString);
	                // Trigger Image upload
	                triggerImageUpload();
	            }
	        }.execute(null, null, null);
	    }
	     
	    public void triggerImageUpload() {
	        makeHTTPCall();
	    }
	 
	    // Make Http call to upload Image to Php server
	    public void makeHTTPCall() {
	        prgDialog.setMessage("Invoking Php");        
	        AsyncHttpClient client = new AsyncHttpClient();
	        // Don't forget to change the IP address to your LAN address. Port no as well.
	        client.post("http://event-showcase.org/webservice/upload_image.php",
	                params, new AsyncHttpResponseHandler() {
	                    // When the response returned by REST has Http
	                    // response code '200'
	                    @Override
	                    public void onSuccess(String response) {
	                        // Hide Progress Dialog
	                        prgDialog.hide();
	                        Toast.makeText(getApplicationContext(), response,
	                                Toast.LENGTH_LONG).show();
	                    }
	 
	                    // When the response returned by REST has Http
	                    // response code other than '200' such as '404',
	                    // '500' or '403' etc
	                    @Override
	                    public void onFailure(int statusCode, Throwable error,
	                            String content) {
	                        // Hide Progress Dialog
	                        prgDialog.hide();
	                        // When Http response code is '404'
	                        if (statusCode == 404) {
	                            Toast.makeText(getApplicationContext(),
	                                    "Requested resource not found",
	                                    Toast.LENGTH_LONG).show();
	                        }
	                        // When Http response code is '500'
	                        else if (statusCode == 500) {
	                            Toast.makeText(getApplicationContext(),
	                                    "Something went wrong at server end",
	                                    Toast.LENGTH_LONG).show();
	                        }
	                        // When Http response code other than 404, 500
	                        else {
	                            Toast.makeText(
	                                    getApplicationContext(),
	                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
	                                            + statusCode, Toast.LENGTH_LONG)
	                                    .show();
	                        }
	                    }
	                });
	    }
	 
	    
	    
	   
	 
	    @Override
	    protected void onDestroy() {
	        // TODO Auto-generated method stub
	        super.onDestroy();
	        // Dismiss the progress bar when application is closed
	        if (prgDialog != null) {
	            prgDialog.dismiss();
	        }
	    }
	}

