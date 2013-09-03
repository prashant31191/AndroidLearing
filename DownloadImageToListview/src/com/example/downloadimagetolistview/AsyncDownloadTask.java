package com.example.downloadimagetolistview;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncDownloadTask extends AsyncTask<Integer, Integer, String> {
	
	@Override
	protected void onPreExecute() {
		// 第一个执行方法
		Log.d("wy", "onPreExecute");
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Integer... params) {
		// 第二个执行方法,onPreExecute()执行完后执行
		Log.d("wy", "doInBackground");
		return "done";
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d("wy", "onPostExecute");
		super.onPostExecute(result);
	}

}
