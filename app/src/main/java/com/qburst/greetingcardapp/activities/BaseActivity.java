package com.qburst.greetingcardapp.activities;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.DisplayUtils;

public class BaseActivity extends Activity {
	private static final int GET_SAVED_CARD = 50;
	private Boolean savedcard;
	ActionBar actionbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayUtils.displaysize(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (this instanceof MainActivity
				|| this instanceof EditTemplateActivity) {
			inflater.inflate(R.menu.main, menu);
			menu.getItem(0).setIcon(R.drawable.open);
			savedcard = false;
		} else if (this instanceof SavedCardActivity) {
			inflater.inflate(R.menu.main, menu);
			menu.getItem(0).setIcon(R.drawable.newcard);
			savedcard = true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (!savedcard) {
			Log.e("tag", "saved card clicked");
			checkFileIfExist();
		} else {
			Log.e("tag", "new card clicked");
			startActivityForResult(new Intent(this,MainActivity.class),GET_SAVED_CARD);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private void checkFileIfExist() {
		File folder = null;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			folder = new File(Environment.getExternalStorageDirectory(),
					"GCardApp/Cards");
			if (folder.exists()) {
				startActivity(new Intent(this, SavedCardActivity.class));
//				finish();
			} else {
				Toast.makeText(getApplicationContext(),
						"no cards saved to list", Toast.LENGTH_SHORT).show();
			}
		}
	}

}