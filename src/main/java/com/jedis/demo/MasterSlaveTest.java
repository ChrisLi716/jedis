package com.jedis.demo;

import redis.clients.jedis.Jedis;

public class MasterSlaveTest {
	public static void main(String[] args) {
		
		Jedis jedis_M = new Jedis("hadoop", 6379);
		Jedis jedis_S = new Jedis("hadoop", 6380);
		
		jedis_S.slaveof("hadoop", 6379);
		
		System.out.println("Master_6379:" + jedis_M.info("replication"));
		System.out.println("Slave_6380:" + jedis_S.info("replication"));
		
		for (int i = 0; i < 5; i++) {
			jedis_M.set("C" + i, "M" + i);
		}
		
		String c1 = jedis_S.get("C1");
		System.out.println("C1=" + c1);
	}
	
}
