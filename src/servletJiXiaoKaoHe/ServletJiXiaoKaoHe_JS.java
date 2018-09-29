package servletJiXiaoKaoHe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletJiXiaoKaoHe_JS
 */
@WebServlet("/ServletJiXiaoKaoHe_JS")
public class ServletJiXiaoKaoHe_JS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_JS() {
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
	    PreparedStatement ps = null;
		String sql = null;
		PrintWriter out =response.getWriter();
		String date=request.getParameter("date");
		
		JSONObject gl_obj = new JSONObject();//JSON对象
		JSONArray gl_arr = new JSONArray(); //JSON数组
		
		try {
			//删除绩效考核数据表指定数据
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement stmt = conn.createStatement();//创建Statement对象
			sql = "DELETE FROM jxkh_result WHERE date='"+date+"'";
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);//删除数据 
			stmt.close();
			//获得绩效管理列表
			sql = "SELECT\n" +
					"jxkh_gl.jxkhgl_id,\n" +
					"jxkh_gl.pxid,\n" +
					"jxkh_gl.cj,\n" +
					"jxkh_gl.khxm,\n" +
					"jxkh_gl.sfdz\n" +
					"FROM\n" +
					"jxkh_gl\n" +
					"ORDER BY\n" +
					"jxkh_gl.pxid ASC";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				
				gl_obj.put("jxkhgl_id", rs.getString("jxkhgl_id"));
				gl_obj.put("pxid", rs.getString("pxid"));
				gl_obj.put("cj", rs.getString("cj"));
				gl_obj.put("khxm_name", rs.getString("khxm"));
				gl_obj.put("sfdz", rs.getString("sfdz"));
				
