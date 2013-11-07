package com.example.myalarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadcastListener extends BroadcastReceiver{
	
	public static String referrer;

	@Override
	public void onReceive(Context context, Intent intent) {

        referrer = intent.getStringExtra("sendinfo");
        Toast.makeText(context, "broadcast:"+referrer, Toast.LENGTH_LONG).show();
	}

}
