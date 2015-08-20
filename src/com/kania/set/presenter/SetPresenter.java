package com.kania.set.presenter;

import java.util.ArrayList;

import android.util.Log;

import com.kania.set.model.ColorProvider;
import com.kania.set.model.RandomNumberProvider;
import com.kania.set.model.SetCardData;
import com.kania.set.model.SetDeckData;
import com.kania.set.model.SetEngine;
import com.kania.set.model.SetGameData;
import com.kania.set.model.ShapeProvider;
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
		mSetGameData = new SetGameData();
		mRandomNumberProvider = new RandomNumberProvider();
		mCandidates = new ArrayList<SetCardData>();
		mCardPositions = mRandomNumberProvider.getRandomNumber(mCardViews.size());

		makeDeckAndSetToView(mCardViews.size());
	}

	@Override
	public void makeDeckAndSetToView(int amount) {
		//mutex lock
		lockAllCards();
		//make deck
		mCardPositions.clear();
		mCardPositions = mRandomNumberProvider.getRandomNumber(mCardViews.size());
		mSetDeckData = mSetEngine.getNewDeck(mCardViews.size());
		//init using made deck
		initAllCards();
		//mutex release
		releaseAllCards();
	}

	@Override
	public void lockAllCards() {
		for (int i = 0; i < mCardViews.size(); ++i) 
			mCardViews.get(i).lockCard();
	}
	@Override
	public void releaseAllCards() {
		for (int i = 0; i < mCardViews.size(); ++i) 
			mCardViews.get(i).releaseCard();
	}

	@Override 
	public void initAllCards() {
		for (int i = 0; i < mCardViews.size(); ++i) {
			int pos = mCardPositions.get(i);
			SetCardData card = mSetDeckData.getCard(i);
			mCardViews.get(pos).initCard(card.getColor(), card.getShape(), card.getFilter());
			Log.d("SET", "pos=" + pos + " / color=" + card.getColor() + " / shape=" + card.getShape() + " / filter=" + card.getFilter());
		}
		mCandidates.clear();
	}

	@Override
	public void pushSetCard(int slotNum) {
		lockAllCards();
		
		for (int i = 0; i < mCardPositions.size(); ++i) {
			if (mCardPositions.get(i) == slotNum) {
				boolean isSecondSelect = false;
				for (int j = 0; j < mCandidates.size(); ++j) {
					if (mCandidates.get(j) == mSetDeckData.getCard(i)) {
						isSecondSelect = true;
						mCandidates.remove(j);
						mCardViews.get(slotNum).updateCard(false);
						break;
					}
				}
				if (!isSecondSelect) {
					mCandidates.add(mSetDeckData.getCard(i));
					mCardViews.get(slotNum).updateCard(true);
				}
			}
			
			//if candidate over 3
			if (mCandidates.size() >= 3) {
				boolean isRight = false;
				if (mSetEngine.isValidSet(mCandidates)) {
					isRight = true;
					mSetPannel.setNotiImage(true);
					mSetGameData.addDeck(mSetDeckData);
					mSetPannel.setSumScore(mSetGameData.getScoreSum());
				} else {
					mSetPannel.setNotiImage(false);
				}
				initAllCards();
				if (isRight) {
					//1 cycle is terminated. remake deck
					makeDeckAndSetToView(mCardViews.size());
				}
			}
		}
		releaseAllCards();
	}
}
