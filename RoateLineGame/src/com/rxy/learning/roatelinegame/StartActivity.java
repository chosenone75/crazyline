package com.rxy.learning.roatelinegame;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity {
	private TextView dif1textView = null;
	private TextView dif2TextView = null;
	private TextView dif3TextView = null;
	private TextView voiceTextView = null;
	private TextView settingTextView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);

		initView();
	}

	private void initView() {
		dif1textView = (TextView) findViewById(R.id.dif_1);
		dif1textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartActivity.this,
						GameActivity.class);
				intent.putExtra("dif", 3.0);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		dif2TextView = (TextView) findViewById(R.id.dif_2);
		dif2TextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartActivity.this,
						GameActivity.class);
				intent.putExtra("dif", 2.0);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		dif3TextView = (TextView) findViewById(R.id.dif_3);
		dif3TextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(StartActivity.this,
						GameActivity.class);
				intent.putExtra("dif", 1.0);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
		voiceTextView = (TextView) findViewById(R.id.voice);
		voiceTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (voiceTextView.getText().equals("音效(On)")) {
					// 保存音效设置 写入sharedpreferences

					voiceTextView.setText("音效(Off)");
				} else {
					// 保存音效设置 写入sharedpreferences

					voiceTextView.setText("音效(On)");
				}
			}
		});
		settingTextView = (TextView) findViewById(R.id.setting);
		settingTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(StartActivity.this,
						SettingActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
	}
	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(StartActivity.this, "再按一次退出",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				System.exit(0);
			}
		}
		return true;
	}

}
