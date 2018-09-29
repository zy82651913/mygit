package servletSuJuLR;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletXMLB_Tree
 */
@WebServlet("/ServletXMLB_Tree")
public class ServletXMLB_Tree extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXMLB_Tree() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	//response.setContentType("text/html;charset=UTF-8");
    	response.setContentType("text/json; charset=utf-8");
    	
		Connection conn=null;//定义为空值
	    ResultSet jizhanrs = null;
	    ResultSet chejianrs = null;
	    ResultSet xiangmurs = null;
		String jizhansql = null;//查询技站数据
		String chejiansql = null;//查询车间数据
		String xiangmusql = null;//查询项目数据
		PrintWriter out =response.getWriter();
		jizhansql = "SELECT\n" +
				"jizhan.id,\n" +
				"jizhan.`name`\n" +
				"FROM\n" +
				"jizhan\n" +
				"ORDER BY\n" +
				"jizhan.pxid ASC";
		chejiansql = "SELECT\n" +
				"chejian.`name` AS chejianname,\n" +
				"chejian_jizhan.jizhanid,\n" +
				"chejian_jizhan.chejianid\n" +
				"FROM\n" +
				"jizhan\n" +
				"INNER JOIN chejian_jizhan ON jizhan.id = chejian_jizhan.jizhanid\n" +
				"INNER JOIN chejian ON chejian.id = chejian_jizhan.chejianid\n" +
				"ORDER BY\n" +
				"chejian.pxid ASC";
		xiangmusql = "SELECT\n" +
				"xmgl.id,\n" +
				"xmgl.chejianid,\n" +
				"xmgl.`name`,\n" +
				"xmgl.`xmclass`,\n" +
				"xmgl.zhbc\n" +
				"FROM\n" +
				"xmgl\n" +
				"ORDER BY\n" +
				"xmgl.pxid ASC";
		try {
			JSONObject jizhanobj = new JSONObject();//JSON对象
			JSONObject chejianobj = new JSONObject();//JSON对象
			JSONObject xiangmuobj = new JSONObject();//JSON对象
			JSONObject jzobj = new JSONObject();//JSON对象
			
			JSONArray jizhanarray = new JSONArray(); //JSON数组
			JSONArray chejianarray = new JSONArray(); //JSON数组
			JSONArray xiangmuarray = new JSONArray(); //JSON数组
			JSONArray jzarray = new JSONArray(); //JSON数组
			
			conn = DB.getConection();//利用封装好的类名来调用连接方法便可
			Statement jizhanstmt = conn.createStatement();//创建Statement对象
			Statement chejianstmt = conn.createStatement();//创建Statement对象
			Statement xiangmustmt = conn.createStatement();//创建Statement对象
			jizhanrs = jizhanstmt.executeQuery(jizhansql);
			chejianrs = chejianstmt.executeQuery(chejiansql);
			xiangmurs = xiangmustmt.executeQuery(xiangmusql);
			while (jizhanrs.next()) {
				jizhanobj.put("jizhanid", jizhanrs.getString("id"));
				jizhanobj.put("jizhanname", jizhanrs.getString("name"));
				jizhanarray.add(jizhanobj);
			}
			while (chejianrs.next()) {
				chejianobj.put("jizhanid", chejianrs.getString("jizhanid"));
				chejianobj.put("chejianid", chejianrs.getString("chejianid"));
				chejianobj.put("chejianname", chejianrs.getString("chejianname"));
				chejianarray.add(chejianobj);
			}
			while (xiangmurs.next()) {
				xiangmuobj.put("chejianid", xiangmurs.getString("chejianid"));
				xiangmuobj.put("xmid", xiangmurs.getString("id"));
				xiangmuobj.put("xiangmuname", xiangmurs.getString("name"));
				xiangmuobj.put("xmclass", xiangmurs.getString("xmclass"));
				xiangmuobj.put("zhbc", xiangmurs.getString("zhbc"));
				xiangmuarray.add(xiangmuobj);
			}
			
			for (int i = 0; i < jizhanarray.size(); i++) {
				JSONObject cjobj = new JSONObject();//JSON对象
				JSONArray cjarray = new JSONArray(); //JSON数组
				jzobj.put("id", jizhanarray.getJSONObject(i).getString("jizhanid"));
				jzobj.put("text", jizhanarray.getJSONObject(i).getString("jizhanname"));
				jzobj.put("state", "open");
				jzobj.put("checked", "false");
				for (int j = 0; j < chejianarray.size(); j++) {
					if (jizhanarray.getJSONObject(i).getString("jizhanid").equals(chejianarray.getJSONObject(j).getString("jizhanid"))) {
						JSONObject xmobj = new JSONObject();//JSON对象
						JSONArray xmarray = new JSONArray(); //JSON数组
						cjobj.put("id", chejianarray.getJSONObject(j).getString("jizhanid"));
						cjobj.put("text", chejianarray.getJSONObject(j).getString("chejianname"));
						
						cjobj.put("checked", "false");
						for (int j2 = 0; j2 < xiangmuarray.size(); j2++) {
							if (chejianarray.getJSONObject(j).getString("chejianid").equals(xiangmuarray.getJSONObject(j2).getString("chejianid"))) {
								xmobj.put("id", xiangmuarray.getJSONObject(j2).getString("xmid"));
								xmobj.put("text", xiangmuarray.getJSONObject(j2).getString("xiangmuname"));
								xmobj.put("xmclass", xiangmuarray.getJSONObject(j2).getString("xmclass"));
								xmobj.put("zhbc", xiangmuarray.getJSONObject(j2).getString("zhbc"));
								xmobj.put("checked", "false");
								xmarray.add(xmobj);
							}
						}
						cjobj.put("children", xmarray);
						cjarray.add(cjobj);
					}
				}
				jzobj.put("children", cjarray);
				jzarray.add(jzobj);
			}

			out.println(jzarray);

			DB.close(conn);
			jizhanrs.close();
			chejianrs.close();
			xiangmurs.close();
			jizhanstmt.close();
			chejianstmt.close();
			xiangmustmt.close();
			} catch (SQLException e) {
			e.printStackTrace();
		
			}finally{
			
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
