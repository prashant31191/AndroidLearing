package com.example.mygesturedetector;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener{
	
	private TextView text;
	private GestureDetector gestureDetector;
	private int MINDISTANCE = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		text = (TextView)findViewById(R.id.text);
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
	public boolean onFling(MotionEvent start, MotionEvent finish, float velocityX,
			float velocityY) {
		//start:手势起点的移动事件,finish:当前手势点的移动事件
		//velocityX:每秒x轴方向移动的像素,velocityY:每秒y轴方向移动的像素
		
		float gapX = start.getRawX() - finish.getRawX();
		float gapY = start.getRawY() - finish.getRawY();
		float distanceX = Math.abs(gapX);
		float distanceY = Math.abs(gapY);

		if (distanceY > distanceX && distanceY > MINDISTANCE) {
			if (gapY > 0) {
				text.setText("up");
			} else {
				text.setText("down");
			}
		} else if(distanceY <= distanceX && distanceX > MINDISTANCE) {
			if (gapX > 0) {
				text.setText("left");
			} else {
				text.setText("right");
			}
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		text.setText("long press");
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		text.setText("press");
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
