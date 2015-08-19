package com.kania.set.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kania.set.R;
import com.kania.set.presenter.SetPresenter;

public class SetInGameActivity extends Activity implements View.OnClickListener {
	SetPresenter presenter;
	
	private int[] cardIds = {R.id.setCard1, R.id.setCard2, R.id.setCard3, 
			R.id.setCard4, R.id.setCard5, R.id.setCard6, 
			R.id.setCard7, R.id.setCard8, R.id.setCard9
			};
	Button btnHint;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ingame);
		presenter = new SetPresenter();
		
		for (int i = 0; i < cardIds.length; ++i) {
			SetCardView scv = (SetCardView)findViewById(cardIds[i]);
			scv.setMediator(presenter);
			scv.setCardSlotNum(i+1);
			presenter.addSetCardView(scv);
		}
		
		btnHint = (Button)findViewById(R.id.btnGameHint);
		btnHint.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//debug
		switch (v.getId()) {
		case R.id.btnGameHint:
			presenter.startGame();
			break;
		}
		
	}
	

}
