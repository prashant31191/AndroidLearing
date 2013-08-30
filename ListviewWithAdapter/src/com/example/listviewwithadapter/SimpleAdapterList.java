package com.example.listviewwithadapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SimpleAdapterList extends Activity{

	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrayadapter);
		
		listview = (ListView)findViewById(R.id.listview);
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.list_item2,new String[]{"title","info","img"},new int[]{R.id.title,R.id.info,R.id.img});
		listview.setAdapter(adapter);
	}
	
	private List<Map<String,Object>>getData(){
		List<Map<String,Object>>list = new ArrayList<Map<String,Object>>();
		
		Map<String,Object>map = new HashMap<String,Object>();
		map.put("title", "G1");
		map.put("info","google 1");
		map.put("img",R.drawable.ic_launcher);
		list.add(map);

		map = new HashMap<String,Object>();
		map.put("title", "G2");
		map.put("info","google 2");
		map.put("img",R.drawable.ic_launcher);
		list.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "G3");
		map.put("info","google 3");
		map.put("img",R.drawable.ic_launcher);
		list.add(map);
		
		return list;
	}
	
}
