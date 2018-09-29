package servletFuHeZhiBiaoGuanLi;

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

import hys.DB;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletFuHeZhiBiao_Delete
 */
@WebServlet("/ServletFuHeZhiBiao_Delete")
public class ServletFuHeZhiBiao_Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeZhiBiao_Delete() {
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
		Connection conn=null;//定义为空值
		Statement stmt = null;
		PrintWriter out =response.getWriter();
		JSONObject jsonobj = new JSONObject();//JSON对象
		String sql = null;
		int n = -1;
		String fhzb_id=request.getParameter("id");
		sql = "DELETE FROM fhzbgl WHERE id='"+fhzb_id+"'";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			n=stmt.executeUpdate(sql);//删除数据 
			//查询来源指标id
			sql = "SELECT\n" +
					"fhzb_laiyuan.id,\n" +
					"fhzb_laiyuan.fhzb_id,\n" +
					"fhzb_laiyuan.xmid,\n" +
					"fhzb_laiyuan.xm_name,\n" +
					"fhzb_laiyuan.sc_name,\n" +
					"fhzb_laiyuan.sc_bh,\n" +
					"fhzb_laiyuan.zhbc\n" +
					"FROM\n" +
					"fhzb_laiyuan\n" +
					"WHERE\n" +
					"fhzb_laiyuan.fhzb_id = '"+fhzb_id+"'";
			stmt = conn.createStatement();//创建Statement对象
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				String id = rs.getString("id");
				sql = "DELETE FROM fhzb_laiyuan WHERE id='"+id+"'";
				stmt.executeUpdate(sql);//删除来源指标数据 
			}
			DB.close(conn);
			rs.close();
			stmt.close();
			if (n>0) {
				//删除成功后，要想刷新可编辑数据表，需要向前台传一个{"success":"true"}
				jsonobj.put("success", "true");
				out.print(jsonobj);
			}else {
				jsonobj.put("success", "false");
				out.print(jsonobj);
			}
			DB.close(conn);
			stmt.close();
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
