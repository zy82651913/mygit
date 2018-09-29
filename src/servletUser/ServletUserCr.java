package servletUser;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import hys.DB;

/**
 * Servlet implementation class ServletUserCr
 */
@WebServlet("/ServletUserCr")
public class ServletUserCr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUserCr() {
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
	    ResultSet rs = null;
	    Statement stmt = null;
		String sql = null;
		String sql1 = null;
		PreparedStatement ps1 = null;
		PrintWriter out =response.getWriter();
		String name=request.getParameter("name");//获取用户姓名
		String username=request.getParameter("username");//获取用户名
		String password=request.getParameter("pwd");//获取用户密码
		/*System.out.println(username);
		System.out.println(name);
		System.out.println(password);*/
		sql = "SELECT * FROM user WHERE UserName='"+username+"'"; //查询username
		sql1 = "INSERT INTO user (UserName, PassWord, Name) VALUES (?,?,?)"; //插入数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			rs=stmt.executeQuery(sql);
			if (rs.next()){ 
				out.print(true);
			}else{
				rs.close();
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1, username);
				ps1.setString(2, password);
				ps1.setString(3, name);
				ps1.executeUpdate();
				out.print(false);
			}
			DB.close(conn);
			rs.close();
			ps1.close();
			} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
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
