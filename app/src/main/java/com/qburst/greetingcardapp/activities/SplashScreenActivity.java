package com.qburst.greetingcardapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.qburst.greetingcardapp.R;

public class SplashScreenActivity extends Activity{
	  private static int SPLASH_TIME_OUT = 3000;
	private ImageView splashScreenImage;
	private final Handler handler = new Handler();
	private final Runnable startActivityRunnable = new Runnable() {

	    @Override
	    public void run() {
	        Intent intent = new Intent();
	            intent.setClass(SplashScreenActivity.this,MainActivity.class);
	        startActivity(intent);
	        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
	        finish();
	    }
	}; 

	  
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.splashscreen_layout);
	        splashScreenImage=(ImageView)findViewById(R.id.splashscreenicon);
	        handler.postDelayed(startActivityRunnable, 2000);
	   
	}
}

