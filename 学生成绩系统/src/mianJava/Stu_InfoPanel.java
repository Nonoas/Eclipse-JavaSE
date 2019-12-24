package mianJava;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import SQL_Utils.DBOpener;
import mSwingUtils.MyButton;

public class Stu_InfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private JLabel jl_ID=new JLabel("学号:");//学号
	private JLabel jl_name=new JLabel("姓名:");//姓名
	private JLabel jl_sex=new JLabel("性别:");//性别
	private JLabel jl_age=new JLabel("年龄:");//年龄
	private JLabel jl_tel=new JLabel("电话:");//电话
	private MyButton btn_logout=new MyButton("退出登录");
	private MyButton btn_modify=new MyButton("修改资料");
	private JTextField jtf_ID=new JTextField();			//“学号” 文本框
	private JTextField jtf_name=new JTextField();		//“姓名” 文本框
	private JTextField jtf_sex=new JTextField();		//“性别” 文本框
	private JTextField jtf_age=new JTextField();		//“年龄” 文本框
	private JTextField jtf_tel=new JTextField();		//“电话” 文本框
	private boolean jtf_changeable=false;	//用于判断用户信息是否可编辑
	private JPanel jp_center=new JPanel(new GridLayout(6,2,10,10)); //中面板：存放用户信息
	private JPanel jp_south=new JPanel(new FlowLayout(FlowLayout.CENTER,40,40));// 南面板：存放按钮
	
	public Stu_InfoPanel(String username,JFrame frame) {
		
		setLayout(new BorderLayout());
		
		Connection connection=new DBOpener().getConnection();
		String sql="select * from stu_info where id ='"+username+"'";//创建查询语句，查询当前用户所有信息
		try {
			Statement stmt=connection.createStatement();
			ResultSet rSet=stmt.executeQuery(sql);
			while(rSet.next()) {
				jtf_ID.setText(rSet.getString("ID"));
				jtf_name.setText(rSet.getString("name"));
				jtf_sex.setText(rSet.getString("sex"));
				jtf_age.setText(rSet.getString("age"));
				jtf_tel.setText(rSet.getString("tel"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		jtf_ID.setEditable(false);
		on_offTextfield();//设置信息文本框不可编辑
		
		jp_center.setBorder(BorderFactory.createEmptyBorder(60,350,60,350));
		jp_center.add(jl_ID);
		jp_center.add(jtf_ID);
		jp_center.add(jl_name);
		jp_center.add(jtf_name);
		jp_center.add(jl_sex);
		jp_center.add(jtf_sex);
		jp_center.add(jl_age);
		jp_center.add(jtf_age);
		jp_center.add(jl_tel);
		jp_center.add(jtf_tel);
		
		jp_south.add(btn_modify);
		jp_south.add(btn_logout);
		
		//“修改资料”按钮监听
		btn_modify.addActionListener((e)->{
			if(!this.jtf_changeable) {
				this.jtf_changeable=!jtf_changeable;
				on_offTextfield();
				btn_modify.setText("保存修改");			//当用户开始填写资料时，修改按钮文字为“保存修改”
			}else {
				if(jtf_name.getText().trim().equals("")||jtf_sex.getText().trim().equals("")||
						jtf_age.getText().trim().equals("")||jtf_tel.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null,"所有项不能为空！");
				}else if(!jtf_sex.getText().trim().equals("男")&&!jtf_sex.getText().trim().equals("女")){
					JOptionPane.showMessageDialog(null,"填写的性别有误！");
				}else if(Integer.valueOf(jtf_age.getText().trim())>100){
					JOptionPane.showMessageDialog(null,"填写的年龄有误！");
				}else if(jtf_tel.getText().trim().length()!=11){
					JOptionPane.showMessageDialog(null,"填写的电话有误！");
				}else {
					this.DB_updata(username);		//将修改的用户信息保存到数据库
					btn_modify.setText("修改资料");		//当用户保存完填写资料时，修改按钮文字为“修改资料”
					this.jtf_changeable=!jtf_changeable;
					JOptionPane.showMessageDialog(null,"保存成功！");
					on_offTextfield();
				}
			}
		});
		// “退出登录”按钮监听
		btn_logout.addActionListener((e)->{
			int select=JOptionPane.showConfirmDialog(null, "确认退出当前账号？","注销",
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(select==JOptionPane.OK_OPTION) {
				frame.dispose();					//关闭当前窗口
				new Stu_Login();						//重新打开教师登录窗口
			}
		});
		
		add(jp_center,BorderLayout.CENTER);
		add(jp_south,BorderLayout.SOUTH);
	}
	/**
	 * 用户资料修改开关
	 */
	private void on_offTextfield() {
		JTextField[] jtFields= {jtf_name,jtf_sex,jtf_age,jtf_tel};
		if(jtf_changeable) {
			for(int i=0;i<jtFields.length;i++) {
				jtFields[i].setEditable(true);
			}
		}else {
			for(int i=0;i<jtFields.length;i++) {
				jtFields[i].setEditable(false);
			}
		}
	}
	/**
	 * 将修改的用户信息保存到数据库
	 * @param username 用户名ID（主键）
	 */
	private void DB_updata(String username) {
		System.out.println(username);
		Connection conn=new DBOpener().getConnection();
		String sql="update stu_info set name=?,sex=?,age=?,tel=? where id=?";
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, jtf_name.getText().trim());
			ps.setString(2, jtf_sex.getText().trim());
			ps.setInt(3, Integer.valueOf(jtf_age.getText().trim()));
			ps.setString(4, jtf_tel.getText().trim());
			ps.setString(5, username);
			ps.execute();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
