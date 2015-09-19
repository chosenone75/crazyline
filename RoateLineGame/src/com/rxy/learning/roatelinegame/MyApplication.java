package com.rxy.learning.roatelinegame;

import java.lang.reflect.Field;

import android.app.Application;
import android.graphics.Typeface;

public class MyApplication extends Application {
	
	private static Typeface mTypeFace = null;
	@Override
	public void onCreate() {
		super.onCreate();
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
}
