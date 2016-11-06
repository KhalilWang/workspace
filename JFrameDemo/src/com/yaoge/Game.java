package com.yaoge;

import java.awt.CardLayout;
import java.awt.Color;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel bottomPane = null; // ��Ҫ��JPanel����JPanel�Ĳ��ֹ��������ó�CardLayout
    private CardLayout card = null; // CardLayout���ֹ�����
    private IndexPanel indexPanel = null;
    private GamePanel gamePanel = null;
    private JPanel p_1 = null, p_2 = null, p_3 = null; // Ҫ�л�������JPanel

    public Game() throws IOException {
        super("Angry Bird");
        /**����һ������ָ����ˮƽ�ʹ�ֱ��϶���¿�Ƭ����*/
        card = new CardLayout(0, 0);
        bottomPane = new JPanel(card); // JPanel�Ĳ��ֹ��������ó�CardLayout
        indexPanel = new IndexPanel();
        gamePanel  = new GamePanel(3);
        p_1 = new JPanel();
        p_2 = new JPanel();
        p_3 = new JPanel();
        bottomPane.setBackground(Color.PINK);
        p_1.setBackground(Color.RED);
        p_2.setBackground(Color.BLUE);
        p_3.setBackground(Color.GREEN);
        p_1.add(new JLabel("JPanel_1"));
        p_2.add(new JLabel("JPanel_2"));
        p_3.add(new JLabel("JPanel_3"));
        bottomPane.add(indexPanel, "indexPanel");
        bottomPane.add(gamePanel, "gamePanel");
        bottomPane.add(p_1, "p1");
        bottomPane.add(p_2, "p2");
        bottomPane.add(p_3, "p3");
        /**�����Ƿ�ת����Ƭ���ֵ�ĳ��������ɲο�API�е��ĵ�*/
//        card.previous(bottomPane);
//        card.next(bottomPane);
//        card.show(bottomPane, "p1");
        
        this.getContentPane().add(bottomPane);
        
        this.setResizable(false); 								// ��ֹ�������
        this.setUndecorated(true);								// ȥ��������
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 	// ���ô���Ĭ�Ϲر��¼�
        this.setSize(gamePanel.getPanelWidth(), gamePanel.getPanelHeight());	// ���ô���Ĵ�С
        this.setLocationRelativeTo(null); 						// �ô������
        this.setVisible(true);
    }
    
    public void changeGamePanel() {
    	card.show(bottomPane, "gamePanel");
    }
    public void changeP1() {
    	card.show(bottomPane, "p1");    	
    }
    public void changeP2() {
    	card.show(bottomPane, "p2");
    }
    public void changeP3() {
    	card.show(bottomPane, "p3");    	
    }

    public void action() throws IOException {
    	String targetPanel = "indexPanel";
    	while (true) {
    		if (targetPanel == "indexPanel") {
    			targetPanel = indexPanel.action();
    		} else if (targetPanel == "gamePanel") {
    			targetPanel = gamePanel.action();
    		} else {
    			targetPanel = "indexPanel";
    			System.out.println("change Panel error");
    		}
    	 	card.show(bottomPane, targetPanel);
    	     		
   			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
}
