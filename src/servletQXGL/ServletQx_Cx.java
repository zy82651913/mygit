package servletQXGL;

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
 * Servlet implementation class ServletQx_Cx
 */
@WebServlet("/ServletQx_Cx")
public class ServletQx_Cx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletQx_Cx() {
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
				"*\n" +
				"FROM\n" +
				"qxgl\n" +
				"ORDER BY\n" +
				"qxgl.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("pxid", rs.getString("pxid"));
				jsonobj.put("qx_name", rs.getString("qx_name"));
				jsonobj.put("a1", rs.getString("a1"));
				jsonobj.put("a2", rs.getString("a2"));
				jsonobj.put("a3", rs.getString("a3"));
				jsonobj.put("a4", rs.getString("a4"));
				jsonobj.put("qx_math", rs.getString("qx_math"));
				jsonobj.put("qx_mnds", rs.getString("qx_mnds"));
				jsonobj.put("qx_note", rs.getString("qx_note"));
				jsonarray.add(jsonobj);
			}		
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
