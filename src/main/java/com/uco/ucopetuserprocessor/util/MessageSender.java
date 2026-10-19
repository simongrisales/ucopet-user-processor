package com.uco.ucopetuserprocessor.util;

public interface MessageSender<T> {

	void execute(T message, Long messageId);
}