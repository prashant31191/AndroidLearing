package com.example.spinnerindialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button button;
	private static int index;
	String[] s = { "A", "B", "C", "D", "E" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(clickListener);
	}

	static class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			index = arg2;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			final ArrayAdapter<String> adp = new ArrayAdapter<String>(
					MainActivity.this, android.R.layout.simple_spinner_item, s);

			final Spinner sp = new Spinner(MainActivity.this);
			sp.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			sp.setOnItemSelectedListener(new SpinnerSelectedListener());
			sp.setAdapter(adp);

			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.this);
			builder.setView(sp);

			builder.setTitle("Choose the string you like")
					.setPositiveButton("confirm",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Toast.makeText(MainActivity.this,s[index],
											Toast.LENGTH_SHORT).show();

								}
							}).setNegativeButton("exit", null);
			builder.create().show();

		}

	};

}
