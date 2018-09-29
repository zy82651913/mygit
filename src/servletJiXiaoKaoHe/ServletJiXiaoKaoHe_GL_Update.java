package servletJiXiaoKaoHe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletJiXiaoKaoHe_GL_Update
 */
@WebServlet("/ServletJiXiaoKaoHe_GL_Update")
public class ServletJiXiaoKaoHe_GL_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_GL_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	response.setContentType("json/html;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		String sql = null;
		PreparedStatement ps = null;
		PrintWriter out =response.getWriter();
		
		String jxkhgl_id=request.getParameter("jxkhgl_id");
		String pxid=request.getParameter("pxid");
		String cj=request.getParameter("cj");
		String khxm=request.getParameter("khxm_name");
		String sfdz=request.getParameter("sfdz");
		
		sql = "UPDATE jxkh_gl SET pxid=?,cj=?,khxm=?,sfdz=? WHERE jxkhgl_id=? "; //修改数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(pxid));
			ps.setString(2, cj);
			ps.setString(3, khxm);
			ps.setString(4, sfdz);
			ps.setString(5, jxkhgl_id);
			n = ps.executeUpdate();
			//返回JSON
			if (n>0) {
				out.print(true);
			}
			DB.close(conn);
			ps.close();
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
