package servletMenu;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletMenuUpdate
 */
@WebServlet("/ServletMenuUpdate")
public class ServletMenuUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMenuUpdate() {
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
		sql = "UPDATE menu SET PXID=?,NodeName=?,ShowName=?,ParentID=?,NodeUrl=? WHERE NodeID=? "; //修改数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, PXID);
			ps1.setString(2, NodeName);
			ps1.setString(3, ShowName);
			ps1.setString(4, ParentID);
			ps1.setString(5, NodeUrl);
			ps1.setString(6, NodeID);
			ps1.executeUpdate();
			//返回JSON
				JSONObject jsonobj = new JSONObject();//JSON对象
				jsonobj.put("NodeID", NodeID);
				jsonobj.put("PXID", PXID);
				jsonobj.put("NodeName", NodeName);
				jsonobj.put("ShowName", ShowName);
				jsonobj.put("ParentID", ParentID);
				jsonobj.put("NodeUrl", NodeUrl);
				//System.out.println(jsonobj);
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
