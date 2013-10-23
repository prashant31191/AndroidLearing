package com.example.qqfriendgroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private List<List<Map<String, Object>>> ChildrenData;
	private List<Map<String, Object>> GroupData;

	private ExpandableListView listview;
	public static Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listview = (ExpandableListView) findViewById(R.id.listview);

		mContext = getApplicationContext();
		getData();

		final MyAdapter adapter = new MyAdapter(ChildrenData, GroupData);
		listview.setAdapter(adapter);

		listview.setGroupIndicator(null);

		listview.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {
				GroupData.get(groupPosition).put("img", R.drawable.play_normal);
				adapter.notifyDataSetChanged();
			}
		});
		listview.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				GroupData.get(groupPosition).put("img", R.drawable.play_open);
				adapter.notifyDataSetChanged();
			}
		});
		listview.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				TextView title = (TextView) v.findViewById(R.id.text_username);
				Toast.makeText(mContext, "click on " + title.getText(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}

	private void getData() {
		GroupData = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.play_normal);
		map.put("groupname", "高中同学 ");
		map.put("groupnum", "2/5 ");
		GroupData.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.play_normal);
		map.put("groupname", "大学同学");
		map.put("groupnum", "1/4 ");
		GroupData.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.play_normal);
		map.put("groupname", "同事");
		map.put("groupnum", "6/7 ");
		GroupData.add(map);

		ChildrenData = new ArrayList<List<Map<String, Object>>>();
		List<Map<String, Object>> Child1 = new ArrayList<Map<String, Object>>();
		for (char name = 'a'; name <= 'e'; name++) {
			map = new HashMap<String, Object>();
			map.put("username", "高中同学  " + name);
			map.put("avatar", R.drawable.qq);
			if (name <= 'b') {
				map.put("isonline", true);
				if (name == 'a')
					map.put("status", R.drawable.user_icon_pc);
				else
					map.put("status", R.drawable.user_icon_phone);
			} else {
				map.put("isonline", false);
			}
			map.put("signature", "我是高中同学 " + name);
			Child1.add(map);
		}
		ChildrenData.add(Child1);

		List<Map<String, Object>> Child2 = new ArrayList<Map<String, Object>>();
		for (char name = 'a'; name <= 'd'; name++) {
			map = new HashMap<String, Object>();
			map.put("username", "大学同学  " + name);
			map.put("avatar", R.drawable.qq);
			if (name <= 'a') {
				map.put("isonline", true);
				if (name <= 'b')
					map.put("status", R.drawable.user_icon_pc);
				else
					map.put("status", R.drawable.user_icon_phone);
			} else
				map.put("isonline", false);
			map.put("signature", "我是大学同学 " + "aaaaaaaaaaaaaaaaaaaaaaaaa");
			Child2.add(map);
		}
		ChildrenData.add(Child2);

		List<Map<String, Object>> Child3 = new ArrayList<Map<String, Object>>();
		for (char name = 'a'; name <= 'g'; name++) {
			map = new HashMap<String, Object>();
			map.put("username", "同事  " + name);
			map.put("avatar", R.drawable.qq);
			if (name <= 'f') {
				map.put("isonline", true);
				if (name <= 'c')
					map.put("status", R.drawable.user_icon_pc);
				else
					map.put("status", R.drawable.user_icon_phone);
			} else
				map.put("isonline", false);
			map.put("signature", "我是同事 " + name);
			Child3.add(map);
		}
		ChildrenData.add(Child3);
	}
}
