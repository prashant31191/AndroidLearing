package com.example.savecrashinfo;


import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	private String s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println(s.equals("any string"));
    }
}
