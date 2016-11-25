package com.example.chat.uitl;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.chat.httpInterface.HttpCallback;

/**
 * Created by Admain on 2016/5/9.
 */
public class HttpUtil {
    public static  void  sendHttpRequest(final String address,final HttpCallback listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
//                    connection.setRequestProperty("apikey", "4d7830b670c7253fddee6fb69a630554");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    if (listener!=null){
                        listener.onFinish(response.toString());
                    }
                }
                catch (Exception e){
                    if (listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
