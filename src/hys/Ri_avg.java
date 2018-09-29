package hys;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*计算日平均*/
public class Ri_avg {
	public static JSONArray ripingjun(String xm_id, String hysj_date) {
		Connection conn = null;// 定义为空值
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		JSONArray jsonArray = new JSONArray();

		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			sql = "SELECT val FROM sczb_ban_avg WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			JSONObject dataObj = new JSONObject();
			JSONArray dataArr = new JSONArray();
			while (rs.next()) {
				dataArr.add(rs.getString("val"));
			}

			DB.close(conn);
			stmt.close();
			rs.close();
			if (dataArr.size() == 0) {
				return null;
			} else {

				for (int i = 0; i < dataArr.getJSONArray(0).size(); i++) {// 项目有多少个指标
					int n = 0;// 计算平均的个数
					double avg;
					double val = 0;
					String zbbh = dataArr.getJSONArray(0).getJSONObject(i).getString("sczb_bh");// 需要查找的指标编号
					dataObj.put("sczb_bh", zbbh);// 获得指标编号
					dataObj.put("sczb_name", dataArr.getJSONArray(0).getJSONObject(i).getString("sczb_name"));// 获得指标名称

					for (int j = 0; j < dataArr.size(); j++) {// 根据指标编号查找数据,j为几批数据
						for (int j2 = 0; j2 < dataArr.getJSONArray(0).size(); j2++) {// j2为指标个数
							String zbbh1 = dataArr.getJSONArray(j).getJSONObject(j2).getString("sczb_bh");
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
		} catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		// System.out.println(jsonArray);
		return jsonArray;// 返回平均json数组

	}
}
