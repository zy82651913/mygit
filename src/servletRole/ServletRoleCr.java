package servletRole;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletRoleCr
 */
@WebServlet("/ServletRoleCr")
public class ServletRoleCr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRoleCr() {
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
		String rolename=request.getParameter("rolename");//获取角色名
		String rolenote=request.getParameter("rolenote");//获取角色备注
		/*System.out.println(rolename);
		System.out.println(rolenote);*/
		sql = "SELECT * FROM role WHERE RoleName='"+rolename+"'"; //查询rolename
		sql1 = "INSERT INTO role (RoleName, RoleNote) VALUES (?,?)"; //插入数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			rs=stmt.executeQuery(sql);
			if (rs.next()){ 
				out.print(true);
			}else{
				rs.close();
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1, rolename);
				ps1.setString(2, rolenote);
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
