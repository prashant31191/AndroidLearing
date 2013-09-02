package com.example.downloadimagetolistview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter  extends BaseAdapter {
	
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
	public View getView(int pos, View convertView, ViewGroup parent) {
		//��getCount��ȡ�����Ⱥ󣬵���getView()��һ����ÿһ��
		
		//�������ж� convertViewΪ�յ�ʱ�򣬾ͻ������ƺõ�List��Item���֣�XML������ΪconvertView��ֵ��������һ��viewHolder����converView����ĸ���View�ؼ���XML�����������Щ�ؼ�����
		//����convertView��setTag��viewHolder���õ�Tag�У��Ա�ϵͳ�ڶ��λ���ListViewʱ��Tag��ȡ����
		//���convertView��Ϊ�յ�ʱ�򣬾ͻ�ֱ����convertView��getTag()�������һ��ViewHolder��
		
		ViewHolder holder = null;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem,null);
			holder.list_image = (ImageView)convertView.findViewById(R.id.list_image);
			holder.title = (TextView)convertView.findViewById(R.id.title);
			holder.artist = (TextView)convertView.findViewById(R.id.artist);
			holder.duration = (TextView)convertView.findViewById(R.id.duration);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.list_image.setBackground((Drawable)MainActivity.data.get(pos).get("list_image"));
		holder.title.setText((String)MainActivity.data.get(pos).get("title"));
		holder.artist.setText((String)MainActivity.data.get(pos).get("artist"));
		holder.duration.setText((String)MainActivity.data.get(pos).get("duration"));
		
		return convertView;
	}
	
    static class ViewHolder
    {
        public ImageView list_image;
        public TextView title;
        public TextView artist;
        public TextView duration;
    }

}
