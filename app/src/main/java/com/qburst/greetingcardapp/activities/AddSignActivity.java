package com.qburst.greetingcardapp.activities;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.Utils;
import com.qburst.greetingcardapp.views.DrawingView;

public class AddSignActivity extends Activity implements OnClickListener {
	private DrawingView drawObject;
	private String text;
	private Bitmap signBitmap;
	private String path;
	private View view;
	private Intent i;
	private String filepath;
	private int value;
	private Button colorButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addsign_layout);
		initialize();
		i = getIntent();
		getBundle(i);
		colorButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				colorPickerdialog();
				
			}
		});
	}
	private void colorPickerdialog() {
		// TODO Auto-generated method stub
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, 0,
				new OnAmbilWarnaListener() {
					@Override
					public void onOk(AmbilWarnaDialog dialog, int color) {
						drawObject.setPaintColor(color,AddSignActivity.this);
					}

					@Override
					public void onCancel(AmbilWarnaDialog dialog) {
						// cancel was selected by the user
					}
				});

		dialog.show();
	}
	private void getBundle(Intent intent) {
		Bundle bundle = intent.getExtras();
		value = bundle.getInt("position");
		text = bundle.getString("text");
		path = bundle.getString("image");
	}

	private void initialize() {
		colorButton=(Button)findViewById(R.id.color_button);
		drawObject = new DrawingView(AddSignActivity.this, null);
		view = findViewById(R.id.drawingview);
	}

	@Override
	public void onClick(View v) {
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
		if (item.getItemId() == R.id.okbutton) {
			view.setDrawingCacheEnabled(true);
			signBitmap = drawObject.save(view);
			filepath = Utils.saveImage(signBitmap, "/Sign");
			i.putExtra("sign", filepath);
			i.putExtra("text", text);
			i.putExtra("position", value);
			i.putExtra("image", path);
			setResult(500, i);
			finish();
		} else {
			drawObject.clear(view);
		}
		return super.onOptionsItemSelected(item);
	}

}