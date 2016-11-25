package com.example.chat.httpInterface;

public interface HttpCallback {
	void onFinish(String response);
    void onError(Exception e);
}
