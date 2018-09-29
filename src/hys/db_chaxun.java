package hys;


import java.sql.*;

public class db_chaxun {
	  public static void main(String[] args) throws Exception {
		          Connection conn=DB.getConection();//利用封装好的类名来调用连接方法便可
		          System.out.println(conn+",成功连接数据库");
		          DB.close(conn);//同样利用类名调用关闭方法即可
		       }
}

