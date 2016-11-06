package com.yaoge;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MyButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int Play = 0;
	private static final int[][] pos = {
		{10, 2, 309, 200},
		{851, 930, 46, 44},
		{41, 826, 114, 110},
		{41, 591, 125, 120},
		{41, 466, 126, 123},
		{449, 156, 127, 121}
	};
	
	private BufferedImage originImage;
	private BufferedImage image;
	private int width;
	private int height;
	
	public MyButton(int n) throws IOException {
		originImage = ImageIO.read(new File("images/pc/BUTTONS_SHEET_2.png"));
		image = originImage.getSubimage(pos[n][0], pos[n][1], pos[n][2], pos[n][3]);
		
		width = image.getWidth();
		height = image.getHeight();
		
		//this.setBackground(new Color(0,0,0)); //��Ӱ��ɫ���������ֵ,ֻ��ռλ���á� 
		//this.setOpaque(false);   					//���ñ���͸��  
		this.setBorderPainted(false);  				// ȡ���߿�
		this.setContentAreaFilled(false); //���ð�ť͸��
		this.setIcon(new ImageIcon(image));  // ���ð�ťͼs	
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
