package hys;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;  
import java.util.TimerTask;  

import hys.ContextListener;
/**  
  * JAVA 一个定时任务  
 *   
  * @author Guox2011-05-10  
  *   
  */  
 public class MailSendTask extends TimerTask {  
  
 public MailSendTask() {  
  
 }  
  
 /**  
   * RUN方法放入一个SCHEDULE的时候 进行执行 一个定时程序执行的进口  
  */  
public void run() {  
  
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String date = format.format(new Date());
	String date1 = format1.format(new Date());
	Calendar cal = Calendar.getInstance();
	int hour=cal.get(Calendar.HOUR_OF_DAY);
	int bc = 0;
	if (hour>=0 && hour<8) {//根据时间设置班次
			bc = 1;
		} else if(hour>=8 && hour<16) {
			bc = 2;
		} else if(hour>=16 && hour<24){
			bc = 3;
		}
	System.out.println(date1+ "---开始执行复合数据生成 ---班次："+ bc + " 小时："+ hour);
	FuHeShuJu_ShengCheng.Fhsj_ShengCheng(date,bc);
	}
}  
