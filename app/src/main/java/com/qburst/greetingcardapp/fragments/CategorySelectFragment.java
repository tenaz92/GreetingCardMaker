package com.qburst.greetingcardapp.fragments;

import com.qburst.greetingcardapp.activities.MainActivity;
import com.qburst.greetingcardapp.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CategorySelectFragment extends Fragment implements OnClickListener {
	private Button categorybutton1;
	private Button categorybutton2;
	private Button categorybutton3;
	private View view;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.categoryselectfragment_layout, container, false);
	initialize();
	try
	{
	categorybutton1.setOnClickListener(this);
	categorybutton2.setOnClickListener(this);
	categorybutton3.setOnClickListener(this);
	}
	catch(Exception e){}
	return view;
}
private void initialize() {
	categorybutton1=(Button)view.findViewById(R.id.category_button1);
	categorybutton2=(Button)view.findViewById(R.id.category_button2);
	categorybutton3=(Button)view.findViewById(R.id.category_button3);
}
@Override
public void onClick(View v) {
	switch(v.getId()){
	case R.id.category_button1:callParent(1);
								break;
								
	case R.id.category_button2:callParent(2);
								break;
	}
	
}
public void callParent(int type){
	Activity parentActivity=getActivity();
	((MainActivity)parentActivity).secfrag(type,this);
}
}