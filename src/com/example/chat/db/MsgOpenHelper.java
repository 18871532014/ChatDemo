package com.example.chat.db;
/*
 * 创建保存消息记录的数据库
 * */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgOpenHelper extends SQLiteOpenHelper{
	private static final String CREATE_MSG="create table Msg("+
				"content text,"+
				"type integer,"+
				"time text,"+
				"msg_path integer)";
	private static final String CREATE_USER="create table User("+
			"username text ," +
			"password text," +
			"id text primary key)";
	private static final String CREATE_FRIEND="create table Friend(" +
			"user text," +
			"friend text," +
			"img integer)";
	public MsgOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_MSG);
		db.execSQL(CREATE_USER);
		db.execSQL(CREATE_FRIEND);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
