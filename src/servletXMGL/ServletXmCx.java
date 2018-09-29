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
 * Servlet implementation class ServletXmCx
 */
@WebServlet("/ServletXmCx")
public class ServletXmCx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXmCx() {
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
    	String chejianid = request.getParameter("id");
    	Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"xmgl.id,\n" +
				"xmgl.chejianid,\n" +
				"xmgl.pxid,\n" +
				"xmgl.`name`,\n" +
				"xmgl.`xmclass`,\n" +
				"xmgl.zhbc,\n" +
				"xmgl.yn\n" +
				"FROM\n" +
				"xmgl\n" +
				"WHERE\n" +
				"xmgl.chejianid = '"+ chejianid + "' \n" +
				"ORDER BY\n" +
				"xmgl.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			int in;
			while(rs.next()){
				
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("chejianid", rs.getString("chejianid"));
				jsonobj.put("pxid", rs.getString("pxid"));
				jsonobj.put("name", rs.getString("name"));
				jsonobj.put("xmclass", rs.getString("xmclass"));
				int xmc = Integer.parseInt(rs.getString("xmclass"));
				if (xmc == 1) {
					jsonobj.put("xmclass", "液相");
				} else if(xmc == 0){
					jsonobj.put("xmclass", "固相");
				}
				jsonobj.put("zhbc", rs.getString("zhbc"));
				in = Integer.parseInt(rs.getString("yn"));
				if (in == 1) {
					jsonobj.put("yn", "可用");
				}else if(in == 0){
					jsonobj.put("yn", "不可用");
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
