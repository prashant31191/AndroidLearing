package com.example.httppost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread(new Runnable() {
			@Override
			public void run() {
				String uriAPI = "http://posttestserver.com/post.php";

				HttpPost httpRequest = new HttpPost(uriAPI);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("id", "12345"));
				params.add(new BasicNameValuePair("stringdata",
						"stringdata!"));

				try {

					httpRequest.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);

					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String strResult = EntityUtils.toString(httpResponse
								.getEntity());
						Log.d("wy", "strResult=" + strResult);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

}
