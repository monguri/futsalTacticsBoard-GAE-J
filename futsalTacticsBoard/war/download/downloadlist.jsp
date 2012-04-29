<%@page import="com.gae.mongry.futsalTacticsBoard.DownloadServlet"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="com.gae.mongry.futsalTacticsBoard.Record"%>
<%@page import="java.util.List"%>
<%@page import="com.gae.mongry.futsalTacticsBoard.ServiceUser"%>
<%@page import="javax.jdo.Query"%>
<%@page import="com.gae.mongry.futsalTacticsBoard.PMF"%>
<%@page import="com.google.appengine.api.users.User"%>
<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@page import="javax.jdo.PersistenceManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%
	//Logger log = Logger.getLogger(DownloadServlet.class.getName());
	//log.warning(">>downloadlist");

	UserService service = UserServiceFactory.getUserService();
	User user = service.getCurrentUser();
	String email = user.getEmail();

	PersistenceManager pm = PMF.get().getPersistenceManager();
	Query query = pm.newQuery(ServiceUser.class);
	ServiceUser serviceUser = null;
	List<Record> records = null;

	try {
		query.setFilter("mailAddress == pEmail");
		query.declareParameters("String pEmail");
		@SuppressWarnings("unchecked")
		List<ServiceUser> users = (List<ServiceUser>)query.execute(email);
		// 該当ユーザがいなければログインID登録画面へ移行
		if (users.size() == 0) {
			//log.warning("user not existed. redirect to signup");
			// フォワードが例外でうまく動かないのでリダイレクトを使う
			// request.getRequestDispatcher("/download/signup.jsp").forward(request, response);
			response.sendRedirect("/download/signup.jsp");
			return;
		}

		serviceUser = users.get(0);

		query = pm.newQuery(Record.class);
		records = (List<Record>)query.execute();
	} catch (Exception e) {
		response.sendRedirect("/download/error.jsp");
	}
	//log.warning("<<downloadlist");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/css/default.css" rel="stylesheet" type="text/css"/>
<title>Record List</title>
</head>
<body>
	<!-- TODO:これは、リストが長大になると意味がないのでそのうち考える -->
	<p>${errMsg}</p>
	<% if (records.size() > 0){ %>
	<table class="sample1">
		<thead>
			<th>Title</th>
			<th>Comment</th>
			<th></th>
		</thead>
		<tbody>
		<% for (Record record : records){ %>
			<!-- もしかしてアンカーじゃなくてactionにしてるからStageWebViewが反応しづらかったのか？ -->
			<form action="/download" method="get">
			<tr>
				<td><%= record.getTitle() %></td>
				<td><%= record.getComment() %></td>
				<td><input type="submit" value="Download"/></td>
				<input type="hidden" name="mode" value="downloadstart"/><br/>
				<input type="hidden" name="recordid" value="<%= record.getId() %>"/>
			</tr>
			</form>
		<% } %>
		</tbody>
	</table>

	<% } else { %>
		レコードが登録されていません
	<% } %>

	<a href="<% service.createLogoutURL("/"); %>">Logout</a>
</body>
</html>