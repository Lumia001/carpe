package com.kola.zk;

import java.util.concurrent.CountDownLatch;

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
 * zookeeper的事件测试
 * @author eagar
 *
 */
public class ZkClientWatcher implements Watcher{

	// 信号量,让zk在连接之前等待,连接成功后才能往下走.
		private static final CountDownLatch countDownLatch = new CountDownLatch(1);
		private ZooKeeper zk;
		// 集群连接地址
		private static final String CONNECT_ADDRES = "127.0.0.1:2181";
		// 会话超时时间
		private static final int SESSIONTIME = 2000;

		private static String LOG_MAIN = "[main]";
		
		public void createConnection(String addres, int sessionTimeOut) {
			try {
				zk = new ZooKeeper(CONNECT_ADDRES, sessionTimeOut, this);
				System.out.println("zk 开始启动连接服务器....");
				countDownLatch.await();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public boolean createPath(String path, String data) throws KeeperException, InterruptedException {
			try {
				exists(path, true);
				this.zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				System.out.println("节点创建成功, Path:" + path + ",data:" + data);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;

		}

		/**
		 * 判断节点是否存在
		 * @param path --节点路径
		 * @param needWatch
		 * @return
		 */
		public Stat exists(String path, boolean needWatch) {
			try {
				return this.zk.exists(path, needWatch);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		public boolean updateNode(String path, String data) throws KeeperException, InterruptedException {
			exists(path, true);
			this.zk.setData(path, data.getBytes(), -1);
			return false;
		}
		public void process(WatchedEvent event) {
			// 获取事件状态
			KeeperState keeperState = event.getState();
			EventType eventType = event.getType();
			
			// zk的路径
			String path = event.getPath();
			System.out.println("进入到 process() keeperState:" + keeperState + ", eventType:" + eventType + ", path:" + path);
			if (KeeperState.SyncConnected == keeperState) {
				if (EventType.None == eventType) {
					// 判断是否建立连接
					System.out.println(LOG_MAIN + "zk建立连接成功...");
					countDownLatch.countDown();
				} else if (EventType.NodeCreated == eventType) {
					System.out.println(LOG_MAIN + "事件通知,新增note节点:" + path);
				} else if (EventType.NodeDataChanged == eventType) {
					System.out.println(LOG_MAIN + "事件通知,当前node节点" + path + "被修改....");
				} else if (EventType.NodeDeleted == eventType) {
					System.out.println(LOG_MAIN + "事件通知,当前node节点" + path + "被删除....");
				}

			}
			System.out.println("===================================================");
		}

		public static void main(String[] args) throws KeeperException, InterruptedException {
			ZkClientWatcher zkClientWatcher = new ZkClientWatcher();
			zkClientWatcher.createConnection(CONNECT_ADDRES, SESSIONTIME);
			boolean createResult = zkClientWatcher.createPath("/p15", "pa-1515");
			System.out.println(createResult+"#####");
//			zkClientWatcher.updateNode("//p15", "123456");
		}

	
	
	
}
