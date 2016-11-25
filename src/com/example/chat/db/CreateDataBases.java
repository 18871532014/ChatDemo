package com.example.chat.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baidu.android.common.logging.Log;
import com.example.chat.bean.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CreateDataBases {
	public static final String DB_NAME="msg";
	public static final int VERSION=2;
	private static CreateDataBases createDataBases;
	private SQLiteDatabase db;
	private singleFriend SingleFriend = new singleFriend();
	private CreateDataBases(Context context){
		MsgOpenHelper dbhelper=new MsgOpenHelper(context, DB_NAME, null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	public synchronized static CreateDataBases getInstance(Context context){
		if (createDataBases==null) {
			createDataBases= new CreateDataBases(context);
		}
		return createDataBases;
	}
	
	
	public void saveMessage(Msg msg){
		SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		if (msg!=null) {
			ContentValues values = new ContentValues();
			values.put("type", msg.getType());
			values.put("content", msg.getContent());
			values.put("msg_path", msg.getMsg_path());
			values.put("time", format.format(new Date()));
			db.insert("Msg", null, values);
		}
	}
	public List<Msg> searchMessage(){
		List<Msg> list = new ArrayList<Msg>();
		Cursor cursor=db.query("Msg", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Msg msg = new Msg();
				msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
				msg.setMsg_path(cursor.getInt(cursor.getColumnIndex("msg_path")));
				msg.setTime(cursor.getString(cursor.getColumnIndex("time")));
				msg.setType(cursor.getInt(cursor.getColumnIndex("type")));
				list.add(msg);
			} while (cursor.moveToNext());
		}
		if (cursor!=null) {
			cursor.close();
		}
		return list;
	}
	public void deleteMsg(){
		db.delete("Msg", null, null);
	}
	
	
	
	public void saveUser(User user){
		if (user!=null) {
			ContentValues values = new ContentValues();
			values.put("id", user.getId());
			values.put("username", user.getName());
			values.put("password", user.getPassword());
			db.insert("User", null, values);
		}
	}
	public User searchUser(String userName){
		User user = new User();
		Cursor cursor = db.query("User", null, "username=?",new String[]{userName}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				user.setId(cursor.getString(cursor.getColumnIndex("id")));
				user.setName(cursor.getString(cursor.getColumnIndex("username")));
				user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
			} while (cursor.moveToNext());
			return user;
		}
		else  {
			return null;
		}
	}
	//保存一个用户的所有好友信息
	public void saveFriend(singleFriend singleFriend){
		if (singleFriend!=null) {
			for(int i=0;i<singleFriend.getFriends().size();i++){
				ContentValues values = new ContentValues();
				values.put("user", singleFriend.getUserName());
				values.put("friend",singleFriend.getFriends().get(i).getFriendName());
				values.put("img", singleFriend.getFriends().get(i).getMsg_path());
				db.insert("Friend", null, values);
			}
		}
	}
	public singleFriend seachFriendforFriendList(String userName){		
		List<Friend> list = new ArrayList<Friend>();
		String username;
		boolean falg=false;
		Cursor cursor=db.query("Friend", null, "user=?", new String[]{userName}, null, null, null);
		if (cursor.moveToFirst()) {
			falg=true;
			username=cursor.getString(cursor.getColumnIndex("user"));
			do{
				Friend friend = new Friend();
				friend.setFriendName(cursor.getString(cursor.getColumnIndex("friend")));
				friend.setMsg_path(cursor.getInt(cursor.getColumnIndex("img")));
				list.add(friend);
			}
			while(cursor.moveToNext());
			SingleFriend.setUserName(username);
			SingleFriend.setFriends(list);
			return SingleFriend;
			
		}
		 else if(!cursor.moveToFirst()) {
			 falg=false;
			SingleFriend=null;
			return SingleFriend;
		}		
		return SingleFriend;
			
	}
	//是否存在一个好友
	public boolean searchFriendForFriend(String userName,String friendName){
		Cursor cursor=db.query("Friend", null, "user=?&friend=?", new String[]{userName,friendName}, null, null, null);
		if (cursor.moveToFirst()) {
			return true;
		}
		else {
			return false;
		}
	}
}
