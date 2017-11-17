package com.qburst.greetingcardapp.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.format.DateFormat;

import com.qburst.greetingcardapp.database.ImageTable;
import com.qburst.greetingcardapp.models.ImageModel;

public class Utils {
	static FileOutputStream outStream;
	public static File imageFilepath;
	Context context;
	ImageTable imageObject;
	static String storagestate = Environment.getExternalStorageState();
	public static ImageModel modelObject = new ImageModel();
	private static String folderType=null;

	public static String saveImage(Bitmap bitmap, String Foldertype) {
		File folder = null, subFolder = null;
		if (storagestate.equals(Environment.MEDIA_MOUNTED)) {
			folder = new File(Environment.getExternalStorageDirectory(),
					"GCardApp");
			if (!folder.exists()) {
				folder.mkdir();
			}
			if(Foldertype!=null){
			subFolder = new File(folder.getAbsolutePath() + Foldertype);
			if (!subFolder.exists()) {
				subFolder.mkdir();
			}
			folder=subFolder;
			}
			outStream = null;
			String timestamp=new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
			imageFilepath = new File(folder.getPath()+File.separator+timestamp
					+ ".PNG");
			try {
				outStream = new FileOutputStream(imageFilepath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imageFilepath.getAbsolutePath();
	}
	
	public static String saveImageforEditing(Bitmap bitmap) {
		File folder = null;
		File subFolder = null;
		if (storagestate.equals(Environment.MEDIA_MOUNTED)) {
			folder = new File(Environment.getExternalStorageDirectory(),
					"GCardApp");
			if (!folder.exists()) {
				folder.mkdir();
			}
			subFolder = new File(folder.getAbsolutePath() + "/Edit");
			if (!subFolder.exists()) {
				subFolder.mkdir();
			}
			imageFilepath = new File(subFolder.getPath() + File.separator
					+ "editImage" + ".PNG");
			try {
				outStream = new FileOutputStream(imageFilepath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				outStream.flush();
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return imageFilepath.getAbsolutePath();
	}
		}
