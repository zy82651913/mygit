package hys;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DD_Yue_avg {
	public static JSONArray yuepingjun(String xm_id, String hysj_date) {
		Connection conn = null;// 定义为空值
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		JSONArray jsonArray = new JSONArray();
		String year = hysj_date.substring(0, 4);// 提取年
		String month = hysj_date.substring(5, 7);// 提取月
		String str_date = year + "-" + month + "-" + "01";
		/*获得上一月最后一天的日期*/
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String start_date = null;
		String end_date = null;
		try {
			Date dt = sdf.parse(str_date);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期减1天
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
			rightNow.add(Calendar.DAY_OF_YEAR,-2);//日期减1天
			end_date = sdf.format(rightNow.getTime());
			//System.out.println(end_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			sql = "SELECT\n" +
					"sczb_ri_avg.id,\n" +
					"sczb_ri_avg.xm_id,\n" +
					"sczb_ri_avg.xm_name,\n" +
					"sczb_ri_avg.hysj_date,\n" +
					"sczb_ri_avg.val\n" +
					"FROM\n" +
					"sczb_ri_avg\n" +
					"WHERE\n" +
					"sczb_ri_avg.hysj_date BETWEEN '"+start_date+"' AND '"+end_date+"' AND\n" +
					"sczb_ri_avg.xm_id = "+xm_id;
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			JSONObject dataObj = new JSONObject();
			JSONArray dataArr = new JSONArray();
			while (rs.next()) {
				dataArr.add(rs.getString("val"));
			}
			stmt.close();
			rs.close();
			DB.close(conn);
			

			if (dataArr.size() == 0) {
				return null;
			} else {
				int str = dataArr.getJSONArray(0).size();
				int zbgs = 0;//指标的个数
				int max_zb = 0;//最大指标在数组中的位置
				for (int i = 0; i < dataArr.size(); i++) {//取得最大的指标个数
					if (dataArr.getJSONArray(i).size()>zbgs) {
						zbgs = dataArr.getJSONArray(i).size();
						max_zb = i;
					}
				}
				for (int i = 0; i < zbgs; i++) {// 项目有多少个指标
					int n = 0;// 计算平均的个数
					double avg;
					double val = 0;
					String zbbh = dataArr.getJSONArray(max_zb).getJSONObject(i).getString("sczb_bh");// 需要查找的指标编号
					dataObj.put("sczb_bh", zbbh);// 获得指标编号
					dataObj.put("sczb_name", dataArr.getJSONArray(max_zb).getJSONObject(i).getString("sczb_name"));// 获得指标名称

					for (int j = 0; j < dataArr.size(); j++) {// 根据指标编号查找数据,j为几批数据
						for (int j2 = 0; j2 < zbgs; j2++) {// j2为指标个数
							String zbbh1 = null;
							try {
								zbbh1 = dataArr.getJSONArray(j).getJSONObject(j2).getString("sczb_bh");
							} catch (Exception e) {
								
							}
							if (zbbh.equals(zbbh1)) {
								if (dataArr.getJSONArray(j).getJSONObject(j2).getString("val").equals("")) {// 判断指标数据是否为空

								} else {
									val = val + Double
											.parseDouble(dataArr.getJSONArray(j).getJSONObject(j2).getString("val"));
									n++;
									break;
								}
							}
						}
					}

					try {
						avg = val / n;// 计算平均
						dataObj.put("val", avg);
						jsonArray.add(dataObj);
					} catch (Exception e) {
						dataObj.put("val", "");
						jsonArray.add(dataObj);
					}
				}
				// System.out.println(jsonArray);
			}

		} catch (SQLException e) {
			System.out.println(e);
		}
		return jsonArray;// 返回平均json数组
	}
}
