package com.example.chat.uitl;
/*ÅĞ¶ÏStringÊÇ·ñÎª¿Õ*/
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
