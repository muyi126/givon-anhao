/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.givon.anhao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.EMNotifier;
import com.easemob.chat.GroupChangeListener;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.HanziToPinyin;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.BaseFragmentActivity;
import com.givon.anhao.Constant;
import com.givon.anhao.R;
import com.givon.anhao.db.HelloUserDao;
import com.givon.anhao.db.InviteMessgeDao;
import com.givon.anhao.db.UserDao;
import com.givon.anhao.db.UserDaoOld;
import com.givon.anhao.domain.InviteMessage;
import com.givon.anhao.domain.InviteMessage.InviteMesageStatus;
import com.givon.anhao.domain.User;
import com.givon.anhao.utils.CommonUtils;
import com.givon.baseproject.entity.UserBean;
import com.givon.baseproject.util.ToastUtil;
import com.givon.baseproject.view.AppTitleBar;
import com.givon.baseproject.view.SlideMenu;
import com.givon.baseproject.view.SlideMenuItem;

public class AnhaoMainActivity extends BaseFragmentActivity {

	protected static final String TAG = "MainActivity";
	// 未读消息textview
	private TextView unreadLabel;
	// 未读通讯录textview
	private TextView unreadAddressLable;

	// private Button[] mTabs;
	// private ContactlistFragment contactListFragment;
	private ChatHistoryFragment chatHistoryFragment;
	private GroupFragment groupFragment;
	// private SettingsFragment settingFragment;
	private Fragment[] fragments;
	private int index;
	private RelativeLayout[] tab_containers;
	// 当前fragment的index
	private int currentTabIndex;
	private NewMessageBroadcastReceiver msgReceiver;
	// 账号在别处登录
	private boolean isConflict = false;
	public static SlideMenu mSlideMenu;
	private SlideMenuItem mItemHome;
	private SlideMenuItem mItemSet;
	// private SlideMenuItem mItemOrders;
	// private SlideMenuItem mItemMyTianGou;
	// private SlideMenuItem mItemMore;
	// private SlideMenuItem mItemLogin;
	private AppTitleBar mTitleBar;
	private boolean isGroup = true;

