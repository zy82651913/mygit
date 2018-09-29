package servletFuHeZhiBiaoGuanLi;

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
 * Servlet implementation class Servlet_FHZB_LaiYuan_Update
 */
@WebServlet("/Servlet_FHZB_LaiYuan_Update")
public class Servlet_FHZB_LaiYuan_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_FHZB_LaiYuan_Update() {
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
				
				String id=request.getParameter("id");
				String fhzb_id=request.getParameter("fhzb_id");
				String xmid=request.getParameter("xmid");
				String xm_name=request.getParameter("xm_name");
				String sc_name=request.getParameter("sc_name");
				String sc_bh=request.getParameter("sc_bh");
				String laiyuan_name=request.getParameter("laiyuan_name");
				String zhbc=request.getParameter("zhbc");
				int n = 0;
				
				sql = "UPDATE fhzb_laiyuan SET xmid=?,xm_name=?,sc_name=?,sc_bh=?,laiyuan_name=?,zhbc=? WHERE id=? "; //修改数据
				try {
					conn=DB.getConection();//利用封装好的类名来调用连接方法便可
					ps = conn.prepareStatement(sql);
					ps.setString(1, xmid);
					ps.setString(2, xm_name);
					ps.setString(3, sc_name);
					ps.setString(4, sc_bh);
					ps.setString(5, laiyuan_name + zhbc);
					ps.setString(6, zhbc);
					ps.setString(7, id);
					ps.executeUpdate();
					
					if (n==0) {
						sql = "INSERT INTO fhzb_laiyuan (fhzb_id, xmid, xm_name, sc_name, sc_bh, laiyuan_name, zhbc) VALUES (?,?,?,?,?,?,?)"; //插入数据
						ps = conn.prepareStatement(sql);
						ps.setString(1, fhzb_id);
						ps.setString(2, xmid);
						ps.setString(3, xm_name);
						ps.setString(4, sc_name);
						ps.setString(5, sc_bh);
						ps.setString(6, laiyuan_name + zhbc);
						ps.setString(7, zhbc);
						ps.executeUpdate();
					}
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
