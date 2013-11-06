package com.example.mynotification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class MainActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        Button button_open = (Button)findViewById(R.id.button_open);
        Button button_close = (Button)findViewById(R.id.button_close);
        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification();
            }
        });
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideNotification();
            }
        });
    }

    private void showNotification() {
        Notification noti = new Notification.Builder(mContext)
                .setWhen(System.currentTimeMillis())
                .setTicker("I am Notification")
                .setSmallIcon(R.drawable.ic_launcher)
                .build();
        noti.flags = Notification.FLAG_INSISTENT;

        RemoteViews remoteView = new RemoteViews(this.getPackageName(),R.layout.notify_view);
        remoteView.setTextViewText(R.id.text , "自定义View");
        noti.contentView = remoteView;

        //An Intent is something that is used right now; a PendingIntent is something that may create an Intent in the future.
        PendingIntent contentIntent = PendingIntent.getActivity
                (MainActivity.this, 0,new Intent("android.settings.SETTINGS"), 0);
        noti.contentIntent = contentIntent;

        NotificationManager mnotiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mnotiManager.notify(0, noti);
    }

    private void hideNotification(){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 取消的只是当前Context的Notification
        mNotificationManager.cancel(0);
    }
}
