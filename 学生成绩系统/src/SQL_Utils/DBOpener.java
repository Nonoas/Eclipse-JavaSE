package SQL_Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * �������ݿ����
 * @author Nonoas
 *
 */
public class DBOpener implements SQLOpenHelper{
	
	private Connection conn=null;//�������ݿ����Ӷ���
	
	public DBOpener() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/ѧ���ɼ�����ϵͳ?serverTimezone=UTC";
			String username="root";//���ݿ��û���
			String password="Hss1356955215";//���ݿ�����
			conn=DriverManager.getConnection(url,username,password);
			//System.out.println(conn);//�����Ƿ����ӳɹ�
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return Connection�������ӵ�ѧ���ɼ�����ϵͳ���ݿ�
	 */
	@Override
	public Connection getConnection() {
		return conn;
	}
}
