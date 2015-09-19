package com.rxy.learning.roatelinegame;

import com.rxy.learning.roatelinegame.RoateLineView.OnRoateListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class GameActivity extends Activity {

	private RoateLineView mRoateLineView = null;
	private double dif = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_game);
		Intent inent = getIntent();
		dif = inent.getDoubleExtra("dif",3.0);
		initView();
	}
	private void initView() {

		mRoateLineView = (RoateLineView) findViewById(R.id.roateview);
		mRoateLineView.setDif((int) dif);
		mRoateLineView.Start();
		
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

	private void showDialog(Context context) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setIcon(R.drawable.ic_launcher);  
        builder.setTitle("AlterDialog");  
        builder.setMessage("                     逗比，你挂啦");  
        builder.setPositiveButton("重新开始",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        mRoateLineView.restart();
                    }  
                });  
        builder.setNegativeButton("退出",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	GameActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    	System.out.println("exit");
                    }  
                });  
        builder.show();  
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
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:
				Toast.makeText(getApplicationContext(), "你输了", Toast.LENGTH_SHORT).show();
				//此处弹出对话框
				
				showDialog(GameActivity.this);
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
