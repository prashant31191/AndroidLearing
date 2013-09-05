package com.example.animation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener{
	
	private Animation myAnimation_Alpha;
	private Animation myAnimation_Scale;
	private Animation myAnimation_Translate;
	private Animation myAnimation_Rotate;

	private Button button_alpha;
	private Button button_scale;
	private Button button_translate;
	private Button button_rotate;
	
	private ImageView ball;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		myAnimation_Alpha= AnimationUtils.loadAnimation(this,R.anim.alpha);
		myAnimation_Scale= AnimationUtils.loadAnimation(this,R.anim.scale);
		myAnimation_Translate= AnimationUtils.loadAnimation(this,R.anim.translate);
		myAnimation_Rotate= AnimationUtils.loadAnimation(this,R.anim.rotate);

		button_alpha = (Button)findViewById(R.id.button_alpha);
		button_scale = (Button)findViewById(R.id.button_scale);
		button_translate = (Button)findViewById(R.id.button_translate);
		button_rotate = (Button)findViewById(R.id.button_rotate);
		ball = (ImageView)findViewById(R.id.ball);

		button_alpha.setOnClickListener(this);
		button_scale.setOnClickListener(this);
		button_translate.setOnClickListener(this);
		button_rotate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_alpha:
			ball.startAnimation(myAnimation_Alpha);
			break;
		case R.id.button_scale:
			ball.startAnimation(myAnimation_Scale);
			break;
		case R.id.button_translate:
			ball.startAnimation(myAnimation_Translate);
			break;
		case R.id.button_rotate:
			ball.startAnimation(myAnimation_Rotate);
			break;
		}
	}
}
