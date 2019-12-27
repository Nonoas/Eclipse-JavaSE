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

public class Stu_opJFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private JLabel jl_image=new JLabel(new ImageIcon("asset\\image1.png"),JLabel.CENTER);
	private JPanel jPanel=new JPanel(new BorderLayout(40,10));
	private JPanel firstJPanel=new JPanel();		//���1�����ð�ť
	private JPanel secondJPanel=new JPanel(new GridLayout());		//���2��������ʾ����
	private String username;//�û���������һ���ഫ��ֵ
	private MyButton btn_srarch=new MyButton("�ɼ���ѯ");
	private MyButton btn_information=new MyButton("�˻���Ϣ");
	
	public Stu_opJFrame(String username) {
		this.username=username;//�����û���
		//this.username=username="0001";
		
		setTitle("ѧ��ϵͳ");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setBounds(0, 0, 1200, 850);
		setMinimumSize(new Dimension(1200,850));
		setLocationRelativeTo(null);
		
		Container container=getContentPane();
		// �ɼ���ѯ ��ť�¼�
		btn_srarch.addActionListener((e)->{
			secondJPanel.removeAll();
			secondJPanel.repaint();
			search();
		});
		// �û���Ϣ ��ť�¼�
		btn_information.addActionListener((e)->{
			secondJPanel.removeAll();
			secondJPanel.repaint();
			this.showUserIofo(this.username);
		});
		//��ӵ�һ����������
		jl_image.setPreferredSize(new Dimension(0,200));//����ͼƬ�߶�
		firstJPanel.setPreferredSize(new Dimension(100,0));
		
		firstJPanel.setLayout(new BoxLayout(firstJPanel, BoxLayout.Y_AXIS));
		firstJPanel.add(btn_information);//�û���Ϣ ��ť
		firstJPanel.add(new JLabel(" "));//�û���Ϣ ��ť
		firstJPanel.add(new JLabel(" "));//�û���Ϣ ��ť
		firstJPanel.add(btn_srarch);//�ɼ���ѯ ��ť
		
		jPanel.add(firstJPanel,BorderLayout.WEST);
		jPanel.add(secondJPanel,BorderLayout.CENTER);
		jPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40,40));
		
		this.showUserIofo(this.username);//��ʼ����ʾ�û���Ϣ
		
		secondJPanel.setBorder(BorderFactory.createBevelBorder(1));
		container.add(jPanel,BorderLayout.CENTER);
		container.add(jl_image,BorderLayout.NORTH);
		
		setVisible(true);
	}
	/**
	 * ���ò�ѯ������
	 */
	private void search() {
		secondJPanel.add(new IndividualGradePanel(username));//��ӳɼ���ѯ���
		secondJPanel.revalidate();//���¼�������С�����в���
	}
	/**
	 * չʾ�û���Ϣ
	 * @param username �û���
	 */
	private void showUserIofo(String username) {
		secondJPanel.add(new Stu_InfoPanel(username,this));//����û���Ϣ���
		secondJPanel.revalidate();//���¼�������С�����в���
	}
	/*
	public static void main(String[] args) {
		new Stu_opJFrame("username");
	}
	*/
}
