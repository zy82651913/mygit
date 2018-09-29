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
 * Servlet implementation class Servlet_FHZB_LaiYuan
 */
@WebServlet("/Servlet_FHZB_LaiYuan_DG")
public class Servlet_FHZB_LaiYuan_DG extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_FHZB_LaiYuan_DG() {
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
		String fhzb_id = request.getParameter("fhzb_id");
		sql = "SELECT\n" +
				"fhzb_laiyuan.id,\n" +
				"fhzb_laiyuan.fhzb_id,\n" +
				"fhzb_laiyuan.xmid,\n" +
				"fhzb_laiyuan.xm_name,\n" +
				"fhzb_laiyuan.sc_name,\n" +
				"fhzb_laiyuan.sc_bh,\n" +
				"fhzb_laiyuan.laiyuan_name,\n" +
				"fhzb_laiyuan.zhbc\n" +
				"FROM\n" +
				"fhzb_laiyuan\n" +
				"WHERE\n" +
				"fhzb_laiyuan.fhzb_id = '"+fhzb_id+"'";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("fhzb_id", rs.getString("fhzb_id"));
				jsonobj.put("xmid", rs.getString("xmid"));
				jsonobj.put("xm_name", rs.getString("xm_name"));
				jsonobj.put("sc_name", rs.getString("sc_name"));
				jsonobj.put("sc_bh", rs.getString("sc_bh"));
				jsonobj.put("laiyuan_name", rs.getString("laiyuan_name"));
				jsonobj.put("zhbc", rs.getString("zhbc"));
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