	public class SDKReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			String s = intent.getAction();
			if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
				ToastUtil.showMessage("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
			} else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
				ToastUtil.showMessage("网络出错");
			}
		}
	}

	private SDKReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		mTitleBar = (AppTitleBar) findViewById(R.id.titlebar);
		mTitleBar.setBackOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mSlideMenu.isOpened()) {
					mSlideMenu.closeMenu();
				} else {
					mSlideMenu.openMenu();
				}
			}
		});
		mTitleBar.setMoreIcon(R.drawable.back_icon);
		mTitleBar.setMoreOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isGroup) {
					isGroup = false;
					FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
					trx.hide(fragments[0]);
					if (!fragments[1].isAdded()) {
						trx.add(R.id.fragment_container, fragments[1]);
					}
					trx.show(fragments[1]).commit();
				} else {
					isGroup = true;
					FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
					trx.hide(fragments[1]);
					if (!fragments[0].isAdded()) {
						trx.add(R.id.fragment_container, fragments[0]);
					}
					trx.show(fragments[0]).commit();
				}
			}
		});

		// 注册 SDK 广播监听者
		IntentFilter iFilter = new IntentFilter();
		iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
		iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
		mReceiver = new SDKReceiver();
		registerReceiver(mReceiver, iFilter);

		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDaoOld(this);
		chatHistoryFragment = new ChatHistoryFragment();
		// contactListFragment = new ContactlistFragment();
		groupFragment = new GroupFragment();

		// settingFragment = new SettingsFragment();
		// try {
		// try {
		// EMGroupManager.getInstance().applyJoinToGroup("1407979795996334",AnhaoApplication.getInstance().getUserName());
		// } catch (EaseMobException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// List<EMGroup> dasd = EMGroupManager.getInstance().getAllGroups();
		// } catch (EaseMobException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// fragments = new Fragment[] { chatHistoryFragment, contactListFragment, settingFragment };
		// // 添加显示第一个fragment
		// getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chatHistoryFragment)
		// .add(R.id.fragment_container, contactListFragment).hide(contactListFragment).show(chatHistoryFragment)
		// .commit();
		// EMGroup group = new EMGroup("1407979795996334");
		// fragments = new Fragment[] { groupFragment, contactListFragment, settingFragment };
		fragments = new Fragment[] { groupFragment, chatHistoryFragment };
		// 添加显示第一个fragment
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, groupFragment)
				.add(R.id.fragment_container, chatHistoryFragment).hide(chatHistoryFragment).show(groupFragment).commit();
		mSlideMenu.clearIgnoredViewList();

		
		
		// 注册一个接收消息的BroadcastReceiver
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance()
				.getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance()
				.getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(3);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

		// setContactListener监听联系人的变化等
		EMContactManager.getInstance().setContactListener(new MyContactListener());
		// 注册一个监听连接状态的listener
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// 注册群聊相关的listener
		EMGroupManager.getInstance().addGroupChangeListener(new MyGroupChangeListener());
		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
		EMChat.getInstance().setAppInited();
	}

	/**
	 * 初始化组件
	 */
	private void initView() {
		unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
		unreadAddressLable = (TextView) findViewById(R.id.unread_address_number);
		// mTabs = new Button[3];
		// mTabs[0] = (Button) findViewById(R.id.btn_conversation);
		// mTabs[1] = (Button) findViewById(R.id.btn_address_list);
		// mTabs[2] = (Button) findViewById(R.id.btn_setting);
		// // 把第一个tab设为选中状态
		// mTabs[0].setSelected(true);

		mSlideMenu = new SlideMenu(this);
		mSlideMenu.setBackground(R.drawable.chat_bg);
		mSlideMenu.attachToActivity(this);
		mItemHome = new SlideMenuItem(this, R.drawable.ic_home_home, "首页");
		mItemSet = new SlideMenuItem(this, R.drawable.ic_home_yugao, "设置");
		// mItemOrders = new SlideMenuItem(this, R.drawable.ic_home_mygoods,
		// "首页");
		// mItemMyTianGou = new SlideMenuItem(this, R.drawable.ic_home_mytiangou,
		// "首页");
		// mItemMore = new SlideMenuItem(this, R.drawable.ic_home_more,
		// "首页");
		// if (!ShareCookie.isLoginAuth()) {
		// mItemLogin = new SlideMenuItem(this, R.drawable.ic_home_login,
		// R.string.home_login);
		// } else {
		// mItemLogin = new SlideMenuItem(this, R.drawable.ic_home_login,
		// R.string.home_logout);
		// }

		MenuClickListener listener = new MenuClickListener();
		mItemHome.setOnClickListener(listener);
		mItemSet.setOnClickListener(listener);

		mSlideMenu.addMenuItem(mItemHome);
		mSlideMenu.addMenuItem(mItemSet);

	}

	private class MenuClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v == mItemHome) {
				if (mSlideMenu.isOpened()) {
					mSlideMenu.closeMenu();
				}
			} else if (v == mItemSet) {
				showActivity(SettingsFragment.class);
			}

		}

	}

	// /**
	// * button点击事件
	// *
	// * @param view
	// */
	// public void onTabClicked(View view) {
	// switch (view.getId()) {
	// case R.id.btn_conversation:
	// index = 0;
	// break;
	// case R.id.btn_address_list:
	// index = 1;
	// break;
	// case R.id.btn_setting:
	// index = 2;
	// break;
	// }
	// if (currentTabIndex != index) {
	// FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
	// trx.hide(fragments[currentTabIndex]);
	// if (!fragments[index].isAdded()) {
	// trx.add(R.id.fragment_container, fragments[index]);
	// }
	// trx.show(fragments[index]).commit();
	// }
	// mTabs[currentTabIndex].setSelected(false);
	// // 把当前tab设为选中状态
	// mTabs[index].setSelected(true);
	// currentTabIndex = index;
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者
		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
		}
		try {
			unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
		}

		if (conflictBuilder != null) {
			conflictBuilder.create().dismiss();
			conflictBuilder = null;
		}
		// 取消监听 SDK 广播
		unregisterReceiver(mReceiver);

	}

	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			unreadLabel.setText(String.valueOf(count));
			unreadLabel.setVisibility(View.VISIBLE);
		} else {
			unreadLabel.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 刷新申请与通知消息数
	 */
	public void updateUnreadAddressLable() {
		runOnUiThread(new Runnable() {
			public void run() {
				int count = getUnreadAddressCountTotal();
				if (count > 0) {
					unreadAddressLable.setText(String.valueOf(count));
					unreadAddressLable.setVisibility(View.VISIBLE);
				} else {
					unreadAddressLable.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	/**
	 * 获取未读申请与通知消息
	 * 
	 * @return
	 */
	public int getUnreadAddressCountTotal() {
		int unreadAddressCountTotal = 0;
		if (AnhaoApplication.getInstance().getContactListOld().get(Constant.NEW_FRIENDS_USERNAME) != null) {
			unreadAddressCountTotal = AnhaoApplication.getInstance().getContactListOld()
					.get(Constant.NEW_FRIENDS_USERNAME).getUnreadMsgCount();
		}
		return unreadAddressCountTotal;
	}

	/**
	 * 获取未读消息数
	 * 
	 * @return
	 */
	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			System.out.println("NewMessageBroadcastReceiver:");
			if (ChatType.Chat.name().equals(message.getChatType().name())) {
				Map<String, User> localUsers = AnhaoApplication.getInstance().getHelloContactList();
				Map<String, User> toAddUsers = new HashMap<String, User>();
				if (!localUsers.containsKey(message.getFrom())) {
					HelloUserDao dao = new HelloUserDao(context);
					User user = new User();
					user.setUsername(message.getFrom());
					String headerName = null;
					if (!TextUtils.isEmpty(user.getNick())) {
						headerName = user.getNick();
					} else {
						headerName = user.getUsername();
					}
					if (message.getFrom().equals(Constant.NEW_FRIENDS_USERNAME)) {
						user.setHeader("");
					} else if (Character.isDigit(headerName.charAt(0))) {
						user.setHeader("#");
					} else {
						user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
								.get(0).target.substring(0, 1).toUpperCase());
						char header = user.getHeader().toLowerCase().charAt(0);
						if (header < 'a' || header > 'z') {
							user.setHeader("#");
						}
					}
					// 暂时有个bug，添加好友时可能会回调added方法两次
					if (!localUsers.containsKey(message.getFrom())) {
						dao.saveContact(user);
					}
					try {
						System.out.println("BroadcastReceiver:"
								+ ((TextMessageBody) message.getBody()).getMessage());
					} catch (Exception e) {
						// TODO: handle exception
					}
					toAddUsers.put(message.getFrom(), user);
					localUsers.putAll(toAddUsers);
				}
			}
			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == 0) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (chatHistoryFragment != null) {
					chatHistoryFragment.refresh();
				}
			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance().getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	private InviteMessgeDao inviteMessgeDao;
	private UserDaoOld userDao;

	/***
	 * 联系人变化listener
	 * 
	 */
	private class MyContactListener implements EMContactListener {

		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = AnhaoApplication.getInstance().getContactListOld();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);
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
					user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1))
							.get(0).target.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);
			// 刷新ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();

		}

		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = AnhaoApplication.getInstance().getContactListOld();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}
			// 刷新ui
			if (currentTabIndex == 1)
				// contactListFragment.refresh();
				updateUnreadLabel();

		}

		@Override
		public void onContactInvited(String username, String reason) {
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			notifyNewIviteMessage(msg);

		}

		@Override
		public void onContactRefused(String username) {
			// 参考同意，被邀请实现此功能,demo未实现

		}

	}

	/**
	 * 保存提示新消息
	 * 
	 * @param msg
	 */
	private void notifyNewIviteMessage(InviteMessage msg) {
		saveInviteMsg(msg);
		// 提示有新消息
		EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

		// 刷新bottom bar消息未读数
		updateUnreadAddressLable();
		// 刷新好友页面ui
		// if (currentTabIndex == 1)
		// contactListFragment.refresh();
	}

	/**
	 * 保存邀请等msg
	 * @param msg
	 */
	private void saveInviteMsg(InviteMessage msg) {
		// 保存msg
		inviteMessgeDao.saveMessage(msg);
		// 未读数加1
		User user = AnhaoApplication.getInstance().getContactListOld()
				.get(Constant.NEW_FRIENDS_USERNAME);
		user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
	}

	/**
	 * 连接监听listener
	 * 
	 */
	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			// chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {
			if (errorString != null && errorString.contains("conflict")) {
				// 显示帐号在其他设备登陆dialog
				showConflictDialog();
			} else {
				// chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
				// chatHistoryFragment.errorText.setText("连接不到聊天服务器");
			}
		}

		@Override
		public void onReConnected() {
			// chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onReConnecting() {
		}

		@Override
		public void onConnecting(String progress) {
		}

	}

	/**
	 * MyGroupChangeListener
	 */
	private class MyGroupChangeListener implements GroupChangeListener {

		@Override
		public void onInvitationReceived(String groupId, String groupName, String inviter,
				String reason) {
			// 被邀请
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(inviter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(inviter + "邀请你加入了群聊"));
			// 保存邀请消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						// chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(AnhaoMainActivity.this).equals(
								GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
				}
			});

		}

		@Override
		public void onInvitationAccpted(String groupId, String inviter, String reason) {

		}

		@Override
		public void onInvitationDeclined(String groupId, String invitee, String reason) {

		}

		@Override
		public void onUserRemoved(String groupId, String groupName) {
			// 提示用户被T了，demo省略此步骤
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						updateUnreadLabel();
						if (currentTabIndex == 0)
							// chatHistoryFragment.refresh();
							if (CommonUtils.getTopActivity(AnhaoMainActivity.this).equals(
									GroupsActivity.class.getName())) {
								GroupsActivity.instance.onResume();
							}
					} catch (Exception e) {
						Log.e("###", "refresh exception " + e.getMessage());
					}

				}
			});
		}

		@Override
		public void onGroupDestroy(String groupId, String groupName) {
			// 群被解散
			// 提示用户群被解散,demo省略
			// 刷新ui
			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					if (currentTabIndex == 0)
						// chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(AnhaoMainActivity.this).equals(
								GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
				}
			});

		}

		@Override
		public void onApplicationReceived(String groupId, String groupName, String applyer,
				String reason) {
			// 用户申请加入群聊
			InviteMessage msg = new InviteMessage();
			msg.setFrom(applyer);
			msg.setTime(System.currentTimeMillis());
			msg.setGroupId(groupId);
			msg.setGroupName(groupName);
			msg.setReason(reason);
			Log.d(TAG, applyer + " 申请加入群聊：" + groupName);
			msg.setStatus(InviteMesageStatus.BEAPPLYED);
			notifyNewIviteMessage(msg);
		}

		@Override
		public void onApplicationAccept(String groupId, String groupName, String accepter) {
			// 加群申请被同意
			EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
			msg.setChatType(ChatType.GroupChat);
			msg.setFrom(accepter);
			msg.setTo(groupId);
			msg.setMsgId(UUID.randomUUID().toString());
			msg.addBody(new TextMessageBody(accepter + "同意了你的群聊申请"));
			// 保存同意消息
			EMChatManager.getInstance().saveMessage(msg);
			// 提醒新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			runOnUiThread(new Runnable() {
				public void run() {
					updateUnreadLabel();
					// 刷新ui
					if (currentTabIndex == 0)
						// chatHistoryFragment.refresh();
						if (CommonUtils.getTopActivity(AnhaoMainActivity.this).equals(
								GroupsActivity.class.getName())) {
							GroupsActivity.instance.onResume();
						}
				}
			});
		}

		@Override
		public void onApplicationDeclined(String groupId, String groupName, String decliner,
				String reason) {
			// 加群申请被拒绝，demo未实现
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isConflict) {
			updateUnreadLabel();
			updateUnreadAddressLable();
			EMChatManager.getInstance().activityResumed();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mSlideMenu.isOpened()) {
				mSlideMenu.closeMenu();
				return true;
			} else {
				// moveTaskToBack(false);
				finish();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private android.app.AlertDialog.Builder conflictBuilder;
	private boolean isConflictDialogShow;

	/**
	 * 显示帐号在别处登录dialog
	 */
	private void showConflictDialog() {
		isConflictDialogShow = true;
		AnhaoApplication.getInstance().logout();

		if (!AnhaoMainActivity.this.isFinishing()) {
			// clear up global variables
			try {
				if (conflictBuilder == null)
					conflictBuilder = new android.app.AlertDialog.Builder(AnhaoMainActivity.this);
				conflictBuilder.setTitle("下线通知");
				conflictBuilder.setMessage(R.string.connect_conflict);
				conflictBuilder.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								conflictBuilder = null;
								finish();
								startActivity(new Intent(AnhaoMainActivity.this,
										LoginActivity.class));
							}
						});
				conflictBuilder.setCancelable(false);
				conflictBuilder.create().show();
				isConflict = true;
			} catch (Exception e) {
				Log.e("###", "---------color conflictBuilder error" + e.getMessage());
			}

		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (getIntent().getBooleanExtra("conflict", false) && !isConflictDialogShow)
			showConflictDialog();
	}

}
