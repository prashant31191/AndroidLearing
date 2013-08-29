package com.example.asynctask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private Button download;
	private ProgressBar pb;
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pb = (ProgressBar)findViewById(R.id.pb);
		text = (TextView)findViewById(R.id.tv);
		download = (Button)findViewById(R.id.download);
		
		download.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DownloadTask dTask = new DownloadTask();
				dTask.execute(100);
			}
		});
	}
	
	//后面尖括号内分别是参数（例子里是线程休息时间），进度(publishProgress用到)，返回值 类型
	class DownloadTask extends AsyncTask<Integer, Integer, String>
	{
		@Override
		protected void onPreExecute() {
			//第一个执行方法 
			Log.d("wy","onPreExecute");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			//第二个执行方法,onPreExecute()执行完后执行
			Log.d("wy","doInBackground");
			for(int i=0;i<=100;i++)
			{
				pb.setProgress(i);
				publishProgress(i);
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return "done";
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			//这个函数在doInBackground调用publishProgress时触发，虽然调用时只有一个参数  
            //但是这里取到的是一个数组,所以要用progesss[0]来取值  
            //第n个参数就用progress[n]来取值  
			text.setText(values[0]+"%");
			Log.d("wy","onProgressUpdate");
			super.onProgressUpdate(values);
		}
	
		@Override
		protected void onPostExecute(String result) {
			Log.d("wy","onPostExecute");
			setTitle(result);
			super.onPostExecute(result);
		}
	}
}
