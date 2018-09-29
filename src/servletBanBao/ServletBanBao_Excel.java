package servletBanBao;

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
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import hys.DB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class ServletBanBao_Excel
 */
@WebServlet("/ServletBanBao_Excel")
public class ServletBanBao_Excel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletBanBao_Excel() {
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
    	DecimalFormat df = new DecimalFormat();
    	String sql;
    	String date=request.getParameter("date");
    	String bc=request.getParameter("bc");
    	String bc_name=request.getParameter("bc_name");
    	String banbao_id=request.getParameter("banbao_id");
    	String banbao_name=request.getParameter("banbao_name");
    	/*班报订制*/
    	JSONObject dzObj = new JSONObject();
    	JSONArray dzArr = new JSONArray();
    	/*班报指标*/
    	JSONObject bbzbObj = new JSONObject();
    	JSONArray bbzbArr = new JSONArray();
    	/*班报数据*/
    	JSONObject bbsjObj = new JSONObject();
    	JSONArray bbsjArr = new JSONArray();
    	/*返回的数据*/
    	JSONObject jsonObject = new JSONObject();
    	JSONArray jsonArray = new JSONArray();
    	/*项目管理数据*/
    	JSONObject xmglObj = new JSONObject();
    	JSONArray xmglArr = new JSONArray();
    	/*班平均数据*/
    	JSONObject banavglObj = new JSONObject();
    	JSONArray banavglArr = new JSONArray();
    	try {
    		conn=DB.getConection();//利用封装好的类名来调用连接方法便可
    		stmt = conn.createStatement();
    		/*获得班报订制信息*/
    		sql = "SELECT\n" +
    				"banbao_dz.id,\n" +
    				"banbao_dz.banbao_id,\n" +
    				"banbao_dz.xm_id,\n" +
    				"banbao_dz.xm_name,\n" +
    				"banbao_dz.`row`,\n" +
    				"banbao_dz.col,\n" +
    				"banbao_dz.sfky\n" +
    				"FROM\n" +
    				"banbao_dz\n" +
    				"WHERE\n" +
    				"banbao_dz.banbao_id = "+banbao_id+" AND\n" +
    				"banbao_dz.sfky = 1\n" +
    				"ORDER BY\n" +
    				"banbao_dz.`row` ASC,\n" +
    				"banbao_dz.col ASC";
    		rs=stmt.executeQuery(sql);
    		while(rs.next()){
    			dzObj.put("xm_id", rs.getString("xm_id"));
    			dzObj.put("xm_name", rs.getString("xm_name"));
    			dzObj.put("row", rs.getString("row"));
    			dzObj.put("col", rs.getString("col"));
    			dzObj.put("sfky", rs.getString("sfky"));
    			dzArr.add(dzObj);
    		}
    		stmt.close();
    		rs.close();
    		
    		/*获取指标*/
    		for (int i = 0; i < dzArr.size(); i++) {
    			stmt = conn.createStatement();
    			String xmid = dzArr.getJSONObject(i).getString("xm_id");
				sql = "SELECT\n" +
						"sczb.xmid,\n" +
						"sczb.zb_name,\n" +
						"sczb.pxid,\n" +
						"sczb.bbyn,\n" +
						"sczb.xsdws\n" +
						"FROM\n" +
						"sczb\n" +
						"WHERE\n" +
						"sczb.xmid = "+xmid+" AND\n" +
						"sczb.bbyn = 1\n" +
						"ORDER BY\n" +
						"sczb.pxid ASC";
				rs=stmt.executeQuery(sql);
	    		while(rs.next()){
	    			bbzbObj.put("xmid", rs.getString("xmid"));
	    			bbzbObj.put("zb_name", rs.getString("zb_name"));
	    			bbzbObj.put("xsdws", rs.getString("xsdws"));
	    			bbzbArr.add(bbzbObj);
	    		}
	    		stmt.close();
	    		rs.close();
			}
    		
    		/*获取数据*/
    		for (int i = 0; i < dzArr.size(); i++) {
    			String xmid = dzArr.getJSONObject(i).getString("xm_id");
    			stmt = conn.createStatement();
    			sql = "SELECT\n" +
    					"sczb_data.xm_id,\n" +
    					"sczb_data.sczb_data,\n" +
    					"sczb_data.beizhu_name,\n" +
    					"sczb_data.gx_zhbc,\n" +
    					"sczb_data.hysjd_time\n" +
    					"FROM\n" +
    					"sczb_data\n" +
    					"WHERE\n" +
    					"sczb_data.xm_id = "+xmid+" AND\n" +
    					"sczb_data.hysj_date = '"+date+"' AND\n" +
    					"sczb_data.shenhe = 1 AND\n" +
    					"sczb_data.hy_bc = "+bc;
    			rs=stmt.executeQuery(sql);
    			while(rs.next()){
    				bbsjObj.put("xmid", rs.getString("xm_id"));
    				bbsjObj.put("gx_zhbc", rs.getString("gx_zhbc"));
    				bbsjObj.put("hysjd_time", rs.getString("hysjd_time"));
    				bbsjObj.put("sczb_data", rs.getString("sczb_data"));
    				bbsjObj.put("beizhu_name", rs.getString("beizhu_name"));
    				bbsjArr.add(bbsjObj);
	    		}
	    		stmt.close();
	    		rs.close();
			}
    		/*if (bbsjArr.size()==0) {//如果没有查到班报数据，那么返回false
				out.print(false);
				return;
			}*/
    		
    		/*获取项目管理数据*/
    		stmt = conn.createStatement();
    		sql = "SELECT\n" +
    				"xmgl.id,\n" +
    				"xmgl.xmclass\n" +
    				"FROM\n" +
    				"xmgl";
    		rs=stmt.executeQuery(sql);
			while(rs.next()){
				xmglObj.put("xmid", rs.getString("id"));
				xmglObj.put("xmclass", rs.getString("xmclass"));
				xmglArr.add(xmglObj);
    		}
			stmt.close();
    		rs.close();
    		/*获取班平均数据*/
    		for (int i = 0; i < dzArr.size(); i++) {
    			String xmid = dzArr.getJSONObject(i).getString("xm_id");
    			stmt = conn.createStatement();
    			sql = "SELECT\n" +
    					"sczb_ban_avg.xm_name,\n" +
    					"sczb_ban_avg.val,\n" +
    					"sczb_ban_avg.xm_id\n" +
    					"FROM `sczb_ban_avg`\n" +
    					"WHERE\n" +
    					"sczb_ban_avg.xm_id = "+xmid+" AND\n" +
    					"sczb_ban_avg.hysj_date = '"+date+"' AND\n" +
    					"sczb_ban_avg.hy_bc = "+bc;
    			rs=stmt.executeQuery(sql);
    			while(rs.next()){
    				banavglObj.put("xmid", rs.getString("xm_id"));
    				banavglObj.put("xm_name", rs.getString("xm_name"));
    				banavglObj.put("ban_val", rs.getString("val"));
    				banavglArr.add(banavglObj);
        		}
    		}
    		stmt.close();
    		rs.close();
    		DB.close(conn);
    		/*System.out.println(dzArr);
    		System.out.println(bbzbArr);
    		System.out.println(bbsjArr);
    		System.out.println(xmglArr);
    		System.out.println(banavglArr);*/
    		/*数据整理*/
    		int row = dzArr.getJSONObject(dzArr.size()-1).getInt("row");//获得班报订制的行数
    		for (int i = 1; i <= row; i++) {
				//先设置表头
    			JSONArray lsArr = new JSONArray();
    			for (int j = 0; j < dzArr.size(); j++) {//遍历班报订制数据
					if (dzArr.getJSONObject(j).getInt("row") == i) {
						String xmid = dzArr.getJSONObject(j).getString("xm_id");
						lsArr.add("项目名称");
						for (int j2 = 0; j2 < xmglArr.size(); j2++) {//遍历项目管理，如果项目类型为液相，加入化验时间
							if (xmglArr.getJSONObject(j2).getString("xmid").equals(xmid)) {
								if (xmglArr.getJSONObject(j2).getString("xmclass").equals("1")) {//1为液相
									lsArr.add("化验时间");
									break;
								}else {//固相显示滞后班次
									lsArr.add("滞后班次");
									break;
								}
							}
						}
						for (int k = 0; k <= bbzbArr.size(); k++) {//遍历指标
							try {
								if (bbzbArr.getJSONObject(k).getString("xmid").equals(xmid)) {
									lsArr.add(bbzbArr.getJSONObject(k).getString("zb_name"));
								}
							} catch (Exception e) {
								lsArr.add("备注");
							}
						}
					}
				}
    			jsonArray.add(lsArr);
    			//整理数据
    			//获取最大数据行数
    			int max = 0;//最大行数
    			for (int j = 0; j < dzArr.size(); j++) {//遍历班报订制数据
    				int num = 0;
    				if (dzArr.getJSONObject(j).getInt("row") == i) {
    					String xmid = dzArr.getJSONObject(j).getString("xm_id");
						for (int j1 = 0; j1 < bbsjArr.size(); j1++) {
							if (bbsjArr.getJSONObject(j1).getInt("xmid") == Integer.parseInt(xmid)) {
								num++;
							}
						}
						if (num>max) {
							max = num;
						}
    				}
    			}
    			/*根据最大行数整理数据*/
    			JSONArray lssjArr = new JSONArray();//临时数据
				for (int k = 0; k <= max; k++) {
					lssjArr = new JSONArray();
					for (int j = 0; j < dzArr.size(); j++) {//遍历班报订制数据
						if (dzArr.getJSONObject(j).getInt("row") == i) {
							String xmid = dzArr.getJSONObject(j).getString("xm_id");
							String xm_name = dzArr.getJSONObject(j).getString("xm_name");
							lssjArr.add(xm_name);//填入项目名称
							JSONArray sjArray = new JSONArray();//一列数据的数组
							for (int l = 0; l < bbsjArr.size(); l++) {//把需要的一列数据查出来
								if (bbsjArr.getJSONObject(l).getString("xmid").equals(xmid)) {
									sjArray.add(bbsjArr.getJSONObject(l));
								}
							}
							for (int j2 = 0; j2 < xmglArr.size(); j2++) {//遍历项目管理，如果项目类型为液相，加入化验时间
								if (xmglArr.getJSONObject(j2).getString("xmid").equals(xmid)) {
									if (xmglArr.getJSONObject(j2).getString("xmclass").equals("1")) {//1为液相
										try {
											String hysjd_time = sjArray.getJSONObject(k).getString("hysjd_time");
											hysjd_time = hysjd_time.substring(0, hysjd_time.length()-3);
											lssjArr.add(hysjd_time);
											break;
										} catch (Exception e) {
											if (k == max) {
												lssjArr.add("平均");
											}else {
												lssjArr.add("");//如果没有查到输出空
											}
											break;
										}
									}else {//固相显示滞后班次
										try {
											String gx_zhbc = sjArray.getJSONObject(k).getString("gx_zhbc");
											lssjArr.add(gx_zhbc);
											break;
										} catch (Exception e) {
											if (k == max) {
												lssjArr.add("平均");
											}else {
												lssjArr.add("");//如果没有查到输出空
											}
											break;
										}
									}
								}
							}
							//插入数据
							for (int l = 0; l <= bbzbArr.size(); l++) {
								if (k != max) {
									try {
										if (bbzbArr.getJSONObject(l).getString("xmid").equals(xmid)) {
											String zb_name = bbzbArr.getJSONObject(l).getString("zb_name");
											try {
												JSONArray sczb_data = sjArray.getJSONObject(k).getJSONArray("sczb_data");
												for (int m = 0; m < sczb_data.size(); m++) {
													if (sczb_data.getJSONObject(m).getString("sczb_name").equals(zb_name)) {
														lssjArr.add(sczb_data.getJSONObject(m).getString("sczb_value"));
														break;
													}
												}
											} catch (Exception e) {
												lssjArr.add("");
											}
										}
									} catch (Exception e) {
										if (l == bbzbArr.size()) {
											try {
												lssjArr.add(sjArray.getJSONObject(k).getString("beizhu_name"));
											} catch (Exception e2) {
												lssjArr.add("");
											}
										}else {
											lssjArr.add("");
										}
									}
								}else {//插入班平均
									JSONArray bavgArray = new JSONArray();//一列数据的班平均数组
									JSONArray sArray = new JSONArray();
									for (int ll = 0; ll < banavglArr.size(); ll++) {//把需要的一列数据查出来
										if (banavglArr.getJSONObject(ll).getString("xmid").equals(xmid)) {
											bavgArray.add(banavglArr.getJSONObject(ll));
										}
									}
									for (int t = 0; t < bbsjArr.size(); t++) {//把需要的一列数据查出来
										if (bbsjArr.getJSONObject(t).getString("xmid").equals(xmid)) {
											sArray.add(bbsjArr.getJSONObject(t));
										}
									}
									try {
										JSONArray ban_val = bavgArray.getJSONObject(0).getJSONArray("ban_val");
										for (int n = 0; n <= ban_val.size(); n++) {
											try {
												String val;
												String sczb_name = ban_val.getJSONObject(n).getString("sczb_name");
												int xsdws = 2;
												for (int m = 0; m < bbzbArr.size(); m++) {//去除小数点位数
													if (bbzbArr.getJSONObject(m).getString("xmid").equals(xmid) && bbzbArr.getJSONObject(m).getString("zb_name").equals(sczb_name)) {
														xsdws = bbzbArr.getJSONObject(m).getInt("xsdws");
													}
												}
												int num = 0;
												for (int m = 0; m < bbzbArr.size(); m++) {//遍历班报指标
													if (sczb_name.equals(bbzbArr.getJSONObject(m).getString("zb_name")) && bbzbArr.getJSONObject(m).getString("xmid").equals(xmid)) {
														num++;
													}
												}
												if (num>0) {//判断是否有指标不让班报显示
													df.setMaximumFractionDigits(xsdws);
													val = df.format(Double.parseDouble(ban_val.getJSONObject(n).getString("val")));
													df.setMinimumFractionDigits(xsdws);
													val = df.format(Double.parseDouble(val));
													if (sArray.size() == 0) {//普通数据没有审核，不显示班平均数据
														lssjArr.add("");
													}else {
														lssjArr.add(val);
													}
												}
											} catch (Exception e) {
												lssjArr.add("");
											}
										}
									} catch (Exception e) {
										if (bavgArray.size()==0) {//没有查到班平均，用空补齐
											for (int m = 0; m <= bbzbArr.size(); m++) {
												try {
													if (bbzbArr.getJSONObject(m).getString("xmid").equals(xmid)) {
														lssjArr.add("");
													}
												} catch (Exception e2) {//多补一个备注位置
													lssjArr.add("");
												}
												
											}
										}
									}
									break;
								}
							}
						}
					}
					jsonArray.add(lssjArr);
				}
			}
    		//out.print(jsonArray);
		} catch (SQLException e) {
			System.out.println(e);
		}
    	
