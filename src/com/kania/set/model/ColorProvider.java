package com.kania.set.model;

import java.util.ArrayList;

import com.kania.set.R;

public class ColorProvider {
	public static int[] mColors = {R.color.pastel_blue, R.color.pastel_pink, R.color.pastel_green,
		R.color.pastel_yellow, R.color.pastel_purple, R.color.pastel_orange};
	
	public static ArrayList<Integer> mColorPos = RandomNumberProvider.getRandomNumber(mColors.length);
	
	public static ArrayList<Integer> getColors(int amount) {
		ArrayList<Integer> retColors = new ArrayList<Integer>();
		//TODO change to random
		for(int i = 0; i < amount; ++i) {
			retColors.add(mColors[mColorPos.get(i) % mColors.length]);
		}
		return retColors;
	}
	
	public static void refreshColors() {
		mColorPos = RandomNumberProvider.getRandomNumber(mColors.length);
	}

}
