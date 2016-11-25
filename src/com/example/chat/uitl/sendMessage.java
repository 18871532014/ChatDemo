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
//向服务器发送消息其中会传入三个值，当前的用户id,目标用户的id,发送的消息
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
					Log.i("err","还是被你发现了");
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
