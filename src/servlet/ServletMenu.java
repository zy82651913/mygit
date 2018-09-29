package servlet;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import java.io.IOException;
import java.io.ObjectOutputStream.PutField;
import java.io.PrintWriter;
import java.sql.*;
import java.util.jar.Attributes.Name;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

//import org.eclipse.jdt.internal.compiler.ast.WhileStatement;

/**
 * Servlet implementation class ServletMenu
 */
@WebServlet("/ServletMenu")
public class ServletMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMenu() {
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
		String name = request.getParameter("name");
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"menu.NodeID,\n" +
				"menu.PXID,\n" +
				"menu.NodeName,\n" +
				"menu.ShowName,\n" +
				"menu.ParentID,\n" +
				"menu.NodeUrl\n" +
				"FROM\n" +
				"`user`\n" +
				"INNER JOIN user_role ON user_role.UserID = `user`.UserID\n" +
				"INNER JOIN role ON role.RoleID = user_role.RoleID\n" +
				"INNER JOIN menu_role ON menu_role.RoleID = role.RoleID\n" +
				"INNER JOIN menu ON menu.NodeID = menu_role.NodeID\n" +
				"WHERE\n" +
				"`user`.UserName = '"+ name + "' " +
				"ORDER BY\n" +
				"menu.PXID ASC,\n" +
				"menu.ParentID ASC";
		
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				
				jsonobj.put("NodeID", rs.getString("NodeID"));
				jsonobj.put("PXID", rs.getString("PXID"));
				jsonobj.put("NodeName", rs.getString("NodeName"));
				jsonobj.put("ShowName", rs.getString("ShowName"));
				jsonobj.put("ParentID", rs.getString("ParentID"));
				jsonobj.put("NodeUrl", rs.getString("NodeUrl"));
				
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
