package com.example.listviewwithadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter2 extends BaseAdapter{
	
	private LayoutInflater mInflater = null;
	private Context mContext;
	
	public MyAdapter2(Context context){
		this.mInflater = LayoutInflater.from(context);
		mContext=context;
	}

	@Override
	public int getCount() {
		// How many items are in the data set represented by this Adapter.(在此适配器中所代表的数据集中的条目数)
		return BaseAdapterList2.data.size();
	}

	@Override
	public Object getItem(int position) {
		// Get the data item associated with the specified position in the data set.(获取数据集中与指定索引对应的数据项)
		return position;
	}

	@Override
	public long getItemId(int position) {
		// Get the row id associated with the specified position in the list.(取在列表中与指定索引对应的行id)
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//当我们判断 convertView为空的时候，就会根据设计好的List的Item布局（XML），来为convertView赋值，并生成一个viewHolder来绑定converView里面的各个View控件（XML布局里面的那些控件）。
		//再用convertView的setTag将viewHolder设置到Tag中，以便系统第二次绘制ListView时从Tag中取出。
		//如果convertView不为空的时候，就会直接用convertView的getTag()，来获得一个ViewHolder。
		
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.list_item3,null);
			holder.img = (ImageView)convertView.findViewById(R.id.img);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.info = (TextView)convertView.findViewById(R.id.info);
			holder.button = (Button)convertView.findViewById(R.id.button);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.img.setBackgroundResource((Integer)BaseAdapterList2.data.get(position).get("img"));
		holder.title.setText((String)BaseAdapterList2.data.get(position).get("title"));
		holder.info.setText((String)BaseAdapterList2.data.get(position).get("info"));
		holder.button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "Hi! I'm a toast.", Toast.LENGTH_SHORT).show()
;			}
		});
		
		return convertView;
	}
	
    static class ViewHolder
    {
        public ImageView img;
        public TextView title;
        public TextView info;
        public Button button;
    }

}
