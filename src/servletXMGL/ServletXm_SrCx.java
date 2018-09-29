package servletXMGL;

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
 * Servlet implementation class ServletXm_SrCx
 */
@WebServlet("/ServletXm_SrCx")
public class ServletXm_SrCx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXm_SrCx() {
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
    	String xmid = request.getParameter("xmid");
    	Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"srzb.id,\n" +
				"srzb.xmid,\n" +
				"srzb.pxid,\n" +
				"srzb.srname,\n" +
				"srzb.zbbh,\n" +
				"srzb.csvalue\n" +
				"FROM\n" +
				"srzb\n" +
				"WHERE\n" +
				"srzb.xmid = '"+xmid+"'\n" +
				"ORDER BY\n" +
				"srzb.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("xmid", rs.getString("xmid"));
				jsonobj.put("pxid", rs.getString("pxid"));
				jsonobj.put("srname", rs.getString("srname"));
				jsonobj.put("zbbh", rs.getString("zbbh"));
				jsonobj.put("csvalue", rs.getString("csvalue"));
				jsonarray.add(jsonobj);
			}
			//System.out.println(jsonarray);
			//JSONObject getJsonObj = jsonarray.getJSONObject(2);//获取json数组中的第一项
			//String str = getJsonObj.getString("ShowName");
			//System.out.println("名字="+name);//检查用户名称能否获取			
			out.println(jsonarray);

			DB.close(conn);
			rs.close();
			stmt.close();
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
