package servletXMGL;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletCombobox_MathClass
 */
@WebServlet("/ServletCombobox_MathClass")
public class ServletCombobox_MathClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletCombobox_MathClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json; charset=utf-8");
		PrintWriter out =response.getWriter();
		JSONObject jsonobj = new JSONObject();//JSON对象
		JSONObject jsonobj1 = new JSONObject();//JSON对象
		JSONArray jsonarray = new JSONArray(); //JSON数组
		jsonobj.put("val", "1");
		jsonobj.put("text", "一般公式");
		//jsonobj.put("selected","true");
		jsonarray.add(jsonobj);
		jsonobj1.put("val", "0");
		jsonobj1.put("text", "曲线公式");
		jsonarray.add(jsonobj1);
		out.println(jsonarray);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
