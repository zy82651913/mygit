package servletQXGL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import hys.QuXianNiHe;
/**
 * Servlet implementation class ServletQuXianNiHe
 */
@WebServlet("/ServletQuXianNiHe")
public class ServletQuXianNiHe extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletQuXianNiHe() {
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
    	String json=request.getParameter("json");
    	JSONObject j=JSONObject.fromObject(json);//把前台传入的json对象字符串转为json对象
    	JSONArray xx= j.getJSONArray("x");//把json对象中的X数组转换成json数组
    	JSONArray yy= j.getJSONArray("y");//把json对象中的y数组转换成json数组
    	double[] x=new double[xx.size()];//新建double数组
    	double[] y=new double[yy.size()];//新建double数组
    	for(int i=0;i<xx.size();i++){//遍历字符串数组转换成double数组
    		x[i]=xx.getDouble(i);
    		y[i]=yy.getDouble(i);
    	}
    	double[] ratio;
    	JSONObject jsonobj = new JSONObject();//JSON对象
		JSONArray jsonarray = new JSONArray(); //JSON数组
    	ratio = QuXianNiHe.polyfit(y, x, 3);
		jsonobj.put("A1", ratio[0]);
		jsonobj.put("A2", ratio[1]);
		jsonobj.put("A3", ratio[2]);
		jsonobj.put("A4", ratio[3]);
		//jsonarray.add(jsonobj);
    	out.println(jsonobj);
    	/*System.out.println(xx);
    	System.out.println(yy);
    	System.out.println(x);
    	System.out.println(y);*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
