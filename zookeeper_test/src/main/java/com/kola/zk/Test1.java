package com.kola.zk;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 * zookeeper测试
 * @author eagar
 *
 */
public class Test1 {
	
	//连接地址
	private static final String ADDRES = "127.0.0.1:2181";
	//session会话
	private static final int SESSION_OUTTIME = 2000;
	
	// 信号量，阻塞程序执行，用户等待zookeeper连接成功，发送信号
	private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);
	
	public static void main(String[] args) throws Throwable {
		ZooKeeper zKeeper = new ZooKeeper(ADDRES, SESSION_OUTTIME, new Watcher() {

			@Override
			public void process(WatchedEvent event) {
				// 获得事件状态
				KeeperState keeperState = event.getState();
				// 获取事件的类型
				EventType eventTpye = event.getType();
				if (keeperState.SyncConnected == keeperState) {
					if (EventType.None == eventTpye) {
						
						System.out.println("zk ... 启动连接。。。");
						COUNT_DOWN_LATCH.countDown();
					}
				}
				
			}
			
		});
		
		//进行阻塞
		COUNT_DOWN_LATCH.await();
		
		String result = zKeeper.create("/test_Lasting12", "Lasting".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(result);
		zKeeper.close();
	}
	
}
