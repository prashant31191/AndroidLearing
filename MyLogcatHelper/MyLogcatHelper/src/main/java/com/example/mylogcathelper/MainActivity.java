package com.example.mylogcathelper;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button_start = (Button)findViewById(R.id.button_start);
        Button button_stop = (Button)findViewById(R.id.button_stop);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogcatHelper.getInstance(MainActivity.this).start();

                Log.d("wy","d");
                Log.v("wy","vv");
                Log.e("wy","eee");
                Log.w("wy","wwww");
                Log.i("wy","iiiii");
                throw new  RuntimeException("my exception error");
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogcatHelper.getInstance(MainActivity.this).stop();
            }
        });
    }
}
