package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {

	public static final String name = "com.mysql.jdbc.Driver";
	/*public static final String url = "jdbc:mysql://localhost:3306/ecg?useUnicode=true&characterEncoding=utf-8";
	public static final String user = "root";
	public static final String password = "";*/
	public static final String url = "jdbc:mysql://localhost:3306/Ji0birn7?useUnicode=true&characterEncoding=utf-8";
	public static final String user = "Ji0birn7";
	public static final String password = "Di2iHS3JpvLy";
	
	

	public Connection conn = null;
	public PreparedStatement pst = null;

	public DBHelper(String sql) {
		// TODO Auto-generated constructor stub
		try {  
            Class.forName(name);//指定连接类型  
            conn = DriverManager.getConnection(url, user, password);//获取连接  
            pst = conn.prepareStatement(sql);//准备执行语句  
        } catch (Exception e) {  
        	System.out.println("建立数据库连接失败");
            e.printStackTrace();  
        }  
	}
	public void close() {  
        try {  
            this.conn.close();  
            this.pst.close();  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
    }  
}
