package com.rxy.learning.roatelinegame;

import java.util.Date;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends Activity {

	private TextView clearTextView = null;
	private TextView aboutTextView = null;
	private TextView feedbackTextView = null;

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
		feedbackTextView = (TextView) findViewById(R.id.feedback_tv);
		clearTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = SettingActivity.this
						.getSharedPreferences("scores", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();
				editor.clear();
				editor.commit();
				Toast.makeText(SettingActivity.this, "数据已清除",
						Toast.LENGTH_SHORT).show();
			}
		});
		aboutTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(SettingActivity.this, "一款安卓平台上的创意小游戏",
						Toast.LENGTH_SHORT).show();
			}
		});
		feedbackTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SettingActivity.this,OpinionActivity.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
		return true;
	}
}
