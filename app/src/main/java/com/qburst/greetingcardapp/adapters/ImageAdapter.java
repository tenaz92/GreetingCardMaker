package com.qburst.greetingcardapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.activities.EditPhotoActivity;
import com.qburst.greetingcardapp.interfaces.ImageAdapterEditPhotoListener;

public class ImageAdapter extends PagerAdapter {
	private Context context;
	private View itemview;
	int[] mResources;
	ImageAdapterEditPhotoListener Listener;
	Activity activity;

	public ImageAdapter(Context context, int[] mResources, EditPhotoActivity activity) {
		this.context = context;
		this.mResources = mResources;
		
//		setListener(activity);
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mLayoutInflater != null) {
			itemview = mLayoutInflater.inflate(R.layout.viewpager_layout,
					container, false);
		}
		ImageView imageView = (ImageView) itemview.findViewById(R.id.viewpagertemplate);
		imageView.setImageResource(mResources[position]);
		Log.e("TAGs", "This page is about to be clicked: " + position);
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					Listener.filterSelected(position);
				Log.i("TAG", "This page was clicked: " + position);
			}
		});
		container.addView(imageView);
		return imageView;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	public void setListener(ImageAdapterEditPhotoListener listener) {
		Listener = listener;
	}
	@Override
	public int getCount() {
		if(mResources.length!=0){
		return mResources.length;
	}
		else{
			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

}
