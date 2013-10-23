package com.example.qqfriendgroup;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

class MyAdapter extends BaseExpandableListAdapter {

	private List<List<Map<String, Object>>> ChildrenData;
	private List<Map<String, Object>> GroupData;
	private Context context;

	public MyAdapter(List<List<Map<String, Object>>> childrendata, List<Map<String, Object>> groupdata) {
		ChildrenData = childrendata;
		GroupData = groupdata;
		context = MainActivity.mContext;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return ChildrenData.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.listview_friends, null);

		TextView text_username = (TextView) relativeLayout.findViewById(R.id.text_username);
		text_username.setText((String) ChildrenData.get(groupPosition).get(childPosition).get("username"));

		TextView text_signature = (TextView) relativeLayout.findViewById(R.id.text_signature);
		String signature = (String) ChildrenData.get(groupPosition).get(childPosition).get("signature");
		if(signature.length()>=20){
			signature = signature.substring(0, 20);
			signature += "...";
		}
		text_signature.setText(signature);

		TextView text_status = (TextView) relativeLayout.findViewById(R.id.text_status);
		Boolean isonline = (Boolean) ChildrenData.get(groupPosition).get(childPosition).get("isonline");
		if (isonline)
			text_status.setText("[‘⁄œﬂ] ");
		else
			text_status.setText("[¿Îœﬂ] ");

		ImageView image_avatar = (ImageView) relativeLayout.findViewById(R.id.image_avatar);
		int id_avatar = (Integer) ChildrenData.get(groupPosition).get(childPosition).get("avatar");
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id_avatar);
		
		if (!isonline) {
//			bitmap = toGrayscale(bitmap);
			bitmap = toLessGrayscale(bitmap);
		}
		image_avatar.setImageBitmap(bitmap);
//		if(!isonline){
//			float xx[] = {0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f};
//			image_avatar.setColorFilter(new ColorMatrixColorFilter(xx));
//			image_avatar.invalidate();
//		}

		if (isonline) {
			ImageView image_status = (ImageView) relativeLayout.findViewById(R.id.image_status);
			image_status.setVisibility(View.VISIBLE);
			int id_status = (Integer) ChildrenData.get(groupPosition).get(childPosition).get("status");
			bitmap = BitmapFactory.decodeResource(context.getResources(), id_status);
			image_status.setImageBitmap(bitmap);
		}else{
			ImageView image_status = (ImageView) relativeLayout.findViewById(R.id.image_status);
			image_status.setVisibility(View.GONE);
		}

		return relativeLayout;
	}

	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
	
	public static Bitmap toLessGrayscale(Bitmap bmpOriginal) {
		
		float xx[] = {0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f,0.0f,0.0f,0.0f,0.0f,0.5f,0.0f};
		
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		
		ColorMatrix cm = new ColorMatrix(xx);
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		
		Paint paint = new Paint();
		paint.setColorFilter(f);
		
		Canvas c = new Canvas(bmpGrayscale);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ChildrenData.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return GroupData.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return GroupData.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout relativeLayout = (RelativeLayout) layoutInflater.inflate(R.layout.listview_groups, null);

		TextView groupname = (TextView) relativeLayout.findViewById(R.id.groupname);
		groupname.setText((String) GroupData.get(groupPosition).get("groupname"));

		TextView groupnum = (TextView) relativeLayout.findViewById(R.id.groupnum);
		groupnum.setText((String) GroupData.get(groupPosition).get("groupnum"));

		ImageView image = (ImageView) relativeLayout.findViewById(R.id.image_play);
		Drawable draw = context.getResources().getDrawable((Integer) GroupData.get(groupPosition).get("img"));
		image.setBackground(draw);

		return relativeLayout;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}