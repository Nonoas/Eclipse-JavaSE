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
	
	private JLabel jl_ID=new JLabel("ѧ��:");//ѧ��
	private JLabel jl_name=new JLabel("����:");//����
	private JLabel jl_sex=new JLabel("�Ա�:");//�Ա�
	private JLabel jl_age=new JLabel("����:");//����
	private JLabel jl_tel=new JLabel("�绰:");//�绰
	private MyButton btn_logout=new MyButton("�˳���¼");
	private MyButton btn_modify=new MyButton("�޸�����");
	private JTextField jtf_ID=new JTextField();			//��ѧ�š� �ı���
	private JTextField jtf_name=new JTextField();		//�������� �ı���
	private JTextField jtf_sex=new JTextField();		//���Ա� �ı���
	private JTextField jtf_age=new JTextField();		//�����䡱 �ı���
	private JTextField jtf_tel=new JTextField();		//���绰�� �ı���
	private boolean jtf_changeable=false;	//�����ж��û���Ϣ�Ƿ�ɱ༭
	private JPanel jp_center=new JPanel(new GridLayout(6,2,10,10)); //����壺����û���Ϣ
	private JPanel jp_south=new JPanel(new FlowLayout(FlowLayout.CENTER,40,40));// ����壺��Ű�ť
	
	public Stu_InfoPanel(String username,JFrame frame) {
		
		setLayout(new BorderLayout());
		
		Connection connection=new DBOpener().getConnection();
		String sql="select * from stu_info where id ='"+username+"'";//������ѯ��䣬��ѯ��ǰ�û�������Ϣ
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
		on_offTextfield();//������Ϣ�ı��򲻿ɱ༭
		
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
		
		//���޸����ϡ���ť����
		btn_modify.addActionListener((e)->{
			if(!this.jtf_changeable) {
				this.jtf_changeable=!jtf_changeable;
				on_offTextfield();
				btn_modify.setText("�����޸�");			//���û���ʼ��д����ʱ���޸İ�ť����Ϊ�������޸ġ�
			}else {
				if(jtf_name.getText().trim().equals("")||jtf_sex.getText().trim().equals("")||
						jtf_age.getText().trim().equals("")||jtf_tel.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null,"�������Ϊ�գ�");
				}else if(!jtf_sex.getText().trim().equals("��")&&!jtf_sex.getText().trim().equals("Ů")){
					JOptionPane.showMessageDialog(null,"��д���Ա�����");
				}else if(Integer.valueOf(jtf_age.getText().trim())>100){
					JOptionPane.showMessageDialog(null,"��д����������");
				}else if(jtf_tel.getText().trim().length()!=11){
					JOptionPane.showMessageDialog(null,"��д�ĵ绰����");
				}else {
					this.DB_updata(username);		//���޸ĵ��û���Ϣ���浽���ݿ�
					btn_modify.setText("�޸�����");		//���û���������д����ʱ���޸İ�ť����Ϊ���޸����ϡ�
					this.jtf_changeable=!jtf_changeable;
					JOptionPane.showMessageDialog(null,"����ɹ���");
					on_offTextfield();
				}
			}
		});
		// ���˳���¼����ť����
		btn_logout.addActionListener((e)->{
			int select=JOptionPane.showConfirmDialog(null, "ȷ���˳���ǰ�˺ţ�","ע��",
					JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
			if(select==JOptionPane.OK_OPTION) {
				frame.dispose();					//�رյ�ǰ����
				new Stu_Login();						//���´򿪽�ʦ��¼����
			}
		});
		
		add(jp_center,BorderLayout.CENTER);
		add(jp_south,BorderLayout.SOUTH);
	}
	/**
	 * �û������޸Ŀ���
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
	 * ���޸ĵ��û���Ϣ���浽���ݿ�
	 * @param username �û���ID��������
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
