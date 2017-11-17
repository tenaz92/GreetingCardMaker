package com.qburst.greetingcardapp.adapters;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.database.ImageTable;
import com.qburst.greetingcardapp.models.ImageModel;

public class SavedCardAdapter extends BaseAdapter {

	private Context context;
	private List<ImageModel> listFile;
	private DisplayImageOptions options;
	private HashMap<Integer, Boolean> selection = new HashMap<Integer, Boolean>();

	public SavedCardAdapter(Context context, List<ImageModel> listFile) {
		super();
		this.context = context;
		this.listFile = listFile;
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).build();
		ImageLoader.getInstance().init(config);
	}

	public void setNewSelection(int position, boolean value) {
		selection.put(position, value);
		notifyDataSetChanged();
	}

	public void removeSelection(int position) {
		selection.remove(position);
		notifyDataSetChanged();
	}

	public boolean isPositionChecked(int position) {
		Boolean result = selection.get(position);
		return result == null ? false : result;
	}

	public Set<Integer> getCurrentCheckedPosition() {
		return selection.keySet();
	}

	public void removeSelectedFromList(int selectedImage) {
		ImageTable.removeRow(context, listFile.get(selectedImage).getCardId());
		listFile.remove(selectedImage);
		selection.remove(selectedImage);
		
	}


	@Override
	public int getCount() {
		return listFile.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.galleryitem_layout, parent,
					false);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (selection.get(position)!=null) {
			convertView.setBackgroundColor(Color.RED);
		}
		else
		{
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		ImageModel imageModel = listFile.get(position);
		ImageLoader.getInstance().displayImage(
				"file://" + imageModel.getCardThumb(), holder.imageView,
				options);
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
	}
}
