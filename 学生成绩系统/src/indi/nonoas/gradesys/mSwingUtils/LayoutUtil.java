package indi.nonoas.gradesys.mSwingUtils;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * ���ڷ�����沼�ֵľ�̬����������
 * @author Nonoas
 *
 */
public class LayoutUtil {
	/**
	 * �������������϶�Ŀհױ�ǩ
	 * @param width ��ǩ���
	 * @param height ��ǩ�߶�
	 * @return �ߴ�Ϊ(width,height)�Ŀհױ�ǩ
	 */
	public static JLabel set_interval(int width,int height) {
		JLabel jLabel=new JLabel();
		jLabel.setPreferredSize(new Dimension(width,height));
		return jLabel;
		
	}
	/**
	 * ���ñ�����ݾ���
	 * @param table
	 */
	public static void setTableCenter(JTable table){
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);
	}
}
