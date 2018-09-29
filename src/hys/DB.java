package hys;


import java.sql.*;


public class DB {
	// JDBC 驱动名及数据库 URL
	private static String url="jdbc:mysql://localhost:3306/hys? useUnicode=true&characterEncoding=UTF8&useSSL=false&serverTimezone=UTC";
	private static String driver="com.mysql.cj.jdbc.Driver";
	//private static String driver="com.mysql.cj.jdbc.Driver";
	// 数据库的用户名与密码
	private static String user="root";
	private static String password="zhangyang";

	public static Connection getConection(){
		Connection conn=null;
		try {
			Class.forName(driver);
		conn = DriverManager.getConnection(url, user, password);//获取连接	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
public static void close(Connection conn) {
	try{
		if(conn != null)
			conn.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
}

