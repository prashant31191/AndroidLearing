package com.example.downloadimagetolistview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class Util {
	public static final String SAVE_PATH = Environment
			.getExternalStorageDirectory() + "/DownloadImagesFromListview/";
//	public static final int MAXSIZE = 1048576; 			// 限定只能使用10MB的内存
	public static final int MAXSIZE = 90000; 			// 限定只能使用90000KB的内存
	public static final int EXPIREDTIME = 86400000;		// 图片资源的有效期为24小时

	//保存bitmap到sd卡上
	public static void saveimage(Bitmap mBitmap, String fileName)
			throws IOException {
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

		/*if (getDirectorySize() > MAXSIZE) {
			removeUnusedFile();
		}*/
	}

	//当文件夹大小超过限定的10MB时，清除%40最近没有被使用的文件
	/*private static void removeUnusedFile() {
		File dir = new File(SAVE_PATH);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		int removeNumber = (int)((0.4*files.length)+1);
		Arrays.sort(files, new FileLastModifSort());
		for (int i = 0; i < removeNumber; i++) {
				files[i].delete();
		}
	}
	
	private static void removeExpiredCache(String filename) {
	    File file = new File(SAVE_PATH,filename);
	    if(!file.exists())
	    	return;
	    if (System.currentTimeMillis() - file.lastModified() > EXPIREDTIME) {
	        file.delete();
	    }
	}*/
	
	//用于文件的排序，比较文件的修改时间
	public static class FileLastModifSort implements Comparator<File>{
	    public int compare(File arg0, File arg1) {
	        if (arg0.lastModified() >arg1.lastModified()) {
	            return 1;
	        } else if (arg0.lastModified() ==arg1.lastModified()) {
	            return 0;
	        } else {
	            return -1;
	        }
	    }
	}

	//获取文件夹的大小
	/*private static long getDirectorySize() {
		File dirFile = new File(SAVE_PATH);
		long size = 0;
		if (dirFile.exists()) {
			File files[] = dirFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				size += files[i].length();
			}
			return size;
		}
		return 0;
	}*/

	public static Bitmap getimagefromurl(String path) {
		try {
			Bitmap mBitmap;
			String filename = Util.md5(path) + ".png";
			File file = new File(Util.SAVE_PATH + filename);
			//removeExpiredCache(filename);
			if (file.exists()) {
				//假如本地有这个图片文件，则直接去本地读取
				long curlen = file.length();
				long prelen = file.length();
				while (true) {
					try {
						Thread.sleep(500);
						curlen = file.length();
						Log.d("wy",prelen+" -> "+curlen);
						if (curlen == prelen) {
							break;
						} else {
							prelen = curlen;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				return Util.ReadFromSdcard(filename);
			} else {
				//在本地妹找到这个图片，则去网上根据url下载对应的图片
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

	public static void makedir() {
		// 如果存在DownloadImagesFromListview文件夹，则先把文件夹里所有的文件删除掉
		File dirFile = new File(SAVE_PATH);
		if (dirFile.exists()) {
			// 遍历目录下所有的文件
			File files[] = dirFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
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
