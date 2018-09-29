package servletDiaoDu;

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
 * Servlet implementation class ServletDD_RiBao_BieMian_Save
 */
@WebServlet("/ServletDD_RiBao_BieMian_Save")
public class ServletDD_RiBao_BieMian_Save extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletDD_RiBao_BieMian_Save() {
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
		String xmid=request.getParameter("xmid");
		String xm_name=request.getParameter("xm_name");
		String zbbh=request.getParameter("zbbh");
		String xmzb_name=request.getParameter("xmzb_name");
		String guiding=request.getParameter("guiding");
		String xsws=request.getParameter("xsdws");
		String sjclass=request.getParameter("sjclass");
		if (sjclass.equals("一般数据")) {
			sjclass = "0";
		} else if (sjclass.equals("复合数据")) {
			sjclass = "1";
		}
		String id=request.getParameter("id");
		
		try {
			int n = 0;
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			if (id.equals("")) {
				sql = "INSERT INTO ddrb_beimian (pxid, xmid, xm_name, zbbh, xmzb_name, guiding, xsws, sjclass) VALUES (?,?,?,?,?,?,?,?)"; //插入数据
				ps = conn.prepareStatement(sql);
				ps.setString(1, pxid);
				ps.setString(2, xmid);
				ps.setString(3, xm_name);
				ps.setString(4, zbbh);
				ps.setString(5, xmzb_name);
				ps.setString(6, guiding);
				ps.setString(7, xsws);
				ps.setString(8, sjclass);
				n = ps.executeUpdate();
				DB.close(conn);
				ps.close();
				//返回JSON
				if (n>0) {
					out.print(true);
				}
			} else {
				sql = "UPDATE ddrb_beimian SET pxid=?,xmid=?,xm_name=?,zbbh=?,xmzb_name=?,guiding=?,xsws=?,sjclass=? WHERE id=? "; //修改数据
				ps = conn.prepareStatement(sql);
				ps.setString(1, pxid);
				ps.setString(2, xmid);
				ps.setString(3, xm_name);
				ps.setString(4, zbbh);
				ps.setString(5, xmzb_name);
				ps.setString(6, guiding);
				ps.setString(7, xsws);
				ps.setString(8, sjclass);
				ps.setString(9, id);
				n = ps.executeUpdate();
				DB.close(conn);
				ps.close();
				//返回JSON
				if (n>0) {
					out.print(true);
				}
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
