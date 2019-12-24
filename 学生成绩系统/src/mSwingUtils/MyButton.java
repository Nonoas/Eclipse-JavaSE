package mSwingUtils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
 
import javax.swing.ImageIcon;
import javax.swing.JButton;
 
 
public class MyButton extends JButton {
	    //�ж����serialVersionUID����֤�汾һ���Ե�
	    private static final long serialVersionUID = 39082560987930759L; 
	    
	    //Ĭ����ɫ ���ֽ���ɫ
	    private Color BUTTON_COLOR1= new Color(255, 255, 255);
	    private Color BUTTON_COLOR2 = new Color(222, 222, 222);  
	    
	    //���°�ťʱ�����Ĭ����ɫ
	    private Color BUTTON_FOREGROUND_COLOR1 = new Color(100, 100, 100); 
	    private Color BUTTON_FOREGROUND_COLOR2 = Color.BLACK;
	    //Ĭ������
	    private Font font = new Font("���������", Font.PLAIN, 15);
	    //�ж��Ƿ���
	    private boolean hover;  
	    private float clickTran = 0.4F, exitTran = 1F;
	    //�޸İ��º�͸����
	    public void setClickTran(float tran) {
	    	clickTran = tran;
	    }
	  //�޸İ���ǰ͸����
	    public void setExitTran(float tran) {
	    	exitTran = tran;
	    }
	    //�ϰ�ν�����ɫ
	    public void setBUTTON_COLOR1(Color bUTTON_COLOR1) {
			BUTTON_COLOR1 = bUTTON_COLOR1;
		}
	  //�°�ν�����ɫ
		public void setBUTTON_COLOR2(Color bUTTON_COLOR2) {
			BUTTON_COLOR2 = bUTTON_COLOR2;
		}
		//����ǰ������ɫ
		public void setBUTTON_FOREGROUND_COLOR1(Color bUTTON_FOREGROUND_COLOR1) {
			BUTTON_FOREGROUND_COLOR1 = bUTTON_FOREGROUND_COLOR1;
		}
		//���º�������ɫ
		public void setBUTTON_FOREGROUND_COLOR2(Color bUTTON_FOREGROUND_COLOR2) {
			BUTTON_FOREGROUND_COLOR2 = bUTTON_FOREGROUND_COLOR2;
		}
		
		public MyButton(ImageIcon icon) {
	    	setIcon(icon);
	        Init();
	    }
	    public MyButton(String name) {
	    	setText(name);
	    	Init();
	    }
	    
	    public void Init() {
	    	setFont(font);  
	        setBorderPainted(false);  
	        setForeground(BUTTON_FOREGROUND_COLOR1);  
	        setFocusPainted(false);  
	        setContentAreaFilled(false);  
	        
	        addMouseListener(new MouseAdapter() {  
	            @Override  
	            public void mouseEntered(MouseEvent e) {  //����ƶ�������ʱ
	                setForeground(BUTTON_FOREGROUND_COLOR2);  
	                hover = true;  
	                repaint();  
	            }  
	  
	            @Override  
	            public void mouseExited(MouseEvent e) {  //����ƿ�ʱ
	                setForeground(BUTTON_FOREGROUND_COLOR1);  
	                hover = false;  
	                repaint();  
	            }  
	        });  
	    }
	    
	    @Override  
	    protected void paintComponent(Graphics g) {  
	        Graphics2D g2d = (Graphics2D) g.create();  
	        int h = getHeight();  
	        int w = getWidth();  
	        float tran = clickTran;  
	        if (!hover) {  
	        	tran = exitTran;
	        }  
	  
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
	                RenderingHints.VALUE_ANTIALIAS_ON);  
	        
	        GradientPaint p1;  
	        GradientPaint p2;  
	        
	        if (getModel().isPressed()) {  
	            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,  
	                    new Color(100, 100, 100));  
	            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,  
	                    new Color(255, 255, 255, 100));  
	        } else {  
	            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,  
	                    new Color(0, 0, 0));  
	            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,  
	                    h - 3, new Color(0, 0, 0, 50));  
	        }  
	        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,  
	                tran));  
	        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,  
	                h - 1, 20, 20);  
	        Shape clip = g2d.getClip();  
	        g2d.clip(r2d);  
	        
	        GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F,  
	                h, BUTTON_COLOR2, true);  
	        
	        g2d.setPaint(gp);  
	        g2d.fillRect(0, 0, w, h);  
	        g2d.setClip(clip);  
	        g2d.setPaint(p1);  
	        g2d.drawRoundRect(0, 0, w - 1, h - 1, 20, 20);  
	        g2d.setPaint(p2);  
	        g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);  
	        g2d.dispose();  
	        super.paintComponent(g);  
	    }  
	    
}