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
		//��ȡ��¼���û���Ϣ
		currentUser=(User)getIntent().getSerializableExtra("userClass");
				/*�˳����Ի�õ�ǰ�û���������Ϣ��Ȼ����ݵ�ǰ�û���id���Һ����б��е����к���
				 * ��һ����ר������������id�����к��ѣ���friend��ļ���
				 * ˼·�Ǹ��ݵ�ǰ�����û���id�õ�һ��List<Friend>�ļ���
				 * дһ����Ӧ�����ݿ���������ż�����Ϣ����
				 * ��ǰ��ʹ��addList������������õĺ���
				 * */
		singleFriend=Login.createDataBases.seachFriendforFriendList(currentUser.getName());
		if (singleFriend!=null) {
			list_friend=singleFriend.getFriends();
			friendAdapter=new FriendAdapter(ShowList.this, R.layout.single_friend,list_friend);
			friend_listView.setAdapter(friendAdapter);
			//�б�ĵ���¼�
			friend_listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Friend currentFriend = list_friend.get(position);
					//����ǰ�û������е���Ϣ������һ���ȥ������
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
