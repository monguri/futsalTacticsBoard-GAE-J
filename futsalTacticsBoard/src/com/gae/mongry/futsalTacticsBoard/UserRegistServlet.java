package com.gae.mongry.futsalTacticsBoard;

import java.io.IOException;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UserRegistServlet extends HttpServlet {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO:このファイルはまだまだ全然未完成
		log.warning(">>userRegist");
		UserService service = UserServiceFactory.getUserService();
		User user = service.getCurrentUser();

		String loginId, mailAddress;
		try {
			loginId = req.getParameter("loginId");
			mailAddress = user.getEmail();
		} catch (NumberFormatException e) {
			log.warning("NumberFormatException");
			return;
		}
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			// 入力されたログインID文字列に問題がないか確認
			if (!isValid(loginId)) {
				req.setAttribute("errMsg", Const.ERR_MSG_INVALID_LOGINID);
				req.getRequestDispatcher("download/signup.jsp").forward(req, resp);
				return;
			}
		
			// downloadlist.jspで該当emailが登録されてないのは確認済みだが直たたきを恐れて
			if (isRegistedEmail(pm, mailAddress)) {
				req.setAttribute("errMsg", Const.ERR_MSG_REGISTED_EMAIL);
				req.getRequestDispatcher("download/signup.jsp").forward(req, resp);
				return;
			}

			// 重複するログインIDがないか確認
			if (isRegistedLoginId(pm, loginId)) {
				req.setAttribute("errMsg", Const.ERR_MSG_DUPLICATED_LOGINID);
				req.getRequestDispatcher("download/signup.jsp").forward(req, resp);
				return;
			}
			
			// 登録処理
			ServiceUser newUser = new ServiceUser(loginId, mailAddress);
			pm.makePersistent(newUser);
		} catch (Exception e) {
			resp.sendRedirect("download/downloadlist.jsp");
		} finally {
			log.warning("<<userRegist");
			pm.close();
		}
		
		req.getRequestDispatcher("download/downloadlist.jsp").forward(req, resp);
//		resp.sendRedirect("download/downloadlist.jsp");
	}
	
	private boolean isValid(String loginId) {
		int len = loginId.length();
		if (len == 0 || len > Const.LOGIN_ID_MAX_LEN) {
			return false;
		}
		
		if (!loginId.matches(Const.LOGIN_ID_REGEXP)) {
			return false;
		}

		return true;
	}
	
	private boolean isRegistedEmail(PersistenceManager pm, String email) {
		Query query = pm.newQuery(ServiceUser.class);
		query.setFilter("mailAddress == pEmail");
		query.setResult("count(this)");
		query.declareParameters("String pEmail");
		int emailCount = (Integer) query.execute(email);
		return (emailCount > 0);
	}
	
	private boolean isRegistedLoginId(PersistenceManager pm, String loginId) {
		Query query = pm.newQuery(ServiceUser.class);
		query.setFilter("loginId == pLoginId");
		query.setResult("count(this)");
		query.declareParameters("String pLoginId");
		int idCount = (Integer) query.execute(loginId);
		return (idCount > 0);
	}
}
