package com.example.smstest;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("wy","onReceive");
		// 判断是系统短信；
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Log.d("wy","done!");
			// 不再往下传播；
			this.abortBroadcast();
			StringBuffer sb = new StringBuffer();
			String sender = null;
			String content = null;
			String sendtime = null;
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				// 通过pdus获得接收到的所有短信消息，获取短信内容；
				Object[] pdus = (Object[]) bundle.get("pdus");
				// 构建短信对象数组；
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					// 获取单条短信内容，以pdu格式存,并生成短信对象；
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage mge : mges) {
					sb.append("短信来自：" + mge.getDisplayOriginatingAddress()
							+ "\n");
					sb.append("短信内容：" + mge.getMessageBody());

					sender = mge.getDisplayOriginatingAddress();// 获取短信的发送者
					content = mge.getMessageBody();// 获取短信的内容
					Date date = new Date(mge.getTimestampMillis());
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					sendtime = format.format(date);// 获取短信发送时间；
					// SmsManager manager = SmsManager.getDefault();
					// manager.sendTextMessage("5556",
					// null,"发送人:"+sender+"-----发送时间:"+sendtime+"----内容:"+content,
					// null, null);//把拦截到的短信发送到指定的手机，此处为5556;
					// if ("5556".equals(sender)){
					// //屏蔽手机号为5556的短信，这里还可以时行一些处理，如把该信息发送到第三人的手机等等。
					// abortBroadcast();
					// }
				}
				Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}
