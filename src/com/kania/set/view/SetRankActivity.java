package com.kania.set.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kania.set.R;
import com.kania.set.SetGameInfo;
import com.kania.set.model.SetRankContract.SetRankEntry;
import com.kania.set.model.SetRankDBHelper;

public class SetRankActivity extends Activity implements View.OnClickListener {
	private final String RANKING_PREFIX = "Ranking";
	private TextView mTextRank;	
	private ListView mRankList;
	private Button mBtnExit;
	
	private SimpleCursorAdapter mAdapter;
	
	private SetRankDBHelper mDBHelper;
	private Cursor mCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_rank);
		
		Intent intent = getIntent();
		int difficulty = intent.getIntExtra(SetGameInfo.DIFFICULTY_NAME, 0);
		
		mTextRank = (TextView) findViewById(R.id.textRankText);
		mRankList = (ListView) findViewById(R.id.listRank);
		mBtnExit = (Button) findViewById(R.id.btnRankExit);
		mBtnExit.setOnClickListener(this);
		
		mDBHelper = new SetRankDBHelper(this);
		SQLiteDatabase db = mDBHelper.getReadableDatabase();
		mCursor = db.rawQuery(mDBHelper.getSelectQueryString(difficulty), null);
		mCursor.moveToFirst();
		String[] from = new String[] {
				SetRankEntry.COLUMN_NAME_NAME, 
				SetRankEntry.COLUMN_NAME_SCORE
				};
		int[] to = new int[] {R.id.textItemName, R.id.textItemScore};
		mAdapter = new SimpleCursorAdapter(this, R.layout.item_rank, mCursor, from, to, 0);
		mRankList.setAdapter(mAdapter);
		db.close();
		
		String titleName = RANKING_PREFIX;
		if (difficulty == SetGameInfo.DIFFICULTY_EASY) {
			titleName += " (EASY)";
		} else if (difficulty == SetGameInfo.DIFFICULTY_HARD) {
			titleName += " (HARD)";
		}
		mTextRank.setText(titleName);
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
