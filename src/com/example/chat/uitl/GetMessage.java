package com.example.chat.uitl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.text.TextUtils;
import android.util.Log;

import com.example.chat.httpInterface.HttpCallback;
//向服务器中接受到消息
public class GetMessage {
	public  static void  sendHttpRequest(final String userid,final HttpCallback listener){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(Url.getUrl+userid);
					connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine())!=null) {
						response.append(line);
					}
				if (response!=null && !response.toString().isEmpty()) {
						listener.onFinish(response.toString());
					}
					
				} catch (IOException e) {
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
