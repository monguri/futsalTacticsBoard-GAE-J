package com.gae.mongry.futsalTacticsBoard;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {
	private Logger log = Logger.getLogger(this.getClass().getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//
		// downloadlist.jsp -> 選択 -> downloadstart&recordid=xx 
		// -> クライアントがURL変更イベントを受けてハンドラでdownload&recordid=xxにリクエスト -> ダウンロード
		//
		String mode = req.getParameter("mode");
		if (mode.equals("downloadlist")) {
			// ダウンロード選択画面
			log.warning(">>downloadlist");
			try {
				req.getRequestDispatcher("/download/downloadlist.jsp").forward(req, resp);
			} catch (Exception e) {
				log.warning("<<downloadlist");
				resp.sendRedirect("/download/error.jsp");
			}
			log.warning("<<downloadlist");
		} else if (mode.equals("downloadstart")) {
			// ダウンロード中画面
			log.warning(">>downloadstart");
//			resp.sendRedirect("/download/downloading.html");
			try {
				req.getRequestDispatcher("/download/downloading.html").forward(req, resp);
			} catch (Exception e) {
				log.warning("<<downloadlist");
				resp.sendRedirect("/download/error.jsp");
			}
			log.warning("<<downloadstart");
		} else if (mode.equals("download")) {
			// ダウンロード
			log.warning(">>download");
			// RecordをクエリでとってきてJSONにして返す処理
			long recordId = -1;
			if (req.getParameter("recordid") != null && req.getParameter("recordid").length() > 0) {
				recordId = Long.parseLong(req.getParameter("recordid"));
			}
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(Record.class);
				query.setFilter("id==pRecordId");
				query.declareParameters("long pRecordId");

				@SuppressWarnings("unchecked")
				List<Record> results = (List<Record>) query.execute(recordId);
				Record record = results.get(0);
				String jsonStr = record.getData().getValue();
				resp.setContentType("application/json; charset=UTF-8");
				resp.getWriter().println(jsonStr);
			} catch (Exception e) {
				log.warning("<<download");
				req.setAttribute("errMsg", Const.ERR_MSG_DOWNLOAD);
				resp.sendRedirect("/download/downloadlist.jsp");
			} finally {
				pm.close();
			}
			log.warning("<<download");
		} else if (mode.equals("deleteRecord")) {
			log.warning(">>deleteRecord");
			// 管理者以外は実行できない 
			UserService service = UserServiceFactory.getUserService();
			if (!service.isUserAdmin()) {
				resp.sendRedirect("/download/error.jsp");
			}

			long recordId = -1;
			if (req.getParameter("recordid") != null && req.getParameter("recordid").length() > 0) {
				recordId = Long.parseLong(req.getParameter("recordid"));
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(Record.class);
				query.setFilter("id==pRecordId");
				query.declareParameters("long pRecordId");

				@SuppressWarnings("unchecked")
				List<Record> results = (List<Record>) query.execute(recordId);
				pm.deletePersistentAll(results);
			} catch (Exception e) {
				log.warning("<<deleteRecord");
				resp.sendRedirect("/download/error.jsp");
			} finally {
				pm.close();
			}
			log.warning("<<deleteRecord");
		} else if (mode.equals("deleteAllRecord")) {
			log.warning(">>deleteAllRecord");
			// 管理者以外は実行できない 
			UserService service = UserServiceFactory.getUserService();
			if (!service.isUserAdmin()) {
				resp.sendRedirect("/download/error.jsp");
			}

			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(Record.class);
				@SuppressWarnings("unchecked")
				List<Record> results = (List<Record>)query.execute();
				pm.deletePersistentAll(results);
			} catch (Exception e) {
				log.warning("<<deleteAllRecord");
				resp.sendRedirect("/download/error.jsp");
			} finally {
				pm.close();
			}
			log.warning("<<deleteAllRecord");
		} else if (mode.equals("deleteUser")) {
			log.warning(">>deleteUser");
			// 管理者以外は実行できない 
			UserService service = UserServiceFactory.getUserService();
			if (!service.isUserAdmin()) {
				resp.sendRedirect("/download/error.jsp");
			}

			long userId = -1;
			if (req.getParameter("id") != null && req.getParameter("id").length() > 0) {
				userId = Long.parseLong(req.getParameter("id"));
			}
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(ServiceUser.class);
				query.setFilter("id==pUserId");
				query.declareParameters("long pUserId");

				@SuppressWarnings("unchecked")
				List<Record> results = (List<Record>) query.execute(userId);
				pm.deletePersistentAll(results);
			} catch (Exception e) {
				log.warning("<<deleteUser");
				resp.sendRedirect("/download/error.jsp");
			} finally {
				pm.close();
			}
			log.warning("<<deleteUser");
		}
	}
//	
//	@Override
//	public void doPost(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		String mode = req.getParameter("mode");
//		if (!mode.equals("upload")) {
//			return;
//		}
//		
//		// TODO:uploadも認証画面用意しないとね まあアカウントが登録済みでなければregist.jsp開くといったところかな
//		log.warning(">>upload");
//		String title = req.getParameter("title");
//		String comment = req.getParameter("comment");
//		Text dataText = new Text(req.getParameter("data")); // String型だとPMFに入るのは５００文字以内なので制限のないTextを使う
//		// TODO:後で取得したloginIdを入れるようにする まあServiceUserからとるようにすればいいだろ
//		String loginId = "monguri";
//		Date now = new Date();
//		Record record = new Record(loginId, now, title, comment, dataText);
//		
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			pm.makePersistent(record);
//		} finally {
//			pm.close();
//		}
//		resp.setContentType("application/json; charset=UTF-8");
//		String success = "{\"result\" : \"success\"}";
//		resp.getWriter().println(success); // 成功をjson形式で返す
//		log.warning("<<upload");
//	}
}
