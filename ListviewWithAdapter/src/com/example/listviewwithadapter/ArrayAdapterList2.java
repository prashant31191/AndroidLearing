package com.example.listviewwithadapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArrayAdapterList2 extends Activity{
	
	private ListView listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arrayadapter);
		
		listview = (ListView)findViewById(R.id.listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item,R.id.textview,getData());
		listview.setAdapter(adapter);
	}
	
	private List<String> getData()
	{
		List<String>list = new ArrayList<String>();
		list.add("test 1");
		list.add("test 2");
		list.add("test 3");
		list.add("test 4");
		return list;
	}

}
