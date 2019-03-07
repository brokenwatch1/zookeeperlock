package com.chenlei.test;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ConnectionTest {
	
	
	public static void main(String[] args) throws Exception {
		
		ZooKeeper zookeeper = new ZooKeeper("localhost", 3000, new TestWatcher());
		String node = "/node2";
		Stat stat = zookeeper.exists(node, false);
		if(stat == null){
			String result = zookeeper.create(node, "hello".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println(result);
			byte[] b = zookeeper.getData(node, false, stat);
			System.out.println(new String(b));
			zookeeper.close();
		}
	}
	

}
class TestWatcher implements Watcher{
	
	@Override
	public void process(WatchedEvent arg0) {
		System.out.println("path:"+arg0.getPath());
		System.out.println("type:"+arg0.getType());
		System.out.println("state:"+arg0.getState());
		
	}
}


