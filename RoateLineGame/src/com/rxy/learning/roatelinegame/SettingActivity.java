package com.rxy.learning.roatelinegame;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private TextView clearTextView = null;
	private TextView aboutTextView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
		
		initView();
		
	}
	private void initView() {
		clearTextView = (TextView) findViewById(R.id.clear_tv);
		aboutTextView = (TextView) findViewById(R.id.about_tv);
		
		clearTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SettingActivity.this, "数据已清除", Toast.LENGTH_SHORT).show();
			}
		});
		aboutTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SettingActivity.this, "一款安卓平台上的创意小游戏", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		}
		return true;
	}
}
