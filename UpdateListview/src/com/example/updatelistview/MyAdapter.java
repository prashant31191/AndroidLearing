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
		//listView�ڿ�ʼ���Ƶ�ʱ��ϵͳ���ȵ���getCount()�������������ķ���ֵ�õ�listView�ĳ���
		return MainActivity.data.size();
	}

	@Override
	public Object getItem(int position) {
		// Get the data item associated with the specified position in the data set.(��ȡ���ݼ�����ָ��������Ӧ��������)
		return MainActivity.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// Get the row id associated with the specified position in the list.(ȡ���б�����ָ��������Ӧ����id)
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//��getCount��ȡ�����Ⱥ󣬵���getView()��һ����ÿһ��
		
		//�������ж� convertViewΪ�յ�ʱ�򣬾ͻ������ƺõ�List��Item���֣�XML������ΪconvertView��ֵ��������һ��viewHolder����converView����ĸ���View�ؼ���XML�����������Щ�ؼ�����
		//����convertView��setTag��viewHolder���õ�Tag�У��Ա�ϵͳ�ڶ��λ���ListViewʱ��Tag��ȡ����
		//���convertView��Ϊ�յ�ʱ�򣬾ͻ�ֱ����convertView��getTag()�������һ��ViewHolder��
		
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
