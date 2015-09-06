package com.rxy.learning.roatelinegame;

import com.rxy.learning.roatelinegame.RoateLineView.OnRoateListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity {

	private RoateLineView mRoateLineView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}
	private void initView() {

		mRoateLineView = (RoateLineView) findViewById(R.id.roateview);
		mRoateLineView.setOnRoateListener(new OnRoateListener() {
			
			@Override
			public void OnStartListener() {
				
			}
			
			@Override
			public void OnDeadListener() {
                	handler.sendEmptyMessage(0x123);			
			}
		});
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:
				Toast.makeText(getApplicationContext(), "ƒ„ ‰¡À", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		};
	};
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
