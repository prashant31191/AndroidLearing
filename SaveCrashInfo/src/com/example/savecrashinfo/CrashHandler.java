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
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�ɸ������ӹܳ���,����¼���ʹ��󱨸�.
 * 
 * @author way http://blog.csdn.net/way_ping_li/article/details/7927273
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	private Thread.UncaughtExceptionHandler mDefaultHandler;			// ϵͳĬ�ϵ�UncaughtException������
	private static CrashHandler INSTANCE = new CrashHandler();			// CrashHandlerʵ��
	private Context mContext;
	private Map<String, String> info = new HashMap<String, String>();	// �����洢�豸��Ϣ���쳣��Ϣ
	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMdd-HHmmss");										// ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����

	/** ��ֻ֤��һ��CrashHandlerʵ�� */
	private CrashHandler() {
	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();		// ��ȡϵͳĬ�ϵ�UncaughtException������
		Thread.setDefaultUncaughtExceptionHandler(this);					// ���ø�CrashHandlerΪ�����Ĭ�ϴ�����
	}

	/**
	 * ��UncaughtException����ʱ��ת�����д�ķ���������
	 */
	@SuppressWarnings("static-access")
	public void uncaughtException(Thread thread, Throwable throwable) {
		if (!handleException(throwable) && mDefaultHandler != null) {
			// ����Զ����û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, throwable);
		} else {
			try {
				thread.sleep(3000);		// ��������ˣ��ó����������3�����˳�����֤�ļ����沢�ϴ���������
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// �˳�����
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 *            �쳣��Ϣ
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false.
	 */
	public boolean handleException(Throwable throwable) {
		if (throwable == null)
			return false;
		new Thread() {
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}.start();
		// �ռ��豸������Ϣ
		collectDeviceInfo(mContext);
		// ������־�ļ�
		saveCrashInfo2File(throwable);
		return true;
	}

	/**
	 * �ռ��豸������Ϣ
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();		// ��ð�������
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);					// �õ���Ӧ�õ���Ϣ������Activity
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

		Field[] fields = Build.class.getDeclaredFields();	// ���ĳ����������������ֶΣ�������public��private��proteced�����ǲ���������������ֶ�
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
		// ѭ���Ű����е��쳣��Ϣд��writer��
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// �ǵùر�
		String result = writer.toString();
		sb.append(result);
		
		// �����ļ�
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
