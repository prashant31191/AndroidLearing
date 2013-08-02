package com.example.writedatatosdcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
	private String savetext;
	private String readtext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		edittext = (EditText) findViewById(R.id.edittext);
		text = (TextView) findViewById(R.id.textview);
		btn_read = (Button) findViewById(R.id.btn_read);
		btn_write = (Button) findViewById(R.id.btn_write);

		btn_read.setOnClickListener(readListener);
		btn_write.setOnClickListener(writeListener);
	}

	private OnClickListener writeListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			savetext = edittext.getText().toString();

			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				File saveFile = new File(sdCardDir, "abc.txt");
				FileOutputStream outStream;
				String result="";
				try {
					outStream = new FileOutputStream(saveFile);
					outStream.write(savetext.getBytes());
					outStream.close();
				} catch (FileNotFoundException e) {
					result+=e.toString();
					e.printStackTrace();
				} catch (IOException e) {
					result+=e.toString();
					e.printStackTrace();
				}
				if(result=="")
					Toast.makeText(getApplicationContext(), "text has been saved in sdcard/abc.txt", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(getApplicationContext(), "error:"+result, Toast.LENGTH_SHORT).show();
			}
		}
	};

	private OnClickListener readListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
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
			    text.setText(readtext);

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
		}
	};

}
