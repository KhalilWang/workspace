package com.yaoge;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class GamePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bgWidth;  				// Panel���
	private int bgHeight;				// Panel�߶�
	private float rate; 				// ͼƬ������
	private BufferedImage background; 	// ����ͼ
	private Bird bird; 					// ��
	private Pig[] pigs; 				// ��
	private int groundHeight;			// ����߶�
	private int pigCount;				// �������
	private Branch branch;  			// ��֦
	boolean running;  			// ��Ϸ������
	boolean isFiring; 			// С���ڷ���״̬
	boolean isPlaying;
	
	public GamePanel(int n) throws IOException {
		rate = 1f;
		pigCount = n;
		running = true;
		isFiring = true;
		isPlaying = false;
		background = ImageIO.read(new File("images/pc/gamebackground.png"));	
		groundHeight = 590; //529;
		
		bgWidth = (int)(background.getWidth()*rate);
		bgHeight = (int)(background.getHeight()*rate);
		branch = new Branch(178, 465, 0.6f);
		bird = new Bird(107, 512, bgWidth, groundHeight);
		pigs = new Pig[pigCount];
		for (int i=0; i<pigCount; ++i) {
			pigs[i] = new Pig(550, 300+80*i);
		}
		
		this.setLayout(null);
		addCloseButton();
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
		//super.paintComponent(g);
		//this.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(new Color(48,22,8)); // ��Ƥ�����ó���辵���ɫ
		int distance = (int)Math.sqrt(
			(bird.getCenterX()-branch.getCenterX())*(bird.getCenterX()-branch.getCenterX()) 
			+(bird.getCenterY()-branch.getCenterY())*(bird.getCenterY()-branch.getCenterY())
		);	// 300 ~ 60
		distance = distance > 300 ? 300 : distance;
		distance = distance < 60  ? 60  : distance;
		g2d.setStroke(new BasicStroke(600 / distance)); // 2 ~ 10
		
		g.drawImage(background, 0, 0, 
				(int)(background.getWidth()*rate), (int)(background.getHeight()*rate), null);
		if (isFiring) {
			g.drawLine(bird.getCenterX(), bird.getCenterY(), branch.getRightBranchNodeX(), branch.getRightBranchNodeY());			
		}
		g.drawImage(branch.getRightBranchImage(), branch.getRightBranchX(), branch.getRightBranchY(), 
				branch.getRightBranchWidth(), branch.getRightBranchHeight(), null);
		g.drawImage(bird.getImage(), bird.getX(), bird.getY(), null);
		if (isFiring) {
			g.drawLine(bird.getCenterX(), bird.getCenterY(), branch.getLeftBranchNodeX(), branch.getLeftBranchNodeY());			
		}
		g.drawImage(branch.getLeftBranchImage(), branch.getLeftBranchX(), branch.getLeftBranchY(), 
				branch.getLeftBranchWidth(), branch.getLeftBranchHeight(), null);
		for (int i=0; i<pigCount; ++i) {
			if (pigs[i].status == Pig.Living) {
				g.drawImage(pigs[i].getImage(), pigs[i].getX(), pigs[i].getY(), null);				
			} else if (pigs[i].status == Pig.Dying) {
				g.drawImage(pigs[i].bubble.getImage(), pigs[i].getBubbleX(), pigs[i].getBubbleY(), null);
			} else if (pigs[i].status == Pig.Dead) {

			}
		}
	}
			
	public int getPanelWidth() {
		return bgWidth;
	}
	public int getPanelHeight() {
		return bgHeight;
	}
	
	public String action() {
		// ����¼���
		MouseMotionListener mml = new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				if (bird.isCatched) {
					// ��������Ƿ����
					int mx = e.getX();
					int my = e.getY();
					int dbb = (int)Math.sqrt(
						(bird.getCenterX()-branch.getCenterX())*(bird.getCenterX()-branch.getCenterX())
						+(bird.getCenterY()-branch.getCenterY())*(bird.getCenterY()-branch.getCenterY())
					);
					int dmb = (int)Math.sqrt(
						(mx-branch.getCenterX())*(mx-branch.getCenterX())
						+(my-branch.getCenterY())*(my-branch.getCenterY())
					);
					if (dmb > 180) { 	// ��������������Ϊdbb=180
						dbb = 180;
						int newX = branch.getCenterX() + (mx-branch.getCenterX())*dbb/dmb;
						int newY = branch.getCenterY() + (my-branch.getCenterY())*dbb/dmb;
						//System.out.println("dmb = "+dmb+", dbb = "+dbb+", ("+newX+", "+newY+")");
						bird.moveTo(newX, newY);
					} else { 			// ����180����������
						bird.moveTo(e.getX(), e.getY());						
					}
					// ���С���Ƿ���ڵ���
					if (bird.getY()+bird.getHeight()>groundHeight) { 	// С����ڵ���,���е���
						int newY = groundHeight - bird.getHeight()/2;
						int dmbrX = e.getX() - branch.getCenterX();
						int dmbrY = e.getY() - branch.getCenterY();
						int dgbrY = groundHeight - branch.getCenterY() - bird.getWidth()/2;
						int newX = branch.getCenterX() + dmbrX * dgbrY / dmbrY;
						bird.moveTo(newX, newY);
					}
				}
			}

			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		MouseListener ml = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
