package servletFuHeZhiBiaoGuanLi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class Servlet_FuHeZhiBiaoGuanLi_DG
 */
@WebServlet("/Servlet_FuHeZhiBiaoGuanLi_DG")
public class Servlet_FuHeZhiBiaoGuanLi_DG extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_FuHeZhiBiaoGuanLi_DG() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json; charset=utf-8");
    	
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"fhzbgl.id,\n" +
				"fhzbgl.pxid,\n" +
				"fhzbgl.fhzb_name,\n" +
				"fhzbgl.fhzb_bh,\n" +
				"fhzbgl.fhzb_math,\n" +
				"fhzbgl.xsws\n" +
				"FROM\n" +
				"fhzbgl\n" +
				"ORDER BY\n" +
				"fhzbgl.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("fhzb_id", rs.getString("id"));
				jsonobj.put("pxid", rs.getString("pxid"));
				jsonobj.put("fhzb_name", rs.getString("fhzb_name"));
				jsonobj.put("fhzb_bh", rs.getString("fhzb_bh"));
				jsonobj.put("fhzb_math", rs.getString("fhzb_math"));
				jsonobj.put("xsws", rs.getString("xsws"));
				jsonarray.add(jsonobj);
			}
	
			out.println(jsonarray);

			DB.close(conn);
			rs.close();
			stmt.close();
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
