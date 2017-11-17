package com.qburst.greetingcardapp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.qburst.greetingcardapp.models.ImageModel;

public class ImageTable {
	int id;
	public static final String TABLENAME = "carddetails";
	public static final String CARD_ID = "cardid";
	public static final String COLOUMN_CARD_THUMB = "cardthumb";
	public static final String COLOUMN_CARD = "savedcard";
	public static final String COLOUMN_FRAME_ID = "cardframe";
	public static final String COLOUMN_TEXT = "text";
	public static final String COLOUMN_SIGN = "signimage";
	public static final String COLOUMN_ORIGINAL = "originalimage";
	public static final String COLOUMN_IMAGE = "edittedimage";
	public static final String TEXT_COLOR = "textcolor";
	public static final String TEXT_X_POSITION = "xtext";
	public static final String TEXT_Y_POSITION = "ytext";
	public static final String FETCH_ALL_DATA_FOR_ROW = "select * from carddetails where cardid=";
	public static final String FETCH_CARD_THUMB = "select cardthumb, cardid from carddetails";

	public static void insertIntoTable(Context context, ImageModel modelObject) {
		insertImage(context, modelObject);
	}

	public static synchronized void insertImage(Context context,
			ImageModel modelObject) {

		SQLiteController dbHelper = SQLiteController.getInstance(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLOUMN_CARD_THUMB, modelObject.getCardThumb());
		values.put(COLOUMN_CARD, modelObject.getCard());
		values.put(COLOUMN_IMAGE, modelObject.getEdittedImage());
		values.put(COLOUMN_ORIGINAL, modelObject.getOriginalImage());
		values.put(COLOUMN_SIGN, modelObject.getSignImage());
		values.put(COLOUMN_TEXT, modelObject.getText());
		values.put(COLOUMN_FRAME_ID, modelObject.getFrameId());
		values.put(TEXT_COLOR, modelObject.getColor());
		values.put(TEXT_X_POSITION, modelObject.getxText());
		values.put(TEXT_Y_POSITION, modelObject.getyText());
		try {
			db.replace(TABLENAME, null, values);
		} catch (Exception e) {
			Log.i("tag", "insertion error");
			e.printStackTrace();
		}
		db.close();
		// fetchImagesList(context, FETCH_ALL_DATA);
	}

	public static List<ImageModel> fetchImagesList(Context context)
			throws SQLException, NullPointerException {
		SQLiteController dbHelper = SQLiteController.getInstance(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		List<ImageModel> imagesList = new ArrayList<ImageModel>();
		Cursor cursor = db.rawQuery(FETCH_CARD_THUMB, null);

		if (cursor != null && cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				ImageModel modelObject = new ImageModel();
				modelObject.setCardId(cursor.getInt(cursor
						.getColumnIndex(CARD_ID)));
				modelObject.setCardThumb(cursor.getString(cursor
						.getColumnIndex(COLOUMN_CARD_THUMB)));
				imagesList.add(modelObject);
			}
			return imagesList;
		}
		else{
		return null;
		}
		
	}

	public static ImageModel fetchimageRow(Context context, int id) {
		SQLiteController dbHelper = SQLiteController.getInstance(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ImageModel modelObject = new ImageModel();
		Log.e("query", "" + FETCH_ALL_DATA_FOR_ROW + id);
		Cursor cursor = db.rawQuery(FETCH_ALL_DATA_FOR_ROW + id, null);
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			modelObject
					.setCardId(cursor.getInt(cursor.getColumnIndex(CARD_ID)));
			modelObject.setCard(cursor.getString(cursor
					.getColumnIndex(COLOUMN_CARD)));
			modelObject.setEdittedImage(cursor.getString(cursor
					.getColumnIndex(COLOUMN_IMAGE)));
			modelObject.setOriginalImage(cursor.getString(cursor
					.getColumnIndex(COLOUMN_ORIGINAL)));
			modelObject.setSignImage(cursor.getString(cursor
					.getColumnIndex(COLOUMN_SIGN)));
			modelObject.setText(cursor.getString(cursor
					.getColumnIndex(COLOUMN_TEXT)));
			modelObject.setFrameId(cursor.getInt(cursor
					.getColumnIndex(COLOUMN_FRAME_ID)));
			modelObject.setColor(cursor.getInt(cursor
					.getColumnIndex(TEXT_COLOR)));
			modelObject.setxText(cursor.getFloat(cursor
					.getColumnIndex(TEXT_X_POSITION)));
			modelObject.setyText(cursor.getFloat(cursor
					.getColumnIndex(TEXT_Y_POSITION)));
		}
		return modelObject;
	}
	public static void removeRow(Context context, int id){
		SQLiteController dbHelper = SQLiteController.getInstance(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		db.delete(TABLENAME, "cardid="+id, null);
}
}