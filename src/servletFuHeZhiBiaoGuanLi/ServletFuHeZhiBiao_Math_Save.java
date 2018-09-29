package servletFuHeZhiBiaoGuanLi;

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
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletFuHeZhiBiao_Math_Update
 */
@WebServlet("/ServletFuHeZhiBiao_Math_Save")
public class ServletFuHeZhiBiao_Math_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeZhiBiao_Math_Save() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 设置响应内容类型
				response.setContentType("text/json;charset=UTF-8");
		    	response.setCharacterEncoding("utf-8");
		    	request.setCharacterEncoding("utf-8");
				Connection conn=null;//定义为空值
				String sql = null;
				PrintWriter out =response.getWriter();
				PreparedStatement ps = null;
				
				String fhzb_id=request.getParameter("fhzb_id");
				String fhzb_math=request.getParameter("fuhezb_math");
				int n = 0;
				
				sql = "UPDATE fhzbgl SET fhzb_math=? WHERE id=? "; //修改数据
				try {
					conn=DB.getConection();//利用封装好的类名来调用连接方法便可
					ps = conn.prepareStatement(sql);
					ps.setString(1, fhzb_math);
					ps.setString(2, fhzb_id);
					n = ps.executeUpdate();
					if (n>0) {
						out.print(true);
					}else {
						out.print(false);
					}
					DB.close(conn);
					ps.close();
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
