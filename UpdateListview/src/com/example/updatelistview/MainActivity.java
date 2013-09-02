package com.example.updatelistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listview;
	private Button btn_add;
	private Button btn_delete;
	private Button btn_update;
	private EditText edittext_deletenumber;
	private EditText edittext_updatenumber;
	private EditText edittext_updatetitle;
	
	public static List<Map<String,Object>> data;
	private MyAdapter adapter;
	
	private int cnt;
	private int linenumber;
	private String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		cnt=3;

		listview = (ListView)findViewById(R.id.listview);
		btn_add = (Button)findViewById(R.id.btn_add);
		btn_delete = (Button)findViewById(R.id.btn_delete);
		btn_update = (Button)findViewById(R.id.btn_update);
		edittext_deletenumber = (EditText)findViewById(R.id.linenumber);
		edittext_updatenumber = (EditText)findViewById(R.id.linenumber2);
		edittext_updatetitle = (EditText)findViewById(R.id.title);
		
		data = getData();
		adapter = new MyAdapter(this);
		listview.setAdapter(adapter);

		btn_add.setOnClickListener(addListener);
		btn_update.setOnClickListener(updateListener);
		btn_delete.setOnClickListener(deleteListener);
	}
	
	private OnClickListener updateListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			linenumber = Integer.valueOf(edittext_updatenumber.getText().toString());
			title = edittext_updatetitle.getText().toString();

			Map<String,Object> map = (HashMap<String, Object>) adapter.getItem(linenumber);
			map.put("title",title);
			adapter.notifyDataSetChanged();
			
		}
	};
	
	private OnClickListener addListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Map<String,Object> map;
			map = new HashMap<String,Object>();
			map.put("img", R.drawable.ic_launcher);
			map.put("title","title "+cnt);
			map.put("info","info "+cnt);
			data.add(map);
			adapter.notifyDataSetChanged();		//通知listview更新数据
			cnt++;
		}
	};
	
	private OnClickListener deleteListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			linenumber = Integer.valueOf(edittext_deletenumber.getText().toString());
			data.remove(linenumber);
			adapter.notifyDataSetChanged();
		}
	};

	private List<Map<String,Object>> getData()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map;
		for(int i=0;i<3;i++)
		{
			map = new HashMap<String,Object>();
			map.put("img", R.drawable.ic_launcher);
			map.put("title","title "+i);
			map.put("info","info "+i);
			list.add(map);
		}
		return list;
	}
}
