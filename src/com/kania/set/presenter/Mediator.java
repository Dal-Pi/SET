package com.kania.set.presenter;


public abstract class Mediator {
	public abstract void startGame();
	
	public abstract void makeDeck(int amount);
	
	public abstract void lockAllCards();
	public abstract void releaseAllCards();
	
	public abstract void initAllCards();
	
	public abstract void pushSetCard(int slotNum);
	
	public abstract void pushHint();
	
	public abstract void inputUserName(String name);

}
