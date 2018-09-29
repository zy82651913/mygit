package servletRole;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletRoleXg
 */
@WebServlet("/ServletRoleXg")
public class ServletRoleXg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRoleXg() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
    	Connection conn=null;//定义为空值
		String sql = null;
		PreparedStatement ps = null;
		PrintWriter out =response.getWriter();
		String RoleID=request.getParameter("RoleID");//获取角色ID
		String RoleName=request.getParameter("rolename");//获取角色名
		String RoleNote=request.getParameter("rolenote");//获取角色备注
		/*System.out.println(RoleID);
		System.out.println(RoleName);
		System.out.println(RoleNote);*/
		sql = "UPDATE role SET RoleName=?,RoleNote=? WHERE RoleID=? ";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, RoleName);
			ps.setString(2, RoleNote);
			ps.setString(3, RoleID);
			ps.executeUpdate();//执行更新
			out.print("true");
			DB.close(conn);
			ps.close();
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
