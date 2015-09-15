package com.kania.set.model;

import android.provider.BaseColumns;

public final class SetRankContract {

	public SetRankContract() {}
	
	public static abstract class SetRankEntry implements BaseColumns {
		public static final String TABLE_NAME = "setrank";
//        public static final String COLUMN_NAME_RANK = "rank";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String COLUMN_NAME_DIFFICULTY = "difficulty";
	}
}
