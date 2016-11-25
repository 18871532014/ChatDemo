package com.example.chat;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chat.R.drawable;
import com.example.chat.bean.Friend;
import com.example.chat.bean.User;
import com.example.chat.bean.singleFriend;
import com.example.chat.invariant.Invariant;

public class ShowList extends Activity implements OnClickListener{
	private Button addFriend;
	private ListView friend_listView;
	private FriendAdapter friendAdapter;
	private List<Friend> list_friend = new ArrayList<Friend>();
	private User currentUser;
	private User addFriend_search;
	private singleFriend singleFriend;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.firendlist);

		friend_listView=(ListView)findViewById(R.id.friend_listView);
		//获取登录的用户信息
		currentUser=(User)getIntent().getSerializableExtra("userClass");
				/*此初可以获得当前用户的所有信息，然后根据当前用户的id查找好友列表中的所有好友
				 * 用一个类专门用来存放相关id的所有好友，即friend类的集合
				 * 思路是根据当前出入用户的id得到一个List<Friend>的集合
				 * 写一个相应的数据库用来存放着几个信息！！
				 * 当前就使用addList这个方法假设获得的好友
				 * */
		singleFriend=Login.createDataBases.seachFriendforFriendList(currentUser.getName());
		if (singleFriend!=null) {
			list_friend=singleFriend.getFriends();
			friendAdapter=new FriendAdapter(ShowList.this, R.layout.single_friend,list_friend);
			friend_listView.setAdapter(friendAdapter);
			//列表的点击事件
			friend_listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Friend currentFriend = list_friend.get(position);
					//将当前用户的所有的信息传到下一个活动去！！！
					Intent intent = new Intent(ShowList.this,MainActivity.class);
					intent.putExtra("friendClass", currentFriend);
					intent.putExtra("userClass", currentUser);
					intent.putExtra("isFirst", Invariant.FIRSTINIT);
					startActivity(intent);
				}
			});
		}				
		addFriend=(Button)findViewById(R.id.addFriend);
		addFriend.setOnClickListener(this);				
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addFriend:
			Intent intent = new Intent(ShowList.this,AddFriend.class);
			intent.putExtra("userClass", currentUser);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		singleFriend=Login.createDataBases.seachFriendforFriendList(currentUser.getName());
		if (singleFriend!=null) {
			list_friend=singleFriend.getFriends();
			friendAdapter=new FriendAdapter(ShowList.this, R.layout.single_friend,list_friend);
			friend_listView.setAdapter(friendAdapter);
		}
	}
}
