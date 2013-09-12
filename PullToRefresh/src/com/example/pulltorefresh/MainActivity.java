package com.example.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import com.example.pulltorefresh.MyListview.OnRefreshListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private LinkedList<String> mListItems;
	private MyListview listview;
	private ImageView image_refresh;
	private ImageView image_back;
	private Animation myAnimation_Rotate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (MyListview) findViewById(R.id.listview);
		image_refresh = (ImageView) findViewById(R.id.image_refresh);
		image_back = (ImageView)findViewById(R.id.image_back);
		myAnimation_Rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);

		// 下拉松开后执行GetDataTask，两秒后添加新的view
		listview.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new GetDataTask().execute();
			}
		});

		// 点击刷新图片后开始刷新
		image_refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listview.clickRefresh();
				listview.smoothScrollToPosition(0);
				image_refresh.startAnimation(myAnimation_Rotate);
			}
		});
		
		image_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mStrings));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mListItems);

		listview.setAdapter(adapter);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				;
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mListItems.addFirst("Added after refresh...");
			listview.onRefreshComplete(getTime());
			super.onPostExecute(result);
		}
	}

	public String getTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return "上次刷新时间:" + str;
	}

	private String[] mStrings = { "Life in Technicolor",
			"Cemeteries of London", "Lost!", "42",
			"Lovers in Japan/Reign of Love", "Yes", "Viva la Vida",
			"Violet Hill", "Strawberry Swing", "Death and All His Friends",
			"Lost? (Japan and iTunes)",
			"Lovers in Japan (Acoustic Version) (iTunes pre-order)",
			"Death Will Never Conquer (Japanese release of Prospekt's March edition)" };
}
