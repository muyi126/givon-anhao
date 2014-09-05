package com.givon.anhao.welcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.jasonfry.android.tools.ui.PageControl;
import uk.co.jasonfry.android.tools.ui.SwipeView;
import uk.co.jasonfry.android.tools.ui.SwipeView.OnPageChangedListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.support.httpclient.HttpCallBack;
import com.android.support.httpclient.HttpClientAsync;
import com.android.support.httpclient.HttpParams;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.HanziToPinyin;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.BaseActivity;
import com.givon.anhao.Constant;
import com.givon.anhao.R;
import com.givon.anhao.activity.AnhaoMainActivity;
import com.givon.anhao.db.YeUserDao;
import com.givon.anhao.utils.CommonUtils;
import com.givon.anhao.utils.HttpUtil;
import com.givon.baseproject.entity.ErrorCode;
import com.givon.baseproject.entity.UserBean;
import com.givon.baseproject.entity.UserEntity;
import com.givon.baseproject.util.ShareCookie;
import com.givon.baseproject.util.StringUtil;
import com.givon.baseproject.util.ToastUtil;
import com.nineoldandroids.view.KeyboardLayout;
import com.nineoldandroids.view.KeyboardLayout.onKybdsChangeListener;

public class ActAnhaoWelcome extends BaseActivity {
	SwipeView mSwipeView;
	ArrayList<Object> lists = new ArrayList<Object>();
	private ImageView mLogoImageView;
	private TextView mTishiTextView;

