package com.example.chat.bean;

import java.util.List;

public class singleFriend {
	private String userName;
	private List<Friend> friends;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<Friend> getFriends() {
		return friends;
	}
	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
