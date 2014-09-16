/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @ActYeContactList.java  2014年9月9日 上午10:11:50 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.ListActivity;
import com.givon.anhao.adapter.YeChatHistoryAdapter;
import com.givon.anhao.domain.User;
import com.givon.baseproject.entity.UserBean;

public class ActYeContactList extends ListActivity<UserBean>{
	private YeChatHistoryAdapter adapter;
	private List<EMContact> beanList;
	private Map<String, User> contactListOld;
	private Map<String, User> contactHelloList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contactListOld = AnhaoApplication.getInstance().getContactListOld();
		contactHelloList = AnhaoApplication.getInstance().getHelloContactList();
		initViewData();
	}
	

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	
	
	private void initViewData(){
		beanList =  loadUsersWithRecentChat();
		
//		List<EMContact> list = new ArrayList<EMContact>();
//		for(User bean : beanList.values()){
//			list.add(new EMContact(bean.getUsername()));
//		}
		adapter = new YeChatHistoryAdapter(this, beanList);
		mListView.setAdapter(adapter);
	}
	
	
	/**
	 * 获取有聊天记录的users和groups
	 * 
	 * @param context
	 * @return
	 */
	private List<EMContact> loadUsersWithRecentChat() {
		List<EMContact> resultList = new ArrayList<EMContact>();
		for (User user : contactListOld.values()) {
			EMConversation conversation = EMChatManager.getInstance().getConversation(
					user.getUsername());
			if (conversation.getMsgCount() > 0) {
				resultList.add(user);
			}
		}
//		for (EMGroup group : EMGroupManager.getInstance().getAllGroups()) {
//			EMConversation conversation = EMChatManager.getInstance().getConversation(
//					group.getGroupId());
//			if (conversation.getMsgCount() > 0) {
//			resultList.add(group);
//			}
//
//		}
		// 排序
		sortUserByLastChatTime(resultList);
		return resultList;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortUserByLastChatTime(List<EMContact> contactList) {
		Collections.sort(contactList, new Comparator<EMContact>() {
			@Override
			public int compare(final EMContact user1, final EMContact user2) {
				EMConversation conversation1 = EMChatManager.getInstance().getConversation(
						user1.getUsername());
				EMConversation conversation2 = EMChatManager.getInstance().getConversation(
						user2.getUsername());

				EMMessage user2LastMessage = conversation2.getLastMessage();
				EMMessage user1LastMessage = conversation1.getLastMessage();
				if (user2LastMessage.getMsgTime() == user1LastMessage.getMsgTime()) {
					return 0;
				} else if (user2LastMessage.getMsgTime() > user1LastMessage.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}
}
