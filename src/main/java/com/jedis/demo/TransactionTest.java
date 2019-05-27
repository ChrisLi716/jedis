package com.jedis.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class TransactionTest {
	
	public static void main(String[] args) {
		Jedis jedis = new Jedis("hadoop", 6379);
		
		// testTrans(jedis);
		try {
			boolean bln = watchBalance(jedis);
			if (bln) {
				System.out.println("reduct money success");
			}
			else {
				System.out.println("reduct money faile, will rewatch balance again!");
				boolean secondResult = watchBalance(jedis);
				if (secondResult) {
					System.out.println("reduct maney success!");
				}
			}
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void testTrans(Jedis jedis) {
		Transaction transaction = jedis.multi();
		for (int i = 0; i < 3; i++) {
			transaction.set("t" + i, String.valueOf(i));
		}
		transaction.exec();
	}
	
	private static boolean watchBalance(Jedis jedis)
		throws InterruptedException {
		int balance = 100;
		int debt = 0;
		int actualCost = 10;
		
		jedis.msetnx("balance", String.valueOf(balance), "debt", String.valueOf(debt));
		jedis.watch("balance");
		
		Thread.sleep(5000);
		
		balance = Integer.valueOf(jedis.get("balance"));
		System.out.println("current balance is : " + balance);
		
		if (balance < actualCost) {
			jedis.unwatch();
			System.out.println("balance is insufficient!");
			return false;
		}
		else {
			Transaction trans = jedis.multi();
			trans.decrBy("balance", actualCost);
			trans.incrBy("debt", actualCost);
			trans.exec();
			
			System.out.println("balance :" + jedis.get("balance"));
			System.out.println("debt : " + jedis.get("debt"));
			
			return true;
		}
	}
}
