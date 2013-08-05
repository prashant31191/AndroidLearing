package com.example.writedatatosdcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText edittext;
	private TextView text;
	private Button btn_read;
	private Button btn_write;
	private Button btn_clean;
	private String readtext;
	String sdCardPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sdCardPath=Environment.getExternalStorageDirectory().getPath();

		edittext = (EditText) findViewById(R.id.edittext);
		text = (TextView) findViewById(R.id.textview);
		btn_read = (Button) findViewById(R.id.btn_read);
		btn_write = (Button) findViewById(R.id.btn_write);
		btn_clean = (Button) findViewById(R.id.btn_clean);

		btn_read.setOnClickListener(readListener);
		btn_write.setOnClickListener(writeListener);
		btn_clean.setOnClickListener(cleanListener);
	}

	private OnClickListener writeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			//get text from edittext
			String savetext = edittext.getText().toString();

			//if sdcard is mounted
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				writeData("abc.txt",savetext,false);
			}
		}
	};
	
	private OnClickListener readListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		    text.setText(readData("abc.txt"));
		}
	};
	
	private OnClickListener cleanListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			writeData("abc.txt","",true);
		}
	};
	
	private void writeData(String filename, String data, boolean clean)
	{
		
		File saveFile = new File(sdCardPath+"/"+filename);
		String str = null;
		readtext="";
		
		//if abc.txt already exists, append savetext to abc.txt
		if(saveFile.exists())
		{
		    InputStream is;
			try {
				is = new FileInputStream(saveFile);
			    InputStreamReader input = new InputStreamReader(is, "UTF-8");
			    BufferedReader reader = new BufferedReader(input);
			    while ((str = reader.readLine()) != null) {
			    	readtext+=str;
			    	readtext+="\n";
			    }
			    data=readtext+data;
			    reader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream outStream;
		String result="";
		try {
			outStream = new FileOutputStream(saveFile);
			if(clean)
			{
				data="";
			}
			outStream.write(data.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			result+=e.toString();
			e.printStackTrace();
		} catch (IOException e) {
			result+=e.toString();
			e.printStackTrace();
		}
		if(result=="")
			if(clean)
				Toast.makeText(getApplicationContext(), "text has been cleared", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "text has been saved in sdcard/abc.txt", Toast.LENGTH_SHORT).show();
				
		else
			Toast.makeText(getApplicationContext(), "error:"+result, Toast.LENGTH_SHORT).show();
	}
	
	private String readData(String filename)
	{
		readtext="";
		String sdCardPath=Environment.getExternalStorageDirectory().getPath();
		File f1 = new File(sdCardPath+"/abc.txt");
		String str = null;
		String result="";
		try {
		    InputStream is = new FileInputStream(f1);
		    InputStreamReader input = new InputStreamReader(is, "UTF-8");
		    BufferedReader reader = new BufferedReader(input);
		    while ((str = reader.readLine()) != null) {
		    	readtext+=str;
		    	readtext+="\n";
		    }
		    reader.close();

		} catch (FileNotFoundException e) {
			result+=e.toString();
		    e.printStackTrace();
		} catch (IOException e) {
			result+=e.toString();
		    e.printStackTrace();
		}
		if(result=="")
			Toast.makeText(getApplicationContext(), "read text from sdcard/abc.txt", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(getApplicationContext(), "error:"+result, Toast.LENGTH_SHORT).show();
		
		return readtext;
	}

}
