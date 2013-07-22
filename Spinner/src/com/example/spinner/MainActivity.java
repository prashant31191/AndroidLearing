package com.example.spinner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Spinner sp;
	private static final String[] m = { "A", "B", "O", "AB", "other" };
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sp = (Spinner) findViewById(R.id.sp);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,m);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp.setAdapter(adapter);
		sp.setOnItemSelectedListener(new SpinnerSelectedListener());
		sp.setVisibility(View.VISIBLE);
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{
		
		public void onItemSelected(AdapterView<?>arg0,View arg1,int arg2,long arg3){
			Toast.makeText(getApplicationContext(), m[arg2], Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			
		}
		
	}

}
