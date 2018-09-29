package servletXiaoBao;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletXiaoBao_DataGridCx
 */
@WebServlet("/ServletXiaoBao_DataGridCx")
public class ServletXiaoBao_DataGridCx extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXiaoBao_DataGridCx() {
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
    	String sql;
    	JSONObject jsonObject = new JSONObject();//向前台传数据的json对象
    	JSONArray jsonArray = new JSONArray();//向前台传数据的json数组
    	JSONObject cjidObj = new JSONObject();//车间id json对象
    	JSONArray cjidArray = new JSONArray();//车间id json数组
    	JSONObject dataObj = new JSONObject();//数据json对象
    	JSONArray dataArray = new JSONArray();//数据 json数组
    	String sj=request.getParameter("sj");
    	JSONObject sjobj=JSONObject.fromObject(sj);//把前台传入的json对象字符串转为json对象
    	String hysj_date=sjobj.getString("hysj_date");//化验日期
    	String hy_bc=sjobj.getString("hy_bc");//化验班次
    	JSONArray chejianidArr = sjobj.getJSONArray("chejianid");//把json对象转换成json数组
    	int[] cjidArr = new int[chejianidArr.size()];
    	for (int i = 0; i < chejianidArr.size(); i++) {//json数组转为int数组
			cjidArr[i] = chejianidArr.getInt(i);
		}
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		for (int i = 0; i < cjidArr.length; i++) {
				int cjid = cjidArr[i];
				//查询有数据的项目输出指标，需要获取输出指标名称
				/*sql = "SELECT DISTINCT\n" +
	    				"sczb_data.xm_name,\n" +
	    				"sczb_data.xm_id,\n" +
	    				"sczb_data.sczb_data\n" +
	    				"xmgl.pxid\n" +
	    				"FROM\n" +
	    				"xmgl\n" +
	    				"INNER JOIN sczb_data ON xmgl.id = sczb_data.xm_id\n" +
	    				"WHERE\n" +
	    				"xmgl.chejianid = "+cjid+" AND\n" +
	        			"sczb_data.hysj_date = '"+hysj_date+"' AND\n" +
	        			"sczb_data.hy_bc = "+hy_bc+" \n" +
	    				"ORDER BY\n" +
	    				"xmgl.pxid ASC";*/
				sql = "SELECT DISTINCT\n" +
						"sczb_data.xm_name,\n" +
						"sczb_data.xm_id,\n" +
						"sczb_data.sczb_data,\n" +
						"xmgl.pxid\n" +
						"FROM\n" +
						"xmgl\n" +
						"INNER JOIN sczb_data ON xmgl.id = sczb_data.xm_id\n" +
						"WHERE\n" +
						"xmgl.chejianid = "+cjid+" AND\n" +
						"sczb_data.hysj_date = '"+hysj_date+"' AND\n" +
						"sczb_data.hy_bc = "+hy_bc+"\n" +
						"ORDER BY\n" +
						"xmgl.pxid ASC";
				rs=stmt.executeQuery(sql);
				
				while(rs.next()){
					int t = 0;
					cjidObj.put("xm_name", rs.getString("xm_name"));
					cjidObj.put("sczb_data", rs.getString("sczb_data"));
					if (cjidArray.size()==0) {
						cjidArray.add(cjidObj);
					} else {
						for (int j = 0; j < cjidArray.size(); j++) {
							/*System.out.println(cjidObj.get("xm_name"));
							System.out.println(cjidArray.getJSONObject(j).get("xm_name"));
							System.out.println(cjidObj.get("xm_name").equals(cjidArray.getJSONObject(j).get("xm_name")));*/
							if (cjidObj.get("xm_name").equals(cjidArray.getJSONObject(j).get("xm_name"))) {
								t++;
								break;
							}
						}
						if (t==0) {
							cjidArray.add(cjidObj);
						}
					}

    			}
			}
			jsonObject.put("xiangmu", cjidArray);//取到的项目名称
			jsonArray.add(jsonObject);
			for (int i = 0; i < cjidArr.length; i++) {
				int cjid = cjidArr[i];
				sql = "SELECT\n" +
						"sczb_data.id,\n" +
						"sczb_data.srzb_data_id,\n" +
						"sczb_data.xm_id,\n" +
						"sczb_data.xm_name,\n" +
						"sczb_data.hysj_date,\n" +
						"sczb_data.hysjd_time,\n" +
						"sczb_data.gx_zhbc,\n" +
						"sczb_data.hy_bc,\n" +
						"sczb_data.sczb_data,\n" +
						"sczb_data.beizhu,\n" +
						"sczb_data.beizhu_name,\n" +
						"sczb_data.shenhe\n" +
						"FROM\n" +
						"xmgl\n" +
						"INNER JOIN sczb_data ON xmgl.id = sczb_data.xm_id\n" +
						"WHERE\n" +
						"xmgl.chejianid = "+cjid+" AND\n" +
	        			"sczb_data.hysj_date = '"+hysj_date+"' AND\n" +
	        			"sczb_data.hy_bc = "+hy_bc+" \n" +
						"ORDER BY\n" +
						"xmgl.pxid ASC,\n" +
						"sczb_data.hysjd_time ASC";
				rs=stmt.executeQuery(sql);
				while(rs.next()){
					dataObj.put("xm_name", rs.getString("xm_name"));
					dataObj.put("sczb_data", rs.getString("sczb_data"));
					dataObj.put("gx_zhbc", rs.getString("gx_zhbc"));
					dataObj.put("beizhu_name", rs.getString("beizhu_name"));
					dataObj.put("shenhe", rs.getString("shenhe"));
					String st = rs.getString("hysjd_time");
					st = st.substring(0, st.length()-3);
					dataObj.put("hysjd_time",st );
					dataArray.add(dataObj);
				}
			}
			jsonObject.clear();
			jsonObject.put("sczb_data", dataArray);
			jsonArray.add(jsonObject);
    		out.print(jsonArray);
    		rs.close();
    		stmt.close();
    		conn.close();
    		DB.close(conn);
		} catch (SQLException e) {
			// TODO: handle exception
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
