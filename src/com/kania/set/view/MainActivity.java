package com.kania.set.view;

import com.kania.set.R;
import com.kania.set.R.id;
import com.kania.set.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {
	Button btnStart;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btnStart = (Button)findViewById(R.id.btnMainStart);
		btnStart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMainStart:
			Intent intent = new Intent();
			intent.setClass(this, SetInGameActivity.class);
			startActivity(intent);
			break;
		}
		
	}
}
