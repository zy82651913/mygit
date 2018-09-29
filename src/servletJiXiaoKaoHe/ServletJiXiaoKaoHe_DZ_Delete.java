package servletJiXiaoKaoHe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.spec.PSource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletJiXiaoKaoHe_DZ_Delete
 */
@WebServlet("/ServletJiXiaoKaoHe_DZ_Delete")
public class ServletJiXiaoKaoHe_DZ_Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_DZ_Delete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		Statement stmt = null;
		PrintWriter out =response.getWriter();
		JSONObject jsonobj = new JSONObject();//JSON对象
		String sql = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		int n = -1;
		String jxkhdz_id=request.getParameter("id");
		String jxkhgl_id=request.getParameter("jxkhgl_id");
		sql = "DELETE FROM jxkh_dz WHERE jxkhdz_id='"+jxkhdz_id+"'";
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			stmt = conn.createStatement();
			n=stmt.executeUpdate(sql);//删除数据 
			if (n>0) {
				sql = "SELECT jxkh_dz.jxkhgl_id FROM jxkh_dz WHERE jxkh_dz.jxkhdz_id = '"+jxkhdz_id+"'";
				rs = stmt.executeQuery(sql);
				if (!rs.next()) {
					sql = "UPDATE jxkh_gl SET sfdz=? WHERE jxkhgl_id=? "; //修改是否订制
					ps = conn.prepareStatement(sql);
					ps.setString(1, "否");
					ps.setString(2, jxkhgl_id);
					n = ps.executeUpdate();
					ps.close();
				}
				rs.close();
				//删除成功后，要想刷新可编辑数据表，需要向前台传一个{"success":"true"}
				jsonobj.put("success", "true");
				out.print(jsonobj);
			}else{
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
