package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.Player.Directions;

public class GameEnv extends JFrame implements KeyListener {
	
	public enum Game_Mode{ONLINE_SERVER,ONLINE_CLIENT,OFFLINE}
	private JMenuBar menu_bar;
	private JMenu admin_options;
	private JButton start_game;
	private JMenuItem map_editor;
	private Icons icons;
	private Map map;
	private Square map_squares[][];
	private JPanel contentPane;
	private JLabel lives_lbl;
	private Player player_in_control;
	private Game_Mode gmode;
	private int game_runtime=0;
	private ServerSocket server_socket;
	private Socket socket;
	private socketRW socket_rw;
	private StartMenu start_menu;
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			if(player_in_control.is_alive()) {
				map.moveTo(player_in_control,Directions.UP);
			}
			break;
		case KeyEvent.VK_S:
			if(player_in_control.is_alive()) {
				map.moveTo(player_in_control,Directions.DOWN);
			}
			break;
		case KeyEvent.VK_D:
			if(player_in_control.is_alive()) {
				map.moveTo(player_in_control,Directions.RIGHT);
			}
			break;
		case KeyEvent.VK_A:
			if(player_in_control.is_alive()) {
				map.moveTo(player_in_control,Directions.LEFT);
			}
			break;
		case KeyEvent.VK_SPACE:
			if(player_in_control.is_alive()) {
				map.fireAtDir(player_in_control);
			}
			break;
		}
	}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	

	/**
	 * Launch the application.
	 */


	public GameEnv(StartMenu start_menu,Map map,int w,int h,Game_Mode gmode,ArrayList<Player>players,Icons icons,Socket socket){
		GameEnv self=this;
		map.setGameEnv(this);
		this.map=map;
		this.start_menu=start_menu;
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		game_runtime=now.toSecondOfDay(); 
		this.gmode=gmode;
		player_in_control=players.get(0);
		
		
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		this.icons=icons;
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		menu_bar=new JMenuBar();
		contentPane.add(menu_bar, BorderLayout.PAGE_START);
		admin_options=new JMenu("Cheet codes");
		menu_bar.add(admin_options);
		start_game=new JButton("Start Game");
		start_game.setFocusable(false);
		start_game.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start_game.setEnabled(false);
				map.setEditorMode(false);
				if(gmode==Game_Mode.OFFLINE) {
					for(int i=0;i<players.size();i++) {
						map.playerFirstSpawn(players.get(i));
						if(players.get(i).getType()==Player.Types.BOT) {
							players.get(i).setMap(map);
							players.get(i).start();
							
						}
						
					
					}
				
				}

				refreshPlayerLives();
				map.start();
				admin_options.setEnabled(false);
			}
		});
		menu_bar.add(start_game);
		map_editor= new JMenuItem("Map Editor");
		admin_options.add(map_editor);
		map_editor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				map.setEditorMode(!map.getEditorMode());
			}
		});
		
		lives_lbl=new JLabel();
		menu_bar.add(lives_lbl);
	
		map_squares=map.getSquares();
		contentPane.add(map.panel);
		
		if(gmode==Game_Mode.ONLINE_CLIENT) {
			map.setSocket(socket);
			self.setTitle("Client");
			map.start();
			refreshPlayerLives();
			//admin_options.setEnabled(false);
			start_game.setEnabled(false);
			start_game.setVisible(false);
		}
		
		else if(gmode==Game_Mode.ONLINE_SERVER) {
			map.setSocket(socket);
			self.setTitle("Server");
			refreshPlayerLives();
			map.start();
			start_game.setEnabled(false);
			start_game.setVisible(false);
			//admin_options.setEnabled(false);
		}

		pack();
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
		       if(map.isAlive()) {
		    	   map.stopRefreshing();
		       }
		       for(int i=0;i<players.size();i++) {
		    	   if(players.get(i).is_alive()) {
		    		   players.get(i).stopRefreshing();
		    		   
		    	   }
		       }
		       start_menu.setVisible(true);
		    }
		});
	}

	public void refreshPlayerLives() {
		if(player_in_control!=null) {
			lives_lbl.setText("  Player Number: "+player_in_control.getNum()+"  "+"Lives: "+player_in_control.getLives());
		}
	}
	public void gameOver(Player winner) {
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		int end=now.toSecondOfDay(); 
		game_runtime=end-game_runtime;
		int sec = game_runtime % 60;
        int hour = game_runtime / 60;
        int min = hour % 60;
        hour = hour / 60;
		MessageBox msg=new MessageBox("Game Runtime: "+hour+":"+min+":"+sec,"Player number "+winner.getNum()+" Won");
		msg.setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		msg.dispose();
		dispose();
	}
	public Game_Mode getGameMode() {
		return gmode;
	}
	

}
