package servletBzgl;

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
 * Servlet implementation class ServletBzUpdate
 */
@WebServlet("/ServletBzUpdate")
public class ServletBzUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBzUpdate() {
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
		String jzid=request.getParameter("jzid");
		String pxid=request.getParameter("pxid");
		String bz_name=request.getParameter("bz_name");

		sql = "UPDATE bzgl SET jzid=?,pxid=?,bz_name=? WHERE id=? "; //修改数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, jzid);
			ps.setString(2, pxid);
			ps.setString(3, bz_name);
			ps.setString(4, id);
			ps.executeUpdate();
			//返回JSON
				JSONObject jsonobj = new JSONObject();//JSON对象
				jsonobj.put("jzid", jzid);
				jsonobj.put("pxid", pxid);
				jsonobj.put("bz_name", bz_name);
				jsonobj.put("id", id);
				out.print(jsonobj);
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
