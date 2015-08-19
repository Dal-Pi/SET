package com.kania.set.presenter;

import java.util.ArrayList;

import com.kania.set.model.ColorProvider;
import com.kania.set.model.ShapeProvider;
import com.kania.set.view.ISetCardAction;

public class SetPresenter extends Mediator {
	
	private ArrayList<ISetCardAction> mCards;
	
	public SetPresenter() {
		mCards = new ArrayList<ISetCardAction>();
	}
	
	public void addSetCardView(ISetCardAction cardView) {
		mCards.add(cardView);
	}
	
	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		initAllCards();
		
		
	}
	
	public void initAllCards() {
		//debug
		ArrayList<Integer> colors = ColorProvider.getColors(3);
		ArrayList<Integer> shapes = ShapeProvider.getColors(3);
		mCards.get(0).initCard(colors.get(0), shapes.get(0), 0);
		mCards.get(1).initCard(colors.get(0), shapes.get(1), 0);
		mCards.get(2).initCard(colors.get(0), shapes.get(2), 0);
		mCards.get(3).initCard(colors.get(1), shapes.get(0), 1);
		mCards.get(4).initCard(colors.get(1), shapes.get(1), 1);
		mCards.get(5).initCard(colors.get(1), shapes.get(2), 1);
		mCards.get(6).initCard(colors.get(2), shapes.get(0), 2);
		mCards.get(7).initCard(colors.get(2), shapes.get(1), 2);
		mCards.get(8).initCard(colors.get(2), shapes.get(0), 2);
	}
	
	@Override
	public void pushSetCard(int slotNum) {
		//process using model. will be created as "SetModel"
		
		//debug
		for (ISetCardAction card : mCards) {
			card.updateCard();
		}
	}

}
