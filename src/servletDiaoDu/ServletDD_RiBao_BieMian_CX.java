package servletDiaoDu;

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
 * Servlet implementation class ServletDD_RiBao_BieMian_CX
 */
@WebServlet("/ServletDD_RiBao_BieMian_CX")
public class ServletDD_RiBao_BieMian_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDD_RiBao_BieMian_CX() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		sql = "SELECT\n" +
				"ddrb_beimian.id,\n" +
				"ddrb_beimian.pxid,\n" +
				"ddrb_beimian.xmid,\n" +
				"ddrb_beimian.xm_name,\n" +
				"ddrb_beimian.zbbh,\n" +
				"ddrb_beimian.xmzb_name,\n" +
				"ddrb_beimian.guiding,\n" +
				"ddrb_beimian.xsws,\n" +
				"ddrb_beimian.sjclass\n" +
				"FROM\n" +
				"ddrb_beimian\n" +
				"ORDER BY\n" +
				"ddrb_beimian.pxid ASC";
		try {

			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			
			JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			
			while(rs.next()){
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("pxid", rs.getString("pxid"));
				jsonobj.put("xmid", rs.getString("xmid"));
				jsonobj.put("xm_name", rs.getString("xm_name"));
				jsonobj.put("zbbh", rs.getString("zbbh"));
				jsonobj.put("xmzb_name", rs.getString("xmzb_name"));
				jsonobj.put("guiding", rs.getString("guiding"));
				jsonobj.put("xsdws", rs.getString("xsws"));
				if (rs.getString("sjclass").equals("0")) {
					jsonobj.put("sjclass", "一般数据");
				}else {
					jsonobj.put("sjclass", "复合数据");
				}
				
				jsonarray.add(jsonobj);
			}
			DB.close(conn);
			rs.close();
			stmt.close();
			out.print(jsonarray);
			} catch (SQLException e) {
				System.out.println(e);
				e.printStackTrace();
			}
	}

}
