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
 * Servlet implementation class ServletQuXianEditor_Cx
 */
@WebServlet("/ServletQuXianEditor_Cx")
public class ServletQuXianEditor_Cx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletQuXianEditor_Cx() {
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
    	String qxid = request.getParameter("qxid");
    	String mnds = request.getParameter("mnds");
    	try {
    		sql = "SELECT * FROM qxgl WHERE qxgl.id='"+qxid+"'";
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
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
			}
			sql = "SELECT * FROM qxlr WHERE qxlr.qxid='"+qxid+"'";
			rs = stmt.executeQuery(sql);
			String[] x = new String[Integer.parseInt(mnds)];
			String[] y = new String[Integer.parseInt(mnds)];
			int i = 0;
			while(rs.next()){
				x[i]=rs.getString("x");
				y[i]=rs.getString("y");
				i++;
			}
			jsonobj.put("x", x);
			jsonobj.put("y", y);
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
