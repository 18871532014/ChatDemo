package com.example.chat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.example.chat.R.drawable;
import com.example.chat.bean.Friend;
import com.example.chat.bean.Msg;
import com.example.chat.bean.User;
import com.example.chat.bean.serviceMsg;
import com.example.chat.db.CreateDataBases;
import com.example.chat.httpInterface.HttpCallback;
import com.example.chat.invariant.Invariant;
import com.example.chat.uitl.GetMessage;
import com.example.chat.uitl.HandleMessage;
import com.example.chat.uitl.HttpUtil;
import com.example.chat.uitl.sendMessage;

/*	假设当前用户的账号id为001
 * 	朋友的id为002
 * */
public class MainActivity extends Activity implements android.view.View.OnClickListener{
	/*显示消息的listView*/
	public static ListView message_list;
	/*发送按钮*/
	private Button send_btn;
	/*返回按钮*/
	private Button back;
	/*输入框*/
	private EditText input_text;
	/*自定义的list适配*/
	private  MsgAdapter msgAdapter;
	/*图像文件的路径*/
	private int msg_path=drawable.img;
	/*承载数据的list*/
	public static List<Msg> list = new ArrayList<Msg>();
	/*json解析之后的信息*/
    private	String tulingMessage="";
    
    /*调用api指定的网站*/
    private String head="http://apis.baidu.com/turing/turing/turing?key=879a6cb3afb84dbf4fc84a1df2ab7319&info=";
    private String end="&userid=eb2edb736";
 
    /*网络变化监听*/
    private NetworkChangeReceiver netchange;
    private IntentFilter filter;   
    /*发送的总请求地址  */
	private String http_send_url;
	/*转到清楚记录的页面*/
	private Button to_delete_btn;
	private View view;
	/*设置定时操作的时间*/
	private static final int time = 1000;
	private Timer timer = new Timer(true);
	/*定义一个接收解析消息之后的List*/
	private List<serviceMsg> receiveList= new ArrayList<serviceMsg>();
	/*定义当前消息的长度*/
	private int msgLength;
	/*定义当前账户*/
	private User currentUser;
	/*定义当前好友*/
	private Friend currentFriend;
	/*消息发到从哪里来*/
	private String from;
	/*消息到哪里去*/
	private String to;
	/*判断是否是第一次进到这个页面*/
	private int IsFirstStart=Invariant.FIRSTINIT;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //加载自己写好的适配器
        msgAdapter=new MsgAdapter(MainActivity.this, R.layout.message_layout, list);	
        
        
        //加载布局
        message_list=(ListView)findViewById(R.id.show_message);
        send_btn=(Button)findViewById(R.id.send_btn);
        send_btn.setOnClickListener(this);
        input_text=(EditText)findViewById(R.id.input_text);
        
        to_delete_btn=(Button)findViewById(R.id.To_delete_layout);
        to_delete_btn.setOnClickListener(this);
        
        back=(Button)findViewById(R.id.back_main);
        back.setOnClickListener(this);
        
        //设置ListView的值
        message_list.setAdapter(msgAdapter);
        
        view=(View)findViewById(R.layout.activity_main);
        
