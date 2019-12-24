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
	
	private MyButton btn_inquire=new MyButton("查询");
	private JLabel jl_course=new JLabel("科目搜索");
	private JTextField jtf_course=new JTextField(20);//科目搜索框
	private JLabel jl_term=new JLabel("学期");
	private JComboBox<String> jcbBox_terms=new JComboBox<String>();//学年下拉列表
	private JTable jt_grade;			//查询结果表格
	private JScrollPane jsp_grade;  	//放置表格的滚动面板
	private JPanel jp_north=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
	private JPanel jp_center=new JPanel(new GridLayout());
	private JPanel jp_south=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
	
	public  IndividualGradePanel(String str) {
		setLayout(new BorderLayout(10,10));
		
		jp_north.add(jl_course);
		jp_north.add(jtf_course);
		jp_north.add(LayoutUtil.set_interval(10,10));//添加空隙
		jp_north.add(jl_term);
		jp_north.add(init_jcbBox_term());		//初始化“科目”下拉列表
		jp_north.add(LayoutUtil.set_interval(10,10));	//添加空隙
		jp_north.add(LayoutUtil.set_interval(100,10));	//添加空隙
		jp_north.add(btn_inquire);				//添加“查询按钮”
		
		jp_center.setBorder(BorderFactory.createEtchedBorder());
		jp_center.add(new JLabel("暂无查询数据",JLabel.CENTER));
		///////“查询”按钮事件
		btn_inquire.addActionListener((e)->{	
			jp_center.removeAll();
			jp_center.repaint();
			jp_center.add(this.inquire(str),BorderLayout.CENTER); //查询
			revalidate();
		});
		
		add(jp_north,BorderLayout.NORTH);
		add(jp_center,BorderLayout.CENTER);
		add(jp_south,BorderLayout.SOUTH);
		add(LayoutUtil.set_interval(10,10),BorderLayout.EAST);
		add(LayoutUtil.set_interval(10,10),BorderLayout.WEST);
		
	}
	/**
	 * 按条件筛选成绩
	 * @return 成绩JScrllPane 
	 */
	private JScrollPane inquire(String username) {
		String strSQLcourse="select course,score from grade where id="+username+" and course like ? ";
		String strSQL="select course,score from grade where id="+username;
		String s=jtf_course.getText().trim();//获取搜索得“科目”
		Connection conn=new DBOpener().getConnection();
		int rows=0,cols=0;//行数，列数
		String[] strsTitle= {"课程","成绩","单科排名","等级"};
		String[][] strssValues = null;
		try {
			PreparedStatement pstmt = null;
			if(s.equals(""))			//如果课程为空
				pstmt = conn.prepareStatement(strSQL);
			else {						//如果课程不为空
				pstmt = conn.prepareStatement(strSQLcourse);
				pstmt.setString(1, "%"+s+"%");
			}
			ResultSet rstSet=pstmt.executeQuery();
			rstSet.last();
			rows=rstSet.getRow();
			cols=rstSet.getMetaData().getColumnCount();
			rstSet.beforeFirst();
			strssValues=new String[rows][cols+2];//JTable数据
			for(int i=0;i<rows;i++) {
				rstSet.next();
				int score=rstSet.getInt(2);//查询结果 成绩
				strssValues[i][0]=rstSet.getString(1);//课程列
				strssValues[i][1]=String.valueOf(score);
				strssValues[i][2]=this.scoreRank(strssValues[i][0],score);//单科排名
				strssValues[i][3]=this.scoreRange(Integer.valueOf(score));//判断分数区间
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
	 * 初始化“科目”下拉列表
	 * @return “科目”下拉列表组件
	 */
	private JComboBox<String> init_jcbBox_term() {
		String[] items= {"2019-2020第一学期","2019-2020第二学期"};
		for(String item : items) {
			jcbBox_terms.addItem(item);
		}
		return jcbBox_terms;
	}
	/**
	 * 判断成绩的等级
	 * @param score 分数
	 * @return 等级(A,B,C)
	 */
	private String scoreRange(int score) {
		if(score>=0&&score<60)	return "C(不合格)";
		else if(score>=60&&score<90) return "B(合格)";
		else return "A(优秀)";
	}
	/**
	 * 单科成绩排名
	 * @param course 课程
	 * @param score 成绩
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
			result=rSet.getInt(1)+1;//排名
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return String.valueOf(result);		//返回结果
	}
}
