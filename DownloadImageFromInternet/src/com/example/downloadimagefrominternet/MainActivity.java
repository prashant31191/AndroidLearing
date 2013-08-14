package com.example.downloadimagefrominternet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView image;

	private final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/wy_test/";
	private Bitmap mBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		image = (ImageView) findViewById(R.id.image);
		new Thread(connectNet).start();
	}

	private Runnable connectNet = new Runnable() {
		@Override
		public void run() {
			try {
				String url = "http://img.my.csdn.net/uploads/201211/21/1353511891_4579.jpg";
				//方法1
				mBitmap = BitmapFactory.decodeStream(getImageStream(url));
				//方法2
				/*byte[] data = getImage(url);  
                if(data!=null){  
                    mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);		// bitmap  
                }else{
                    Log.d("wy", "Image error!");
                } */

				// 发送消息，通知handler在主线程中更新UI
				connectHanlder.sendEmptyMessage(0);
				saveFile(mBitmap, "test.jpg");
				Log.d("wy", "set image ...");
			} catch (Exception e) {
				e.printStackTrace();
				Log.d("wy",e.toString());
			}
		}
	};

	private Handler connectHanlder = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 更新UI，显示图片
			if (mBitmap != null) {
				image.setImageBitmap(mBitmap);
			}
		}
	};
	

	public void saveFile(Bitmap bitmap, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);	//80 是压缩率，表示压缩20%; 如果不压缩是100，表示压缩率为0  
		bos.flush();
		bos.close();
	}

	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);		//设置连接的时限为5秒
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}
	
	public byte[] getImage(String path) throws Exception{  
        URL url = new URL(path);  
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
        conn.setConnectTimeout(5 * 1000);  
        conn.setRequestMethod("GET");  
        
        InputStream inStream = conn.getInputStream();
        
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){  
            return readStream(inStream);
        }  
        return null;  
    }
	
	public static byte[] readStream(InputStream inStream) throws Exception{  
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while( (len=inStream.read(buffer)) != -1){  
            outStream.write(buffer, 0, len);  
        }  
        outStream.close();  
        inStream.close();  
        return outStream.toByteArray();  
    } 
}