        //EditText的焦点触发时间
        input_text.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean bool) {
				EditText selectEdit=(EditText)v;
				if (!bool) {//失去焦点
					selectEdit.setHint(selectEdit.getTag().toString());
				}else {
					String hint=selectEdit.getHint().toString();
					selectEdit.setTag(hint);
					selectEdit.setHint("");
					msgAdapter.notifyDataSetChanged();
			        message_list.setSelection(list.size());
				}
			}
		});
        
        //注册广播事件，判断当前网络情况
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netchange=new NetworkChangeReceiver();
        registerReceiver(netchange, filter);
        
        
        //刷新当前的listView并且定位到最后一行
        msgAdapter.notifyDataSetChanged();
        message_list.setSelection(list.size());
        
        //获取上一个对象传过来的类对象
        //这个对象中可以获得到：当前用户的id，选中好友的id
        //currentFriend=(Friend)getIntent().getSerializableExtra("friendClass");
        
       
        //开启定时操作
        timer.schedule(task, 0,time);
        
        //获取从上面界面传过来的用户和将要发送的好友
        currentUser=(User)getIntent().getSerializableExtra("userClass");
        currentFriend=(Friend)getIntent().getSerializableExtra("friendClass");
        IsFirstStart=getIntent().getIntExtra("isFirst", Invariant.FIRSTINIT);
        //获取消息到从哪里来和自己接收什么消息
        from=currentUser.getName();
        to=currentFriend.getFriendName();
    }

    //按钮点击事件
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:	 
			/*发送消息按钮*/
			String content=input_text.getText().toString();
			if (!"".equals(content) && content.trim().isEmpty()==false) {
				Msg msg = new Msg();
				msg.setContent(content);
				msg.setMsg_path(msg_path);
				msg.setType(Msg.TYPE_SEND);
				list.add(msg);	
				msgAdapter.notifyDataSetChanged();
				message_list.setSelection(list.size());
				input_text.setText("");
				
				//将发送的消息实时的保存到数据库中去
				Login.createDataBases.saveMessage(msg);
								
				//向服务器中发送消息
				sendMessage.sendHttpRequest(content+"&from="+from+"&to="+to,new HttpCallback() {				
					@Override
					public void onFinish(String response) {						
					}
					
					@Override
					public void onError(Exception e) {
						
					}
				});				
			}
			break;
			
			
			
			
			/*进入到设置页面
			 * 
			 * 在哪个页面进行数据库的清除
			 * */
		case R.id.To_delete_layout:
			Intent intent = new Intent(MainActivity.this,DeleteMsg.class);
			startActivity(intent);
			break;
			//返回按钮
		case R.id.back_main:
			finish();
			break;
		default:
			break;
		}
	}
	//处理事件，得到从网络返回的值，并且保存到list表单中去
	 Handler handler = new Handler(){
		public void handleMessage(Message msg) {					
			switch (msg.what) {
			case Invariant.REVEIVE_MESSAGE:
				//Log.d("MainActivity_Handle",(String)msg.obj);
				Msg msg2 = new Msg();
				msg2.setContent((String)msg.obj);
				msg2.setMsg_path(msg_path);
				msg2.setType(Msg.TYPE_REVEIVE);
				list.add(msg2);
				msgAdapter.notifyDataSetChanged();
				message_list.setSelection(list.size());
				
				//保存到本地数据库中去
				Login.createDataBases.saveMessage(msg2);
				break;
				//定时操作
			case Invariant.TIMING:
				GetMessage.sendHttpRequest(from,new HttpCallback() {
					@Override
					public void onFinish(String response) {
						if(receiveList!=null && receiveList.size()>0 ){
								msgLength=receiveList.size();
							}
						else {
						}
						receiveList=HandleMessage.handleMessage(response);
						if (receiveList==null) {
						}						
					}					
					@Override
					public void onError(Exception e) {
						
					}
				});
				if( receiveList!=null){
					if(receiveList.size()>0 && receiveList.size()!=msgLength ){
						if(msgLength!=0){
							Msg msg3 = new Msg();
							msg3.setContent(receiveList.get(msgLength).getContent());
							msg3.setMsg_path(msg_path);
							msg3.setType(Msg.TYPE_REVEIVE);
							list.add(msg3);
							msgAdapter.notifyDataSetChanged();
							message_list.setSelection(list.size());
						}
					}
				}
				break;
			default:
				break;
			}
		}
	};
	
	//定义一个定时器
	private TimerTask task = new TimerTask() {		
		@Override
		public void run() {
			Message message = new Message();
			message.what=Invariant.TIMING;
			handler.sendMessage(message);
		}
	};
	
	/*
	 * 监听网络的方法
	 * */
	class NetworkChangeReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = (ConnectivityManager) 
					getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo ==null || networkInfo.isAvailable()==false) {
				Toast.makeText(context, "No Network", Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(netchange);
		timer.cancel();
	}
	/*开启线程来清楚ListView中的数据*/
	class Refresh implements Runnable {
		 public void run() {
		  while (!Thread.currentThread().isInterrupted()) {
		   try {
			   Thread.sleep(100);
		   }
		    catch (InterruptedException e) {
		    	Thread.currentThread().interrupt();
		   }
		   message_list.postInvalidate();
		  }
		 }
		}
	
	//当这个活动再次被唤醒的时候刷新listView
	@Override
	protected void onStart() {
		super.onStart();
		//从本地数据库获得所有的消息，添加大消息框中去
		msgAdapter.notifyDataSetChanged();
	}
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
    	super.onBackPressed();
    }
}
