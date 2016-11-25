package com.example.chat.uitl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.example.chat.bean.*;

public class HandleMessage {
	
	/*
	 * 通过从浏览器获取得到的String
	 * Json解析
	 * 将需要的文本保存到Msg中去
	 * */
		 public static String handleJson(String response) {
		        String msg = new String();
		        if (TextUtils.isEmpty(response) != true) {
		            try {
		                    JSONObject jsonObject = new JSONObject(response);
		                    msg=jsonObject.getString("text");
		                    //Log.d("HandleJson",msg);
		            } 
		            catch (JSONException e) {
		                throw new RuntimeException(e);
		            }
		        }
		        return msg;
		    }
		 
		 /*
		  * 解析从服务器中接受到的消息
		  * */
		 public static List<serviceMsg> handleMessage(String response){
			 List<serviceMsg> list = new ArrayList<serviceMsg>();
			 if (response !=null && !response.contains("Error")) {
				String[] messagelist = response.split("&");
				if (messagelist!=null && messagelist.length>0) {
					for (int i = 0; i < messagelist.length; i++) {
						String message=messagelist[i];
						String[] contentMssage = message.split("\\|");
						if (contentMssage!=null && contentMssage.length>0) {							
								serviceMsg serviceMsg = new serviceMsg();
								serviceMsg.setContent(contentMssage[0]);
								serviceMsg.setFrom(contentMssage[1]);
								serviceMsg.setTo(contentMssage[2]);
								list.add(serviceMsg);
						}
					}
				}
				return list;
			}
			 else {
				return null;
			}
			 
		 }
}
