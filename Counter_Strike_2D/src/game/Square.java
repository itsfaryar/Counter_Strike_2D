package game;


import java.awt.Insets;
import java.util.Random;

import javax.swing.JButton;


import game.Player.Directions;

public class Square extends JButton{
	public enum Materials{WALL,Empty,NOTAVAILBLE}
	
	private final static java.awt.Color COLOR_WALL=new java.awt.Color(94, 38, 5);
	private final static java.awt.Color COLOR_EMPTY=new java.awt.Color(248, 217, 109);
	private final static java.awt.Color COLOR_NOTAVAILBLE=new java.awt.Color(54, 40, 25);
	private Player player=null;
	private Icons icons;
	private Materials material;
	public Square(Icons icons,int w,int h) {
		this.icons=icons;
		Random rand = new Random();
		int r=rand.nextInt(w*h);
		if(r<(0.4*w*h)) {
			this.material=Materials.WALL;
			this.setBackground(COLOR_WALL);
		}
		else {
			this.material=Materials.Empty;
			this.setBackground(COLOR_EMPTY);
		}
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setIcon(icons.icon_none);
	}
	public Square(Icons icons,int w,int h,Materials material) {
		this.material=material;
		if(material==Materials.WALL) {
			this.setBackground(COLOR_WALL);
		}
		else if(material==Materials.Empty){
			this.setBackground(COLOR_EMPTY);
		}
		else {
			this.setBackground(COLOR_NOTAVAILBLE);
			this.setEnabled(false);
		}
		
		
		Random rand = new Random(); 
		this.setMargin(new Insets(0, 0, 0, 0));
		this.setIcon(icons.icon_none);
	}
	
	public Materials getMaterial() {
		return material;
	}
	public void setMaterial(Materials material) {
		this.material=material;
		if(material==Materials.WALL) {
			this.setBackground(COLOR_WALL);
		}
		else if(material==Materials.Empty){
			this.setBackground(COLOR_EMPTY);
		}
		else {
			this.setBackground(COLOR_NOTAVAILBLE);
			this.setEnabled(false);
		}
	}
	public boolean is_availble() {
		boolean out=false;
		if(player==null && material==Materials.Empty) {
			out=true;
		}
		return out;
	}
	public boolean setPlayer(Player pl) {
		boolean out=false;
		if(player==null ) {
			this.player=pl;
			this.setIcon(player.getIcon());
			out=true;
		}
		return out;
	}
	public void unsetPlayer() {
		this.player=null;
		if(icons==null);//System.out.println("shiit");
		else this.setIcon(icons.icon_none);

	}
	
}
