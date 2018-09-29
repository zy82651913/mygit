package servletSuJuTongJi;

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
 * Servlet implementation class ServletDianYang_CX
 */
@WebServlet("/ServletDianYang_CX")
public class ServletDianYang_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDianYang_CX() {
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
    	JSONObject jsonObject = new JSONObject();//向前台传数据的json对象
    	JSONArray jsonArray = new JSONArray();//向前台传数据的json数组
    	JSONObject title_obj = new JSONObject();
    	JSONArray title_arr = new JSONArray();
    	JSONObject content_obj = new JSONObject();
    	JSONArray content_arr = new JSONArray();
    	String date=request.getParameter("date");
    	String jizhanid=request.getParameter("jizhanid");
    	String year = date.substring(0, 4);// 提取年
		String month = date.substring(5, 7);// 提取月
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		//取得表头的指标名称
			sql = "SELECT DISTINCT\n" +
					"sczb.zb_name,\n" +
					"sczb.zbbh\n" +
					"FROM\n" +
					"chejian_jizhan\n" +
					"INNER JOIN chejian ON chejian_jizhan.chejianid = chejian.id\n" +
					"INNER JOIN xmgl ON chejian.id = xmgl.chejianid\n" +
					"INNER JOIN sczb ON xmgl.id = sczb.xmid\n" +
					"WHERE\n" +
					"chejian_jizhan.jizhanid = '"+jizhanid+"'";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				title_obj.put("zb_name", rs.getString("zb_name"));
				title_obj.put("zbbh", rs.getString("zbbh"));
				title_arr.add(title_obj);
			}
			stmt.close();
			rs.close();
			//查询输出数据
			stmt = conn.createStatement();
			sql = "SELECT\n" +
					"sczb_data.xm_name,\n" +
					"sczb_data.hysj_date,\n" +
					"sczb_data.hysjd_time,\n" +
					"sczb_data.hy_bc,\n" +
					"sczb_data.sczb_data\n" +
					"FROM\n" +
					"chejian_jizhan\n" +
					"INNER JOIN xmgl ON chejian_jizhan.chejianid = xmgl.chejianid\n" +
					"INNER JOIN sczb_data ON xmgl.id = sczb_data.xm_id\n" +
					"WHERE\n" +
					"chejian_jizhan.jizhanid = '"+jizhanid+"' AND\n" +
					"year(hysj_date) = '"+year+"' AND\n" +
					"month(hysj_date) = '"+month+"'\n" +
					"ORDER BY\n" +
					"sczb_data.xm_name ASC,\n" +
					"sczb_data.hysj_date ASC,\n" +
					"sczb_data.hysjd_time ASC";
			rs=stmt.executeQuery(sql);
			while(rs.next()){
				content_obj.put("xm_name", rs.getString("xm_name"));
				content_obj.put("hysj_date", rs.getString("hysj_date"));
				content_obj.put("hysjd_time", rs.getString("hysjd_time"));
				content_obj.put("hy_bc", rs.getString("hy_bc"));
				content_obj.put("sczb_data", rs.getString("sczb_data"));
				content_arr.add(content_obj);
			}
			stmt.close();
			rs.close();
			conn.close();
			DB.close(conn);
			
			jsonObject.put("xm_name", "项目名称");
			jsonObject.put("hysj_date", "日期");
			jsonObject.put("hysjd_time", "时间");
			jsonObject.put("hy_bc", "班次");
			for (int i = 0; i < title_arr.size(); i++) {//插入第一行头
				String zb_name = title_arr.getJSONObject(i).getString("zb_name");
				String zbbh = title_arr.getJSONObject(i).getString("zbbh");
				jsonObject.put(zbbh, zb_name);
				
			}
			jsonArray.add(jsonObject);
			jsonObject = new JSONObject();
			for (int i = 0; i < content_arr.size(); i++) {
				jsonObject.put("xm_name", content_arr.getJSONObject(i).getString("xm_name"));
				jsonObject.put("hysj_date", content_arr.getJSONObject(i).getString("hysj_date"));
				jsonObject.put("hysjd_time", content_arr.getJSONObject(i).getString("hysjd_time"));
				jsonObject.put("hy_bc", content_arr.getJSONObject(i).getString("hy_bc"));
				JSONArray sczb_data = content_arr.getJSONObject(i).getJSONArray("sczb_data");
				for (int j = 0; j < title_arr.size(); j++) {
					for (int j2 = 0; j2 < sczb_data.size(); j2++) {
						if (title_arr.getJSONObject(j).getString("zbbh").equals(sczb_data.getJSONObject(j2).getString("sczb_bh"))) {
							String zbbh = title_arr.getJSONObject(j).getString("zbbh");
							String val = sczb_data.getJSONObject(j2).getString("sczb_value");
							jsonObject.put(zbbh, val);
							break;
						}else {
							jsonObject.put(title_arr.getJSONObject(j).getString("zbbh"), "");
						}
					}
				}
				jsonArray.add(jsonObject);
			}
			
			out.print(jsonArray);
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
