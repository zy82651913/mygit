package servletSuJuLR;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletScDataGrid_Cx
 */
@WebServlet("/ServletScDataGrid_Cx")
public class ServletScDataGrid_Cx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletScDataGrid_Cx() {
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
    	String xm_id=request.getParameter("xm_id");
    	String hysj_date=request.getParameter("hysj_date");
    	String hy_bc=request.getParameter("hy_bc");
    	sql = "SELECT\n" +
    			"sczb_data.id,\n" +
    			"sczb_data.srzb_data_id,\n" +
    			"sczb_data.xm_id,\n" +
    			"sczb_data.xm_name,\n" +
    			"sczb_data.hysj_date,\n" +
    			"sczb_data.hysjd_time,\n" +
    			"sczb_data.gx_zhbc,\n" +
    			"sczb_data.hy_bc,\n" +
    			"sczb_data.sczb_data,\n" +
    			"sczb_data.beizhu,\n" +
    			"sczb_data.beizhu_name\n" +
    			"FROM\n" +
    			"sczb_data\n" +
    			"WHERE\n" +
    			"sczb_data.xm_id = '"+xm_id+"' AND\n" +
    			"sczb_data.hysj_date = '"+hysj_date+"' AND\n" +
    			"sczb_data.hy_bc = '"+hy_bc+"'" +
    			"ORDER BY\n" +
    			"sczb_data.hysjd_time ASC";
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		rs=stmt.executeQuery(sql);
    		JSONObject jsonobj = new JSONObject();//JSON对象
			JSONArray jsonarray = new JSONArray(); //JSON数组
			while(rs.next()){
				jsonobj.put("id", rs.getString("id"));
				jsonobj.put("srzb_data_id", rs.getString("srzb_data_id"));
				jsonobj.put("xm_id", rs.getString("xm_id"));
				jsonobj.put("xm_name", rs.getString("xm_name"));
				jsonobj.put("hysj_date", rs.getString("hysj_date"));
				String st = rs.getString("hysjd_time");
				st = st.substring(0, st.length()-3);
				jsonobj.put("hysjd_time",st );
				jsonobj.put("gx_zhbc", rs.getString("gx_zhbc"));
				jsonobj.put("hy_bc", rs.getString("hy_bc"));
				String sczbdata = rs.getString("sczb_data");
				JSONArray sd = JSONArray.fromObject(sczbdata);
				for (int i = 0; i < sd.size(); i++) {
					JSONObject jso = sd.getJSONObject(i);
					jsonobj.put(jso.get("sczb_bh"), jso.get("sczb_value"));
				}
				jsonobj.put("beizhu", rs.getString("beizhu"));
				jsonobj.put("beizhu_name", rs.getString("beizhu_name"));
				jsonarray.add(jsonobj);
			}
			out.println(jsonarray);
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
