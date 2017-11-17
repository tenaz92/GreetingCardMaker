package com.qburst.greetingcardapp.database;

import java.util.ArrayList;

import com.qburst.greetingcardapp.models.ImageModel;

import android.content.Context;
import android.os.AsyncTask;

public class DatabaseInsertion {
	private ArrayList<String> list;
	private Context context;
	private int frameId;
	private float xPositiontext;
	private float yPositiontext;
	private int color;
	public static final int EDIT_IMAGE = 0;
	public static final int SIGN = 1;
	public static final int TEXT = 2;
	public static final int CARD = 3;
	public static final int ORIGINAL_IMAGE = 4;
	public static final int THUMB = 5;

	public void insertIntodatabse(Context context, ArrayList<String> list,
			int frameId, int color, float x, float y) {
		this.context = context;
		this.list = list;
		this.frameId = frameId;
		this.color = color;
		this.xPositiontext = x;
		this.yPositiontext = y;
		insertDatabase databaseObject = new insertDatabase();
		databaseObject.execute();
	}

	class insertDatabase extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ImageModel modelObject = new ImageModel();
			modelObject.setCardThumb(list.get(THUMB));
			modelObject.setCard(list.get(CARD));
			modelObject.setEdittedImage(list.get(EDIT_IMAGE));
			modelObject.setSignImage(list.get(SIGN));
			modelObject.setText(list.get(TEXT));
			modelObject.setOriginalImage(list.get(ORIGINAL_IMAGE));
			modelObject.setFrameId(frameId);
			modelObject.setColor(color);
			modelObject.setxText(xPositiontext);
			modelObject.setyText(yPositiontext);
			ImageTable.insertIntoTable(context, modelObject);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}
}
