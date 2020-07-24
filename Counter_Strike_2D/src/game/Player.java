package game;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Random;

import javax.swing.ImageIcon;

import game.Player.Directions;

public class Player extends Thread{
	public enum status{ALIVE,DEAD}
	public enum Directions{UP,LEFT,DOWN,RIGHT};
	public enum Types{BOT,ME,PL_SERVER,PL_CLIENT}
	private Types type;
	private Directions dir;
	private status state;
	private Position pos;
	private ImageIcon icon;
	private Icons icons;
	private Map map;
	private int num;
	private int lives;
	private int last_shot=0;
	private boolean runningTread=true;
	private int timeOfDeath=-1;
	public Player(Icons icons,Types type,Directions dir,int num) {
		this.state =status.ALIVE;
		this.lives=3;
		this.icons=icons;
		this.dir=dir;
		this.type=type;
		this.num=num;
		icon=icons.getPlayerIcon(dir,num);
	
	}
	public void setMap(Map map) {
		this.map=map;
	}
	
	public void run() {
		if(type==Types.BOT) {
			while(runningTread) {
				if(!map.fireAtDir(this)) {
					map.randomMove(this);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
	}
	public int getTimeOfDeath() {
		return timeOfDeath;
	}
	public int getNum() {
		return num;
	}
	public boolean is_alive() {
		if(state==status.ALIVE)return true;
		else return false;
	}
	public Directions getDirection() {
		return dir;
	}
	public void setDirection(Directions dir) {
		this.dir=dir;
		refreshIcon();
	}
	public void setIcons(Icons icons) {
		this.icons=icons;
		icon=icons.getPlayerIcon(dir,num);
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public Types getType() {
		return type;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public int getLives() {
		return lives;
	}
	public Position getPosAtDir(Directions dir) {
		Position new_pos=new Position(pos.i, pos.j);
		switch (dir) {
		case UP:
			new_pos.i--;
			break;
		case DOWN:
			new_pos.i++;
			break;
		case RIGHT:
			new_pos.j++;
			break;
		case LEFT:
			new_pos.j--;
			break;
		default:
			break;
		}
		return new_pos;
	}
	public void refreshIcon() {
		icon=icons.getPlayerIcon(dir,num);
	}
	private void die() {
		lives=0;
		state=status.DEAD;
		runningTread=false;
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		timeOfDeath=now.toSecondOfDay(); 
		
	}
	public void hitByBullet() {
		this.lives--;
		if(lives<=0) {
			die();
		}
	}
	public boolean is_gunAvailble() {
		if(type==Types.BOT)return true;
		LocalTime now = LocalTime.now(ZoneId.systemDefault());
		int t=now.toSecondOfDay(); 
		if(t-last_shot>=1) {
			last_shot=t;
			return true;
		}
		else {
			return false;
		}
	}
	public void stopRefreshing() {
		runningTread=false;
	}
	public void takeHealthObj() {
		if(lives<4)lives++;
		
	}
}
