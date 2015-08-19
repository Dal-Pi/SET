package com.kania.set.model;

import java.util.ArrayList;

import com.kania.set.R;

public class ColorProvider {
	public static int[] mColors = {R.color.pastel_blue, R.color.pastel_pink, R.color.pastel_green};
	
	public static ArrayList<Integer> getColors(int amount) {
		ArrayList<Integer> retColors = new ArrayList<Integer>();
		//TODO change to random
		for(int i = 0; i < amount; ++i) {
			retColors.add(mColors[i % amount]);
		}
		return retColors;
	}

}
