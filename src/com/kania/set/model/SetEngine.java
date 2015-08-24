package com.kania.set.model;

import java.util.ArrayList;

import android.util.Log;

public class SetEngine {
	public final int CASE_OF_COLOR = 3;
	public final int CASE_OF_SHAPE = 3;
	public final int CASE_OF_FILTER = 3;
	public final int DECK_SCORE_BASE = 3;
	public final int GAME_STYLE_TIMEATTACK = 9;
	
	ArrayList<SetCardData> mHintCards;
	public SetEngine() {
		mHintCards = new ArrayList<SetCardData>();
	}
	
	public SetDeckData getNewDeck(int amount) {
		//debug stub
		ArrayList<SetCardData> cardList = getAllCards();
		ArrayList<SetCardData> randomCardList = new ArrayList<SetCardData>();
		ArrayList<SetCardData> newCards = new ArrayList<SetCardData>();
		ArrayList<Integer> sequence = RandomNumberProvider.getRandomNumber(CASE_OF_COLOR * CASE_OF_SHAPE * CASE_OF_FILTER);
		for (int i = 0; i < cardList.size(); ++i) {
			randomCardList.add(cardList.get(sequence.get(i)));
		}
		
		//init hint
		mHintCards.clear();
		//select two cards
		SetCardData firstCard = randomCardList.get(sequence.get(0));
		SetCardData secondCard = randomCardList.get(sequence.get(1));
		Log.d("SET", "Find first card! :" + firstCard.getColor() + firstCard.getShape() + firstCard.getFilter());
		newCards.add(firstCard);
		Log.d("SET", "Find second card! :" + secondCard.getColor() + secondCard.getShape() + secondCard.getFilter());
		newCards.add(secondCard);
		//add hint as first and second cards
		mHintCards.add(firstCard);
		mHintCards.add(secondCard);
		//remove card for avoid duplication
		randomCardList.remove(firstCard);
		randomCardList.remove(secondCard);
		
		
		//calculate SET from two cards
		int incompleteColor = (newCards.get(0).getColor() + newCards.get(1).getColor()) % CASE_OF_COLOR;
		int incompleteShape = (newCards.get(0).getShape() + newCards.get(1).getShape()) % CASE_OF_SHAPE;
		int incompleteFilter = (newCards.get(0).getFilter() + newCards.get(1).getFilter()) % CASE_OF_FILTER;
		//it is the third card
		SetCardData thirdCard = null;
		int thirdCardColor = (CASE_OF_COLOR - incompleteColor) % CASE_OF_COLOR;
		int thirdCardShape = (CASE_OF_SHAPE - incompleteShape) % CASE_OF_SHAPE;
		int thirdCardFilter = (CASE_OF_FILTER - incompleteFilter) % CASE_OF_FILTER;
		//find third card from remained card list
		for (int i = 0; i < randomCardList.size(); ++i) {
			if (randomCardList.get(i).getColor() == thirdCardColor && randomCardList.get(i).getShape() == thirdCardShape && randomCardList.get(i).getFilter() == thirdCardFilter) {
				thirdCard = randomCardList.get(i);
				break;
			}
		}
		Log.d("SET", "Find third card! :" + thirdCard.getColor() + thirdCard.getShape() + thirdCard.getFilter());
		newCards.add(thirdCard);
		
		ArrayList<SetCardData> forCheck = new ArrayList<SetCardData>();
		for (int i = 0; i < randomCardList.size(); ++i) {
			boolean isItMakeSet = false;
			for (int j = 0; j < newCards.size() - 1; ++j) {
				for (int k = j + 1; k < newCards.size(); ++k) {
					//add twice card
					forCheck.clear();
					forCheck.add(newCards.get(j));
					forCheck.add(newCards.get(k));
					//add card to check
					Log.d("SET", "checked card! :" + randomCardList.get(i).getColor() + randomCardList.get(i).getShape() + randomCardList.get(i).getFilter());
					forCheck.add(randomCardList.get(i));
					
					if (isValidSet(forCheck)) { //if is set, it will van
						isItMakeSet = true;
						break;
					}
					Log.d("SET", "result! :" + isValidSet(forCheck));
				}
				if (isItMakeSet)
					break;
			}
			if (!isItMakeSet) {
				newCards.add(randomCardList.get(i));
				if (newCards.size() == amount)
					break;
			}
		}
		
		return new SetDeckData(newCards, DECK_SCORE_BASE);
	}
	
	public ArrayList<SetCardData> getAllCards() {
		ArrayList<SetCardData> alCards = new ArrayList<SetCardData>();

		for(int i = 0; i < CASE_OF_COLOR; ++i)
			for(int j = 0; j < CASE_OF_SHAPE; ++j)
				for(int k = 0; k < CASE_OF_FILTER; ++k)
					alCards.add(new SetCardData(i, j, k));
		
		return alCards;
	}
	
	public boolean isValidSet(ArrayList<SetCardData> candidates) {
		int colorSum = 0;
		int shapeSum = 0;
		int filterSum = 0;
		
		for (int i = 0; i < 3; ++i) {
			SetCardData scd = candidates.get(i);
			colorSum += scd.getColor();
			shapeSum += scd.getShape();
			filterSum += scd.getFilter();
		}
		Log.d("SET", "colorSum/shapeSum/filterSum = " + colorSum + "/" + shapeSum + "/" + filterSum);
		
		if ((colorSum % 3 == 0) && (shapeSum % 3 == 0) && (filterSum % 3 == 0))
			return true;
		else 
			return false;
	}
	
	public SetCardData getHint(int times) {
		if (times == 1)
			return mHintCards.get(0);
		else if (times == 2)
			return mHintCards.get(1);
		else
			return null;
	}
}
