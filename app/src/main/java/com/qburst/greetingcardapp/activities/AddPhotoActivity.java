package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.DisplayUtils;
import com.qburst.greetingcardapp.utils.Utils;

public class AddPhotoActivity extends Activity implements OnClickListener {
	private Button camera;
	private Button gallery;
	private static final int TAKE_PICTURE_REQUEST_B = 100;
	private static final int RESULT_LOAD_IMAGE = 200;
	private static final int EDIT_PHOTO = 200;
	private Bitmap imageBitmap;
	private String filepath = null, text, sign, editedimage, originalImage;
	private Uri selectedImage;
	private int value;
	Intent i;
	private Display currentDisplay;
	private int windowWidth;
	private Uri imageUri;
	private int windowHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addphoto_layout);
		initialize();
		i = getIntent();
		getBundle(i);
		camera.setOnClickListener(this);
		gallery.setOnClickListener(this);

	}

	private void getBundle(Intent intent) {
		Bundle bundle = intent.getExtras();
		originalImage = bundle.getString("originalimage");
		text = bundle.getString("text");
		sign = bundle.getString("sign");
		value = bundle.getInt("position");
		editedimage = bundle.getString("image");
	}

	private void initialize() {
		camera = (Button) findViewById(R.id.camera_button);
		gallery = (Button) findViewById(R.id.gallery_button);
		currentDisplay = getWindowManager().getDefaultDisplay();
		windowWidth = DisplayUtils.displaysize(this).x;
		windowHeight = DisplayUtils.displaysize(this).y;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.camera_button:
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			imageUri = getImageUri();
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(intent, TAKE_PICTURE_REQUEST_B);
			break;
		case R.id.gallery_button:
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(i, RESULT_LOAD_IMAGE);
			break;
		}

	}

	private Uri getImageUri() {
		// TODO Auto-generated method stub
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory(),
					"GcardApp");
			if (!file.exists()) {
				file.mkdir();
			}
			imageUri = Uri.fromFile(new File(file.getPath() + File.separator
					+ "editImage" + ".PNG"));
			return imageUri;
		}
		return null;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_REQUEST_B && resultCode == RESULT_OK) {
			Log.d("in onActivity", "" + SystemClock.currentThreadTimeMillis());
			filepath = getFilePathFromURI(imageUri);
			Log.d("in onActivity after getting filepath",
					"" + SystemClock.currentThreadTimeMillis());
			callEditPhoto();
		} else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			selectedImage = data.getData();
			FetchImage fetchObject = new FetchImage();
			fetchObject.execute();
		} else if (requestCode == EDIT_PHOTO && resultCode == 300) {
			getBundle(data);
			i.putExtra("text", text);
			i.putExtra("sign", sign);
			i.putExtra("image", editedimage);
			i.putExtra("position", value);
			i.putExtra("originalimage", originalImage);
			setResult(400, i);
			finish();
		}
	}

	private class FetchImage extends AsyncTask<Void, Void, Void> {
		ProgressDialog progress = new ProgressDialog(AddPhotoActivity.this);

		@Override
		protected void onPreExecute() {
			progress.setMessage("Loading image...");
			progress.setCancelable(false);
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			InputStream imageStream;

			try {
				imageStream = getContentResolver().openInputStream(
						selectedImage);
				imageBitmap = DisplayUtils.getBitmap(imageStream,
						AddPhotoActivity.this);
				filepath = Utils.saveImage(imageBitmap, null);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Log.e("before progress dismiss", "" + System.currentTimeMillis());
			progress.dismiss();
			Log.e("after progress dismiss", "" + System.currentTimeMillis());
			callEditPhoto();
			Log.e("after calledit dismiss", "" + System.currentTimeMillis());
			super.onPostExecute(result);
		}

	}

	public void callEditPhoto() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AddPhotoActivity.this,
				EditPhotoActivity.class);
		intent.putExtra("image", filepath);
		intent.putExtra("text", text);
		intent.putExtra("sign", sign);
		intent.putExtra("position", value);
		Log.e("passing intent", "" + System.currentTimeMillis());
		startActivityForResult(intent, EDIT_PHOTO);
		Log.e("passed intent", "" + System.currentTimeMillis());
	}

	public String getFilePathFromURI(Uri contentUri) {
		try {
			String[] proj = { MediaStore.MediaColumns.DATA };
			Cursor cursor = getContentResolver().query(contentUri, proj, null,
					null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} catch (Exception e) {
			return contentUri.getPath();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (imageBitmap != null) {
			imageBitmap.recycle();
			imageBitmap = null;
		}
		super.onDestroy();
	}
}