package com.example.myviewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ViewPager viewpager; // 页卡内容
	private PagerTabStrip pagerTabStrip;
	private List<View> listview; // Tab页面列表
	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private Drawable draw_image1;
	private Drawable draw_image2;
	private List<String> titleList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstuse);
		
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		image1 = (ImageView)findViewById(R.id.image1);
		image2 = (ImageView)findViewById(R.id.image2);
		image3 = (ImageView)findViewById(R.id.image3);
		draw_image1 = getResources().getDrawable(R.drawable.image1);
		draw_image2 = getResources().getDrawable(R.drawable.image2);

		pagerTabStrip=(PagerTabStrip) findViewById(R.id.pagerTabStrip);
		pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.violet)); 
		pagerTabStrip.setDrawFullUnderline(false);
		pagerTabStrip.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTabStrip.setTextSpacing(50);
		
		listview = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listview.add(mInflater.inflate(R.layout.layout1, null));
		listview.add(mInflater.inflate(R.layout.layout2, null));
		listview.add(mInflater.inflate(R.layout.layout3, null));
		
		viewpager.setAdapter(new MyPagerAdapter(listview));
		
		titleList = new ArrayList<String>();// 每个页面的Title数据
		titleList.add("title1");
		titleList.add("title2");
		titleList.add("title3");

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(final int arg0) {
				new Handler().postDelayed(new Runnable(){  
				    public void run() {  
						updateMark(arg0);
				    }  
				 }, 300);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	void updateMark(int index){
		switch (index) {
		case 0:
			image1.setImageDrawable(draw_image2);
			image2.setImageDrawable(draw_image1);
			image3.setImageDrawable(draw_image1);
			break;
		case 1:
			image1.setImageDrawable(draw_image1);
			image2.setImageDrawable(draw_image2);
			image3.setImageDrawable(draw_image1);
			break;
		case 2:
			image1.setImageDrawable(draw_image1);
			image2.setImageDrawable(draw_image1);
			image3.setImageDrawable(draw_image2);
			break;
		default:
			break;
		}
	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titleList.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