//				running = false;	
				System.out.println(e.getX() + ", " + e.getY());
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if (bird.catchCursor(e.getX(), e.getY())) {
					bird.isCatched = true;
					bird.isMoving = false;
					isFiring = true;
					//ball.moveTo(e.getX(), e.getY());
				}
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (bird.isCatched) {
					bird.setVx((branch.getCenterX() - bird.getCenterX()) / 7f);
					bird.setVy((branch.getCenterY() - bird.getCenterY()) / 7f);
					bird.isCatched = false;
					bird.isMoving = true;
					bird.isStatic = false;
				}
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		/** ����������¼� */
		this.addMouseListener(ml);// ����¼�
		this.addMouseMotionListener(mml);

		// ��������
		File f1;
		URI uri1;
		URL url1; 
		try {      
			f1 = new File("music/penta kill.wav"); 
			uri1 = f1.toURI();
			url1 = uri1.toURL();  //������ַ
			AudioClip aau; 
			aau = Applet.newAudioClip(url1);
			aau.play();
		} catch (Exception e) { 
			e.printStackTrace();
		}

		
		// �����߳�
		bird.start();
		for (int i=0; i<pigCount; ++i) {
			pigs[i].start();
		}
		while (running) {
			if (isFiring && bird.isMoving && bird.getCenterX() > branch.getCenterX()) {
				isFiring = false;
			}
			for (int i=0; i<pigCount; ++i) {
				int d = 0;
				if (pigs[i].status == Pig.Living) {
					d = (int)Math.sqrt(
						(bird.getCenterX()-pigs[i].getCenterX())*(bird.getCenterX()-pigs[i].getCenterX())
						+(bird.getCenterY()-pigs[i].getCenterY())*(bird.getCenterY()-pigs[i].getCenterY())
					);
					if (d < (bird.getWidth()+pigs[i].getWidth())/2) {
						pigs[i].status = Pig.Dying;
					}
				}
			}
			for (int i=0; i<pigCount; ++i) {
				if (pigs[i].status != Pig.Dead) {
					break;
				}
				if (i == pigCount-1 && !isPlaying) {
					File f;
					URI uri;
					URL url; 
					try {      
						f = new File("music/triple kill.wav"); 
						uri = f.toURI();
						url = uri.toURL();  //������ַ
						AudioClip aau; 
						aau = Applet.newAudioClip(url);
						aau.play();
					} catch (Exception e) { 
						e.printStackTrace();
					}
					
					isPlaying = true;

					//return "indexPanel";
				}
			}
			repaint();// ���»��ƽ���
			try {
				Thread.sleep(10); // �߳�����10ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "gamePanel error";
	}
}


