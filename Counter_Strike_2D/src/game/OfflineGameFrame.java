package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.GameEnv.Game_Mode;
import game.Player.Directions;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class OfflineGameFrame extends JFrame {

	private JPanel contentPane;
	private JTextField w_txt;
	private JTextField h_txt;
	private StartMenu start_menu;
	
	public OfflineGameFrame(StartMenu start_menu) {
		this.start_menu=start_menu;
		OfflineGameFrame self=this;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 148);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		w_txt = new JTextField();
		w_txt.setColumns(10);
		w_txt.setBounds(75, 15, 50, 19);
		contentPane.add(w_txt);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(12, 15, 70, 15);
		contentPane.add(lblWidth);
		
		h_txt = new JTextField();
		h_txt.setColumns(10);
		h_txt.setBounds(210, 15, 50, 19);
		contentPane.add(h_txt);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(150, 15, 70, 15);
		contentPane.add(lblHeight);
		
		JButton btnStartGame = new JButton("Start Game");
		btnStartGame.addActionListener(new ActionListener() {
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
					players.add(new Player(icons,Player.Types.ME, Directions.RIGHT,1));
					players.add(new Player(icons,Player.Types.BOT, Directions.RIGHT,2));
					players.add(new Player(icons,Player.Types.BOT, Directions.RIGHT,3));
					players.add(new Player(icons,Player.Types.BOT, Directions.RIGHT,4));
					Map map=new Map(icons,w, h, players);
					
					GameEnv ge=new GameEnv(start_menu,map,w, h, Game_Mode.OFFLINE, players,icons,null);
					start_menu.setVisible(false);
					self.dispose();
				}
			}
		});
		btnStartGame.setBounds(82, 65, 117, 25);
		contentPane.add(btnStartGame);
	}
}
