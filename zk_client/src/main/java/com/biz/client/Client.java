package com.biz.client;

import java.security.SecureRandom;
import java.util.List;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;

import com.alibaba.fastjson.JSON;
import com.bit.zookeeper.BitStatus;
import com.bit.zookeeper.Stat;
import com.bit.zookeeper.ZkServer;

/**
 * 客户端，相当于consumer
 *
 */
public class Client implements Watcher {

	private ZkServer zkServer = new ZkServer();
	private ZooKeeper zk;
	private String clientName;

	public Stat invokeServer(List<String> data, int dom, int i) 
		throws Exception {
		if (data.size() == i) {
			throw new Exception("没有可用的服务");
		}
		String node = data.get(dom);
		byte[] bytes = zk.getData(ZkServer.path, true, null);
		String deta = new String(bytes);
		Stat stat = JSON.parseObject(deta, Stat.class);
		if (BitStatus.stop.equals(stat.getStatus())) {
			i++;
			invokeServer(data, dom, i);
		}
		stat.setNode(node);
		return stat;
	}

	public void subscribe(String clientName) throws Exception{
		this.clientName = clientName;
		zk = zkServer.getConnection(this);
//		Thread.sleep(5000);
		if (zk.exists(ZkServer.path, true) == null) {
			throw new Exception("没有可用的服务");
		}
		List<String> data = zk.getChildren(ZkServer.path, null);
		if (data.isEmpty()) {
			throw new Exception("没有可用的服务");
		}
		
		// 随机
		int dom = new SecureRandom().nextInt(data.size());
		Stat stat = invokeServer(data, dom, 0);
		stat.setNum(stat.getNum() + 1);
		stat.setClient(stat.getClient() == null? clientName: stat.getClient());
		stat.setStatus(BitStatus.run);
		zk.setData(ZkServer.path + "/" + stat.getNode(), JSON.toJSONString(stat).getBytes(), 
				-1);
	}

	public void call() throws InterruptedException {
		System.out.println("客户端开启，建立netty连接");
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getType() == EventType.NodeChildrenChanged) {
				System.out.println("服务器发生改变，重新订阅");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public static void main( String[] args ) throws Exception {
        Client client = new Client();
        client.subscribe(args[0]);
        try {
			client.call();
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
