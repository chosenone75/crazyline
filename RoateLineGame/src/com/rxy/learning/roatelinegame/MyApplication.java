package com.rxy.learning.roatelinegame;

import java.lang.reflect.Field;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.graphics.Typeface;

public class MyApplication extends Application {
	
	private static Typeface mTypeFace = null;
	
	//volley请求队列
	private static RequestQueue mRequestQueue = null;
	@Override
	public void onCreate() {
		super.onCreate();
		//初始化请求队列
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		//设置字体
		mTypeFace = Typeface.createFromAsset(getAssets(), "fonts/MFYueHei_Noncommercial-Regular.otf");
		try
		{
			Field field = Typeface.class.getDeclaredField("SERIF");
			field.setAccessible(true);
			field.set(null, mTypeFace);
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
	
	public static RequestQueue getQueue(){
		return mRequestQueue;
	}
}
