package com.wanghui.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hadoop.fs.FileStatus;

import com.wanghui.dao.HdfsDao;
import com.wanghui.dao.UserDao;
import com.wanghui.pojo.User;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
 
	private static final long serialVersionUID = 1L;
 
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 接收表单信息
		String username = request.getParameter("user");
		String password = request.getParameter("password");
		// 设置回显
		request.setAttribute("user", username);
		request.setAttribute("password", password);
		// 根据用户名查询用户
		User user = new UserDao().findUser(username);
		if (user != null) {
			if (user.getPassword().equals(password)) {
 
				HttpSession session = request.getSession();
				session.setAttribute("username", username); 
				FileStatus[] documentList = HdfsDao.showFiles(username);
				request.setAttribute("documentList", documentList);
				request.getRequestDispatcher("index.jsp").forward(request, response); 
			} else {
				// （1）只弹出弹窗
				response.getWriter()
						.write("<script>alert('密码错误！！'); window.location='login.jsp'; window.close();</script>");
				response.getWriter().flush();
			}
		} else {
			response.getWriter()
					.write("<script>alert('用户不存在！！'); window.location='login.jsp'; window.close();</script>");
			response.getWriter().flush();
		}

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
 
}
