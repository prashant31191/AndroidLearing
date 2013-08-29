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
	
	//����������ڷֱ��ǲ��������������߳���Ϣʱ�䣩������(publishProgress�õ�)������ֵ ����
	class DownloadTask extends AsyncTask<Integer, Integer, String>
	{
		@Override
		protected void onPreExecute() {
			//��һ��ִ�з��� 
			Log.d("wy","onPreExecute");
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {
			//�ڶ���ִ�з���,onPreExecute()ִ�����ִ��
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
			//���������doInBackground����publishProgressʱ��������Ȼ����ʱֻ��һ������  
            //��������ȡ������һ������,����Ҫ��progesss[0]��ȡֵ  
            //��n����������progress[n]��ȡֵ  
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
