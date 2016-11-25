package com.example.chat.uitl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.R.string;
import android.util.Log;
import android.widget.Toast;

import com.example.chat.MainActivity;
import com.example.chat.httpInterface.HttpCallback;
//�������������Ϣ���лᴫ������ֵ����ǰ���û�id,Ŀ���û���id,���͵���Ϣ
public class sendMessage {
	public  static void  sendHttpRequest(final String information,final HttpCallback listener){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(Url.saveUrl+information);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine())!=null) {
						response.append(line);
					}
					if (response!=null) {
						listener.onFinish(response.toString());
					}
					
				} catch (IOException e) {
					Log.i("err","���Ǳ��㷢����");
					listener.onError(e);
				}
				finally{
					if (connection !=null) {
						connection.disconnect();
					}
				}
			}
    }).start();
}
}
