package com.example.chat.uitl;
/*�ж�String�Ƿ�Ϊ��*/
public class IsEmpty {
	public static boolean isEmpty(String string){
		if (string==null || string.trim().isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
}
