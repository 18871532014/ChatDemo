package com.example.chat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.example.chat.R.drawable;
import com.example.chat.bean.Friend;
import com.example.chat.bean.User;
import com.example.chat.bean.singleFriend;
import com.example.chat.invariant.Invariant;
import com.example.chat.uitl.IsEmpty;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriend extends Activity implements OnClickListener{
	private Button searchFriend;
	private EditText inputFriend;
	private Button showFriend;
	private User user;
	private Friend friend;
	private User currentUser;
	public singleFriend SingleFriend;
	private List<Friend> list=new ArrayList<Friend>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addfriend_layout);
		searchFriend=(Button)findViewById(R.id.addFriend_btn);
		inputFriend=(EditText)findViewById(R.id.addFriend_Edit);
		showFriend=(Button)findViewById(R.id.confirm);
		showFriend.setVisibility(View.GONE);
		searchFriend.setOnClickListener(this);
		showFriend.setOnClickListener(this);
		currentUser=(User)getIntent().getSerializableExtra("userClass");
		friend = new Friend();
		SingleFriend = new singleFriend();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addFriend_btn:
			String friendName = inputFriend.getText().toString();
			if (!IsEmpty.isEmpty(friendName)) {
				if (Login.createDataBases.searchUser(friendName)!=null) {
					user=Login.createDataBases.searchUser(friendName);
					showFriend.setVisibility(View.VISIBLE);
					showFriend.setText(user.getName());
					
				}
				else {
					Toast.makeText(AddFriend.this, "找不到该好友", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.confirm:
				//添加弹框
				AlertDialog.Builder dialog = new AlertDialog.Builder(AddFriend.this);
				dialog.setTitle("提示");
				dialog.setMessage("是否添加好友");
				dialog.setCancelable(true);
				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//判断是否存在该好友
						friend.setFriendName(user.getName());
						friend.setLastMsg("haha");
						friend.setMsg_path(drawable.img);
						list.add(friend);
						SingleFriend.setUserName(currentUser.getName());
						SingleFriend.setFriends(list);
						Login.createDataBases.saveFriend(SingleFriend);	
						finish();
					}
				});
				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(AddFriend.this,"取消添加该好友" , Toast.LENGTH_SHORT).show();
					}
				});			
				dialog.show();
			break;
		default:
			break;
		}
	}
}
