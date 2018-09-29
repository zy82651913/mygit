package servletSuJuLR;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletQuXian
 */
@WebServlet("/ServletQuXian")
public class ServletQuXian extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletQuXian() {
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
    	PrintWriter out =response.getWriter();
    	Connection conn=null;//定义为空值
    	ResultSet rs = null;
    	Statement stmt = null;
    	String sql;
    	JSONObject jsonobj = new JSONObject();//JSON对象
    	String qx_name = request.getParameter("qx_name");
    	try {
    		sql = "SELECT * FROM qxgl WHERE qxgl.qx_name='"+qx_name+"'";
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				jsonobj.put("a1", rs.getString("a1"));
				jsonobj.put("a2", rs.getString("a2"));
				jsonobj.put("a3", rs.getString("a3"));
				jsonobj.put("a4", rs.getString("a4"));
			}
			out.println(jsonobj);
			DB.close(conn);
			rs.close();
			stmt.close();
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
