package com.rxy.learning.roatelinegame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RoateLineView extends View {
	private static final String tag = "RoateLineView";

	private int WINDOW_WIDTH;
	private int WINDOW_HEIGHT;
	private float CENTER_X;
	private float CENTER_Y;
	private int LENGTH_OF_LINE;
	private static final int RADUIS_OF_SHADOW = 5;
	private int curAngle;
	private int INTERVAL = 2;

	// 测试数据

	private volatile int TouchX = 150;
	private volatile int TouchY = 220;
	private boolean isTouched = false;
	private int secAngle;
	private int SEC_LENGTH_OF_LINE;

	
	//分数相关
	private int score = 0;
	
	private RoateThread mRoateThread = null;

	public RoateLineView(Context context) {
		this(context, null, 0);
	}

	public RoateLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoateLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mRoateThread = new RoateThread();
		mRoateThread.start();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		Paint paint = new Paint();
		
		drawScore(canvas,paint);
		
		
		drawShadow(canvas, paint);
		drawLine(curAngle, canvas, paint);

		if (isTouched)
			drawSecLine(canvas, paint);

		drawTouch(canvas, paint);// /////////////////

		if (hasTarget)
			drawTarget(canvas, paint);
	}

	private Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/MFYueHei_Noncommercial-Regular.otf");
	private void drawScore(Canvas canvas, Paint paint) {
		
		paint.setColor(Color.parseColor("#52ae65"));
		paint.setTypeface(mTypeface);
		paint.setTextSize((float) (WINDOW_WIDTH*1.0/12));
		canvas.drawText("得分:"+score, WINDOW_WIDTH/10, WINDOW_HEIGHT/10, paint);
		
	}

	private void drawTarget(Canvas canvas, Paint paint) {
		paint.setColor(Color.MAGENTA);
		canvas.drawCircle(
				(float) (CENTER_X + targetLength
						* Math.cos(Math.toRadians(targetAngle))),
				(float) (CENTER_Y + targetLength
						* Math.sin(Math.toRadians(targetLength))),
				(float) (2 * RADUIS_OF_SHADOW), paint);
	}

	// ///////////////////////////////
	private void drawSecLine(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		canvas.drawLine(
				TouchX,
				TouchY,
				(float) (TouchX + SEC_LENGTH_OF_LINE
						* Math.cos(Math.toRadians(secAngle))),
				(float) (TouchY + SEC_LENGTH_OF_LINE
						* Math.sin(Math.toRadians(secAngle))), paint);
		canvas.drawCircle(
				(float) (TouchX + SEC_LENGTH_OF_LINE
						* Math.cos(Math.toRadians(secAngle))),
				(float) (TouchY + SEC_LENGTH_OF_LINE
						* Math.sin(Math.toRadians(secAngle))),
				RADUIS_OF_SHADOW, paint);
	}

	private void drawTouch(Canvas canvas, Paint paint) {
		paint.setColor(Color.BLUE);
		canvas.drawCircle(TouchX, TouchY, 15, paint);
	}// ///////////////////

	private void drawShadow(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		canvas.drawCircle(CENTER_X, CENTER_Y, RADUIS_OF_SHADOW, paint);

	}

	private void drawLine(int curAngle2, Canvas canvas, Paint paint) {
		// Log.i(tag, "OnDraw");
		paint.setColor(Color.parseColor("#000000"));
		canvas.drawLine(
				CENTER_X,
				CENTER_Y,
				(float) (CENTER_X + LENGTH_OF_LINE
						* Math.cos(Math.toRadians(curAngle2))),
				(float) (CENTER_Y + LENGTH_OF_LINE
						* Math.sin(Math.toRadians(curAngle2))), paint);
		canvas.drawCircle(
				(float) (CENTER_X + LENGTH_OF_LINE
						* Math.cos(Math.toRadians(curAngle2))),
				(float) (CENTER_Y + LENGTH_OF_LINE
						* Math.sin(Math.toRadians(curAngle2))),
				2 * RADUIS_OF_SHADOW, paint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		WINDOW_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
		WINDOW_HEIGHT = MeasureSpec.getSize(heightMeasureSpec);

		CENTER_X = (float) (WINDOW_WIDTH / 2.0);
		CENTER_Y = (float) (WINDOW_HEIGHT / 2.0);

		if (LENGTH_OF_LINE == 0)
			LENGTH_OF_LINE = ((WINDOW_WIDTH < WINDOW_HEIGHT ? WINDOW_WIDTH
					: WINDOW_HEIGHT) - 2 * RADUIS_OF_SHADOW - 6) / 2;

		curAngle = 0;

		isMeasureOver = true;
		// Log.i(tag, "width height x y length "+
		// WINDOW_WIDTH+" "+WINDOW_HEIGHT+" "+CENTER_X+" "+CENTER_Y+" "+LENGTH_OF_LINE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isTouched) {
			TouchY = (int) event.getY();
			TouchX = (int) event.getX();
		}
		return true;
	}

	private boolean isMeasureOver = false;

	private class RoateThread extends Thread {
		@Override
		public void run() {
			while (true) {
				while (!isMeasureOver) {
				}

				if (isTouched || checkTouchPoint()) {
					if (isTouched) {
						secAngle = (secAngle + 1) % 360;

						/*
						 * 碰撞检测
						 */
						if (hasCollisionAndWin()) {
							Log.i("碰撞", "成功");
							/*
							 * 生成碰撞效果
							 */
							hasTarget = false;
							
							getTargetLocation();
							
//							secAngle = curAngle;
							
							score += 5;
						}
						if (secAngle == curAngle) {
							isTouched = false;
							TouchX = 150;
							TouchY = 150;
							LENGTH_OF_LINE = tmp;
						}
					} else {
						secAngle = curAngle;
						isTouched = true;

						//
						if(!hasTarget)
						getTargetLocation();
						//

					}

				} else {
					curAngle = (curAngle + 1) % 360;
				}

				postInvalidate();
				try {
					sleep(INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int tmp;

	/*
	 * 判断触摸点是否在直线上
	 */
	public boolean checkTouchPoint() {
		double result1 = ((TouchX - CENTER_X) * 1.0 / (TouchY - CENTER_Y));
		double result2 = ((CENTER_X + LENGTH_OF_LINE
				* Math.cos(Math.toRadians(curAngle)) - CENTER_X) * 1.0 / (CENTER_Y
				+ LENGTH_OF_LINE * Math.sin(Math.toRadians(curAngle)) - CENTER_Y));
		double endX = CENTER_X + LENGTH_OF_LINE
				* Math.cos(Math.toRadians(curAngle));
		double endY = (CENTER_Y + LENGTH_OF_LINE
				* Math.sin(Math.toRadians(curAngle)));

		double angle1 = Math.atan(result1);
		double angle2 = Math.atan(result2);
		if (-1 < TouchY - CENTER_Y && TouchY - CENTER_Y < 1) {
			Log.i(tag, Math.atan(result1) + "");
			Log.i(tag, "" + Math.atan(result2));
		}

		if ((Math.abs(result2 - result1) < 0.03 || Math.abs(angle1 - angle2) <= 0.03)
				&& Math.sqrt(Math.pow(TouchX - CENTER_X, 2)
						+ Math.pow(TouchY - CENTER_Y, 2))
						+ Math.sqrt(Math.pow(endX - TouchX, 2)
								+ Math.pow(endY - TouchY, 2)) < LENGTH_OF_LINE + 5) {

			tmp = LENGTH_OF_LINE;
			LENGTH_OF_LINE = (int) Math.sqrt(Math.pow(TouchX - CENTER_X, 2)
					+ Math.pow(TouchY - CENTER_Y, 2));
			SEC_LENGTH_OF_LINE = tmp - LENGTH_OF_LINE;

//			Log.i("!!!!---1", ((TouchX - CENTER_X) * 1.0 / (TouchY - CENTER_Y))
//					+ "");
//			Log.i("!!!!!---2",
//					((CENTER_X + LENGTH_OF_LINE
//							* Math.cos(Math.toRadians(curAngle)) - CENTER_X) * 1.0 / (CENTER_Y
//							+ LENGTH_OF_LINE
//							* Math.sin(Math.toRadians(curAngle)) - CENTER_Y))
//							+ "");
			return true;
		}
		return false;
	}

	public boolean hasCollisionAndWin() {

		int vDistance = (int) (targetLength * Math.sin(Math.toRadians(secAngle
				- targetAngle)));
		float x1 = (float) (TouchX + SEC_LENGTH_OF_LINE
				* Math.cos(Math.toRadians(secAngle)));
		float y1 = (float) (TouchY + SEC_LENGTH_OF_LINE
				* Math.sin(Math.toRadians(secAngle)));
		float x2 = (float) (CENTER_X + targetLength
				* Math.cos(Math.toRadians(targetAngle)));
		float y2 = (float) (CENTER_Y + targetLength
				* Math.sin(Math.toRadians(targetLength)));

		int cDistance = (int) Math.sqrt(Math.pow(x1 - x2, 2)
				+ Math.pow(y1 - y2, 2));
		if (vDistance <= 2 * RADUIS_OF_SHADOW
				&& cDistance <= 3 * RADUIS_OF_SHADOW) {

			return true;
		}

		return false;
	}

	private int targetAngle = 0;
	private int targetLength;
	private boolean hasTarget = false;

	private void getTargetLocation() {
		targetAngle = (int) (Math.random() * 360);
		targetLength = (int) (LENGTH_OF_LINE * 1.0 / 10 + Math.random()
				* (LENGTH_OF_LINE * 9.0 / 10));
		hasTarget = true;
	}
}
