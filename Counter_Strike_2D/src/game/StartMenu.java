package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.GameEnv.Game_Mode;
import game.Player.Directions;
import game.Player.Modes;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class StartMenu extends JFrame {

	private JPanel contentPane;
	private JTextField w_txt;
	private JTextField h_txt;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 321, 192);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		w_txt = new JTextField();
		w_txt.setBounds(75, 12, 60, 19);
		contentPane.add(w_txt);
		w_txt.setColumns(10);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(12, 14, 70, 15);
		contentPane.add(lblWidth);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(180, 12, 70, 15);
		contentPane.add(lblHeight);
		
		h_txt = new JTextField();
		h_txt.setBounds(240, 12, 60, 19);
		contentPane.add(h_txt);
		h_txt.setColumns(10);
		
		JButton btnStartOfflineMode = new JButton("Start Offline Mode");
		btnStartOfflineMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int w=0,h=0;
				try {
					w=Integer.parseInt(w_txt.getText());
					h=Integer.parseInt(h_txt.getText());
				} catch (Exception e) {
				}
				if(w>0&&h>0) {
					Icons icons=new  Icons(w, h);
					ArrayList<Player>players=new ArrayList<Player>();
					players.add(new Player(icons, Modes.ME, Directions.RIGHT));
					GameEnv ge=new GameEnv(w, h, Game_Mode.OFFLINE, players,icons);
				}
			}
		});
		btnStartOfflineMode.setBounds(51, 43, 203, 25);
		contentPane.add(btnStartOfflineMode);
	}
}
