package com.yaoge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class IndexPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage background; 	// ����ͼ
	private boolean running;			// ��������
	private int bgWidth;  				// Panel���
	private int bgHeight;				// Panel�߶�
	private float rate; 				// ͼƬ������
	
	public IndexPanel() throws IOException {
		running = true;
		rate = 1f;
		background = ImageIO.read(new File("images/pc/indexbackground.png"));
		bgWidth = background.getWidth();
		bgHeight = background.getHeight();
		
		this.setLayout(null);
		addLoginButton();
		addCloseButton();
	}
	
	protected void addLoginButton() throws IOException {
		MyButton myButton = new MyButton(MyButton.Play);
		myButton.setBounds((bgWidth-myButton.getWidth())/2, bgHeight-myButton.getHeight(), myButton.getWidth(), myButton.getHeight());
		this.add(myButton);
		myButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	running = false;
		    }
		});
	}
	
	protected void addCloseButton() {
		JButton closeButton;
		closeButton = new JButton();
		closeButton.setBackground(new Color(123,123,123));  //��Ӱ��ɫ���������ֵ,ֻ��ռλ���á� 
		closeButton.setOpaque(false);   					//���ñ���͸��  
		closeButton.setBorderPainted(false);  				// ȡ���߿�
		closeButton.setIcon(new ImageIcon("images/BlueMidBall.png"));  // ���ð�ťͼƬ
		this.add(closeButton);
  		closeButton.setBounds(bgWidth-closeButton.getIcon().getIconWidth(), 0, closeButton.getIcon().getIconWidth(), closeButton.getIcon().getIconHeight());
		closeButton.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {
		    	 System.exit(0);
		     }
		});
	}

	
	protected void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, (int)(background.getWidth()*rate), (int)(background.getHeight()*rate), null);
		//g.drawImage(myButton.getImage(), (bgWidth-myButton.getWidth())/2, bgHeight-myButton.getHeight(), null);
	}
	
	public int getPanelWidth() {
		return bgWidth;
	}
	
	public int getPanelHeight() {
		return bgHeight;
	}
	
	public String action() {
		MouseMotionListener mml = new MouseMotionListener() {

			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		MouseListener ml = new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				//running = false;
			}

			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		
		this.addMouseMotionListener(mml);
		this.addMouseListener(ml);
		
		running = true;
		
		while (running) {
			repaint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "gamePanel";
	}
}
