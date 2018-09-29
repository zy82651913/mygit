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
 * Servlet implementation class ServletXm_ScCx
 */
@WebServlet("/ServletXm_ScCx")
public class ServletXm_ScCx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXm_ScCx() {
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
				"sczb.id,\n" +
				"sczb.xmid,\n" +
				"sczb.pxid,\n" +
				"sczb.zb_name,\n" +
				"sczb.zbbh,\n" +
				"sczb.mathclass,\n" +
				"sczb.math,\n" +
				"sczb.qxdyzb,\n" +
				"sczb.xsdws,\n" +
				"sczb.bbyn\n" +
				"FROM\n" +
				"sczb\n" +
				"WHERE\n" +
				"sczb.xmid = '"+xmid+"'\n" +
				"ORDER BY\n" +
				"sczb.pxid ASC";
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
				jsonobj.put("zb_name", rs.getString("zb_name"));
				jsonobj.put("zbbh", rs.getString("zbbh"));
				if (Integer.parseInt(rs.getString("mathclass"))==1) {
					jsonobj.put("mathclass", "一般公式");
				}else{
					jsonobj.put("mathclass", "曲线公式");
				}
				jsonobj.put("math", rs.getString("math"));
				jsonobj.put("qxdyzb", rs.getString("qxdyzb"));
				jsonobj.put("xsdws", rs.getString("xsdws"));
				if (Integer.parseInt(rs.getString("bbyn"))==1) {
					jsonobj.put("bbyn", "显示");
				}else{
					jsonobj.put("bbyn", "不显示");
				}
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
