package com.example.chat;

import java.util.List;

import com.example.chat.bean.Friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FriendAdapter extends ArrayAdapter<Friend>{
	private int resourceid;
	
	
	public FriendAdapter(Context context, int resource, 
			List<Friend> objects) {
		super(context, resource, objects);
		resourceid=resource;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Friend friend= getItem(position);
		View view;
		ViewHoder viewHoder;
		if (convertView==null) {
			view=LayoutInflater.from(getContext()).inflate(resourceid, null);
			viewHoder=new ViewHoder();
			viewHoder.imageView=(ImageView)view.findViewById(R.id.friend_img);
			viewHoder.friend_Name=(TextView)view.findViewById(R.id.friend_name);
			viewHoder.last_Msg=(TextView)view.findViewById(R.id.lastMsg);
			
			viewHoder.imageView.setImageResource(friend.getMsg_path());
			viewHoder.friend_Name.setText(friend.getFriendName());
			viewHoder.last_Msg.setText(friend.getLastMsg());
			view.setTag(viewHoder);
		}
		else {
			view=convertView;
			viewHoder=(ViewHoder)view.getTag();
		}
		return view;
	}
	class ViewHoder{
        ImageView imageView;
        TextView friend_Name;
        TextView last_Msg;
    }

}
