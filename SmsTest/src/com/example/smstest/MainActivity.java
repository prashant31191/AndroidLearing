package com.example.smstest;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button button;
	private EditText number;
	private EditText send_text;
	private String num;
	private String text;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button);
		number = (EditText) findViewById(R.id.number);
		send_text = (EditText) findViewById(R.id.send_text);
		button.setOnClickListener(clickListener);

		mContext = getApplicationContext();

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String SENT_SMS_ACTION = "SENT_SMS_ACTION";
			Intent sentIntent = new Intent(SENT_SMS_ACTION);
			PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0,
					sentIntent, 0);
			// register the Broadcast Receivers
			mContext.registerReceiver(new BroadcastReceiver() {
				@Override
				public void onReceive(Context _context, Intent _intent) {
					switch (getResultCode()) {
					case Activity.RESULT_OK:
						Toast.makeText(mContext, "SMS was sent successfully",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(mContext, "SMS send failed",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
			}, new IntentFilter(SENT_SMS_ACTION));

			num = number.getText().toString();
			text = send_text.getText().toString();
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(num, null, text, sentPI, null);
		}
	};

}
