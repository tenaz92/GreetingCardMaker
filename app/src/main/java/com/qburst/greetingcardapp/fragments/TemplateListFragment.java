package com.qburst.greetingcardapp.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.activities.EditTemplateActivity;
import com.qburst.greetingcardapp.adapters.MyPagerAdapter;
import com.qburst.greetingcardapp.interfaces.TemplateListFragmentListener;

public class TemplateListFragment extends Fragment implements
		TemplateListFragmentListener {
	private View view;
	private PagerContainer mContainer;
	private ViewPager pager = null;
	private int[] mResources;
	int type = 0;
	private int[] mBirthdayResources = { R.drawable.birthday1,
			R.drawable.birthday2};
	private int[] mChristmasResources = { R.drawable.christmas1,R.drawable.christmas2 };

	public TemplateListFragment() {

	}

	public TemplateListFragment(int type2) {
		this.type = type2;
		if (this.type == 1) {
			this.mResources = mBirthdayResources;
		} else if (this.type == 2) {
			this.mResources = mChristmasResources;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("listoffragments", "reached");
		view = inflater.inflate(R.layout.listoftemplates_layout, container,
				false);
		initialize();
		pager = mContainer.getViewPager();
		PagerAdapter adapter = new MyPagerAdapter(getActivity(), mResources,
				this);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(adapter.getCount());
		pager.setPageMargin(0);
		pager.setClipChildren(false);
		return view;
	}

	private void initialize() {
		mContainer = (PagerContainer) view.findViewById(R.id.pager_container1);
	}

	@Override
	public void templateSelected(int position) {
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		Intent intent = new Intent(getActivity(), EditTemplateActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
		getActivity()
				.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
}
