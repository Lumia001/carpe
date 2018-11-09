package com.kola.zk;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.xml.transform.ErrorListener;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Test2 zookeeper测试
 * @author eagar
 *
 */
public class Test2 implements Watcher{

	//信号量 让zk在连接之前等待，连接成功之后才能往下走
	private static final CountDownLatch countDownLatch = new CountDownLatch(1);
	
	private ZooKeeper zk;
	
	// 集群的连接地址
	private static final String CONNECT_ADDRES = "127.0.0.1:2181";
	
	// 会话超时时间
	private static final int SESSIONTIME = 2000;
	
	public void createConnection(String addres, int sessionTimeOut){
		try {
			zk = new ZooKeeper(CONNECT_ADDRES, sessionTimeOut, this);
			System.out.println("zk...开始启动连接服务器......");
			countDownLatch.await();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createPath(String path,String data){
		
		try {
			exists(path, true);
			this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("节点创建成功，path:" + path + ",data:" + data);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private Stat exists(String path, boolean needWatch) {
		
		try {
			return this.zk.exists(path, needWatch);
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public boolean updateNode(String path, String data) throws Exception{
		
		exists(path, true);
		this.zk.setData(path, data.getBytes(), -1);
		return false;
	}

	@Override
	public void process(WatchedEvent event) {
		// 获取事件状态
		KeeperState keeperState = event.getState();
		EventType eventType = event.getType();
		String path = event.getPath();
		System.out.println("进入到process方法，事件通知Path:" + path);
		if (KeeperState.SyncConnected == keeperState) {
			if (EventType.None == eventType) {
				// 建立连接状态
				System.out.println("zk开始连接。。。。。。。");
				countDownLatch.countDown();
			}else if (EventType.NodeCreated == eventType) {
				System.out.println("事件通知，新增node节点：" + path);
			}else if(EventType.NodeDataChanged == eventType) {
				System.out.println("事件通知，当前node节点：" + path + "被修改.......");
			}else if (EventType.NodeDeleted == eventType) {
				System.out.println("事件通知，当前node节点" + path + "被删除........" );
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		Test2 test2 = new Test2();
		test2.createConnection(CONNECT_ADDRES, SESSIONTIME);
		test2.createPath("/test_node", "sansan");
//		test2.updateNode("/test_node", "zhansansan");
	}

	
	
}
