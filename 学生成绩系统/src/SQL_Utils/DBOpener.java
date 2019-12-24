package SQL_Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 链接数据库的类
 * @author Nonoas
 *
 */
public class DBOpener implements SQLOpenHelper{
	
	private Connection conn=null;//创建数据库链接对象
	
	public DBOpener() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/学生成绩管理系统?serverTimezone=UTC";
			String username="root";//数据库用户名
			String password="Hss1356955215";//数据库密码
			conn=DriverManager.getConnection(url,username,password);
			//System.out.println(conn);//测试是否链接成功
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return Connection对象，连接到学生成绩管理系统数据库
	 */
	@Override
	public Connection getConnection() {
		return conn;
	}
}
