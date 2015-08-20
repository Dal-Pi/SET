package com.kania.set.model;

public class SetCardData {
	private int mColor;
	private int mShape;
	private int mFilter;
	
	public SetCardData(int color, int shape, int filter) {
		mColor = color;
		mShape = shape;
		mFilter = filter;
	}
	
	public int getColor() {
		return mColor;
	}
	public int getShape() {
		return mShape;
	}
	public int getFilter() {
		return mFilter;
	}
}
