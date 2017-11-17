package com.qburst.greetingcardapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint({ "DrawAllocation", "WrongCall" })
public class DrawingView extends View {
	private static final String COLOR_KEY = "color";
	private Paint paint=new Paint();
	private static Path path = new Path();
	private Bitmap signImage;
	private Context context;
	private int paintColor;
	private String MyPREFERENCES = "colorPref";
	public DrawingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		paintColor = Color.BLACK;
		paint.setColor(paintColor);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(10f);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(
				MyPREFERENCES,Context.MODE_PRIVATE);
		paintColor = sharedpreferences.getInt(COLOR_KEY, Color.BLACK);
		paint.setColor(paintColor);
		canvas.drawPath(path, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float eventX = event.getX();
		float eventY = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(eventX, eventY);
			return true;
		case MotionEvent.ACTION_MOVE:
			path.lineTo(eventX, eventY);
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			return false;
		}
		invalidate();
		return true;
	}

	public void clear(View view) {
		path.reset();
		view.invalidate();
	}

	public void setPaintColor(int color, Context context) {
		SharedPreferences sharedpreferences = context.getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sharedpreferences.edit();
		edit.putInt(COLOR_KEY, color);
		edit.commit();
	}

	public Bitmap save(View view) {
		signImage = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(signImage);
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.TRANSPARENT);
		c.drawPaint(paint);
		view.setBackgroundColor(context.getResources().getColor(
				android.R.color.white));
		view.layout(0, 0, view.getLayoutParams().width,
				view.getLayoutParams().height);
		view.draw(c);
		signImage = Bitmap.createScaledBitmap(signImage, 250, 250, true);
		return signImage;
	}
}
