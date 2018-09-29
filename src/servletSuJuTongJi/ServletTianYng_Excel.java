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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Iterator;

import javax.print.attribute.standard.Fidelity;
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
 * Servlet implementation class ServletTianYng_Excel
 */
@WebServlet("/ServletTianYng_Excel")
public class ServletTianYng_Excel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTianYng_Excel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/vnd.ms-excel;charset=utf-8");// 设置contentType为excel格式
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-Disposition", "Attachment;Filename=点样数据.xls");  
    	request.setCharacterEncoding("utf-8");
    	Connection conn=null;//定义为空值
    	ResultSet rs = null;
    	Statement stmt = null;
    	DecimalFormat df = new DecimalFormat();
    	String sql;
    	JSONArray jsonArray = new JSONArray();//向前台传数据的json数组
    	JSONObject title_obj = new JSONObject();
    	JSONArray title_arr = new JSONArray();
    	JSONObject content_obj = new JSONObject();
    	JSONArray content_arr = new JSONArray();
    	String date=request.getParameter("date");
    	String jizhanid=request.getParameter("jizhanid");
    	String jizhanname=request.getParameter("jizhanname");
    	String year = date.substring(0, 4);// 提取年
		String month = date.substring(5, 7);// 提取月
		
		try {
			conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		//查表头数据
    		sql = "SELECT DISTINCT\n" +
    				"sczb.xmid,\n" +
    				"xmgl.`name`,\n" +
    				"sczb.zb_name,\n" +
    				"sczb.zbbh,\n" +
    				"sczb.xsdws\n" +
    				"FROM\n" +
    				"chejian_jizhan\n" +
    				"INNER JOIN chejian ON chejian_jizhan.chejianid = chejian.id\n" +
    				"INNER JOIN xmgl ON chejian.id = xmgl.chejianid\n" +
    				"INNER JOIN sczb ON xmgl.id = sczb.xmid\n" +
    				"WHERE\n" +
    				"chejian_jizhan.jizhanid = '"+jizhanid+"'\n" +
    				"ORDER BY\n" +
    				"xmgl.`name` ASC";
    		rs=stmt.executeQuery(sql);
			while(rs.next()){
				title_obj.put("xmid", rs.getString("xmid"));
				title_obj.put("xm_name", rs.getString("name"));
				title_obj.put("zb_name", rs.getString("zb_name"));
				title_obj.put("zbbh", rs.getString("zbbh"));
				title_obj.put("fid", rs.getString("name") + rs.getString("zbbh"));
				title_obj.put("xsdws", rs.getString("xsdws"));
				title_arr.add(title_obj);
			}
			stmt.close();
			rs.close();
			jsonArray.add(title_arr);
			stmt = conn.createStatement();
			sql = "SELECT\n" +
					"sczb_ri_avg.xm_name,\n" +
					"sczb_ri_avg.hysj_date,\n" +
					"sczb_ri_avg.val\n" +
					"FROM\n" +
					"chejian_jizhan\n" +
					"INNER JOIN chejian ON chejian_jizhan.chejianid = chejian.id\n" +
					"INNER JOIN xmgl ON chejian.id = xmgl.chejianid\n" +
					"INNER JOIN sczb_ri_avg ON xmgl.id = sczb_ri_avg.xm_id\n" +
					"WHERE\n" +
					"year(hysj_date) = '"+year+"' AND\n" +
					"month(hysj_date) = '"+month+"' AND\n" +
					"chejian_jizhan.jizhanid = "+jizhanid+"\n" +
					"ORDER BY\n" +
					"sczb_ri_avg.xm_name ASC,\n" +
					"sczb_ri_avg.hysj_date ASC";
			rs=stmt.executeQuery(sql);
			JSONObject linshi_obj = new JSONObject();
	    	JSONArray linshi_arr = new JSONArray();
			while(rs.next()){
				linshi_obj.put("xm_name", rs.getString("xm_name"));
				linshi_obj.put("hysj_date", rs.getString("hysj_date"));
				linshi_obj.put("val", rs.getString("val"));
				linshi_arr.add(linshi_obj);
			}
			stmt.close();
			rs.close();
			Calendar a = Calendar.getInstance();
			a.set(Calendar.YEAR, Integer.parseInt(year));
			a.set(Calendar.MONTH, Integer.parseInt(month) - 1);
			a.set(Calendar.DATE, 1);
			a.roll(Calendar.DATE, -1);
			int tianshu = a.get(Calendar.DATE);
			int d = 1;
			for (int i = 1; i <= tianshu; i++) {
				String day;
				if (d<10) {
					day = "0"+d;
				}else {
					day = String.valueOf(d);
				}
				String fhzb_date = date + "-" + day;
				content_obj = new JSONObject();
				content_obj.put("hysj_date", fhzb_date);
				
				for (int j = 0; j < linshi_arr.size(); j++) {
					if (fhzb_date.equals(linshi_arr.getJSONObject(j).getString("hysj_date"))) {
						JSONArray val = new JSONArray();
						val = linshi_arr.getJSONObject(j).getJSONArray("val");
						
						for (int j2 = 0; j2 < val.size(); j2++) {
							String xm_name = linshi_arr.getJSONObject(j).getString("xm_name");
							String zbbh = val.getJSONObject(j2).getString("sczb_bh");
							int xsdws = 0;
							for (int k = 0; k < title_arr.size(); k++) {
								if (title_arr.getJSONObject(k).getString("xm_name").equals(xm_name) && title_arr.getJSONObject(k).getString("zbbh").equals(zbbh)) {
									xsdws = Integer.parseInt(title_arr.getJSONObject(k).getString("xsdws"));
									break;
								}
							}
							String str_val = val.getJSONObject(j2).getString("val");
							if (!str_val.equals("")) {
								df.setGroupingUsed(false);//不以逗号分隔
								df.setMaximumFractionDigits(xsdws);
								str_val = df.format(Double.parseDouble(str_val));
								df.setMinimumFractionDigits(xsdws);
								str_val = df.format(Double.parseDouble(str_val));
							}
							content_obj.put(xm_name+zbbh, str_val);
						}
						
						//content_obj.put(linshi_arr.getJSONObject(j).getString("fhzb_id"), linshi_arr.getJSONObject(j).getString("fhzb_val"));
					}
				}
				content_arr.add(content_obj);
				d++;
			}
			
			DB.close(conn);
		} catch (SQLException e) {
			System.out.println(e);
		}
		//导出Excel，po
    	//创建工作簿  
    	SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        //创建工作表  
        SXSSFSheet sheet = workbook.createSheet(jizhanname);
        //创建行,0表示第一行
        SXSSFRow row = sheet.createRow(0);//创建表头
        //设置默认列宽
        sheet.setDefaultColumnWidth(15);
        //设置居中
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中
        //添加表头
        SXSSFCell cell = row.createCell(0);//添加日期
        cell.setCellValue("日期");
        //合并日期
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,1,0,0);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        cell.setCellStyle(cellStyle);
        for (int i = 0; i < title_arr.size(); i++) {//写入第一行表头
			String xm_name = title_arr.getJSONObject(i).getString("xm_name");
			cell = row.createCell(i+1);//从第二列开始
			cell.setCellValue(xm_name);
		}
        row = sheet.createRow(1);
        for (int i = 0; i < title_arr.size(); i++) {//写入第一行表头
			String zb_name = title_arr.getJSONObject(i).getString("zb_name");
			cell = row.createCell(i+1);//从第二列开始
			cell.setCellValue(zb_name);
			cell.setCellStyle(cellStyle);
		}
        for (int i = 0; i < content_arr.size(); i++) {//写入数据
        	row = sheet.createRow(i+2);//从第三行开始
        	cell = row.createCell(0);//日期在0列
        	String riqi = content_arr.getJSONObject(i).getString("hysj_date");
        	cell.setCellValue(riqi);
        	cell.setCellStyle(cellStyle);
        	for (int j = 0; j < title_arr.size(); j++) {//遍历表头
        		cell = row.createCell(j+1);//从第二列开始
        		String fid = title_arr.getJSONObject(j).getString("fid");
        		Iterator<String> it = content_arr.getJSONObject(i).keys();
        		while (it.hasNext()) {//遍历表头
                    String key = it.next();
                    if (fid.equals(key)) {
                    	String val = content_arr.getJSONObject(i).getString(key);
                    	cell.setCellValue(val);
                    	cell.setCellStyle(cellStyle);
					}
        		}
			}
		}
        //合并单元格
        //读取行  
        SXSSFRow getrow = sheet.getRow(0);
        int cellCount = getrow.getLastCellNum();//一共多少列
        int n1 = 0;//开始列数
        int n2 = 0;//结束列数
        String value1;//开始值
        String value2;//结束值
        for (int i = 1; i < cellCount;) {//遍历表头
            //读取单元格  
            SXSSFCell getcell1 = getrow.getCell(i);
            value1 = getcell1.getStringCellValue();
            n1 = i;
            for (int j = i; j < cellCount; j++) {
            	SXSSFCell getcell2 = getrow.getCell(j);
            	value2 = getcell2.getStringCellValue();
				if (!value1.equals(value2)) {
					n2 = j-1;
					if (n1!=n2) {
						callRangeAddress = new CellRangeAddress(0,0,n1,n2);//起始行,结束行,起始列,结束列
			        	//添加合并区域  
			            sheet.addMergedRegion(callRangeAddress);
			            SXSSFCell getcell3 = getrow.getCell(n1);
			            getcell3.setCellStyle(cellStyle);
			            i = j;
			            break;
					}else {
						SXSSFCell getcell3 = getrow.getCell(n1);
						getcell3.setCellStyle(cellStyle);
			            i=j;
			            break;
					}
				}else {//最后一个格
					n2 = j;
					if (j == cellCount-1) {
						if (n1!=n2) {
							callRangeAddress = new CellRangeAddress(0,0,n1,n2);//起始行,结束行,起始列,结束列
				        	//添加合并区域  
				            sheet.addMergedRegion(callRangeAddress);
				            SXSSFCell getcell3 = getrow.getCell(n1);
				            getcell3.setCellStyle(cellStyle);
				            i = j+1;
						}else {
							SXSSFCell getcell3 = getrow.getCell(n1);
							getcell3.setCellStyle(cellStyle);
				            i = j+1;
						}
			            break;
					}
				}
			}
        }
        	
        OutputStream fos = null;  
        try {  
            fos = response.getOutputStream();  
            String userAgent = request.getHeader("USER-AGENT");  
            String fileName = date+jizhanname+"天样数据";  
            try {  
                if(StringUtils.contains(userAgent, "Mozilla")){  
                    fileName = new String(fileName.getBytes(), "ISO8859-1");  
                }else {  
                    fileName = URLEncoder.encode(fileName, "utf8");  
                }  
            } catch (UnsupportedEncodingException e) {  
                e.printStackTrace();  
            }
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