				gl_arr.add(gl_obj);
			}
			stmt.close();
			rs.close();
			//System.out.println(gl_arr.size());
			
			for (int i = 0; i < gl_arr.size(); i++) {//循环遍历绩效管理列表数据
				String jxkhgl_id = gl_arr.getJSONObject(i).getString("jxkhgl_id");//考核管理id
				String cj = gl_arr.getJSONObject(i).getString("cj");//车间
				String khxm_name = gl_arr.getJSONObject(i).getString("khxm_name");//考核项目名称
				String khfs = null;//考核方式
				JSONObject dz_obj = new JSONObject();//JSON对象
				JSONArray dz_arr = new JSONArray(); //JSON数组
				sql = "SELECT\n" +   //查绩效订制数据
						"jxkh_dz.jxkhdz_id,\n" +
						"jxkh_dz.jxkhgl_id,\n" +
						"jxkh_dz.sjkxm_id,\n" +
						"jxkh_dz.sjkxm_name,\n" +
						"jxkh_dz.sjkzb_name,\n" +
						"jxkh_dz.sjkzb_bh,\n" +
						"jxkh_dz.start_date,\n" +
						"jxkh_dz.start_time,\n" +
						"jxkh_dz.end_date,\n" +
						"jxkh_dz.end_time,\n" +
						"jxkh_dz.khfs,\n" +
						"jxkh_dz.khbz,\n" +
						"jxkh_dz.sjclass\n" +
						"FROM\n" +
						"jxkh_dz\n" +
						"WHERE\n" +
						"jxkh_dz.jxkhgl_id = "+jxkhgl_id+"\n" +
						"ORDER BY\n" +
						"jxkh_dz.sjkxm_name ASC,\n" +
						"jxkh_dz.start_date ASC";
				
				stmt = conn.createStatement();//创建Statement对象
				rs = stmt.executeQuery(sql);
				while(rs.next()){
					
					dz_obj.put("jxkhdz_id", rs.getString("jxkhdz_id"));
					dz_obj.put("jxkhgl_id", rs.getString("jxkhgl_id"));
					dz_obj.put("sjkxm_id", rs.getString("sjkxm_id"));
					dz_obj.put("sjkxm_name", rs.getString("sjkxm_name"));
					dz_obj.put("sjkzb_name", rs.getString("sjkzb_name"));
					dz_obj.put("sjkzb_bh", rs.getString("sjkzb_bh"));
					dz_obj.put("start_date", rs.getString("start_date"));
					dz_obj.put("start_time", rs.getString("start_time"));
					dz_obj.put("end_date", rs.getString("end_date"));
					dz_obj.put("end_time", rs.getString("end_time"));
					/*if (rs.getString("khfs").equals("1")) {
						dz_obj.put("khfs", "个数");
					} else if (rs.getString("khfs").equals("2")) {
						dz_obj.put("khfs", "合格率");
					} else if (rs.getString("khfs").equals("3")) {
						dz_obj.put("khfs", "平均值");
					}*/
					dz_obj.put("khfs", rs.getString("khfs"));
					dz_obj.put("khbz", rs.getString("khbz"));
					dz_obj.put("sjclass", rs.getString("sjclass"));
					
					dz_arr.add(dz_obj);
				}
				stmt.close();
				rs.close();
				//System.out.println(dz_arr);
				int gs = 0;//个数
				int zgs = 0;//总个数
				String hgl = null;
				String avg = null;
				for (int j = 0; j < dz_arr.size(); j++) {//循环遍历订制数据列表
					ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
					String sjkxm_id = dz_arr.getJSONObject(j).getString("sjkxm_id");//项目id
					String sjkzb_bh = dz_arr.getJSONObject(j).getString("sjkzb_bh");//指标编号
					String start_date = dz_arr.getJSONObject(j).getString("start_date");//开始日期
					String start_time = dz_arr.getJSONObject(j).getString("start_time");//开始时间
					String end_date = dz_arr.getJSONObject(j).getString("end_date");//结束日期
					String end_time = dz_arr.getJSONObject(j).getString("end_time");//结束时间
					khfs = dz_arr.getJSONObject(j).getString("khfs");//考核方式
					String khbz = dz_arr.getJSONObject(j).getString("khbz");//考核标准
					String sjclass = dz_arr.getJSONObject(j).getString("sjclass");//数据类型
					String khbz_fs = null;//考核标准的方式，区间或者区间外或者大于小于等
					String start_bz = null;//区间的开始数据标准
					String end_bz = null;//区间的结束数据标准
					//System.out.println(khbz);
					if (khbz.indexOf("-") != -1 && khbz.indexOf("!") != -1) {//考核标准为区间外
						khbz_fs = "qujianwai";
						String[] temp;
						temp = khbz.split("-");
						start_bz = temp[0].substring(0,0)+temp[0].substring(0+1);
						end_bz = temp[1];
						//System.out.println(start_bz);
						//System.out.println(end_bz);
					} else if (khbz.indexOf("-") != -1) {//考核标准为区间
						khbz_fs = "qujian";
						String[] temp;
						temp = khbz.split("-");
						start_bz = temp[0];
						end_bz = temp[1];
					}
					
					if (khfs.equals("1")) {//个数
						if (sjclass.equals("一般数据")) {//一般数据
							sql = "SELECT\n" +
									"sczb_data.sczb_data\n" +
									"FROM\n" +
									"sczb_data\n" +
									"WHERE\n" +
									"CONCAT(hysj_date,' ',hysjd_time) BETWEEN '"+start_date+" "+start_time+"' AND '"+end_date+" "+end_time+"' AND\n" +
									"sczb_data.xm_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								JSONObject sj_obj = new JSONObject();//JSON对象
								sj_obj.put("sczb_data", rs.getString("sczb_data"));
								/*System.out.println(khxm_name);
								System.out.println(sj_obj.getJSONArray("sczb_data"));*/
								//System.out.println(sj_obj.getJSONArray("sczb_data").size());
								for (int k = 0; k < sj_obj.getJSONArray("sczb_data").size(); k++) {
									if (sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_bh").equals(sjkzb_bh)) {//找到指标编号
										if (sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_value").equals("")) {
											continue;
										}
										double sj = Double.parseDouble(sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_value"));
										//System.out.println(sj);
										if (khbz_fs == null) {//普通考核标准
											if ((boolean) jse.eval(String.valueOf(sj)+khbz)) {
												gs++;
											}
										} else if (khbz_fs.equals("qujian")) {//区间考核标准
											if (Double.doubleToLongBits(sj) >= Double.doubleToLongBits(Double.parseDouble(start_bz)) && Double.doubleToLongBits(sj) <= Double.doubleToLongBits(Double.parseDouble(end_bz))) {
												gs++;
											}
										} else if (khbz_fs.equals("qujianwai")) {//区间外考核标准
											if (Double.doubleToLongBits(sj) < Double.doubleToLongBits(Double.parseDouble(start_bz)) || Double.doubleToLongBits(sj) > Double.doubleToLongBits(Double.parseDouble(end_bz))) {
												gs++;
											}
										}
									}
								}
							}
							stmt.close();
							rs.close();
						} else {//复合数据
							sql = "SELECT\n" +
									"fhzb_data.fhzb_val\n" +
									"FROM\n" +
									"fhzb_data\n" +
									"WHERE\n" +
									"fhzb_date BETWEEN '"+start_date+"' AND '"+end_date+"' AND\n" +
									"fhzb_data.fhzb_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								String sj = rs.getString("fhzb_val");
								if (!sj.equals("")) {//数据不为空
									if (khbz_fs == null) {//普通考核标准
										if ((boolean) jse.eval(sj+khbz)) {
											gs++;
										}
									} else if (khbz_fs.equals("qujian")) {//区间考核标准
										if (Double.doubleToLongBits(Double.parseDouble(sj)) >= Double.doubleToLongBits(Double.parseDouble(start_bz)) && Double.doubleToLongBits(Double.parseDouble(sj)) <= Double.doubleToLongBits(Double.parseDouble(end_bz))) {
											gs++;
										}
									} else if (khbz_fs.equals("qujianwai")) {//区间外考核标准
										if (Double.doubleToLongBits(Double.parseDouble(sj)) < Double.doubleToLongBits(Double.parseDouble(start_bz)) || Double.doubleToLongBits(Double.parseDouble(sj)) > Double.doubleToLongBits(Double.parseDouble(end_bz))) {
											gs++;
										}
									}
								}
							}
							stmt.close();
							rs.close();
						}
					} else if (khfs.equals("2")) {//合格率
						if (sjclass.equals("一般数据")) {//一般数据
							sql = "SELECT\n" +
									"sczb_data.sczb_data\n" +
									"FROM\n" +
									"sczb_data\n" +
									"WHERE\n" +
									"CONCAT(hysj_date,' ',hysjd_time) BETWEEN '"+start_date+" "+start_time+"' AND '"+end_date+" "+end_time+"' AND\n" +
									"sczb_data.xm_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								JSONObject sj_obj = new JSONObject();//JSON对象
								sj_obj.put("sczb_data", rs.getString("sczb_data"));
								/*System.out.println(khxm_name);
								System.out.println(sj_obj.getJSONArray("sczb_data"));*/
								//System.out.println(sj_obj.getJSONArray("sczb_data").size());
								for (int k = 0; k < sj_obj.getJSONArray("sczb_data").size(); k++) {
									if (sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_bh").equals(sjkzb_bh)) {//找到指标编号
										if (sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_value").equals("")) {
											continue;
										}
										zgs++;
										double sj = Double.parseDouble(sj_obj.getJSONArray("sczb_data").getJSONObject(k).getString("sczb_value"));
										//System.out.println(sj);
										if (khbz_fs == null) {//普通考核标准
											if ((boolean) jse.eval(String.valueOf(sj)+khbz)) {
												gs++;
											}
										} else if (khbz_fs.equals("qujian")) {//区间考核标准
											if (Double.doubleToLongBits(sj) >= Double.doubleToLongBits(Double.parseDouble(start_bz)) && Double.doubleToLongBits(sj) <= Double.doubleToLongBits(Double.parseDouble(end_bz))) {
												gs++;
											}
										} else if (khbz_fs.equals("qujianwai")) {//区间外考核标准
											if (Double.doubleToLongBits(sj) < Double.doubleToLongBits(Double.parseDouble(start_bz)) || Double.doubleToLongBits(sj) > Double.doubleToLongBits(Double.parseDouble(end_bz))) {
												gs++;
											}
										}
									}
								}
							}
							stmt.close();
							rs.close();
						} else {//复合数据
							sql = "SELECT\n" +
									"fhzb_data.fhzb_val\n" +
									"FROM\n" +
									"fhzb_data\n" +
									"WHERE\n" +
									"fhzb_date BETWEEN '"+start_date+"' AND '"+end_date+"' AND\n" +
									"fhzb_data.fhzb_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								String sj = rs.getString("fhzb_val");
								zgs++;
								if (!sj.equals("")) {//数据不为空
									if (khbz_fs == null) {//普通考核标准
										if ((boolean) jse.eval(sj+khbz)) {
											gs++;
										}
									} else if (khbz_fs.equals("qujian")) {//区间考核标准
										if (Double.doubleToLongBits(Double.parseDouble(sj)) >= Double.doubleToLongBits(Double.parseDouble(start_bz)) && Double.doubleToLongBits(Double.parseDouble(sj)) <= Double.doubleToLongBits(Double.parseDouble(end_bz))) {
											gs++;
										}
									} else if (khbz_fs.equals("qujianwai")) {//区间外考核标准
										if (Double.doubleToLongBits(Double.parseDouble(sj)) < Double.doubleToLongBits(Double.parseDouble(start_bz)) || Double.doubleToLongBits(Double.parseDouble(sj)) > Double.doubleToLongBits(Double.parseDouble(end_bz))) {
											gs++;
										}
									}
								}
							}
							stmt.close();
							rs.close();
						}
					} else if (khfs.equals("3")) {//平均值
						if (sjclass.equals("一般数据")) {//一般数据
							sql = "SELECT\n" +
									"sczb_yue_avg.val\n" +
									"FROM\n" +
									"sczb_yue_avg\n" +
									"WHERE\n" +
									"sczb_yue_avg.hysj_date = '"+start_date.substring(0, 7)+"' AND\n" +
									"sczb_yue_avg.xm_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								JSONObject sj_obj = new JSONObject();//JSON对象
								sj_obj.put("val", rs.getString("val"));
								for (int k = 0; k < sj_obj.getJSONArray("val").size(); k++) {
									if (sj_obj.getJSONArray("val").getJSONObject(k).getString("sczb_bh").equals(sjkzb_bh)) {
										avg = sj_obj.getJSONArray("val").getJSONObject(k).getString("val");
									}
								}
							}
							stmt.close();
							rs.close();
						} else {//复合数据
							sql = "SELECT\n" +
									"fhzb_yue_avg.fhzb_val\n" +
									"FROM\n" +
									"fhzb_yue_avg\n" +
									"WHERE\n" +
									"fhzb_yue_avg.fhzb_date = '"+start_date.substring(0, 7)+"' AND\n" +
									"fhzb_yue_avg.fhzb_id = "+sjkxm_id;
							stmt = conn.createStatement();//创建Statement对象
							rs = stmt.executeQuery(sql);
							while(rs.next()){
								avg = rs.getString("fhzb_val");
							}
							stmt.close();
							rs.close();
						}
					
					}
				}
				
				sql = "INSERT INTO jxkh_result (jxkhgl_id, cj, khxm, date, result) VALUES (?,?,?,?,?)"; //插入数据
				if (khfs.equals("1")) {//个数
					ps = conn.prepareStatement(sql);
					ps.setString(1, jxkhgl_id);
					ps.setString(2, cj);
					ps.setString(3, khxm_name);
					ps.setString(4, date);
					ps.setString(5, String.valueOf(gs));
					int n = ps.executeUpdate();
					ps.close();
					if (n==0) {
						DB.close(conn);
						break;
					}
					/*System.out.println(khxm_name);
					System.out.println(gs);*/
				} else if (khfs.equals("2")) {//合格率
					DecimalFormat df = new DecimalFormat();
					df.setGroupingUsed(false);//不以逗号分隔
					df.setMaximumFractionDigits(2);
					hgl = df.format((float)gs/zgs*100);
					df.setMinimumFractionDigits(2);
					ps = conn.prepareStatement(sql);
					ps.setString(1, jxkhgl_id);
					ps.setString(2, cj);
					ps.setString(3, khxm_name);
					ps.setString(4, date);
					ps.setString(5, hgl+"%");
					int n = ps.executeUpdate();
					ps.close();
					if (n==0) {
						DB.close(conn);
						break;
					}
					/*System.out.println(khxm_name);
					System.out.println(hgl+"%");*/
				} else if (khfs.equals("3")) {//平均值
					try {
						DecimalFormat df = new DecimalFormat();
						df.setGroupingUsed(false);//不以逗号分隔
						df.setMaximumFractionDigits(3);
						avg = df.format(Double.parseDouble(avg));
						df.setMinimumFractionDigits(3);
						ps = conn.prepareStatement(sql);
						ps.setString(1, jxkhgl_id);
						ps.setString(2, cj);
						ps.setString(3, khxm_name);
						ps.setString(4, date);
						ps.setString(5, avg);
						int n = ps.executeUpdate();
						ps.close();
						if (n==0) {
							DB.close(conn);
							break;
						}
					} catch (Exception e) {
						// 如果平均值为空，输出“没有数据”
						ps = conn.prepareStatement(sql);
						ps.setString(1, jxkhgl_id);
						ps.setString(2, cj);
						ps.setString(3, khxm_name);
						ps.setString(4, date);
						ps.setString(5, "没有数据");
						int n = ps.executeUpdate();
						ps.close();
						if (n==0) {
							DB.close(conn);
							break;
						}
					}
					/*System.out.println(khxm_name);
					System.out.println(avg);*/
				}
			}
			out.print(true);
			DB.close(conn);
		} catch (SQLException e) {
			System.out.println(e);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
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
