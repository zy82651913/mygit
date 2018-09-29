package servletQXGL;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SelectQuXianNiHe_Save
 */
@WebServlet("/SelectQuXianNiHe_Save")
public class SelectQuXianNiHe_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectQuXianNiHe_Save() {
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
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Statement stmt = null;
    	String sql;
    	int n = -1;
    	JSONObject jsonobj = new JSONObject();//JSON对象
    	String json=request.getParameter("json");
    	JSONObject j=JSONObject.fromObject(json);//把前台传入的json对象字符串转为json对象
    	String qx_name=j.getString("qxname");//获得曲线名称
    	String qxid=j.getString("qxid");//获得曲线ID
    	String gongshi=j.getString("gongshi");//获得公式
    	String mnds=j.getString("v");//获得模拟点数
    	String A1=j.getString("A1");//获得A1
    	String A2=j.getString("A2");//获得A2
    	String A3=j.getString("A3");//获得A3
    	String A4=j.getString("A4");//获得A4
    	String pxid=j.getString("paixuid");//获得备注
    	String beizhu=j.getString("beizhu");//获得备注
    	JSONArray xx= j.getJSONArray("x");//把json对象中的X数组转换成json数组
    	JSONArray yy= j.getJSONArray("y");//把json对象中的y数组转换成json数组
    	double[] x=new double[xx.size()];//新建double数组
    	double[] y=new double[yy.size()];//新建double数组
    	for(int i=0;i<xx.size();i++){//遍历字符串数组转换成double数组
    		x[i]=xx.getDouble(i);
    		y[i]=yy.getDouble(i);
    	}
    	if (Integer.parseInt(qxid)==0) {//插入新曲线
    		try {
    			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    			sql = "SELECT * FROM qxgl WHERE qx_name='"+qx_name+"'"; //查询rolename
    			stmt = conn.createStatement();
    			rs=stmt.executeQuery(sql);
    			if (rs.next()) {
					out.print(true);
				}else {
					sql = "INSERT INTO qxgl (pxid,qx_name,a1,a2,a3,a4,qx_math,qx_mnds,qx_note) VALUES (?,?,?,?,?,?,?,?,?)"; //插入数据"
    				ps = conn.prepareStatement(sql);
    				ps.setString(1, pxid);
    				ps.setString(2, qx_name);
    				ps.setString(3, A1);
    				ps.setString(4, A2);
    				ps.setString(5, A3);
    				ps.setString(6, A4);
    				ps.setString(7, gongshi);
    				ps.setString(8, mnds);
    				ps.setString(9, beizhu);
    				ps.executeUpdate();
    				sql = "SELECT id FROM qxgl WHERE qx_name='"+qx_name+"'";//获得插入数据后的自增ID
    				stmt = conn.createStatement();
        			rs=stmt.executeQuery(sql);
        			while(rs.next()){
        				qxid=rs.getString("id");
        			}
        			sql = "INSERT INTO qxlr (qxid,x,y) VALUES(?,?,?)";
        			for(int i=0;i<x.length;i++){
        				ps = conn.prepareStatement(sql);
        				ps.setString(1, qxid);
        				ps.setDouble(2, x[i]);
        				ps.setDouble(3, y[i]);
        				ps.executeUpdate();
        			}
        			ps.close();
        	    	jsonobj.put("qxid", qxid);
        	    	jsonobj.put("qxname", qx_name);
        	    	out.println(jsonobj);
				}
    			DB.close(conn);
    			stmt.close();
    			rs.close();
    			
    			} catch (SQLException e) {
    			e.printStackTrace();
    		}
		}else {//更新曲线
			try {
				conn=DB.getConection();//利用封装好的类名来调用连接方法便可
				sql = "UPDATE qxgl SET pxid=?,qx_name=?,a1=?,a2=?,a3=?,a4=?,qx_math=?,qx_mnds=?,qx_note=? WHERE id=?"; //插入数据"
				ps = conn.prepareStatement(sql);
				ps.setString(1, pxid);
				ps.setString(2, qx_name);
				ps.setString(3, A1);
				ps.setString(4, A2);
				ps.setString(5, A3);
				ps.setString(6, A4);
				ps.setString(7, gongshi);
				ps.setString(8, mnds);
				ps.setString(9, beizhu);
				ps.setString(10, qxid);
				n=ps.executeUpdate();
				if (n<1) {
					out.println("e");
				}else {
					sql = "DELETE FROM qxlr WHERE qxid='"+qxid+"'";//先删除曲线录入表中的数据
					stmt = conn.createStatement();
					n=stmt.executeUpdate(sql);//删除曲线录入数据
					if (n<1) {
						out.println("e");
					}else {
						sql = "INSERT INTO qxlr (qxid,x,y) VALUES(?,?,?)";//再插入新的数据
	        			for(int i=0;i<x.length;i++){
	        				ps = conn.prepareStatement(sql);
	        				ps.setString(1, qxid);
	        				ps.setDouble(2, x[i]);
	        				ps.setDouble(3, y[i]);
	        				n=ps.executeUpdate();
	        				if (n<1) {
	        					out.println("e");
							}
	        			}
					}
					
        			ps.close();
        	    	jsonobj.put("qxid", qxid);
        	    	jsonobj.put("qxname", qx_name);
        	    	out.println(jsonobj);
				}
				DB.close(conn);
    			stmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
