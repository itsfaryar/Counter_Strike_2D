package game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import game.GameEnv.Game_Mode;
import game.Player.Directions;
import game.Player.Types;
import game.Square.Materials;

public class Map extends Thread implements ActionListener{

	/**
	 * Create the panel.
	 */
	public JPanel panel;
	private boolean editor_mode=false;
	private Square[][] map_squares;
	private int w,h;
	private Icons icons;
	private ArrayList<Player>players;
	private GameEnv ge;
	private boolean runningTread=true;
	private int startTime_for_heart=0;
	private Socket socket;
	public Map(Icons icons,int w,int h,ArrayList<Player>players) {
		
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		startTime_for_heart=now.toSecondOfDay(); 
	
		this.players=players;
		panel=new JPanel();
		this.w=w;
		this.h=h;
		this.icons=icons;
		map_squares= new Square[h+2][w+2];
		panel.setLayout(new GridLayout(h+2,w+2));
		initBoard();
		
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
		recData rd=new recData(this);
		rd.start();
		
		sendPlPos sd=new sendPlPos(this);
		sd.start();
	}
	public void setGameEnv(GameEnv ge) {
		this.ge=ge;
	}
	public void run() {
		Player lastStandng=null;
		int alive_counter=0;
		while(runningTread) {
			if(ge.getGameMode()==Game_Mode.OFFLINE) {
				LocalTime now = LocalTime.now(ZoneId.systemDefault());
				int t=now.toSecondOfDay(); 
				if(t-startTime_for_heart>=20) {
					Position pos=getFreePos();
					getSquareAt(pos).putHealthObj();
					startTime_for_heart=t;
					
				}
				ge.refreshPlayerLives();
				removeAllPlayers();
				lastStandng=null;
				alive_counter=0;
				for(int i=0;i<players.size();i++) {
					
					Position pos=players.get(i).getPos();
					
					if(pos!=null) {
						if(players.get(i).is_alive()) {
							alive_counter++;
							lastStandng=players.get(i);
							map_squares[pos.i][pos.j].setPlayer(players.get(i));
							//map_squares[pos.i][pos.j].setText(players.get(i).getLives()+"");
						}
					}
					
				}
				if(alive_counter<=1) {
					ge.gameOver(lastStandng);
					break;
				}
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			else {
				if(ge.getGameMode()==Game_Mode.ONLINE_SERVER) {
					LocalTime now = LocalTime.now(ZoneId.systemDefault());
					int t=now.toSecondOfDay(); 
					if(t-startTime_for_heart>=20) {
						Position pos=getFreePos();
						sendData sd=new sendData(this, "HP ; "+pos.i+" ; "+pos.j);
						sd.start();
						getSquareAt(pos).putHealthObj();
						startTime_for_heart=t;
					}
				}
				ge.refreshPlayerLives();
				removeAllPlayers();
				lastStandng=null;
				alive_counter=0;
				for(int i=0;i<players.size();i++) {
					
					Position pos=players.get(i).getPos();
					
					if(pos!=null) {
						if(players.get(i).is_alive()) {
							alive_counter++;
							lastStandng=players.get(i);
							map_squares[pos.i][pos.j].setPlayer(players.get(i));
							//map_squares[pos.i][pos.j].setText(players.get(i).getLives()+"");
						}
						else {
							LocalTime now = LocalTime.now(ZoneId.systemDefault());
							int t=now.toSecondOfDay(); 
							if(t-players.get(i).getTimeOfDeath()>=5) {
								Player pl=players.get(i);
								pl.revive();
								playerFirstSpawn(players.get(i));
								sendData sd=new sendData(this, "REVIVE ; "+pl.getNum());
								sd.start();
							}
						}
					}
					
				}
				
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			
		}
	}
	
	public void stopRefreshing() {
		runningTread=false;
	}
	public void initBoard() {
		
		for (int i = 0; i < map_squares.length; i++) {
            for (int j = 0; j < map_squares[i].length; j++) {
            	Square b;
            	if(i==0 || i==h+1 || j==0 || j==w+1) {
            		b = new Square(icons,w,h,Square.Materials.NOTAVAILBLE);
            		b.setEnabled(false);
            	}
            	else{
            		b = new Square(icons,w,h);
            	}
                b.addActionListener(this);
                b.setFocusable(false);
                panel.add(b);
                map_squares[i][j]=b;

            }
        }
		
	}
	public void actionPerformed(ActionEvent e) { 
		Square sqr=(Square) e.getSource();
		if(editor_mode) {
			if(sqr.getMaterial()==Materials.Empty)sqr.setMaterial(Materials.WALL);
			else if(sqr.getMaterial()==Materials.WALL)sqr.setMaterial(Materials.Empty);
		}
	}
	public void setEditorMode(boolean b) {
		this.editor_mode=b;
	}
	public boolean getEditorMode() {
		return editor_mode;
	}
	public Square[][] getSquares() {
		return map_squares;
	}
	public Icons getIcons() {
		return icons;
	}
	public void playerFirstSpawn(Player pl) {
		Random rnd=new Random();
		int i,j;
		while(true) {
			i=rnd.nextInt(h)+1;
			j=rnd.nextInt(w)+1;
			if(map_squares[i][j].is_availble()) {
				break;
			}
			
		}
		map_squares[i][j].setPlayer(pl);
		pl.setPos(new Position(i, j));
	}
	public Position getFreePos() {
		Random rnd=new Random();
		int i,j;

		while(true) {
			i=rnd.nextInt(h)+1;
			j=rnd.nextInt(w)+1;
			if(map_squares[i][j].is_availble()) {
				break;
			}
			
		}
		return new Position(i,j);
	}
	public Square getSquareAt(Position pos) {
		return map_squares[pos.i][pos.j];
	}
	public boolean checkPosTo(Position pos) {
		Square sqr=getSquareAt(pos);
		if(!sqr.is_availble())return false;
		boolean out=true;
		
		for(int i=0;i<players.size();i++) {
			Player pl=players.get(i);
			if(pl!=null) {
				Position pl_pos=pl.getPos();
				if(pl_pos!=null) {
					if(pl_pos.equals(pos)&& pl.is_alive()) {
						out=false;
						break;
					}
				}
				
			}
		}
		return out;
	}
	public boolean moveTo(Player pl,Directions dir) {
		boolean out=false;
		pl.setDirection(dir);
		Position pos=pl.getPosAtDir(dir);
		if(checkPosTo(pos)) {
			pl.setPos(pos);
			out=true;
				
		}
		return out;
	}
	public boolean fireAtDir(Player pl) {
		boolean out=false;
		
		if(pl.is_gunAvailble()) {
			Directions dir=pl.getDirection();
			Position bullet_pos=new Position(pl.getPos().i, pl.getPos().j);
			
			if(dir==Directions.UP) {
				Square sqr;
				
				while(true) {
					bullet_pos.i--;
					sqr=getSquareAt(bullet_pos);
					if(!sqr.is_availble())break;
				}
				Player pl_target=sqr.getPlayer();
				if(pl_target!=null) {
					out=true;
					pl_target.hitByBullet();
					ge.refreshPlayerLives();
					if(pl_target.getType()==Types.PL_CLIENT||pl_target.getType()==Types.PL_SERVER) {
						sendData sd=new sendData(this,"GUNSHOT ; ");
						sd.start();
					}
				}
			}
			else if(dir==Directions.DOWN) {
				Square sqr;
				
				while(true) {
					bullet_pos.i++;
					sqr=getSquareAt(bullet_pos);
					if(!sqr.is_availble())break;
				}
				Player pl_target=sqr.getPlayer();
				if(pl_target!=null) {
					out=true;
					pl_target.hitByBullet();
					ge.refreshPlayerLives();
					if(pl_target.getType()==Types.PL_CLIENT||pl_target.getType()==Types.PL_SERVER) {
						sendData sd=new sendData(this,"GUNSHOT ; ");
						sd.start();
					}
				}
			}
			else if(dir==Directions.RIGHT) {
				Square sqr;
				
				while(true) {
					bullet_pos.j++;
					sqr=getSquareAt(bullet_pos);
					if(!sqr.is_availble())break;
				}
				Player pl_target=sqr.getPlayer();
				if(pl_target!=null) {
					out=true;
					pl_target.hitByBullet();
					ge.refreshPlayerLives();
					if(pl_target.getType()==Types.PL_CLIENT||pl_target.getType()==Types.PL_CLIENT) {
						sendData sd=new sendData(this,"GUNSHOT ; ");
						sd.start();
					}
				}
			}
			else if(dir==Directions.LEFT) {
				Square sqr;
				
				while(true) {
					bullet_pos.j--;
					sqr=getSquareAt(bullet_pos);
					if(!sqr.is_availble())break;
				}
				Player pl_target=sqr.getPlayer();
				if(pl_target!=null) {
					out=true;
					pl_target.hitByBullet();
					ge.refreshPlayerLives();
					if(pl_target.getType()==Types.PL_CLIENT||pl_target.getType()==Types.PL_SERVER) {
						sendData sd=new sendData(this,"GUNSHOT ; ");
						sd.start();
					}
				}
			}
		}
		return out;
	}
	public void randomMove(Player pl) {
		boolean done=false;
		while(!done) {
			Random rnd=new Random();
			int rnd_num=rnd.nextInt(4);
			Directions d;
			switch(rnd_num) {
			case 0:
				d=Directions.UP;
				break;
			case 1:
				d=Directions.DOWN;
				break;
			case 2:
				d=Directions.RIGHT;
				break;
			default:
				d=Directions.LEFT;
				break;
			}
			
			done=moveTo(pl, d);
		}
	}
	public void removeAllPlayers() {
		for (int i = 0; i < map_squares.length; i++) {
            for (int j = 0; j < map_squares[i].length; j++) {
            	map_squares[i][j].unsetPlayer();
            }
		}
	}
	private static class sendPlPos extends Thread {
	
		private DataOutputStream out;
	
		private Map map;

	public sendPlPos(Map map) {
		this.map=map;
		
	}

	public void run() {
		boolean running=true;
		
		while(running) {
			
			try {
				out=new DataOutputStream(map.socket.getOutputStream());
				if(map.socket!=null) {
					if(map.socket.isConnected()) {
						
						out.writeUTF("PL_S ; "+map.players.get(0).getPos().i+" ; "+map.players.get(0).getPos().j+" ; "+map.players.get(0).getDirection());
						
					}
				}
			} catch (IOException e) {}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		}
	
	}

	private static class sendData extends Thread {

		private DataOutputStream out;
		private String data;
		private Map map;

	public sendData(Map map,String data) {
		this.map=map;
		this.data=data;
	}

	public void run() {
		try {
			out=new DataOutputStream(map.socket.getOutputStream());
			if(map.socket!=null) {
				if(map.socket.isConnected()) {
					out.writeUTF(data);
					System.out.println(data);
				}
			}
		} catch (IOException e) {}
	
	}
}
	private static class recData extends Thread {

		private Map map;
		private DataInputStream inp;
		

		// set socket
		public recData(Map map) {
			this.map=map;
			try {
				if(map.socket.isConnected()) {
					inp = new DataInputStream(map.socket.getInputStream());
				}
			} catch (IOException e) {
			}
			
		}
	
		public void run() {
			while(true) {
				try {
	
					String str=inp.readUTF();
					String[] data=str.split(" ; ");
					if(data[0].equals("HP")) {
					int i=Integer.parseInt(data[1]);
					int j=Integer.parseInt(data[2]);
					Square sqr=map.getSquareAt(new Position(i,j));
					sqr.putHealthObj();
						}
					else if(data[0].equals("PL_S")) {
						int i=Integer.parseInt(data[1]);
						int j=Integer.parseInt(data[2]);
						Directions d=null;
						if(data[3].equals("UP"))d=Directions.UP;
						else if(data[3].equals("DOWN"))d=Directions.DOWN;
						else if(data[3].equals("RIGHT"))d=Directions.RIGHT;
						else if(data[3].equals("LEFT"))d=Directions.LEFT;
						map.players.get(1).setPos(new Position(i, j));
						map.players.get(1).setDirection(d);
					}
					else if(data[0].equals("GUNSHOT")) {
						map.players.get(0).hitByBullet();
					}
					else if(data[0].equals("REVIVE")) {
						int num=Integer.parseInt(data[1]);
						for(int i=0;i<map.players.size();i++) {
							if(map.players.get(i).getNum()==num) {
								map.players.get(i).revive();
								break;
							}
						}
					}
				} catch (IOException e) {}
				
			}
			
	
		}
	}
}
