/* 
 * Copyright 2014 ShangDao.Ltd  All rights reserved.
 * SiChuan ShangDao.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @GroupFragment.java  2014年8月20日 上午10:28:09 - Guzhu
 * @author Guzhu
 * @email:muyi126@163.com
 * @version 1.0
 */

package com.givon.anhao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.support.httpclient.HttpCallBack;
import com.android.support.httpclient.HttpClientAsync;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.givon.anhao.AnhaoApplication;
import com.givon.anhao.BaseListFragment;
import com.givon.anhao.R;
import com.givon.anhao.db.YeUserDao;
import com.givon.anhao.utils.HttpUtil;
import com.givon.baseproject.entity.ErrorCode;
import com.givon.baseproject.entity.RoomBean;
import com.givon.baseproject.entity.RoomEntity;
import com.givon.baseproject.entity.UserBean;
import com.givon.baseproject.util.DbHelper;
import com.givon.baseproject.util.StringUtil;
import com.givon.baseproject.util.ToastUtil;
import com.givon.baseproject.view.PullListView.PullListViewListener;

public class GroupFragment extends BaseListFragment<RoomBean> implements PullListViewListener {

	private final static int TYPE_RIGHT = 0;
	private final static int TYPE_LEFT = 1;
	private DbHelper mDbHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DbHelper(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		mListView.setPullListener(this);
		mListView.startOnRefresh();
		mTitleBar.setVisibility(View.GONE);
		mListView.setmSlideMenu(((AnhaoMainActivity) getActivity()).mSlideMenu);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		final RoomBean bean = mAdapter.getItem(position);
		if (null == convertView) {
			int type = getViewType(position);
			switch (type) {
			case TYPE_RIGHT:
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.group_list_adapter_right, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
				break;
			case TYPE_LEFT:
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.group_list_adapter_left, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
				break;

			default:
				break;
			}
		}
		if (null != bean) {
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.tv_Name_cn.setText(bean.getRoomName());
			viewHolder.tv_Name_en.setText(bean.getDescription());
			viewHolder.tv_Num.setText((position + 1) + "");
			bitmapUtils.display(viewHolder.iv_Background, bean.getCover());
		}
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 进入聊天页面
				if (null != bean) {
					new Thread(new Runnable() {

						public void run() {
							// 从服务器获取详情
							try {
								final EMGroup group = EMGroupManager.getInstance()
										.getGroupFromServer(bean.getGroupId());
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										// 获取详情成功，并且自己不在群中，才让加入群聊
										if (!group.getMembers().contains(
												EMChatManager.getInstance().getCurrentUser())) {
											// 如果是membersOnly的群，需要申请加入，不能直接join
											try {
												if (group.isMembersOnly()) {
													EMGroupManager.getInstance().applyJoinToGroup(
															bean.getGroupId(), "求加入");

												} else {
													EMGroupManager.getInstance().joinGroup(
															bean.getGroupId());
												}
												startActChat(bean);
											} catch (EaseMobException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										} else {
											startActChat(bean);
										}
									}
								});
							} catch (final EaseMobException e) {
								e.printStackTrace();
								getActivity().runOnUiThread(new Runnable() {
									public void run() {
										ToastUtil.showMessage("获取群聊信息失败: " + e.getMessage());
									}
								});
							}

						}
					}).start();
					// EMGroupInfo info = new EMGroupInfo(entity.getRoomId(), entity.getRoomName());

				}
			}
		});
		return convertView;
	}

	@Override
	public void onPullRefresh() {
		loadData();
	}

	private void startActChat(RoomBean bean) {
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
		intent.putExtra("groupId", bean.getGroupId());
//		YeUserDao dao = new YeUserDao(getActivity());
//		UserBean userBean = dao.getAUserBean(bean.getGroupId());
//		intent.putExtra("data", userBean);
		startActivity(intent);

	}

	@Override
	public void onPullLoadMore() {

	}

	private void loadData() {

		HttpClientAsync httpClientAsync = HttpClientAsync.getInstance();
		httpClientAsync.get(HttpUtil.getUrl(HttpUtil.RECOMMEND), new HttpCallBack() {

			@Override
			public void onHttpSuccess(Object obj) {
				dismissWaitingDialog();
				final RoomEntity entity = (RoomEntity) obj;
				if (null != entity && null != entity.getResult() && entity.getResult().size() > 0) {
					// Dao<RoomBean, Integer> dao = mDbHelper.getRoomDao();
					// final YeUserDao dao = new YeUserDao(getActivity());
					// new Thread(new Runnable() {
					//
					// @Override
					// public void run() {
					// for (int i = 0; i < entity.getResult().size(); i++) {
					// RoomBean bean = entity.getResult().get(i);
					// entity.getResult().get(i).setEmId(bean.getGroupId());
					// // List<RoomBean> list = dao.queryForEq("groupId", bean.getGroupId());
					// if(dao.hasEmId(bean.getGroupId())){
					//
					// }else {
					// //这个userbean的type还不确定
					// UserBean userBean = new UserBean();
					// userBean.setEasemobId(bean.getGroupId());
					// bean.setType(ChatActivity.CHATTYPE_GROUP);
					// userBean.setNick(bean.getRoomName());
					// userBean.setNickname(bean.getRoomName());
					// userBean.setUsername(bean.getGroupId());
					// try {
					// EMGroupManager.getInstance().applyJoinToGroup(bean.getGroupId(),AnhaoApplication.getInstance().getUserName());
					// } catch (EaseMobException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
					// dao.saveContact(userBean);
					// }
					// }
					// }
					// }).start();

					// EMGroupManager.getInstance().applyJoinToGroup("1407979795996334",AnhaoApplication.getInstance().getUserName());
					mAdapter.putData(entity.getResult());
				}
			}

			@Override
			public void onHttpStarted() {
				showWaitingDialog("稍等一下。。。");
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
		}, RoomEntity.class);
		// ArrayList<RoomEntity> list = new ArrayList<RoomEntity>();
		// for (int i = 0; i < 4; i++) {
		// RoomEntity entity = new RoomEntity();
		// entity.setRoomName("不作死就不会死");
		// entity.setWsHost("NO DO NO DIE");
		// entity.setRoomId("1407979795996334");
		// entity.setOnlineCount("NO.1");
		// entity.setCover("http://e.hiphotos.baidu.com/image/pic/item/cc11728b4710b9128462b9fec1fdfc039245220a.jpg");
		// list.add(entity);
		// }
		// mAdapter.putData(list);
		mListView.onLoadFinish();
		mListView.onRefreshFinish();

	}

	@Override
	protected int getViewType(int position) {
		return position % 2;
	}

	private class ViewHolder {
		private TextView tv_Name_cn;
		private TextView tv_Name_en;
		private TextView tv_Num;
		private ImageView iv_Background;

		public ViewHolder(View view) {
			tv_Name_cn = (TextView) view.findViewById(R.id.name_cn);
			tv_Name_en = (TextView) view.findViewById(R.id.name_en);
			tv_Num = (TextView) view.findViewById(R.id.room_num);
			iv_Background = (ImageView) view.findViewById(R.id.room_bg);
			tv_Name_cn.setTypeface(AnhaoApplication.getTypeface());
			tv_Name_en.setTypeface(AnhaoApplication.getTypeface());
			tv_Num.setTypeface(AnhaoApplication.getTypeface());
		}
	}

	@Override
	protected int getTypeCount() {
		return 2;
	}

}
