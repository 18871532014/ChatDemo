package com.example.chat;

import java.util.UUID;

import com.baidu.android.common.logging.Log;
import com.example.chat.bean.User;
import com.example.chat.invariant.Invariant;
import com.example.chat.uitl.IsEmpty;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Regsist extends Activity implements OnClickListener{
	private Button regist;
	private Button back;
	private	TextView userName;
	private TextView passWord;
	private User user = new User();
	
	
	
	private Button test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.regist_layout);
		regist=(Button)findViewById(R.id.regist);
		userName=(TextView)findViewById(R.id.username_regist);
		passWord=(TextView)findViewById(R.id.password_regist);
		back=(Button)findViewById(R.id.back_regist);
		regist.setOnClickListener(this);
		back.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.regist:
			String username = userName.getText().toString();
			String password = passWord.getText().toString();
			if (!IsEmpty.isEmpty(username) && !IsEmpty .isEmpty(password) && 
					username.length()>Invariant.LENGTH && password.length()>Invariant.LENGTH){
				if (Login.createDataBases.searchUser(username)==null) 
					{
						user.setName(username);
						user.setPassword(password);
						String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
						user.setId(uuid);
						Login.createDataBases.saveUser(user);
						Toast.makeText(Regsist.this, "注册成功", Toast.LENGTH_SHORT).show();
					}	
				else {
					Toast.makeText(Regsist.this, "已经存在这个用户", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(Regsist.this, "正确输入用户名密码", Toast.LENGTH_SHORT).show();
			}
			userName.setText("");
			passWord.setText("");
			break;
			case R.id.back_regist:
				finish();
		default:
			break;
		}
	}

}
