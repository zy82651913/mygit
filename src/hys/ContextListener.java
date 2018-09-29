package hys;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import java.util.Timer;
/**  
 *   
 * 文件发送的监听 需要再WEB-XML中配置  
*/
@WebListener
public class ContextListener implements ServletContextListener {
	
    public ContextListener() {
        // TODO Auto-generated constructor stub
    }

    /**  
     * 监听开始销毁  
    */  
    public void contextDestroyed(ServletContextEvent event)  {
    	
    }

    /**  
     * 监听开始执行  
    */  
    public void contextInitialized(ServletContextEvent event)  { 
    	// 当监听开始执行时,设置一个TIME  
	   Timer timer = new Timer();  
	   System.out.println("-------MailSend   Timer开始进行执行--------------");  
	   MailSendTask msendTask = new MailSendTask();  
	   timer.schedule(msendTask, 0, 60 * 60 * 1000);
	   System.out.println("-------MailSend   Timer已经在执行--------------");  
	  }
}
