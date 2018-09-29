package servletBanBao;

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
 * Servlet implementation class ServletBanBao_DingZhi_CX
 */
@WebServlet("/ServletBanBao_DingZhi_CX")
public class ServletBanBao_DingZhi_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBanBao_DingZhi_CX() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		String banbao_id=request.getParameter("banbao_id");
		sql = "SELECT\n" +
				"banbao_dz.id,\n" +
				"banbao_dz.banbao_id,\n" +
				"banbao_dz.xm_id,\n" +
				"banbao_dz.xm_name,\n" +
				"banbao_dz.`row`,\n" +
				"banbao_dz.col,\n" +
				"banbao_dz.sfky\n" +
				"FROM\n" +
				"banbao_dz\n" +
				"WHERE\n" +
				"banbao_dz.banbao_id = '"+banbao_id+"'\n" +
				"ORDER BY\n" +
				"banbao_dz.`row` ASC,\n" +
				"banbao_dz.col ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("banbao_id", rs.getString("banbao_id"));
				jsonobj.put("xm_id", rs.getString("xm_id"));
				jsonobj.put("xm_name", rs.getString("xm_name"));
				jsonobj.put("row", rs.getString("row"));
				jsonobj.put("col", rs.getString("col"));
				if (rs.getString("sfky").equals("1")) {
					jsonobj.put("sfky", "可用");
				}else {
					jsonobj.put("sfky", "不可用");
				}
				
				jsonarray.add(jsonobj);
			}
			DB.close(conn);
			rs.close();
			stmt.close();
			out.println(jsonarray);
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
