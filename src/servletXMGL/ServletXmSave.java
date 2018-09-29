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
 * Servlet implementation class ServletXmSave
 */
@WebServlet("/ServletXmSave")
public class ServletXmSave extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletXmSave() {
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
				String chejianid=request.getParameter("chejianid");
				String pxid=request.getParameter("pxid");
				String name=request.getParameter("name");
				String xmclass=request.getParameter("xmclass");
				if (xmclass.equals("液相")) {
					xmclass = "1";
				} else if((xmclass.equals("固相"))){
					xmclass = "0";
				}
				String zhbc=request.getParameter("zhbc");
				String yn=request.getParameter("yn");
				if (yn.equals("可用")) {
					yn = "1";
				} else if(yn.equals("不可用")){
					xmclass = "0";
				}
/*				System.out.println(chejianid);
				System.out.println(pxid);
				System.out.println(name);
				System.out.println(zhbc);
				System.out.println(yn);*/
				sql = "INSERT INTO xmgl (chejianid, pxid, name, xmclass, zhbc, yn) VALUES (?,?,?,?,?,?)"; //插入数据
				try {
					conn=DB.getConection();//利用封装好的类名来调用连接方法便可
					ps = conn.prepareStatement(sql);
					ps.setString(1, chejianid);
					ps.setString(2, pxid);
					ps.setString(3, name);
					ps.setString(4, xmclass);
					ps.setString(5, zhbc);
					ps.setString(6, yn);
					ps.executeUpdate();
					DB.close(conn);
					ps.close();
					//返回JSON
						JSONObject jsonobj = new JSONObject();//JSON对象
						jsonobj.put("chejianid", chejianid);
						jsonobj.put("pxid", pxid);
						jsonobj.put("name", name);
						jsonobj.put("xmclass", xmclass);
						jsonobj.put("zhbc", zhbc);
						jsonobj.put("yn", yn);
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
