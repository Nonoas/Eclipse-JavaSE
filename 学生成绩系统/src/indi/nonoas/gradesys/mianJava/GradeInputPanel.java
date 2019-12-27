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
	private MyButton btn_upLoadfile=new MyButton("�ϴ��ļ�");
	private MyButton btn_enterUpload=new MyButton("��ʼ����");
	private JTable jTable;
	private JScrollPane jScrollPane;
	private JPanel jp_north=new JPanel();
	private JPanel jp_center=new JPanel(new GridLayout());
	
	
	public GradeInputPanel() {
		setLayout(new BorderLayout(20,20));
		jt_filePath.setEditable(false);//�����ı��򲻿ɱ༭
		
		btn_upLoadfile.addActionListener((e)->{
			JFileChooser jFileChooser=new JFileChooser();//�����ļ�ѡ��������
			FileNameExtensionFilter fileFilter=new FileNameExtensionFilter("Excel�ĵ�", "xls","xlsx");
			jFileChooser.setFileFilter(fileFilter);		//�����ļ�������
			int i=jFileChooser.showOpenDialog(getRootPane());//��ʾ�ļ�ѡ���������ص����ť��ID
			if(i==JFileChooser.APPROVE_OPTION) {		//��������ȷ��
				File file=jFileChooser.getSelectedFile();//��ȡѡ�е��ļ�
				jt_filePath.setText(file.getAbsolutePath());//��ʾѡ���ļ��ľ���·��
				this.setTable(file);
			}
		});
		
		btn_enterUpload.addActionListener((e)->{
			if(jt_filePath.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(getRootPane(), "����ѡ���ļ���","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			}else {
				this.saveToDB();				//���浽���ݿ�
				JOptionPane.showMessageDialog(getRootPane(), "�ϴ��ɹ���","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		jp_north.add(jt_filePath);
		jp_north.add(btn_upLoadfile);
		jp_north.add(btn_enterUpload);
		
		jp_center.setBorder(BorderFactory.createEtchedBorder());
		jp_center.add(new JLabel("�����ϴ��ļ�",JLabel.CENTER));
		
		add(jp_north,BorderLayout.NORTH);
		add(jp_center,BorderLayout.CENTER);
		add(LayoutUtil.set_interval(10,0),BorderLayout.EAST);
		add(LayoutUtil.set_interval(10,0),BorderLayout.WEST);
		add(LayoutUtil.set_interval(0,10),BorderLayout.SOUTH);
	}
	/**
	 * �����ļ�����ʾ���
	 * @param file ѡ����ļ�
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
		int rows=sheet.getRows();			//��ȡ����
		int cols=sheet.getColumns();		//��ȡ����
		String[] strs=new String[cols];		//��ͷ
		String[][] strs2=new String[rows-1][cols];//�������
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
		LayoutUtil.setTableCenter(jTable);		//���ñ�������ʾ
		jp_center.removeAll();
		jp_center.repaint();
		jScrollPane=new JScrollPane(jTable);
		jp_center.add(jScrollPane,BorderLayout.CENTER);
		this.revalidate();
	}
	/**
	 * ������е����ݲ��뵽���ݿ⣬�����ظ���ԭ���ݽ�������
	 */
	private void saveToDB() {
		String sql="replace into grade(id,course,score) values (?,?,?)";
		int rows=jTable.getRowCount();//��ȡ�������
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
