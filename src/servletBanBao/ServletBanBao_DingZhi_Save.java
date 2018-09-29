package servletBanBao;

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
 * Servlet implementation class ServletBanBao_DingZhi_Save
 */
@WebServlet("/ServletBanBao_DingZhi_Save")
public class ServletBanBao_DingZhi_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBanBao_DingZhi_Save() {
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
		String banbao_id=request.getParameter("banbao_id");
		String xm_id=request.getParameter("xm_id");
		String xm_name=request.getParameter("xm_name");
		String row=request.getParameter("row");
		String col=request.getParameter("col");
		String sfky=request.getParameter("sfky");
		sql = "INSERT INTO banbao_dz (banbao_id, xm_id, xm_name, `row`, col, sfky) VALUES (?,?,?,?,?,?)"; //插入数据
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			ps = conn.prepareStatement(sql);
			ps.setString(1, banbao_id);
			ps.setString(2, xm_id);
			ps.setString(3, xm_name);
			ps.setString(4, row);
			ps.setString(5, col);
			ps.setString(6, sfky);
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
