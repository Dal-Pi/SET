package com.kania.set.view;

public interface ISetCardAction {
	
	public abstract void initCard(int color, int shape, int filterType);
	
	public abstract void setCard(int color, int shape, int filterType);
	
	public abstract void lockCard();
	public abstract void releaseCard();
	
	public abstract void updateCard(boolean enable); //needs card data as augments

}
