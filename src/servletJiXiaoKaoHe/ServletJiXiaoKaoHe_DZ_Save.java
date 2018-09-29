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
 * Servlet implementation class ServletJiXiaoKaoHe_DZ_Save
 */
@WebServlet("/ServletJiXiaoKaoHe_DZ_Save")
public class ServletJiXiaoKaoHe_DZ_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_DZ_Save() {
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
		String sql = null;
		PreparedStatement ps = null;
		PrintWriter out =response.getWriter();
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
		
		sql = "INSERT INTO jxkh_dz (jxkhgl_id, sjkxm_id, sjkxm_name, sjkzb_name, sjkzb_bh, start_date, start_time, end_date, end_time, khfs, khbz, sjclass) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"; //插入数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, jxkhgl_id);
			ps.setString(2, sjkxm_id);
			ps.setString(3, sjkxm_name);
			ps.setString(4, sjkzb_name);
			ps.setString(5, sjkzb_bh);
			ps.setString(6, start_date);
			ps.setString(7, start_time);
			ps.setString(8, end_date);
			ps.setString(9, end_time);
			ps.setString(10, khfs);
			ps.setString(11, khbz);
			ps.setString(12, sjclass);
			n = ps.executeUpdate();
			ps.close();
			//返回JSON
			if (n>0) {
				sql = "UPDATE jxkh_gl SET sfdz=? WHERE jxkhgl_id=? "; //修改是否订制
				ps = conn.prepareStatement(sql);
				ps.setString(1, "是");
				ps.setString(2, jxkhgl_id);
				n = ps.executeUpdate();
				out.print(true);
				ps.close();
				DB.close(conn);
			}else {
				out.print(false);
				DB.close(conn);
			}
			/*JSONObject jsonobj = new JSONObject();//JSON对象
			jsonobj.put("jzid", jzid);;
			out.print(jsonobj);*/
			} catch (SQLException e) {
				DB.close(conn);
				System.out.println(e);
				out.print(e);
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
