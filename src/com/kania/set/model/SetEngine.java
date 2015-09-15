package com.kania.set.model;

import java.util.ArrayList;

import com.kania.set.RandomNumberProvider;
import com.kania.set.SetGameInfo;

import android.util.Log;

public class SetEngine {
	public final int CASE_OF_COLOR = 3;
	public final int CASE_OF_SHAPE = 3;
	public final int CASE_OF_FILTER = 3;
	public final int DECK_SCORE_BASE = 3;
	public final int GAME_STYLE_TIMEATTACK = 9;
	
	public final int MAX_HINT_COUNT = 2;
	
	public SetEngine() {
	}
	
	public SetDeckData getNewDeck(int amount, int difficulty) {
		ArrayList<SetCardData> cardList = getAllCards();
		ArrayList<SetCardData> randomCardList = new ArrayList<SetCardData>();
		ArrayList<SetCardData> newCards = new ArrayList<SetCardData>();
		ArrayList<Integer> sequence = RandomNumberProvider.getRandomNumber(CASE_OF_COLOR * CASE_OF_SHAPE * CASE_OF_FILTER);
		for (int i = 0; i < cardList.size(); ++i) {
			randomCardList.add(cardList.get(sequence.get(i)));
		}
		
		//first card is member of solution absolutely
		SetCardData firstCard = randomCardList.get(sequence.get(0));
		Log.d("SET", "[All] Find first card! :" + firstCard.getColor() + firstCard.getShape() + firstCard.getFilter());
		newCards.add(firstCard);
		SetCardData secondCard = null;
		SetCardData thirdCard = null;
		
		if (difficulty == SetGameInfo.DIFFICULTY_HARD) {
			//select second card
			secondCard = randomCardList.get(sequence.get(1));
			Log.d("SET", "[Hard] Find second card! :" + secondCard.getColor() + secondCard.getShape() + secondCard.getFilter());
			newCards.add(secondCard);
			//remove card for avoid duplication
			randomCardList.remove(firstCard);
			randomCardList.remove(secondCard);
		} else if (difficulty == SetGameInfo.DIFFICULTY_EASY) {
			randomCardList.remove(firstCard);
			for (SetCardData scd : randomCardList) {
				int matchCount = 0;
				//count matched attribute
				if (firstCard.getColor() == scd.getColor()) matchCount++;
				if (firstCard.getShape() == scd.getShape()) matchCount++;
				if (firstCard.getFilter() == scd.getFilter()) matchCount++;
				
				if (matchCount == 2) {
					secondCard = scd;
					Log.d("SET", "[Easy] Find second card! :" + secondCard.getColor() + secondCard.getShape() + secondCard.getFilter());
					newCards.add(secondCard);
					break;
				}
			}
			randomCardList.remove(secondCard);
		}
		
		//calculate SET from two cards
		int incompleteColor = (firstCard.getColor() + secondCard.getColor()) % CASE_OF_COLOR;
		int incompleteShape = (firstCard.getShape() + secondCard.getShape()) % CASE_OF_SHAPE;
		int incompleteFilter = (firstCard.getFilter() + secondCard.getFilter()) % CASE_OF_FILTER;
		//it is the third card
		
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
		Log.d("SET", "[All] Find third card! :" + thirdCard.getColor() + thirdCard.getShape() + thirdCard.getFilter());
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
	
//	public SetDeckData getNewDeck(int amount) {
//		//debug stub
//		ArrayList<SetCardData> newCards = new ArrayList<SetCardData>();
//		
//		
//		newCards.add(new SetCardData(0, 0, 0));
//		newCards.add(new SetCardData(0, 0, 0));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		newCards.add(new SetCardData(2, 2, 2));
//		
//		
//		return new SetDeckData(newCards, DECK_SCORE_BASE);
//	}
	
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
}
