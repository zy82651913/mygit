package hys;

import java.awt.image.AreaAveragingScaleFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.*;
import java.time.chrono.MinguoChronology;
import java.util.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JSeparator;

//import org.eclipse.jdt.internal.compiler.parser.ParserBasicInformation;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FuHeShuJu_ShengCheng {
	
	public static void Fhsj_ShengCheng(String date,int bc) {
		Connection conn = null;// 定义为空值
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement stmt = null;
		DecimalFormat df = new DecimalFormat();
		ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine se = manager.getEngineByName("js");
		String sql;
		
		/*查询全部复合指标数据*/
		sql = "SELECT\n" +
				"fhzbgl.fhzb_name,\n" +
				"fhzbgl.fhzb_bh,\n" +
				"fhzbgl.fhzb_math,\n" +
				"fhzbgl.xsws,\n" +
				"fhzbgl.id\n" +
				"FROM\n" +
				"fhzbgl\n" +
				"ORDER BY\n" +
				"fhzbgl.pxid ASC";
		JSONObject fhzb_sj_obj = new JSONObject();
		JSONArray fhzb_sj_arr = new JSONArray();
		try {
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();//创建Statement对象
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				fhzb_sj_obj.put("id", rs.getString("id"));
				fhzb_sj_obj.put("fhzb_name", rs.getString("fhzb_name"));
				fhzb_sj_obj.put("fhzb_bh", rs.getString("fhzb_bh"));
				fhzb_sj_obj.put("fhzb_math", rs.getString("fhzb_math"));
				fhzb_sj_obj.put("xsws", rs.getString("xsws"));
				fhzb_sj_arr.add(fhzb_sj_obj);
			}
			rs.close();
			stmt.close();
			//遍历出复合数据
			for (int i = 0; i < fhzb_sj_arr.size(); i++) {
				String xmid;//项目id
				String laiyuan_name;//来源指标名称
				String fhzb_id;//复合指标名称
				String fhzb_name;//复合指标名称
				String zbbh;//指标编号
				String fhzb_math;//复合指标公式
				String xsws;//小数点位数
				String fhzb_val;//最终的复合指标数据结果
				JSONObject fhzb_lysj_obj = new JSONObject();
				JSONArray fhzb_lysj_arr = new JSONArray();
				JSONObject jclysj_obj = new JSONObject();
				JSONArray jclysj_arr = new JSONArray();
				try {
					fhzb_id = fhzb_sj_arr.getJSONObject(i).getString("id");
					fhzb_name = fhzb_sj_arr.getJSONObject(i).getString("fhzb_name");
					fhzb_math = fhzb_sj_arr.getJSONObject(i).getString("fhzb_math");
					xsws = fhzb_sj_arr.getJSONObject(i).getString("xsws");
				} catch (Exception e) {
					continue;
				}
				
				if (fhzb_sj_arr.getJSONObject(i).getString("fhzb_math").equals("")) {//如果该复合指标没有定义公式，跳过这次循环
					continue;
				}else {
					//根据复合指标id查出复合指标来源数据
					String id = fhzb_sj_arr.getJSONObject(i).getString("id");
					sql = "SELECT\n" +
							"fhzb_laiyuan.xmid,\n" +
							"fhzb_laiyuan.sc_bh,\n" +
							"fhzb_laiyuan.laiyuan_name,\n" +
							"fhzb_laiyuan.zhbc\n" +
							"FROM\n" +
							"fhzb_laiyuan\n" +
							"WHERE\n" +
							"fhzb_laiyuan.fhzb_id = '"+id+"'";
					stmt = conn.createStatement();//创建Statement对象
					rs = stmt.executeQuery(sql);
					while(rs.next()){
						fhzb_lysj_obj.put("xmid", rs.getString("xmid"));
						fhzb_lysj_obj.put("sc_bh", rs.getString("sc_bh"));
						fhzb_lysj_obj.put("laiyuan_name", rs.getString("laiyuan_name"));
						fhzb_lysj_obj.put("zhbc", rs.getString("zhbc"));
						fhzb_lysj_arr.add(fhzb_lysj_obj);
					}
					rs.close();
					stmt.close();
					//根据日期，班次，项目id，输出指标编号，滞后班次获取复合指标来源
					for (int j = 0; j < fhzb_lysj_arr.size(); j++) {
						laiyuan_name = fhzb_lysj_arr.getJSONObject(j).getString("laiyuan_name");
						xmid = fhzb_lysj_arr.getJSONObject(j).getString("xmid");
						zbbh = fhzb_lysj_arr.getJSONObject(j).getString("sc_bh");
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Calendar ca = Calendar.getInstance();
						Date date2 = format.parse(date,new ParsePosition(0));
						ca.setTime(date2);
						String enddate = null;
						int bc1 = 0;
						//转换字符串格式为date格式
						int zhbc = Integer.parseInt(fhzb_lysj_arr.getJSONObject(j).getString("zhbc"));
						int v = (Math.abs((zhbc-bc)/3)+1)*-1;
						
						if ((bc - zhbc) > 0) {//项目班次减去滞后班次
							bc1 = bc-zhbc;
							enddate = format.format(date2);
						} else if ((bc - zhbc) == 0) {
							bc1 = 3;
							ca.add(Calendar.DATE, v);// num为增加的天数，可以改变的
							date2 = ca.getTime();
							enddate = format.format(date2);
						} else if ((bc-zhbc) < 0) {
							bc1 = 3-(Math.abs(bc-zhbc)%3);
							ca.add(Calendar.DATE, v);// num为增加的天数，可以改变的
							date2 = ca.getTime();
							enddate = format.format(date2);
						}
						
						sql = "SELECT\n" +
								"sczb_ban_avg.val\n" +
								"FROM\n" +
								"sczb_ban_avg\n" +
								"WHERE\n" +
								"sczb_ban_avg.xm_id = '"+xmid+"' AND\n" +
								"sczb_ban_avg.hysj_date = '"+enddate+"' AND\n" +
								"sczb_ban_avg.hy_bc = '"+bc1+"'";
						stmt = conn.createStatement();//创建Statement对象
						rs = stmt.executeQuery(sql);
						if (rs.next()) {
							rs.previous();//指针上移一位
							while(rs.next()){
								jclysj_obj.put("laiyuan_name", fhzb_lysj_arr.getJSONObject(j).getString("laiyuan_name"));
								JSONObject jo = new JSONObject();
								jo.put("val", rs.getString("val"));
								for (int k = 0; k < jo.getJSONArray("val").size(); k++) {//查出指标编号对应的值
									if (jo.getJSONArray("val").getJSONObject(k).getString("sczb_bh").equals(zbbh)) {
										jclysj_obj.put("val", jo.getJSONArray("val").getJSONObject(k).getString("val"));
									}
								}
								jclysj_arr.add(jclysj_obj);
							}
						} else {
							jclysj_obj.put("laiyuan_name", fhzb_lysj_arr.getJSONObject(j).getString("laiyuan_name"));
							jclysj_obj.put("val", "");
							jclysj_arr.add(jclysj_obj);
						}
						
						rs.close();
						stmt.close();
					}
				}
				//复合指标计算
				int index = 0;
				while( ( index = fhzb_math.indexOf("avg(", index) ) != -1 ){//先替换公式中的平均
					JSONArray ja = new JSONArray();
					int a = 0;//找到平均后的数学计算符号的位置
						if (fhzb_math.indexOf("+", index) != -1) {
							a = fhzb_math.indexOf("+", index);
							ja.add(a);
						}else if (fhzb_math.indexOf("-", index) != -1) {
							a = fhzb_math.indexOf("-", index);
							ja.add(a);
						}else if (fhzb_math.indexOf("*", index) != -1) {
							a = fhzb_math.indexOf("*", index);
							ja.add(a);
						}else if (fhzb_math.indexOf("/", index) != -1) {
							a = fhzb_math.indexOf("/", index);
							ja.add(a);
						}else {
							a = fhzb_math.length();
						}
					//JSONArray sArray = new JSONArray();
					String str = fhzb_math.substring(index, a);
					//String string = str.substring(4,str.length()-1);
					
					
					double n = 0;//平均个数
					double sum = 0;//数据的和
					double number = 0;
					for (int k = 0; k < jclysj_arr.size(); k++) {//计算平均
						//System.out.println(jclysj_arr.getJSONObject(k).getString("laiyuan_name"));
						if (str.indexOf(jclysj_arr.getJSONObject(k).getString("laiyuan_name")) != -1) {
							try {
								number = Double.parseDouble(jclysj_arr.getJSONObject(k).getString("val"));
								sum+=number;
								n++;
							} catch (Exception e) {
								
							}
						}
					}
					double avg = sum/n;
					fhzb_math = fhzb_math.replace(str, Double.toString(avg));
					index = index + 1;
				}
				//替换完平均后，替换其他元素
				for (int k = 0; k < jclysj_arr.size(); k++) {
					if (fhzb_math.indexOf(jclysj_arr.getJSONObject(k).getString("laiyuan_name"), index) != -1) {
						try {
							double number = Double.parseDouble(jclysj_arr.getJSONObject(k).getString("val"));
							fhzb_math = fhzb_math.replace(jclysj_arr.getJSONObject(k).getString("laiyuan_name"), Double.toString(number));
						} catch (Exception e) {
							fhzb_math = "";
						}
					}
				}
				//最终公式的计算
				try {
					df.setGroupingUsed(false);//不以逗号分隔
					df.setMaximumFractionDigits(Integer.parseInt(xsws));
					fhzb_val = df.format((Double)se.eval(fhzb_math));//最终的计算结果
					df.setMinimumFractionDigits(Integer.parseInt(xsws));
					fhzb_val = df.format(Double.parseDouble(fhzb_val));//最终的计算结果
				} catch (Exception e) {
					fhzb_val = "";
				}
				//保存更新操作复合指标数据
				int n = 0;
				int id; 
				sql = "SELECT\n" +
						"fhzb_data.id\n" +
						"FROM\n" +
						"fhzb_data\n" +
						"WHERE\n" +
						"fhzb_data.fhzb_id = '"+fhzb_id+"' AND\n" +
						"fhzb_data.fhzb_date = '"+date+"' AND\n" +
						"fhzb_data.fhzb_bc = "+bc+"";
				stmt = conn.createStatement();//创建Statement对象
				rs = stmt.executeQuery(sql);
				if (rs.next()) {//判断是否有数据，如果有更新
					rs.previous();//指针上移一位
					while(rs.next()){
						id = rs.getInt("id");
						sql = "UPDATE fhzb_data SET fhzb_id=?,fhzb_name=?,fhzb_date=?,fhzb_bc=?,fhzb_val=? WHERE id=?"; //修改数据
						ps = conn.prepareStatement(sql);
						ps.setString(1, fhzb_id);
						ps.setString(2, fhzb_name);
						ps.setString(3, date);
						ps.setInt(4, bc);
						ps.setString(5, fhzb_val);
						ps.setInt(6, id);
						ps.executeUpdate();
					}
				} else {
					sql = "INSERT INTO fhzb_data (fhzb_id, fhzb_name, fhzb_date, fhzb_bc, fhzb_val) VALUES (?,?,?,?,?)"; //插入数据
					ps = conn.prepareStatement(sql);
					ps.setString(1, fhzb_id);
					ps.setString(2, fhzb_name);
					ps.setString(3, date);
					ps.setInt(4, bc);
					ps.setString(5, fhzb_val);
					ps.executeUpdate();
				}
				rs.close();
				stmt.close();
				ps.close();
				//计算复合数据月平均
				String year = date.substring(0, 4);// 提取年
				String month = date.substring(5, 7);// 提取月
				String ym = date.substring(0, 7);// 提取年月
				sql = "SELECT\n" +
						"fhzb_data.fhzb_id,\n" +
						"fhzb_data.fhzb_name,\n" +
						"fhzb_data.fhzb_date,\n" +
						"fhzb_data.fhzb_bc,\n" +
						"fhzb_data.fhzb_val\n" +
						"FROM\n" +
						"fhzb_data\n" +
						"WHERE\n" +
						"fhzb_data.fhzb_id = '"+fhzb_id+"' AND\n" +
						"year(fhzb_date) = '"+year+"' AND\n" +
						"month(fhzb_date) = '"+month+"'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				JSONArray dataArr = new JSONArray();
				while (rs.next()) {
					dataArr.add(rs.getString("fhzb_val"));
				}
				String avg = "";
				if (dataArr.size() != 0) {
					double zbgs = 0;//指标的个数,不计算空值
					double sum = 0;//指标的和
					for (int j = 0; j < dataArr.size(); j++) {
						if (!dataArr.get(j).equals("")) {
							sum +=Double.parseDouble(dataArr.getString(j));
							zbgs++;
						}
					}
					avg = Double.toString(sum/zbgs);
					if (avg.equals("NaN")) {
						avg = "";
					}
				}else {
					avg = "";
				}
				try {
					df.setGroupingUsed(false);//不以逗号分隔
					df.setMaximumFractionDigits(Integer.parseInt(xsws));
					avg = df.format((Double)se.eval(avg));//最终的计算结果
					df.setMinimumFractionDigits(Integer.parseInt(xsws));
					avg = df.format(Double.parseDouble(avg));//最终的计算结果
				} catch (Exception e) {
					fhzb_val = "";
				}
				sql = "UPDATE fhzb_yue_avg SET fhzb_id='" + fhzb_id + "',fhzb_name='" + fhzb_name + "',fhzb_date='" + ym
						+ "',fhzb_val='" + avg + "' WHERE fhzb_id='" + fhzb_id + "' AND fhzb_date='" + ym + "'";
				int nn = 0;
				nn = stmt.executeUpdate(sql);
				if (nn == 0) {
					sql = "INSERT INTO fhzb_yue_avg (fhzb_id, fhzb_name, fhzb_date, fhzb_val) VALUES (?,?,?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, fhzb_id);
					ps.setString(2, fhzb_name);
					ps.setString(3, ym);
					ps.setString(4, fhzb_val);
					ps.executeUpdate();
				}
				rs.close();
				
				//计算调度复合数据月平均（从上个月最后一天开始，当月倒数第二天结束）
				String str_date = year + "-" + month + "-" + "01";
				/*获得上一月最后一天的日期*/
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String start_date = null;
				String end_date = null;
				try {
					Date dt = sdf.parse(str_date);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(dt);
					rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期减2天
					start_date = sdf.format(rightNow.getTime());
					//System.out.println(start_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*获得当前月倒数第二天的日期*/
				try {
					Date dt = sdf.parse(str_date);
					Calendar rightNow = Calendar.getInstance();
					rightNow.setTime(dt);
					rightNow.add(Calendar.MONTH,1);//日期加1个月
					rightNow.add(Calendar.DAY_OF_YEAR,-2);//日期减2天
					end_date = sdf.format(rightNow.getTime());
					//System.out.println(end_date);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sql = "SELECT\n" +
						"fhzb_data.id,\n" +
						"fhzb_data.fhzb_id,\n" +
						"fhzb_data.fhzb_name,\n" +
						"fhzb_data.fhzb_date,\n" +
						"fhzb_data.fhzb_bc,\n" +
						"fhzb_data.fhzb_val\n" +
						"FROM\n" +
						"fhzb_data\n" +
						"WHERE\n" +
						"fhzb_data.fhzb_date BETWEEN '"+start_date+"' AND '"+end_date+"' AND\n" +
						"fhzb_data.fhzb_id = " + fhzb_id;
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				dataArr = new JSONArray();
				while (rs.next()) {
					dataArr.add(rs.getString("fhzb_val"));
				}
				avg = "";
				if (dataArr.size() != 0) {
					double zbgs = 0;//指标的个数,不计算空值
					double sum = 0;//指标的和
					for (int j = 0; j < dataArr.size(); j++) {
						if (!dataArr.get(j).equals("")) {
							sum +=Double.parseDouble(dataArr.getString(j));
							zbgs++;
						}
					}
					avg = Double.toString(sum/zbgs);
					if (avg.equals("NaN")) {
						avg = "";
					}
				}else {
					avg = "";
				}
				try {
					df.setGroupingUsed(false);//不以逗号分隔
					df.setMaximumFractionDigits(Integer.parseInt(xsws));
					avg = df.format((Double)se.eval(avg));//最终的计算结果
					df.setMinimumFractionDigits(Integer.parseInt(xsws));
					avg = df.format(Double.parseDouble(avg));//最终的计算结果
				} catch (Exception e) {
					fhzb_val = "";
				}
				sql = "UPDATE fhzb_yue_avg_dd SET fhzb_id='" + fhzb_id + "',fhzb_name='" + fhzb_name + "',fhzb_date='" + ym
						+ "',fhzb_val='" + avg + "' WHERE fhzb_id='" + fhzb_id + "' AND fhzb_date='" + ym + "'";
				nn = stmt.executeUpdate(sql);
				if (nn == 0) {
					sql = "INSERT INTO fhzb_yue_avg_dd (fhzb_id, fhzb_name, fhzb_date, fhzb_val) VALUES (?,?,?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, fhzb_id);
					ps.setString(2, fhzb_name);
					ps.setString(3, ym);
					ps.setString(4, fhzb_val);
					ps.executeUpdate();
				}
				rs.close();
				
				stmt.close();
				ps.close();
			}
			DB.close(conn);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
}
