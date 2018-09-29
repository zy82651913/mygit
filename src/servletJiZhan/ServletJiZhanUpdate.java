package servletJiZhan;

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
 * Servlet implementation class ServletJiZhanUpdate
 */
@WebServlet("/ServletJiZhanUpdate")
public class ServletJiZhanUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiZhanUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
		response.setContentType("text/json;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		String sql = null;
		PreparedStatement ps1 = null;
		PrintWriter out =response.getWriter();
		String id=request.getParameter("id");
		String pxid=request.getParameter("pxid");
		String name=request.getParameter("name");
/*		System.out.println(NodeID);
		System.out.println(PXID);
		System.out.println(NodeName);
		System.out.println(ShowName);
		System.out.println(ParentID);
		System.out.println(NodeUrl);*/
		sql = "UPDATE jizhan SET pxid=?,name=? WHERE id=? "; //修改数据
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps1 = conn.prepareStatement(sql);
			ps1.setString(1, pxid);
			ps1.setString(2, name);
			ps1.setString(3, id);
			ps1.executeUpdate();
			//返回JSON
				JSONObject jsonobj = new JSONObject();//JSON对象
				jsonobj.put("id", id);
				jsonobj.put("pxid", pxid);
				jsonobj.put("name", name);
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
