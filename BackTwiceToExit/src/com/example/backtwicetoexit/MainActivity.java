package com.example.backtwicetoexit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends Activity {

	private boolean doubleBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		doubleBack=false;
	}

	@Override
	public void onBackPressed() {
		if (doubleBack) {
			super.onBackPressed();
			return;
		}
		doubleBack = true;
		Toast.makeText(this, "Please click BACK again to exit",
				Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBack = false;
			}
		}, 2000);
	}

}
