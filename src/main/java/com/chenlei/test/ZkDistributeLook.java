package com.chenlei.test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public class ZkDistributeLook implements Lock {
	private ZkClient client;
	private String lockPath;
	private String currentPath;
	private String beforePath;
	
	public ZkDistributeLook(String lockPath){
		super();
		this.client = new ZkClient("localhost:2181");
		this.lockPath = lockPath;
		this.client.setZkSerializer(new MyZkSerializer());
		if(!this.client.exists(lockPath)){
			try{
				client.createPersistent(lockPath);
			}catch(Exception e){
				
			}
		}
	}

	@Override
	public void lock() {
		// TODO Auto-generated method stub
		if(!tryLock()){
			//未获得锁,阻塞自己
			waitForLock();
			lock();
		}
	}

	private void waitForLock() {
		// TODO Auto-generated method stub
		final CountDownLatch cdl = new CountDownLatch(1);
		IZkDataListener listener = new IZkDataListener() {
			
			@Override
			public void handleDataDeleted(String arg0) throws Exception {
				System.out.println("删除，释放锁");
				cdl.countDown();
			}
			
			@Override
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				
			}
		};
		//注册watcher
		this.client.subscribeDataChanges(beforePath, listener);
		//阻塞自己
		if(this.client.exists(beforePath)){
			try {
				cdl.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//取消注册
		this.client.unsubscribeDataChanges(beforePath, listener);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean tryLock() {
		//当前节点不存在创建临时顺序节点
		if(currentPath == null){
			currentPath = this.client.createEphemeralSequential(lockPath+"/", "aaa");
		}
		List<String> children = this.client.getChildren(lockPath);
		//排序
		Collections.sort(children);
		System.out.println(children.toString());
		if(currentPath.equals(lockPath+"/"+children.get(0))){
			return true;
		}else{
			System.out.println("##"+currentPath.substring(lockPath.length()+1));
			int currIndex = children.indexOf(currentPath.substring(lockPath.length()+1));
			beforePath = lockPath+"/"+children.get(currIndex - 1);
		}
		return false;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		client.delete(currentPath);
	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

}

