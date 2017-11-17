package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.DisplayUtils;

public class PreviewActivity extends Activity {
	private ImageView cardPreview;
	private String filepath;
	protected boolean shareBarActive = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.preview_layout);
		Bundle bundle = getIntent().getExtras();
		filepath = bundle.getString("cardpath");
		Uri imageUri = Uri.fromFile(new File(filepath));
		InputStream imageStream = null;
		try {
			imageStream = getContentResolver().openInputStream(imageUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = DisplayUtils.getBitmap(imageStream, this);
		initialize();
		cardPreview.setImageBitmap(bitmap);
		super.onCreate(savedInstanceState);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		cardPreview = (ImageView) findViewById(R.id.cardview);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater menuInflator = new MenuInflater(this);
		menuInflator.inflate(R.menu.sharecustombar, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.share) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_TEXT, "Greetings");
			intent.putExtra(Intent.EXTRA_TITLE, "Happy Birthday1");
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.parse("file://" + filepath));

			Intent openInChooser = new Intent(intent);
			startActivity(openInChooser);
		}
		return super.onOptionsItemSelected(item);
	}
}
