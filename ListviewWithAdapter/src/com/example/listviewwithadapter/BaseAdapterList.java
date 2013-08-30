package com.example.listviewwithadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class BaseAdapterList extends Activity{


	private ListView listview;
	public static List<Map<String,Object>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrayadapter);
		
		listview = (ListView)findViewById(R.id.listview);
		data = getData();
		MyAdapter adapter = new MyAdapter(this);
		listview.setAdapter(adapter);
	}
	
	private List<Map<String,Object>> getData()
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object>map;
		for(int i=0;i<10;i++)
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
