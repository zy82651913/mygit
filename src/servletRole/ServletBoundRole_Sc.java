package servletRole;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletBoundRole_Sc
 */
@WebServlet("/ServletBoundRole_Sc")
public class ServletBoundRole_Sc extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBoundRole_Sc() {
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
		String sql = null;
		PreparedStatement ps1 = null;
		PrintWriter out =response.getWriter();
		String menu_role[] = request.getParameterValues("menu_role");//获取数组
		String RoleID=menu_role[0];//获取RoleID
		sql = "DELETE FROM menu_role WHERE RoleID='"+RoleID+"'";//删除所有查到的RoleID
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);//删除数据 
			stmt.close();
			DB.close(conn);
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
