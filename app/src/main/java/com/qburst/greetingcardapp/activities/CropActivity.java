package com.qburst.greetingcardapp.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.edmodo.cropper.CropImageView;
import com.qburst.greetingcardapp.R;
import com.qburst.greetingcardapp.utils.DisplayUtils;
import com.qburst.greetingcardapp.utils.Utils;

public class CropActivity extends Activity {
	Bitmap image;
	Intent intent;
	CropImageView cropImageView;
	 Button cropButton ;
	private String imagePath;
	private String croppedImagepath;
	private Animation slideUp;
	private Animation slideDown;
	private RelativeLayout cropLayout;
	private RelativeLayout cropDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cropactivity_layout);
		intent = getIntent();
		imagePath=intent.getExtras().getString("imagepath");
		Uri imageUri = Uri.fromFile(new File(imagePath));
		InputStream imageStream = null;
		try {
			imageStream = getContentResolver().openInputStream(
					imageUri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		image = DisplayUtils.getBitmap(imageStream,this);
		initialize();
		cropButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cropImage();
			}
		});
		cropLayout.startAnimation(slideDown);
		cropLayout.setVisibility(View.VISIBLE);
		cropDone.startAnimation(slideUp);
		cropDone.setVisibility(View.VISIBLE);
	}

	protected void cropImage() {
		// TODO Auto-generated method stub
		Bitmap croppedImage = cropImageView.getCroppedImage();
		croppedImagepath=Utils.saveImageforEditing(croppedImage);
		intent.putExtra("croppedImage", croppedImagepath);
		setResult(100, intent);
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		finish();
	}

	private void initialize() {
		slideUp=AnimationUtils.loadAnimation(this, R.anim.bottom_up);
		slideDown=AnimationUtils.loadAnimation(this, R.anim.up_bottom);
		cropImageView = (CropImageView) findViewById(R.id.CropImageView);
		cropImageView.setImageBitmap(image);
		cropButton = (Button) findViewById(R.id.crop_button);
		cropLayout=(RelativeLayout)findViewById(R.id.croplayout);
		cropDone=(RelativeLayout)findViewById(R.id.cropdone);
	}

}
