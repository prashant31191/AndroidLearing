package com.example.downloadimagetolistview;

import android.os.AsyncTask;
import android.util.Log;

public class AsyncDownloadTask extends AsyncTask<Integer, Integer, String> {
	
	@Override
	protected void onPreExecute() {
		// ��һ��ִ�з���
		Log.d("wy", "onPreExecute");
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Integer... params) {
		// �ڶ���ִ�з���,onPreExecute()ִ�����ִ��
		Log.d("wy", "doInBackground");
		return "done";
	}

	@Override
	protected void onPostExecute(String result) {
		Log.d("wy", "onPostExecute");
		super.onPostExecute(result);
	}

}
