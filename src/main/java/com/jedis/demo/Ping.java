package com.jedis.demo;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class Ping {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("hadoop", 6379);
		String ping = jedis.ping();
		System.out.println(ping);
		
		System.out.println(jedis.dbSize());
		
		Set<String> keys = jedis.keys("*");
		System.out.println(keys.toString());
		
		jedis.select(0);
		jedis.flushDB();

		for (int i = 0; i < 10; i++) {
			jedis.set("k" + i, String.valueOf(i));
		}
		
	}
	
	private void connect(Jedis jedis) {
		jedis.auth("password");
		jedis.connect();// 连接
		jedis.disconnect();// 断开连接
	}
}
