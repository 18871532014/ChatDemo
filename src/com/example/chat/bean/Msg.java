package com.example.chat.bean;

public class Msg {
	public static final int TYPE_REVEIVE=0;
	public static final int TYPE_SEND=1;
	private int type;
	private String content;
	private int msg_path;
	private String time;
	//private int currentId;
	//private int toId;
//	public int getCurrentId() {
//		return currentId;
//	}
//
//	public void setCurrentId(int currentId) {
//		this.currentId = currentId;
//	}
//
//	public int getToId() {
//		return toId;
//	}
//
//	public void setToId(int toId) {
//		this.toId = toId;
//	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}

	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getMsg_path() {
		return msg_path;
	}
	public void setMsg_path(int msg_path) {
		this.msg_path = msg_path;
	}
	
}
