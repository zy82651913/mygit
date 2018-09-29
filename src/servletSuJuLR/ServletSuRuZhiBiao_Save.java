package servletSuJuLR;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.eclipse.jdt.internal.compiler.ast.AND_AND_Expression;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletSuRuZhiBiao_Save
 */
@WebServlet("/ServletSuRuZhiBiao_Save")
public class ServletSuRuZhiBiao_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletSuRuZhiBiao_Save() {
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
    	String srzb_data_id = null;
    	String sr_data=request.getParameter("sr_data");
    	JSONObject data=JSONObject.fromObject(sr_data);//把前台传入的json对象字符串转为json对象
    	String xm_id = data.getString("xm_id");
    	String xm_name = data.getString("xm_name");
    	String hysj_date = data.getString("hysj_date");
    	String hysjd_time = data.getString("hysjd_time");
    	String hy_bc = data.getString("hy_bc");
    	String srzb_data = data.getString("srzb_data");
    	
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		sql = "SELECT id FROM srzb_data WHERE xm_id='"+xm_id+"' AND hysj_date='"+hysj_date+"' AND hy_bc='"+hy_bc+"' AND hysjd_time='"+hysjd_time+"'";
    		stmt = conn.createStatement();
			rs=stmt.executeQuery(sql);
			if (rs.next()) {//如果查到了数据更新数据
				sql = "UPDATE srzb_data SET xm_id=?,xm_name=?,hysj_date=?,hysjd_time=?,hy_bc=?,srzb_data=? WHERE xm_id=? AND hysj_date=? AND hysjd_time=?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, xm_id);
				ps.setString(2, xm_name);
				ps.setString(3, hysj_date);
				ps.setString(4, hysjd_time);
				ps.setString(5, hy_bc);
				ps.setString(6, srzb_data);
				ps.setString(7, xm_id);
				ps.setString(8, hysj_date);
				ps.setString(9, hysjd_time);
				ps.executeUpdate();
				stmt.close();
    			rs.close();
				sql = "SELECT id FROM srzb_data WHERE xm_id='"+xm_id+"' AND hysj_date='"+hysj_date+"' AND hy_bc='"+hy_bc+"' AND hysjd_time='"+hysjd_time+"'";
	    		stmt = conn.createStatement();
				rs=stmt.executeQuery(sql);
				while(rs.next()){
					srzb_data_id=rs.getString("id");
    			}
				out.print(srzb_data_id);
				DB.close(conn);
    			stmt.close();
    			rs.close();
    			ps.close();
			} else {
				sql = "INSERT INTO srzb_data (xm_id,xm_name,hysj_date,hysjd_time,hy_bc,srzb_data) VALUES(?,?,?,?,?,?)";
				ps = conn.prepareStatement(sql);
				ps.setString(1, xm_id);
				ps.setString(2, xm_name);
				ps.setString(3, hysj_date);
				ps.setString(4, hysjd_time);
				ps.setString(5, hy_bc);
				ps.setString(6, srzb_data);
				ps.executeUpdate();
				stmt.close();
    			rs.close();
				sql = "SELECT id FROM srzb_data WHERE xm_id='"+xm_id+"' AND hysj_date='"+hysj_date+"' AND hy_bc='"+hy_bc+"' AND hysjd_time='"+hysjd_time+"'";
	    		stmt = conn.createStatement();
				rs=stmt.executeQuery(sql);
				while(rs.next()){
					srzb_data_id=rs.getString("id");
    			}
				out.print(srzb_data_id);
				DB.close(conn);
    			stmt.close();
    			rs.close();
    			ps.close();
			}
    	} catch (SQLException e) {
    		e.printStackTrace();
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
