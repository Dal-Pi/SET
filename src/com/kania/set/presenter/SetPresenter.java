package com.kania.set.presenter;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.util.Log;

import com.kania.set.model.ColorProvider;
import com.kania.set.model.RandomNumberProvider;
import com.kania.set.model.SetCardData;
import com.kania.set.model.SetDeckData;
import com.kania.set.model.SetEngine;
import com.kania.set.model.SetGameData;
import com.kania.set.view.ISetCardAction;
import com.kania.set.view.ISetPannelAction;

public class SetPresenter extends Mediator {

	//	private final int MAX_CARD_AMOUNT = 9;
	private final String KEY_EVENT_TYPE = "eventtype";
	private final int TIME_EVENT = 1;
	private final int TIME_DELAY_MILLIS = 1000;
	private final int TIME_LIMIT = 61;

	private SetEngine mSetEngine;
	
	private ArrayList<ISetCardAction> mCardViews;
	private ISetPannelAction mSetPannel;
	private SetGameData mSetGameData;
	private SetDeckData mSetDeckData;
	private ArrayList<Integer> mCardPositions;
	private ArrayList<SetCardData> mCandidates;
	
	private int mHintCount;
	private int mRemainTime = 0;;
	private int mBackButtonSecondGap = 3; //for escape game view
	
	public Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			switch (data.getInt(KEY_EVENT_TYPE, 0)) {
			case TIME_EVENT:
				mRemainTime--;
				
				if (mRemainTime <= 0) {
					mSetPannel.setRemainTime("End!");
					mSetPannel.setEnableHint(false);
					mHandler.removeMessages(TIME_EVENT);
					lockAllCards();
					//TODO go to rank activity
				} else {
					mSetPannel.setRemainTime("" + mRemainTime);
					makeAndSendTimeEvent();
				}
				break;
			}
			
		};
	};

	public SetPresenter() {
		//init view and model
		mCardViews = new ArrayList<ISetCardAction>();
		mSetEngine = new SetEngine();
	}

	public void addSetCardView(ISetCardAction cardView) {
		mCardViews.add(cardView);
	}
	
	public void setPannel(ISetPannelAction pannel) {
		mSetPannel = pannel;
	}

	@Override
	public void startGame() {
		//init game
		
		
		//mutex lock
		lockAllCards();
		
		mRemainTime = TIME_LIMIT;
		
		mSetGameData = new SetGameData();
		mCandidates = new ArrayList<SetCardData>();
		mCardPositions = RandomNumberProvider.getRandomNumber(mCardViews.size());

		makeDeck(mCardViews.size());
		initAllCards();
		
		//mutex release
		releaseAllCards();
		
		//start Game using timer
		makeAndSendTimeEvent();
	}
	
	public void makeAndSendTimeEvent() {
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putInt(KEY_EVENT_TYPE, TIME_EVENT);
		msg.setData(data);
		mHandler.sendMessageDelayed(msg, TIME_DELAY_MILLIS);
	}

	@Override
	public void makeDeck(int amount) {
		//refresh color and ...TODO
		ColorProvider.refreshColors();
		//make deck
		mCardPositions.clear();
		mCardPositions = RandomNumberProvider.getRandomNumber(mCardViews.size());
		mSetDeckData = mSetEngine.getNewDeck(mCardViews.size());
		//init hint
		mHintCount = 0;
		mSetPannel.setEnableHint(true);
	}

	@Override
	public void lockAllCards() {
		for (int i = 0; i < mCardViews.size(); ++i) 
			mCardViews.get(i).lockCard();
	}
	@Override
	public void releaseAllCards() {
		Log.d("SET", "releaseAllCards start!");
		for (int i = 0; i < mCardViews.size(); ++i) 
			mCardViews.get(i).releaseCard();
		Log.d("SET", "releaseAllCards end!");
	}

	@Override 
	public void initAllCards() {
		Log.d("SET", "initAllCards start!");
		for (int i = 0; i < mCardViews.size(); ++i) {
			int pos = mCardPositions.get(i);
			SetCardData card = mSetDeckData.getCard(i);
			mCardViews.get(pos).initCard(card.getColor(), card.getShape(), card.getFilter());
			Log.d("SET", "pos=" + pos + " / color=" + card.getColor() + " / shape=" + card.getShape() + " / filter=" + card.getFilter());
			mCardViews.get(pos).updateCardBG(false);
		}
		mCandidates.clear();
		Log.d("SET", "initAllCards end!");
	}

	@Override
	public void pushSetCard(int slotNum) {
		for (int i = 0; i < mCardPositions.size(); ++i) {
			if (mCardPositions.get(i) == slotNum) {
				boolean isSecondSelect = false;
				for (int j = 0; j < mCandidates.size(); ++j) {
					if (mCandidates.get(j) == mSetDeckData.getCard(i)) {
						isSecondSelect = true;
						mCandidates.remove(j);
						mCardViews.get(slotNum).updateCardBG(false);
						break;
					}
				}
				if (!isSecondSelect) {
					mCandidates.add(mSetDeckData.getCard(i));
					mCardViews.get(slotNum).updateCardBG(true);
				}
			}
			
			//if candidate over 3
			if (mCandidates.size() >= 3) {
				boolean isRight = false;
				if (mSetEngine.isValidSet(mCandidates)) {
					isRight = true;
					mSetPannel.setNotiImage(true);
					mSetPannel.setAddedScore(mSetDeckData.getScore());
					//it will make large array.
					mSetGameData.addDeck(mSetDeckData);
					mSetPannel.setSumScore(mSetGameData.getScoreSum());
				} else {
					mSetPannel.setNotiImage(false);
				}
				lockAllCards();
				if (isRight) {
					//1 cycle is terminated. remake deck
					makeDeck(mCardViews.size());
				}
				initAllCards();
				releaseAllCards();
				break;
			}
		}
	}
	
	@Override
	public void pushHint() {
		initAllCards();
		int nowDeckScore = mSetDeckData.getScore();
		mSetDeckData.setScore(1);
		for (int i = 0; i <= mHintCount; ++i) {
			pushSetCard(mCardPositions.get(i));
		}
		mHintCount++;
		if (mHintCount >= 2) {
			mSetDeckData.setScore(0);
			mSetPannel.setEnableHint(false);
		}
	}
}
