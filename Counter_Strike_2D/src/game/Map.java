package game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

import game.Player.Directions;
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
	public Map(GameEnv ge,Icons icons,int w,int h,ArrayList<Player>players) {
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		startTime_for_heart=now.toSecondOfDay(); 
		this.ge=ge;
		this.players=players;
		panel=new JPanel();
		this.w=w;
		this.h=h;
		this.icons=icons;
		map_squares= new Square[h+2][w+2];
		panel.setLayout(new GridLayout(h+2,w+2));
		initBoard();
		
	}
	public void run() {
		Player lastStandng=null;
		int alive_counter=0;
		while(runningTread) {
			LocalTime now = LocalTime.now(ZoneId.systemDefault());
			int t=now.toSecondOfDay(); 
			if(t-startTime_for_heart>=20) {
				getSquareAt(getFreePos()).putHealthObj();
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
		boolean flag=false;
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
		boolean flag=false;
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
			//Square sqr=getSquareAt(pos);
			
			//Position old_pos=pl.getPos();
			//if(old_pos!=null)getSquareAt(old_pos).unsetPlayer();
				
			//sqr.setPlayer(pl);
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
}
