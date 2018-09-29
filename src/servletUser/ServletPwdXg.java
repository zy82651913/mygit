package servletUser;

import java.awt.TexturePaint;
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
 * Servlet implementation class ServletPwdXg
 */
@WebServlet("/ServletPwdXg")
public class ServletPwdXg extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletPwdXg() {
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
    	ResultSet rs = null;
		String sql = null;
		String sql1 = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PrintWriter out =response.getWriter();
		String UserID=request.getParameter("UserID");//获取用户ID
		String passwordy=request.getParameter("pwdy_passxg");//获取用户原始密码
		String passwordx=request.getParameter("pwde_passxg");//获取用户新密码
		//System.out.println(UserID);
		sql = "SELECT * FROM user WHERE UserID=? AND PassWord=? ";
		sql1 = "UPDATE user SET PassWord=? WHERE UserID=? ";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, UserID);
			ps.setString(2, passwordy);
			rs=ps.executeQuery();
			if (rs.next()){ 
				rs.close();
				ps1=conn.prepareStatement(sql1);
				ps1.setString(1,passwordx);
				ps1.setString(2, UserID);
				ps1.executeUpdate();
				out.print("true");
			}else{
				out.print("false");
			}
			DB.close(conn);
			rs.close();
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
