package com.qburst.greetingcardapp.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qburst.greetingcardapp.fragments.TemplateListFragment;
import com.qburst.greetingcardapp.interfaces.TemplateListFragmentListener;
import com.qburst.greetingcardapp.R;

public class MyPagerAdapter extends PagerAdapter {
	private Context context;
	private View itemview;
	int[] mResources;
	TemplateListFragmentListener listener;

	public MyPagerAdapter(Context context, int[] mResources,
			TemplateListFragment fragment) {
		this.context = context;
		this.mResources = mResources;
		SetListener(fragment);
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
				listener.templateSelected(mResources[position]);
				Log.i("TAG", "This page was clicked: " + position);
			}
		});
		container.addView(imageView);
		return imageView;
	}

	public void SetListener(TemplateListFragmentListener listener) {
		this.listener = listener;
	}

	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		if (mResources.length != 0) {
			return mResources.length;
		} else {
			return 0;
		}
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == object);
	}

}
