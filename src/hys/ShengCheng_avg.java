package hys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*批量生成班平均数据*/
public class ShengCheng_avg {

	public static void main(String[] args) {
		Connection conn = null;// 定义为空值
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement stmt = null;
		String sql;
		JSONArray hysj_dateArray = new JSONArray();
		JSONObject xmJsonObject = new JSONObject();
		JSONArray xmJsonArray = new JSONArray();
		/* 查询全部日期 */
		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			/* 查询全部日期 */
			sql = "SELECT DISTINCT\n" + "sczb_data.hysj_date\n" + "FROM\n" + "sczb_data";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				hysj_dateArray.add(rs.getString("hysj_date"));
			}
			/* 查询项目id和项目名称 */
			sql = "SELECT DISTINCT\n" + "sczb_data.xm_name,\n" + "sczb_data.xm_id\n" + "FROM\n" + "sczb_data";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				xmJsonObject.put("xm_id", rs.getString("xm_id"));
				xmJsonObject.put("xm_name", rs.getString("xm_name"));
				xmJsonArray.add(xmJsonObject);
			}
			/* 批量处理班平均 */
			for (int i = 0; i < hysj_dateArray.size(); i++) {// 日期
				for (int j = 0; j < xmJsonArray.size(); j++) {// 项目
					for (int j2 = 1; j2 < 4; j2++) {// 班次
						String xm_id = xmJsonArray.getJSONObject(j).getString("xm_id");
						String xm_name = xmJsonArray.getJSONObject(j).getString("xm_name");
						String hy_bc = Integer.toString(j2);
						String hysj_date = hysj_dateArray.getString(i);
						String avg;
						JSONArray a = Ban_avg.banpingjun(xm_id, xm_name, hy_bc, hysj_date);// 计算项目的平均值
						if (a != null) {
							avg = a.toString();
							sql = "UPDATE sczb_ban_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='"
									+ hysj_date + "',hy_bc='" + hy_bc + "',val='" + avg + "' WHERE xm_id='" + xm_id
									+ "' AND hysj_date='" + hysj_date + "' AND hy_bc='" + hy_bc + "'";
							int n = 0;
							n = stmt.executeUpdate(sql);
							if (n == 0) {
								sql = "INSERT INTO sczb_ban_avg (xm_id, xm_name, hysj_date, hy_bc, val) VALUES (?,?,?,?,?)";
								ps = conn.prepareStatement(sql);
								ps.setString(1, xm_id);
								ps.setString(2, xm_name);
								ps.setString(3, hysj_date);
								ps.setString(4, hy_bc);
								ps.setString(5, avg);
								ps.executeUpdate();
								ps.close();
							}
						}
					}
				}
			}
			/* 批量处理日平均 */
			for (int i = 0; i < hysj_dateArray.size(); i++) {// 日期
				for (int j = 0; j < xmJsonArray.size(); j++) {// 项目
					for (int j2 = 1; j2 < 4; j2++) {// 班次
						String xm_id = xmJsonArray.getJSONObject(j).getString("xm_id");
						String xm_name = xmJsonArray.getJSONObject(j).getString("xm_name");
						String hysj_date = hysj_dateArray.getString(i);
						String avg;
						JSONArray a = Ri_avg.ripingjun(xm_id, hysj_date);// 计算项目的平均值
						if (a != null) {
							avg = a.toString();
							sql = "UPDATE sczb_ri_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='"
									+ hysj_date + "',val='" + avg + "' WHERE xm_id='" + xm_id + "' AND hysj_date='"
									+ hysj_date + "'";
							int n = 0;
							n = stmt.executeUpdate(sql);
							if (n == 0) {
								sql = "INSERT INTO sczb_ri_avg (xm_id, xm_name, hysj_date, val) VALUES (?,?,?,?)";
								ps = conn.prepareStatement(sql);
								ps.setString(1, xm_id);
								ps.setString(2, xm_name);
								ps.setString(3, hysj_date);
								ps.setString(4, avg);
								ps.executeUpdate();
								ps.close();
							}
						}
					}
				}
			}

			/* 批量处理月平均 */
			for (int i = 0; i < hysj_dateArray.size(); i++) {// 日期
				for (int j = 0; j < xmJsonArray.size(); j++) {// 项目
					for (int j2 = 1; j2 < 4; j2++) {// 班次
						String xm_id = xmJsonArray.getJSONObject(j).getString("xm_id");
						String xm_name = xmJsonArray.getJSONObject(j).getString("xm_name");
						String hysj_date = hysj_dateArray.getString(i);

						String avg_yue;
						JSONArray aaa = Yue_avg.yuepingjun(xm_id, hysj_date);// 计算项目的平均值
						String ym = hysj_date.substring(0, 7);// 提取年月

						if (aaa != null) {
							avg_yue = aaa.toString();
							sql = "UPDATE sczb_yue_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='"
									+ ym + "',val='" + avg_yue + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym
									+ "'";
							int nn = 0;
							nn = stmt.executeUpdate(sql);
							if (nn == 0) {
								sql = "INSERT INTO sczb_yue_avg (xm_id, xm_name, hysj_date, val) VALUES (?,?,?,?)";
								ps = conn.prepareStatement(sql);
								ps.setString(1, xm_id);
								ps.setString(2, xm_name);
								ps.setString(3, ym);
								ps.setString(4, avg_yue);
								ps.executeUpdate();
							}
						}

					}
				}
			}

			DB.close(conn);
			stmt.close();
			rs.close();
			/*
			 * if (hysj_dateArray.size() == 0) { System.out.println("日期为空"); }
			 * else { System.out.println(hysj_dateArray); } if
			 * (xmJsonArray.size() == 0) { System.out.println("项目为空"); } else {
			 * System.out.println(xmJsonArray); }
			 */
			System.out.println(hysj_dateArray);
			System.out.println(xmJsonArray);
		} catch (SQLException e) {
			System.out.println(e);
		}

	}

}
