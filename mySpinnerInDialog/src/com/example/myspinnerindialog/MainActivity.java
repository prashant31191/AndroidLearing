package com.example.myspinnerindialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button button;
	String[] s = { "India ", "Arica", "India ", "Arica", "India ", "Arica",
			"India ", "Arica", "India ", "Arica" };

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

			final ArrayAdapter<String> adp = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_spinner_item, s);

			final Spinner sp = new Spinner(MainActivity.this);
			sp.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			sp.setAdapter(adp);

			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setView(sp);

			builder.setTitle("Choose the string you like")
					.setPositiveButton("confirm",null).setNegativeButton("exit", null);
			builder.create().show();

		}

	};

}
