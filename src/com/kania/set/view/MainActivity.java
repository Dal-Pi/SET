package com.kania.set.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kania.set.R;
import com.kania.set.SetGameInfo;

public class MainActivity extends Activity implements View.OnClickListener {
	
	private LinearLayout layoutPannelStart;
	private LinearLayout layoutPannelDifficulty;
	private Button btnStart;
	private Button btnEasy;
	private Button btnHard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		layoutPannelStart = (LinearLayout) findViewById(R.id.pannelStart);
		layoutPannelDifficulty = (LinearLayout) findViewById(R.id.pannelDifficulty);
		
		btnStart = (Button)findViewById(R.id.btnMainStart);
		btnStart.setOnClickListener(this);
		btnEasy = (Button)findViewById(R.id.btnMainEasy);
		btnEasy.setOnClickListener(this);
		btnHard = (Button)findViewById(R.id.btnMainHard);
		btnHard.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnMainStart:
			layoutPannelStart.setVisibility(View.INVISIBLE);
			layoutPannelDifficulty.setVisibility(View.VISIBLE);
			break;
		case R.id.btnMainEasy:
			startGame(SetGameInfo.DIFFICULTY_EASY);
			break;
		case R.id.btnMainHard:
			startGame(SetGameInfo.DIFFICULTY_HARD);
			break;
		}
	}
	
	@Override
	protected void onResume() {
		layoutPannelDifficulty.setVisibility(View.INVISIBLE);
		layoutPannelStart.setVisibility(View.VISIBLE);
		super.onResume();
	}
	
	public void startGame(int difficulty) {
		Intent intent = new Intent();
		intent.setClass(this, SetInGameActivity.class);
		intent.putExtra(SetGameInfo.DIFFICULTY_NAME, difficulty);
		startActivity(intent);
	}
}
