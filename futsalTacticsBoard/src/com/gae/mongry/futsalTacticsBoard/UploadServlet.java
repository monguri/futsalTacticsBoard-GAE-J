package com.gae.mongry.futsalTacticsBoard;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// uploadはログインしてなかったりユーザ登録してなかったりの場合はダウンロードからやらせるようにする
		log.warning(">>upload");
		resp.setContentType("application/json; charset=UTF-8");
		String retJson;

		UserService service = UserServiceFactory.getUserService();
		User user = service.getCurrentUser();
		if (user == null) {
			retJson = "{\"result\" : \"" + Const.RESULT_CD_NOT_LOGIN_ERROR + "\"}";
			resp.getWriter().println(retJson);
			return;
		}
		
		String email = user.getEmail();
		String title = req.getParameter("title");
		String comment = req.getParameter("comment");
		Text dataText = new Text(req.getParameter("data")); // String型だとPMFに入るのは５００文字以内なので制限のないTextを使う
		Date now = new Date();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(ServiceUser.class);

		try {
			query.setFilter("email == pEmail");
			@SuppressWarnings("unchecked")
			List<ServiceUser> users = (List<ServiceUser>)query.execute(email);
			if (users.size() == 0) {
				retJson = "{\"result\" : \"" + Const.RESULT_CD_NOT_REGIST_ERROR + "\"}";
				resp.getWriter().println(retJson);
				return;
			}
			
			String loginId = users.get(0).getLoginId();
			Record record = new Record(loginId, now, title, comment, dataText);

			pm.makePersistent(record);
		} catch (Exception e) {
			retJson = "{\"result\" : \"" + Const.RESULT_CD_SYSTEM_ERROR + "\"}";
			resp.getWriter().println(retJson);
			log.warning("<<upload");
		} finally {
			pm.close();
		}
		
		retJson = "{\"result\" : \"" + Const.RESULT_CD_SUCCESS + "\"}";
		resp.getWriter().println(retJson);
		log.warning("<<upload");
	}
}
