package com.example.readimage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView image_drawable;
	private ImageView image_assets;
	private ImageView image_sdcard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		image_drawable=(ImageView)findViewById(R.id.image_drawable);
		image_assets=(ImageView)findViewById(R.id.image_assets);
		image_sdcard=(ImageView)findViewById(R.id.image_sdcard);
		
		ReadFromDrawable();
		ReadFromAssets();
		ReadFromSdcard();
	}
	
	private void ReadFromDrawable()
	{
		Drawable drawable=getResources().getDrawable(R.drawable.pixar);
		image_drawable.setImageDrawable(drawable);
		
		/*Resources res=getResources();
		Bitmap bitmap=BitmapFactory.decodeResource(res, R.drawable.pixar);
		image_drawable.setImageBitmap(bitmap);*/
		
		/*Resources res=getResources();
		int id =res.getIdentifier("pixar", "drawable", "com.example.readimage"); //name:image name. defType:drawable,string。。。， defPackage:package name
		Drawable drawable2=getResources().getDrawable(id);
		image_drawable.setImageDrawable(drawable2);*/
	}
	private void ReadFromAssets()
	{
		AssetManager asm=getAssets();
		InputStream is = null;
		try {
			is = asm.open("pixar2.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable drawable = Drawable.createFromStream(is, null);
		image_assets.setImageDrawable(drawable);
		
		/*Bitmap bitmap=BitmapFactory.decodeStream(is);
		image_assets.setImageBitmap(bitmap);*/
	}
	private void ReadFromSdcard()
	{
		String imgFilePath = Environment.getExternalStorageDirectory().toString()
				+ "/pixar3.jpg";
		Bitmap bit = BitmapFactory.decodeFile(imgFilePath);
		image_sdcard.setImageBitmap(bit);
		
		/*FileInputStream fis = null;
		try {
			fis = new FileInputStream(new File(imgFilePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}//文件输入流
		Bitmap bitmap = BitmapFactory.decodeStream(fis);
		image_sdcard.setImageBitmap(bitmap);*/
	}

}
