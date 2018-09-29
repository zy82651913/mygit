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
 * Servlet implementation class ServletBoundRole_Cr
 */
@WebServlet("/ServletBoundRole_Cr")
public class ServletBoundRole_Cr extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBoundRole_Cr() {
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
		String sql1 = null;
		PreparedStatement ps1 = null;
		String menu_role[] = request.getParameterValues("menu_role");//获取数组
		String RoleID=menu_role[0];//获取RoleID
		//System.out.println(RoleID);
		sql1 = "INSERT INTO menu_role (RoleID, NodeID) VALUES (?,?)"; //插入数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			conn.setAutoCommit(false);
			ps1 = conn.prepareStatement(sql1);
			for (int i = 1; i < menu_role.length; i++) {//批量插入数据
				ps1.setString(1,RoleID);
				ps1.setString(2, menu_role[i]);
				ps1.addBatch();
			}
			ps1.executeBatch();
			conn.commit();
			DB.close(conn);
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
