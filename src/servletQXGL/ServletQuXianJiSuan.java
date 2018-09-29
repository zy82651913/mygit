package servletQXGL;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletQuXianJiSuan
 */
@WebServlet("/ServletQuXianJiSuan")
public class ServletQuXianJiSuan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletQuXianJiSuan() {
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
    	PrintWriter out =response.getWriter();
    	String a1 = request.getParameter("a1");
    	String a2 = request.getParameter("a2");
    	String a3 = request.getParameter("a3");
    	String a4 = request.getParameter("a4");
    	String csz = request.getParameter("csz");
    	String zzz = request.getParameter("zzz");
    	Double A1 = Double.parseDouble(a1);//string转double
    	Double A2 = Double.parseDouble(a2);
    	Double A3 = Double.parseDouble(a3);
    	Double A4 = Double.parseDouble(a4);
    	Double CSZ = Double.parseDouble(csz);//初始值
    	Double ZZZ = new BigDecimal(zzz).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();//最终值
    	DecimalFormat df =new DecimalFormat("#0.000");
    	DecimalFormat df1 =new DecimalFormat("#0.00");
    	JSONObject obj = new JSONObject();//JSON对象
    	JSONArray jsonarray = new JSONArray(); //JSON数组
    	for (double i = CSZ; i <= ZZZ;) {
    		Double x = i;
    		obj.put("a0", df1.format(x));
    		obj.put("a1", df.format(A1+A2*x+A3*Math.pow(x,2)+A4*Math.pow(x,3)));
    		obj.put("a2", df.format(A1+A2*(x+0.001)+A3*Math.pow((x+0.001),2)+A4*Math.pow((x+0.001),3)));
    		obj.put("a3", df.format(A1+A2*(x+0.002)+A3*Math.pow((x+0.002),2)+A4*Math.pow((x+0.002),3)));
    		obj.put("a4", df.format(A1+A2*(x+0.003)+A3*Math.pow((x+0.003),2)+A4*Math.pow((x+0.003),3)));
    		obj.put("a5", df.format(A1+A2*(x+0.004)+A3*Math.pow((x+0.004),2)+A4*Math.pow((x+0.004),3)));
    		obj.put("a6", df.format(A1+A2*(x+0.005)+A3*Math.pow((x+0.005),2)+A4*Math.pow((x+0.005),3)));
    		obj.put("a7", df.format(A1+A2*(x+0.006)+A3*Math.pow((x+0.006),2)+A4*Math.pow((x+0.006),3)));
    		obj.put("a8", df.format(A1+A2*(x+0.007)+A3*Math.pow((x+0.007),2)+A4*Math.pow((x+0.007),3)));
    		obj.put("a9", df.format(A1+A2*(x+0.008)+A3*Math.pow((x+0.008),2)+A4*Math.pow((x+0.008),3)));
    		obj.put("a10", df.format(A1+A2*(x+0.009)+A3*Math.pow((x+0.009),2)+A4*Math.pow((x+0.009),3)));
    		jsonarray.add(obj);
    		BigDecimal a = new BigDecimal(Double.toString(i));
    		BigDecimal b = new BigDecimal("0.01");
    		i=a.add(b).doubleValue();
		}
    	out.print(jsonarray);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
