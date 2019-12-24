package SQL_Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLOperations {
	private Connection connection;// 数据库连接对象

	public SQLOperations(Connection connection) {
		this.connection = connection;
	}

	/**
	 * 查询表中数据
	 * @param table         指定表名
	 * @param columns       指定查询的列，为空则返回所有列
	 * @param selection     指定where子句，可以使用占位符“？”
	 * @param selectionArgs 占位符的值，不包含占位符则为null
	 * @param orderBy       排序方式，可以为null
	 * @return ResultSet 返回结果集
	 */
	public ResultSet query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		StringBuffer sql = new StringBuffer();// 拼接查询条件
		int numOfArgs = selectionArgs.length; // 通配符个数
		PreparedStatement ps = null;
		ResultSet rs = null;
		/* 拼接SQL语句 */
		sql.append("select ");
		if (columns != null) // 如果查询列不为空
			for (int i = 0; i < columns.length; i++) {
				sql.append(columns[i]);
				if (i < columns.length - 1)
					sql.append(",");
				else
					sql.append(" ");
			}
		else
			sql.append("* ");
		sql.append(" from " + table + " " + selection);
		if (orderBy != null)
			sql.append(" " + orderBy);// 排序方式

		try {
			ps = connection.prepareStatement(sql.toString());
			for (int i = 0; i < numOfArgs; i++) { // 设置通配符的值
				ps.setObject(i + 1, selectionArgs[i]);
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 向表中插入数据
	 * @param table   表名
	 * @param nullColimnHack 指定当values为空时，将那个字段设置为空
	 * @param values  具体的字段值，键值对集合
	 * @return 插入的条目数量
	 */
	public long insert(String table, String nullColimnHack, ContentValues values) {
		long countItems = 0;// 影响的条目数量
		StringBuffer sql = new StringBuffer();// 拼接查询语句
		List<String> listKey = values.getkeys();
		List<Object> listValue = values.getValues();
		int len = listKey.size();// 获取字段个数
		/*拼接SQL语句*/
		sql.append("update set " + table + "(");
		if (values != null && values.size() > 0) {
			for (int i = 0; i < len; i++) {
				sql.append(listKey.get(i));
				if (i < listKey.size() - 1)
					sql.append(",");
			}
			sql.append(") values(");
			for (int i = 0; i < len; i++) {
				sql.append("?");
				if (i < listValue.size() - 1)
					sql.append(",");
			}
			sql.append(")");
		} else {
			sql.append(nullColimnHack + ") ");
			sql.append("null");
		}

		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(sql.toString());
			for (int i = 0; i < len; i++) {
				ps.setObject(i + 1, listValue.get(i));
			}
			countItems = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countItems;
	}

	/**
	 * 更新表中数据
	 * @param table 表名
	 * @param values 具体的字段值，键值对集合
	 * @param whereClause 指定where子句，可以使用占位符“？”
	 * @param whereArgs  占位符的值，不包含占位符则为null
	 * @return
	 */
	public long update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		long countItems = 0;	// 影响的条目数量
		StringBuffer sql=new StringBuffer();// 拼接查询语句
		List<String> listKeys=values.getkeys();
		List<Object> listValues=values.getValues();
		int len=listKeys.size();
		PreparedStatement ps;
		
		sql.append("update "+table+" "+"set"+" ");
		for(int i=0;i<len;i++) {
			sql.append(listKeys.get(i)+"=?");
			if(i<len-1) 
				sql.append(",");
		}
		sql.append(" "+whereClause);
		
		try {
			ps=connection.prepareStatement(sql.toString());
			for(int i=0;i<len;i++) {
				ps.setObject(i+1, listValues.get(i));
			}
			countItems=ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countItems;
	}

	/**
	 * 删除表中数据
	 * @param table 表名
	 * @param whereClause where子句，可以使用占位符“？”
	 * @param whereArgs 占位符的值，不包含占位符则为null
	 * @return
	 */
	public long delete(String table, String whereClause, String[] whereArgs) {
		long countItems = 0;	// 影响的条目数量
		StringBuffer sql=new StringBuffer();// 拼接查询语句
		PreparedStatement ps;
		sql.append("delete from "+table+" "+whereClause);
		try {
			ps=connection.prepareStatement(sql.toString());
			for(int i=0;i<whereArgs.length;i++) {
				ps.setObject(i+1,whereArgs);
			}
			countItems=ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return countItems;
	}

	/**
	 * 执行通用SQL语句
	 * @param sql 完整得SQL语句
	 * @return boolean
	 */
	public boolean excSQL(String sql) {
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(sql);
			return ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 关闭connection对象
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public static void main(String[] args) {
//		Connection connection=new DBOpener().getConnection();
//		SQLOperations sqlOperations=new SQLOperations(connection);
//		ResultSet rs=sqlOperations.query("grade", new String[]{"id","course"}, "where id=?", new String[] {"0001"}, null);
//		try {
//			while(rs.next()) {
//				System.out.print(rs.getString("id")+"\t");
//				System.out.println(rs.getString("course"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
}
