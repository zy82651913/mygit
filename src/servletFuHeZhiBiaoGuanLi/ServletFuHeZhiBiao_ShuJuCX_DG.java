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
 * Servlet implementation class ServletFuHeZhiBiao_ShuJuCX_DG
 */
@WebServlet("/ServletFuHeZhiBiao_ShuJuCX_DG")
public class ServletFuHeZhiBiao_ShuJuCX_DG extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeZhiBiao_ShuJuCX_DG() {
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
		String fhzb_date=request.getParameter("fhzb_date");
		String fhzb_bc=request.getParameter("fhzb_bc");
		sql = "SELECT\n" +
				"fhzb_data.id,\n" +
				"fhzb_data.fhzb_id,\n" +
				"fhzb_data.fhzb_name,\n" +
				"fhzb_data.fhzb_date,\n" +
				"fhzb_data.fhzb_bc,\n" +
				"fhzb_data.fhzb_val\n" +
				"FROM\n" +
				"fhzb_data\n" +
				"WHERE\n" +
				"fhzb_data.fhzb_date = '"+fhzb_date+"' AND\n" +
				"fhzb_data.fhzb_bc = '"+fhzb_bc+"'";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("fhzb_id", rs.getString("id"));
				jsonobj.put("fhzb_name", rs.getString("fhzb_name"));
				jsonobj.put("fhzb_date", rs.getString("fhzb_date"));
				if (rs.getString("fhzb_bc").equals("1")) {
					jsonobj.put("fhzb_bc", "零点");
				}else if (rs.getString("fhzb_bc").equals("2")) {
					jsonobj.put("fhzb_bc", "白班");
				}else if (rs.getString("fhzb_bc").equals("3")) {
					jsonobj.put("fhzb_bc", "四点");
				}
				jsonobj.put("fhzb_val", rs.getString("fhzb_val"));
				jsonarray.add(jsonobj);
			}
	
			out.print(jsonarray);

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
