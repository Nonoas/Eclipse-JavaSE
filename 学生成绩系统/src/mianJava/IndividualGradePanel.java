package mianJava;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import SQL_Utils.DBOpener;
import mSwingUtils.LayoutUtil;
import mSwingUtils.MyButton;

public class IndividualGradePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private MyButton btn_inquire=new MyButton("��ѯ");
	private JLabel jl_course=new JLabel("��Ŀ����");
	private JTextField jtf_course=new JTextField(20);//��Ŀ������
	private JLabel jl_term=new JLabel("ѧ��");
	private JComboBox<String> jcbBox_terms=new JComboBox<String>();//ѧ�������б�
	private JTable jt_grade;			//��ѯ������
	private JScrollPane jsp_grade;  	//���ñ��Ĺ������
	private JPanel jp_north=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
	private JPanel jp_center=new JPanel(new GridLayout());
	private JPanel jp_south=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
	
	public  IndividualGradePanel(String str) {
		setLayout(new BorderLayout(10,10));
		
		jp_north.add(jl_course);
		jp_north.add(jtf_course);
		jp_north.add(LayoutUtil.set_interval(10,10));//��ӿ�϶
		jp_north.add(jl_term);
		jp_north.add(init_jcbBox_term());		//��ʼ������Ŀ�������б�
		jp_north.add(LayoutUtil.set_interval(10,10));	//��ӿ�϶
		jp_north.add(LayoutUtil.set_interval(100,10));	//��ӿ�϶
		jp_north.add(btn_inquire);				//��ӡ���ѯ��ť��
		
		jp_center.setBorder(BorderFactory.createEtchedBorder());
		jp_center.add(new JLabel("���޲�ѯ����",JLabel.CENTER));
		///////����ѯ����ť�¼�
		btn_inquire.addActionListener((e)->{	
			jp_center.removeAll();
			jp_center.repaint();
			jp_center.add(this.inquire(str),BorderLayout.CENTER); //��ѯ
			revalidate();
		});
		
		add(jp_north,BorderLayout.NORTH);
		add(jp_center,BorderLayout.CENTER);
		add(jp_south,BorderLayout.SOUTH);
		add(LayoutUtil.set_interval(10,10),BorderLayout.EAST);
		add(LayoutUtil.set_interval(10,10),BorderLayout.WEST);
		
	}
	/**
	 * ������ɸѡ�ɼ�
	 * @return �ɼ�JScrllPane 
	 */
	private JScrollPane inquire(String username) {
		String strSQLcourse="select course,score from grade where id="+username+" and course like ? ";
		String strSQL="select course,score from grade where id="+username;
		String s=jtf_course.getText().trim();//��ȡ�����á���Ŀ��
		Connection conn=new DBOpener().getConnection();
		int rows=0,cols=0;//����������
		String[] strsTitle= {"�γ�","�ɼ�","��������","�ȼ�"};
		String[][] strssValues = null;
		try {
			PreparedStatement pstmt = null;
			if(s.equals(""))			//����γ�Ϊ��
				pstmt = conn.prepareStatement(strSQL);
			else {						//����γ̲�Ϊ��
				pstmt = conn.prepareStatement(strSQLcourse);
				pstmt.setString(1, "%"+s+"%");
			}
			ResultSet rstSet=pstmt.executeQuery();
			rstSet.last();
			rows=rstSet.getRow();
			cols=rstSet.getMetaData().getColumnCount();
			rstSet.beforeFirst();
			strssValues=new String[rows][cols+2];//JTable����
			for(int i=0;i<rows;i++) {
				rstSet.next();
				int score=rstSet.getInt(2);//��ѯ��� �ɼ�
				strssValues[i][0]=rstSet.getString(1);//�γ���
				strssValues[i][1]=String.valueOf(score);
				strssValues[i][2]=this.scoreRank(strssValues[i][0],score);//��������
				strssValues[i][3]=this.scoreRange(Integer.valueOf(score));//�жϷ�������
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jt_grade=new JTable(strssValues,strsTitle);
		LayoutUtil.setTableCenter(jt_grade);
		jsp_grade=new JScrollPane(jt_grade);
		return jsp_grade;
	}
	/**
	 * ��ʼ������Ŀ�������б�
	 * @return ����Ŀ�������б����
	 */
	private JComboBox<String> init_jcbBox_term() {
		String[] items= {"2019-2020��һѧ��","2019-2020�ڶ�ѧ��"};
		for(String item : items) {
			jcbBox_terms.addItem(item);
		}
		return jcbBox_terms;
	}
	/**
	 * �жϳɼ��ĵȼ�
	 * @param score ����
	 * @return �ȼ�(A,B,C)
	 */
	private String scoreRange(int score) {
		if(score>=0&&score<60)	return "C(���ϸ�)";
		else if(score>=60&&score<90) return "B(�ϸ�)";
		else return "A(����)";
	}
	/**
	 * ���Ƴɼ�����
	 * @param course �γ�
	 * @param score �ɼ�
	 * @return
	 */
	private String scoreRank(String course,int score) {
		String sql="select count(id) from grade where course=? and score>?";
		Connection conn=new DBOpener().getConnection();
		int result=0;
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, course);
			pstmt.setInt(2, score);
			ResultSet rSet=pstmt.executeQuery();
			rSet.next();
			result=rSet.getInt(1)+1;//����
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(result);		//���ؽ��
	}
}
