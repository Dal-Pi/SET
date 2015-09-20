package com.kania.set.presenter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kania.set.ColorProvider;
import com.kania.set.RandomNumberProvider;
import com.kania.set.SetGameInfo;
import com.kania.set.ShapeProvider;
import com.kania.set.model.SetCardData;
import com.kania.set.model.SetDeckData;
import com.kania.set.model.SetEngine;
import com.kania.set.model.SetGameData;
import com.kania.set.model.SetRankContract.SetRankEntry;
import com.kania.set.model.SetRankDBHelper;
import com.kania.set.view.ISetCardAction;
import com.kania.set.view.ISetPannelAction;
import com.kania.set.view.SetRankActivity;

public class SetPresenter extends Mediator {

	//	private final int MAX_CARD_AMOUNT = 9;
	private final String KEY_EVENT_TYPE = "eventtype";
	private final int TIME_EVENT = 1;
	private final int TIME_DELAY_MILLIS = 1000;
	
	private Context mContext;
	
	private SetEngine mSetEngine;
	
	private ArrayList<ISetCardAction> mCardViews;
	private ISetPannelAction mSetPannel;
	private SetGameData mSetGameData;
	private SetDeckData mSetDeckData;
	private ArrayList<Integer> mCardPositions;
	private ArrayList<SetCardData> mCandidates;
	
	private int mDifficulty;
	
	private int mHintCount;
	private int mRemainTime = 0;;
	private int mBackButtonSecondGap = 3; //for escape game view
	
	private SetRankDBHelper mDbHelper;
	
	public Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			switch (data.getInt(KEY_EVENT_TYPE, 0)) {
			case TIME_EVENT:
				mRemainTime--;
				
				if (mRemainTime <= 0) {
					lockAllCards();
					mHandler.removeMessages(TIME_EVENT);
					mSetPannel.setRemainTime("End!");
					mSetPannel.setEnableHint(false);
					mSetPannel.setInputNameEnable(true);
					//TODO go to rank activity
				} else {
					mSetPannel.setRemainTime("" + mRemainTime);
					makeAndSendTimeEvent();
				}
				break;
			}
			
		};
	};

	public SetPresenter(Context context) {
		mContext = context;
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
	
	public void setDifficulty(int difficulty) {
		mDifficulty = difficulty;
	}

	@Override
	public void startGame() {
		//init game
		mSetPannel.setInputNameEnable(false);
		
		//mutex lock
		lockAllCards();
		
		mRemainTime = SetGameInfo.TIME_LIMIT;
		
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
		ShapeProvider.refreshShapes();
		//make deck
		mCardPositions.clear();
		mCardPositions = RandomNumberProvider.getRandomNumber(mCardViews.size());
		mSetDeckData = mSetEngine.getNewDeck(mCardViews.size(), mDifficulty);
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

	@Override
	public void inputUserName(String name) {
		Intent intent = new Intent();
		intent.setClass(mContext, SetRankActivity.class);
		intent.putExtra(SetGameInfo.RESULT_USER_NAME, name);
		intent.putExtra(SetGameInfo.DIFFICULTY_NAME, mDifficulty);
		mContext.startActivity(intent);
		mSetPannel.finishGame();
		
		//TODO need to improve
		mDbHelper = new SetRankDBHelper(mContext);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(SetRankEntry.COLUMN_NAME_NAME, name);
		values.put(SetRankEntry.COLUMN_NAME_SCORE, mSetGameData.getScoreSum());
		values.put(SetRankEntry.COLUMN_NAME_DIFFICULTY, mDifficulty);
		db.insert(SetRankEntry.TABLE_NAME, null, values);
		db.close();
	}
}
