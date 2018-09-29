package servletFuHeZhiBiaoGuanLi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.DB;

/**
 * Servlet implementation class ServletFuHeZhiBiao_save
 */
@WebServlet("/ServletFuHeZhiBiao_save")
public class ServletFuHeZhiBiao_save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeZhiBiao_save() {
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
				PreparedStatement ps = null;
				ResultSet rs = null;
				PrintWriter out =response.getWriter();
				
				String fhzb_id=request.getParameter("fhzb_id");
				String pxid=request.getParameter("pxid");
				String fhzb_name=request.getParameter("fhzb_name");
				String fhzb_bh=request.getParameter("fhzb_bh");
				//String fuhezb_math=request.getParameter("fuhezb_math");
				String xsws=request.getParameter("xsws");
				
				try {
					conn=DB.getConection();
					sql = "INSERT INTO fhzbgl (pxid, fhzb_name, fhzb_bh, xsws) VALUES (?,?,?,?)"; //插入数据
					ps = conn.prepareStatement(sql);
					ps.setString(1, pxid);
					ps.setString(2, fhzb_name);
					ps.setString(3, fhzb_bh);
					ps.setString(4, xsws);
					ps.executeUpdate();
					
					sql = "SELECT LAST_INSERT_ID()";//查询自增id
					Statement stmt = conn.createStatement();//创建Statement对象
					rs = stmt.executeQuery(sql);
					int id = 0;
					while(rs.next()){
				        id = rs.getInt(1);
				    }
					out.println(id);
					DB.close(conn);
					ps.close();
					
					//out.print(fhzb_id);
					
				} catch (Exception e) {
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