    	//查找最大列数
    	 Integer[] maxArr = new Integer[jsonArray.size()];
    	for (int i = 0; i < jsonArray.size(); i++) {
			maxArr[i] = jsonArray.getJSONArray(i).size();
		}
    	int max = (int) Collections.max(Arrays.asList(maxArr));
    	
    	//导出Excel，po
    	//创建工作簿  
    	SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        //创建工作表  
        SXSSFSheet sheet = workbook.createSheet(banbao_name);
        //创建行,0表示第一行
        SXSSFRow row = sheet.createRow(0);//创建表头
        //设置默认列宽
        sheet.setDefaultColumnWidth(15);
        //设置表头字体
        CellStyle cellStyle1 = workbook.createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中
        Font f1 = workbook.createFont();
        f1.setFontHeightInPoints((short) 20);//字号  
        f1.setBold(true);//加粗 
        cellStyle1.setFont(f1);
        //添加第一行班报名称
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(banbao_name);
        cell.setCellStyle(cellStyle1);
        CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,max-1);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        //添加第二行，日期班次
        row = sheet.createRow(1);
        CellStyle cellStyle2 = workbook.createCellStyle();
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中
        Font f2 = workbook.createFont();
        f2.setFontHeightInPoints((short) 14);//字号 
        cellStyle2.setFont(f2);
        cell = row.createCell(0);
        cell.setCellValue("日期："+date+" "+" "+" "+" "+"班次："+bc_name);
        cell.setCellStyle(cellStyle2);
        callRangeAddress = new CellRangeAddress(1,1,0,max-1);//起始行,结束行,起始列,结束列
        sheet.addMergedRegion(callRangeAddress);
        //添加表格
        //设置居中
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//设置水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//设置垂直居中
        for (int i = 0; i < jsonArray.size(); i++) {//行数
        	row = sheet.createRow(i+2);
			for (int j = 0; j < jsonArray.getJSONArray(i).size(); j++) {//列数
				cell = row.createCell(j);
				String val = jsonArray.getJSONArray(i).getString(j);
				cell.setCellValue(val);
				cell.setCellStyle(cellStyle);
			}
		}
        
        
        
        OutputStream fos = null;  
        try {  
            fos = response.getOutputStream();  
            String userAgent = request.getHeader("USER-AGENT");  
            String fileName = date+bc_name+banbao_name;  
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
