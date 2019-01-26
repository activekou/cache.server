package com.chuan.server;

import com.chuan.server.business.SimpleDevCache;

import io.netty.buffer.ByteBuf;

public class Main {
	private static CacheNetty netty = new CacheNetty();
	private static SimpleDevCache cache = new SimpleDevCache();
	public static void main(String[] args) {
		try {
			netty.start();
		} catch (InterruptedException e) {
			System.out.println("netty ≥ı ºªØ ß∞‹");
			e.printStackTrace();
		}
		
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void put(ByteBuf msg){
		cache.put(msg);
	}
}
