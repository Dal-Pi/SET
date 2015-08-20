package com.kania.set.model;

import java.util.ArrayList;

import android.util.Log;

public class SetEngine {
	public final int CASE_OF_COLOR = 3;
	public final int CASE_OF_SHAPE = 3;
	public final int CASE_OF_FILTER = 3;
	public final int DECK_SCORE_BASE = 3;
	
	public SetEngine() {
		// TODO Auto-generated constructor stub
	}
	
	public SetDeckData getNewDeck(int amount) {
		//debug stub
		ArrayList<SetCardData> newCards = new ArrayList<SetCardData>();
		
		
		//needs to be changed
		newCards.add(new SetCardData(0, 0, 0));
		newCards.add(new SetCardData(0, 1, 0));
		newCards.add(new SetCardData(0, 2, 0));
		newCards.add(new SetCardData(1, 0, 1));
		newCards.add(new SetCardData(1, 1, 1));
		newCards.add(new SetCardData(1, 2, 1));
		newCards.add(new SetCardData(2, 0, 2));
		newCards.add(new SetCardData(2, 1, 2));
		newCards.add(new SetCardData(2, 2, 2));
		
		return new SetDeckData(newCards, DECK_SCORE_BASE);
	}
	
	public boolean isValidSet(ArrayList<SetCardData> candidates) {
		int colorSum = 0;
		int shapeSum = 0;
		int filterSum = 0;
		
		for (int i = 0; i < 3; ++i) {
			SetCardData scd = candidates.get(i);
			colorSum += scd.getColor() + 1;
			shapeSum += scd.getShape() + 1;
			filterSum += scd.getFilter() + 1;
		}
		Log.d("SET", "colorSum/shapeSum/filterSum = " + colorSum + "/" + shapeSum + "/" + filterSum);
		
		if ((colorSum % 3 == 0) && (shapeSum % 3 == 0) && (filterSum % 3 == 0))
			return true;
		else 
			return false;
	}
}
