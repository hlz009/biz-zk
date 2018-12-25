package com.bit.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

/**
 *zookeeper启动注册类
 */
public class ZkServer {

	private ZooKeeper zookeeper;
	private static final String host = "127.0.0.1:2181";
	public static final String path = "/xiaozhi";
	
	public ZooKeeper getConnection(Watcher watcher) throws IOException {
		zookeeper = new ZooKeeper(host, 50, watcher);
		return zookeeper;
	}

    public static void main( String[] args ) {
        
    }
}
