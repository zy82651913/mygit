package servletFuHeZhiBiaoGuanLi;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletFuHeZhiBiao_Update
 */
@WebServlet("/ServletFuHeZhiBiao_Update")
public class ServletFuHeZhiBiao_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeZhiBiao_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
		response.setContentType("text/json;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		String sql = null;
		PrintWriter out =response.getWriter();
		PreparedStatement ps = null;
		
		String fhzb_id=request.getParameter("fhzb_id");
		String pxid=request.getParameter("pxid");
		String fhzb_name=request.getParameter("fhzb_name");
		String fhzb_bh=request.getParameter("fhzb_bh");
		String xsws=request.getParameter("xsws");
		
		sql = "UPDATE fhzbgl SET pxid=?,fhzb_name=?,fhzb_bh=?,xsws=? WHERE id=? "; //修改数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, pxid);
			ps.setString(2, fhzb_name);
			ps.setString(3, fhzb_bh);
			ps.setString(4, xsws);
			ps.setString(5, fhzb_id);
			ps.executeUpdate();
			DB.close(conn);
			ps.close();
		} catch (SQLException e) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", e);
			out.print(jsonObject);
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
