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

/*	���赱ǰ�û����˺�idΪ001
 * 	���ѵ�idΪ002
 * */
public class MainActivity extends Activity implements android.view.View.OnClickListener{
	/*��ʾ��Ϣ��listView*/
	public static ListView message_list;
	/*���Ͱ�ť*/
	private Button send_btn;
	/*���ذ�ť*/
	private Button back;
	/*�����*/
	private EditText input_text;
	/*�Զ����list����*/
	private  MsgAdapter msgAdapter;
	/*ͼ���ļ���·��*/
	private int msg_path=drawable.img;
	/*�������ݵ�list*/
	public static List<Msg> list = new ArrayList<Msg>();
	/*json����֮�����Ϣ*/
    private	String tulingMessage="";
    
    /*����apiָ������վ*/
    private String head="http://apis.baidu.com/turing/turing/turing?key=879a6cb3afb84dbf4fc84a1df2ab7319&info=";
    private String end="&userid=eb2edb736";
 
    /*����仯����*/
    private NetworkChangeReceiver netchange;
    private IntentFilter filter;   
    /*���͵��������ַ  */
	private String http_send_url;
	/*ת�������¼��ҳ��*/
	private Button to_delete_btn;
	private View view;
	/*���ö�ʱ������ʱ��*/
	private static final int time = 1000;
	private Timer timer = new Timer(true);
	/*����һ�����ս�����Ϣ֮���List*/
	private List<serviceMsg> receiveList= new ArrayList<serviceMsg>();
	/*���嵱ǰ��Ϣ�ĳ���*/
	private int msgLength;
	/*���嵱ǰ�˻�*/
	private User currentUser;
	/*���嵱ǰ����*/
	private Friend currentFriend;
	/*��Ϣ������������*/
	private String from;
	/*��Ϣ������ȥ*/
	private String to;
	/*�ж��Ƿ��ǵ�һ�ν������ҳ��*/
	private int IsFirstStart=Invariant.FIRSTINIT;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //�����Լ�д�õ�������
        msgAdapter=new MsgAdapter(MainActivity.this, R.layout.message_layout, list);	
        
        
        //���ز���
        message_list=(ListView)findViewById(R.id.show_message);
        send_btn=(Button)findViewById(R.id.send_btn);
        send_btn.setOnClickListener(this);
        input_text=(EditText)findViewById(R.id.input_text);
        
        to_delete_btn=(Button)findViewById(R.id.To_delete_layout);
        to_delete_btn.setOnClickListener(this);
        
        back=(Button)findViewById(R.id.back_main);
        back.setOnClickListener(this);
        
        //����ListView��ֵ
        message_list.setAdapter(msgAdapter);
        
        view=(View)findViewById(R.layout.activity_main);
        
        //EditText�Ľ��㴥��ʱ��
        input_text.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean bool) {
				EditText selectEdit=(EditText)v;
				if (!bool) {//ʧȥ����
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
        
        //ע��㲥�¼����жϵ�ǰ�������
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netchange=new NetworkChangeReceiver();
        registerReceiver(netchange, filter);
        
        
        //ˢ�µ�ǰ��listView���Ҷ�λ�����һ��
        msgAdapter.notifyDataSetChanged();
        message_list.setSelection(list.size());
        
        //��ȡ��һ�����󴫹����������
        //��������п��Ի�õ�����ǰ�û���id��ѡ�к��ѵ�id
        //currentFriend=(Friend)getIntent().getSerializableExtra("friendClass");
        
       
        //������ʱ����
        timer.schedule(task, 0,time);
        
        //��ȡ��������洫�������û��ͽ�Ҫ���͵ĺ���
        currentUser=(User)getIntent().getSerializableExtra("userClass");
        currentFriend=(Friend)getIntent().getSerializableExtra("friendClass");
        IsFirstStart=getIntent().getIntExtra("isFirst", Invariant.FIRSTINIT);
        //��ȡ��Ϣ�������������Լ�����ʲô��Ϣ
        from=currentUser.getName();
        to=currentFriend.getFriendName();
    }

    //��ť����¼�
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_btn:	 
			/*������Ϣ��ť*/
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
				
				//�����͵���Ϣʵʱ�ı��浽���ݿ���ȥ
				Login.createDataBases.saveMessage(msg);
								
				//��������з�����Ϣ
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
			
			
			
			
			/*���뵽����ҳ��
			 * 
			 * ���ĸ�ҳ��������ݿ�����
			 * */
		case R.id.To_delete_layout:
			Intent intent = new Intent(MainActivity.this,DeleteMsg.class);
			startActivity(intent);
			break;
			//���ذ�ť
		case R.id.back_main:
			finish();
			break;
		default:
			break;
		}
	}
	//�����¼����õ������緵�ص�ֵ�����ұ��浽list����ȥ
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
				
				//���浽�������ݿ���ȥ
				Login.createDataBases.saveMessage(msg2);
				break;
				//��ʱ����
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
	
	//����һ����ʱ��
	private TimerTask task = new TimerTask() {		
		@Override
		public void run() {
			Message message = new Message();
			message.what=Invariant.TIMING;
			handler.sendMessage(message);
		}
	};
	
	/*
	 * ��������ķ���
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
	/*�����߳������ListView�е�����*/
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
	
	//�������ٴα����ѵ�ʱ��ˢ��listView
	@Override
	protected void onStart() {
		super.onStart();
		//�ӱ������ݿ������е���Ϣ����Ӵ���Ϣ����ȥ
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
