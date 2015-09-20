package com.rxy.learning.roatelinegame;

import java.util.Date;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OpinionActivity extends Activity {

	private EditText editText = null;
	private TextView textView = null;
	private static final String FEED_BACK_ADDRESS = "http://1.rxylearningandroid.sinaapp.com/roatelinegame.php";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_opinion);
        initView();
	}

	private void initView() {
         editText = (EditText) findViewById(R.id.opinion_edit);
         textView = (TextView) findViewById(R.id.submit_tv);
         textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                    if(editText.getText().toString().trim().equals("")){
                    	Toast.makeText(OpinionActivity.this, "����������", Toast.LENGTH_SHORT).show();
                    }else{
                    	submitOpinion(editText.getText().toString().trim());
                    }
			}
		});
         
	}
	private void submitOpinion(String opinion){
		Date date = new Date();
		String time = (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+(date.getDate());
		System.out.println(opinion);
		volley_Get(FEED_BACK_ADDRESS+"?user="+getInfo()+"&details="+opinion.replace("/n", "")+"&time="+time);
	}
	
	private String getInfo() {
//		TelephonyManager mTm = (TelephonyManager) this
//				.getSystemService(TELEPHONY_SERVICE);
//		String imei = mTm.getDeviceId();
//		String imsi = mTm.getSubscriberId();
//		String mtype = android.os.Build.MODEL; // �ֻ��ͺ�
		String mtyb = android.os.Build.BRAND;// �ֻ�Ʒ��
//		String numer = mTm.getLine1Number(); // �ֻ����룬�еĿɵã��еĲ��ɵ�
//		Log.i("text", "�ֻ�IMEI�ţ�" + imei + "�ֻ�IESI�ţ�" + imsi + "�ֻ��ͺţ�" + mtype
//				+ "�ֻ�Ʒ�ƣ�" + mtyb + "�ֻ�����" + numer);
        return mtyb;
	}

	public void volley_Get(String url) {

		StringRequest sr = new StringRequest(Method.GET, url,
				new Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						Toast.makeText(OpinionActivity.this, "��л����������", Toast.LENGTH_SHORT).show();
						finish();
						overridePendingTransition(android.R.anim.fade_in,
								android.R.anim.fade_out);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						
						Log.i("Error", arg0.toString());
					}
				});
		sr.setTag("volleyget");
		MyApplication.getQueue().add(sr);
		MyApplication.getQueue().start();
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
