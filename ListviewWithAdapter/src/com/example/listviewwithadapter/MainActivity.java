package com.example.listviewwithadapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btn_ArrayAdapter;
	private Button btn_ArrayAdapter2;
	private Button btn_SimpleAdapter;
	private Button btn_BaseAdapter;
	private Button btn_BaseAdapter2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_ArrayAdapter = (Button)findViewById(R.id.btn_ArrayAdapter);
		btn_ArrayAdapter2 = (Button)findViewById(R.id.btn_ArrayAdapter2);
		btn_SimpleAdapter = (Button)findViewById(R.id.btn_SimpleAdapter);
		btn_BaseAdapter = (Button)findViewById(R.id.btn_BaseAdapter);
		btn_BaseAdapter2 = (Button)findViewById(R.id.btn_BaseAdapter2);

		btn_ArrayAdapter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ArrayAdapterList.class);
				startActivity(intent);
			}
		});
		
		btn_ArrayAdapter2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ArrayAdapterList2.class);
				startActivity(intent);
			}
		});
		
		btn_SimpleAdapter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SimpleAdapterList.class);
				startActivity(intent);
			}
		});

		btn_BaseAdapter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BaseAdapterList.class);
				startActivity(intent);
			}
		});
		
		btn_BaseAdapter2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, BaseAdapterList2.class);
				startActivity(intent);
			}
		});
	}
}
