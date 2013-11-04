package com.example.floatview;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class MainActivity extends Activity {

	private static FloatWindowSmallView smallWindow;
	private static LayoutParams smallWindowParams;
	private static WindowManager mWindowManager;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createSmallWindow(getApplicationContext());
		finish();
	}

	public static void createSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (smallWindowParams == null) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.type = LayoutParams.TYPE_PHONE;	//该类型提供与用户交互，置于所有应用程序上方，但是在状态栏后面，即悬浮窗
				smallWindowParams.format = PixelFormat.RGBA_8888;	//Android设备display默认是采用16-bits color palette来表示所有颜色，因此对于带alpha值的32位png图片会出现显示失真。设置需要显示Activity的PixelFormat可以解决这个问题
//				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight / 2;
			}
			smallWindow.setParam(smallWindowParams);
			windowManager.addView(smallWindow, smallWindowParams);
		}
	}

	public static void removeSmallWindow(Context context) {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
	}

	public static boolean isWindowShowing() {
		return smallWindow != null;
	}

	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
}
