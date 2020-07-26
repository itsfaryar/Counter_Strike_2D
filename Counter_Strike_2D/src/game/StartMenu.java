package game;


import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.GameEnv.Game_Mode;
import game.Player.Directions;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Color;

public class StartMenu extends JFrame {

	private JPanel contentPane;
	public final int PORT=1025;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartMenu frame = new StartMenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartMenu() {
		setBackground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Counter Strike 2D");
		StartMenu self=this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 305, 293);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnStartOfflineMode = new JButton("Offline");
		btnStartOfflineMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OfflineGameFrame og=new OfflineGameFrame(self);
				og.setVisible(true);
			}
		});
		btnStartOfflineMode.setBounds(40, 170, 85, 25);
		contentPane.add(btnStartOfflineMode);
		
		JButton btnStartOnlineMode = new JButton("Online");
		btnStartOnlineMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OnlineGameFrame og=new OnlineGameFrame(self);
				og.setVisible(true);
			}
		});
		btnStartOnlineMode.setBounds(40, 210, 85, 25);
		contentPane.add(btnStartOnlineMode);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(12, 5, 145, 145);
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JLabel loading_image_lbl = new JLabel(resizeIcon(new ImageIcon("Data/Images/Logo.png"), 145,145));
		loading_image_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(loading_image_lbl, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBounds(165, 5, 125, 230);
		contentPane.add(panel_1);
		JLabel gun_image_lbl = new JLabel(resizeIcon(new ImageIcon("Data/Images/Gun.png"), 125,230));
		gun_image_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(gun_image_lbl, BorderLayout.CENTER);
	
		
	}
	public ImageIcon resizeIcon(ImageIcon image ,int w,int h) {
		return new ImageIcon(image.getImage().getScaledInstance(w,h, Image.SCALE_DEFAULT));
	}
}
