package servletBanBao;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletBanBao_GL_Update
 */
@WebServlet("/ServletBanBao_GL_Update")
public class ServletBanBao_GL_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBanBao_GL_Update() {
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
		
		String id=request.getParameter("id");
		String pxid=request.getParameter("pxid");
		String banbao_name=request.getParameter("banbao_name");
		String banbao_bh=request.getParameter("banbao_bh");
		String sfky=request.getParameter("sfky");
		if (sfky.equals("可用")) {
			sfky = "1";
		}else if(sfky.equals("不可用")){
			sfky = "0";
		}
		
		sql = "UPDATE banbao_gl SET pxid=?,banbao_name=?,banbao_bh=?,sfky=? WHERE banbao_id=? "; //修改数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(pxid));
			ps.setString(2, banbao_name);
			ps.setString(3, banbao_bh);
			ps.setInt(4, Integer.parseInt(sfky));
			ps.setString(5, id);
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
