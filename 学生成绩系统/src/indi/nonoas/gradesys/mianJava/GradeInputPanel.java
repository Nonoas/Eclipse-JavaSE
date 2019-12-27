package indi.nonoas.gradesys.mianJava;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import SQL_Utils.DBOpener;
import indi.nonoas.gradesys.mSwingUtils.LayoutUtil;
import indi.nonoas.gradesys.mSwingUtils.MyButton;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class GradeInputPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JTextField jt_filePath=new JTextField(50);
	private MyButton btn_upLoadfile=new MyButton("上传文件");
	private MyButton btn_enterUpload=new MyButton("开始导入");
	private JTable jTable;
	private JScrollPane jScrollPane;
	private JPanel jp_north=new JPanel();
	private JPanel jp_center=new JPanel(new GridLayout());
	
	
	public GradeInputPanel() {
		setLayout(new BorderLayout(20,20));
		jt_filePath.setEditable(false);//设置文本框不可编辑
		
		btn_upLoadfile.addActionListener((e)->{
			JFileChooser jFileChooser=new JFileChooser();//创建文件选择器对象
			FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("Excel文档", "xls","xlsx");
			jFileChooser.setFileFilter(fileFilter);		//设置文件过滤器
			int i=jFileChooser.showOpenDialog(getRootPane());//显示文件选择器，返回点击按钮的ID
			if(i==JFileChooser.APPROVE_OPTION) {		//如果点击了确定
				File file=jFileChooser.getSelectedFile();//获取选中的文件
				jt_filePath.setText(file.getAbsolutePath());//显示选中文件的绝对路径
				this.setTable(file);
			}
		});
		
		btn_enterUpload.addActionListener((e)->{
			if(jt_filePath.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(getRootPane(), "请先选择文件！","提示",JOptionPane.INFORMATION_MESSAGE);
			}else {
				this.saveToDB();				//保存到数据库
				JOptionPane.showMessageDialog(getRootPane(), "上传成功！","提示",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		jp_north.add(jt_filePath);
		jp_north.add(btn_upLoadfile);
		jp_north.add(btn_enterUpload);
		
		jp_center.setBorder(BorderFactory.createEtchedBorder());
		jp_center.add(new JLabel("暂无上传文件",JLabel.CENTER));
		
		add(jp_north,BorderLayout.NORTH);
		add(jp_center,BorderLayout.CENTER);
		add(LayoutUtil.set_interval(10,0),BorderLayout.EAST);
		add(LayoutUtil.set_interval(10,0),BorderLayout.WEST);
		add(LayoutUtil.set_interval(0,10),BorderLayout.SOUTH);
	}
	/**
	 * 导入文件后显示表格
	 * @param file 选择的文件
	 */
	private void setTable(File file) {
		Workbook workbook=null;
		Sheet sheet = null;
		try {
			workbook=Workbook.getWorkbook(file);
			sheet=workbook.getSheet(0);
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rows=sheet.getRows();			//获取行数
		int cols=sheet.getColumns();		//获取列数
		String[] strs=new String[cols];		//表头
		String[][] strs2=new String[rows-1][cols];//表格内容
		for(int i=0;i<cols;i++) {
			strs[i]=sheet.getCell(i, 0).getContents();
		}
		for(int i=1;i<rows;i++) {
			for(int j=0;j<cols;j++) {
				strs2[i-1][j]=sheet.getCell(j,i).getContents();
			}
		}
		this.jTable=new JTable(strs2,strs);
		jTable.setEnabled(false);
		LayoutUtil.setTableCenter(jTable);		//设置表格居中显示
		jp_center.removeAll();
		jp_center.repaint();
		jScrollPane=new JScrollPane(jTable);
		jp_center.add(jScrollPane,BorderLayout.CENTER);
		this.revalidate();
	}
	/**
	 * 将表格中的数据插入到数据库，主键重复则原数据将被覆盖
	 */
	private void saveToDB() {
		String sql="replace into grade(id,course,score) values (?,?,?)";
		int rows=jTable.getRowCount();//获取表格行数
		Connection conn=new DBOpener().getConnection();
		try {
			PreparedStatement pStatement=conn.prepareStatement(sql);
			for(int i=0;i<rows;i++) {
				pStatement.setString(1, jTable.getValueAt(i, 0).toString());
				pStatement.setString(2, jTable.getValueAt(i,1).toString());
				pStatement.setInt(3,Integer.valueOf((String) jTable.getValueAt(i,2)));
				pStatement.addBatch();
			}
			pStatement.executeBatch();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
