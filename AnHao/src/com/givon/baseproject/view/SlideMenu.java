/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @SlideMenu.java  2014-2-20 下午2:49:23 - Carson
 * @author YanXu
 * @email:981385016@qq.com
 * @version 1.0
 */

package com.givon.baseproject.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.givon.anhao.R;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class SlideMenu extends FrameLayout implements GestureDetector.OnGestureListener {

	private ImageView mImageShadow;
	private ImageView mBackground;
	private LinearLayout mLayoutMenu;
	private ScrollView sv_menu;
	private AnimatorSet scaleUp_shadow;
	private AnimatorSet scaleUp_activity;
	private AnimatorSet scaleDown_activity;
	private AnimatorSet scaleDown_shadow;
	/** 这个activity是该控件依附的activity */
	private Activity activity;
	/** 这个布局来自activity */
	private ViewGroup view_decor;
	/** 这个ViewGroup来自activity */
	private ViewGroup view_activity;
	/** 标志菜单打开状态 */
	private boolean isOpened;
	private GestureDetector gestureDetector;
	private float shadow_ScaleX;
	/** 不想拦截哪个View的触摸屏事件 */
	private List<View> ignoredViews;
	private List<View> ignoredViewsBack;
	private List<SlideMenuItem> menuItems;
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private OnMenuListener menuListener;

	public SlideMenu(Context context) {
		super(context);
		initViews(context);
	}

	private void initViews(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.slidemenu, this);
		sv_menu = (ScrollView) findViewById(R.id.sv_menu);
		mImageShadow = (ImageView) findViewById(R.id.iv_shadow);
		mLayoutMenu = (LinearLayout) findViewById(R.id.layout_menu);
		mBackground = (ImageView) findViewById(R.id.iv_background);
	}

	/**
	 * 这个方法用来设置菜单项需要显示到哪一个activity
	 * 
	 * @param activity
	 */
	public void attachToActivity(Activity activity) {
		initValue(activity);
		setShadowScaleXByOrientation();
		buildAnimationSet();
	}

	private void initValue(Activity activity) {
		this.activity = activity;
		menuItems = new ArrayList<SlideMenuItem>();
		gestureDetector = new GestureDetector(activity, this);
		ignoredViews = new ArrayList<View>();
		ignoredViewsBack = new ArrayList<View>();
		view_decor = (ViewGroup) activity.getWindow().getDecorView();
		view_activity = (ViewGroup) view_decor.getChildAt(0);
	}

	private void setShadowScaleXByOrientation() {
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			shadow_ScaleX = 0.5335f;
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			shadow_ScaleX = 0.504f;
		}
	}

	/**
	 * 设置背景图片
	 */
	public void setBackground(int imageResrouce) {
		mBackground.setImageResource(imageResrouce);
	}

	/**
	 * 在activity下面显示阴影
	 */
	public void setShadowVisible(boolean isVisible) {
		if (isVisible)
			mImageShadow.setImageResource(R.drawable.shadow);
		else
			mImageShadow.setImageBitmap(null);
	}

	/**
	 * 添加菜单项到菜单
	 */
	public void addMenuItem(SlideMenuItem menuItem) {
		this.menuItems.add(menuItem);
	}

	/**
	 * 用List设置菜单项
	 */
	public void setMenuItems(List<SlideMenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * 得到菜单项集合
	 */
	public List<SlideMenuItem> getMenuItems() {
		return menuItems;
	}

	/**
	 * 如果你需要在activity关闭或打开时设置某事件，设置这个监听器
	 */
	public void setMenuListener(OnMenuListener menuListener) {
		this.menuListener = menuListener;
	}

	/**
	 * activity关闭或打开时的回调
	 */
	public OnMenuListener getMenuListener() {
		return menuListener;
	}

	/**
	 * 如果我们需要调用这个方法，要在在菜单显示之前，因为padding属性需要在onCreateView()之前设置
	 */
	private void setViewPadding() {
		this.setPadding(view_activity.getPaddingLeft(), view_activity.getPaddingTop(),
				view_activity.getPaddingRight(), view_activity.getPaddingBottom());
	}

	public void openMenu() {
		if (!isOpened) {
			isOpened = true;
			showOpenMenuRelative();
			ignoredViewsBack.clear();
			ignoredViewsBack.addAll(ignoredViews);
			ignoredViews.clear();
		}
	}

	/**
	 * 移除菜单布局（内容界面全屏时）
	 */
	private void removeMenuLayout() {
		ViewGroup parent = ((ViewGroup) sv_menu.getParent());
		parent.removeView(sv_menu);
	}

	public void closeMenu() {
		if (isOpened) {
			isOpened = false;
			scaleUp_activity.start();
			ignoredViews.addAll(ignoredViewsBack);
		}
	}

	public boolean isOpened() {
		return isOpened;
	}

	/**
	 * 打开菜单相关的方法
	 */
	private void showOpenMenuRelative() {
		setViewPadding();
		scaleDown_activity.start();
		// remove self if has not remove
		if (getParent() != null)
			view_decor.removeView(this);
		if (sv_menu.getParent() != null)
			removeMenuLayout();
		view_decor.addView(this, 0);
		view_decor.addView(sv_menu);
	}

	/**
	 * 过渡动画
	 */
	private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
		@Override
		public void onAnimationStart(Animator animation) {
			if (isOpened) {
				mLayoutMenu.removeAllViews();
				showMenuDelay();
				if (menuListener != null)
					menuListener.openMenu();
			}
		}

		/**
		 * 过渡动画结束时被回调
		 */
		@Override
		public void onAnimationEnd(Animator animation) {
			// reset the view;
			if (!isOpened) {
				view_decor.removeView(SlideMenu.this);
				view_decor.removeView(sv_menu);
				if (menuListener != null)
					menuListener.closeMenu();
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {

		}

		@Override
		public void onAnimationRepeat(Animator animation) {

		}
	};

	/**
	 * 延迟显示菜单（实现菜单逐个蹦出来的效果）
	 */
	private void showMenuDelay() {
		mLayoutMenu.removeAllViews();
		for (int i = 0; i < menuItems.size(); i++)
			showMenuItem(menuItems.get(i), i);
	}

	/**
	 * 显示菜单
	 */
	private void showMenuItem(SlideMenuItem menuItem, int menu_index) {
		mLayoutMenu.addView(menuItem);
		ViewHelper.setAlpha(menuItem, 0);
		AnimatorSet scaleUp = new AnimatorSet();
		scaleUp.playTogether(ObjectAnimator.ofFloat(menuItem, "translationX", -100.f, 0.0f),
				ObjectAnimator.ofFloat(menuItem, "alpha", 0.0f, 1.0f));

		scaleUp.setInterpolator(AnimationUtils.loadInterpolator(activity,
				android.R.anim.anticipate_overshoot_interpolator));
		// with animation;
		scaleUp.setStartDelay(50 * menu_index);
		scaleUp.setDuration(400).start();
	}

	private void buildAnimationSet() {
		scaleUp_activity = buildScaleUpAnimation(view_activity, 1.0f, 1.0f);
		scaleUp_shadow = buildScaleUpAnimation(mImageShadow, 1.0f, 1.0f);
		scaleDown_activity = buildScaleDownAnimation(view_activity, 0.5f, 0.5f);
		scaleDown_shadow = buildScaleDownAnimation(mImageShadow, shadow_ScaleX, 0.475f);
		scaleUp_activity.addListener(animationListener);
		scaleUp_activity.playTogether(scaleUp_shadow);
		scaleDown_shadow.addListener(animationListener);
		scaleDown_activity.playTogether(scaleDown_shadow);
	}

	/**
	 * 创建菜单退出的动画效果
	 * 
	 * @param target
	 * @param targetScaleX
	 * @param targetScaleY
	 * @return
	 */
	private AnimatorSet buildScaleDownAnimation(View target, float targetScaleX, float targetScaleY) {
		int pivotX = (int) (getScreenWidth() * 1.5);
		int pivotY = (int) (getScreenHeight() * 0.5);

		ViewHelper.setPivotX(target, pivotX);
		ViewHelper.setPivotY(target, pivotY);
		AnimatorSet scaleDown = new AnimatorSet();
		scaleDown.playTogether(ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
				ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));

		scaleDown.setInterpolator(AnimationUtils.loadInterpolator(activity,
				android.R.anim.decelerate_interpolator));
		scaleDown.setDuration(250);
		return scaleDown;
	}

	/**
	 * 创建菜单打开的动画效果
	 * 
	 * @param target
	 * @param targetScaleX
	 * @param targetScaleY
	 * @return
	 */
	private AnimatorSet buildScaleUpAnimation(View target, float targetScaleX, float targetScaleY) {
		AnimatorSet scaleUp = new AnimatorSet();
		scaleUp.playTogether(ObjectAnimator.ofFloat(target, "scaleX", targetScaleX),
				ObjectAnimator.ofFloat(target, "scaleY", targetScaleY));
		scaleUp.setDuration(250);
		return scaleUp;
	}

	/**
	 * 添加不拦截触摸事件的控件
	 */
	public void addIgnoredView(View v) {
		for (int i = 0; i < ignoredViews.size(); i++) {
			if (v == ignoredViews.get(i)) {
				return;
			}
		}
		ignoredViews.add(v);
	}

	/**
	 * 移除不拦截触摸事件的控件
	 * 
	 * @param v
	 */
	public void removeIgnoredView(View v) {
		ignoredViews.remove(v);
	}

	/**
	 * 清空不拦截触摸事件的控件
	 */
	public void clearIgnoredViewList() {
		ignoredViews.clear();
	}

	/**
	 * 判断触摸是否发生在 不拦截触摸事件的控件上
	 * 
	 * @Rect类主要用于表示坐标系中的一块矩形区域
	 * @param ev
	 * @return
	 */
	private boolean isInIgnoredView(MotionEvent ev) {
		Rect rect = new Rect();
		for (View v : ignoredViews) {
			v.getGlobalVisibleRect(rect); // 将view位置保存到rect中
			if (rect.contains((int) ev.getX(), (int) ev.getY()))
				return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean t = gestureDetector.onTouchEvent(event);
//		System.out.println("onTouchEvent----slidemenu "+t);
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		boolean t = super.onInterceptTouchEvent(ev);
//		System.out.println("onInterceptTouchEvent----slidemenu "+t);
		return t;
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
		System.out.println("onFling----slidemenu");
		if (isInIgnoredView(motionEvent) || isInIgnoredView(motionEvent2))
			return false;

		int distanceX = (int) (motionEvent2.getX() - motionEvent.getX());
		int distanceY = (int) (motionEvent2.getY() - motionEvent.getY());
		int screenWidth = (int) getScreenWidth();

		if (Math.abs(distanceY) > screenWidth * 0.3)
			return false;

		if (Math.abs(distanceX) > screenWidth * 0.3) {
			if (distanceX > 0 && !isOpened) {
				// from left to right;
				openMenu();
			} else if (distanceX < 0 && isOpened) {
				// from right th left;
				closeMenu();
			}
		}
		return false;
	}

	public int getScreenHeight() {
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}

	public int getScreenWidth() {
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}

	public interface OnMenuListener {

		public void openMenu();

		public void closeMenu();
	}

}
