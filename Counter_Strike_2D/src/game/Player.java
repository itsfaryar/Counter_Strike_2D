package game;

import javax.swing.ImageIcon;

import game.Player.Directions;

public class Player extends Thread{
	public enum Directions{UP,LEFT,DOWN,RIGHT};
	public enum Modes{BOT,ME,ONLINEPLAYER}
	private Modes mode;
	private Directions dir;
	private Position pos;
	private ImageIcon icon;
	private Icons icons;
	public Player(Icons icons,Modes mode,Directions dir) {
		this.icons=icons;
		this.dir=dir;
		this.mode=mode;
		icon=icons.getPlayerIcon(dir);
	
	}
	public void run() {
		System.out.println("yea");
	}
	public Directions getDirection() {
		return dir;
	}
	public void setDirection(Directions dir) {
		this.dir=dir;
		refreshIcon();
	}
	public ImageIcon getIcon() {
		return icon;
	}
	public Modes getMode() {
		return mode;
	}
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
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
		icon=icons.getPlayerIcon(dir);
	}
}
