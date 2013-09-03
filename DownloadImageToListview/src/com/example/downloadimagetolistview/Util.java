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
				conn.setConnectTimeout(5 * 1000); // �������ӵ�ʱ��Ϊ5��
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
		//�������DownloadImagesFromListview�ļ��У����Ȱ��ļ��������е��ļ�ɾ����
		File dirFile = new File(SAVE_PATH);
		if (dirFile.exists()) {

			File files[] = dirFile.listFiles(); 		// ����Ŀ¼�����е��ļ� files[];
			for (int i = 0; i < files.length; i++) { 	// ����Ŀ¼�����е��ļ�
				files[i].delete(); 	// ��ÿ���ļ� ������������е���
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
