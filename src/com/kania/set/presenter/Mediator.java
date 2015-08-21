package com.kania.set.presenter;

import java.util.ArrayList;

public abstract class Mediator {
	public abstract void startGame();
	
	public abstract void makeDeck(int amount);
	
	public abstract void lockAllCards();
	public abstract void releaseAllCards();
	
	public abstract void initAllCards();
	
	public abstract void pushSetCard(int slotNum);

}
