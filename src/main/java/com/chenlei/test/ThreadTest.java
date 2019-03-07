package com.chenlei.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		final CyclicBarrier cb = new CyclicBarrier(10);
		for(int i = 0; i < 10; i++){
			new Thread(new Runnable() {
				OrderSelect os = new OrderSelect();
				
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName()+"准备好！");
					try {
						cb.await();
					} catch (Exception e) {
						e.printStackTrace();
					}
					os.queryOrder();
				}
				
			}).start();
		}
	}
	
	
}

class OrderSelect{
	private Lock lock =new ZkDistributeLook("/test");
	//private Lock lock =new DistributedLock("localhost:2181","/lock");
	
	public void queryOrder(){
		try{
			lock.lock();
			System.out.println(Thread.currentThread().getName()+"查询到数据:"+System.currentTimeMillis());
			Thread.sleep(2000);
		}catch( Exception e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
}
