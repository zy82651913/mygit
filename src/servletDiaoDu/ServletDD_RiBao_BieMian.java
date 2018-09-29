package servletDiaoDu;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletDD_RiBao_BieMian 
 * 调度日报背面数据处理
 */
@WebServlet("/ServletDD_RiBao_BieMian")
public class ServletDD_RiBao_BieMian extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletDD_RiBao_BieMian() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json; charset=utf-8");

		Connection conn = null;// 定义为空值
		Statement stmt = null;
		ResultSet rs = null;
		String sql = null;
		DecimalFormat df = new DecimalFormat();
		PrintWriter out = response.getWriter();
		String hysj_date = request.getParameter("hysj_date");// 取得时间
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		JSONObject banObject = new JSONObject();
		JSONObject yueObject = new JSONObject();
		JSONArray banArray = new JSONArray();
		JSONArray yueArray = new JSONArray();
		JSONObject ddObject = new JSONObject();// 调度日报定制数据
		JSONArray ddArray = new JSONArray();// 调度日报定制数据
		JSONObject fh_banObject = new JSONObject();//复合班数据
		JSONArray fh_banArray = new JSONArray();//复合班数据
		JSONObject fh_yueObject = new JSONObject();//复合月数据
		JSONArray fh_yueArray = new JSONArray();//复合月数据
		String ym = hysj_date.substring(0, 7);// 提取年月
		int col = 54;// 每列的行数
		try {
			conn = DB.getConection();// 利用封装好的类名来调用连接方法便可
			/* 提取调度日报背面定制的数据 */
			sql = "SELECT\n" +
					"ddrb_beimian.xmid,\n" +
					"ddrb_beimian.xm_name,\n" +
					"ddrb_beimian.zbbh,\n" +
					"ddrb_beimian.xmzb_name,\n" +
					"ddrb_beimian.guiding,\n" +
					"ddrb_beimian.xsws,\n" +
					"ddrb_beimian.sjclass\n" +
					"FROM\n" +
					"ddrb_beimian\n" +
					"ORDER BY\n" +
					"ddrb_beimian.pxid ASC";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				ddObject.put("xmid", rs.getString("xmid"));
				ddObject.put("xm_name", rs.getString("xm_name"));
				ddObject.put("zbbh", rs.getString("zbbh"));
				ddObject.put("xmzb_name", rs.getString("xmzb_name"));
				ddObject.put("guiding", rs.getString("guiding"));
				ddObject.put("xsws", rs.getString("xsws"));
				ddObject.put("sjclass", rs.getString("sjclass"));
				ddArray.add(ddObject);
			}
			stmt.close();
			rs.close();
			/* 班平均查询 */
			sql = "SELECT\n" + "sczb_ban_avg.xm_id,\n" + "sczb_ban_avg.hy_bc,\n" + "sczb_ban_avg.val\n" + "FROM\n"
					+ "sczb_ban_avg\n" + "WHERE\n" + "sczb_ban_avg.hysj_date = '" + hysj_date + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				banObject.put("xm_id", rs.getString("xm_id"));
				banObject.put("hy_bc", rs.getString("hy_bc"));
				banObject.put("val", rs.getString("val"));
				banArray.add(banObject);
			}
			stmt.close();
			rs.close();

			/* 月平均查询 */
			sql = "SELECT\n" + "sczb_yue_avg_dd.xm_id,\n" + "sczb_yue_avg_dd.val\n" + "FROM\n" + "sczb_yue_avg_dd\n" + "WHERE\n"
					+ "sczb_yue_avg_dd.hysj_date = '" + ym + "'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				yueObject.put("xm_id", rs.getString("xm_id"));
				yueObject.put("val", rs.getString("val"));
				yueArray.add(yueObject);
			}
			stmt.close();
			rs.close();
			
			/*复合班平均数据*/
			sql = "SELECT\n" +
					"fhzb_data.fhzb_id,\n" +
					"fhzb_data.fhzb_bc,\n" +
					"fhzb_data.fhzb_val\n" +
					"FROM\n" +
					"fhzb_data\n" +
					"WHERE\n" +
					"fhzb_data.fhzb_date = '"+hysj_date+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				fh_yueObject.put("fhzb_id", rs.getString("fhzb_id"));
				fh_yueObject.put("fhzb_bc", rs.getString("fhzb_bc"));
				fh_yueObject.put("fhzb_val", rs.getString("fhzb_val"));
				fh_banArray.add(fh_yueObject);
			}
			stmt.close();
			rs.close();
			/*复合月平均数据*/
			sql = "SELECT\n" +
					"fhzb_yue_avg_dd.fhzb_id,\n" +
					"fhzb_yue_avg_dd.fhzb_val\n" +
					"FROM\n" +
					"fhzb_yue_avg_dd\n" +
					"WHERE\n" +
					"fhzb_yue_avg_dd.fhzb_date = '"+ym+"'";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				fh_banObject.put("fhzb_id", rs.getString("fhzb_id"));
				fh_banObject.put("fhzb_val", rs.getString("fhzb_val"));
				fh_yueArray.add(fh_banObject);
			}
			stmt.close();
			rs.close();
			
			DB.close(conn);
			
			double aaa = ddArray.size();
			int ii = (int) Math.ceil(aaa / col);// 根据输入的行数算出有几列
			String guiding = null;

			for (int i = 0; i < col; i++) {// 定义的行数
				if (i < ddArray.size()) {// 如果数据多余第一列
					String xmid = ddArray.getJSONObject(i).getString("xmid");// 项目id
					String zbbh = ddArray.getJSONObject(i).getString("zbbh");// 指标编号
					String xmzb_name = ddArray.getJSONObject(i).getString("xmzb_name");// 项目指标名称
					String hybc = null;// 化验班次
					String sjclass = ddArray.getJSONObject(i).getString("sjclass");// 项目数据类型
					JSONArray it = new JSONArray();
					int itt = 0;
					try {
						guiding = ddArray.getJSONObject(i).getString("guiding");
					} catch (Exception e) {
						guiding = "";
					}
					int xsws = Integer.parseInt(ddArray.getJSONObject(i).getString("xsws"));
					jsonObject.put("xmzb1", xmzb_name);
					jsonObject.put("guiding1", guiding);

					/* 先查第一列的数据 */
					if (sjclass.equals("0")) {//普通数据
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							// 查班数据取出零点白班中班数据
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								it.add(banArray.getJSONObject(j1).getString("hy_bc"));
								if (banArray.getJSONObject(j1).getString("hy_bc").equals("1")) {
									hybc = "lingdian1";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("2")) {
									hybc = "baiban1";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("3")) {
									hybc = "zhongban1";
								}
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int k = 0; k < valArray.size(); k++) {
									String string1 = (valArray.getJSONObject(k).getString("sczb_bh"));
									if (valArray.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										// jsonObject = new JSONObject();
										if (valArray.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put(hybc, "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(valArray.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put(hybc, val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put(hybc, "");
									}
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian1", "");
							jsonObject.put("baiban1", "");
							jsonObject.put("zhongban1", "");
						} else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian1", "");
											}
											if (j == 2) {
												jsonObject.put("baiban1", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban1", "");
											}
										}
									}
								}
							}
						}
						
						//计算日平均
						double sum = 0;//数据的和
						double gs = 0;//数据的个数
						String avg = "";
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int j = 0; j < valArray.size(); j++) {
									if (valArray.getJSONObject(j).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										if (valArray.getJSONObject(j).getString("val").equals("")) {
											break;
										} else {
											sum += Double.parseDouble(valArray.getJSONObject(j).getString("val"));
											gs++;
											break;
										}
									}
								}
							}else {
								jsonObject.put("riping1", "");
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping1", avg);
						} catch (Exception e) {
							jsonObject.put("riping1", "");
						}
						
						// 查月平均数据取出零点白班中班数据
						for (int j = 0; j < yueArray.size(); j++) {
							if (yueArray.getJSONObject(j).getString("xm_id").equals(xmid)) {// 查月平均id匹配
								itt++;
								JSONArray yue_valArry = yueArray.getJSONObject(j).getJSONArray("val");// 获得月平均值数据
								for (int k = 0; k < yue_valArry.size(); k++) {
									if (yue_valArry.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查找指标编号匹配
										if (yue_valArry.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put("leiping1", "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(yue_valArry.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put("leiping1", val);
											
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put("leiping1", "");
									}
								}
							}
						}
						if (itt == 0) {// 如果项目id不匹配，累平为空
							jsonObject.put("leiping1", "");
						}
					}else{//复合数据
						//遍历复合数据班
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								it.add(fh_banArray.getJSONObject(j).getString("fhzb_bc"));
								if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("1")) {
									jsonObject.put("lingdian1", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("2")) {
									jsonObject.put("baiban1", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("3")) {
									jsonObject.put("zhongban1", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian1", "");
							jsonObject.put("baiban1", "");
							jsonObject.put("zhongban1", "");
						} else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian1", "");
											}
											if (j == 2) {
												jsonObject.put("baiban1", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban1", "");
											}
										}
									}
								}
							}
						}
						
						//遍历复合日平均
						double sum = 0;
						double gs = 0;
						String avg = "";
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								try {
									sum += Double.parseDouble(fh_banArray.getJSONObject(j).getString("fhzb_val"));
									gs++;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping1", avg);
						} catch (Exception e) {
							jsonObject.put("riping1", "");
						}
						
						//遍历复合数据月平均
						for (int j = 0; j < fh_yueArray.size(); j++) {
							if (fh_yueArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								jsonObject.put("leiping1", fh_yueArray.getJSONObject(j).getString("fhzb_val"));
								itt++;
							}
							if (itt == 0) {
								jsonObject.put("leiping1", "");
							}
						}
					}
				} else {
					jsonObject.put("xmzb1", "");
					jsonObject.put("guiding1", "");
					jsonObject.put("lingdian1", "");
					jsonObject.put("baiban1", "");
					jsonObject.put("zhongban1", "");
					jsonObject.put("leiping1", "");
				}

				/* 查询第二列的数据 */
				if (i + col < ddArray.size()) {// 如果数据多余第一列
					String xmid = ddArray.getJSONObject(i + col).getString("xmid");// 项目id
					String zbbh = ddArray.getJSONObject(i + col).getString("zbbh");// 指标编号
					String xmzb_name = ddArray.getJSONObject(i + col).getString("xmzb_name");// 项目指标名称
					String sjclass = ddArray.getJSONObject(i + col).getString("sjclass");// 项目数据类型
					String hybc = null;// 化验班次
					JSONArray it = new JSONArray();
					int itt = 0;
					try {
						guiding = ddArray.getJSONObject(i + col).getString("guiding");
					} catch (Exception e) {
						guiding = "";
					}
					int xsws = Integer.parseInt(ddArray.getJSONObject(i + col).getString("xsws"));
					jsonObject.put("xmzb2", xmzb_name);
					jsonObject.put("guiding2", guiding);

					/*
					 * banArray.getJSONObject(j1).getString("hy_bc").equals("1")
					 * )
					 */
					/* 先查第二列的数据 */
					if (sjclass.equals("0")) {//普通数据
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							// 查班数据取出零点白班中班数据
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								it.add(banArray.getJSONObject(j1).getString("hy_bc"));
								if (banArray.getJSONObject(j1).getString("hy_bc").equals("1")) {
									hybc = "lingdian2";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("2")) {
									hybc = "baiban2";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("3")) {
									hybc = "zhongban2";
								}
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int k = 0; k < valArray.size(); k++) {
									if (valArray.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										// jsonObject = new JSONObject();
										if (valArray.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put(hybc, "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(valArray.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put(hybc, val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put(hybc, "");
									}
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian2", "");
							jsonObject.put("baiban2", "");
							jsonObject.put("zhongban2", "");
						}else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian2", "");
											}
											if (j == 2) {
												jsonObject.put("baiban2", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban2", "");
											}
										}
									}
								}
							}
						}
						
						//计算日平均
						double sum = 0;//数据的和
						double gs = 0;//数据的个数
						String avg = "";
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int j = 0; j < valArray.size(); j++) {
									if (valArray.getJSONObject(j).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										if (valArray.getJSONObject(j).getString("val").equals("")) {
											break;
										} else {
											sum += Double.parseDouble(valArray.getJSONObject(j).getString("val"));
											gs++;
											break;
										}
									}
								}
							}else {
								jsonObject.put("riping2", "");
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping2", avg);
						} catch (Exception e) {
							jsonObject.put("riping2", "");
						}
						
						// 查月平均数据取出零点白班中班数据
						for (int j = 0; j < yueArray.size(); j++) {
							if (yueArray.getJSONObject(j).getString("xm_id").equals(xmid)) {// 查月平均id匹配
								itt++;
								JSONArray yue_valArry = yueArray.getJSONObject(j).getJSONArray("val");// 获得月平均值数据
								for (int k = 0; k < yue_valArry.size(); k++) {
									if (yue_valArry.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查找指标编号匹配
										if (yue_valArry.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put("leiping2", "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(yue_valArry.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put("leiping2", val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put("leiping2", "");
									}
								}
							}
						}
						
						if (itt == 0) {// 如果项目id不匹配，累平为空
							jsonObject.put("leiping2", "");
						}
					}else{//复合数据
						//遍历复合数据班
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								it.add(fh_banArray.getJSONObject(j).getString("fhzb_bc"));
								if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("1")) {
									jsonObject.put("lingdian2", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("2")) {
									jsonObject.put("baiban2", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("3")) {
									jsonObject.put("zhongban2", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian2", "");
							jsonObject.put("baiban2", "");
							jsonObject.put("zhongban2", "");
						} else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian2", "");
											}
											if (j == 2) {
												jsonObject.put("baiban2", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban2", "");
											}
										}
									}
								}
							}
						}
						
						//遍历复合日平均
						double sum = 0;
						double gs = 0;
						String avg = "";
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								try {
									sum += Double.parseDouble(fh_banArray.getJSONObject(j).getString("fhzb_val"));
									gs++;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping2", avg);
						} catch (Exception e) {
							jsonObject.put("riping2", "");
						}
						
						//遍历复合数据月平均
						for (int j = 0; j < fh_yueArray.size(); j++) {
							if (fh_yueArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								jsonObject.put("leiping2", fh_yueArray.getJSONObject(j).getString("fhzb_val"));
								itt++;
							}
							if (itt == 0) {
								jsonObject.put("leiping2", "");
							}
						}
					}
				} else {// 如果数据没有占满行补空
					jsonObject.put("xmzb2", "");
					jsonObject.put("guiding2", "");
					jsonObject.put("lingdian2", "");
					jsonObject.put("baiban2", "");
					jsonObject.put("zhongban2", "");
					jsonObject.put("leiping2", "");
				}

				/* 查询第三列的数据 */
				if (i + col + col < ddArray.size()) {// 如果数据多余第一列
					String xmid = ddArray.getJSONObject(i + col + col).getString("xmid");// 项目id
					String zbbh = ddArray.getJSONObject(i + col + col).getString("zbbh");// 指标编号
					String xmzb_name = ddArray.getJSONObject(i + col + col).getString("xmzb_name");// 项目指标名称
					String sjclass = ddArray.getJSONObject(i + col +col).getString("sjclass");// 项目数据类型
					String hybc = null;// 化验班次
					JSONArray it = new JSONArray();
					int itt = 0;
					try {
						guiding = ddArray.getJSONObject(i + col + col).getString("guiding");
					} catch (Exception e) {
						guiding = "";
					}
					int xsws = Integer.parseInt(ddArray.getJSONObject(i + col + col).getString("xsws"));
					jsonObject.put("xmzb3", xmzb_name);
					jsonObject.put("guiding3", guiding);

					/*
					 * banArray.getJSONObject(j1).getString("hy_bc").equals("1")
					 * )
					 */
					/* 先查第三列的数据 */
					if (sjclass.equals("0")) {//普通数据
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							// 查班数据取出零点白班中班数据
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								it.add(banArray.getJSONObject(j1).getString("hy_bc"));
								if (banArray.getJSONObject(j1).getString("hy_bc").equals("1")) {
									hybc = "lingdian3";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("2")) {
									hybc = "baiban3";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("3")) {
									hybc = "zhongban3";
								}
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int k = 0; k < valArray.size(); k++) {
									if (valArray.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										// jsonObject = new JSONObject();
										if (valArray.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put(hybc, "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(valArray.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put(hybc, val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put(hybc, "");
									}
								}
							}
							if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
								jsonObject.put("lingdian3", "");
								jsonObject.put("baiban3", "");
								jsonObject.put("zhongban3", "");
							}else if (it.size()<3 || it.size()>0) {
								for (int j = 1; j < 4; j++) {
									for (int j2 = 0; j2 < it.size(); j2++) {
										if (it.getString(j2).equals(Integer.toString(j))) {
											break;
										}else{
											if (j2+1 == it.size()) {
												if (j == 1) {
													jsonObject.put("lingdian3", "");
												}
												if (j == 2) {
													jsonObject.put("baiban3", "");
												}
												if (j == 3) {
													jsonObject.put("zhongban3", "");
												}
											}
										}
									}
								}
							}
							
							//计算日平均
							double sum = 0;//数据的和
							double gs = 0;//数据的个数
							String avg = "";
							for (int j11 = 0; j11 < banArray.size(); j11++) {
								if (banArray.getJSONObject(j11).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
									JSONArray valArray = banArray.getJSONObject(j11).getJSONArray("val");// 获得班平均值得数组
									for (int j = 0; j < valArray.size(); j++) {
										if (valArray.getJSONObject(j).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
											if (valArray.getJSONObject(j).getString("val").equals("")) {
												break;
											} else {
												sum += Double.parseDouble(valArray.getJSONObject(j).getString("val"));
												gs++;
												break;
											}
										}
									}
								}else {
									jsonObject.put("riping3", "");
								}
							}
							try {
								df.setGroupingUsed(false);//不以逗号分隔
								df.setMaximumFractionDigits(xsws);
								avg = df.format(sum/gs);
								df.setMinimumFractionDigits(xsws);
								avg = df.format(Double.parseDouble(avg));
								jsonObject.put("riping3", avg);
							} catch (Exception e) {
								jsonObject.put("riping3", "");
							}
							
							// 查月平均数据取出零点白班中班数据
							for (int j = 0; j < yueArray.size(); j++) {
								if (yueArray.getJSONObject(j).getString("xm_id").equals(xmid)) {// 查月平均id匹配
									itt++;
									JSONArray yue_valArry = yueArray.getJSONObject(j).getJSONArray("val");// 获得月平均值数据
									for (int k = 0; k < yue_valArry.size(); k++) {
										if (yue_valArry.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查找指标编号匹配
											if (yue_valArry.getJSONObject(k).getString("val").equals("")) {
												jsonObject.put("leiping3", "");
												break;
											} else {
												df.setGroupingUsed(false);//不以逗号分隔
												df.setMaximumFractionDigits(xsws);
												String val = df.format(Double.parseDouble(yue_valArry.getJSONObject(k).getString("val")));
												df.setMinimumFractionDigits(xsws);
												val = df.format(Double.parseDouble(val));
												jsonObject.put("leiping3", val);
												break;
											}
										}else {//没有找到对应的指标编号，设置为空
											jsonObject.put("leiping3", "");
										}
									}
								}
							}
						}
						
						if (itt == 0) {// 如果项目id不匹配，累平为空
							jsonObject.put("leiping3", "");
						}
					}else{//复合数据
						//遍历复合数据班
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								it.add(fh_banArray.getJSONObject(j).getString("fhzb_bc"));
								if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("1")) {
									jsonObject.put("lingdian3", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("2")) {
									jsonObject.put("baiban3", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("3")) {
									jsonObject.put("zhongban3", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian3", "");
							jsonObject.put("baiban3", "");
							jsonObject.put("zhongban3", "");
						} else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian3", "");
											}
											if (j == 2) {
												jsonObject.put("baiban3", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban3", "");
											}
										}
									}
								}
							}
						}
						
						//遍历复合日平均
						double sum = 0;
						double gs = 0;
						String avg = "";
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								try {
									sum += Double.parseDouble(fh_banArray.getJSONObject(j).getString("fhzb_val"));
									gs++;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping3", avg);
						} catch (Exception e) {
							jsonObject.put("riping3", "");
						}
						
						//遍历复合数据月平均
						for (int j = 0; j < fh_yueArray.size(); j++) {
							if (fh_yueArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								jsonObject.put("leiping3", fh_yueArray.getJSONObject(j).getString("fhzb_val"));
								itt++;
							}
							if (itt == 0) {
								jsonObject.put("leiping3", "");
							}
						}
					}
				} else {// 如果数据没有占满行补空
					jsonObject.put("xmzb3", "");
					jsonObject.put("guiding3", "");
					jsonObject.put("lingdian3", "");
					jsonObject.put("baiban3", "");
					jsonObject.put("zhongban3", "");
					jsonObject.put("leiping3", "");
				}

				/* 查询第四列的数据 */
				if (i + col + col + col < ddArray.size()) {// 如果数据多余第一列
					String xmid = ddArray.getJSONObject(i + col + col + col).getString("xmid");// 项目id
					String zbbh = ddArray.getJSONObject(i + col + col + col).getString("zbbh");// 指标编号
					String xmzb_name = ddArray.getJSONObject(i + col + col + col).getString("xmzb_name");// 项目指标名称
					String sjclass = ddArray.getJSONObject(i + col +col +col).getString("sjclass");// 项目数据类型
					String hybc = null;// 化验班次
					JSONArray it = new JSONArray();
					int itt = 0;
					try {
						guiding = ddArray.getJSONObject(i + col + col + col).getString("guiding");
					} catch (Exception e) {
						guiding = "";
					}
					int xsws = Integer.parseInt(ddArray.getJSONObject(i + col + col + col).getString("xsws"));
					jsonObject.put("xmzb4", xmzb_name);
					jsonObject.put("guiding4", guiding);

					/*
					 * banArray.getJSONObject(j1).getString("hy_bc").equals("1")
					 * )
					 */
					/* 先查第四列的数据 */
					if (sjclass.equals("0")) {//普通数据
						for (int j1 = 0; j1 < banArray.size(); j1++) {
							// 查班数据取出零点白班中班数据
							if (banArray.getJSONObject(j1).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								it.add(banArray.getJSONObject(j1).getString("hy_bc"));
								if (banArray.getJSONObject(j1).getString("hy_bc").equals("1")) {
									hybc = "lingdian4";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("2")) {
									hybc = "baiban4";
								} else if (banArray.getJSONObject(j1).getString("hy_bc").equals("3")) {
									hybc = "zhongban4";
								}
								JSONArray valArray = banArray.getJSONObject(j1).getJSONArray("val");// 获得班平均值得数组
								for (int k = 0; k < valArray.size(); k++) {
									if (valArray.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										// jsonObject = new JSONObject();
										if (valArray.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put(hybc, "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(valArray.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put(hybc, val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put(hybc, "");
									}
								}
							}
							if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
								jsonObject.put("lingdian4", "");
								jsonObject.put("baiban4", "");
								jsonObject.put("zhongban4", "");
							}else if (it.size()<3 || it.size()>0) {
								for (int j = 1; j < 4; j++) {
									for (int j2 = 0; j2 < it.size(); j2++) {
										if (it.getString(j2).equals(Integer.toString(j))) {
											break;
										}else{
											if (j2+1 == it.size()) {
												if (j == 1) {
													jsonObject.put("lingdian4", "");
												}
												if (j == 2) {
													jsonObject.put("baiban4", "");
												}
												if (j == 3) {
													jsonObject.put("zhongban4", "");
												}
											}
										}
									}
								}
							}
						}
						
						//计算日平均
						double sum = 0;//数据的和
						double gs = 0;//数据的个数
						String avg = "";
						for (int j = 0; j < banArray.size(); j++) {
							if (banArray.getJSONObject(j).getString("xm_id").equals(xmid)) {// 先查项目id匹配的
								JSONArray valArray = banArray.getJSONObject(j).getJSONArray("val");// 获得班平均值得数组
								for (int l = 0; l < valArray.size(); l++) {
									if (valArray.getJSONObject(l).getString("sczb_bh").equals(zbbh)) {// 查指标编号匹配的
										if (valArray.getJSONObject(l).getString("val").equals("")) {
											break;
										}else {
											sum += Double.parseDouble(valArray.getJSONObject(l).getString("val"));
											gs++;
											break;
										}
									}
								}
							}else {
								jsonObject.put("riping4", "");
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping4", avg);
						} catch (Exception e) {
							jsonObject.put("riping4", "");
						}
						
						// 查月平均数据取出零点白班中班数据
						for (int j = 0; j < yueArray.size(); j++) {
							if (yueArray.getJSONObject(j).getString("xm_id").equals(xmid)) {// 查月平均id匹配
								itt++;
								JSONArray yue_valArry = yueArray.getJSONObject(j).getJSONArray("val");// 获得月平均值数据
								for (int k = 0; k < yue_valArry.size(); k++) {
									if (yue_valArry.getJSONObject(k).getString("sczb_bh").equals(zbbh)) {// 查找指标编号匹配
										if (yue_valArry.getJSONObject(k).getString("val").equals("")) {
											jsonObject.put("leiping4", "");
											break;
										} else {
											df.setGroupingUsed(false);//不以逗号分隔
											df.setMaximumFractionDigits(xsws);
											String val = df.format(Double.parseDouble(yue_valArry.getJSONObject(k).getString("val")));
											df.setMinimumFractionDigits(xsws);
											val = df.format(Double.parseDouble(val));
											jsonObject.put("leiping4", val);
											break;
										}
									}else {//没有找到对应的指标编号，设置为空
										jsonObject.put("leiping4", "");
									}
								}
							}
						}
						
						if (itt == 0) {// 如果项目id不匹配，累平为空
							jsonObject.put("leiping4", "");
						}
					}else{//复合数据
						//遍历复合数据班
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								it.add(fh_banArray.getJSONObject(j).getString("fhzb_bc"));
								if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("1")) {
									jsonObject.put("lingdian4", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("2")) {
									jsonObject.put("baiban4", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								} else if (fh_banArray.getJSONObject(j).getString("fhzb_bc").equals("3")) {
									jsonObject.put("zhongban4", fh_banArray.getJSONObject(j).getString("fhzb_val"));
								}
							}
						}
						if (it.size() == 0) {// 如果项目id不匹配，零点白班四点数据都为空
							jsonObject.put("lingdian4", "");
							jsonObject.put("baiban4", "");
							jsonObject.put("zhongban4", "");
						} else if (it.size()<3 || it.size()>0) {
							for (int j = 1; j < 4; j++) {
								for (int j2 = 0; j2 < it.size(); j2++) {
									if (it.getString(j2).equals(Integer.toString(j))) {
										break;
									}else{
										if (j2+1 == it.size()) {
											if (j == 1) {
												jsonObject.put("lingdian4", "");
											}
											if (j == 2) {
												jsonObject.put("baiban4", "");
											}
											if (j == 3) {
												jsonObject.put("zhongban4", "");
											}
										}
									}
								}
							}
						}
						
						//遍历复合日平均
						double sum = 0;
						double gs = 0;
						String avg = "";
						for (int j = 0; j < fh_banArray.size(); j++) {
							if (fh_banArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								try {
									sum += Double.parseDouble(fh_banArray.getJSONObject(j).getString("fhzb_val"));
									gs++;
								} catch (Exception e) {
									// TODO: handle exception
								}
							}
						}
						try {
							df.setGroupingUsed(false);//不以逗号分隔
							df.setMaximumFractionDigits(xsws);
							avg = df.format(sum/gs);
							df.setMinimumFractionDigits(xsws);
							avg = df.format(Double.parseDouble(avg));
							jsonObject.put("riping4", avg);
						} catch (Exception e) {
							jsonObject.put("riping4", "");
						}
						
						//遍历复合数据月平均
						for (int j = 0; j < fh_yueArray.size(); j++) {
							if (fh_yueArray.getJSONObject(j).getString("fhzb_id").equals(xmid)) {
								jsonObject.put("leiping4", fh_yueArray.getJSONObject(j).getString("fhzb_val"));
								itt++;
							}
							if (itt == 0) {
								jsonObject.put("leiping4", "");
							}
						}
					}
				} else {// 如果数据没有占满行补空
					jsonObject.put("xmzb4", "");
					jsonObject.put("guiding4", "");
					jsonObject.put("lingdian4", "");
					jsonObject.put("baiban4", "");
					jsonObject.put("zhongban4", "");
					jsonObject.put("riping4", "");
					jsonObject.put("leiping4", "");
				}

				jsonArray.add(jsonObject);
			}
			out.println(jsonArray);
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
