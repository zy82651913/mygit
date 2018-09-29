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
 * Servlet implementation class ServletBanBao_combobox
 */
@WebServlet("/ServletBanBao_combobox")
public class ServletBanBao_combobox extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBanBao_combobox() {
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
		sql = "SELECT\n" +
				"banbao_gl.banbao_id,\n" +
				"banbao_gl.pxid,\n" +
				"banbao_gl.banbao_name,\n" +
				"banbao_gl.banbao_bh,\n" +
				"banbao_gl.sfky\n" +
				"FROM\n" +
				"banbao_gl\n" +
				"WHERE\n" +
				"banbao_gl.sfky = 1\n" +
				"ORDER BY\n" +
				"banbao_gl.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			int i = 0;
			while(rs.next()){
				jsonobj = new JSONObject();//JSON对象
				if (i==0) {
					jsonobj.put("value", rs.getString("banbao_id"));
					jsonobj.put("text", rs.getString("banbao_name"));
					jsonobj.put("selected", "true");
					i++;
				}else {
					jsonobj.put("value", rs.getString("banbao_id"));
					jsonobj.put("text", rs.getString("banbao_name"));
					i++;
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
