package servletJiXiaoKaoHe;

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
 * Servlet implementation class ServletJiXiaoKaoHe_DZ_CX
 */
@WebServlet("/ServletJiXiaoKaoHe_DZ_CX")
public class ServletJiXiaoKaoHe_DZ_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_DZ_CX() {
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
		String jxkhgl_id=request.getParameter("jxkhgl_id");
		sql = "SELECT\n" +
				"jxkh_dz.jxkhdz_id,\n" +
				"jxkh_dz.jxkhgl_id,\n" +
				"jxkh_dz.sjkxm_id,\n" +
				"jxkh_dz.sjkxm_name,\n" +
				"jxkh_dz.sjkzb_name,\n" +
				"jxkh_dz.sjkzb_bh,\n" +
				"jxkh_dz.start_date,\n" +
				"jxkh_dz.start_time,\n" +
				"jxkh_dz.end_date,\n" +
				"jxkh_dz.end_time,\n" +
				"jxkh_dz.khfs,\n" +
				"jxkh_dz.khbz,\n" +
				"jxkh_dz.sjclass\n" +
				"FROM\n" +
				"jxkh_dz\n" +
				"WHERE\n" +
				"jxkh_dz.jxkhgl_id = "+jxkhgl_id+"\n" +
				"ORDER BY\n" +
				"jxkh_dz.sjkxm_name ASC,\n" +
				"jxkh_dz.start_date ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("jxkhdz_id", rs.getString("jxkhdz_id"));
				jsonobj.put("jxkhgl_id", rs.getString("jxkhgl_id"));
				jsonobj.put("sjkxm_id", rs.getString("sjkxm_id"));
				jsonobj.put("sjkxm_name", rs.getString("sjkxm_name"));
				jsonobj.put("sjkzb_name", rs.getString("sjkzb_name"));
				jsonobj.put("sjkzb_bh", rs.getString("sjkzb_bh"));
				jsonobj.put("start_date", rs.getString("start_date"));
				jsonobj.put("start_time", rs.getString("start_time"));
				jsonobj.put("end_date", rs.getString("end_date"));
				jsonobj.put("end_time", rs.getString("end_time"));
				if (rs.getString("khfs").equals("1")) {
					jsonobj.put("khfs", "个数");
				} else if (rs.getString("khfs").equals("2")) {
					jsonobj.put("khfs", "合格率");
				} else if (rs.getString("khfs").equals("3")) {
					jsonobj.put("khfs", "平均值");
				}
				
				jsonobj.put("khbz", rs.getString("khbz"));
				jsonobj.put("sjclass", rs.getString("sjclass"));
				
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
