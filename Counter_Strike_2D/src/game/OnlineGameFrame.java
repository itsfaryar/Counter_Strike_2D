package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.GameEnv.Game_Mode;
import game.Player.Directions;
import game.Square.Materials;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class OnlineGameFrame extends JFrame {

	private JPanel contentPane;
	private JTextField w_text;
	private JTextField h_text;
	private JTextField textField_2;
	private JLabel alert_lbl;
	private JLabel client_alert_lbl;
	private StartMenu start_menu;
	private socketRW socket_rw;
	private Map map;
	/**
	 * Create the frame.
	 */
	public OnlineGameFrame(StartMenu start_menu) {
		OnlineGameFrame self=this;
		this.start_menu=start_menu;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 266, 213);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		tabbedPane.setBounds(5, 5, 249, 166);
		contentPane.add(tabbedPane);
		
		JLayeredPane server_pane = new JLayeredPane();
		tabbedPane.addTab("Server", null, server_pane, null);
		server_pane.setLayout(null);
		
		JButton btnStartServer = new JButton("Start server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				alert_lbl.setText("Waiting For Client");
				int w=0,h=0;
				try {
					w=Integer.parseInt(w_text.getText());
					h=Integer.parseInt(h_text.getText());
				} catch (Exception e) {
				}
				if(w>0&&h>0) {
					socket_rw=new socketRW(null,start_menu.PORT);
					server_trasfer cr=new server_trasfer(self,socket_rw,w,h);
					cr.start();
				}
				
			
			}
		});
		btnStartServer.setBounds(54, 65, 148, 25);
		server_pane.add(btnStartServer);
		
		w_text = new JTextField();
		w_text.setColumns(10);
		w_text.setBounds(65, 12, 47, 19);
		server_pane.add(w_text);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setBounds(15, 14, 70, 15);
		server_pane.add(lblWidth);
		
		h_text = new JTextField();
		h_text.setColumns(10);
		h_text.setBounds(185, 12, 47, 19);
		server_pane.add(h_text);
		
		JLabel lblHeight = new JLabel("height");
		lblHeight.setBounds(132, 14, 70, 15);
		server_pane.add(lblHeight);
		
		alert_lbl = new JLabel("");
		alert_lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
		alert_lbl.setForeground(Color.RED);
		alert_lbl.setBounds(12, 102, 224, 15);
		server_pane.add(alert_lbl);
		
		JLayeredPane client_pane = new JLayeredPane();
		tabbedPane.addTab("Client", null, client_pane, null);
		
		textField_2 = new JTextField();
		textField_2.setText("127.0.0.1");
		textField_2.setBounds(107, 12, 114, 19);
		client_pane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblServersIp = new JLabel("Server's IP");
		lblServersIp.setBounds(12, 14, 91, 15);
		client_pane.add(lblServersIp);
		
		JButton btnConnectToServer = new JButton("Connect To Server");
		btnConnectToServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				client_alert_lbl.setText("Connecting to server...");
				String ip=textField_2.getText();
				socket_rw=new socketRW(ip,start_menu.PORT);
				client_recive cr=new client_recive(self,socket_rw);
				cr.start();
					
			}
		});
		btnConnectToServer.setBounds(37, 57, 170, 25);
		client_pane.add(btnConnectToServer);
		
		client_alert_lbl = new JLabel("");
		client_alert_lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
		client_alert_lbl.setForeground(Color.RED);
		client_alert_lbl.setBounds(12, 94, 237, 15);
		client_pane.add(client_alert_lbl);
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	private void startGame() {
		alert_lbl.setText("Done!");
		client_alert_lbl.setText("Done!");
		start_menu.setVisible(false);
		this.dispose();
	}
	
	private static class server_trasfer extends Thread {
		private ServerSocket server_skt;
		private Socket skt;
		private ObjectInputStream inp_obj;
		private ObjectOutputStream out_obj;
		private DataInputStream inp;
		private DataOutputStream out;
		private socketRW s_rw;
		private int w,h;
		private Map map;
		private OnlineGameFrame ogf;

		// set socket
		public server_trasfer(OnlineGameFrame ogf,socketRW s_rw,int w,int h) {
			this.map=map;
			this.s_rw=s_rw;
			this.w=w;
			this.h=h;
			this.ogf=ogf;
		}

		public void run() {
			
			Icons icons=new  Icons(w, h);
			ArrayList<Player>players=new ArrayList<Player>();
			players.add(new Player(icons,Player.Types.PL_SERVER, Directions.RIGHT,1));
			players.add(new Player(icons,Player.Types.PL_CLIENT, Directions.RIGHT,2));
			try {
				server_skt=new ServerSocket(s_rw.port);
				skt=server_skt.accept();
				inp = new DataInputStream(skt.getInputStream());
				out=new DataOutputStream(skt.getOutputStream());
				
			} catch (IOException e) {}
			if(skt!=null) {
				if(skt.isConnected()) {
				ogf.alert_lbl.setText("Client Connected.");
					try {
						out.write(w);
						out.write(h);
						
						map=new Map(icons, w, h, players);
						map.playerFirstSpawn(players.get(0));
						map.playerFirstSpawn(players.get(1));
						out.writeChar('s');
						out.write(players.get(0).getPos().i);
						out.write(players.get(0).getPos().j);
						out.writeChar('c');
						out.write(players.get(1).getPos().i);
						out.write(players.get(1).getPos().j);
						out.writeChar('m');
						Square[][]map_squares=map.getSquares();
						for (int i = 0; i < map_squares.length; i++) {
				            for (int j = 0; j < map_squares[i].length; j++) {//0forEmpty-1forNotAvailble-2ForWall
				         
				            	if(map_squares[i][j].getMaterial()==Materials.Empty) {
				            		out.write(0);
				            	}
				            	else if(map_squares[i][j].getMaterial()==Materials.NOTAVAILBLE) {
				            		out.write(1);
				            	}
				            	else {
				            		out.write(2);
				            	}
				            	
				            }
				        }
						GameEnv ge=new GameEnv(ogf.start_menu,map,w, h, Game_Mode.ONLINE_SERVER, players,icons,skt);
						ogf.startGame();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	private static class client_recive extends Thread {

		private Socket skt;

		private DataInputStream inp;
		private DataOutputStream out;
		private Map map;
		private socketRW s_rw;
		private InputStream inp_stream;
		private OnlineGameFrame ogf;
		// set socket
		public client_recive(OnlineGameFrame ogf,socketRW s_rw) {
			this.s_rw=s_rw;
			this.ogf=ogf;
		}

		public void run() {
			int w,h;
			int c=0;
			boolean connected=false;
			while(c<1000) {
				try {
					skt = new Socket(s_rw.ip, s_rw.port);
					inp_stream=skt.getInputStream();
					inp = new DataInputStream(inp_stream);
					out=new DataOutputStream(skt.getOutputStream());
				
					if(skt.isConnected()) {
						connected=true;
						ogf.client_alert_lbl.setText("Downloading Map/Data...");
						w=inp.read();
						h=inp.read();
						Icons icons=new  Icons(w, h);
						ArrayList<Player>players=new ArrayList<Player>();
						players.add(new Player(icons,Player.Types.PL_CLIENT, Directions.RIGHT,2));
						players.add(new Player(icons,Player.Types.PL_SERVER, Directions.RIGHT,1));
						if(inp.readChar()=='s') {
							int i=inp.read();
							int j=inp.read();
							players.get(1).setPos(new Position(i, j));
						}
						if(inp.readChar()=='c') {
							int i=inp.read();
							int j=inp.read();
							players.get(0).setPos(new Position(i, j));
						}
						map=new Map(icons, w, h, players);
						if(inp.readChar()=='m') {
							Square[][]map_squares=map.getSquares();
							for (int i = 0; i < map_squares.length; i++) {
					            for (int j = 0; j < map_squares[i].length; j++) {//0forEmpty-1forNotAvailble-2ForWall
					            	int mode=inp.read();
					            	if(mode==0) {
					            		map_squares[i][j].setMaterial(Materials.Empty);
					            	}
					            	else if(mode==1) {
					            		map_squares[i][j].setMaterial(Materials.NOTAVAILBLE);
					            	}
					            	else {
					            		map_squares[i][j].setMaterial(Materials.WALL);
					            	}
					            }
					        }
						}
						GameEnv ge=new GameEnv(ogf.start_menu,map,w, h, Game_Mode.ONLINE_CLIENT, players,icons,skt);
						ogf.startGame();
						break;
					}
				} catch (IOException e) {}
				
				c++;
			}
			if(!connected)infoBox("Server is not running on this IP.", "connection problem");
			
	}
		 public static void infoBox(String infoMessage, String titleBar)
		    {
		        JOptionPane.showMessageDialog(null, infoMessage,  titleBar, JOptionPane.INFORMATION_MESSAGE);
		    }
	}
}
