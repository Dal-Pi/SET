package com.kania.set.view;

import com.kania.set.R;
import com.kania.set.presenter.Mediator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SetCardView extends ImageView implements View.OnClickListener, ISetCardAction {
	
	public static final boolean SELECTED = true;
	public static final boolean NOT_SELECTED = false;
	
	public final float smallFilterRatio = 0.9f;
	
	private Context mContext;
	
	private int mCardSlotNum;
	private boolean mSelected;
	private int mShape;
	private int mColor;
	private int mFilterType;
	
	private Mediator mediator;

	public SetCardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		mShape = 0;
		mColor = 0;
		mFilterType = 0;
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "onClick() on SetCardView" + mCardSlotNum, Toast.LENGTH_SHORT).show();
		mediator.pushSetCard(mCardSlotNum);
		
	}
	
	@Override
	public void initCard(int color, int shape, int filterType) {
		// TODO Auto-generated method stub
		setColor(color);
		setShape(shape);
		setFilterType(filterType);
		this.invalidate();
	}

	@Override
	public void updateCard() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, "updateCard() on SetCardView + " + mCardSlotNum, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		Resources res = mContext.getResources();
		Paint paint = new Paint();
		Matrix matrix = new Matrix();
		
		canvas.drawColor(res.getColor(R.color.base_white));
		
		if (mColor != 0 && mShape != 0) {
			canvas.drawColor(res.getColor(mColor));
			
			Bitmap bitmapShape = BitmapFactory.decodeResource(mContext.getResources(), mShape);
			PorterDuffColorFilter filter = new PorterDuffColorFilter(res.getColor(mColor), Mode.MULTIPLY);
			matrix.setScale(((float) getWidth()) / bitmapShape.getWidth(), ((float) getHeight()) / bitmapShape.getHeight());
			canvas.drawBitmap(bitmapShape, matrix, paint);
			
			if (mFilterType == 1) { 
				
				Drawable drawable = res.getDrawable(R.drawable.base_filter);
				drawable.mutate();
				paint.setColorFilter(filter);
				canvas.drawBitmap(((BitmapDrawable) drawable).getBitmap(), matrix, paint);
			} else if (mFilterType == 2) {
				matrix.postScale(smallFilterRatio, smallFilterRatio);
				float delta = ((1.0f - smallFilterRatio) / 2.0f);
				matrix.postTranslate(getWidth() * delta, getHeight() * delta);
				
				Drawable d = new BitmapDrawable(bitmapShape);
				d.mutate();
				paint.setColorFilter(filter);
				canvas.drawBitmap(((BitmapDrawable) d).getBitmap(), matrix, paint);
			}
		}
		super.onDraw(canvas);
	}
	
	public void setMediator(Mediator mediator) {
		this.mediator = mediator;
	}
	
	public void setCardSlotNum(int num) {
		mCardSlotNum = num;
	}
	
	public void setSelectCard(boolean select) {
		mSelected = select;
	}
	
	public void setShape(int shape) {
		mShape = shape;
	}
	
	public void setColor(int color) {
		mColor = color;
	}
	
	public void setFilterType(int filterType) {
		mFilterType = filterType;
	}

	


}
