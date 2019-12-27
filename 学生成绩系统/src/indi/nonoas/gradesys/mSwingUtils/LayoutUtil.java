package indi.nonoas.gradesys.mSwingUtils;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 用于方便界面布局的静态方法工具类
 * @author Nonoas
 *
 */
public class LayoutUtil {
	/**
	 * 用于生成组件间隙的空白标签
	 * @param width 标签宽度
	 * @param height 标签高度
	 * @return 尺寸为(width,height)的空白标签
	 */
	public static JLabel set_interval(int width,int height) {
		JLabel jLabel=new JLabel();
		jLabel.setPreferredSize(new Dimension(width,height));
		return jLabel;
		
	}
	/**
	 * 设置表格数据居中
	 * @param table
	 */
	public static void setTableCenter(JTable table){
		DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
		r.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, r);
	}
}
