package game;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	public Map(Icons icons,int w,int h,ArrayList<Player>players) {
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
		
		while(true) {
			removeAllPlayers();
			for(int i=0;i<players.size();i++) {
				
				Position pos=players.get(i).getPos();
				
				if(pos!=null) {
					//if(pos.i<h && pos.j<w) {
						map_squares[pos.i][pos.j].setPlayer(players.get(i));
					//}
				}
				
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
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
	public Square getSquareAt(Position pos) {
		return map_squares[pos.i][pos.j];
	}

	public boolean moveTo(Player pl,Directions dir) {
		boolean out=false;
		Position pos=pl.getPosAtDir(dir);
		//if(pos.i>=w || pos.j>=h)return false;
		Square sqr=getSquareAt(pos);
		if(sqr.is_availble()) {
			//Position old_pos=pl.getPos();
			//if(old_pos!=null)getSquareAt(old_pos).unsetPlayer();
			
			//sqr.setPlayer(pl);
			pl.setPos(pos);
			pl.setDirection(dir);;
		}
		System.out.println(pos.i+","+pos.j);
		return out;
	}
	public boolean moveTo(Player pl,Position pos) {
		boolean out=false;
		//if(pos.i>=w || pos.j>=h)return false;
		Square sqr=getSquareAt(pos);
		if(sqr.is_availble()) {
			Position old_pos=pl.getPos();
			//if(old_pos!=null)getSquareAt(old_pos).unsetPlayer();
			
			//sqr.setPlayer(pl);
			pl.setPos(pos);
		}
		return out;
	}
	public void removeAllPlayers() {
		for (int i = 0; i < map_squares.length; i++) {
            for (int j = 0; j < map_squares[i].length; j++) {
            	map_squares[i][j].unsetPlayer();
            }
		}
	}
}
