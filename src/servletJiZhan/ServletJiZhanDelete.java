package servletJiZhan;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
 * Servlet implementation class ServletJiZhanDelete
 */
@WebServlet("/ServletJiZhanDelete")
public class ServletJiZhanDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiZhanDelete() {
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
		PrintWriter out =response.getWriter();
		JSONObject jsonobj = new JSONObject();//JSON对象
		String sql = null;
		String sql1 = null;
		int n = -1;
		String id=request.getParameter("id");
		sql = "DELETE FROM jizhan WHERE id='"+id+"'";
		sql1 = "DELETE FROM chejian_jizhan WHERE jizhanid='"+id+"'";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			n=stmt.executeUpdate(sql);//删除数据 
			n=stmt.executeUpdate(sql1);//删除数据 
			if (n>0) {
				//删除成功后，要想刷新可编辑数据表，需要向前台传一个{"success":"true"}
				jsonobj.put("success", "true");
				out.print(jsonobj);
			}else{
				jsonobj.put("success", "false");
				out.print(jsonobj);
			}
			DB.close(conn);
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
