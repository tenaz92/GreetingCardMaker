package com.qburst.greetingcardapp.activities;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.database.SQLiteController;
import com.qburst.greetingcardapp.fragments.CategorySelectFragment;
import com.qburst.greetingcardapp.fragments.TemplateListFragment;

public class MainActivity extends BaseActivity {
	private String tag = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createDatabase();
		FragmentManager fragmentmanager = getFragmentManager();
		FragmentTransaction fragmenttransaction = fragmentmanager
				.beginTransaction();
		CategorySelectFragment categoryobj = new CategorySelectFragment();
		fragmenttransaction.replace(R.id.container, categoryobj);
		fragmenttransaction.commit();
	}

	private void createDatabase() {
		SQLiteController helper = SQLiteController.getInstance(this);
		try {
			helper.createDataBase();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void secfrag(int type, Fragment fragment) {
		FragmentManager fragmentmanager = getFragmentManager();
		FragmentTransaction fragmenttransaction = fragmentmanager
				.beginTransaction();
		TemplateListFragment templateobj = new TemplateListFragment(type);
		tag = fragment.getClass().getName();
		fragmenttransaction.replace(R.id.container, templateobj, tag);
		fragmenttransaction.addToBackStack(tag);
		fragmenttransaction.commit();

	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			Log.i("MainActivity", "popping backstack");
			fm.popBackStack();
		} else {
			Log.i("MainActivity", "nothing on backstack, calling super");
			AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
			exitDialog
					.setMessage("Do you want to exit?")
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									MainActivity.this.finish();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							}).create().show();
		}
	}
}