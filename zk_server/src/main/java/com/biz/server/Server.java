package com.biz.server;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import com.alibaba.fastjson.JSON;
import com.bit.zookeeper.BitStatus;
import com.bit.zookeeper.Stat;
import com.bit.zookeeper.ZkServer;

/**
 * Zk服务端  当作 provider
 */
public class Server {

	private ZkServer zkServer = new ZkServer();
	private ZooKeeper zk;

	private boolean init =  false;
	void register(String serverName) throws IOException, KeeperException, InterruptedException {
		zk = zkServer.getConnection(new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				if (event.getState() == KeeperState.SyncConnected) {
					// 连接成功
					init = true;
					System.out.println("连接成功...");
				}
			}
		});
		System.out.println("获取连接...");
		/**连接成功，才能进行节点相关操作，这里简化处理*/
		while(true) {
			if (!init) {
				System.out.println("wating......");
				continue;
			}
			zk.create(ZkServer.path + "/server", serverName.getBytes(), 
					ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			System.out.println("创建成功" + serverName);
			break;
		}

	}

	public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
		Server server = new Server();
		Stat stat = new Stat();
		int i = 0;
		stat.setIp(args[i++]);
		stat.setName(args[i++]);
		stat.setPort(args[i++]);
		stat.setNum(0);// 注册服务开始时，连接数为0;
		stat.setStatus(BitStatus.wait);// 注册服务开始时，wait状态;
		stat.setNode(args[i++]);
		server.register(JSON.toJSONString(stat));
	}
}
