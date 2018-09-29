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
 * Servlet implementation class ServletXmUpdate
 */
@WebServlet("/ServletXmUpdate")
public class ServletXmUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXmUpdate() {
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
				String id=request.getParameter("id");
				String chejianid=request.getParameter("chejianid");
				String pxid=request.getParameter("pxid");
				String name=request.getParameter("name");
				String xmclass=request.getParameter("xmclass");
				if (xmclass.equals("液相")) {
					xmclass = "1";
				} else if (xmclass.equals("固相")) {
					xmclass = "0";
				}
				String zhbc=request.getParameter("zhbc");
				String yn=request.getParameter("yn");
				if (yn.equals("可用")) {
					yn = "1";
				} else if (yn.equals("不可用")) {
					yn = "0";
				}
/*				System.out.println(id);
				System.out.println(chejianid);
				System.out.println(pxid);
				System.out.println(name);
				System.out.println(zhbc);
				System.out.println(yn);*/
				sql = "UPDATE xmgl SET chejianid=?,pxid=?,name=?,xmclass=?,zhbc=?,yn=? WHERE id=? "; //修改数据
				try {
					conn=DB.getConection();//利用封装好的类名来调用连接方法便可
					ps = conn.prepareStatement(sql);
					ps.setString(1, chejianid);
					ps.setString(2, pxid);
					ps.setString(3, name);
					ps.setString(4, xmclass);
					ps.setString(5, zhbc);
					ps.setString(6, yn);
					ps.setString(7, id);
					ps.executeUpdate();
					sql = "UPDATE srzb_data SET xm_name=? WHERE xm_id=? "; //修改输入指标名称
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, id);
					ps.executeUpdate();
					sql = "UPDATE sczb_data SET xm_name=? WHERE xm_id=? "; //修改输出指标名称
					ps = conn.prepareStatement(sql);
					ps.setString(1, name);
					ps.setString(2, id);
					ps.executeUpdate();
					
					//返回JSON
						JSONObject jsonobj = new JSONObject();//JSON对象
						jsonobj.put("chejianid", chejianid);
						jsonobj.put("pxid", pxid);
						jsonobj.put("name", name);
						jsonobj.put("zhbc", zhbc);
						jsonobj.put("yn", yn);
						out.print(jsonobj);
						DB.close(conn);
						ps.close();
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
