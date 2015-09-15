package com.kania.set;

import java.util.ArrayList;
import java.util.Random;

import android.util.Log;

public class RandomNumberProvider {
	private static Random random = new Random();
	
	public RandomNumberProvider() {
		random = new Random(); //TODO ***** need seed?
	}
	
	//TODO need verification
	public static ArrayList<Integer> getRandomNumber(int amount) {
		ArrayList<Integer> ret = new ArrayList<Integer>();
		int[] retTemp = new int[amount];
		
		//init
		for (int i = 0; i < amount; ++i) {
			retTemp[i] = -1;
		}
		
		for (int i = amount; i > 0; --i) {
			Log.d("SET", "i = " + i);
			int num = random.nextInt(i);
			Log.d("SET", "num = " + num);
			int target = 0;
			for (int j = 0; j < amount; ++j) {
				if (retTemp[j] == -1) {
					if (target == num) {
						retTemp[j] = i-1;
						break;
					} else
						target++;
				}
			}
		}
		
		for (int i = 0; i < amount; ++i) {
			ret.add(retTemp[i]);
		}
		
		return ret;
	}

}
