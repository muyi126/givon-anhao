/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @WaitingDialog.java  2014年4月1日 下午7:45:26 - Jerry
 * @author JiangYue
 * @version 1.0
 */

package com.givon.baseproject.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.givon.anhao.R;

public class WaitingDialog extends Dialog implements android.view.View.OnClickListener{
	private TextView tvMsg;
	private ImageView ivClose;
	private ImageView ivLoading;

	public WaitingDialog(Context ctx) {
		super(ctx);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initViews(ctx);
	}
	
	public void initViews(Context context) {
		View view=LayoutInflater.from(context).inflate(R.layout.layout_waiting_dialog, null);
		tvMsg = (TextView) view.findViewById(R.id.tv_msg);
		ivClose = (ImageView) view.findViewById(R.id.iv_close);
		ivLoading = (ImageView) view.findViewById(R.id.iv_loading);
		ivClose.setOnClickListener(this);
		setContentView(view);
		Window window = getWindow();  
        window.setGravity(Gravity.CENTER);
//        //设置SelectPicPopupWindow弹出窗体动画效果  
//		window.setWindowAnimations(R.style.PopupAnimation_SetTip);
//        //设置SelectPicPopupWindow弹出窗体的背景  
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  
        
		LayoutParams lp = window.getAttributes();
        //设置SelectPicPopupWindow弹出窗体的宽  
		lp.width = LayoutParams.WRAP_CONTENT;
        //设置SelectPicPopupWindow弹出窗体的高  
		lp.height = LayoutParams.WRAP_CONTENT;
		
		AnimationDrawable ad = (AnimationDrawable) ivLoading.getDrawable();
		ad.start();
	}
	
	public void setMessage(String msg) {
		tvMsg.setText(msg);
	}

	@Override
	public void onClick(View v) {
		if(v == ivClose){
			dismiss();
		}
	}
}
