package com.kania.set.view;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kania.set.R;
import com.kania.set.model.SetRankDBHelper;
import com.kania.set.model.SetRankContract.SetRankEntry;

public class SetRankActivity extends Activity implements View.OnClickListener {
	TextView mTextRank;	
	ListView mRankList;
	Button mBtnExit;
	
	SimpleCursorAdapter mAdapter;
	
	SetRankDBHelper mDBHelper;
	Cursor mCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rank);
		
		mTextRank = (TextView) findViewById(R.id.textRankText);
		mRankList = (ListView) findViewById(R.id.listRank);
		mBtnExit = (Button) findViewById(R.id.btnRankExit);
		mBtnExit.setOnClickListener(this);
		
		mDBHelper = new SetRankDBHelper(this);
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		mCursor = db.rawQuery(SetRankDBHelper.SQL_SELECT_ENTRIES, null);
		mCursor.moveToFirst();
		String[] from = new String[] {
				SetRankEntry.COLUMN_NAME_NAME, 
				SetRankEntry.COLUMN_NAME_SCORE
				};
		int[] to = new int[] {R.id.textItemName, R.id.textItemScore};
		mAdapter = new SimpleCursorAdapter(this, R.layout.item_rank, mCursor, from, to, 0);
		mRankList.setAdapter(mAdapter);
		db.close();
	}
	
	@Override
	protected void onResume() {
		// TODO if need animation.
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		mCursor.close();
		super.onStop();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnRankExit:
			finish();
			break;
		}
		
	}

}
