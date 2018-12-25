package controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.bit.zookeeper.BitStatus;
import com.bit.zookeeper.Stat;
import com.bit.zookeeper.ZkServer;

@Controller
public class manage {
	private ZkServer zkServer = new ZkServer();
	private ZooKeeper zk;

	@RequestMapping(value = "/list", method=RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) throws Exception {

		zk = zkServer.getConnection(new Watcher() {

			@Override
			public void process(WatchedEvent event) {

			}
		});
		Thread.sleep(5000);// 等待时间，等待连接成功后，再访问
		List<String> data = zk.getChildren(ZkServer.path, null);
		if (data.isEmpty()) {
			throw new Exception("没有可用的服务");
		}
		List<Stat> result = new ArrayList<>();
		for (int i = 0, len = data.size(); i < len; i++) {
			Stat stat = invokeServer(data, i, 0);
			result.add(stat);
		}
		model.addAttribute("list", result);
		return "list";
	}

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
}
