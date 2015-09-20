package com.rxy.learning.roatelinegame;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	private int INTERVAL = 1;
    private int COUNT_RADUIS_SHADOW = 1;
	// 测试数据

	private volatile int TouchX = -150;
	private volatile int TouchY = -220;
	private boolean isTouched = false;
	private int secAngle;
	private int SEC_LENGTH_OF_LINE;

	private SharedPreferences sharedPreferences = null;
	private Editor editor = null;
	//分数相关
	private int score = 0;
	private int record = 0;
	//碰撞效果相关
	private static final String[] colors = { "#33B5E5", "#0099CC", "#AA66CC",
		"#9933CC", "#99CC00", "#669900", "#FFBB33", "#FF8800", "#FF4444",
		"#CC0000" };
	
	//游戏结束相关
	private int deadCount = 0;
	private RoateThread mRoateThread = null;

	public RoateLineView(Context context) {
		this(context, null, 0);
	}

	public RoateLineView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoateLineView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		
		balls = new ArrayList<RoateLineView.Ball>();
        sharedPreferences = context.getSharedPreferences("scores",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        record = sharedPreferences.getInt("dif"+COUNT_RADUIS_SHADOW, 0);
		mRoateThread = new RoateThread();
	}

	
	public void Start(){
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
		
		//碰撞产生的小球
		drawBalls(canvas, paint);
	}

	private void drawBalls(Canvas canvas, Paint paint) {
		for (int i = 0; i < balls.size(); i++) {
			paint.setColor(balls.get(i).color);
			canvas.drawCircle(balls.get(i).x, balls.get(i).y, (float) (RADUIS_OF_SHADOW *2.0 / 2.5), paint);
		}
	}

	private Typeface mTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/MFYueHei_Noncommercial-Regular.otf");
	private void drawScore(Canvas canvas, Paint paint) {
		
		paint.setColor(Color.parseColor("#52ae65"));
		paint.setTypeface(mTypeface);
		paint.setTextSize((float) (WINDOW_WIDTH*1.0/12));
		canvas.drawText("得分:"+score, WINDOW_WIDTH/10, WINDOW_HEIGHT/10, paint);
		canvas.drawText("记录:"+record, WINDOW_WIDTH*7/10, WINDOW_HEIGHT/10, paint);
	}

	private void drawTarget(Canvas canvas, Paint paint) {
		paint.setColor(Color.parseColor("#8B4500"));
		canvas.drawCircle(
				(float) (CENTER_X + targetLength
						* Math.cos(Math.toRadians(targetAngle))),
				(float) (CENTER_Y + targetLength
						* Math.sin(Math.toRadians(targetAngle))),
				(float) (COUNT_RADUIS_SHADOW * RADUIS_OF_SHADOW), paint);
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
		canvas.drawCircle(TouchX, TouchY, 2*RADUIS_OF_SHADOW, paint);
	}// ///////////////////

	private void drawShadow(Canvas canvas, Paint paint) {
		paint.setColor(Color.RED);
		canvas.drawCircle(CENTER_X, CENTER_Y, RADUIS_OF_SHADOW, paint);

	}

	private void drawLine(int curAngle2, Canvas canvas, Paint paint) {
		// Log.i(tag, "OnDraw");
		paint.setColor(Color.parseColor("#000000"));
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2.0f);
		canvas.drawLine(
				CENTER_X,
				CENTER_Y,
				(float) (CENTER_X + LENGTH_OF_LINE
						* Math.cos(Math.toRadians(curAngle2))),
				(float) (CENTER_Y + LENGTH_OF_LINE
						* Math.sin(Math.toRadians(curAngle2))), paint);
		if(!isTouched)
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

		getTargetLocation();
		
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

	private boolean isOn = true;
	
	public void restart(){
		score = 0;
		curAngle = 0;
		deadCount = 0;
		isTouched = false;
	    balls.clear();
	    isOn = true;
	    
		mRoateThread = null;
		mRoateThread = new RoateThread();
		mRoateThread.start();
	}
	private class RoateThread extends Thread {
		private int count = 0;
		private boolean onlyonce = true;
		@Override
		public void run() {
			while (isOn) {
				while (!isMeasureOver) {
				}

				if (isTouched || checkTouchPoint()) {
					if (isTouched) {
						secAngle = (secAngle + 1) % 360;

						/*
						 * 碰撞检测
						 */
						if (onlyonce && hasCollisionAndWin()) {
							
		Log.i("碰撞", "成功");
							/*
							 * 生成碰撞效果
							 */
							
							hasTarget = false;
							
							
//							secAngle = curAngle;
							//更新分数
							score += 5;
							
							if(score >=record)
								record = score;
							
							onlyonce = false;
							
							addBalls();
						}
						if (secAngle == curAngle) {
							isTouched = false;
							TouchX = -150;
							TouchY = -150;
							LENGTH_OF_LINE = tmp;
							
							if(hasTarget){
								if(++deadCount >= 3){
									
									Log.i("dead", "挂了");
									if(mOnRoateListener != null){
										mOnRoateListener.OnDeadListener();
									}
									isOn = false;
									
									editor.putInt("dif"+COUNT_RADUIS_SHADOW, record);
									editor.commit();
								}
							}
							
							getTargetLocation();
							onlyonce = true;
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
				if(count == 30){
					updateBallPosition();
				    count = 0;
				}
				postInvalidate();
				try {
					sleep(INTERVAL);
					count+=INTERVAL;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 模拟自由上抛运动的小球 ，数据集合
		private class Ball {
			float x;// x坐标
			float y;// y坐标
			int vx;// x方向速度
			int vy;// y方向速度
			int g;// 下落加速度 g
			int color;// 小球的填充色
		}
		private OnRoateListener mOnRoateListener  = null;
		public void setOnRoateListener(OnRoateListener mOnRoateListener) {
			this.mOnRoateListener = mOnRoateListener;
		}
	//自定义回调接口
		public interface OnRoateListener{
			public abstract void OnDeadListener();
			public abstract void OnStartListener();
		}
	private ArrayList<Ball> balls;
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

	private static final float u = 0.5f;
	public void updateBallPosition() {
		for (int i = 0; i < balls.size(); i++) {
			balls.get(i).x += balls.get(i).vx;
			balls.get(i).y += balls.get(i).vy;
			balls.get(i).vy += balls.get(i).g;
			if (balls.get(i).y >= WINDOW_HEIGHT - RADUIS_OF_SHADOW *2.0/3) {
				balls.get(i).y = (float) (WINDOW_HEIGHT - RADUIS_OF_SHADOW *2.0/3);
				balls.get(i).vy = (int) (-balls.get(i).vy * u);
			}
		}

		// 性能优化
		int count = 0;
		for (int i = 0; i < balls.size(); i++) {
			if (balls.get(i).x + RADUIS_OF_SHADOW *2.0/3 > 0
					&& balls.get(i).x - RADUIS_OF_SHADOW *2.0/3 < WINDOW_WIDTH) {
				balls.set(count++, balls.get(i));
			}
		}
		for (int j = count; j < balls.size(); j++) {
			balls.remove(j);
		}
		// 具体ArrayList大小与特定绘图区域大小相关
		// Log.i(tag, balls.size()+"");
	}

	public void addBalls() {
		float x = (float) (CENTER_X + targetLength
				* Math.cos(Math.toRadians(targetAngle)));
		float y = (float) (CENTER_Y + targetLength
				* Math.sin(Math.toRadians(targetAngle)));
		int count = (int) (4 + Math.random()* 4); 
		for(int i = 0 ;i<count;i++){
			Ball tmp = new Ball();
			tmp.x = x;
			tmp.y = y;
			tmp.g = (int) (1.5 + Math.random());
			tmp.vx = Math.random() > 0.5 ? -4 : 4;
			tmp.vy = -5;
			tmp.color = Color
					.parseColor(colors[(int) (Math.random() * 10)]);
			balls.add(tmp);
		}
	}

	public int getScore() {
		return score;
	}
	public void setDif(int dif) {
		INTERVAL = dif;
		COUNT_RADUIS_SHADOW = dif;
		
		 record = sharedPreferences.getInt("dif"+dif, 0);
	}

	public boolean hasCollisionAndWin() {

//		double vDistance =  (targetLength * Math.sin(Math.toRadians(targetAngle
//				- secAngle)));
		float x1 = (float) (TouchX + SEC_LENGTH_OF_LINE
				* Math.cos(Math.toRadians(secAngle)));
		float y1 = (float) (TouchY + SEC_LENGTH_OF_LINE
				* Math.sin(Math.toRadians(secAngle)));
		float x2 = (float) (CENTER_X + targetLength
				* Math.cos(Math.toRadians(targetAngle)));
		float y2 = (float) (CENTER_Y + targetLength
				* Math.sin(Math.toRadians(targetAngle)));

		double cDistance =  Math.sqrt(Math.pow(x1 - x2, 2)
				+ Math.pow(y1 - y2, 2));
		if ( cDistance <= (COUNT_RADUIS_SHADOW+1) * RADUIS_OF_SHADOW) {

			return true;
		}

		return false;
	}

	private int targetAngle = 0;
	private int targetLength;
	private boolean hasTarget = false;

	private void getTargetLocation() {
		targetAngle = (int) (Math.random() * 360);
		targetLength = (int) (LENGTH_OF_LINE * 0.2 + Math.random()
				* (LENGTH_OF_LINE *0.7));
		hasTarget = true;
	}
	@Override
	protected void onDetachedFromWindow() {
		editor.putInt("dif"+COUNT_RADUIS_SHADOW, record);
		editor.commit();
		super.onDetachedFromWindow();
	}
}
