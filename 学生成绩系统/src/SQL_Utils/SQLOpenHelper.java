package SQL_Utils;
/**
 * 用于打开一个数据库
 * @author Nonoas
 */

import java.sql.Connection;

public interface SQLOpenHelper {
	/**
	 * 获取数据库的Connection对象
	 * @return
	 */
	public Connection getConnection();
}
