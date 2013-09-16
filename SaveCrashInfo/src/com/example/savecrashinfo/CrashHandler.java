package com.example.savecrashinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 * 
 * @author way http://blog.csdn.net/way_ping_li/article/details/7927273
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	private Thread.UncaughtExceptionHandler mDefaultHandler;			// 系统默认的UncaughtException处理类
	private static CrashHandler INSTANCE = new CrashHandler();			// CrashHandler实例
	private Context mContext;
	private Map<String, String> info = new HashMap<String, String>();	// 用来存储设备信息和异常信息
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMdd-HHmmss");										// 用于格式化日期,作为日志文件名的一部分

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();		// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);					// 设置该CrashHandler为程序的默认处理器
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	@SuppressWarnings("static-access")
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (!handleException(throwable) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, throwable);
		} else {
			try {
				thread.sleep(3000);		// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 *            异常信息
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable throwable) {
		if (throwable == null)
			return false;
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(throwable);
		return true;
	}

	/**
	 * 收集设备参数信息
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();		// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);					// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();	// 获得某个类的所有申明的字段，即包括public、private和proteced，但是不包括父类的申明字段
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveCrashInfo2File(Throwable throwable) {
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey().toLowerCase();
			String value = entry.getValue();
			sb.append(key + " = " + value + "\r\n");
		}
		sb.append("\n");
		
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		throwable.printStackTrace(pw);
		Throwable cause = throwable.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		String result = writer.toString();
		sb.append(result);
		
		// 保存文件
		String time = format.format(new Date());
		String fileName = time + ".log";
		
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(Environment.getExternalStorageDirectory()
						+ "/crash");
				if (!dir.exists())
					dir.mkdir();
				
				FileOutputStream outStream = new FileOutputStream(dir + "/" + fileName);
				outStream.write(sb.toString().getBytes());
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
