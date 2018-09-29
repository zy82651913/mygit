package servletUser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import hys.DB;

/**
 * Servlet implementation class ServletUserSc
 */
@WebServlet("/ServletUserSc")
public class ServletUserSc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUserSc() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	//response.setContentType("text/html;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
	    Statement stmt = null;
	    int n;
		String sql = null;
		String sql1 = null;
		PrintWriter out =response.getWriter();
		String UserID=request.getParameter("UserID");//获取选定表格行的UserID
		//System.out.println(UserID);
		sql = "DELETE FROM user WHERE UserID='"+UserID+"'";
		sql1 = "DELETE FROM user_role WHERE UserID='"+UserID+"'";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			n = stmt.executeUpdate(sql);
			DB.close(conn);
			stmt.close();
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			n = stmt.executeUpdate(sql1);
			if (n>0) {
				out.print(true);
			}else {
				out.print(false);
			}
			DB.close(conn);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
