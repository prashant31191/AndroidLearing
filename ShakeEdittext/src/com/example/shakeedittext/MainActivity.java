package com.example.shakeedittext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button button_shake;
	private EditText edit_shake;
	private TextView text_shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		button_shake = (Button) findViewById(R.id.button_shake);
		edit_shake = (EditText) findViewById(R.id.edit_shake);
		text_shake = (TextView) findViewById(R.id.text_shake);

		button_shake.setOnClickListener(shakeOnListener);

		text_shake.setFocusable(true);
		text_shake.setFocusableInTouchMode(true);
		text_shake.requestFocus();
	}

	private OnClickListener shakeOnListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Animation shakeAnim1;
			shakeAnim1 = AnimationUtils.loadAnimation(MainActivity.this,
					R.anim.shake_x);
			edit_shake.startAnimation(shakeAnim1);
		}
	};

}
