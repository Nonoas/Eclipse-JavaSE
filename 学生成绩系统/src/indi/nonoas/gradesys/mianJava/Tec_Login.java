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
	private JLabel jl_title=new JLabel("��   ¼",JLabel.CENTER);
	private JLabel jl_username=new JLabel("�û���",null,JLabel.RIGHT);
	private JLabel jl_password=new JLabel("��   ��",null,JLabel.RIGHT);
	private JTextField jt_username=new JTextField(50);//�û�����
	private JPasswordField jpw_password=new JPasswordField(50);//�����
	private MyButton btn_login=new MyButton("��¼");
	private MyButton btn_back=new MyButton("����");
	private MyButton btn_register=new MyButton("ע��");
	private JPanel jp1=new JPanel(new GridLayout(2,2,20,25));
	private JPanel jp2=new JPanel(new FlowLayout(FlowLayout.CENTER,40,30));
	
	public Tec_Login() {
		setTitle("����Աϵͳ");
		setBounds(0, 0,500,350);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(3,1));
		setLocationRelativeTo(null);//ʹ���ھ�����ʾ����Ļ
		Container c=getContentPane();
		
		jl_title.setBounds(200, 30, 100, 50);
		jl_title.setFont(new Font("����",Font.BOLD,20));
		
		jp1.add(jl_username); 
		jp1.add(jt_username);//����û�����ǩ�������
		jp1.add(jl_password); 
		jp1.add(jpw_password);//������뼰�����ǩ
		jp1.setBorder(new EmptyBorder(0, -100, 0, 100));
		
		jp2.add(btn_login);
		btn_back.setSize(40, 10);
		jp2.add(btn_back);
		btn_register.setForeground(Color.BLUE);//����ע���ǩΪ��ɫ
		jp2.add(btn_register);
		/////////////��¼��ť����////////////////
		btn_login.addActionListener((e)->{
			String str_name=jt_username.getText().trim();//��ȡ�û�������û���
			char[] str_password=jpw_password.getPassword();//��ȡ�û����������
			if(str_name.equals("")) {             			//�ж� �û��� �Ƿ�Ϊ��
				JOptionPane.showMessageDialog(null, "�û�������Ϊ�գ�","��¼�쳣",JOptionPane.INFORMATION_MESSAGE);
			}else if(str_password==null||str_password.length==0) {//�ж� ���� �Ƿ�Ϊ��
				JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�","��¼�쳣",JOptionPane.INFORMATION_MESSAGE);
			}else {										//�������ݿ���֤��Ϣ
				Connection conn=new DBOpener().getConnection();
				try {
					PreparedStatement pStatement=conn.prepareStatement("select password from user_password "
							+ "where id=? and tec_root=1");//ʹ��PrepareStatement������Ч����sqlע��
					pStatement.setString(1, str_name);
					ResultSet rSet=pStatement.executeQuery();
					String password=new String();
					while(rSet.next()) {
						password=rSet.getString("password");
					}
					if(password.equals(new String(str_password))) {
						dispose();
						new Tec_opJFrame(str_name);//�������Աϵͳ����
						System.out.println("��¼�ɹ�");
					}else {
						JOptionPane.showMessageDialog(null, "�û������������","��¼�쳣",JOptionPane.INFORMATION_MESSAGE);
						jt_username.setText("");//����û�����
						jpw_password.setText("");//���������
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		///////////////���ذ�ť����///////////////////
		btn_back.addActionListener((e)->{
			dispose(); new SysChoseJframe();//�رյ�ǰ���ڣ���ϵͳѡ�񴰿�
		});
		//////////////ע�ᰴť����///////////////////////
		btn_register.addActionListener((e)->{
			//TODO
		});
		
		c.add(jl_title);
		c.add(jp1); c.add(jp2);
		setVisible(true);	
	}
}
