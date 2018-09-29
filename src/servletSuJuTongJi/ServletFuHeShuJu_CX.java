package servletSuJuTongJi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletFuHeShuJu_CX
 */
@WebServlet("/ServletFuHeShuJu_CX")
public class ServletFuHeShuJu_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeShuJu_CX() {
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
    	JSONArray jsonArray = new JSONArray();//向前台传数据的json数组
    	JSONObject title_obj = new JSONObject();
    	JSONArray title_arr = new JSONArray();
    	JSONObject content_obj = new JSONObject();
    	JSONArray content_arr = new JSONArray();
    	String date=request.getParameter("date");
    	String year = date.substring(0, 4);// 提取年
		String month = date.substring(5, 7);// 提取月
		
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			//取得表头数据
			title_obj.put("日期", "fhzb_date");
			title_obj.put("班次", "fhzb_bc");
    		stmt = conn.createStatement();
    		sql = "SELECT\n" +
    				"fhzbgl.fhzb_name,\n" +
    				"fhzbgl.id\n" +
    				"FROM\n" +
    				"fhzbgl\n" +
    				"ORDER BY\n" +
    				"fhzbgl.pxid ASC";
    		rs=stmt.executeQuery(sql);
			while(rs.next()){
				title_obj.put(rs.getString("fhzb_name"), rs.getString("id"));
			}
			title_arr.add(title_obj);
			stmt.close();
			rs.close();
    		
    		//取得复合数据
    		stmt = conn.createStatement();
			sql = "SELECT\n" +
					"fhzb_data.fhzb_name,\n" +
					"fhzb_data.fhzb_val,\n" +
					"fhzb_data.fhzb_date,\n" +
					"fhzb_data.fhzb_bc,\n" +
					"fhzb_data.fhzb_id\n" +
					"FROM\n" +
					"fhzb_data\n" +
					"WHERE\n" +
					"year(fhzb_date) = '"+year+"' AND\n" +
					"month(fhzb_date) = '"+month+"'\n" +
					"ORDER BY\n" +
					"fhzb_data.fhzb_name ASC,\n" +
					"fhzb_data.fhzb_date ASC,\n" +
					"fhzb_data.fhzb_bc ASC";
			rs=stmt.executeQuery(sql);
			JSONObject linshi_obj = new JSONObject();
	    	JSONArray linshi_arr = new JSONArray();
			while(rs.next()){
				linshi_obj.put("fhzb_id", rs.getString("fhzb_id"));
				linshi_obj.put("fhzb_date", rs.getString("fhzb_date"));
				linshi_obj.put("fhzb_bc", rs.getString("fhzb_bc"));
				linshi_obj.put("fhzb_name", rs.getString("fhzb_name"));
				linshi_obj.put("fhzb_val", rs.getString("fhzb_val"));
				linshi_arr.add(linshi_obj);
			}
			
			Calendar a = Calendar.getInstance();
			a.set(Calendar.YEAR, Integer.parseInt(year));
			a.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			a.set(Calendar.DATE, 1);
			a.roll(Calendar.DATE, -1);
			int tianshu = a.get(Calendar.DATE);
			
			int bc = 1;
			int n = 0;
			int d = 1;
			for (int i = 1; i <= tianshu*3; i++) {
				String day;
				if (n == 3) {
					d++;
				}
				if (d<10) {
					day = "0"+d;
				}else {
					day = String.valueOf(d);
				}
				if (n == 3) {
					bc = 1;
				}
				if (n == 3) {
					n = 0;
				}
				
				String fhzb_date = date + "-" + day;
				content_obj = new JSONObject();
				content_obj.put("fhzb_date", fhzb_date);
				content_obj.put("fhzb_bc", bc);
				for (int j = 0; j < linshi_arr.size(); j++) {
					if (fhzb_date.equals(linshi_arr.getJSONObject(j).getString("fhzb_date")) && String.valueOf(bc).equals(linshi_arr.getJSONObject(j).getString("fhzb_bc"))) {
						content_obj.put(linshi_arr.getJSONObject(j).getString("fhzb_id"), linshi_arr.getJSONObject(j).getString("fhzb_val"));
					}
				}
				content_arr.add(content_obj);
				bc++;
				n++;
			}
			stmt.close();
			rs.close();
			DB.close(conn);
			jsonArray.add(title_arr);
			jsonArray.add(content_arr);
			out.print(jsonArray);
		} catch (SQLException e) {
			System.out.println(e);
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
