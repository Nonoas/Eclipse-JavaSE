package SQL_Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SQLOperations {
	private Connection connection;// ���ݿ����Ӷ���

	public SQLOperations(Connection connection) {
		this.connection = connection;
	}

	/**
	 * ��ѯ��������
	 * @param table         ָ������
	 * @param columns       ָ����ѯ���У�Ϊ���򷵻�������
	 * @param selection     ָ��where�Ӿ䣬����ʹ��ռλ��������
	 * @param selectionArgs ռλ����ֵ��������ռλ����Ϊnull
	 * @param orderBy       ����ʽ������Ϊnull
	 * @return ResultSet ���ؽ����
	 */
	public ResultSet query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		StringBuffer sql = new StringBuffer();// ƴ�Ӳ�ѯ����
		int numOfArgs = selectionArgs.length; // ͨ�������
		PreparedStatement ps = null;
		ResultSet rs = null;
		/* ƴ��SQL��� */
		sql.append("select ");
		if (columns != null) // �����ѯ�в�Ϊ��
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
			sql.append(" " + orderBy);// ����ʽ

		try {
			ps = connection.prepareStatement(sql.toString());
			for (int i = 0; i < numOfArgs; i++) { // ����ͨ�����ֵ
				ps.setObject(i + 1, selectionArgs[i]);
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * ����в�������
	 * @param table   ����
	 * @param nullColimnHack ָ����valuesΪ��ʱ�����Ǹ��ֶ�����Ϊ��
	 * @param values  ������ֶ�ֵ����ֵ�Լ���
	 * @return �������Ŀ����
	 */
	public long insert(String table, String nullColimnHack, ContentValues values) {
		long countItems = 0;// Ӱ�����Ŀ����
		StringBuffer sql = new StringBuffer();// ƴ�Ӳ�ѯ���
		List<String> listKey = values.getkeys();
		List<Object> listValue = values.getValues();
		int len = listKey.size();// ��ȡ�ֶθ���
		/*ƴ��SQL���*/
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
	 * ���±�������
	 * @param table ����
	 * @param values ������ֶ�ֵ����ֵ�Լ���
	 * @param whereClause ָ��where�Ӿ䣬����ʹ��ռλ��������
	 * @param whereArgs  ռλ����ֵ��������ռλ����Ϊnull
	 * @return
	 */
	public long update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		long countItems = 0;	// Ӱ�����Ŀ����
		StringBuffer sql=new StringBuffer();// ƴ�Ӳ�ѯ���
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
	 * ɾ����������
	 * @param table ����
	 * @param whereClause where�Ӿ䣬����ʹ��ռλ��������
	 * @param whereArgs ռλ����ֵ��������ռλ����Ϊnull
	 * @return
	 */
	public long delete(String table, String whereClause, String[] whereArgs) {
		long countItems = 0;	// Ӱ�����Ŀ����
		StringBuffer sql=new StringBuffer();// ƴ�Ӳ�ѯ���
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
	 * ִ��ͨ��SQL���
	 * @param sql ������SQL���
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
	 * �ر�connection����
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
