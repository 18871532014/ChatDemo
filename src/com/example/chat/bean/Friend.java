package com.example.chat.bean;

import java.io.Serializable;

import android.R.string;
/*适配显示用户名所用到的bean类*/
public class Friend implements Serializable{
	private String frtiendid;
	private String userid;
	public String getUserid() {
		return userid;
	}
	public String getFrtiendid() {
		return frtiendid;
	}
	public void setFrtiendid(String frtiendid) {
		this.frtiendid = frtiendid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	private int msg_path;
	private String friendName;
	private String lastMsg;
	public int getMsg_path() {
		return msg_path;
	}
	public void setMsg_path(int msg_path) {
		this.msg_path = msg_path;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public String getLastMsg() {
		return lastMsg;
	}
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}
}
