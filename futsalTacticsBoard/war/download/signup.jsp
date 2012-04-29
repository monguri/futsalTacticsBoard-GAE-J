<%@page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@page import="com.google.appengine.api.users.UserService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<% UserService service = UserServiceFactory.getUserService(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Sign up</title>
	</head>

	<body>
		<div id="header">
			<div id="logo">
				<hl>
					<a href="#">futsalTacticsBoard</a>
				</hl>
			</div>
			<div id="menu">
				<ul>
					<li>
						<a href="<% service.createLogoutURL("/"); %>">Logout</a>
					</li>
				</ul>
			</div>
		</div>
		
		<div id="wrapper">
			<div id="page">
				<div id="content">
					<div class="post">
						<div class="entry">
							<h2>サインアップしてください</h2>
							<form action="/userRegist" method="post">
								ログインID:<input type="text" name="loginId" />
								<input type="submit" value="regist" />${errMsg}
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<div style="clear :both;">&nbsp;</div>
	</body>
</html>