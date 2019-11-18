package com.txl.commonlibrary.utils.file;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * 文件操作工具
 * 
 * @author bin
 */
public class FileUtil {
	public static final String SDCARD  = Environment.getExternalStorageDirectory().getPath();
	public static String HOST=null;//"/meilihelan";
	public static String PATH =null;// SDCARD +HOST+ "/";


	public static String USER    =null;// PATH + "user/";
	public static String CONTENT =null;// PATH + "content/";
	public static String BUFFER  = null;//PATH + "buffer/";
	public static String FILEAPK = null;//PATH + "apk/";
	public static String CACHE   = null;//PATH + "cache/";
	public static String LOG     = null;//PATH + "log/";
	public static String PHOTO_APP     = null;//PATH + "photo/";

	public static String TEMP=null;

	/**
	 * 首页缓缓目录
	 * */
	public static String HOME_CACHE_DIR =null;//CACHE+"home/";

	public static final String TXT     = ".txt";
	public static final String TMP     = ".tmp";
	public static final String JPG     = ".jpg";
	public static final String PNG     = ".png";
	public static final String MP3     = ".mp3";
	public static final String APK     = ".apk";

	public static final String TAG = "FileUtil";
	/**
	 * 是否可以保存写数据  默认是可以的
	 */
	public static boolean canSaveFlag=true;

	public static synchronized void initPackage(Context context)
	{
		if(TextUtils.isEmpty(HOST))
		{
			HOST="/"+context.getPackageName();
			PATH =SDCARD+HOST+"/";
			USER= PATH + "user/";
			CONTENT = PATH + "content/";
			BUFFER  = PATH + "buffer/";
			FILEAPK = PATH + "apk/";
			CACHE  = PATH + "cache/";
			LOG  = PATH + "log/";
			PHOTO_APP = PATH + "photo/";
			HOME_CACHE_DIR=CACHE+"home/";
			TEMP= PATH +"temp/";
			String[] fileDir={HOST, PATH,USER,CONTENT,BUFFER,FILEAPK,CACHE,LOG,PHOTO_APP,HOME_CACHE_DIR,TEMP};
			for(int i=0;i<fileDir.length;i++)
			{
				createDirectory(fileDir[i]);
			}
		}
	}

	/**
	 * 生成文件路径
	 * 
	 * @param dir
	 * @param filename
	 * @param suf
	 * @return
	 */
	public static synchronized String createFilePath(String dir, String filename, String suf) {
		String filepath = null;
		try {
			if (!TextUtils.isEmpty( filename )) {
				File file = new File( dir + filename + suf );
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				filepath = file.getPath();
			}
		} catch (Exception ex) {
			filepath = null;
		}
		return filepath;
	}

	public static synchronized long fileLength(String path) {
		try {
			File file = new File( path );
			if (file.exists()) {
				return file.length();
			}
		} catch (Exception ex) {

		}
		return 0;
	}

	/**
	 * 新建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized File createDirectory(String path) {
		File file;
		try {
			file = new File( path );
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			file = null;
		}
		return file;
	}

	/**
	 * 新建文件
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized File createFile(String path) {
		File file;
		try {
			file = new File( path );
			if (!file.exists()) {
				File parent = file.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
				file.createNewFile();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			file = null;
		}
		return file;
	}

	/**
	 * 新建临时文件
	 * 
	 * @param prefix
	 *            文件名
	 * @param suffix
	 *            文件类型 ".txt"
	 * @param directory
	 *            根目录
	 * @return
	 */
	public static synchronized File createTempFile(String prefix, String suffix, File directory) {
		File file;
		try {
			if (!directory.exists()) {
				directory.mkdirs();
			}
			file = File.createTempFile( prefix, suffix, directory );
		} catch (IOException ex) {
			ex.printStackTrace();
			file = null;
		}
		return file;
	}

