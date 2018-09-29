package servletJiXiaoKaoHe;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletJiXiaoKaoHe_DZ_Update
 */
@WebServlet("/ServletJiXiaoKaoHe_DZ_Update")
public class ServletJiXiaoKaoHe_DZ_Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_DZ_Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
    	response.setContentType("json/html;charset=UTF-8");
    	response.setCharacterEncoding("utf-8");
    	request.setCharacterEncoding("utf-8");
		Connection conn=null;//定义为空值
		String sql = null;
		PreparedStatement ps = null;
		PrintWriter out =response.getWriter();
		
		String jxkhdz_id=request.getParameter("jxkhdz_id");
		String jxkhgl_id=request.getParameter("jxkhgl_id");
		String sjkxm_id=request.getParameter("sjkxm_id");
		String sjkxm_name=request.getParameter("sjkxm_name");
		String sjkzb_name=request.getParameter("sjkzb_name");
		String sjkzb_bh=request.getParameter("sjkzb_bh");
		String start_date=request.getParameter("start_date");
		String start_time=request.getParameter("start_time");
		String end_date=request.getParameter("end_date");
		String end_time=request.getParameter("end_time");
		String khfs=request.getParameter("khfs");
		if (khfs.equals("个数")) {
			khfs = "1";
		} else if (khfs.equals("合格率")) {
			khfs = "2";
		} else if (khfs.equals("平均值")) {
			khfs = "3";
		}
		String khbz=request.getParameter("khbz");
		String sjclass=request.getParameter("sjclass");
		
		sql = "UPDATE jxkh_dz SET sjkxm_id=?,sjkxm_name=?,sjkzb_name=?,sjkzb_bh=?,start_date=?,start_time=?,end_date=?,end_time=?,khfs=?,khbz=?,sjclass=? WHERE jxkhdz_id=? "; //修改数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, sjkxm_id);
			ps.setString(2, sjkxm_name);
			ps.setString(3, sjkzb_name);
			ps.setString(4, sjkzb_bh);
			ps.setString(5, start_date);
			ps.setString(6, start_time);
			ps.setString(7, end_date);
			ps.setString(8, end_time);
			ps.setString(9, khfs);
			ps.setString(10, khbz);
			ps.setString(11, sjclass);
			ps.setString(12, jxkhdz_id);
			n = ps.executeUpdate();
			//返回JSON
			if (n>0) {
				out.print(true);
			}
			DB.close(conn);
			ps.close();
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
