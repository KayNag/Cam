package com.example.androidcolor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mobvcasting.mjpegffmpeg.CropImage;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Greycheck extends Activity {
    ImageView imViewAndroid,imageview;
    Bitmap bm,bmfirst,news;
    String filename;
    @  Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.decisive);
      
        Button quick = (Button) findViewById(R.id.quick);
        Button normal = (Button) findViewById(R.id.normal);       
        
        
       
           
	    quick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	setContentView(R.layout.framepick);
            	Intent intent = getIntent();
		        String file = intent.getExtras().getString("croppedpath");
		        String filename = file;
		        
	      bm = ShrinkBitmap(filename, 640, 480);
		        
		       
		        
		        LoadWebPageASYNC task = new LoadWebPageASYNC();
				task.execute();
            	
            }
        });
          
	    normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	setContentView(R.layout.framepick);
            	Intent intent = getIntent();
		        String file = intent.getExtras().getString("croppedpath");
		        
		     
        bm = BitmapFactory.decodeFile(file);
		         
		       
		        
		        
		        LoadWebPageASYNC task = new LoadWebPageASYNC();
				task.execute();
            }
        });
	    
    }
    private class LoadWebPageASYNC extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			news = transform(bm);
           	return null;
		}

		@Override
		protected void onPostExecute(String result) {
			  setContentView(R.layout.grey);
			  imViewAndroid = (ImageView) findViewById(R.id.imageView1);
		        imageview = (ImageView) findViewById(R.id.imViewAndroid);
		        imViewAndroid.setImageBitmap(bm);
		        imageview.setImageBitmap(news);
		}
	}
  

	
  
    
    
   public Bitmap ShrinkBitmap(String file, int height, int width) {
	   BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	    bmpFactoryOptions.inJustDecodeBounds = true;
	    Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

	    int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
	    int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);

	    if (heightRatio > 1 || widthRatio > 1)
	    {
	     if (heightRatio > widthRatio)
	     {
	      bmpFactoryOptions.inSampleSize = heightRatio;
	     } else {
	      bmpFactoryOptions.inSampleSize = widthRatio; 
	     }
	    }

	    bmpFactoryOptions.inJustDecodeBounds = false;
	    bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	 return bitmap;
	}
	


public static Bitmap transform(Bitmap bm) {
	

    // constant factors
	  
	
   if (bm.getWidth()>bm.getHeight()){
		   
		   bm = bm.createBitmap(bm,(bm.getWidth()-bm.getHeight())/2,0,bm.getHeight(), bm.getHeight());
	   }
		
	else if (bm.getWidth()<bm.getHeight())
		{

		bm = bm.createBitmap(bm,0, (bm.getHeight()-bm.getWidth())/2,bm.getWidth(), bm.getWidth());
			
		}
			
    
	   final double GS_RED = 0.299;
	    final double GS_GREEN = 0.587;
	    final double GS_BLUE = 0.114;
	  
	    int l = bm.getWidth() / 2;
	    int i, j;
	    int x, y;
	    double radius, theta;
	    double fTrueX, fTrueY;
    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());
    // pixel information
	    // color info
        int A, R, G, B;
        int pixelColor;
        // image size
        int height = bm.getHeight();
        int width = bm.getWidth();
     
        // scan through every pixel
        for (i = 0; i < height; ++i)
        {
            for (j = 0; j < width; ++j)
            {
               radius = (double)(l - i);
                theta = 2.0 * Math.PI * (double)(4.0 * l - j) / (double)(4.0 * l);
              //  theta = 2.0 * Math.PI * (double)(-j) / (double)(4.0 * l);
                fTrueX = radius * Math.cos(theta);
                fTrueY = radius * Math.sin(theta);
                x = (int)(Math.round(fTrueX)) + l;
                y = l - (int)(Math.round(fTrueY));
                // set newly-inverted pixel to output image
                if (x >= 0 && x < bm.getWidth() && y >= 0 && y < bm.getHeight())
                {
                	//bmOut.setPixel(i,j,Color.argb(A, R, G, B));
                	bmOut.setPixel(i,j,bm.getPixel(x,y));
                }
                
            }
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmOut, height, width, true);
     // create a matrix object
        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
     // create a new bitmap from the original using the matrix to transform the result
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        //you can create a new file name "test.jpg" in sdcard folder.
        File f = new File(Environment.getExternalStorageDirectory() + "/photint/" + String.valueOf(System.currentTimeMillis()) +".jpg");
        try {
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
	        fo.write(bytes.toByteArray());

	        // remember close de FileOutput
	        fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //write the bytes in file
        
        // return final bitmap
        return rotatedBitmap;
    }
   
   
}