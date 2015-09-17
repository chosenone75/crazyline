package com.rxy.learning.roatelinegame;

import com.rxy.learning.roatelinegame.RoateLineView.OnRoateListener;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

	private void showDialog(Context context) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(context);  
        builder.setIcon(R.drawable.ic_launcher);  
        builder.setTitle("AlterDialog");  
        builder.setMessage("            �����");  
        builder.setPositiveButton("���¿�ʼ",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        mRoateLineView.restart();
                    }  
                });  
        builder.setNegativeButton("�˳�",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                    	MainActivity.this.finish();
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    	System.exit(0);                      
                    	System.out.println("exit");
                    }  
                });  
        builder.show();  
    }  
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x123:
				Toast.makeText(getApplicationContext(), "������", Toast.LENGTH_SHORT).show();
				//�˴������Ի���
				
				showDialog(MainActivity.this);
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
