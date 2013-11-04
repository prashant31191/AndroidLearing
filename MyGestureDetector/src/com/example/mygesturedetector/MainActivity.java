package com.example.mygesturedetector;

import java.util.ArrayList;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends Activity implements OnGestureListener {

	private TextView text;
	private GestureDetector gestureDetector;
	private GestureOverlayView gestureView;
	private int MINDISTANCE = 50;
	private GestureLibrary gestureLibrary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		text = (TextView) findViewById(R.id.text);
		gestureDetector = new GestureDetector(this, this);
		
		gestureView = (GestureOverlayView) findViewById(R.id.gesture);  
        // �������ƵĻ�����ɫ  
        gestureView.setGestureColor(Color.BLUE);
        // �������ƵĻ��ƿ��  
        gestureView.setGestureStrokeWidth(4);  
        // Ϊgesture����������¼����¼�������  

		gestureLibrary = GestureLibraries.fromRawResource(this,
				R.raw.gestures);
		if (gestureLibrary.load()) {
			
			gestureView.addOnGesturePerformedListener(new OnGesturePerformedListener(){
				@Override
				public void onGesturePerformed(GestureOverlayView overlay,
						Gesture gesture) {
					
				       ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
				       if(predictions.size()>0){
				          for(int i=0;i<predictions.size();i++)
				          {
				              Prediction prediction = (Prediction) predictions.get(i);
				              if(prediction.score>1.0)
				              {
				                 text.setText(prediction.name);
				                 break;
				              }
				          }
				       }
				   }
			});
		}
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
		// start:���������ƶ��¼�,finish:��ǰ���Ƶ���ƶ��¼�
		// velocityX:ÿ��x�᷽���ƶ�������,velocityY:ÿ��y�᷽���ƶ�������

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
		} else if (distanceY <= distanceX && distanceX > MINDISTANCE) {
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
