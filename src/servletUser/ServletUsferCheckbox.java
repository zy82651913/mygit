package servletUser;

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
 * Servlet implementation class ServletUsferCheckbox
 */
@WebServlet("/ServletUsferCheckbox")
public class ServletUsferCheckbox extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletUsferCheckbox() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json; charset=utf-8");
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		String UserID=request.getParameter("UserID");
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"user_role.RoleID\n" +
				"FROM\n" +
				"user_role\n" +
				"WHERE\n" +
				"user_role.UserID = '"+UserID+"'";
		try {
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("RoleID", rs.getString("RoleID"));
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
