package com.kania.set.view;

public interface ISetCardAction {
	
	public abstract void initCard(int color, int shape, int filterType);
	
	public abstract void updateCard(); //needs card data as augments

}
