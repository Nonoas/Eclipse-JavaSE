package indi.nonoas.gradesys.mianJava;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import SQL_Utils.DBOpener;
import indi.nonoas.gradesys.mSwingUtils.MyButton;

public class Tec_Login extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel jl_title=new JLabel("登   录",JLabel.CENTER);
	private JLabel jl_username=new JLabel("用户名",null,JLabel.RIGHT);
	private JLabel jl_password=new JLabel("密   码",null,JLabel.RIGHT);
	private JTextField jt_username=new JTextField(50);//用户名框
	private JPasswordField jpw_password=new JPasswordField(50);//密码框
	private MyButton btn_login=new MyButton("登录");
	private MyButton btn_back=new MyButton("返回");
	private MyButton btn_register=new MyButton("注册");
	private JPanel jp1=new JPanel(new GridLayout(2,2,20,25));
	private JPanel jp2=new JPanel(new FlowLayout(FlowLayout.CENTER,40,30));
	
	public Tec_Login() {
		setTitle("管理员系统");
		setBounds(0, 0,500,350);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3,1));
		setLocationRelativeTo(null);//使窗口剧中显示在屏幕
		Container c=getContentPane();
		
		jl_title.setBounds(200, 30, 100, 50);
		jl_title.setFont(new Font("黑体",Font.BOLD,20));
		
		jp1.add(jl_username); 
		jp1.add(jt_username);//添加用户名标签及输入框
		jp1.add(jl_password); 
		jp1.add(jpw_password);//添加密码及密码标签
		jp1.setBorder(new EmptyBorder(0, -100, 0, 100));
		
		jp2.add(btn_login);
		btn_back.setSize(40, 10);
		jp2.add(btn_back);
		btn_register.setForeground(Color.BLUE);//设置注册标签为蓝色
		jp2.add(btn_register);
		/////////////登录按钮监听////////////////
		btn_login.addActionListener((e)->{
			String str_name=jt_username.getText().trim();//获取用户输入的用户名
			char[] str_password=jpw_password.getPassword();//获取用户输入的密码
			if(str_name.equals("")) {             			//判断 用户名 是否为空
				JOptionPane.showMessageDialog(null, "用户名不能为空！","登录异常",JOptionPane.INFORMATION_MESSAGE);
			}else if(str_password==null||str_password.length==0) {//判断 密码 是否为空
				JOptionPane.showMessageDialog(null, "密码不能为空！","登录异常",JOptionPane.INFORMATION_MESSAGE);
			}else {										//访问数据库验证信息
				Connection conn=new DBOpener().getConnection();
				try {
					PreparedStatement pStatement=conn.prepareStatement("select password from user_password "
							+ "where id=? and tec_root=1");//使用PrepareStatement对象有效避免sql注入
					pStatement.setString(1, str_name);
					ResultSet rSet=pStatement.executeQuery();
					String password=new String();
					while(rSet.next()) {
						password=rSet.getString("password");
					}
					if(password.equals(new String(str_password))) {
						dispose();
						new Tec_opJFrame(str_name);//进入管理员系统界面
						System.out.println("登录成功");
					}else {
						JOptionPane.showMessageDialog(null, "用户名或密码错误！","登录异常",JOptionPane.INFORMATION_MESSAGE);
						jt_username.setText("");//清空用户名栏
						jpw_password.setText("");//清空密码栏
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		///////////////返回按钮监听///////////////////
		btn_back.addActionListener((e)->{
			dispose(); new SysChoseJframe();//关闭当前窗口，打开系统选择窗口
		});
		//////////////注册按钮监听///////////////////////
		btn_register.addActionListener((e)->{
			//TODO
		});
		
		c.add(jl_title);
		c.add(jp1); c.add(jp2);
		setVisible(true);	
	}
}
