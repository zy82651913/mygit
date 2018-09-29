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
 * Servlet implementation class ServletJiXiaoKaoHe_JS_CX
 */
@WebServlet("/ServletJiXiaoKaoHe_JS_CX")
public class ServletJiXiaoKaoHe_JS_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_JS_CX() {
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
		String date=request.getParameter("date");
		sql = "SELECT\n" +
				"jxkh_result.jxkhgl_id,\n" +
				"jxkh_result.cj,\n" +
				"jxkh_result.khxm,\n" +
				"jxkh_result.date,\n" +
				"jxkh_result.result\n" +
				"FROM\n" +
				"jxkh_result\n" +
				"WHERE\n" +
				"jxkh_result.date = '"+date+"'";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("jxkhgl_id", rs.getString("jxkhgl_id"));
				jsonobj.put("cj", rs.getString("cj"));
				jsonobj.put("khxm", rs.getString("khxm"));
				jsonobj.put("date", rs.getString("date"));
				jsonobj.put("result", rs.getString("result"));
				
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
