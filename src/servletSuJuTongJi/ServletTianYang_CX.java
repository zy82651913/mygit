package servletSuJuTongJi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletTianYang_CX
 */
@WebServlet("/ServletTianYang_CX")
public class ServletTianYang_CX extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTianYang_CX() {
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
    	DecimalFormat df = new DecimalFormat();
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
    		//查表头数据
    		sql = "SELECT DISTINCT\n" +
    				"sczb.xmid,\n" +
    				"xmgl.`name`,\n" +
    				"sczb.zb_name,\n" +
    				"sczb.zbbh,\n" +
    				"sczb.xsdws\n" +
    				"FROM\n" +
    				"chejian_jizhan\n" +
    				"INNER JOIN chejian ON chejian_jizhan.chejianid = chejian.id\n" +
    				"INNER JOIN xmgl ON chejian.id = xmgl.chejianid\n" +
    				"INNER JOIN sczb ON xmgl.id = sczb.xmid\n" +
    				"WHERE\n" +
    				"chejian_jizhan.jizhanid = '"+jizhanid+"'\n" +
    				"ORDER BY\n" +
    				"xmgl.`name` ASC";
    		rs=stmt.executeQuery(sql);
			while(rs.next()){
				title_obj.put("xmid", rs.getString("xmid"));
				title_obj.put("xm_name", rs.getString("name"));
				title_obj.put("zb_name", rs.getString("zb_name"));
				title_obj.put("zbbh", rs.getString("zbbh"));
				title_obj.put("fid", rs.getString("name") + rs.getString("zbbh"));
				title_obj.put("xsdws", rs.getString("xsdws"));
				title_arr.add(title_obj);
			}
			stmt.close();
			rs.close();
			jsonArray.add(title_arr);
			stmt = conn.createStatement();
			sql = "SELECT\n" +
					"sczb_ri_avg.xm_name,\n" +
					"sczb_ri_avg.hysj_date,\n" +
					"sczb_ri_avg.val\n" +
					"FROM\n" +
					"chejian_jizhan\n" +
					"INNER JOIN chejian ON chejian_jizhan.chejianid = chejian.id\n" +
					"INNER JOIN xmgl ON chejian.id = xmgl.chejianid\n" +
					"INNER JOIN sczb_ri_avg ON xmgl.id = sczb_ri_avg.xm_id\n" +
					"WHERE\n" +
					"year(hysj_date) = '"+year+"' AND\n" +
					"month(hysj_date) = '"+month+"' AND\n" +
					"chejian_jizhan.jizhanid = "+jizhanid+"\n" +
					"ORDER BY\n" +
					"sczb_ri_avg.xm_name ASC,\n" +
					"sczb_ri_avg.hysj_date ASC";
			rs=stmt.executeQuery(sql);
			JSONObject linshi_obj = new JSONObject();
	    	JSONArray linshi_arr = new JSONArray();
			while(rs.next()){
				linshi_obj.put("xm_name", rs.getString("xm_name"));
				linshi_obj.put("hysj_date", rs.getString("hysj_date"));
				linshi_obj.put("val", rs.getString("val"));
				linshi_arr.add(linshi_obj);
			}
			stmt.close();
			rs.close();
			Calendar a = Calendar.getInstance();
			a.set(Calendar.YEAR, Integer.parseInt(year));
			a.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			a.set(Calendar.DATE, 1);
			a.roll(Calendar.DATE, -1);
			int tianshu = a.get(Calendar.DATE);
			int d = 1;
			for (int i = 1; i <= tianshu; i++) {
				String day;
				if (d<10) {
					day = "0"+d;
				}else {
					day = String.valueOf(d);
				}
				String fhzb_date = date + "-" + day;
				content_obj = new JSONObject();
				content_obj.put("hysj_date", fhzb_date);
				
				for (int j = 0; j < linshi_arr.size(); j++) {
					if (fhzb_date.equals(linshi_arr.getJSONObject(j).getString("hysj_date"))) {
						JSONArray val = new JSONArray();
						val = linshi_arr.getJSONObject(j).getJSONArray("val");
						
						for (int j2 = 0; j2 < val.size(); j2++) {
							String xm_name = linshi_arr.getJSONObject(j).getString("xm_name");
							String zbbh = val.getJSONObject(j2).getString("sczb_bh");
							int xsdws = 0;
							for (int k = 0; k < title_arr.size(); k++) {
								if (title_arr.getJSONObject(k).getString("xm_name").equals(xm_name) && title_arr.getJSONObject(k).getString("zbbh").equals(zbbh)) {
									xsdws = Integer.parseInt(title_arr.getJSONObject(k).getString("xsdws"));
									break;
								}
							}
							String str_val = val.getJSONObject(j2).getString("val");
							if (!str_val.equals("")) {
								df.setMaximumFractionDigits(xsdws);
								str_val = df.format(Double.parseDouble(str_val));
								str_val = str_val.replace(",", "");
								df.setMinimumFractionDigits(xsdws);
								str_val = df.format(Double.parseDouble(str_val));
								str_val = str_val.replace(",", "");
							}
							content_obj.put(xm_name+zbbh, str_val);
						}
						
						//content_obj.put(linshi_arr.getJSONObject(j).getString("fhzb_id"), linshi_arr.getJSONObject(j).getString("fhzb_val"));
					}
				}
				content_arr.add(content_obj);
				d++;
			}
			jsonArray.add(content_arr);
			
			DB.close(conn);
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
