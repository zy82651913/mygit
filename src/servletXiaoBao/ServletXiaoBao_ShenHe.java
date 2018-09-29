package servletXiaoBao;

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

import hys.DB;
import hys.Ri_avg;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletXiaoBao_ShenHe
 */
@WebServlet("/ServletXiaoBao_ShenHe")
public class ServletXiaoBao_ShenHe extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletXiaoBao_ShenHe() {
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
		ResultSet rs = null;
		Statement stmt = null;
		PreparedStatement ps = null;
		String sql;
		int n = 0;
		JSONArray xm = new JSONArray();
		JSONObject xmxm = new JSONObject();
		String sj = request.getParameter("sj");
		JSONObject sjobj = JSONObject.fromObject(sj);// 把前台传入的json对象字符串转为json对象
		String hysj_date = sjobj.getString("hysj_date");// 化验日期
		String hy_bc = sjobj.getString("hy_bc");// 化验班次
		JSONArray chejianidArr = sjobj.getJSONArray("chejianid");// 把json对象转换成json数组
		int[] cjidArr = new int[chejianidArr.size()];
		for (int i = 0; i < chejianidArr.size(); i++) {// json数组转为int数组
			cjidArr[i] = chejianidArr.getInt(i);
		}
		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			for (int i = 0; i < cjidArr.length; i++) {// 通过车间id取出对应的项目id,name
				int cjid = cjidArr[i];
				sql = "SELECT\n" + "xmgl.id,\n" + "xmgl.name\n" + "FROM\n" + "xmgl\n" + "WHERE\n" + "xmgl.chejianid = "
						+ cjid + "";
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					xmxm.put("xm_id", rs.getInt("id"));
					xmxm.put("xm_name", rs.getString("name"));
					xm.add(xmxm);
				}
			}
			/* 审核时计算选中的技站车间的月平均 */
			/*for (int i = 0; i < xm.size(); i++) {
				String xm_id = xm.getJSONObject(i).getString("xm_id");
				String xm_name = xm.getJSONObject(i).getString("xm_name");

				String avg;
				JSONArray a = Ri_avg.ripingjun(xm_id, hysj_date);// 计算项目的平均值
				if (a != null) {
					avg = a.toString();
					sql = "UPDATE sczb_ri_avg SET xm_id='" + xm_id + "',xm_name='" + xm_name + "',hysj_date='"
							+ hysj_date + "',hy_bc='" + hy_bc + "',val='" + avg + "' WHERE xm_id='" + xm_id
							+ "' AND hysj_date='" + hysj_date + "' AND hy_bc='" + hy_bc + "'";
					int nn = 0;
					nn = stmt.executeUpdate(sql);
					if (n == 0) {
						sql = "INSERT INTO sczb_ri_avg (xm_id, xm_name, hysj_date, hy_bc, val) VALUES (?,?,?,?,?)";
						ps = conn.prepareStatement(sql);
						ps.setString(1, xm_id);
						ps.setString(2, xm_name);
						ps.setString(3, hysj_date);
						ps.setString(4, hy_bc);
						ps.setString(5, avg);
						ps.executeUpdate();
					}
				}
			}*/
			/*设置项目id的字符串，用于批量更新*/
			String xmid_str = "(";
			for (int i = 0; i <= xm.size(); i++) {
				if (i < xm.size()) {
					xmid_str = xmid_str + xm.getJSONObject(i).getInt("xm_id") + ",";
				}else {
					xmid_str = xmid_str.substring(0, xmid_str.length() - 1);
					xmid_str = xmid_str + ")";
				}
			}
			//System.out.println(xmid_str);
				int n1 = 0;
				sql = "UPDATE sczb_data SET shenhe=1 WHERE xm_id in" + xmid_str
						+ " AND hysj_date='" + hysj_date + "' AND hy_bc=" + hy_bc + "";
				// 计算班平均
				// Ban_avg.banpingjun(xm.getJSONObject(i).getInt("xm_id"),
				// xm.getJSONObject(i).getString("xm_name"), hy_bc, hysj_date);
				n1 = stmt.executeUpdate(sql);
				n = n + n1;
			DB.close(conn);
			stmt.close();
			rs.close();
			if (n > 0) {
				out.print("成功");
			} else {
				out.print("失败");
			}

		} catch (SQLException e) {
			System.out.println(e);
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
