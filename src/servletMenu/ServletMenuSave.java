package servletMenu;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletMenuSave
 */
@WebServlet("/ServletMenuSave")
public class ServletMenuSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMenuSave() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	//response.setContentType("text/html;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		String sql = null;
		PreparedStatement ps1 = null;
		PrintWriter out =response.getWriter();
		String NodeID=request.getParameter("NodeID");
		String PXID=request.getParameter("PXID");
		String NodeName=request.getParameter("NodeName");
		String ShowName=request.getParameter("ShowName");
		String ParentID=request.getParameter("ParentID");
		String NodeUrl=request.getParameter("NodeUrl");
/*		System.out.println(NodeID);
		System.out.println(PXID);
		System.out.println(NodeName);
		System.out.println(ShowName);
		System.out.println(ParentID);
		System.out.println(NodeUrl);*/
		sql = "INSERT INTO menu (PXID, NodeName, ShowName, ParentID, NodeUrl) VALUES (?,?,?,?,?)"; //插入数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, PXID);
			ps1.setString(2, NodeName);
			ps1.setString(3, ShowName);
			ps1.setString(4, ParentID);
			ps1.setString(5, NodeUrl);
			ps1.executeUpdate();
			DB.close(conn);
			ps1.close();
			//返回JSON
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			String sql1 = "SELECT\n" +
					"menu.NodeID,\n" +
					"menu.PXID,\n" +
					"menu.NodeName,\n" +
					"menu.ShowName,\n" +
					"menu.ParentID,\n" +
					"menu.NodeUrl\n" +
					"FROM\n" +
					"menu\n" +
					"WHERE\n" +
					"menu.PXID = ? AND\n" +
					"menu.NodeName = ? AND\n" +
					"menu.ShowName = ? AND\n" +
					"menu.ParentID = ? AND\n" +
					"menu.NodeUrl = ?";
				ps1 = conn.prepareStatement(sql1);
				ps1.setString(1, PXID);
				ps1.setString(2, NodeName);
				ps1.setString(3, ShowName);
				ps1.setString(4, ParentID);
				ps1.setString(5, NodeUrl);
				ps1.executeQuery();
				JSONObject jsonobj = new JSONObject();//JSON对象
				jsonobj.put("NodeID", NodeID);
				jsonobj.put("PXID", PXID);
				jsonobj.put("NodeName", NodeName);
				jsonobj.put("ShowName", ShowName);
				jsonobj.put("ParentID", ParentID);
				jsonobj.put("NodeUrl", NodeUrl);
				out.print(jsonobj);
				DB.close(conn);
				ps1.close();
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
