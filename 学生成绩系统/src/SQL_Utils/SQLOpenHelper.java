package SQL_Utils;
/**
 * ���ڴ�һ�����ݿ�
 * @author Nonoas
 */

import java.sql.Connection;

public interface SQLOpenHelper {
	/**
	 * ��ȡ���ݿ��Connection����
	 * @return
	 */
	public Connection getConnection();
}
