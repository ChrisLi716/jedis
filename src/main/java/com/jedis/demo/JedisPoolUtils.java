package com.jedis.demo;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class JedisPoolUtils {
	
	private JedisPoolUtils() {
	}
	
	private static volatile JedisPool jedisPool;
	
	public static JedisPool getJedisPoolInstance() {
		if (null == jedisPool) {
			synchronized (JedisSentinelPool.class) {
				if (null == jedisPool) {
					JedisPoolConfig poolConfig = new JedisPoolConfig();
					poolConfig.setMaxTotal(1000);
					poolConfig.setMaxIdle(20);
					// 在borrow 一个jedish实例时是否检查连通性
					poolConfig.setTestOnBorrow(true);
					// 当borrow一个jedis实例时最大的等待时长，当超时时抛出JedishConnectionExcetion
					poolConfig.setMaxWaitMillis(100 * 1000);
					
					jedisPool = new JedisPool(poolConfig, "hadoop", 6379);
				}
			}
		}
		return jedisPool;
	}
	
}
