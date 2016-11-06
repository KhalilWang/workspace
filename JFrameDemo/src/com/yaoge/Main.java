package com.yaoge;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * ������Ŀ������
 */
public class Main {
	public static void main(String[] args) {

		JFrame frame = new JFrame("�������"); // ����һ������
		frame.setSize(800, 510); // ���ô���Ĵ�С
		frame.setResizable(false); // ��ֹ�������
		frame.setLocationRelativeTo(null); // �ô������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô���Ĭ�Ϲر��¼�
		Pool pool = new Pool(); // ������ض���
		frame.add(pool); // �������ӵ�������
		frame.setVisible(true); // ��ʾ����
		pool.action(); // ������صĿ��Ʒ���
	}
}

/**
 * ������Ŀ�����,��������Ƴ���
 */
class Pool extends JPanel {
	BufferedImage background; // ����ͼ
	Fish[] fishes; // ����һ���������
	Net net; // ����һ������
	int count; // ����
	int score; // �Ʒ�

	/** Pool��Ĺ��췽��, �������������㡢�����и�ֵ */
	public Pool() {
		try {
			background = ImageIO.read(new File("images/background.png")); // ���ر���ͼƬ
			fishes = new Fish[9 + 9 + 9 + 2]; // ����������,����29
			for (int i = 0; i < 9; i++) { // ���������,����������
				fishes[i] = new Fish("fish0" + (i + 1));
				fishes[i + 9] = new Fish("fish0" + (i + 1));
				fishes[i + 9 + 9] = new Fish("fish0" + (i + 1));
			}
			fishes[fishes.length - 2] = new Fish("fish13");
			fishes[fishes.length - 1] = new Fish("fish14");
			net = new Net(); // ������������

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��ͼ����,ϵͳ�Զ����� */
	public void paint(Graphics g) {

		g.drawImage(background, 0, 0, null); // ���Ʊ���ͼƬ
		for (int i = 0; i < fishes.length; i++) { // �������������е���
			g.drawImage(fishes[i].image, fishes[i].x, fishes[i].y, null);
		}

		/** ͨ���������ĵ�x,y������������λ�ã������������Ƴ��� */
		if (net.show) {
			int x = net.x - net.width / 2;
			int y = net.y - net.height / 2;
			g.drawImage(net.image, x, y, null);
		}

		/** ���Ʋ���������ͷ��� */
		Font f = new Font("", Font.BOLD, 30);
		g.setFont(f);
		g.setColor(Color.WHITE);
		g.drawString("count:" + count, 20, 30);
		g.drawString("score:" + score, 20, 60);
	}

	/** ��Pool�������Ӳ��㷽�� */
	public void catchFish() {
		for (int i = fishes.length - 1; i >= 0; i--) {
			/** �ж����Ƿ񱻲��� */
			if (net.catchThe(fishes[i])) {
				count++; // ����
				if (fishes[i].width > 100) { // �Ʒ�
					score += 15;
				} else {
					score += fishes[i].width / 10;
				}
				fishes[i].catched = true; // ������catched=true;
				break; // �˳�ѭ��
			}
		}
	}

	/** �������ζ��ķ��� */
	public void action() {
		// ����¼���
		MouseAdapter ma = new MouseAdapter() {
			// ��갴��
			@Override
			public void mousePressed(MouseEvent e) {
				catchFish();// ��Pool�����Ӳ��㷽��
			}

			// ������
			@Override
			public void mouseEntered(MouseEvent e) {
				net.show = true;
			}

			// ����Ƴ�
			@Override
			public void mouseExited(MouseEvent e) {
				net.show = false;
			}

			// ����ƶ�
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				net.moveTo(x, y);
			}
		};
		
		/** ����������¼� */
		this.addMouseListener(ma);// ����¼�
		this.addMouseMotionListener(ma);// �϶��¼�
		// �����߳�
		for (int i = 0; i < fishes.length; i++) {
			fishes[i].start();
		}
		while (true) {
			repaint();// ���»��ƽ���
			try {
				Thread.sleep(20); // �߳�����20ms
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * �������
 */
class Fish extends Thread { // ������Ϊһ�������߳�
	BufferedImage[] images; // ��ͼƬ������
	BufferedImage image; // ����ʾ�ĵ�ǰͼƬ
	int width; // ��ͼƬ�Ŀ��
	int height; // ��ͼƬ�ĸ߶�
	int x; // ���x����
	int y; // ���y����
	int step; // ���ζ����ٶ�
	int index; // ��ͼƬ��ת
	boolean catched; // ���Ƿ񱻲���
	BufferedImage catch01;
	BufferedImage catch02;

	/** Fish��Ĺ��췽�������������Ը�ֵ */
	public Fish(String name) throws Exception {
		// ������ͼƬ����
		images = new BufferedImage[10];
		// ����ÿ�����10��ͼƬ������������
		for (int i = 0; i < 10; i++) {
			String file = "images/" + name + "_0" + i + ".png";
			images[i] = ImageIO.read(new File(file));
		}
		image = images[0]; // ������ĵ�һ��ͼƬ������ǰͼƬ
		width = image.getWidth(); // ��ȡ��ǰͼƬ�Ŀ��
		height = image.getHeight(); // ��ȡ��ǰͼƬ�ĸ߶�
		Random r = new Random();
		x = r.nextInt(800 - width); // �����x���긳ֵ
		y = r.nextInt(480 - height); // �����y���긳ֵ
		step = r.nextInt(5) + 1; // ��ʼ������ٶ�(1~5)
		index = 0; // ͼƬ��ת�ĳ�ʼֵΪ0
		catched = false;
		catch01 = ImageIO.read(new File("images/" + name + "_catch_01.png"));
		catch02 = ImageIO.read(new File("images/" + name + "_catch_02.png"));
	}

	/** ���ζ��ķ��� */
	public void move() {
		index++; // ���ζ�ʱ10��ͼƬ��ת,�ﵽ�ζ�Ч��
		image = images[index % images.length];
		x -= step; // ������������ζ�
		if (x < -width) { // �����γ�����,���´��ұ߽���
			x = 800;
			Random r = new Random();
			y = r.nextInt(480 - height);
			step = r.nextInt(5) + 1;
		}
	}

	// �̷߳�����ϵͳ�Զ�����
	public void run() {
		while (true) {
			try {
				// ����㱻����
				if (catched) {
					turnOver(); // 1��ԭ�ط���
					catched = false; // 2��catched=false;
					getOut(); // 3������ʧ
				} else {
					move(); // ����������ζ�
					Thread.sleep(100);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/** ��ԭ�ط��ڵķ��� */
	public void turnOver() {
		try {
			for (int i = 0; i < 5; i++) {
				image = catch01;
				Thread.sleep(100);
				image = catch02;
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** ��������� */
	public void getOut() {
		x = 800;
		Random r = new Random();
		y = r.nextInt(480 - height);
		step = r.nextInt(5) + 1;
	}
}

class Net {
	BufferedImage image; // ������ͼƬ
	int width; // �����Ŀ��
	int height; // �����ĸ߶�
	int x; // ���������ĵ�x����
	int y; // ���������ĵ�y����
	boolean show; // �����Ƿ���ʾ

	/** Net�๹�췽��,�������������Ը�ֵ */
	public Net() throws Exception {
		image = ImageIO.read( // ��������ͼƬ
				new File("images/net09.png"));
		width = image.getWidth(); // ��ȡͼƬ�Ŀ��
		height = image.getHeight(); // ��ȡͼƬ�ĸ߶�
		Random r = new Random();
		x = r.nextInt(800 - width); // ��������һ�γ���ʱ��x����
		y = r.nextInt(480 - height); // ��������һ�γ���ʱ��y����
		show = true; // ����һ��ʼ������������ʾ״̬
	}

	/** �����ƶ��ķ��� ��������λ�õ�x,y ��ֵ��������x,y���� */
	public void moveTo(int x, int y) {
		this.x = x; // this.x��ʾ����������,x��ʾ��������λ��x����
		this.y = y; // this.y��ʾ����������,y��ʾ��������λ��y����
	}

	/** �ж����Ƿ񱻲��� �ж��������ĵ��Ƿ�����ͼƬ֮�� ���������ͷ���true ���򷵻�false */
	public boolean catchThe(Fish fish) {
		int dx = x - fish.x;
		int dy = y - fish.y;
		return dx > 0 && dx < fish.width && dy > 0 && dy < fish.height;
	}
}