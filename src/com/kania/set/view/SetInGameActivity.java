package com.kania.set.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kania.set.R;
import com.kania.set.SetGameInfo;
import com.kania.set.presenter.SetPresenter;

public class SetInGameActivity extends Activity implements View.OnClickListener, ISetPannelAction {
	SetPresenter presenter;
	
	private int[] cardIds = {R.id.setCard1, R.id.setCard2, R.id.setCard3, 
			R.id.setCard4, R.id.setCard5, R.id.setCard6, 
			R.id.setCard7, R.id.setCard8, R.id.setCard9
			};
	private int[] cardBGIds = {R.id.setBGCard1, R.id.setBGCard2, R.id.setBGCard3, 
			R.id.setBGCard4, R.id.setBGCard5, R.id.setBGCard6, 
			R.id.setBGCard7, R.id.setBGCard8, R.id.setBGCard9
			};
	private Button mBtnHint;
	private ImageView mNotiImage;
	private TextView mSumScore;
	private TextView mAddedScore;
	private TextView mRemainTime;
	
	private RelativeLayout mFrameInputName;
	private EditText mEditInputName;
	private Button mBtnInputName;
	
	private int mLongAnimationDuration;
	private int mShortAnimationDuration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		presenter = new SetPresenter(this);
		
		Intent intent = getIntent();
		int difficulty = intent.getIntExtra(SetGameInfo.DIFFICULTY_NAME, 0);
		presenter.setDifficulty(difficulty);
		
		for (int i = 0; i < cardIds.length; ++i) {
			SetCardView scv = (SetCardView) findViewById(cardIds[i]);
			FrameLayout bg = (FrameLayout) findViewById(cardBGIds[i]);
			scv.setMediator(presenter);
			scv.setCardSlotNum(i);
			scv.setBackgroundLayout(bg);
			presenter.addSetCardView(scv);
		}
		
		mBtnHint = (Button) findViewById(R.id.btnGameHint);
		mBtnHint.setOnClickListener(this);
		
		mNotiImage = (ImageView) findViewById(R.id.imgPannelNoti);
		
		mSumScore = (TextView) findViewById(R.id.textPannelSumScore);
		mAddedScore = (TextView) findViewById(R.id.textPannelAddedScore);
		mRemainTime = (TextView) findViewById(R.id.textPannelTime);
		
		mFrameInputName = (RelativeLayout) findViewById(R.id.frameInputName);
		mEditInputName = (EditText) findViewById(R.id.editInputName);
		mBtnInputName = (Button) findViewById(R.id.btnInputName);
		mBtnInputName.setOnClickListener(this);
		
		presenter.setPannel(this);
		
		//retrive and cache the system's default "short" animation time.
		 mLongAnimationDuration = mShortAnimationDuration = 2000;
		//mLongAnimationDuration = getResources().getInteger(android.R.integer.config_longAnimTime);
		//mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
		
		
		//if prepared all settings, start game
		presenter.startGame();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnGameHint:
			presenter.pushHint();
			break;
		case R.id.btnInputName:
			String username = mEditInputName.getText().toString();
			if (username == null || "".equals(username))
				username = "Unknown";
			presenter.inputUserName(username);
//			Toast.makeText(this, "It is not implemented yet", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void setSumScore(int sumScore) {
		mSumScore.setText("Score : " + sumScore);
	}

	@Override
	public void setAddedScore(int addedScore) {
		mAddedScore.setText(" +" + addedScore);
	}

	@Override
	public void setNotiImage(boolean status) {
		if (status) {
			mNotiImage.setImageDrawable(getResources().getDrawable(R.drawable.answer_right));
			crossfadeAddedScore();
		} else {
			mNotiImage.setImageDrawable(getResources().getDrawable(R.drawable.answer_wrong));
		}
		crossfadeNotiImage();
	}
	
	private void crossfadeAddedScore() {
		mAddedScore.setAlpha(1f);
		mAddedScore.animate()
			.alpha(0f)
			.setDuration(2000)
			.setListener(null);
	}
	
	private void crossfadeNotiImage() {
		mNotiImage.setAlpha(1f);
		mNotiImage.animate()
			.alpha(0f)
			.setDuration(mLongAnimationDuration)
			.setListener(null);
	}
	
	@Override
	public void setEnableHint(boolean enabled) {
		mBtnHint.setEnabled(enabled);
	}
	
	@Override
	public void setRemainTime(String seconds) {
		mRemainTime.setText(seconds);
	}
	
	@Override
	public void setInputNameEnable(boolean enabled) {
		mFrameInputName.setVisibility(enabled ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public void finishGame() {
		finish();
	}

}
