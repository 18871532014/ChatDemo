package com.example.chat;

import java.util.List;

import com.example.chat.bean.Msg;

import android.content.Context;
import android.provider.CalendarContract.Instances;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MsgAdapter extends ArrayAdapter<Msg>{
	private int resurceid;
	public MsgAdapter(Context context, int resource, 
			List<Msg> objects) {
		super(context, resource,objects);
		 resurceid=resource;
	}
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg=getItem(position);
        View view;
        ViewHoder viewHoder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resurceid,null);
            viewHoder=new ViewHoder();
            viewHoder.left_layout=(LinearLayout)view.findViewById(R.id.left_layout);
            viewHoder.right_layout=(LinearLayout)view.findViewById(R.id.right_layout);
            viewHoder.left_text=(TextView)view.findViewById(R.id.left_msg);
            viewHoder.right_text=(TextView)view.findViewById(R.id.right_msg);
            viewHoder.left_img=(ImageView)view.findViewById(R.id.received_img);
            viewHoder.right_img=(ImageView)view.findViewById(R.id.send_img);
            view.setTag(viewHoder);
        }
        else {
			view=convertView;
			viewHoder=(ViewHoder)view.getTag();
		}
        if (msg.getType() == Msg.TYPE_SEND) {
			viewHoder.left_layout.setVisibility(View.GONE);
			viewHoder.right_layout.setVisibility(View.VISIBLE);
			viewHoder.right_text.setText(msg.getContent());
			viewHoder.right_img.setImageResource(msg.getMsg_path());
		}
        else if(msg.getType() == Msg.TYPE_REVEIVE){
        	viewHoder.right_layout.setVisibility(View.GONE);
        	viewHoder.left_layout.setVisibility(View.VISIBLE);
        	viewHoder.left_img.setImageResource(msg.getMsg_path());
        	viewHoder.left_text.setText(msg.getContent());
        }
        return view;
    }
    class ViewHoder{
        LinearLayout left_layout;
        LinearLayout right_layout;
        TextView left_text;
        TextView right_text;
        ImageView left_img;
        ImageView right_img;
    }

}
