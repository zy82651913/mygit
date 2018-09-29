package servletSuJuLR;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.Ban_avg;
import hys.DB;
import hys.DD_Yue_avg;
import hys.Ri_avg;
import hys.Yue_avg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletSuChuZhiBiao_Save
 */
@WebServlet("/ServletSuChuZhiBiao_Save")
public class ServletSuChuZhiBiao_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletSuChuZhiBiao_Save() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		Connection conn = null;// 定义为空值
		PreparedStatement ps = null;
		ResultSet rs = null;
		Statement stmt = null;
		double val;// 计算后班平均的值
		String sql;
		String sc_data = request.getParameter("sc_data");
		JSONObject data = JSONObject.fromObject(sc_data);// 把前台传入的json对象字符串转为json对象
		String xm_id = data.getString("xm_id");
		String srzb_data_id = data.getString("srzb_data_id");
		String xm_name = data.getString("xm_name");
		String hysj_date = data.getString("hysj_date");
		String hysjd_time = data.getString("hysjd_time");
		String hy_bc = data.getString("hy_bc");
		String hy_zhbc = data.getString("hy_zhbc");
		String beizhu = data.getString("beizhu");
		String beizhu_name = data.getString("beizhu_name");
		String sczb_data = data.getString("sczb_data");
		String sczb_data_id = null;
		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			sql = "SELECT id FROM sczb_data WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date + "' AND hy_bc='"
					+ hy_bc + "' AND srzb_data_id='" + srzb_data_id + "' AND gx_zhbc='" + hy_zhbc + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next()) {// 如果查到了数据更新数据
				sql = "UPDATE sczb_data SET srzb_data_id=?,xm_id=?,xm_name=?,hysj_date=?,hysjd_time=?,gx_zhbc=?,hy_bc=?,sczb_data=?,beizhu=?,beizhu_name=? WHERE xm_id=? AND hysj_date=? AND srzb_data_id=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, srzb_data_id);
				ps.setString(2, xm_id);
				ps.setString(3, xm_name);
				ps.setString(4, hysj_date);
				ps.setString(5, hysjd_time);
				ps.setString(6, hy_zhbc);
				ps.setString(7, hy_bc);
				ps.setString(8, sczb_data);
				ps.setString(9, beizhu);
				ps.setString(10, beizhu_name);
				ps.setString(11, xm_id);
				ps.setString(12, hysj_date);
				ps.setString(13, srzb_data_id);
				ps.executeUpdate();
				stmt.close();
				rs.close();
				sql = "SELECT id FROM sczb_data WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date
						+ "' AND hy_bc='" + hy_bc + "' AND srzb_data_id='" + srzb_data_id + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					sczb_data_id = rs.getString("id");
				}
				out.print(sczb_data_id);
			} else {
				sql = "INSERT INTO sczb_data (srzb_data_id,xm_id,xm_name,hysj_date,hysjd_time,gx_zhbc,hy_bc,sczb_data,beizhu,beizhu_name,shenhe) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, srzb_data_id);
				ps.setString(2, xm_id);
				ps.setString(3, xm_name);
				ps.setString(4, hysj_date);
				ps.setString(5, hysjd_time);
				ps.setString(6, hy_zhbc);
				ps.setString(7, hy_bc);
				ps.setString(8, sczb_data);
				ps.setString(9, beizhu);
				ps.setString(10, beizhu_name);
				ps.setString(11, "0");
				ps.executeUpdate();
				stmt.close();
				rs.close();
				sql = "SELECT id FROM sczb_data WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date
						+ "' AND hy_bc='" + hy_bc + "' AND srzb_data_id='" + srzb_data_id + "'";
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					sczb_data_id = rs.getString("id");
				}
				out.print(sczb_data_id);
			}
			/* 班平均计算 */
			String avg;
			JSONArray a = Ban_avg.banpingjun(xm_id, xm_name, hy_bc, hysj_date);// 计算项目的平均值
			if (a != null) {
				avg = a.toString();
				sql = "UPDATE sczb_ban_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + hysj_date
						+ "',hy_bc='" + hy_bc + "',val='" + avg + "' WHERE xm_id='" + xm_id + "' AND hysj_date='"
						+ hysj_date + "' AND hy_bc='" + hy_bc + "'";
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
				}
			}
			/* 日平均计算 */
			String avg_ri;
			JSONArray aa = Ri_avg.ripingjun(xm_id, hysj_date);// 计算项目的平均值
			if (aa != null) {
				avg_ri = aa.toString();
				sql = "UPDATE sczb_ri_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + hysj_date
						+ "',val='" + avg_ri + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date + "'";
				int nn = 0;
				nn = stmt.executeUpdate(sql);
				if (nn == 0) {
					sql = "INSERT INTO sczb_ri_avg (xm_id, xm_name, hysj_date, val) VALUES (?,?,?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, xm_id);
					ps.setString(2, xm_name);
					ps.setString(3, hysj_date);
					ps.setString(4, avg_ri);
					ps.executeUpdate();
				}
			}
			/* 月平均计算 */
			String avg_yue;
			JSONArray aaa = Yue_avg.yuepingjun(xm_id, hysj_date);// 计算项目的平均值
			String ym = hysj_date.substring(0, 7);// 提取年月

			if (aaa != null) {
				avg_yue = aaa.toString();
				sql = "UPDATE sczb_yue_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + ym
						+ "',val='" + avg_yue + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
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
			rs.close();
			ps.close();
			
			/* 调度月平均计算（从上个月最后一天开始，当月倒数第二天结束） */
			JSONArray bbb = DD_Yue_avg.yuepingjun(xm_id, hysj_date);// 计算项目的平均值
			if (bbb != null) {
				avg_yue = bbb.toString();
				sql = "UPDATE sczb_yue_avg_dd SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + ym
						+ "',val='" + avg_yue + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
				int nn = 0;
				nn = stmt.executeUpdate(sql);
				if (nn == 0) {
					sql = "INSERT INTO sczb_yue_avg_dd (xm_id, xm_name, hysj_date, val) VALUES (?,?,?,?)";
					ps = conn.prepareStatement(sql);
					ps.setString(1, xm_id);
					ps.setString(2, xm_name);
					ps.setString(3, ym);
					ps.setString(4, avg_yue);
					ps.executeUpdate();
				}
			}
			DB.close(conn);
			stmt.close();
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
