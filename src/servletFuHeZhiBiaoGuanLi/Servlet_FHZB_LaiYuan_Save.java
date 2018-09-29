package servletFuHeZhiBiaoGuanLi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class Servlet_FHZB_LaiYuan_Save
 */
@WebServlet("/Servlet_FHZB_LaiYuan_Save")
public class Servlet_FHZB_LaiYuan_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_FHZB_LaiYuan_Save() {
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
		PreparedStatement ps = null;
		ResultSet rs = null;
		PrintWriter out =response.getWriter();
		
		String fhzb_id=request.getParameter("fhzb_id");
		String xmid=request.getParameter("xmid");
		String xm_name=request.getParameter("xm_name");
		String sc_name=request.getParameter("sc_name");
		String sc_bh=request.getParameter("sc_bh");
		String laiyuan_name=request.getParameter("laiyuan_name");
		String zhbc=request.getParameter("zhbc");
		
		try {
			conn=DB.getConection();
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
			
			out.print(fhzb_id);
			DB.close(conn);
			ps.close();
			
			//out.print(fhzb_id);
			
		} catch (Exception e) {
			System.out.println(e);
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
