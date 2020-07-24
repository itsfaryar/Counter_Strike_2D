package game;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Player.Directions;

public class Icons {
	public ImageIcon icon_none;
	public ImageIcon[] icon_pl1=new ImageIcon[4];
	public Icons(int w,int h) {
		w+=2;
		h+=2;
		int max=w;
		if(h>max)max=h;
		if(max>10) {
			max=(500/max);
		}
		else {
			max=64;
		}
		icon_none=resizeIcon(new ImageIcon("Data/Images/none.png"),w);
		icon_pl1[0]=resizeIcon(new ImageIcon("Data/Images/PL1/U.png"),w);
		icon_pl1[1]=resizeIcon(new ImageIcon("Data/Images/PL1/D.png"),w);
		icon_pl1[2]=resizeIcon(new ImageIcon("Data/Images/PL1/R.png"),w);
		icon_pl1[3]=resizeIcon(new ImageIcon("Data/Images/PL1/L.png"),w);
	}
	public ImageIcon resizeIcon(ImageIcon image ,int w) {
		return new ImageIcon(image.getImage().getScaledInstance(w,w, Image.SCALE_DEFAULT));
	}
	public ImageIcon getPlayerIcon(Directions dir) {
		ImageIcon out= null;
		switch (dir) {
		case UP:
			out=icon_pl1[0];
			break;
		case DOWN:
			out=icon_pl1[1];
			break;
		case RIGHT:
			out=icon_pl1[2];
			break;
		case LEFT:
			out=icon_pl1[3];
			break;
		default:
			break;
		}
		return out;
	}
}
