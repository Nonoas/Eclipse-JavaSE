package mianJava;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

import mSwingUtils.MyButton;

/**
 * 系统选择界面
 * 
 * @author Nonoas
 */
public class SysChoseJframe extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel jl_title = new JLabel("系统选择", JLabel.CENTER);
	private MyButton btn_tec = new MyButton("教师系统");
	private MyButton btn_stu = new MyButton("学生系统");

	// 构造器
	public SysChoseJframe() {
		setBounds(0, 0, 500, 350);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("学生成绩系统");
		setLocationRelativeTo(null);// 设置居中显示

		Container c = getContentPane();// 获取容器

		jl_title.setBounds(200, 30, 100, 50);
		jl_title.setFont(new Font("黑体", Font.BOLD, 20));
		btn_tec.setBounds(150, 120, 200, 50);
		btn_stu.setBounds(150, 200, 200, 50);

		btn_tec.addActionListener((e) -> {
			new Tec_Login();// 进入教师系统
			dispose();
		});
		btn_stu.addActionListener((e) -> {
			new Stu_Login();
			dispose();
		});

		c.add(jl_title);
		c.add(btn_tec);
		c.add(btn_stu);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SysChoseJframe();
	}
}
