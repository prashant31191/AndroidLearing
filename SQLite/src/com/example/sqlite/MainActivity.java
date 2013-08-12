package com.example.sqlite;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private EditText name;
	private EditText age;
	private Button insert;
	private Button update;
	private Button read;
	private TextView readtext;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		name = (EditText)findViewById(R.id.name);
		age = (EditText)findViewById(R.id.age);
		insert = (Button)findViewById(R.id.insert);
		update = (Button)findViewById(R.id.update);
		read = (Button)findViewById(R.id.read);
		readtext = (TextView)findViewById(R.id.readtext);
		
		insert.setOnClickListener(insertListener);
		read.setOnClickListener(readListener);
		update.setOnClickListener(updateListener);
		
		db = openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
		db.execSQL("DROP TABLE IF EXISTS person");
		db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");
	}

	private OnClickListener insertListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String db_name = name.getText().toString();
			String db_age = age.getText().toString();

			//db.execSQL("INSERT INTO person VALUES (NULL, \'"+db_name+"\', \'"+db_age+"\')");
			
			ContentValues cv = new ContentValues();
			cv.put("name",db_name);
			cv.put("age", db_age);
			db.insert("person",null,cv);
		}
	};
	
	private OnClickListener updateListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			String db_name = name.getText().toString();
			String db_age = age.getText().toString();
			ContentValues cv = new ContentValues();

			cv.put("age", db_age);
			db.update("person",cv,"name = ?",new String[]{db_name});
		}
	};

	private OnClickListener readListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			String text = "";
			
			Cursor c = db.rawQuery("SELECT * FROM person",null);
			while(c.moveToNext())
			{
				int _id = c.getInt(c.getColumnIndex("_id"));
				String name = c.getString(c.getColumnIndex("name"));
				int age = c.getInt(c.getColumnIndex("age"));
				text+="_id="+_id+" name="+name+" age="+age+"\n";
			}
			readtext.setText(text);
			c.close();
		}
	};
}
