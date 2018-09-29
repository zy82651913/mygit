package servletXMGL;

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
 * Servlet implementation class ServletXm_SrSave
 */
@WebServlet("/ServletXm_SrSave")
public class ServletXm_SrSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXm_SrSave() {
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
		PrintWriter out =response.getWriter();
		String xmid=request.getParameter("xmid");
		String pxid=request.getParameter("pxid");
		String srname=request.getParameter("srname");
		String zbbh=request.getParameter("zbbh");
		String csvalue=request.getParameter("csvalue");
/*				System.out.println(chejianid);
		System.out.println(pxid);
		System.out.println(name);
		System.out.println(zhbc);
		System.out.println(yn);*/
		sql = "INSERT INTO srzb (xmid, pxid, srname, zbbh, csvalue) VALUES (?,?,?,?,?)"; //插入数据
		try {
			if (csvalue=="") {
				conn=DB.getConection();//利用封装好的类名来调用连接方法便可
				sql = "INSERT INTO srzb (xmid, pxid, srname, zbbh) VALUES (?,?,?,?)"; //插入数据
				ps = conn.prepareStatement(sql);
				ps.setString(1, xmid);
				ps.setString(2, pxid);
				ps.setString(3, srname);
				ps.setString(4, zbbh);
				ps.executeUpdate();
			}else {
				conn=DB.getConection();//利用封装好的类名来调用连接方法便可
				ps = conn.prepareStatement(sql);
				ps.setString(1, xmid);
				ps.setString(2, pxid);
				ps.setString(3, srname);
				ps.setString(4, zbbh);
				ps.setString(5, csvalue);
				ps.executeUpdate();
			}
			DB.close(conn);
			ps.close();
			//返回JSON
				JSONObject jsonobj = new JSONObject();//JSON对象
				jsonobj.put("xmid", xmid);
				jsonobj.put("pxid", pxid);
				jsonobj.put("srname", srname);
				jsonobj.put("zbbh", zbbh);
				jsonobj.put("csvalue", csvalue);
				out.print(jsonobj);
			} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			
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
