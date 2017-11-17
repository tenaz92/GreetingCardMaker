package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.adapters.SavedCardAdapter;
import com.qburst.greetingcardapp.database.ImageTable;
import com.qburst.greetingcardapp.models.ImageModel;

public class SavedCardActivity extends BaseActivity implements
		OnItemClickListener, MultiChoiceModeListener {
	private GridView gridView;
	private SavedCardAdapter adapter;
	private List<ImageModel> imageModellist = new ArrayList<ImageModel>();
	Intent intent;
	private int noOfSelection=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.gridview);
		gridView = (GridView) findViewById(R.id.grid);
		intent=getIntent();
		getImagefiles();
		if (imageModellist!= null) {
			 adapter = new SavedCardAdapter(this,
					imageModellist);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(this);
			gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
			gridView.setMultiChoiceModeListener(this);
		} else {
			AlertDialog.Builder alertObject=new AlertDialog.Builder(this);
			alertObject.setTitle("Alert");
			alertObject
			.setMessage("No Cards saved")
			.setCancelable(false)
			.setNeutralButton("ok",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
					finish();
				}
			});
			alertObject.create();
			alertObject.show();
		}
		super.onCreate(savedInstanceState);
	}

	private void getImagefiles() {
		imageModellist = ImageTable.fetchImagesList(this);
		if (imageModellist != null) {
			Log.d("activity", "" + imageModellist.get(0).getCardThumb());
			Log.d("id", "" + imageModellist.get(0).getCardId());
			for (int i = imageModellist.size() - 1; i >= 0; i--) {
				File file = new File(imageModellist.get(i).getCardThumb());
				if (!file.exists()) {
					imageModellist.remove(i);
			}
		}
	}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int cardId =((ImageModel)gridView.getAdapter().getItem(position)).getCardId();	
		ImageModel rowObject=ImageTable.fetchimageRow(this, cardId);
		Log.e("id", ""+rowObject.getCardId());
		Intent intent = new Intent(this,EditTemplateActivity.class);
		intent.putExtra("Activity", "SavedCardActivity");
		intent.putExtra("sign", rowObject.getSignImage());
		intent.putExtra("image", rowObject.getEdittedImage());
		intent.putExtra("text", rowObject.getText());
		intent.putExtra("originalimage", rowObject.getOriginalImage());
		intent.putExtra("position", rowObject.getFrameId());
		intent.putExtra("color", rowObject.getColor());
		intent.putExtra("xtext", rowObject.getxText());
		intent.putExtra("ytext", rowObject.getyText());
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		mode.getMenuInflater().inflate(R.menu.contextualmenu, menu);
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.item_delete:
		//Set<Integer> set = new HashSet<Integer>();
		List<Integer> set= new ArrayList<Integer>();
		set.addAll(adapter.getCurrentCheckedPosition());
		Collections.sort(set);
		Collections.reverse(set);
		Iterator<Integer> iterator=set.iterator();
		while(iterator.hasNext()){
		adapter.removeSelectedFromList(iterator.next());
		}
		adapter.notifyDataSetChanged();
		noOfSelection = 0;
		mode.finish();
		}
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		// TODO Auto-generated method stub
		 if (checked) {
             noOfSelection++;
             adapter.setNewSelection(position, checked);                   
         } else {
        	 noOfSelection--;
        	 mode.setTag(position);
             adapter.removeSelection(position);                
         }
         mode.setTitle(noOfSelection + " selected");
       
 }
	
}
