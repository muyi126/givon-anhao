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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.givon.anhao.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;

public class WelcomeView_3 implements Animator.AnimatorListener{
	boolean isFirst=true;
	private View view;
	private Context mContext;
	private AnimatorSet mAnimatorSet;
	private AnimatorSet mAnimatorReSet;
	private Button weixinStart;
	private UMSocialService mController;
  	
	public WelcomeView_3(Context context) {
		super();
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		view = (View) LayoutInflater.from(context).inflate(R.layout.layout_welcome_anhao_3, null);
		mAnimatorSet = new AnimatorSet();
		mAnimatorReSet = new AnimatorSet();
		mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		weixinStart = (Button) view.findViewById(R.id.weixinStart);
		weixinStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lodingWX(mContext);
			}
		});
		
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
	
	/**
	 * 微信
	 */
	private void lodingWX(final Context context) {
		
		mController.doOauthVerify(context, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
					Toast.makeText(context, "授权成功.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}

			@Override
			public void onStart(SHARE_MEDIA platform) {
			}
		});
	}
	

}
