
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import utility.HttpUtility;

@SuppressWarnings("all")

@WebServlet(urlPatterns = { "/login", "/get", "/logout" })

public class Myserv extends HttpServlet {
	static final String SERIAL = "serial";
	String base_url = "http://localhost:8082";
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if (req.getServletPath().equals("/login")) {
			this.doLogin(req, res);
		}
		if (req.getServletPath().equals("/get")) {
			this.get(req, res);
		}
		if (req.getServletPath().equals("/logout")) {
			this.logout(req, res);
		}
	}

	private void logout(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		Enumeration<String> h = req.getHeaderNames();

		while (h.hasMoreElements()) {
			String string = (String) h.nextElement();
			headers.put(string, req.getHeader(string));
		}
		String response = HttpUtility.doGet(base_url + "/logout", null, headers);
		System.out.println(response);
	}

	private void get(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HashMap<String, String> headers = new HashMap<String, String>();
		HttpSession session = req.getSession(false);
		

		Enumeration<String> h = req.getHeaderNames();
		while (h.hasMoreElements()) {
			String string = (String) h.nextElement();
			headers.put(string, req.getHeader(string));
		}
		String response = HttpUtility.doGet(base_url + "/get", null, headers);
		System.out.println(response);
		res.getWriter().write(response);
	}

	protected void doLogin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson gson = new Gson();
		HashMap<String, String> headers = new HashMap<String, String>();
		Enumeration<String> h = request.getHeaderNames();
		while (h.hasMoreElements()) {
			String string = (String) h.nextElement();
			headers.put(string, request.getHeader(string));
		}
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("msg", request.getParameter("msg"));
		String res = HttpUtility.sendPOST(base_url + "/login", gson.toJson(params), headers);
		System.out.println(res);
	}

}
