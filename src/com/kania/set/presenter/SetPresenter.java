package com.kania.set.presenter;

import java.util.ArrayList;

import android.util.Log;

import com.kania.set.model.RandomNumberProvider;
import com.kania.set.model.SetCardData;
import com.kania.set.model.SetDeckData;
import com.kania.set.model.SetEngine;
import com.kania.set.model.SetGameData;
import com.kania.set.view.ISetCardAction;
import com.kania.set.view.ISetPannelAction;

public class SetPresenter extends Mediator {

	//	private final int MAX_CARD_AMOUNT = 9;

	private SetEngine mSetEngine;
	
	private ArrayList<ISetCardAction> mCardViews;
	private ISetPannelAction mSetPannel;
	private SetGameData mSetGameData;
	private SetDeckData mSetDeckData;
	private RandomNumberProvider mRandomNumberProvider;
	private ArrayList<Integer> mCardPositions;
	private ArrayList<SetCardData> mCandidates;

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
		
		mSetGameData = new SetGameData();
		mRandomNumberProvider = new RandomNumberProvider();
		mCandidates = new ArrayList<SetCardData>();
		mCardPositions = mRandomNumberProvider.getRandomNumber(mCardViews.size());

		makeDeck(mCardViews.size());
		initAllCards();
		
		//mutex release
		releaseAllCards();
	}

	@Override
	public void makeDeck(int amount) {
		//make deck
		mCardPositions.clear();
		mCardPositions = mRandomNumberProvider.getRandomNumber(mCardViews.size());
		mSetDeckData = mSetEngine.getNewDeck(mCardViews.size());
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
}
