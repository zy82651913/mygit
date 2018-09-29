package servletXMGL;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
 * Servlet implementation class ServletXm_xtsrzbSave
 */
@WebServlet("/ServletXm_xtsrzbSave")
public class ServletXm_xtsrzbSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXm_xtsrzbSave() {
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
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		String sql1 = null;
		PreparedStatement ps = null;
		PrintWriter out =response.getWriter();
		JSONObject jsonobj = new JSONObject();//JSON对象
		String json=request.getParameter("json");
    	JSONObject j=JSONObject.fromObject(json);//把前台传入的json对象字符串转为json对象
    	String xmid = j.getString("xmid");
    	JSONArray aa= j.getJSONArray("aa");
    	sql = "INSERT INTO srzb (xmid, pxid, srname, zbbh) VALUES (?,?,?,?)"; //插入数据
    	try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			for (int i = 0; i < aa.size(); i++) {
				String pxid = aa.getJSONObject(i).getString("pxid");
				String srname = aa.getJSONObject(i).getString("srname");
				String zbbh = aa.getJSONObject(i).getString("zbbh");
				sql1 = "SELECT\n" +
		    			"srzb.srname\n" +
		    			"FROM\n" +
		    			"srzb\n" +
		    			"WHERE\n" +
		    			"srzb.xmid = '"+xmid+"' AND\n" +
		    			"srzb.srname = '"+srname+"'";
				stmt = conn.createStatement();//创建Statement对象
				rs = stmt.executeQuery(sql1);
				if (rs.next()) {
					continue;
				}else {
					ps = conn.prepareStatement(sql);
					ps.setString(1, xmid);
					ps.setString(2, pxid);
					ps.setString(3, srname);
					ps.setString(4, zbbh);
					ps.executeUpdate();
				}
			}
				DB.close(conn);
				stmt.close();
				rs.close();
				if (ps!=null) {
					ps.close();
				}
				//返回JSON
				jsonobj.put("xmid", xmid);
				out.print(jsonobj);
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
