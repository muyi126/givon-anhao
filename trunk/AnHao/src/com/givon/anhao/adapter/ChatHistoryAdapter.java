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
package com.givon.anhao.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.R;
import com.givon.anhao.activity.ChatActivity;
import com.givon.anhao.utils.SmileUtils;
import com.givon.baseproject.util.StringUtil;
import com.givon.baseproject.util.ToastUtil;

/**
 * 聊天记录adpater
 * 
 */
public class ChatHistoryAdapter extends BaseAdapter {

	// private LayoutInflater inflater;
	private Context context;
	private final static int TYPE_FIRST = 0;
	private final static int TYPE_EMGROUP = 1;
	private final static int TYPE_CHAT = 2;
	private List<EMContact> list;

	// public ChatHistoryAdapter(Context context, int textViewResourceId, List<EMContact> objects) {
	// super(context, textViewResourceId, objects);
	// inflater = LayoutInflater.from(context);
	// this.context = context;
	// }
	public ChatHistoryAdapter(Context context, List<EMContact> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int type = getItemViewType(position);
		EMContact user = getItem(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.row_chat_history, null,
					false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
			// switch (type) {
			// case TYPE_FIRST:
			// convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
			// break;
			// case TYPE_EMGROUP:
			// convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
			// break;
			// case TYPE_CHAT:
			// convertView = inflater.inflate(R.layout.row_chat_history, parent, false);
			// break;
			//
			// default:
			// break;
			// }
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		// if (holder == null) {
		// holder = new ViewHolder();
		// holder.name = (TextView) convertView.findViewById(R.id.name);
		// holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
		// holder.message = (TextView) convertView.findViewById(R.id.message);
		// holder.time = (TextView) convertView.findViewById(R.id.time);
		// holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
		// holder.msgState = convertView.findViewById(R.id.msg_state);
		// holder.list_item_layout = (RelativeLayout) convertView
		// .findViewById(R.id.list_item_layout);
		// convertView.setTag(holder);
		// }
		String username = "";
		if (null != user) {
			username = user.getUsername();
		}
		switch (type) {
		case TYPE_FIRST:
			break;
		case TYPE_EMGROUP:
			// 群聊消息，显示群聊头像
			holder.avatar.setImageResource(R.drawable.group_icon);
			break;
		case TYPE_CHAT:
			holder.avatar.setImageResource(R.drawable.default_avatar);
			break;

		default:
			break;
		}
		if (type != TYPE_FIRST) {
			EMConversation conversation = EMChatManager.getInstance().getConversation(username);
			holder.name.setText(StringUtil.isEmpty(user.getNick()) ? user.getNick() : username);
			if (conversation.getUnreadMsgCount() > 0) {
				// 显示与此用户的消息未读数
				holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
				holder.unreadLabel.setVisibility(View.VISIBLE);
			} else {
				holder.unreadLabel.setVisibility(View.INVISIBLE);
			}
			if (conversation.getMsgCount() != 0) {
				// 把最后一条消息的内容作为item的message内容
				EMMessage lastMessage = conversation.getLastMessage();
				holder.message.setText(
						SmileUtils.getSmiledText(context, getMessageDigest(lastMessage, context)),
						BufferType.SPANNABLE);

				holder.time
						.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
				if (lastMessage.direct == EMMessage.Direct.SEND
						&& lastMessage.status == EMMessage.Status.FAIL) {
					holder.msgState.setVisibility(View.VISIBLE);
				} else {
					holder.msgState.setVisibility(View.GONE);
				}
			}
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EMContact emContact = getItem(position);
				if (getItem(position).getUsername().equals(
						AnhaoApplication.getInstance().getUserName()))
					ToastUtil.showMessage("不能和自己聊天");
				else {
					// 进入聊天页面
					Intent intent = new Intent(context, ChatActivity.class);
					if (emContact instanceof EMGroup) {
						intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
						intent.putExtra("groupId", ((EMGroup) emContact).getGroupId());
					} else {
						intent.putExtra("userId", emContact.getUsername());
					}
					context.startActivity(intent);
				}
			}
		});

		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		return convertView;
	}

	/**
	 * 根据消息内容和消息类型获取消息内容提示
	 * 
	 * @param message
	 * @param context
	 * @return
	 */
	private String getMessageDigest(EMMessage message, Context context) {
		String digest = "";
		switch (message.getType()) {
		case LOCATION: // 位置消息
			if (message.direct == EMMessage.Direct.RECEIVE) {
				// 从sdk中提到了ui中，使用更简单不犯错的获取string方法
				// digest = EasyUtils.getAppResourceString(context, "location_recv");
				digest = getStrng(context, R.string.location_recv);
				digest = String.format(digest, message.getFrom());
				return digest;
			} else {
				// digest = EasyUtils.getAppResourceString(context, "location_prefix");
				digest = getStrng(context, R.string.location_prefix);
			}
			break;
		case IMAGE: // 图片消息
			ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
			digest = getStrng(context, R.string.picture) + imageBody.getFileName();
			break;
		case VOICE:// 语音消息
			digest = getStrng(context, R.string.voice);
			break;
		case VIDEO: // 视频消息
			digest = getStrng(context, R.string.video);
			break;
		case TXT: // 文本消息
			TextMessageBody txtBody = (TextMessageBody) message.getBody();
			digest = txtBody.getMessage();
			break;
		case FILE: // 普通文件消息
			digest = getStrng(context, R.string.file);
			break;
		default:
			System.err.println("error, unknow type");
			return "";
		}

		return digest;
	}

	private static class ViewHolder {

		/** 和谁的聊天记录 */
		TextView name;
		/** 消息未读数 */
		TextView unreadLabel;
		/** 最后一条消息的内容 */
		TextView message;
		/** 最后一条消息的时间 */
		TextView time;
		/** 用户头像 */
		ImageView avatar;
		/** 最后一条消息的发送状态 */
		View msgState;
		/** 整个list中每一行总布局 */
		RelativeLayout list_item_layout;

		ViewHolder(View view) {
			name = (TextView) view.findViewById(R.id.name);
			unreadLabel = (TextView) view.findViewById(R.id.unread_msg_number);
			message = (TextView) view.findViewById(R.id.message);
			time = (TextView) view.findViewById(R.id.time);
			avatar = (ImageView) view.findViewById(R.id.avatar);
			list_item_layout = (RelativeLayout) view.findViewById(R.id.list_item_layout);
			msgState = view.findViewById(R.id.msg_state);
		}

	}

	String getStrng(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return TYPE_FIRST;
		} else {
			if (getItem(position) instanceof EMGroup) {
				return TYPE_EMGROUP;
			} else {
				return TYPE_CHAT;
			}
		}
	}

	@Override
	public int getCount() {
		return list.size() + 1;
	}

	@Override
	public EMContact getItem(int position) {
		// TODO Auto-generated method stub
		return position == 0 ? new EMContact("野招呼") : list.get(position - 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
