package servletJiXiaoKaoHe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletJiXiaoKaoHe_DateEdit
 */
@WebServlet("/ServletJiXiaoKaoHe_DateEdit")
public class ServletJiXiaoKaoHe_DateEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_DateEdit() {
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
	    ResultSet rs = null;	
		String sql = null;
		PrintWriter out =response.getWriter();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Calendar calendar = Calendar.getInstance();
		String date=request.getParameter("date");
		String year = date.substring(0, 4);// 提取年
		String month = date.substring(5, 7);// 提取月
		Date date_new = null;
		try {
			date_new = sdf.parse(date);
		} catch (Exception e) {
			// TODO: handle exception
		}
		calendar.setTime(date_new);
		int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获得原始最大天数
		//date = date + "-" + max;
		JSONObject jsonobj = new JSONObject();//JSON对象
		JSONArray jsonarray = new JSONArray(); //JSON数组
		
		sql = "SELECT\n" +
				"jxkh_dz.jxkhdz_id,\n" +
				"jxkh_dz.start_date,\n" +
				"jxkh_dz.end_date\n" +
				"FROM\n" +
				"jxkh_dz";
		try {
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				jsonobj.put("jxkhdz_id", rs.getString("jxkhdz_id"));
				jsonobj.put("start_date", rs.getString("start_date"));
				jsonobj.put("end_date", rs.getString("end_date"));
				jsonarray.add(jsonobj);
			}
			rs.close();
			stmt.close();
			
			Date date_old = null;
			sdf = new SimpleDateFormat("yyyy-MM-dd");
			calendar = Calendar.getInstance();
			try {
				date_old = sdf.parse(jsonarray.getJSONObject(0).getString("start_date"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			calendar.setTime(date_old);
			int max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获得原始最大天数
			String end_date_old = jsonarray.getJSONObject(0).getString("start_date").substring(0, 8);
			/*
			for (int i = 0; i < jsonarray.size(); i++) {
				jsonarray.getJSONObject(i).put("start_date", date + jsonarray.getJSONObject(i).getString("start_date").substring(7, 10));
				if (jsonarray.getJSONObject(i).getString("end_date").equals(end_date_old)) {
					jsonarray.getJSONObject(i).put("end_date", date + "-" + max);
				}
			}
			*/
			int n = -1;
			stmt = conn.createStatement();//创建Statement对象
			for (int i = 0; i < jsonarray.size(); i++) {
				String jxkhdz_id = jsonarray.getJSONObject(i).getString("jxkhdz_id");
				String start_date = date + jsonarray.getJSONObject(i).getString("start_date").substring(7, 10);
				String end_date = null;
				if (jsonarray.getJSONObject(i).getString("end_date").equals(end_date_old+max_day)) {
					end_date = date + "-" + max;
				} else {
					end_date = date + jsonarray.getJSONObject(i).getString("end_date").subSequence(7, 10);
				}
				
				
				sql = "UPDATE jxkh_dz SET start_date='"+start_date+"',end_date='"+end_date+"' WHERE jxkhdz_id="+jxkhdz_id;
				n = stmt.executeUpdate(sql);
				
				if (n==-1) {
					out.print("false");
					stmt.close();
					DB.close(conn);
				}
			}
			if (n>0) {
				out.print("true");
			}
			stmt.close();
			DB.close(conn);
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
