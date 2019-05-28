package com.jedis.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class PoolTest {
	
	public static void main(String[] args) {
		final JedisPool jedisPool = JedisPoolUtils.getJedisPoolInstance();
		printJedisPoolInfo(jedisPool);
		
		List<Jedis> jedisList = new ArrayList<>();
		try {
			for (int i = 0; i < 200; i++) {
				final int n = i;
				Thread t = new Thread(() -> {
					Jedis jedis = jedisPool.getResource();
					for (int j = 0; j < 100; j++) {
						printJedisPoolInfo(jedisPool);
						jedis.set("Thread_" + n + "_" + j, "V_" + n + "_" + j);
						jedisList.add(jedis);
					}
				});
				t.start();
				t.join();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			printJedisPoolInfo(jedisPool);
		}
		finally {
			for (Jedis jedis : jedisList) {
				if (null != jedis) {
					jedis.close();
					printJedisPoolInfo(jedisPool);
				}
			}
			
		}
	}
	
	private static void printJedisPoolInfo(JedisPool jedisPool) {
		int numberActive = jedisPool.getNumActive();
		int numberIdle = jedisPool.getNumIdle();
		System.out.println("numberActive:" + numberActive + ", numberIdle:" + numberIdle);
	}
}
