package com.kania.set.model;

import java.util.ArrayList;

public class SetGameData {
	private int mScoreSum;
	private ArrayList<SetDeckData> mDecks;
	
	public SetGameData() {
		mScoreSum = 0;
		mDecks = new ArrayList<SetDeckData>();
	}
	
	public void addDeck(SetDeckData deck) {
		mDecks.add(deck);
		mScoreSum += deck.getScore();
	}
	
	public int getScoreSum() {
		return mScoreSum;
	}
}
