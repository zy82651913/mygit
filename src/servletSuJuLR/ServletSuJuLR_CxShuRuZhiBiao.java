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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletSuJuLR_CxShuRuZhiBiao
 */
@WebServlet("/ServletSuJuLR_CxShuRuZhiBiao")
public class ServletSuJuLR_CxShuRuZhiBiao extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletSuJuLR_CxShuRuZhiBiao() {
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
    	String id=request.getParameter("id");
    	sql = "SELECT\n" +
    			"srzb_data.srzb_data\n" +
    			"FROM\n" +
    			"srzb_data\n" +
    			"WHERE\n" +
    			"srzb_data.id = '"+id+"'";
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		rs=stmt.executeQuery(sql);
			String srzbdata = null;
			while(rs.next()){
				srzbdata = rs.getString("srzb_data");
			}
			JSONArray sd = JSONArray.fromObject(srzbdata);
			out.println(sd);
			DB.close(conn);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO: handle exception
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
