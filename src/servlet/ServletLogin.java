package servlet;
import hys.DB;
import java.io.IOException;  
import java.io.PrintWriter;  
import java.sql.Connection;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.PreparedStatement;  
import javax.naming.Context;  
import javax.naming.InitialContext;  
import javax.naming.NamingException;  
import javax.servlet.ServletException;  
import javax.servlet.annotation.WebServlet;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.ObjectUtils.Null;



/**
 * Servlet implementation class ServletLogin
 */
@WebServlet("/ServletLogin")
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletLogin() {
        super();
        // TODO Auto-generated constructor stub
    }
    @SuppressWarnings("unused")
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// 设置响应内容类型
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		Connection conn=null;//定义为空值
	    ResultSet rs = null;
		String sql = null;
		Context ctx = null;
		DataSource ds = null;
		PreparedStatement ps = null;
		//PrintWriter out =response.getWriter();
		String username=request.getParameter("username");
		if (username==null) {
			request.getRequestDispatcher("login.html").forward(request, response);
		}
	
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		Context ctx = null;
		DataSource ds = null;
		PreparedStatement ps = null;
		//PrintWriter out =response.getWriter();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if (username.equals("")) {
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
		sql = "SELECT * FROM user WHERE UserName=? AND PassWord=? "; //SQL语句
		try {
			ctx = new InitialContext();//初始化查找空间名
			//ds = (DataSource)ctx.lookup("java:comp/env/jdbc/hys");
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps= conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			rs=ps.executeQuery();
			if (rs.next()){
				request.setAttribute("name", username);
				request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
			}else{
				request.getRequestDispatcher("login.html").forward(request, response);
			}
			DB.close(conn);
			rs.close();
			ps.close();
			} catch (SQLException e) {
			e.printStackTrace();
		} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			
		}

	}
		
}
