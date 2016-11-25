package com.example.chat;

import java.util.List;

import com.example.chat.bean.Msg;
import com.example.chat.db.CreateDataBases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class DeleteMsg extends Activity implements OnClickListener{
	private Button delete_db;
	private Button back;
	private CreateDataBases createDataBases;  
	private ListView listView;
	private List<Msg> list;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.delete_layout);
			delete_db=(Button)findViewById(R.id.delete_db);
			delete_db.setOnClickListener(this);
			back=(Button)findViewById(R.id.back);
			back.setOnClickListener(this);
			createDataBases=Login.createDataBases;	
			listView=MainActivity.message_list;
			list=MainActivity.list;
		}
		@Override
		protected void onDestroy() {
		super.onDestroy();
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.delete_db:
				createDataBases.deleteMsg();
				list.clear();
				Toast.makeText(DeleteMsg.this, "清楚记录成功", Toast.LENGTH_SHORT).show();
				break;
			case R.id.back:
				//返回到上一级
				finish();
				break;
			default:
				break;
			}
		}
		@Override
		public void onBackPressed() {
			super.onBackPressed();
		}
}
