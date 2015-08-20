package com.kania.set.model;

import java.util.ArrayList;

//Deck should not know the solution of game

public class SetDeckData {
	private ArrayList<SetCardData> mCards;
	private int mScore;
	
	public SetDeckData(ArrayList<SetCardData> cards, int score) {
		mCards = cards;
		mScore = score;
	}
	
	public ArrayList<SetCardData> getCards() {
		return mCards;
	}
	public SetCardData getCard(int index) {
		return mCards.get(index);
	}
	public void setScore(int score) {
		mScore = score;
	}
	public int getScore() {
		return mScore;
	}
}
