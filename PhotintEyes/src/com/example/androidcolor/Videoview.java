package com.example.androidcolor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
 
public class Videoview extends Activity {
   private VideoView mVideoView;
 
   @Override
   public void onCreate(Bundle icicle) {
	   
     super.onCreate(icicle);
     setContentView(R.layout.viewselector);
     mVideoView = (VideoView) findViewById(R.id.surface_view);
     Intent intent = getIntent();
     String file = intent.getExtras().getString("files");
    
     mVideoView.setVideoURI(Uri.parse(file));
     mVideoView.setMediaController(new MediaController(this));
     mVideoView.requestFocus();
     mVideoView.start();
   }
}