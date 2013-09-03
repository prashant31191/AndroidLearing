package com.example.downloadimagetolistview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class Util {
	public static final String SAVE_PATH = Environment
			.getExternalStorageDirectory() + "/DownloadImagesFromListview/";

	public static void saveimage(Bitmap mBitmap, String fileName) throws IOException {
		File f = new File(SAVE_PATH + fileName);
		try {

			f.createNewFile();
			FileOutputStream fOut = null;
			fOut = new FileOutputStream(f);
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public static Bitmap getimagefromurl(String path) {
		try {
			Bitmap mBitmap;
			String filename = Util.md5(path) + ".png";
			File file = new File(Util.SAVE_PATH + filename);
			if (file.exists()) {
				return Util.ReadFromSdcard(filename);
			} else {
				URL url = new URL(path);
				HttpURLConnection conn;
				conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5 * 1000); // 设置连接的时限为5秒
				conn.setRequestMethod("GET");

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					mBitmap = BitmapFactory.decodeStream(conn.getInputStream());
					Util.saveimage(mBitmap, filename);
					return mBitmap;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static Bitmap ReadFromSdcard(String path) {
		String imgFilePath = SAVE_PATH + path;
		Bitmap mBitmap = BitmapFactory.decodeFile(imgFilePath);
		return mBitmap;
	}
	
	public static void makedir(){
		//如果存在DownloadImagesFromListview文件夹，则先把文件夹里所有的文件删除掉
		File dirFile = new File(SAVE_PATH);
		if (dirFile.exists()) {

			File files[] = dirFile.listFiles(); 		// 声明目录下所有的文件 files[];
			for (int i = 0; i < files.length; i++) { 	// 遍历目录下所有的文件
				files[i].delete(); 	// 把每个文件 用这个方法进行迭代
			}

			dirFile.delete();
		}
		dirFile.mkdir();
	}
	
	public static String getNextText(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		String result = parser.nextText();
		if (parser.getEventType() != XmlPullParser.END_TAG) {
			parser.nextTag();
		}
		return result;
	}

	public static String md5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}
}
