/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @RoundImageView.java  2014年9月10日 上午11:52:40 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.givon.anhao.R;

public class RoundImageView extends ImageView {

	private int mBorderThickness = 0;

	private Context mContext;

	private int defaultColor = 0xFFFFFFFF;

	// 如果只有其中一个有值，则只画一个圆形边框

	private int mBorderOutsideColor = 0;

	private int mBorderInsideColor = 0;

	// 控件默认长、宽

	private int defaultWidth = 0;

	private int defaultHeight = 0;
	private Bitmap roundBitmap;

	public RoundImageView(Context context) {

		super(context);

		mContext = context;

	}

	public RoundImageView(Context context, AttributeSet attrs) {

		super(context, attrs);

		mContext = context;

		setCustomAttributes(attrs);

	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		mContext = context;

		setCustomAttributes(attrs);

	}

	private void setCustomAttributes(AttributeSet attrs) {

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.roundedimageview);

		mBorderThickness = a

		.getDimensionPixelSize(R.styleable.roundedimageview_border_thickness, 0);

		mBorderOutsideColor = a.getColor(R.styleable.roundedimageview_border_outside_color,

		defaultColor);

		mBorderInsideColor = a.getColor(R.styleable.roundedimageview_border_inside_color,

		defaultColor);

	}

	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();

		if (drawable == null) {

			return;

		}

		if (getWidth() == 0 || getHeight() == 0) {

			return;

		}

		this.measure(0, 0);

		if (drawable.getClass() == NinePatchDrawable.class)

			return;
		Bitmap b = null;
		if (drawable instanceof BitmapDrawable) {
			b = ((BitmapDrawable) drawable).getBitmap();
		} else {
			return;
		}

		if (defaultWidth == 0) {

			defaultWidth = getWidth();

		}

		if (defaultHeight == 0) {

			defaultHeight = getHeight();

		}

		// 保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）

		if (defaultWidth != 0 && defaultHeight != 0) {

			if (getLayoutParams() instanceof ViewGroup.LayoutParams) {

				ViewGroup.LayoutParams params = getLayoutParams();

				params.width = defaultWidth;

				params.height = defaultHeight;

				setLayoutParams(params);

			} else if (getLayoutParams() instanceof RelativeLayout.LayoutParams) {

				RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();

				params.width = defaultWidth;

				params.height = defaultHeight;

				setLayoutParams(params);

			}

		}

		int radius = 0;

		if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// 定义画两个边框，分别为外圆边框和内圆边框

			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2

			* mBorderThickness;

			// 画内圆

			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);

			// 画外圆

			drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2,

			mBorderOutsideColor);

		} else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// 定义画一个边框

			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2

			- mBorderThickness;

			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);

		} else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// 定义画一个边框

			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2

			- mBorderThickness;

			drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);

		} else {// 没有边框

			radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;

		}
		if (null == roundBitmap) {
			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
			roundBitmap = getCroppedRoundBitmap(bitmap, radius);
		} else {
		}

		canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);

	}

	/**
	 * 
	 * 获取裁剪后的圆形图片
	 * 
	 * 
	 * 
	 * @param radius
	 * 
	 *        半径
	 */

	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {

		Bitmap scaledSrcBmp;

		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片

		int bmpWidth = bmp.getWidth();

		int bmpHeight = bmp.getHeight();

		int squareWidth = 0, squareHeight = 0;

		int x = 0, y = 0;

		Bitmap squareBitmap;

		if (bmpHeight > bmpWidth) {// 高大于宽

			squareWidth = squareHeight = bmpWidth;

			x = 0;

			y = (bmpHeight - bmpWidth) / 2;

			// 截取正方形图片

			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);

		} else if (bmpHeight < bmpWidth) {// 宽大于高

			squareWidth = squareHeight = bmpHeight;

			x = (bmpWidth - bmpHeight) / 2;

			y = 0;

			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);

		} else {

			squareBitmap = bmp;

		}

		if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {

			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter, diameter, true);

		} else {

			scaledSrcBmp = squareBitmap;

		}

		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(),

		Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();

		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);

		paint.setFilterBitmap(true);

		paint.setDither(true);

		canvas.drawARGB(0, 0, 0, 0);

		canvas.drawCircle(scaledSrcBmp.getWidth() / 2, scaledSrcBmp.getHeight() / 2,

		scaledSrcBmp.getWidth() / 2, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

		// bitmap回收(recycle导致在布局文件XML看不到效果)

		// bmp.recycle();

		// squareBitmap.recycle();

		// scaledSrcBmp.recycle();

		bmp = null;

		squareBitmap = null;

		scaledSrcBmp = null;

		return output;

	}

	/**
	 * 
	 * 边缘画圆
	 */

	private void drawCircleBorder(Canvas canvas, int radius, int color) {

		Paint paint = new Paint();

		/* 去锯齿 */

		paint.setAntiAlias(true);

		paint.setFilterBitmap(true);

		paint.setDither(true);

		paint.setColor(color);

		/* 设置paint的　style　为STROKE：空心 */

		paint.setStyle(Paint.Style.STROKE);

		/* 设置paint的外框宽度 */

		paint.setStrokeWidth(mBorderThickness);

		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);

	}
	
	public void cleanbitmap() {
		roundBitmap = null;
	}

}
