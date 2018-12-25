package com.bit.zookeeper;

import java.io.Serializable;

public class Stat implements Serializable{

	private static final long serialVersionUID = -7948452867747137931L;

	private String ip;
	private String name;
	private String port;
	/**连接数*/
	private int num;
	private String status;
	private String node;
	/**客户端状态*/
	private String client;

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
