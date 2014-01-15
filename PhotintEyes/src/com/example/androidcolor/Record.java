package com.example.androidcolor;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mobvcasting.mjpegffmpeg.CropImage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class Record extends Activity {
	
	private static final int CAPTURE_PICTURE_INTENT = 0;
	private static final int CAPTURE_VIDEO_INTENT = 1;
	
	Uri mCapturedImageURI ;
	VideoView videoviewPlay;
	Intent intent;
	 String uploadfile ;
	Toast toast;
	
	String email;
	String filename ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	File wallpaperDirectory = new File("/sdcard/photint/");
        wallpaperDirectory.mkdirs();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoot);
        
         Button imageshoot = (Button) findViewById(R.id.image);
         Button videoshoot = (Button) findViewById(R.id.video);        
        

                
         imageshoot.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // Perform action on click
            	 Context context = getApplicationContext();
            	 
                 CharSequence text = "Please shoot a image by pressing the capture button.";
                 int duration = Toast.LENGTH_LONG;
               //Get the bundle


                 toast = Toast.makeText(context, text, duration);
                 toast.show();

               
                 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  
            
                 intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  
                 
                 startActivityForResult(intent, CAPTURE_PICTURE_INTENT);
                 
            	 
            	 
             }
         });
         videoshoot.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 // Perform action on click
            	 Context context = getApplicationContext();
                 CharSequence text = "Please record a video by pressing the record button.";
                 int duration = Toast.LENGTH_LONG;
                
               //Get the bundle


               toast = Toast.makeText(context, text, duration);
                 toast.show();

               
                 Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);  
            
                 intent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);  
                 
                 startActivityForResult(intent, CAPTURE_VIDEO_INTENT);
//                 
            	 
            	 
             }
         });
         
        
       
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
  	  super.onActivityResult(requestCode, resultCode, intent);
  	  toast.cancel();
  	  int a = requestCode ;
   if (requestCode == 0)
   {
  	  if (resultCode!= RESULT_OK) return;
  	  
  	  	  try {
  	    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(intent.getData(), "r");
  	    FileInputStream fis = videoAsset.createInputStream();
  	  
  	  
  	
  	 filename = "/photint/" + String.valueOf(System.currentTimeMillis()) +".jpg";
  	  uploadfile = String.valueOf(System.currentTimeMillis()) +".jpg";
      File tmpFile = new File(Environment.getExternalStorageDirectory(), filename); 
  	    FileOutputStream fos = new FileOutputStream(tmpFile);
  	    
  	
  		
  	    byte[] buf = new byte[1024];
  	    int len;
  	    while ((len = fis.read(buf)) > 0) {
  	        fos.write(buf, 0, len);
  	    }       
  	    fis.close();
  	    fos.close();
  	      	// ftp();        	
  	  } catch (IOException io_e) {
  	    // TODO: handle error
  	  }

  	this.finishActivity(1);
  	 	finish(); 
  	 	Intent convert = new Intent(this, CropImage.class);
	  	String path = filename;
	 	convert.putExtra("files",path);
	 	startActivity(convert); 
   }
   
   else if (requestCode == 1)
   {
  	  if (resultCode != RESULT_OK) return;
  	  
  	  	  try {
  	    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(intent.getData(), "r");
  	    FileInputStream fis = videoAsset.createInputStream();
  	  
  	  
  	
  	 filename = "/photint/" + String.valueOf(System.currentTimeMillis()) +".mp4";
  	  uploadfile = String.valueOf(System.currentTimeMillis()) +".mp4";
      File tmpFile = new File(Environment.getExternalStorageDirectory(), filename); 
  	    FileOutputStream fos = new FileOutputStream(tmpFile);
  	
  		
  	    byte[] buf = new byte[1024];
  	    int len;
  	    while ((len = fis.read(buf)) > 0) {
  	        fos.write(buf, 0, len);
  	    }       
  	    fis.close();
  	    fos.close();
  	      	// ftp();        	
  	 
	  	
  	  } catch (IOException io_e) {
  	    // TODO: handle error
  	  }
  	  	  
  	  	this.finishActivity(1);
  	 	finish(); 
  	 	Intent convert = new Intent(this, FramePicker.class);
	  	String path = filename;
	 	convert.putExtra("files",path);
	 	startActivity(convert); 
   }
  	  	

	  		  	  	  	
    }

	
    
    
}