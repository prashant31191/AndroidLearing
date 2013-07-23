package com.example.sendbroadcast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(clickListener);
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent it = new Intent("com.example.sendbroadcast.broadcast");
			it.putExtra("sendinfo", "greetings!");
			sendBroadcast(it);
		}
	};

}
