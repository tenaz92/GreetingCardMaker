package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextWatcher;
import android.util.FloatMath;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.database.DatabaseInsertion;
import com.qburst.greetingcardapp.utils.DisplayUtils;
import com.qburst.greetingcardapp.utils.Utils;

public class EditTemplateActivity extends BaseActivity implements
		OnClickListener, OnTouchListener, OnLongClickListener {
	private ImageView templateimageview, photoimageview, signview;
	private Button addPhotobutton, addTextbutton, addSignbutton,
			saveImagebutton;
	private EditText editText;
	private String textDraw, filepath, signPath, originalImage;
	int[] posXY, posXY_t, posXY_s;
	public static Bitmap bitmap = null, template=null, signBitmap=null;
	int value, _xDelta, _yDelta, x_t, y_t, x_s, y_s;
	BitmapDrawable ob;
	TextWatcher watcher;
	private float x, y;
	private static final int TAKE_PICTURE_OPTIONS_B = 100;
	private static final int ADD_SIGN = 0;
	private static final int THUMBNAIL_SIZE = 250;
	RelativeLayout.LayoutParams layoutParams1, layoutParams2, layoutParams;
	Intent i;
	private Matrix savedMatrix = new Matrix();
	private Matrix matrix = new Matrix();
	private PointF start = new PointF();
	private float oldDist;
	private PointF mid = new PointF();
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int DRAW = 3;
	static final int DRAG_TEXT = 4;
	int mode = NONE;
	private float[] lastEvent;
	private float newRot;
	private float d;
	private int color = 0;
	private RelativeLayout cardView;
	private String cardThumb;
	private boolean isMoved = true;
	private String activity = null;
	private float dx;
	private FrameLayout touchInterceptor;
	private Animation slideUp;
	private RelativeLayout editOptionsBar;
	private String path = null;
	private int init = 0;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittemplate_layout);
		i = getIntent();
		getBundleitems(i);
		initialize();
		setViews();
		setListenersmethod();
		registerForContextMenu(photoimageview);
		registerForContextMenu(editText);
		registerForContextMenu(signview);
		editOptionsBar.startAnimation(slideUp);
		editOptionsBar.setVisibility(View.VISIBLE);
		progress = new ProgressDialog(EditTemplateActivity.this);
	}

	private void setListenersmethod() {
		addPhotobutton.setOnClickListener(this);
		addTextbutton.setOnClickListener(this);
		saveImagebutton.setOnClickListener(this);
		addSignbutton.setOnClickListener(this);
		editText.setLayoutParams(layoutParams1);
		photoimageview.setOnTouchListener(this);
		photoimageview.setOnLongClickListener(this);
		editText.setOnLongClickListener(this);
		signview.setOnLongClickListener(this);
		editText.setOnTouchListener(this);
		editText.setDrawingCacheEnabled(true);
		editText.buildDrawingCache();
		signview.setOnTouchListener(this);
		touchInterceptor.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (editText.isFocused()) {
						Rect outRect = new Rect();
						editText.getGlobalVisibleRect(outRect);
						if (!outRect.contains((int) event.getRawX(),
								(int) event.getRawY())) {
							editText.clearFocus();
							InputMethodManager imm = (InputMethodManager) v
									.getContext().getSystemService(
											Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
							touchInterceptor.requestFocus();
						}
					}
				}
				return false;
			}
		});
	}

	private void setViews() {
		Log.e("before decoding frame", ""+SystemClock.currentThreadTimeMillis());
		template = BitmapFactory.decodeResource(getResources(), value);
		Log.e("after decoding frame", ""+SystemClock.currentThreadTimeMillis());
		if (path != null) {
			Uri imageUri = Uri.fromFile(new File(path));
			InputStream imageStream = null;
			try {
				imageStream = getContentResolver().openInputStream(imageUri);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bitmap = DisplayUtils.getBitmap(imageStream, this);
		}
		templateimageview.setImageBitmap(template);
		photoimageview.setImageBitmap(bitmap);
		if (activity != null) {
			editText.setText(textDraw);
			editText.setTextColor(color);
			editText.setVisibility(View.VISIBLE);
		}
		if(signPath!=null){
		signBitmap = BitmapFactory.decodeFile(signPath);
		}
		signview.setImageBitmap(signBitmap);
	}

	private void getBundleitems(Intent intent) {
		Bundle bundle = intent.getExtras();
		activity = bundle.getString("Activity");
		signPath = bundle.getString("sign");
		path = bundle.getString("image");
		textDraw = bundle.getString("text");
		value = bundle.getInt("position");
		originalImage = bundle.getString("originalimage");
		color = bundle.getInt("color");
		x = bundle.getFloat("xtext");
		y = bundle.getFloat("ytext");
	}

	private void initialize() {
		editOptionsBar = (RelativeLayout) findViewById(R.id.editbar);
		slideUp = AnimationUtils.loadAnimation(this, R.anim.bottom_up);
		touchInterceptor = (FrameLayout) findViewById(R.id.touchinterceptor);
		cardView = (RelativeLayout) findViewById(R.id.cardimageview);
		editText = (EditText) findViewById(R.id.edittext);
		templateimageview = (ImageView) findViewById(R.id.templateview);
		photoimageview = (ImageView) findViewById(R.id.imageview);
		signview = (ImageView) findViewById(R.id.signimage);
		addPhotobutton = (Button) findViewById(R.id.addphoto_button);
		addTextbutton = (Button) findViewById(R.id.addtext_button);
		addSignbutton = (Button) findViewById(R.id.addsign_button);
		saveImagebutton = (Button) findViewById(R.id.save_button);
		posXY = new int[2];
		posXY_t = new int[2];
		posXY_s = new int[2];
		layoutParams1 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams1.leftMargin = (int) x;
		layoutParams1.topMargin = (int) y;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addphoto_button:
			Intent intent = new Intent(EditTemplateActivity.this,
					AddPhotoActivity.class);
			textDraw = editText.getText().toString().trim();
			intent.putExtra("position", value);
			intent.putExtra("text", textDraw);
			intent.putExtra("sign", signPath);
			startActivityForResult(intent, TAKE_PICTURE_OPTIONS_B);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			break;

		case R.id.addtext_button:
			showDialogbox();
			break;

		case R.id.addsign_button:
			Intent addSignintent = new Intent(EditTemplateActivity.this,
					AddSignActivity.class);
			textDraw = editText.getText().toString().trim();
			addSignintent.putExtra("text", textDraw);
			addSignintent.putExtra("image", path);
			addSignintent.putExtra("position", value);
			startActivityForResult(addSignintent, ADD_SIGN);
			break;

		case R.id.save_button:
			touchInterceptor.requestFocus();
			editText.clearFocus();
			if(editText.getText().length()==0){
				editText.setVisibility(View.INVISIBLE);
			}
			SaveImage saveCardObject = new SaveImage();
			saveCardObject.execute();
			break;
		}
	}

	private class SaveImage extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setMessage("Saving..");
			progress.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			textDraw = editText.getText().toString().trim();
			saveImage();
			color = editText.getCurrentTextColor();
			y = editText.getTop();
			x = editText.getLeft();
			ArrayList<String> list = new ArrayList<String>();
			list.add(path);
			list.add(signPath);
			list.add(textDraw);
			list.add(filepath);
			list.add(originalImage);
			list.add(cardThumb);
			DatabaseInsertion databaseobject = new DatabaseInsertion();
			databaseobject.insertIntodatabse(EditTemplateActivity.this, list,
					value, color, x, y);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress.dismiss();
			Log.d("after dbinsertion",
					"" + SystemClock.currentThreadTimeMillis());
			Intent previewIntent = new Intent(EditTemplateActivity.this,
					PreviewActivity.class);
			previewIntent.putExtra("cardpath", filepath);
			startActivity(previewIntent);
			if (progress.isShowing()) {
				progress.dismiss();
			}
			finish();
		}
	}

	private void colorPickerdialog() {
		// TODO Auto-generated method stub
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, 0,
				new OnAmbilWarnaListener() {
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						editText.setTextColor(color);
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						// cancel was selected by the user
					}
				});

		dialog.show();
	}

	private void showDialogbox() {
		editText.setVisibility(View.VISIBLE);
	}

	private void saveImage() {
		Log.d("text", "" + editText.getText().toString());
		View view = cardView;
		int width = cardView.getWidth(), height = cardView.getHeight();
		view.measure(View.MeasureSpec.makeMeasureSpec(width,
				View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
				height, View.MeasureSpec.EXACTLY));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap cardbitmap = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(cardbitmap);
		view.draw(canvas);
		filepath = Utils.saveImage(cardbitmap, "/Cards");
		Bitmap thumbnail = Bitmap.createScaledBitmap(cardbitmap,
				THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
		cardThumb = Utils.saveImage(thumbnail, "/thumb");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_OPTIONS_B && resultCode == 400) {
			getBundleitems(data);
			setViews();
		}
		if (requestCode == ADD_SIGN && resultCode == 500) {
			getBundleitems(data);
			setViews();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView image;
		EditText text;
		switch (v.getId()) {
		case R.id.imageview:
			image = photoimageview;
			moveView(event, image);
			break;
		case R.id.edittext:
			text = editText;
			moveView(event, text);
			break;
		case R.id.signimage:
			image = signview;
			moveView(event, image);
		}
		return false;
	}

	public boolean moveView(final MotionEvent event, final View view) {
		final int X = (int) event.getRawX();
		final int Y = (int) event.getRawY();
		ImageView image = new ImageView(this);
		if (view != editText) {
			image = (ImageView) view;
			image.setScaleType(ImageView.ScaleType.MATRIX);
		} else {
			editText.setFocusableInTouchMode(true);
		}

		switch (event.getAction() & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN:
			isMoved = false;
			if (view == editText) {
				init = 0;
				mode = DRAG_TEXT;
				start.set(event.getX(), event.getY());
				RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view
						.getLayoutParams();
				_xDelta = X - lParams.leftMargin;
				_yDelta = Y - lParams.topMargin;
			} else {
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				lastEvent = null;
			}
			break;

		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			lastEvent = new float[4];
			lastEvent[0] = event.getX(0);
			lastEvent[1] = event.getX(1);
			lastEvent[2] = event.getY(0);
			lastEvent[3] = event.getY(1);
			d = rotation(event);
			break;

		case MotionEvent.ACTION_UP:
			start.set(event.getX(), event.getY());
			if (mode == DRAG_TEXT && !isMoved) {
				editText.clearFocus();
				InputMethodManager imm = (InputMethodManager) view.getContext()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
				mode = NONE;
				break;
			} else {
				break;
			}

		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			lastEvent = null;
			break;

		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG_TEXT) {
				editText.setFocusableInTouchMode(false);
				dx = event.getX() - start.x;
				Log.e("dx", "" + dx);
				if (Math.abs(dx) > 3) {
					Log.d("dx value of edittext", "" + dx);
					isMoved = true;
					Log.e("ismoved inside if loop", "" + isMoved);
				}
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
						.getLayoutParams();
				layoutParams.leftMargin = X - _xDelta;
				layoutParams.topMargin = Y - _yDelta;
				layoutParams.rightMargin = -300;
				layoutParams.bottomMargin = -300;
				view.setLayoutParams(layoutParams);
				init = 1;
			} else if (mode == DRAG) {
				matrix.set(savedMatrix);
				dx = event.getX() - start.x;
				if (Math.abs(dx) > 4) {
					Log.d("dx value", "" + dx);
					isMoved = true;
				}
				float dy = event.getY() - start.y;
				float[] matrixValues = new float[9];
				matrix.getValues(matrixValues);
				float matrixX = matrixValues[2];
				float matrixY = matrixValues[5];
				float width = matrixValues[0]
						* (image.getDrawable().getIntrinsicWidth());
				float height = matrixValues[4]
						* (image.getDrawable().getIntrinsicHeight());
				if (matrixX + dx < 0) {
					dx = -matrixX;
				}
				if (matrixX + dx + width > image.getWidth()) {
					dx = image.getWidth() - matrixX - width;
				}
				if (matrixY + dy < 0) {
					dy = -matrixY;
				}
				if (matrixY + dy + height > image.getHeight()) {
					dy = image.getHeight() - matrixY - height;
				}
				matrix.postTranslate(dx, dy);
			} else if (mode == ZOOM) {
				isMoved = true;
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = (newDist / oldDist);
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
				if (lastEvent != null && event.getPointerCount() == 3) {
					newRot = rotation(event);
					float r = newRot - d;
					float[] values = new float[9];
					matrix.getValues(values);
					float tx = values[2];
					float ty = values[5];
					float sx = values[0];
					float xc = (image.getWidth() / 2) * sx;
					float yc = (image.getHeight() / 2) * sx;
					matrix.postRotate(r, tx + xc, ty + yc);
				}
			}
			break;

		}
		image.setImageMatrix(matrix);
		return true;
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	private float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		Log.e("ismoved inside onlongclick", "" + isMoved);
		Log.e("dx", "" + dx);
		if (Math.abs(dx) < 4 && isMoved == false) {
			openContextMenu(v);
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v == editText) {
			menu.add(0, v.getId(), 0, "Change Color");
			menu.add(0, v.getId(), 0, "Remove");
		} else if (v == photoimageview) {
			Log.d("sign", "entered");
			menu.add(0, v.getId(), 0, "Add new");
		} else if (v == signview) {
			Log.d("sign", "entered");
			menu.add(0, v.getId(), 0, "Remove");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getTitle() == "Add new") {
			Intent intent = new Intent(EditTemplateActivity.this,
					AddPhotoActivity.class);
			textDraw = editText.getText().toString().trim();
			intent.putExtra("position", value);
			intent.putExtra("text", textDraw);
			intent.putExtra("sign", signPath);
			startActivityForResult(intent, TAKE_PICTURE_OPTIONS_B);
		}
		if (item.getTitle() == "Remove") {
			signview.setVisibility(View.INVISIBLE);
		}
		if (item.getTitle() == "Change Color") {
			colorPickerdialog();
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		if (signBitmap != null) {
			signBitmap.recycle();
			signBitmap = null;
		}
		super.onDestroy();
	}
}