	private int durationMillis = 1000;
	private int delayMillis = 1000;
	private boolean isUP = false;
	private int mSwipeViewX_1;
	private int mSwipeViewX_2;
	private float mLogoImageViewX_1;
	private float mLogoImageViewX_2;
	private float mLogoImageViewH_1;
	private EditText user;
	private EditText psd;
	private boolean progressShow;
	private Button mStarButton;
	private boolean isRegist = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_welcome_anhao);
		// girlImageView = (ImageView) findViewById(R.id.welcome_girl);
		// girlImageView_2 = (ImageView) findViewById(R.id.welcome_girl_2);
		// animate(girlImageView_2).rotationYBy(-90).setDuration(500);
		mLogoImageView = (ImageView) findViewById(R.id.logo);
		PageControl mPageControl = (PageControl) findViewById(R.id.page_control);
		mSwipeView = (SwipeView) findViewById(R.id.swipe_view);
		mTishiTextView = (TextView) findViewById(R.id.tv_tishi);
		int margin = mSwipeView.setPageWidth(getResources().getDisplayMetrics().widthPixels);

		loadImages();
		keyBoardListener();
		for (int i = 0; i < 4; i++) {
			FrameLayout frame = new FrameLayout(this);
			if (i == 0) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				params.leftMargin = margin;
				frame.setLayoutParams(params);
			} else if (i == 6) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				params.rightMargin = margin;
				frame.setLayoutParams(params);
			}

			mSwipeView.addView(frame);
		}

		((ViewGroup) mSwipeView.getChildContainer().getChildAt(0)).addView(((WelcomeView_1) lists
				.get(0)).getView());
		((ViewGroup) mSwipeView.getChildContainer().getChildAt(1)).addView(((WelcomeView_2) lists
				.get(1)).getView());
		SwipeImageLoader mSwipeImageLoader = new SwipeImageLoader();

		mSwipeView.setOnPageChangedListener(mSwipeImageLoader);

		mSwipeView.setPageControl(mPageControl);
		
		if(ShareCookie.isLoginAuth()){
			UserBean bean = ShareCookie.getUserInfo();
			AnhaoApplication.getInstance().setUserName(bean.getEasemobId());
			AnhaoApplication.getInstance().setPassword(bean.getEasemobPassword());
			login(bean.getEasemobId(), bean.getEasemobPassword());
//			startActivity(new Intent(ActAnhaoWelcome.this, AnhaoMainActivity.class));
//			finish();
		}else {
			
		}
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}

	private void keyBoardListener() {
		KeyboardLayout mainView = (KeyboardLayout) findViewById(R.id.mainbg);
		mainView.setOnkbdStateListener(new onKybdsChangeListener() {
			public void onKeyBoardStateChange(int state, int height) {
				switch (state) {
				case KeyboardLayout.KEYBOARD_STATE_HIDE:
					// Toast.makeText(getApplicationContext(), "软键盘隐藏", Toast.LENGTH_SHORT).show();
					slideviewLogo(height, 0);
					break;
				case KeyboardLayout.KEYBOARD_STATE_SHOW:
					if (mLogoImageViewX_1 == 0) {
						mLogoImageViewX_1 = mLogoImageView.getTop();
					}
					if (mSwipeViewX_1 == 0) {
						mSwipeViewX_1 = (int) mSwipeView.getTop();
					}
					if (mLogoImageViewH_1 == 0) {
						mLogoImageViewH_1 = mLogoImageView.getHeight();
					}
					// Toast.makeText(getApplicationContext(), "软键盘弹起", Toast.LENGTH_SHORT).show();
					slideview(height, 0);
					break;
				}
			}
		});

		mTishiTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRegist) {
					isRegist = false;
					mTishiTextView.setText("还没有暗号账号？点击注册");
					if (null != mStarButton) {
						mStarButton.setBackgroundResource(R.drawable.login_button_1);
					}
				} else {

					if (null != mStarButton) {
						mStarButton.setBackgroundResource(R.drawable.login_button);
					}
					isRegist = true;
					mTishiTextView.setText("已经拥有暗号账号？直接登录");
				}

				for (int i = mSwipeView.getCurrentPage(); i < mSwipeView.getChildContainer()
						.getChildCount(); i++) {
					mSwipeView.smoothScrollToPage(i);
				}
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	public void slideviewLogo(final float p1, final float p2) {
		if (isUP) {
			mLogoImageView.setVisibility(View.VISIBLE);
			TranslateAnimation animation = new TranslateAnimation(0, 0, 0, mSwipeViewX_1);
			animation.setInterpolator(new OvershootInterpolator());
			animation.setDuration(durationMillis);
			animation.setStartOffset(delayMillis);
			animation.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					int left = mSwipeView.getLeft();
					int top = (int) (mSwipeViewX_1);
					int width = mSwipeView.getWidth();
					int height = mSwipeView.getHeight()
							- (AnhaoApplication.dip2px(ActAnhaoWelcome.this, 0));
					mSwipeView.clearAnimation();
					mSwipeView.layout(left, top, left + width, (int) top + height);
				}
			});
			mSwipeView.startAnimation(animation);
			TranslateAnimation animation1 = new TranslateAnimation(0, 0, mLogoImageView.getTop()
					- p1, AnhaoApplication.dip2px(this, 40));
			animation1.setInterpolator(new OvershootInterpolator());
			animation1.setDuration(durationMillis);
			animation1.setStartOffset(delayMillis);
			animation1.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					int left = mLogoImageView.getLeft();
					int top = (int) (AnhaoApplication.dip2px(ActAnhaoWelcome.this, 40));
					int width = mLogoImageView.getWidth();
					int height = mLogoImageView.getHeight();
					mLogoImageView.clearAnimation();
					mLogoImageView.layout(left, top, left + width, (int) top + height);
				}
			});
			mLogoImageView.startAnimation(animation1);
			isUP = false;
		}
	}

	public void slideview(final float p1, final float p2) {

		if (!isUP) {
			TranslateAnimation animation1 = new TranslateAnimation(0, 0, mLogoImageView.getTop()
					- AnhaoApplication.dip2px(this, 40), mLogoImageView.getTop() - p1);
			animation1.setInterpolator(new OvershootInterpolator());
			animation1.setDuration(durationMillis);
			animation1.setStartOffset(delayMillis);
			animation1.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					int left = mLogoImageView.getLeft();
					int top = (int) (mLogoImageView.getTop() - p1);
					int width = mLogoImageView.getWidth();
					int height = mLogoImageView.getHeight();
					mLogoImageView.clearAnimation();
					mLogoImageView.layout(left, top, left + width, (int) top + height);
					mLogoImageView.setVisibility(View.GONE);
				}
			});
			mLogoImageView.startAnimation(animation1);
			TranslateAnimation animation2 = new TranslateAnimation(0, 0, 0, mLogoImageViewH_1 - p1
					+ (AnhaoApplication.dip2px(ActAnhaoWelcome.this, 40)));
			animation2.setInterpolator(new OvershootInterpolator());
			animation2.setDuration(durationMillis);
			animation2.setStartOffset(delayMillis);
			animation2.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					int left = mSwipeView.getLeft();
					int top = (int) (AnhaoApplication.dip2px(ActAnhaoWelcome.this, 40));
					int width = mSwipeView.getWidth();
					int height = mSwipeView.getHeight();
					mSwipeView.clearAnimation();
					mSwipeView.layout(left, top, left + width, (int) top + height);
				}
			});
			mSwipeView.startAnimation(animation2);
			isUP = true;
		}
	}

	private class SwipeImageLoader implements OnPageChangedListener {

		public void onPageChanged(int oldPage, int newPage) {
			if (newPage > oldPage) {
				if (newPage != (mSwipeView.getPageCount() - 1)) {
					if (lists.get(newPage + 1) instanceof WelcomeView_1) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage + 1))
								.addView(((WelcomeView_1) lists.get(newPage + 1)).getView());
					} else if (lists.get(newPage + 1) instanceof WelcomeView_2) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage + 1))
								.addView(((WelcomeView_2) lists.get(newPage + 1)).getView());

					} else if (lists.get(newPage + 1) instanceof WelcomeView_3) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage + 1))
								.addView(((WelcomeView_3) lists.get(newPage + 1)).getView());

					} else if (lists.get(newPage + 1) instanceof WelcomeView_4) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage + 1))
								.addView(((WelcomeView_4) lists.get(newPage + 1)).getView());

					}

				}
				if (oldPage != 0) {
					((ViewGroup) mSwipeView.getChildContainer().getChildAt(oldPage - 1))
							.removeAllViews();
					if ((newPage + 1) == mSwipeView.getPageCount()) {
						if (lists.get(newPage) instanceof WelcomeView_4) {
							// 事件
						}
					}

				}
				if (lists.get(newPage) instanceof WelcomeView_1) {

					((WelcomeView_1) lists.get(newPage)).startAnim();
				} else if (lists.get(newPage) instanceof WelcomeView_2) {
					((WelcomeView_2) lists.get(newPage)).startAnim();
				} else if (lists.get(newPage) instanceof WelcomeView_3) {
					((WelcomeView_3) lists.get(newPage)).startAnim();

				} else if (lists.get(newPage) instanceof WelcomeView_4) {
					user = (EditText) ((WelcomeView_4) lists.get(newPage)).getView().findViewById(
							R.id.user);
					psd = (EditText) ((WelcomeView_4) lists.get(newPage)).getView().findViewById(
							R.id.password);
					mStarButton = (Button) findViewById(R.id.login);
					if (isRegist) {
						mStarButton.setBackgroundResource(R.drawable.login_button);
					} else {
						mStarButton.setBackgroundResource(R.drawable.login_button_1);
					}
					if (AnhaoApplication.getInstance().getUserName() != null
							&& AnhaoApplication.getInstance().getPassword() != null) {
						startActivity(new Intent(ActAnhaoWelcome.this, AnhaoMainActivity.class));
						finish();
						
					}
					// 如果用户名改变，清空密码
					user.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {
							psd.setText(null);
						}

						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,
								int after) {

						}

						@Override
						public void afterTextChanged(Editable s) {

						}
					});
					mStarButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (isRegist) {
								// mStarButton.setBackgroundResource(R.drawable.login_button);
								register(v);
							} else {
								// mStarButton.setBackgroundResource(R.drawable.login_button_1);
//								login();
								loginAnhao(user.getText().toString().trim(), psd.getText().toString().trim());
							}
						}
					});

				}
			} else {
				if (newPage != 0) {
					if (lists.get(newPage - 1) instanceof WelcomeView_1) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage - 1))
								.addView(((WelcomeView_1) lists.get(newPage - 1)).getView());
					} else if (lists.get(newPage - 1) instanceof WelcomeView_2) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage - 1))
								.addView(((WelcomeView_2) lists.get(newPage - 1)).getView());

					} else if (lists.get(newPage - 1) instanceof WelcomeView_3) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage - 1))
								.addView(((WelcomeView_3) lists.get(newPage - 1)).getView());

					} else if (lists.get(newPage - 1) instanceof WelcomeView_4) {
						((ViewGroup) mSwipeView.getChildContainer().getChildAt(newPage - 1))
								.addView(((WelcomeView_4) lists.get(newPage - 1)).getView());

					}
				}
				if (oldPage != (mSwipeView.getPageCount() - 1)) {
					((ViewGroup) mSwipeView.getChildContainer().getChildAt(oldPage + 1))
							.removeAllViews();
				}
			}

		}

	}

	private void loadImages() {
		WelcomeView_1 entity_0 = new WelcomeView_1(ActAnhaoWelcome.this);
		WelcomeView_2 entity_1 = new WelcomeView_2(ActAnhaoWelcome.this);
		WelcomeView_3 entity_2 = new WelcomeView_3(ActAnhaoWelcome.this);
		WelcomeView_4 entity_3 = new WelcomeView_4(ActAnhaoWelcome.this);
		lists.add(entity_0);
		lists.add(entity_1);
		lists.add(entity_2);
		lists.add(entity_3);

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		System.out.println("dispatchKeyEvent:" + event.getAction() + " " + KeyEvent.KEYCODE_BACK
				+ "  " + KeyEvent.KEYCODE_CLEAR);
		return super.dispatchKeyEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		System.out.println("dispatchTouchEvent:" + ev.getAction());
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		System.out.println("onTouchEvent:" + event.getAction());
		return super.onTouchEvent(event);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean isResult = super.onKeyDown(keyCode, event);
		System.out.println("Activity:onKeyDown:" + event.getAction() + " " + keyCode + "  "
				+ KeyEvent.KEYCODE_CLEAR + " " + isResult);
		if (null != user && user.isFocused()) {
			user.onKeyDown(keyCode, event);
		}
		if (null != psd && psd.isFocused()) {
			psd.onKeyDown(keyCode, event);
		}
		return isResult;
	}

	/*
	 * 登陆
	 * 
	 * @param view
	 */
	public void login(final String username,final String password) {
		dismissWaitingDialog();
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
//		final String username = user.getText().toString();
//		final String password = psd.getText().toString();
		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
			// progressShow = true;
			// final ProgressDialog pd = new ProgressDialog(ActAnhaoWelcome.this);
			// pd.setCanceledOnTouchOutside(false);
			// pd.setOnCancelListener(new OnCancelListener() {
			//
			// @Override
			// public void onCancel(DialogInterface dialog) {
			// progressShow = false;
			// }
			// });
			// pd.setMessage("正在登陆...");
			// pd.show();
			showWaitingDialog("正在登陆...");
			
			// 调用sdk登陆方法登陆聊天服务器
			EMChatManager.getInstance().login(username, password, new EMCallBack() {

				@Override
				public void onSuccess() {
					if (!isShowingDialog()) {
						return;
					}
					// 登陆成功，保存用户名密码
					AnhaoApplication.getInstance().setUserName(username);
					AnhaoApplication.getInstance().setPassword(password);
					 runOnUiThread(new Runnable() {
					 public void run() {
						 dismissWaitingDialog();
						 showWaitingDialog("正在获取好友和群聊列表...");
					 }
					 });
					
					
					try {
						YeUserDao dao = new YeUserDao(ActAnhaoWelcome.this);
						
						// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
						List<String> usernames;
						Map<String, UserBean> userlist;
						if(!dao.hasData()){
							usernames = EMChatManager.getInstance().getContactUserNames();
							userlist = new HashMap<String, UserBean>();
							for (String username : usernames) {
								UserBean user = new UserBean();
								user.setUsername(username);
								setUserHearder(username, user);
								userlist.put(username, user);
							}
							List<UserBean> users = new ArrayList<UserBean>(userlist.values());
							dao.saveContactList(users);
						}else {
							userlist = dao.getContactList();
						}
//						userlist = new HashMap<String, UserBean>();
//						for (String username : usernames) {
//							UserBean user = new UserBean();
//							user.setUsername(username);
//							setUserHearder(username, user);
//							userlist.put(username, user);
//						}
						// 添加user"申请与通知"
						UserBean newFriends = new UserBean();
						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
						newFriends.setNick("申请与通知");
						newFriends.setHeader("");
						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
						// 添加"群聊"
						UserBean groupUser = new UserBean();
						groupUser.setUsername(Constant.GROUP_USERNAME);
						groupUser.setNick("群聊");
						groupUser.setHeader("");
						userlist.put(Constant.GROUP_USERNAME, groupUser);

						// 存入内存
						AnhaoApplication.getInstance().setContactList(userlist);
						// 存入db  移动到前面处理
//						YeUserDao dao = new YeUserDao(ActAnhaoWelcome.this);
//						List<UserBean> users = new ArrayList<UserBean>(userlist.values());
//						dao.saveContactList(users);

						// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
						EMGroupManager.getInstance().getGroupsFromServer();
						// after login, we join groups in separate threads;
						EMGroupManager.getInstance().joinGroupsAfterLogin();
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (!ActAnhaoWelcome.this.isFinishing()) {
						// pd.dismiss();
						runOnUiThread(new Runnable() {
							public void run() {
								dismissWaitingDialog();
							}
						});
						
					}
					// 进入主页面
					startActivity(new Intent(ActAnhaoWelcome.this, AnhaoMainActivity.class));
					finish();
				}

				@Override
				public void onProgress(int progress, String status) {

				}

				@Override
				public void onError(int code, final String message) {
					if (!isShowingDialog()) {
						return;
					}
					
					runOnUiThread(new Runnable() {
						public void run() {
							dismissWaitingDialog();
							if (message.indexOf("not support the capital letters") != -1) {
								Toast.makeText(getApplicationContext(), "用户名不支持大写字母", 0).show();
							} else {
								Toast.makeText(getApplicationContext(), "登录失败: " + message, 0)
										.show();
							}

						}
					});
				}
			});
		}
	}

	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		final String username = user.getText().toString().trim();
		final String pwd = psd.getText().toString().trim();
		String confirm_pwd = psd.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, "用户名不能为空！", Toast.LENGTH_SHORT).show();
			user.requestFocus();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
			psd.requestFocus();
			return;
		} else if (TextUtils.isEmpty(confirm_pwd)) {
			Toast.makeText(this, "确认密码不能为空！", Toast.LENGTH_SHORT).show();
			psd.requestFocus();
			return;
		} else if (!pwd.equals(confirm_pwd)) {
			Toast.makeText(this, "两次输入的密码不一致，请重新输入！", Toast.LENGTH_SHORT).show();
			return;
		}

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
			// final ProgressDialog pd = new ProgressDialog(this);
			// pd.setMessage("正在注册...");
			// pd.show();

			HttpClientAsync clientAsync = HttpClientAsync.getInstance();
			HttpParams params = new HttpParams();
			params.put("username", username);
			params.put("nickname", "anan");
			params.put("password", pwd);
			params.put("userSex", "1");
			clientAsync.post(HttpUtil.getUrl(HttpUtil.REGIST), params, new HttpCallBack() {

				@Override
				public void onHttpSuccess(Object obj) {
					dismissWaitingDialog();
					UserEntity entity = (UserEntity) obj;
					if (null != entity && null != entity.getResult()) {
						UserBean bean = entity.getResult();
						bean.setPassword(pwd);
						ShareCookie.saveUserInfo(bean);
						ShareCookie.setLoginAuth(true);
						// 登陆成功，保存用户名密码
						AnhaoApplication.getInstance().setUserName(bean.getEasemobId());
						AnhaoApplication.getInstance().setPassword(bean.getEasemobPassword());
						loginAnhao(bean.getEasemobId(), bean.getEasemobPassword());
						// runOnUiThread(new Runnable() {
						// public void run() {
						// pd.setMessage("正在获取好友和群聊列表...");
						// }
						// });
//
//						showWaitingDialog("正在获取好友和群聊列表...");
//						try {
//							// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
//							List<String> usernames = EMChatManager.getInstance()
//									.getContactUserNames();
//							Map<String, User> userlist = new HashMap<String, User>();
//							for (String username : usernames) {
//								User user = new User();
//								user.setUsername(username);
//								setUserHearder(username, user);
//								userlist.put(username, user);
//							}
//							// 添加user"申请与通知"
//							User newFriends = new User();
//							newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//							newFriends.setNick("申请与通知");
//							newFriends.setHeader("");
//							userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//							// 添加"群聊"
//							User groupUser = new User();
//							groupUser.setUsername(Constant.GROUP_USERNAME);
//							groupUser.setNick("群聊");
//							groupUser.setHeader("");
//							userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//							// 存入内存
//							AnhaoApplication.getInstance().setContactList(userlist);
//							// 存入db
//							UserDao dao = new UserDao(ActAnhaoWelcome.this);
//							List<User> users = new ArrayList<User>(userlist.values());
//							dao.saveContactList(users);
//
//							// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
//							EMGroupManager.getInstance().getGroupsFromServer();
//							// after login, we join groups in separate threads;
//							EMGroupManager.getInstance().joinGroupsAfterLogin();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						if (!ActAnhaoWelcome.this.isFinishing()) {
//							// pd.dismiss();
//							dismissWaitingDialog();
//						}
//						// 进入主页面
//						startActivity(new Intent(ActAnhaoWelcome.this, AnhaoMainActivity.class));
//						finish();
					}
				}

				@Override
				public void onHttpStarted() {
					showWaitingDialog("正在注册...");
				}

				@Override
				public void onHttpFailure(Exception e, String message, int Errorcode) {
					dismissWaitingDialog();
					e.printStackTrace();
					if (Errorcode != 0) {
						message = ErrorCode.getCodeValue(Errorcode);
						if (StringUtil.isEmpty(message)) {
							ToastUtil.showMessage(getString(R.string.network_isnot_available));
						} else {
							ToastUtil.showMessage(message);
						}
					}
				}

			}, UserEntity.class);
			// new Thread(new Runnable() {
			// public void run() {
			// try {
			// // 调用sdk注册方法
			// EMChatManager.getInstance().createAccountOnServer(username, pwd);
			// runOnUiThread(new Runnable() {
			// public void run() {
			// pd.dismiss();
			// // 保存用户名
			// AnhaoApplication.getInstance().setUserName(username);
			// Toast.makeText(getApplicationContext(), "注册成功", 0).show();
			// login();
			// }
			// });
			// } catch (final Exception e) {
			// runOnUiThread(new Runnable() {
			// public void run() {
			// pd.dismiss();
			// if (e != null && e.getMessage() != null) {
			// String errorMsg = e.getMessage();
			// if (errorMsg.indexOf("EMNetworkUnconnectedException") != -1) {
			// Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", 0)
			// .show();
			// } else if (errorMsg.indexOf("conflict") != -1) {
			// Toast.makeText(getApplicationContext(), "用户已存在！", 0).show();
			// } else if (errorMsg.indexOf("not support the capital letters") != -1) {
			// Toast.makeText(getApplicationContext(), "用户名不支持大写字母！", 0)
			// .show();
			// } else {
			// Toast.makeText(getApplicationContext(),
			// "注册失败: " + e.getMessage(), 1).show();
			// }
			//
			// } else {
			// Toast.makeText(getApplicationContext(), "注册失败: 未知异常", 1).show();
			//
			// }
			// }
			// });
			// }
			// }
			// }).start();

		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, UserBean user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
					.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	/**
	 * 暗号登录
	 */
	private void loginAnhao(String username, final String password) {
		HttpParams params = new HttpParams();
		params.put("username", username);
		params.put("password", password);
		HttpClientAsync httpClientAsync = HttpClientAsync.getInstance();
		httpClientAsync.post(HttpUtil.getUrl(HttpUtil.LOGIN), params, new HttpCallBack() {

			@Override
			public void onHttpSuccess(Object obj) {
				dismissWaitingDialog();
				UserEntity entity = (UserEntity) obj;
				if (null != entity && null != entity.getResult()) {
					UserBean bean = entity.getResult();
					bean.setPassword(password);
					ShareCookie.saveUserInfo(bean);
					ShareCookie.setLoginAuth(true);
					// 登陆成功，保存用户名密码
					AnhaoApplication.getInstance().setUserName(bean.getEasemobId());
					AnhaoApplication.getInstance().setPassword(bean.getEasemobPassword());
					login(bean.getEasemobId(), bean.getEasemobPassword());
					// runOnUiThread(new Runnable() {
					// public void run() {
					// pd.setMessage("正在获取好友和群聊列表...");
					// }
					// });
//
//					showWaitingDialog("正在获取好友和群聊列表...");
//					try {
//						// demo中简单的处理成每次登陆都去获取好友username，开发者自己根据情况而定
//						List<String> usernames = EMChatManager.getInstance()
//								.getContactUserNames();
//						Map<String, User> userlist = new HashMap<String, User>();
//						for (String username : usernames) {
//							User user = new User();
//							user.setUsername(username);
//							setUserHearder(username, user);
//							userlist.put(username, user);
//						}
//						// 添加user"申请与通知"
//						User newFriends = new User();
//						newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
//						newFriends.setNick("申请与通知");
//						newFriends.setHeader("");
//						userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
//						// 添加"群聊"
//						User groupUser = new User();
//						groupUser.setUsername(Constant.GROUP_USERNAME);
//						groupUser.setNick("群聊");
//						groupUser.setHeader("");
//						userlist.put(Constant.GROUP_USERNAME, groupUser);
//
//						// 存入内存
//						AnhaoApplication.getInstance().setContactList(userlist);
//						// 存入db
//						UserDao dao = new UserDao(ActAnhaoWelcome.this);
//						List<User> users = new ArrayList<User>(userlist.values());
//						dao.saveContactList(users);
//
//						// 获取群聊列表,sdk会把群组存入到EMGroupManager和db中
//						EMGroupManager.getInstance().getGroupsFromServer();
//						// after login, we join groups in separate threads;
//						EMGroupManager.getInstance().joinGroupsAfterLogin();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//					if (!ActAnhaoWelcome.this.isFinishing()) {
//						// pd.dismiss();
//						dismissWaitingDialog();
//					}
//					// 进入主页面
//					startActivity(new Intent(ActAnhaoWelcome.this, AnhaoMainActivity.class));
//					finish();
				
				}
			}

			@Override
			public void onHttpStarted() {
				showWaitingDialog("正在登陆...");
			}

			@Override
			public void onHttpFailure(Exception e, String message, int Errorcode) {
				dismissWaitingDialog();
				e.printStackTrace();
				if (Errorcode != 0) {
					message = ErrorCode.getCodeValue(Errorcode);
					if (StringUtil.isEmpty(message)) {
						ToastUtil.showMessage(getString(R.string.network_isnot_available));
					} else {
						ToastUtil.showMessage(message);
					}
				}
			}
		}, UserEntity.class);

	}
}