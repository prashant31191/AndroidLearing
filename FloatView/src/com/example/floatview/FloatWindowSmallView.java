package com.example.floatview;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FloatWindowSmallView extends LinearLayout {

	public static int viewWidth;
	public static int viewHeight;
	private static int statusBarHeight;
	private WindowManager windowManager;
	private WindowManager.LayoutParams params;
	private float xInScreen;
	private float yInScreen;
	private float xInView;
	private float yInView;
	private LinearLayout view;
	private ImageView image;
	private Drawable drawable;
	private int screenWidth;
	private int screenHeight;

	public FloatWindowSmallView(Context context) {
		super(context);

		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);
		view = (LinearLayout) findViewById(R.id.small_window_layout);
		image = (ImageView) view.findViewById(R.id.image);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;

		screenWidth = windowManager.getDefaultDisplay().getWidth();
		screenHeight = windowManager.getDefaultDisplay().getHeight();

		image.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					drawable = getResources().getDrawable(R.drawable.btn_assistivetouch_pressed);
					image.setBackground(drawable);

					xInView = event.getX();
					yInView = event.getY();
					xInScreen = event.getRawX();
					yInScreen = event.getRawY() - getStatusBarHeight();
					break;
				case MotionEvent.ACTION_MOVE:

					xInScreen = event.getRawX();
					yInScreen = event.getRawY() - getStatusBarHeight();
					updateViewPosition();
					break;
				case MotionEvent.ACTION_UP:

					drawable = getResources().getDrawable(R.drawable.btn_assistivetouch);
					image.setBackground(drawable);

					xInScreen = event.getRawX();
					yInScreen = event.getRawY() - getStatusBarHeight();

					moveView();
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	public void setParam(WindowManager.LayoutParams param) {
		params = param;
	}

	private void moveView() {
		float disLeft = image.getX();
		float disRight = screenWidth - image.getX() - image.getWidth();
		float disUp = image.getY();
		float disDown = screenHeight - image.getY() - image.getHeight() - getStatusBarHeight();

		float minDis = Math.min(Math.min(disLeft, disRight), Math.min(disUp, disDown));
		float moveToX = 0.0f;
		float moveToY = 0.0f;
		if (disLeft == minDis) {
			moveToX = 0;
			moveToY = image.getY();
		} else if (disRight == minDis) {
			moveToX = screenWidth - image.getWidth();
			moveToY = image.getY();
		} else if (disUp == minDis) {
			moveToX = image.getX();
			moveToY = 0;
		} else if (disDown == minDis) {
			moveToX = image.getX();
			moveToY = screenHeight - image.getHeight() - getStatusBarHeight();
		}

		image.setX(moveToX);
		image.setY(moveToY);
	}

	private void updateViewPosition() {
		float moveToX = xInScreen - xInView;
		float moveToY = yInScreen - yInView;
		if (moveToY < 0.0f)
			moveToY = 0.0f;
		if (moveToY > screenHeight - getStatusBarHeight() - image.getHeight())
			moveToY = screenHeight - getStatusBarHeight() - image.getHeight();
		if (moveToX < 0.0f)
			moveToX = 0.0f;
		if (moveToX > screenWidth - image.getWidth())
			moveToX = screenWidth - image.getWidth();

		image.setX(moveToX);
		image.setY(moveToY);
	}

	// 获取状态栏的高度
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

}
