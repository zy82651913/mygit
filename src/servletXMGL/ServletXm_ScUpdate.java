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
 * Servlet implementation class ServletXm_ScUpdate
 */
@WebServlet("/ServletXm_ScUpdate")
public class ServletXm_ScUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXm_ScUpdate() {
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
				String xmid=request.getParameter("xmid");
				String pxid=request.getParameter("pxid");
				String zb_name=request.getParameter("zb_name");
				String zbbh=request.getParameter("zbbh");
				String mathclass=request.getParameter("mathclass");
				String math=request.getParameter("math");
				String qxdyzb=request.getParameter("qxdyzb");
				String xsdws=request.getParameter("xsdws");
				String bbyn=request.getParameter("bbyn");
				if (math=="") {
					math=null;
					}
				if (mathclass.equals("一般公式")) {
					mathclass="1";
					}
				if (mathclass.equals("曲线公式")) {
					mathclass="0";
					}
				if (bbyn.equals("显示")) {
					bbyn="1";
				}
				if (bbyn.equals("不显示")) {
					bbyn="0";
				}
				sql = "UPDATE sczb SET xmid=?,pxid=?,zb_name=?,zbbh=?,mathclass=?,math=?,qxdyzb=?,xsdws=?,bbyn=? WHERE id=? "; //修改数据
				try {
						conn=DB.getConection();//利用封装好的类名来调用连接方法便可
						ps = conn.prepareStatement(sql);
						ps.setString(1, xmid);
						ps.setString(2, pxid);
						ps.setString(3, zb_name);
						ps.setString(4, zbbh);
						ps.setString(5, mathclass);
						ps.setString(6, math);
						ps.setString(7, qxdyzb);
						ps.setString(8, xsdws);
						ps.setString(9, bbyn);
						ps.setString(10, id);
						ps.executeUpdate();
					//返回JSON
						JSONObject jsonobj = new JSONObject();//JSON对象
						jsonobj.put("id", id);
						jsonobj.put("xmid", xmid);
						jsonobj.put("pxid", pxid);
						jsonobj.put("zb_name", zb_name);
						jsonobj.put("zbbh", zb_name);
						jsonobj.put("mathclass", mathclass);
						jsonobj.put("math", math);
						jsonobj.put("qxdyzb", qxdyzb);
						jsonobj.put("xsdws", xsdws);
						jsonobj.put("bbyn", bbyn);
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
