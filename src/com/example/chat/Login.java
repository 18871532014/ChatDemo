package com.example.chat;

import android.app.Activity;
import android.app.Notification.Action;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chat.bean.User;
import com.example.chat.db.CreateDataBases;
import com.example.chat.uitl.IsEmpty;

public class Login extends Activity implements OnClickListener{
		private Button login_ben;
		private EditText userName;
		private EditText passWord;
		private Button regist;
		User user = new User();
		 /*创建数据库*/
	    public static CreateDataBases createDataBases; 
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.login_layout);
			createDataBases=CreateDataBases.getInstance(this);
			login_ben=(Button)findViewById(R.id.login);
			userName=(EditText)findViewById(R.id.username);
			passWord=(EditText)findViewById(R.id.password);
			regist=(Button)findViewById(R.id.regist);
			regist.setOnClickListener(this);
			login_ben.setOnClickListener(this);
			View view =findViewById(R.layout.activity_main);
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			//注册功能的实现，跳转到注册页面！
			case R.id.regist:
				Intent intent = new Intent(Login.this,Regsist.class);
				startActivity(intent);
				break;
			case R.id.login:
				String username = userName.getText().toString();
				String password = passWord.getText().toString();
				userName.setText("");
				passWord.setText("");
				if (!IsEmpty.isEmpty(username) && !IsEmpty .isEmpty(password)) {
					if (Login.createDataBases.searchUser(username)!=null) {
						User user=Login.createDataBases.searchUser(username);
						if (user.getName().equals(username) && user.getPassword().equals(password)) {
							Intent intent2 = new Intent(Login.this,ShowList.class);
							intent2.putExtra("userClass", user);
							startActivity(intent2);
						}
						else {
							Toast.makeText(Login.this,"输入正确的用户名密码", Toast.LENGTH_SHORT).show();
						}
					}
					else {
						Toast.makeText(Login.this,"不存在这用户", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(Login.this,"用户名密码不能为空", Toast.LENGTH_SHORT).show();
				}								
			default:
				break;
			}
		}
}
