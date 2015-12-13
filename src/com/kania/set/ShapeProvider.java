package com.kania.set;

import java.util.ArrayList;

public class ShapeProvider {
	public static int[] mShapes = {R.drawable.basic_circle, R.drawable.basic_square, R.drawable.basic_triangle/*, 
		R.drawable.basic_diamond /*, R.drawable.basic_plus, R.drawable.basic_star*/};
	
	public static ArrayList<Integer> mShapePos = RandomNumberProvider.getRandomNumber(mShapes.length);
	
	public static ArrayList<Integer> getShapes(int amount) {
		ArrayList<Integer> retShapes = new ArrayList<Integer>();
		//TODO change to random
		for(int i = 0; i < amount; ++i) {
			retShapes.add(mShapes[mShapePos.get(i) % mShapes.length]);
		}
		return retShapes;
	}
	
	public static void refreshShapes() {
		mShapePos = RandomNumberProvider.getRandomNumber(mShapes.length);
	}

}
