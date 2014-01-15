package com.example.androidcolor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


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
import android.graphics.Point;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Bananatransformation extends Activity {
    ImageView imViewAndroid,imageview;
    Bitmap bm,news;
    String filename;
    static Bitmap finalimg = null;
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
		        String file = intent.getExtras().getString("files");
		        String filename = (Environment.getExternalStorageDirectory() + file);
		      bm = ShrinkBitmap(filename, 640, 480);
		        
		       
		        
		        LoadWebPageASYNC task = new LoadWebPageASYNC();
				task.execute();
            	
            }
        });
          
	    normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	setContentView(R.layout.framepick);
            	Intent intent = getIntent();
		       // String file = intent.getExtras().getString("/photint/1.png");
		        String filename = (Environment.getExternalStorageDirectory() +"/photint/1.png");
		     
		        bm = BitmapFactory.decodeFile(filename);
		       
		        
		        
		        LoadWebPageASYNC task = new LoadWebPageASYNC();
				task.execute();
            }
        });
	    
    }
    private class LoadWebPageASYNC extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			bananaTransformSingleSide( bm,20,0);
		           

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			  setContentView(R.layout.grey);
			  imViewAndroid = (ImageView) findViewById(R.id.imageView1);
		        imageview = (ImageView) findViewById(R.id.imViewAndroid);
		        imViewAndroid.setImageBitmap(bm);
		        imageview.setImageBitmap(finalimg);
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
	


public static Bitmap bananaTransformSingleSide(Bitmap img,int Radius, int mode){
    
    int OutperimeterS = (int)Math.round((Math.PI*Radius*2));   
  //  result = new Bitmap(2*Radius,2*Radius,Bitmap.TYPE_3BYTE_BGR);
    if(mode == 0){ //left
      int centreX = 100;
      int centreY = 100;
      int resdim = 2*Radius;
      double shearFactor = -1/(2*Math.PI);
      double startangle=2*Math.PI;      //left hemisphere  - start from Math.PI  
      for(int resy=0;resy<resdim;++resy){ 
        double yfactor = resy/2;
        for(int resx=0;resx<resdim-resy;++resx){  
           double xsheared = resx*Math.PI;
           double k = yfactor - xsheared*shearFactor;
           double tempangle = startangle+(xsheared/k);
           
           int x = (int)(Math.round(Math.cos(tempangle) * k + centreX)); 
           int y  = (int)(Math.round(Math.sin(tempangle) * k +centreY));
           finalimg.setPixel(resx, resy, img.getPixel(x, y));
           
        }
      }
    }
    else{          //right
      int centreX = 100;
      int centreY = 100;
      int resdim = 2*Radius;
      double shearFactor = -1/(2*Math.PI);
      double startangle= Math.PI;      //right hemisphere  - start from Math.PI  
      for(int resy=0;resy<resdim;++resy){
        double yfactor = resy/2;//  
        for(int resx=resdim-1;resx>=resdim-resy;--resx){  //points on the diameter of each circle
           double i = OutperimeterS-1-(resx-1)*Math.PI;
           double k = resdim + (OutperimeterS-i-1)*shearFactor-1-yfactor;
           double tempangle = startangle+(i/k);
           int x = (int)(Math.round(Math.cos(tempangle) * k + centreX)); 
           int y  = (int)(Math.round(Math.sin(tempangle) * k +centreY));
           finalimg.setPixel(resx, resy, img.getPixel(x, y));
         }
      }
    }

    
    return finalimg;
}
   
   public static Bitmap stitchTriangleTransforms(Bitmap left,Bitmap right){
	    
	    if(left!=null && right!=null){
	        int wR = right.getWidth();  
	        int hR = right.getHeight(); 
	        int wL = left.getWidth();
	        int hL = left.getHeight();
	        int h,w;
	        if(hR > hL){
	          h=hR;
	          w=wR;
	        }
	        else{
	           h=hL;
	           w=wL; 
	        }   
//	        Bitmap dimg = new Bitmap(w, h*2, right.getColorModel().getTransparency()); 
//	        Graphics2D g = dimg.createGraphics();
//	        int sclaedSize = 2*h;
//	        g.drawImage(left,0,0,w,h,0,0,wL,hL, null);  
//	        g.drawImage(right,0,h,w,sclaedSize,0,0,wR,hR,null);
//	        Bitmap processed = new Bitmap(dimg.getWidth(), dimg.getHeight(),Bitmap.TYPE_3BYTE_BGR); 
//	        AffineTransform af = new AffineTransform();
//	        double shearFactor = -1/(2*Math.PI);
//	        af.setToShear(-0.0, shearFactor); 
//	        AffineTransformOp affineTransformOp = new AffineTransformOp(af, null);
//	        processed = affineTransformOp.filter(dimg, null);
//	        g.dispose();
//	        Bitmap clipped = processed.getSubimage(0,0,processed.getWidth(),(int)processed.getHeight()/2);
//	        Bitmap scaled =  new Bitmap(sclaedSize,sclaedSize,clipped.getColorModel().getTransparency());
//	        Graphics2D gr = scaled.createGraphics(); 
//	        gr.drawImage(clipped,0,0,sclaedSize,sclaedSize,0,1,clipped.getWidth(),clipped.getHeight()-1,null);
//	        gr.dispose();
//	        return scaled;
	    }
	  return null;
	}


   
}