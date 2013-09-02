package com.example.updatelistview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater = null;
	
	public MyAdapter(Context context){
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		//listView在开始绘制的时候，系统首先调用getCount()函数，根据他的返回值得到listView的长度
		return MainActivity.data.size();
	}

	@Override
	public Object getItem(int position) {
		// Get the data item associated with the specified position in the data set.(获取数据集中与指定索引对应的数据项)
		return MainActivity.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//在getCount获取到长度后，调用getView()逐一绘制每一行
		
		//当我们判断 convertView为空的时候，就会根据设计好的List的Item布局（XML），来为convertView赋值，并生成一个viewHolder来绑定converView里面的各个View控件（XML布局里面的那些控件）。
		//再用convertView的setTag将viewHolder设置到Tag中，以便系统第二次绘制ListView时从Tag中取出。
		//如果convertView不为空的时候，就会直接用convertView的getTag()，来获得一个ViewHolder。
		
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item,null);
			holder.img = (ImageView)convertView.findViewById(R.id.img);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.info = (TextView)convertView.findViewById(R.id.info);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.img.setBackgroundResource((Integer)MainActivity.data.get(position).get("img"));
		holder.title.setText((String)MainActivity.data.get(position).get("title"));
		holder.info.setText((String)MainActivity.data.get(position).get("info"));
		
		return convertView;
	}
	
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
    }

}
