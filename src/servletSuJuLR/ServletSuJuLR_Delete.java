package servletSuJuLR;

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

import hys.Ban_avg;
import hys.DB;
import hys.DD_Yue_avg;
import hys.Ri_avg;
import hys.Yue_avg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletSuJuLR_Delete
 */
@WebServlet("/ServletSuJuLR_Delete")
public class ServletSuJuLR_Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletSuJuLR_Delete() {
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
		Connection conn = null;// 定义为空值
		Statement stmt = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();
		JSONObject jsonobj = new JSONObject();// JSON对象
		String sql = null;
		int n = -1;
		String xm_id = null;
		String xm_name = null;
		String hy_bc = null;
		String hysj_date = null;
		String srzb_data_id = null;
		String id = request.getParameter("id");

		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			sql = "SELECT xm_id,xm_name,hy_bc,hysj_date,srzb_data_id FROM sczb_data WHERE id='" + id + "'";
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				xm_id = rs.getString("xm_id");
				xm_name = rs.getString("xm_name");
				hy_bc = rs.getString("hy_bc");
				hysj_date = rs.getString("hysj_date");
				srzb_data_id = rs.getString("srzb_data_id");
			}

			sql = "DELETE FROM srzb_data WHERE id='" + srzb_data_id + "'";
			n = stmt.executeUpdate(sql);// 删除数据
			sql = "DELETE FROM sczb_data WHERE id='" + id + "'";
			stmt = conn.createStatement();
			n = stmt.executeUpdate(sql);// 删除数据
			if (n>0) {
				// 删除成功后，要想刷新可编辑数据表，需要向前台传一个{"success":"true"}
				jsonobj.put("success", "true");
				out.print(jsonobj);
			}else {
				jsonobj.put("success", "false");
				out.print(jsonobj);
			}
			/* 班平均计算 */
			String avg;
			JSONArray a = Ban_avg.banpingjun(xm_id, xm_name, hy_bc, hysj_date);// 计算项目的平均值
			if (a != null) {
				avg = a.toString();
				sql = "UPDATE sczb_ban_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + hysj_date
						+ "',hy_bc='" + hy_bc + "',val='" + avg + "' WHERE xm_id='" + xm_id + "' AND hysj_date='"
						+ hysj_date + "' AND hy_bc='" + hy_bc + "'";
				stmt.executeUpdate(sql);
			} else {// 如果项目没有找到，删除班平均对应数据
				sql = "DELETE FROM sczb_ban_avg WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date
						+ "' AND hy_bc='" + hy_bc + "'";// 如果项目没有找到，删除班平均对应数据
				stmt.executeUpdate(sql);// 删除数据
			}
			/* 日平均计算 */
			String avg_ri;
			JSONArray aa = Ri_avg.ripingjun(xm_id, hysj_date);// 计算项目的平均值
			if (aa != null) {
				avg_ri = aa.toString();
				sql = "UPDATE sczb_ri_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + hysj_date
						+ "',val='" + avg_ri + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date + "'";
				stmt.executeUpdate(sql);
			} else {
				sql = "DELETE FROM sczb_ri_avg WHERE xm_id='" + xm_id + "' AND hysj_date='" + hysj_date + "'";
				stmt.executeUpdate(sql);// 删除数据
			}
			/* 月平均计算 */
			String avg_yue;
			JSONArray aaa = Yue_avg.yuepingjun(xm_id, hysj_date);// 计算项目的平均值
			String ym = hysj_date.substring(0, 7);// 提取年月

			if (aaa != null) {
				avg_yue = aaa.toString();
				sql = "UPDATE sczb_yue_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + ym
						+ "',val='" + avg_yue + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
				stmt.executeUpdate(sql);
			} else {
				sql = "DELETE FROM sczb_yue_avg WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
				stmt.executeUpdate(sql);// 删除数据
			}

			rs.close();
			/* 调度月平均计算（从上个月最后一天开始，当月倒数第二天结束） */
			JSONArray bbb = DD_Yue_avg.yuepingjun(xm_id, hysj_date);// 计算项目的平均值
			if (bbb != null) {
				avg_yue = bbb.toString();
				sql = "UPDATE sczb_yue_avg_dd SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='" + ym
						+ "',val='" + avg_yue + "' WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
				stmt.executeUpdate(sql);
			} else {
				sql = "DELETE FROM sczb_yue_avg_dd WHERE xm_id='" + xm_id + "' AND hysj_date='" + ym + "'";
				stmt.executeUpdate(sql);// 删除数据
			}
			DB.close(conn);
			stmt.close();
			rs.close();
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
