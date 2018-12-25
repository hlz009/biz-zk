<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
    <head>
        <title>服务列表</title>
        <meta name="content-type" content="text/html; charset=utf-8">
    </head>
<body>
	<table border="1px solid">
	   <tr>
	    <th>服务名</th>
	    <th>ip</th>
        <th>端口</th>
        <th>连接数</th>
        <th>负载均衡（随机）</th>
        <th>节点</th>
        <th>客户端</th>
        <th>状态（wait-无消费者;run-运行中;stop-禁止）</th>
        <th>操作</th>
	  </tr>
	<c:forEach items="${list}" var="server">
	  <tr>
	    <td>${server.name}</td>
	    <td>${server.ip}</td>
        <td>${server.port}</td>
        <td>${server.num}</td>
        <td>${server.node}</td>
        <td>---</td>
        <td>${server.client}</td>
        <td>${server.status}</td>
        <td>---</td>
	  </tr>
	</c:forEach>

	</table>
</body>
</html>