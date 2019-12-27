package indi.nonoas.gradesys.mianJava;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import indi.nonoas.gradesys.mSwingUtils.MyButton;

public class Tec_opJFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel jl_image=new JLabel(new ImageIcon("asset\\image1.png"),JLabel.CENTER);
	private JPanel jPanel=new JPanel(new BorderLayout(40,10));
	private JPanel firstJPanel=new JPanel();		//面板1：放置按钮
	private JPanel secondJPanel=new JPanel(new GridLayout());		//面板2：放置显示内容
	private String username;//用户名，从上一个类传递值
	private MyButton btn_input=new MyButton("成绩导入");
	private MyButton btn_srarch=new MyButton("成绩查询");
	private MyButton btn_information=new MyButton("账户信息");
	//private FlowLayout flowLayout=new FlowLayout(FlowLayout.CENTER,20,20);
	
	public Tec_opJFrame(String username) {
		this.username=username;//传递用户名
		//this.username=username="2222";
		
		setTitle("管理员系统");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setBounds(0, 0, 1200, 850);
		setMinimumSize(new Dimension(1200,850));
		setLocationRelativeTo(null);
		
		Container container=getContentPane();
		//  成绩导入  按钮事件
		btn_input.addActionListener((e)->{
			secondJPanel.removeAll();
			secondJPanel.repaint();
			this.inputGrade();
		});
		// 成绩查询 按钮事件
		btn_srarch.addActionListener((e)->{
			secondJPanel.removeAll();
			secondJPanel.repaint();
			search();
		});
		// 用户信息 按钮事件
		btn_information.addActionListener((e)->{
			secondJPanel.removeAll();
			secondJPanel.repaint();
			this.showUserIofo(this.username);
		});
		//添加第一个面板的内容
		jl_image.setPreferredSize(new Dimension(0,200));//设置图片高度
		firstJPanel.setPreferredSize(new Dimension(100,0));
		
		firstJPanel.setLayout(new BoxLayout(firstJPanel, BoxLayout.Y_AXIS));
		firstJPanel.add(btn_information);//用户信息 按钮
		firstJPanel.add(new JLabel(" "));//用户信息 按钮
		firstJPanel.add(btn_input);//成绩导入 按钮
		firstJPanel.add(new JLabel(" "));//用户信息 按钮
		firstJPanel.add(btn_srarch);//成绩查询 按钮
		
		jPanel.add(firstJPanel,BorderLayout.WEST);
		jPanel.add(secondJPanel,BorderLayout.CENTER);
		jPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40,40));
		
		this.showUserIofo(this.username);//初始化显示用户信息
		
		secondJPanel.setBorder(BorderFactory.createBevelBorder(1));
		container.add(jPanel,BorderLayout.CENTER);
		container.add(jl_image,BorderLayout.NORTH);
		
		setVisible(true);
	}
	private void search() {
		secondJPanel.add(new GradeInquirePanel());//添加成绩查询面板
		secondJPanel.revalidate();//重新计算面板大小并进行布局
	}
	/**
	 * 展示用户信息
	 * @param username 用户名
	 */
	private void showUserIofo(String username) {
		secondJPanel.add(new Tec_InfoPanel(username,this));//添加用户信息面板
		secondJPanel.revalidate();//重新计算面板大小并进行布局
	}
	/**
	 * 导入成绩
	 */
	private void inputGrade() {
		secondJPanel.add(new GradeInputPanel());
		secondJPanel.revalidate();				//重新布局界面
	}
	/*
	public static void main(String[] args) {
		new Tec_opJFrame("username");
	}
	*/
}
