package com.kania.set.model;

import java.util.ArrayList;

import com.kania.set.R;

public class ShapeProvider {
	public static int[] mShapes = {R.drawable.basic_circle, R.drawable.basic_square, R.drawable.basic_triangle};
	
	public static ArrayList<Integer> getShapes(int amount) {
		ArrayList<Integer> retShapes = new ArrayList<Integer>();
		//TODO change to random
		for(int i = 0; i < amount; ++i) {
			retShapes.add(mShapes[i % amount]);
		}
		return retShapes;
	}

}
