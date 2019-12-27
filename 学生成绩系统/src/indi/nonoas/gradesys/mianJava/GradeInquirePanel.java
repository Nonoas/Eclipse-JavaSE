package indi.nonoas.gradesys.mianJava;
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
import indi.nonoas.gradesys.mSwingUtils.LayoutUtil;
import indi.nonoas.gradesys.mSwingUtils.MyButton;

public class GradeInquirePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private MyButton btn_inquire=new MyButton("查询");
	private JLabel jl_id=new JLabel("学号");
	private JTextField jtf_id=new JTextField(20);
	private JLabel jl_course=new JLabel("科目");
	private JComboBox<String> jcbBox_course=new JComboBox<String>();//科目下拉列表
	private JLabel jl_sort=new JLabel("排序方式");
	private JComboBox<String> jcbBox_sort=new JComboBox<String>();//排序下拉列表
	private JTable jt_grade;			//查询结果表格
	private JScrollPane jsp_grade;  	//放置表格的滚动面板
	private JPanel jp_north=new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
	private JPanel jp_center=new JPanel(new GridLayout());
	private JPanel jp_south=new JPanel(new FlowLayout(FlowLayout.CENTER,10,20));
	private JLabel jl_result=new JLabel("单科统计结果：");
	private JLabel jlb_A=new JLabel("90分以上:");
	private JLabel jlb_B=new JLabel("60-89分:");
	private JLabel jlb_C=new JLabel("60分以下:");
	private JLabel jlb_avg=new JLabel("平均分:");
	private JTextField jtf_A=new JTextField(10);
	private JTextField jtf_B=new JTextField(10);
	private JTextField jtf_C=new JTextField(10);
	private JTextField jtf_avg=new JTextField(10);
	
	public GradeInquirePanel() {
		setLayout(new BorderLayout(10,10));
		
		jp_north.add(jl_id);						//添加“学号标签”以及“学号文本框”
		jp_north.add(jtf_id);
		jp_north.add(LayoutUtil.set_interval(10,10));	//添加空隙
		jp_north.add(jl_course);
		jp_north.add(init_jcbBox_course());		//初始化“科目”下拉列表
		jp_north.add(LayoutUtil.set_interval(10,10));	//添加空隙
		jp_north.add(jl_sort);
		jp_north.add(init_jcbBox_sort());		//初始化“排序方式”下拉列表
		jp_north.add(LayoutUtil.set_interval(100,10));	//添加空隙
		jp_north.add(btn_inquire);				//添加“查询按钮”
		
		jp_center.setBorder(BorderFactory.createEtchedBorder());
		jp_center.add(new JLabel("暂无查询数据",JLabel.CENTER));
		///////“查询”按钮事件
		btn_inquire.addActionListener((e)->{	
			jp_center.removeAll();
			jp_center.repaint();
			jp_center.add(this.inquire(),BorderLayout.CENTER); //查询
			this.setNumOfStatistics();
			revalidate();
		});
		
		jtf_A.setEditable(false);jtf_B.setEditable(false);jtf_C.setEditable(false);//设置统计框不可编辑
		jtf_avg.setEditable(false);
		jp_south.add(jl_result);
		jp_south.add(jlb_A); jp_south.add(jtf_A);
		jp_south.add(LayoutUtil.set_interval(10, 10));
		jp_south.add(jlb_B); jp_south.add(jtf_B);
		jp_south.add(LayoutUtil.set_interval(10, 10));
		jp_south.add(jlb_C); jp_south.add(jtf_C);
		jp_south.add(LayoutUtil.set_interval(10, 10));
		jp_south.add(jlb_avg); jp_south.add(jtf_avg);
		
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
	private JScrollPane inquire() {
		String strId=jtf_id.getText().trim();//学号
		String strSort=(String) jcbBox_sort.getSelectedItem();//排序		
		
		String sqlSearch0="select grade.id,stu_info.name,grade.course,grade.score "
				+ "from stu_info,grade where stu_info.id=grade.id and grade.id=? and grade.course=? order by ";
		String sqlSearch1="select grade.id,stu_info.name,grade.course,grade.score "
				+ "from stu_info,grade where stu_info.id=grade.id and grade.course=? order by";
		String sqlSearch;
		String sqlFinal;
		//System.out.println(strSort);
		if(strId.equals("")) sqlSearch=sqlSearch1;
		else sqlSearch=sqlSearch0;//设置学号筛选语句
		
		switch(strSort){
			case "分数升序":sqlFinal=sqlSearch+" grade.score asc";break;
			case "分数降序":sqlFinal=sqlSearch+" grade.score desc";break;
			default:sqlFinal=sqlSearch+" id asc";
		}
		
		ResultSet rsSet = null;
		int rows = 0;//查询结果 行数
		int cols = 0;//查询结果 列数
		try {	
			rsSet=setPrstmt(strId, sqlFinal).executeQuery();
			rsSet.last();//查询游标移动到最后以获取“行数”
			rows=rsSet.getRow();
			cols=rsSet.getMetaData().getColumnCount();
			System.out.println(rows);
			System.out.println(cols);
			rsSet.beforeFirst();//游标移到第一个元素之前，以获取“查询结果”
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		String[] strsTitle= {"序号","学号","姓名","科目","成绩"};
		String[][] strsValues=new String[rows][cols+1];
		try {
			for(int i=0;i<rows;i++) {
				rsSet.next();
				strsValues[i][0]=i+1+"";//排序
				strsValues[i][1]=rsSet.getString(1);//学号
				strsValues[i][2]=rsSet.getString(2);//科目
				strsValues[i][3]=rsSet.getString(3);//科目
				strsValues[i][4]=String.valueOf(rsSet.getInt(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jt_grade=new JTable(strsValues,strsTitle);
		LayoutUtil.setTableCenter(jt_grade);
		jsp_grade=new JScrollPane(jt_grade);
		return jsp_grade;
	}
	/**
	 * 统计分数段比例
	 */
	private void setNumOfStatistics() {
		String strCourse=jcbBox_course.getSelectedItem().toString();
		String sqlCurrentA="select count(*) from grade where course=? and score between 90 and 100";
		String sqlCurrentB="select count(*) from grade where course=? and score between 60 and 89";
		String sqlCurrentC="select count(*) from grade where course=? and score between 0 and 59";
		String sqlTotal="select count(*) from grade where course=?";
		String sqlAvg="select avg(score) from grade where course=?";
		
		float numA=0;float numB=0;float numC=0;float numT=0;float avgScore=0;
		
		Connection conn=new DBOpener().getConnection();
		try {
			PreparedStatement pstmtA=conn.prepareStatement(sqlCurrentA);
			PreparedStatement pstmtB=conn.prepareStatement(sqlCurrentB);
			PreparedStatement pstmtC=conn.prepareStatement(sqlCurrentC);
			PreparedStatement pstmtT=conn.prepareStatement(sqlTotal);
			PreparedStatement pstmtAVG=conn.prepareStatement(sqlAvg);
			pstmtA.setString(1, strCourse);
			pstmtB.setString(1, strCourse);
			pstmtC.setString(1, strCourse);
			pstmtT.setString(1, strCourse);
			pstmtAVG.setString(1, strCourse);
			ResultSet[] rSets= {pstmtA.executeQuery(),pstmtB.executeQuery(),
					pstmtC.executeQuery(),pstmtT.executeQuery(),pstmtAVG.executeQuery()};
			for(ResultSet rSet:rSets) rSet.next();
			numA=rSets[0].getFloat(1);
			numB=rSets[1].getFloat(1);
			numC=rSets[2].getFloat(1);
			numT=rSets[3].getFloat(1);
			avgScore=rSets[4].getFloat(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//统计百分比
		numA=numA/numT*100;
		numB=numB/numT*100;
		numC=numC/numT*100;
		jtf_A.setText(numA+"%");
		jtf_B.setText(numB+"%");
		jtf_C.setText(numC+"%");
		jtf_avg.setText(avgScore+"");
	}
	/**
	 * 创建查询的PrepareStatement对象
	 * @param str id栏
	 * @param sql 查询Sql
	 * @return pstmt
	 */
	private PreparedStatement setPrstmt(String str,String sql) {
		Connection con=new DBOpener().getConnection();
		PreparedStatement pstmt = null ;
		try {
			pstmt = con.prepareStatement(sql);
			if(str.equals("")) {
				pstmt.setString(1, jcbBox_course.getSelectedItem().toString());
			}else {
				pstmt.setString(1, str);
				pstmt.setString(2, jcbBox_course.getSelectedItem().toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	/**
	 * 初始化“科目”下拉列表
	 * @return “科目”下拉列表组件
	 */
	private JComboBox<String> init_jcbBox_course() {
		String[] items= {"线性代数","高等数学","离散结构","英语(1)","C语言","数据库"};
		for(String item : items) {
			jcbBox_course.addItem(item);
		}
		return jcbBox_course;
	}
	/**
	 * 初始化“排序方式”下拉列表
	 * @return “排序方式”下拉列表组件
	 */
	private JComboBox<String> init_jcbBox_sort() {
		String[] items= {"学号顺序","分数降序","分数升序"};
		for(String item : items) {
			jcbBox_sort.addItem(item);
		}
		return jcbBox_sort;
	}
	
}
