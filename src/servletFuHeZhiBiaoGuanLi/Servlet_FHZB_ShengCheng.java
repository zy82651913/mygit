package servletFuHeZhiBiaoGuanLi;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hys.FuHeShuJu_ShengCheng;
import net.sf.json.JSONArray;

/**
 * Servlet implementation class Servlet_FHZB_ShengCheng
 */
@WebServlet("/Servlet_FHZB_ShengCheng")
public class Servlet_FHZB_ShengCheng extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Servlet_FHZB_ShengCheng() {
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
    	PrintWriter out =response.getWriter();
		String date_start=request.getParameter("date_start");
		String date_end=request.getParameter("date_end");
		String banci_start=request.getParameter("banci_start");
		String banci_end=request.getParameter("banci_end");
		
		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
		Date d_start = null;
		Date d_end = null;
		String fhzb_date;
		try {
			d_start = sdformat.parse(date_start);
			d_end = sdformat.parse(date_end);
		} catch (Exception e) {
			// TODO: handle exception
		}
		long day = d_end.getTime() - d_start.getTime();
		day = day / (1000 * 60 * 60 * 24);
		
		for (int i = 0; i <= day; i++) {
			int bc = 1;
			Calendar c = Calendar.getInstance();
			c.setTime(d_start);
			c.add(Calendar.DAY_OF_MONTH, i);
			fhzb_date = sdformat.format(c.getTime());
			for (int ii = 1; ii <= 3 ; ii++) {
				bc = ii;
				if (fhzb_date.equals(date_start) && bc < Integer.parseInt(banci_start)) {
					bc = Integer.parseInt(banci_start);
					ii = bc;
				}
				FuHeShuJu_ShengCheng.Fhsj_ShengCheng(fhzb_date,bc);
				//System.out.println("日期"+fhzb_date+"--班次"+bc);
				if (fhzb_date.equals(date_end) && banci_end.equals(bc+"")) {
					out.print(true);
					break;
				}
			}
			
		}
		//int bc = Integer.parseInt(bc_get);
		//FuHeShuJu_ShengCheng.Fhsj_ShengCheng(fhzb_date,bc);
		//out.print(true);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
