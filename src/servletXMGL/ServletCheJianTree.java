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
 * Servlet implementation class ServletCheJianTree
 */
@WebServlet("/ServletCheJianTree")
public class ServletCheJianTree extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCheJianTree() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	//response.setContentType("text/html;charset=UTF-8");
    	response.setContentType("text/json; charset=utf-8");
    	
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"chejian.`name` AS chejianname,\n" +
				"jizhan.`name` AS jizhanname,\n" +
				"chejian_jizhan.jizhanid,\n" +
				"chejian_jizhan.chejianid\n" +
				"FROM\n" +
				"jizhan\n" +
				"INNER JOIN chejian_jizhan ON jizhan.id = chejian_jizhan.jizhanid\n" +
				"INNER JOIN chejian ON chejian.id = chejian_jizhan.chejianid\n" +
				"ORDER BY\n" +
				"chejian.pxid ASC";
		
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("jizhanid", rs.getString("jizhanid"));
				jsonobj.put("jizhanname", rs.getString("jizhanname"));
				jsonobj.put("chejianid", rs.getString("chejianid"));
				jsonobj.put("chejianname", rs.getString("chejianname"));
				jsonarray.add(jsonobj);
			}
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
