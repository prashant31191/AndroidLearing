package com.example.listviewwithimage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity {
	
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listview = (ListView)findViewById(R.id.listview);
		
		String[] from = new String[]{"imgageview","textview"};
		int[] to = new int[]{R.id.imageview,R.id.textview};
		
		SimpleAdapter adapter = new SimpleAdapter(this,getdata(),R.layout.listitem,from,to);
		listview.setAdapter(adapter);
	}
	
	public List<Map<String,Object>> getdata()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = null;
		for(int i = 0;i < 50;i++){
			map = new HashMap<String,Object>();
			map.put("imgageview", R.drawable.ic_launcher);
			map.put("textview", i);
			list.add(map);
		}
		
		return list;
	}
}
