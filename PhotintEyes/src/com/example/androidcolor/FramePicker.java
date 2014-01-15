package com.example.androidcolor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.example.androidcolor.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.widget.TextView;

public class FramePicker extends Activity {

	public static final String LOGTAG = "Photint";

	String filename, savename, imagepath;
	static FileOutputStream fo;
	int framerate;
	TextView textview;
	static String fileno;

	static File folderconvert, folderfinal, folder;

	String[] libraryAssets = { "ffmpeg" };
	static int out = 1;

	Bitmap bm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.framepick);
		textview = (TextView) findViewById(R.id.textView2);
		LoadWebPageASYNC task = new LoadWebPageASYNC();
		task.execute();

	}

	private class LoadWebPageASYNC extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {

		ffmpeg();

	videotoimage();

			Conversion();
			imagetovideo();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	public void ffmpeg() {
	Intent intent = getIntent();
	String file = intent.getExtras().getString("files");

	filename = (Environment.getExternalStorageDirectory() + file);
		// filename = Environment.getExternalStorageDirectory().getPath() +
		// "/test.mp4";
		folder = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/photint/videotoimage");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		folderfinal = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/photint/videofile");
		if (!folderfinal.exists()) {
			folderfinal.mkdirs();
		}

		folderconvert = new File(Environment.getExternalStorageDirectory()
				.getPath() + "/photint/imagetovideo");
		
		if (!folderconvert.exists()) {
			folderconvert.mkdirs();
		}

		for (int i = 0; i < libraryAssets.length; i++) {
			try {
				InputStream ffmpegInputStream = this.getAssets().open(
						libraryAssets[i]);
				FileMover fm = new FileMover(ffmpegInputStream,
						"/data/data/com.example.androidcolor/"
								+ libraryAssets[i]);
				fm.moveIt();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		Process process = null;

		try {
			String[] args = { "/system/bin/chmod", "755",
					"/data/data/com.example.androidcolor/ffmpeg" };

			process = new ProcessBuilder(args).start();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			process.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void videotoimage() {
		

		savename = filename;

		Process ffmpegProcess = null;

		try {
			// orginal script
			String[] ffmpegCommand = {"/data/data/com.example.androidcolor/ffmpeg","-i",savename, "-f", "image2", folder + "/%d.png" };
			// to select frame from every "xth" second
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-i",savename,"-f","image2",
			// folder+"/%d.png"};
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-i", savename
			// ,"-vsync","0","-vf",s, folder+"/%d.png"};
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-i",savename,"-f","image2","-vf","fps=fps=1/4",
			// folder+"/%d.png"};

			ffmpegProcess = new ProcessBuilder(ffmpegCommand)
					.redirectErrorStream(true).start();

			OutputStream ffmpegOutStream = ffmpegProcess.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ffmpegProcess.getInputStream()));

			String line;

			Log.v(LOGTAG, "***Starting FFMPEG***");
			while ((line = reader.readLine()) != null) {

				Log.v(LOGTAG, "***" + line + "***");

			}
			Log.v(LOGTAG, "***Ending FFMPEG***");

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (ffmpegProcess != null) {
			ffmpegProcess.destroy();
		}

	}

	void Conversion()

	{

		//textview.setText("Applying algorithm on Images...");
		

		File[] files = folder.listFiles();
		int count = files.length;
		int fps = 24 - 9;

		for (int file = 1; file <= count;) {
			fileno = Integer.toString(file);
			imagepath = folder + "/" + fileno + ".png";
			bm = BitmapFactory.decodeFile(imagepath);
			transform(bm);
			file = file + fps;
			count = files.length;

		}

	}

	public void imagetovideo() {
		//textview.setText("Image to video conversion is in progress...");
		// String finalfile = folderfinal +"/"+
		// String.valueOf(System.currentTimeMillis()) +".mp4";
		String finalfile = folderfinal + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".mp4";
		Process process = null;

		Process ffmpegProcess = null;

		try {
			// Orginal
			String[] ffmpegCommand = {"/data/data/com.example.androidcolor/ffmpeg", "-vframes",
					"1", "-i", folderconvert + "/%d.jpg", "-r", "25", finalfile };
			
//			String[] ffmpegCommand =
//					 {"/data/data/com.example.androidcolor/ffmpeg","-r","1/0.2","-i",folderconvert+"/%d.jpg","-r","25",finalfile
//					 };
			
//			String[] ffmpegCommand =
//				 {"/data/data/com.example.androidcolor/ffmpeg","-r","1/0.2","-i",folderconvert+"/%d.jpg","vcodec","mjpeg","samq","-r","24","-y",finalfile};
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-i",folderconvert
			// +"/%d.jpg","-r","25",finalfile };
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-r","1/2","-i",folderconvert
			// +"/%d.jpg","-c:v","libx264","-vf","fps=25 -pix_fmt yuv420p",finalfile
			// };
			// working only in HTC//
			// String[] ffmpegCommand =
			// {"/data/data/com.example.androidcolor/ffmpeg","-r","1/0.2","-i",folderconvert+"/%d.jpg","-r","25",finalfile
			// };

			ffmpegProcess = new ProcessBuilder(ffmpegCommand)
					.redirectErrorStream(true).start();

			OutputStream ffmpegOutStream = ffmpegProcess.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					ffmpegProcess.getInputStream()));

			String line;

			Log.v(LOGTAG, "***Starting FFMPEG***");
			while ((line = reader.readLine()) != null) {

				Log.v(LOGTAG, "***" + line + "***");

			}
			Log.v(LOGTAG, "***Ending FFMPEG***");

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (ffmpegProcess != null) {
			ffmpegProcess.destroy();
		}

		// DeleteRecursive(folder);
		// DeleteRecursive(folderconvert);
		this.finishActivity(1);
		finish();
		Intent convert = new Intent(this, Videoview.class);
		String path = finalfile;
		convert.putExtra("files", path);
		startActivity(convert);

	}

	public static Bitmap transform(Bitmap bm) {

		// constant factors

		if (bm.getWidth() > bm.getHeight()) {

			bm = bm.createBitmap(bm, (bm.getWidth() - bm.getHeight()) / 2, 0,
					bm.getHeight(), bm.getHeight());
		}

		else if (bm.getWidth() < bm.getHeight()) {

			bm = bm.createBitmap(bm, 0, (bm.getHeight() - bm.getWidth()) / 2,
					bm.getWidth(), bm.getWidth());

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
		Bitmap bmOut = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				bm.getConfig());
		// pixel information
		// color info
		int A, R, G, B;
		int pixelColor;
		// image size
		int height = bm.getHeight();
		int width = bm.getWidth();

		// scan through every pixel
		for (i = 0; i < height; ++i) {
			for (j = 0; j < width; ++j) {
				radius = (double) (l - i);
				theta = 2.0 * Math.PI * (double) (4.0 * l - j)
						/ (double) (4.0 * l);
				// theta = 2.0 * Math.PI * (double)(-j) / (double)(4.0 * l);
				fTrueX = radius * Math.cos(theta);
				fTrueY = radius * Math.sin(theta);
				x = (int) (Math.round(fTrueX)) + l;
				y = l - (int) (Math.round(fTrueY));
				// set newly-inverted pixel to output image
				if (x >= 0 && x < bm.getWidth() && y >= 0 && y < bm.getHeight()) {
					// bmOut.setPixel(i,j,Color.argb(A, R, G, B));
					bmOut.setPixel(i, j, bm.getPixel(x, y));
				}

			}
		}
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmOut, height, width,
				true);
		// create a matrix object
		Matrix matrix = new Matrix();
		matrix.postRotate(-90);
		// create a new bitmap from the original using the matrix to transform
		// the result
		Bitmap rotatedBitmap = Bitmap
				.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
						scaledBitmap.getHeight(), matrix, true);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

		// you can create a new file name "test.jpg" in sdcard folder.

		File f = new File(folderconvert + "/" + out + ".jpg");
		try {
			f.createNewFile();
			fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());

			// remember close de FileOutput
			fo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out++;
		// write the bytes in file

		// return final bitmap
		return rotatedBitmap;

	}

	// private void DeleteRecursive(File fileOrDirectory) {
	// if (fileOrDirectory.isDirectory())
	// for (File child : fileOrDirectory.listFiles())
	// {
	// child.delete();
	// DeleteRecursive(child);
	// }
	//
	// fileOrDirectory.delete();
	// }
	//

}
