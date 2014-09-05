/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @welcomeAnimEntity_1.java  2014年6月12日 上午11:36:37 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.welcome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.givon.anhao.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

public class WelcomeView_1 implements Animator.AnimatorListener{
	boolean isFirst=true;
	private View view;
	private Context mContext;
	private AnimatorSet mAnimatorSet;
	private AnimatorSet mAnimatorReSet;
  	
	public WelcomeView_1(Context context) {
		super();
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		view = (View) LayoutInflater.from(context).inflate(R.layout.layout_welcome_anhao_1, null);
		mAnimatorSet = new AnimatorSet();
		mAnimatorReSet = new AnimatorSet();
		
	}
	
	public void startAnim(){
		createAnimation();
		if(null!=mAnimatorSet&&isFirst){
			if(mAnimatorReSet.isRunning()){
				mAnimatorReSet.cancel();
			}
			mAnimatorSet.start();
			isFirst= false;
		}else {
			
		}
	}
	private void createAnimation() {
    }
	
	
	
	private int getHeight() {
		return mContext.getResources().getDisplayMetrics().heightPixels;
	}
	private int getWidth() {
		return mContext.getResources().getDisplayMetrics().widthPixels;
	}

	public void seek(long seekTime) {
        createAnimation();
    }

	@Override
	public void onAnimationStart(Animator animation) {
		
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	

}