	/**
	 * 最后修改时间
	 * 
	 * @param dir
	 * @param filename
	 * @param suf
	 * @return
	 */
	public static synchronized long lastModified(String dir, String filename, String suf) {
		return lastModified( createFilePath( dir, filename, suf ) );
	}

	/**
	 * 最后修改时间
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized long lastModified(String path) {
		try {
			if (!TextUtils.isEmpty( path )) {
				File file = new File( path );
				if (file.exists()) {
					return file.lastModified();
				}
			}
		} catch (Exception ex) {
		}
		return 0;
	}

	/**
	 * 文件是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized boolean hasFile(String path) {
		boolean value = false;
		try {
			if (!TextUtils.isEmpty( path )) {
				File file = new File( path );
				if (file.exists() && file.isFile()) {
					value = true;
				}
			}
		} catch (Exception ex) {
		}
		return value;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized boolean delFile(String path) {
		if (null == path || path.length() == 0)
			return true;
		File file = new File( path );
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件
	 * 
	 * @param
	 * @return
	 */
	public static synchronized boolean delFile(File file) {
		if (file.exists() && file.isFile()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized boolean delDirectory(String path) {
		File directory = new File( path );
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					if (!delDirectory( file ))
						delFile( file );
				}
			}
			return directory.delete();
		}
		return false;
	}

	/**
	 * 删除文件夹
	 * 
	 * @param directory
	 * @return
	 */
	public static synchronized boolean delDirectory(File directory) {
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (File file : files) {
					if (!delDirectory( file ))
						delFile( file );
				}
			}
			return directory.delete();
		}
		return false;
	}

	/**
	 * 清空文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static synchronized boolean clearDirectory(String path) {
		File directory = new File( path );

		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();

			if (null != files) {
				for (File file : files) {
					if (!delDirectory( file ))
						delFile( file );
				}
			}
		}

		return true;
	}

	/*
	 * 获取SD卡路径
	 */
	public static synchronized String getSDPath() {
		return SDCARD;
	}

	/*
	 * 文件更名
	 */
	public static synchronized void renameFile(String strDest, String strSrc) {
		File fileDest = new File( strDest ), fileSrc = new File( strSrc );

		if (fileDest.exists() && fileDest.isFile()) {
			fileDest.delete();
		}

		if (fileSrc.exists() && fileSrc.isFile()) {
			fileSrc.renameTo( fileDest );
		}

		if (fileSrc.exists() && fileSrc.isFile()) {
			fileSrc.delete();
		}
	}

	/**
	 * 
	 * @param strPath
	 * @param strText
	 * @return
	 */
	public static synchronized boolean saveTextFile(String strPath, String strText) {
		Log.i(TAG, "-->saveTextFile 路径："+strPath+",内容："+strText);
		if(!canSaveFlag)
			return false;
		OutputStream outputStream = null;
		OutputStreamWriter outputStreamWriter = null;
		boolean bRet = true;
		File file = null;

		try {
			delFile( strPath );

			file = new File( strPath );

			outputStream = new FileOutputStream( file );
			outputStreamWriter = new OutputStreamWriter( outputStream );

			outputStreamWriter.write( strText );
			outputStreamWriter.close();
		} catch (Exception e) {
			bRet = false;
		}

		return bRet;
	}

	/**
	 * 读取文本内容
	 * @param strPath 带文件名的文件的全路径
	 * @return
	 */
	public static synchronized String readTextFile(String strPath) {
		InputStream inputStream = null;
		BufferedInputStream bufferedInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		String strText;
		StringBuffer stringBuffer = null;
		File file = null;

		try {
			file = new File( strPath );
			if (file.exists() && ( file.isFile() )) {
				stringBuffer = new StringBuffer();

				inputStream  = new FileInputStream( file );
				bufferedInputStream = new BufferedInputStream( inputStream );
				inputStreamReader = new InputStreamReader( bufferedInputStream, "UTF-8" );
				bufferedReader = new BufferedReader( inputStreamReader );

				while ( ( strText = bufferedReader.readLine() ) != null) {
					stringBuffer.append( strText );
				}

				strText = stringBuffer.toString();
			} else {
				strText = null;
			}
		} catch (Exception e) {
			strText = null;
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
					bufferedReader = null;
				}
				if (null != inputStreamReader) {
					inputStreamReader.close();
					inputStreamReader = null;
				}
				if (null != bufferedInputStream) {
					bufferedInputStream.close();
					bufferedInputStream = null;
				}
				if (null != inputStream) {
					inputStream.close();
					inputStream = null;
				}
			} catch (Exception ex) {
			}
		}

		return strText;
	}

	/**
	 * 保存数据到data/data目录下
	 * @param fileName
	 * @param text
	 */
	public static synchronized void saveTextDataDir(String fileName,String text,Context context){
		try {
			FileOutputStream outStream=context.openFileOutput(fileName,Context.MODE_PRIVATE);
			outStream.write(text.getBytes());
			outStream.close();
		} catch (FileNotFoundException e) {
			return;
		}
		catch (IOException e){
			return ;
		}
	}
	/**
	 * 从data/data目录下读取文件
	 * @param fileName
	 * @return
	 */
	public static synchronized String readTextFromDataDir(String fileName,Context context) {
		String text = "";
		try {
			FileInputStream inStream = context.openFileInput(fileName);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			stream.close();
			inStream.close();

			text = stream.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		}
		return text;
	}

	/**
	 * 读取文件流
	 * 
	 * @param ins
	 * @return
	 */
	public static synchronized String readTextInputStream(InputStream ins) {
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		String strText;
		StringBuffer stringBuffer = null;

		try {
			stringBuffer = new StringBuffer();

			inputStreamReader = new InputStreamReader( ins, "UTF-8" );
			bufferedReader = new BufferedReader( inputStreamReader );

			while ( ( strText = bufferedReader.readLine() ) != null) {
				stringBuffer.append( strText );
			}

			strText = stringBuffer.toString();
		} catch (Exception e) {
			strText = null;
		} finally {
			try {
				if (null != bufferedReader) {
					bufferedReader.close();
					bufferedReader = null;
				}
				if (null != inputStreamReader) {
					inputStreamReader.close();
					inputStreamReader = null;
				}
			} catch (Exception e) {
			}
		}

		return strText;
	}

	/**
	 * 复制文件
	 * 
	 * @param ins
	 * @param ops
	 * @return
	 */
	public static synchronized boolean copyFile(InputStream ins, OutputStream ops) {
		boolean result = false;
		try {
			byte[] byBuffer = new byte[1024];
			int readLen = 0;
			while ( ( readLen = ins.read( byBuffer ) ) > 0) {
				ops.write( byBuffer, 0, readLen );
			}
			ops.flush();

			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			try {
				if (ops != null) {
					ops.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * 复制文件
	 * 
	 * @param strDest
	 * @param strSrc
	 * @return
	 */
	public static synchronized boolean copyFile(String strDest, String strSrc) {
		boolean result = false;

		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			inputStream = new FileInputStream( strSrc );
			outputStream = new FileOutputStream( strDest, false );

			byte byBuffer[] = new byte[1024];
			int nRead;
			while ( ( nRead = inputStream.read( byBuffer ) ) > 0) {
				outputStream.write( byBuffer, 0, nRead );
			}
			outputStream.flush();

			result = true;
		} catch (Exception e) {
			result = false;
		} finally {
			try {
				if (outputStream != null) {
					outputStream.close();
				}

				if (inputStream != null) {
					inputStream.close();
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	public static synchronized boolean hasStorage() 
	{  
		String state = Environment.getExternalStorageState();  
		return Environment.MEDIA_MOUNTED.equals(state);
	}  


	public static synchronized boolean isFileOutofLength(String path,double targetLength){
		try {
			File file = new File( path );
			if (file.exists()) {
				return file.length() > targetLength;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static synchronized boolean isFileEnable(String path){
		try {
			File file = new File( path );
			if (file.exists() && file.length() > 0) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static synchronized boolean isFileEnable(File file){
		return file != null && file.exists() && file.length() > 0;
	}

	@SuppressWarnings("deprecation")
	public static synchronized ArrayList<String> getGalleryPhotos(Activity act) {
		Map<String,ArrayList<String>> maps = new HashMap<String,ArrayList<String>>();
		ArrayList<String> galleryList = new ArrayList<String>();
		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = act.managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			if (imagecursor != null && imagecursor.getCount() > 0) {
				while (imagecursor.moveToNext()) {
					String item = new String();
					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					item = imagecursor.getString(dataColumnIndex);
					File file = new File(item);
					putFile2Folders(maps,file.getAbsolutePath(),item);
					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(galleryList);
		return galleryList;
	}
	@SuppressWarnings("deprecation")
	public static synchronized Map<String,ArrayList<String>> getGalleryPhotosWithFolder(Activity act) {
		Map<String,ArrayList<String>> maps = new HashMap<String,ArrayList<String>>();
		ArrayList<String> galleryList = new ArrayList<String>();
		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			Cursor imagecursor = act.managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			if (imagecursor != null && imagecursor.getCount() > 0) {
				while (imagecursor.moveToNext()) {
					String item = new String();
					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					item = imagecursor.getString(dataColumnIndex);
					File file = new File(item);
					String path = file.getAbsolutePath();
					if(null != file.getParent()){
						path = file.getParent();
					}
					putFile2Folders(maps,getKey(path),item);
					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(galleryList);
		maps.put("all*--",galleryList);
		return maps;
	}

	public static synchronized Map<String,ArrayList<String>> getVideosWithFolder(Activity act) {
		Map<String,ArrayList<String>> maps = new HashMap<String,ArrayList<String>>();
		ArrayList<String> galleryList = new ArrayList<String>();
		try {
			final String[] columns = { MediaStore.Video.Media.DATA,
					MediaStore.Video.Media._ID };
			final String orderBy = MediaStore.Video.Media._ID;
			Cursor imagecursor = act.managedQuery(
					MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			if (imagecursor != null && imagecursor.getCount() > 0) {
				while (imagecursor.moveToNext()) {
					String item = new String();
					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Video.Media.DATA);
					item = imagecursor.getString(dataColumnIndex);
					File file = new File(item);
					String path = file.getAbsolutePath();
					if(null != file.getParent()){
						path = file.getParent();
					}
					putFile2Folders(maps,getKey(path),item);
					galleryList.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.reverse(galleryList);
		maps.put("all*--",galleryList);
		return maps;
	}
	private static String getKey(String dir){
		if(dir.contains("/")){
			int index = dir.lastIndexOf("/");
			dir = dir.substring(index+1, dir.length());
		}
		return dir;
	}
	private static void putFile2Folders(Map<String,ArrayList<String>> maps,String key,String path){
		if(maps.containsKey(key)){
			ArrayList<String> filePaths = maps.get(key);
			filePaths.add(path);
		}else{
			ArrayList<String> filePaths = new ArrayList<String>();
			filePaths.add(path);
			maps.put(key, filePaths);
		}
	}
	/**
	 * 用时间戳生成照片名称
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static synchronized String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	public static synchronized File saveData(String data,String fileName)
	{
		return saveData(data.getBytes(), fileName);
	}
	public static synchronized File saveData(byte[] data,String fileName)
	{
		FileOutputStream outSteam=null;
		File file=null;
		try
		{
			file=new File(CACHE+fileName);
			outSteam=new FileOutputStream(file);
			outSteam.write(data);
			outSteam.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return file;
	}
	public static synchronized File saveData(String data,String filePath,String fileName)
	{
		data+="";
		return saveData(data.getBytes(),filePath,fileName);
	}

	public static synchronized void appendData(String data,String filePath,String fileName)
	{
		if(!canSaveFlag)
			return;
		try {
			File file=new File(filePath+fileName);
			if(!file.exists())
				createFile(file.getAbsolutePath());
			RandomAccessFile randomFile = new RandomAccessFile(file, "rw");
			long fileLength = randomFile.length();
			randomFile.seek(fileLength);
			data=new String(data.getBytes("GBK"), "ISO8859_1");
			randomFile.writeBytes(data);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static synchronized File saveData(byte[] data,String filePath,String fileName)
	{
		if(!canSaveFlag)
			return null;
		FileOutputStream outSteam=null;
		File file=null;
		try
		{
			file=new File(filePath+fileName);
			outSteam=new FileOutputStream(file);
			outSteam.write(data);
			outSteam.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
		}
		return file;
	}

	public static synchronized String getFileNameNoSuffix(String filePath)
	{
		try {
			int endIndex=filePath.lastIndexOf(".");
			if(endIndex==-1)
				endIndex=filePath.length();
			if(filePath.lastIndexOf(File.separator)!=-1)
				return filePath.substring(filePath.lastIndexOf(File.separator)+1, endIndex);
			else if(filePath.lastIndexOf("\\")!=-1)
				return filePath.substring(filePath.lastIndexOf("\\")+1, endIndex);
			else 
				return filePath.substring(0, endIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}

	public static synchronized String getFileName(String filePath)
	{
		try {
			if(filePath.lastIndexOf(File.separator)!=-1)
				return filePath.substring(filePath.lastIndexOf(File.separator)+1);
			else
				return filePath;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "";
	}
	public static synchronized String saveCrashInfo2File(Throwable ex) 
	{
		if(!canSaveFlag)
			return null;
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while(cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		Log.i(TAG, result.toString());
		try {
			long timestamp = System.currentTimeMillis();
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".txt";
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = LOG;
				File dir = new File(path);
				if(!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	public static synchronized void saveCrashInfo2FileDaemon(final Throwable ex) 
	{
		if(!canSaveFlag)
			return;
		Runnable thread=new Runnable()
		{
			@Override
			public void run()
			{
				saveCrashInfo2File(ex);
			}
		};
		Executors.newCachedThreadPool().execute(thread);
	}
	public static synchronized void appendSaveDataDaemon(final String data,final String filePath,final String fileName)
	{
		if(!canSaveFlag)
			return;
		Runnable thread=new Runnable()
		{
			@Override
			public void run()
			{
				appendData(data, filePath, fileName);
			}
		};
		Executors.newCachedThreadPool().execute(thread);
	}

	public static String readFileByNio(String filePath,String charsetName)
	{
		StringBuffer stringBuffer=new StringBuffer();
		FileInputStream fileInputStream=null;
		FileChannel fileChanel=null;
		try
		{
			fileInputStream=new FileInputStream(filePath);
			fileChanel=fileInputStream.getChannel();
			ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
			int readResult=fileChanel.read(byteBuffer);
			while(readResult!=-1)
			{
				byteBuffer.flip();
				CharBuffer charBuffer = CharBuffer.allocate(1024);
				Charset charset = Charset.forName(charsetName);
				CharsetDecoder decoder = charset.newDecoder();
				decoder.decode(byteBuffer, charBuffer, true);
				charBuffer.flip();
				while(charBuffer.hasRemaining())
				{
					char chars= charBuffer.get();
					stringBuffer.append(chars);
				}
				byteBuffer.clear();
				readResult = fileChanel.read(byteBuffer);

			}

			byteBuffer.clear();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(fileInputStream!=null)
					fileInputStream.close();
			}
			catch (Exception e)
			{
			}
			try
			{
				if(fileChanel!=null)
					fileChanel.close();
			}
			catch (Exception e)
			{
			}
		}
		return stringBuffer.toString();
	}
	public static String readFileByNio(String filePath)
	{
		return readFileByNio(filePath,"UTF-8");
	}

	public static void saveBitmap(String path, Bitmap bitmap) {
		try {
			File f = createFile(path);
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}