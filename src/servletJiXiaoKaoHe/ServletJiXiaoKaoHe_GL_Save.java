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
 * Servlet implementation class ServletJiXiaoKaoHe_GL_Save
 */
@WebServlet("/ServletJiXiaoKaoHe_GL_Save")
public class ServletJiXiaoKaoHe_GL_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletJiXiaoKaoHe_GL_Save() {
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
		String pxid=request.getParameter("pxid");
		String cj=request.getParameter("cj");
		String khxm=request.getParameter("khxm_name");
		String sfdz=request.getParameter("sfdz");
		sql = "INSERT INTO jxkh_gl (pxid, cj, khxm, sfdz) VALUES (?,?,?,?)"; //插入数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, pxid);
			ps.setString(2, cj);
			ps.setString(3, khxm);
			ps.setString(4, sfdz);
			n = ps.executeUpdate();
			DB.close(conn);
			ps.close();
			//返回JSON
			if (n>0) {
				out.print(true);
			}
			/*JSONObject jsonobj = new JSONObject();//JSON对象
			jsonobj.put("jzid", jzid);;
			out.print(jsonobj);*/
			} catch (SQLException e) {
				System.out.println(e);
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
