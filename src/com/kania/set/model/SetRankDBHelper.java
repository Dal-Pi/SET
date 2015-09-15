package com.kania.set.model;

import com.kania.set.model.SetRankContract.SetRankEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

	
public class SetRankDBHelper extends SQLiteOpenHelper {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "SetRank.db";
	
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGET_TPYE = " INTEGER";
	private static final String COMMA_SEP = ",";
	
	private static final String SQL_CREATE_ENTRIES =
			"CREATE TABLE " + SetRankEntry.TABLE_NAME + " (" +
			SetRankEntry._ID + " INTEGER PRIMARY KEY," +
			/*SetRankEntry.COLUMN_NAME_RANK + INTEGET_TPYE + COMMA_SEP +*/
			SetRankEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
			SetRankEntry.COLUMN_NAME_SCORE + INTEGET_TPYE + COMMA_SEP +
			SetRankEntry.COLUMN_NAME_DIFFICULTY + INTEGET_TPYE +
			" )";
	private static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + SetRankEntry.TABLE_NAME;
	public static final String SQL_SELECT_ENTRIES_PRE = 
			"SELECT * FROM " + SetRankEntry.TABLE_NAME + 
			" WHERE " + SetRankEntry.COLUMN_NAME_DIFFICULTY + " = ";
	public static final String SQL_SELECT_ENTRIES_POST = 
			" ORDER BY " + SetRankEntry.COLUMN_NAME_SCORE + " DESC";
	
	public SetRankDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
		
//		//debug
//		for(int i = 0; i < 98; ++i) {
//			ContentValues values = new ContentValues();
////			values.put(SetRankEntry.COLUMN_NAME_RANK, i+1);
//			values.put(SetRankEntry.COLUMN_NAME_NAME, "test");
//			values.put(SetRankEntry.COLUMN_NAME_SCORE, i*10);
//			
//			long newRowId = db.insert(SetRankEntry.TABLE_NAME, null, values);
//		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	public String getSelectQueryString(int difficulty) {
		return SQL_SELECT_ENTRIES_PRE + difficulty + SQL_SELECT_ENTRIES_POST;
	}
}


