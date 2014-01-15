package com.example.androidcolor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.widget.VideoView;
import android.app.Activity;
import android.content.Intent;


public class VideoGrabber extends Activity {

	String uri = Environment.getExternalStorageDirectory() + "/test.mp4";
	static Bitmap rotatedBitmap = null;
	@Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);

  
  Intent intent = getIntent();
  String file = intent.getExtras().getString("files");
  String filename = (Environment.getExternalStorageDirectory() + file);
 
//  videoview = (VideoView) findViewById(R.id.videoView1);
// 
//  imViewAndroid.setImageBitmap((BitmapFactory.decodeFile(filename)));
//  imageview.setImageBitmap(transform(bm));
//  
 
  
  
 }
	
	 
		   
	
}