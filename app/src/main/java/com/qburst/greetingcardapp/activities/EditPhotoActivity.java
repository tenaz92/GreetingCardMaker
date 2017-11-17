package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.Ragnarok.BitmapFilter;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.DisplayUtils;
import com.qburst.greetingcardapp.utils.Utils;

public class EditPhotoActivity extends Activity implements OnClickListener {

	private static final int CROP_IMAGE = 200;
	private ImageView photoview;
	private int windowWidth, windowHeight, value;
	private Button edit, crop;
	private String imagePath = null;
	private Bitmap mBitmap = null, bitmap, cropBitmap;
	private String text, sign, path;
	private Intent intent;
	private ProgressDialog progress;
	private String currentEditingImagePath;
	private HorizontalScrollView scrollView;
	private Bitmap changeBitmap;
	private boolean isEditphotoActive = false;
	private LinearLayout layout;
	private Animation slideUp;
	private LinearLayout editBar;;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editphoto_layout);
		getActionBar().setTitle("Edit Photo");
		initialize();
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		getBundle(bundle);
		loadImagestoScrollview();
		setViews();
		setListenersmethod();
		editBar.startAnimation(slideUp);
		editBar.setVisibility(View.VISIBLE);
		changeBitmap = bitmap.copy(bitmap.getConfig(), true);
	}

	private void loadImagestoScrollview() {
		layout = new LinearLayout(this);
		LayoutParams lParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 200);
		layout.setLayoutParams(lParams);
		LayoutInflater mLayoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// TODO Auto-generated method stub
		Bitmap scrollviewImage = bitmap.copy(bitmap.getConfig(), true);
		scrollviewImage = Bitmap.createScaledBitmap(scrollviewImage, 200, 200,
				true);
		List<Bitmap> bitmapList = new ArrayList<Bitmap>();
		bitmapList.add(BitmapFilter.changeStyle(scrollviewImage,
				BitmapFilter.AVERAGE_BLUR_STYLE, 5));
		bitmapList
				.add(BitmapFilter.changeStyle(scrollviewImage,
						BitmapFilter.LOMO_STYLE,
						(bitmap.getWidth() / 2.0) * 95 / 100.0));
		bitmapList.add(BitmapFilter.changeStyle(scrollviewImage,
				BitmapFilter.OIL_STYLE, 5));
		bitmapList.add(BitmapFilter.changeStyle(scrollviewImage,
				BitmapFilter.NEON_STYLE, 200, 100, 50));
		bitmapList.add(BitmapFilter.changeStyle(scrollviewImage,
				BitmapFilter.PIXELATE_STYLE, 10));
		bitmapList.add(BitmapFilter.changeStyle(scrollviewImage,
				BitmapFilter.SOFT_GLOW_STYLE, 0.6));
		View view = null;
		for (int i = 0; i < 5; i++) {
			if (mLayoutInflater != null) {
				view = mLayoutInflater.inflate(R.layout.scrollviewcell_layout,
						null);
			}
			ImageView cellImage = (ImageView) view.findViewById(R.id.cellimage);
			cellImage.setImageBitmap(bitmapList.get(i));
			TextView text = (TextView) view.findViewById(R.id.celltext);
			text.setText("filter");
			view.setTag(i);
			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = (Integer) v.getTag();
					ApplyFilter(id);
					Log.d("filter id", "" + v.getTag());
				}
			});
			layout.addView(view);
		}
		scrollView.addView(layout);
	}

	private void setListenersmethod() {
		edit.setOnClickListener(this);
		crop.setOnClickListener(this);
	}

	private void setViews() {
		photoview.setImageBitmap(bitmap);
	}

	private void getBundle(Bundle bundle) {
		text = bundle.getString("text");
		imagePath = bundle.getString("image");
		sign = bundle.getString("sign");
		value = bundle.getInt("position");
		Log.d("before get bitmap", ""+SystemClock.currentThreadTimeMillis());
		Uri imageUri = Uri.fromFile(new File(imagePath));
		InputStream imageStream = null;
		try {
			imageStream = getContentResolver().openInputStream(imageUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mBitmap = DisplayUtils.getBitmap(imageStream, this);
		Log.d("after get bitmap", ""+SystemClock.currentThreadTimeMillis());
		currentEditingImagePath = imagePath;
		bitmap = mBitmap.copy(mBitmap.getConfig(), true);

	}

	private void initialize() {
		slideUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
		editBar = (LinearLayout) findViewById(R.id.editoptionslayout);
		photoview = (ImageView) findViewById(R.id.photoview);
		edit = (Button) findViewById(R.id.edit_button);
		crop = (Button) findViewById(R.id.crop_button);
		scrollView = (HorizontalScrollView) findViewById(R.id.filterscrollview);
		System.out.println("display size " + windowWidth + "x" + windowHeight);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.edit_button:
			isEditphotoActive = true;
			getActionBar().setTitle("Add Filter");
			scrollView.startAnimation(slideUp);
			scrollView.setVisibility(View.VISIBLE);
			editBar.setVisibility(View.INVISIBLE);
			break;
		case R.id.crop_button:
			Intent cropIntent = new Intent(EditPhotoActivity.this,
					CropActivity.class);
			cropIntent.putExtra("imagepath", currentEditingImagePath);
			startActivityForResult(cropIntent, CROP_IMAGE);
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (isEditphotoActive) {
			setEditphotoView();
			isEditphotoActive = false;
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CROP_IMAGE && resultCode == 100) {
			bitmap = BitmapFactory.decodeFile(data.getExtras().getString(
					"croppedImage"));
			photoview.setImageBitmap(bitmap);
		}

	}

	private void setEditphotoView() {
		getActionBar().setTitle("Edit Photo");
		photoview.setImageBitmap(bitmap);
		editBar.startAnimation(slideUp);
		scrollView.setVisibility(View.INVISIBLE);
		editBar.setVisibility(View.VISIBLE);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			photoview.setImageBitmap(changeBitmap);
			progress.dismiss();
			super.handleMessage(msg);
		}

	};
	private String editString = "Edit Photo";

	public void ApplyFilter(final int id) {
		progress = new ProgressDialog(this);
		progress.setMessage("Please wait..");
		progress.show();
		new Thread() {
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				selectFilter(id);
				Message msg = Message.obtain();
				msg.what = 1;
				handler.sendMessage(msg);
			}

		}.start();
	}

	public void selectFilter(int id) {
		switch (id) {
		case 0:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.AVERAGE_BLUR_STYLE, 5);

			break;
		case 1:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.LOMO_STYLE,
					(bitmap.getWidth() / 2.0) * 95 / 100.0);

			break;
		case 2:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.OIL_STYLE, 5);

			break;
		case 3:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.NEON_STYLE, 200, 100, 50);

			break;
		case 4:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.PIXELATE_STYLE, 10);

			break;
		case 5:
			changeBitmap = BitmapFilter.changeStyle(bitmap,
					BitmapFilter.SOFT_GLOW_STYLE, 0.6);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater menuInflator = new MenuInflater(this);
		menuInflator.inflate(R.menu.customactionbar_layout, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.okbutton) {
			if ((getActionBar().getTitle().toString()).equals(editString)) {
				path = Utils.saveImage(bitmap, "/Edited");
				intent.putExtra("image", path);
				intent.putExtra("text", text);
				intent.putExtra("sign", sign);
				intent.putExtra("position", value);
				intent.putExtra("originalimage", path);
				setResult(300, intent);
				finish();
			} else {
				bitmap = changeBitmap;
				currentEditingImagePath = Utils.saveImageforEditing(bitmap);
				photoview.setImageBitmap(bitmap);
				setEditphotoView();
				isEditphotoActive = false;
			}
		} else if (item.getItemId() == R.id.clearbutton) {
			if (item.getTitle() == "Edit Photo") {
				bitmap = mBitmap.copy(mBitmap.getConfig(), true);
				currentEditingImagePath = Utils.saveImageforEditing(bitmap);
				photoview.setImageBitmap(bitmap);
			} else {
				bitmap = mBitmap.copy(mBitmap.getConfig(), true);
				currentEditingImagePath = Utils.saveImageforEditing(bitmap);
				photoview.setImageBitmap(bitmap);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		if (cropBitmap != null) {
			cropBitmap.recycle();
			cropBitmap = null;
		}
		super.onDestroy();
	}
}
