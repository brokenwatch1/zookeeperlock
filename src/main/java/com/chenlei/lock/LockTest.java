package com.chenlei.lock;

import org.I0Itec.zkclient.ZkClient;

public class LockTest {
    public static void main(String[] args) throws Exception {
        ZkClient zkClient = new ZkClient("localhost:2181", 1000);
        SimpleDistributedLock simple = new SimpleDistributedLock(zkClient, "/locker");
        
        for (int i = 0; i < 10; i++) {
            try {
                simple.acquire();
                System.out.println("正在进行运算操作：" + System.currentTimeMillis());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                simple.release();
                System.out.println("=================\r\n");
            }
        }
    }
}