package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.Player.Directions;
import game.Player.Modes;

public class GameEnv extends JFrame implements KeyListener {
	public enum Game_Mode{ONLINE,OFFLINE}
	private JMenuBar menu_bar;
	private JMenu admin_options;
	private JMenuItem map_editor;
	private Icons icons;
	private Map map;
	private Square map_squares[][];
	private JPanel contentPane;
	private Player player_in_control;
	public void keyPressed(KeyEvent e) {
		
		if (e.getKeyCode() == KeyEvent.VK_W) {
			if(player_in_control!=null) {
				map.moveTo(player_in_control,Directions.UP);
			}
		}
		     
		else if (e.getKeyCode() == KeyEvent.VK_S) {
			if(player_in_control!=null) {
				map.moveTo(player_in_control,Directions.DOWN);
			}
		}
		      
		else if (e.getKeyCode() == KeyEvent.VK_D) {
			if(player_in_control!=null) {
				map.moveTo(player_in_control,Directions.RIGHT);
			}
		}
		     
		else if (e.getKeyCode() == KeyEvent.VK_A) {
			if(player_in_control!=null) {
				map.moveTo(player_in_control,Directions.LEFT);
			}
		}
	}

		  public void keyReleased(KeyEvent e) {
	

		  }

		  public void keyTyped(KeyEvent e) {
		   
		  }


	/**
	 * Launch the application.
	 */


	public GameEnv(int w,int h,Game_Mode gmode,ArrayList<Player>players,Icons icons){
		player_in_control=players.get(0);
		map=new Map(icons,w, h, players);
		map_squares=map.getSquares();
		for(int i=0;i<players.size();i++) {
			map.playerFirstSpawn(players.get(i));
			if(players.get(i).getMode()==Modes.BOT||players.get(i).getMode()==Modes.ONLINEPLAYER)players.get(i).start();
		}
		map.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.icons=icons;
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		contentPane.add(map.panel);
		menu_bar=new JMenuBar();
		contentPane.add(menu_bar, BorderLayout.PAGE_START);
		admin_options=new JMenu("Admin Options");
		menu_bar.add(admin_options);
		map_editor= new JMenuItem("Map Editor");
		admin_options.add(map_editor);
		map_editor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				map.setEditorMode(!map.getEditorMode());
			}
		});
		pack();
	}

}
