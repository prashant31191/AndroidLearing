package com.example.mytabhost;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends FragmentActivity implements
		GestureDetector.OnGestureListener {

	private GestureDetector gestureDetector;
	private int MINDISTANCE = 50;
	private TabHost tabhost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tabhost = (TabHost) findViewById(R.id.tabhost);
		tabhost.setup();

		TabSpec spec1 = tabhost.newTabSpec("TAB 1");
		spec1.setContent(R.id.tab1);
		spec1.setIndicator("TAB 1");

		TabSpec spec2 = tabhost.newTabSpec("TAB 2");
		spec2.setIndicator("TAB 2");
		spec2.setContent(R.id.tab2);

		TabSpec spec3 = tabhost.newTabSpec("TAB 3");
		spec3.setContent(R.id.tab3);
		spec3.setIndicator("TAB 3");

		tabhost.addTab(spec1);
		tabhost.addTab(spec2);
		tabhost.addTab(spec3);

		gestureDetector = new GestureDetector(this, this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent start, MotionEvent finish,
			float velocityX, float velocityY) {
		// start:手势起点的移动事件,finish:当前手势点的移动事件
		// velocityX:每秒x轴方向移动的像素,velocityY:每秒y轴方向移动的像素

		float gapX = start.getRawX() - finish.getRawX();
		float gapY = start.getRawY() - finish.getRawY();
		float distanceX = Math.abs(gapX);
		float distanceY = Math.abs(gapY);

		if (distanceY <= distanceX && distanceX > MINDISTANCE) {
			if (gapX > 0) {
				// left
				tabhost.getCurrentView().startAnimation(
						AnimationUtils
								.loadAnimation(this, R.anim.push_left_out));
				tabhost.setCurrentTab((tabhost.getCurrentTab() + 1) % 3);
				tabhost.getCurrentView()
						.startAnimation(
								AnimationUtils.loadAnimation(this,
										R.anim.push_left_in));
			} else {
				// right
				tabhost.getCurrentView().startAnimation(
						AnimationUtils.loadAnimation(this,
								R.anim.push_right_out));
				tabhost.setCurrentTab((tabhost.getCurrentTab() + 2) % 3);
				tabhost.getCurrentView().startAnimation(
						AnimationUtils
								.loadAnimation(this, R.anim.push_right_in));
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
