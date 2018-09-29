package servletSuJuTongJi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletFuHeShuJu_Excel
 */
@WebServlet("/ServletFuHeShuJu_Excel")
public class ServletFuHeShuJu_Excel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletFuHeShuJu_Excel() {
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
    	ResultSet rs = null;
    	Statement stmt = null;
    	String sql;
    	JSONObject jsonObject = new JSONObject();//向前台传数据的json对象
    	JSONArray jsonArray = new JSONArray();//向前台传数据的json数组
    	JSONObject title_obj = new JSONObject();
    	JSONArray title_arr = new JSONArray();
    	JSONObject content_obj = new JSONObject();
    	JSONArray content_arr = new JSONArray();
    	String date=request.getParameter("date");
    	String year = date.substring(0, 4);// 提取年
		String month = date.substring(5, 7);// 提取月
		
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
			//取得表头数据
			title_obj.put("日期", "fhzb_date");
			title_obj.put("班次", "fhzb_bc");
    		stmt = conn.createStatement();
    		sql = "SELECT\n" +
    				"fhzbgl.fhzb_name,\n" +
    				"fhzbgl.id\n" +
    				"FROM\n" +
    				"fhzbgl\n" +
    				"ORDER BY\n" +
    				"fhzbgl.pxid ASC";
    		rs=stmt.executeQuery(sql);
			while(rs.next()){
				title_obj.put(rs.getString("fhzb_name"), rs.getString("id"));
			}
			title_arr.add(title_obj);
			stmt.close();
			rs.close();
    		
    		//取得复合数据
    		stmt = conn.createStatement();
			sql = "SELECT\n" +
					"fhzb_data.fhzb_name,\n" +
					"fhzb_data.fhzb_val,\n" +
					"fhzb_data.fhzb_date,\n" +
					"fhzb_data.fhzb_bc,\n" +
					"fhzb_data.fhzb_id\n" +
					"FROM\n" +
					"fhzb_data\n" +
					"WHERE\n" +
					"year(fhzb_date) = '"+year+"' AND\n" +
					"month(fhzb_date) = '"+month+"'\n" +
					"ORDER BY\n" +
					"fhzb_data.fhzb_name ASC,\n" +
					"fhzb_data.fhzb_date ASC,\n" +
					"fhzb_data.fhzb_bc ASC";
			rs=stmt.executeQuery(sql);
			JSONObject linshi_obj = new JSONObject();
	    	JSONArray linshi_arr = new JSONArray();
			while(rs.next()){
				linshi_obj.put("fhzb_id", rs.getString("fhzb_id"));
				linshi_obj.put("fhzb_date", rs.getString("fhzb_date"));
				linshi_obj.put("fhzb_bc", rs.getString("fhzb_bc"));
				linshi_obj.put("fhzb_name", rs.getString("fhzb_name"));
				linshi_obj.put("fhzb_val", rs.getString("fhzb_val"));
				linshi_arr.add(linshi_obj);
			}
			
			Calendar a = Calendar.getInstance();
			a.set(Calendar.YEAR, Integer.parseInt(year));
			a.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			a.set(Calendar.DATE, 1);
			a.roll(Calendar.DATE, -1);
			int tianshu = a.get(Calendar.DATE);
			
			int bc = 1;
			int n = 0;
			int d = 1;
			for (int i = 1; i <= tianshu*3; i++) {
				String day;
				if (n == 3) {
					d++;
				}
				if (d<10) {
					day = "0"+d;
				}else {
					day = String.valueOf(d);
				}
				if (n == 3) {
					bc = 1;
				}
				if (n == 3) {
					n = 0;
				}
				
				String fhzb_date = date + "-" + day;
				content_obj = new JSONObject();
				content_obj.put("fhzb_date", fhzb_date);
				content_obj.put("fhzb_bc", bc);
				for (int j = 0; j < linshi_arr.size(); j++) {
					if (fhzb_date.equals(linshi_arr.getJSONObject(j).getString("fhzb_date")) && String.valueOf(bc).equals(linshi_arr.getJSONObject(j).getString("fhzb_bc"))) {
						content_obj.put(linshi_arr.getJSONObject(j).getString("fhzb_id"), linshi_arr.getJSONObject(j).getString("fhzb_val"));
					}
				}
				content_arr.add(content_obj);
				bc++;
				n++;
			}
			stmt.close();
			rs.close();
			DB.close(conn);
		} catch (SQLException e) {
			System.out.println(e);
		}
		//导出Excel，po
    	//创建工作簿  
    	SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        //创建工作表  
        SXSSFSheet sheet = workbook.createSheet("复合数据");
        //设置居中
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中
        //创建行,0表示第一行
        SXSSFRow row = sheet.createRow(0);//创建表头
        
        //设置默认列宽
        sheet.setDefaultColumnWidth(15);
        //添加表头
        int n = 0;
        Iterator<String> it = title_arr.getJSONObject(0).keys();
		while (it.hasNext()) {//遍历表头
			//创建单元格行号由row确定,列号作为参数传递给createCell;第一列从0开始计算
            SXSSFCell cell = row.createCell(n);
            String key = it.next();
            cell.setCellValue(key);
            cell.setCellStyle(cellStyle);
            n++;
		}
        //添加数据
		for (int i = 0; i < content_arr.size(); i++) {
			n = 0;//列
        	//创建行,0表示第一行
            row = sheet.createRow(i+1);
            it = title_arr.getJSONObject(0).keys();
			while (it.hasNext()) {//遍历表头
				String key = it.next();
				String title_val = title_arr.getJSONObject(0).getString(key);
				Iterator<String> itt = content_arr.getJSONObject(i).keys();
				SXSSFCell cell = row.createCell(n);
				while (itt.hasNext()) {//遍历数据
					String key_sj = itt.next();
					if (title_val.equals(key_sj)) {
						//给单元格赋值
			            String val = content_arr.getJSONObject(i).getString(key_sj);
			            cell.setCellValue(val);
			            cell.setCellStyle(cellStyle);
			            break;
					}
				}
				n++;
			}
		}
		//创建合并单元格对象
		//int cellRow = sheet.getLastRowNum();//一共多少行
        int len = content_arr.size();//数据的行数
        int r1 = 1;
        int r2 = 3;
        for (int i = 1; i <= len;) {
        	CellRangeAddress callRangeAddress = new CellRangeAddress(r1,r2,0,0);//起始行,结束行,起始列,结束列
        	//添加合并区域  
            sheet.addMergedRegion(callRangeAddress);
            SXSSFRow getrow = sheet.getRow(r1);
            SXSSFCell getcell3 = getrow.getCell(0);
            getcell3.setCellStyle(cellStyle);
        	r1 +=3;
        	r2 +=3;
        	i = r2;
		}
        
        try {
        	OutputStream fos = response.getOutputStream();  
            String userAgent = request.getHeader("USER-AGENT");  
            String fileName = date+"复合数据";  
            try {  
                if(StringUtils.contains(userAgent, "Mozilla")){  
                    fileName = new String(fileName.getBytes(), "ISO8859-1");  
                }else {  
                    fileName = URLEncoder.encode(fileName, "utf8");  
                }  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }  
            response.reset();
            response.setHeader("Content-Disposition", "Attachment;Filename="+ fileName+".xlsx");  
            workbook.write(fos);
            workbook.close();
            fos.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
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
