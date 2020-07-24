package game;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.Player.Directions;

public class Icons {
	public ImageIcon icon_none;
	public ImageIcon icon_heart;
	public ImageIcon[][] icon_pl=new ImageIcon[4][4];
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
		icon_none=resizeIcon(new ImageIcon("Data/Images/none.png"),max);
		icon_heart=resizeIcon(new ImageIcon("Data/Images/heart.png"),max);
		icon_pl[0][0]=resizeIcon(new ImageIcon("Data/Images/PL1/U.png"),max);
		icon_pl[0][1]=resizeIcon(new ImageIcon("Data/Images/PL1/D.png"),max);
		icon_pl[0][2]=resizeIcon(new ImageIcon("Data/Images/PL1/R.png"),max);
		icon_pl[0][3]=resizeIcon(new ImageIcon("Data/Images/PL1/L.png"),max);
		
		icon_pl[1][0]=resizeIcon(new ImageIcon("Data/Images/PL2/U.png"),max);
		icon_pl[1][1]=resizeIcon(new ImageIcon("Data/Images/PL2/D.png"),max);
		icon_pl[1][2]=resizeIcon(new ImageIcon("Data/Images/PL2/R.png"),max);
		icon_pl[1][3]=resizeIcon(new ImageIcon("Data/Images/PL2/L.png"),max);
		
		icon_pl[2][0]=resizeIcon(new ImageIcon("Data/Images/PL3/U.png"),max);
		icon_pl[2][1]=resizeIcon(new ImageIcon("Data/Images/PL3/D.png"),max);
		icon_pl[2][2]=resizeIcon(new ImageIcon("Data/Images/PL3/R.png"),max);
		icon_pl[2][3]=resizeIcon(new ImageIcon("Data/Images/PL3/L.png"),max);
		
		icon_pl[3][0]=resizeIcon(new ImageIcon("Data/Images/PL4/U.png"),max);
		icon_pl[3][1]=resizeIcon(new ImageIcon("Data/Images/PL4/D.png"),max);
		icon_pl[3][2]=resizeIcon(new ImageIcon("Data/Images/PL4/R.png"),max);
		icon_pl[3][3]=resizeIcon(new ImageIcon("Data/Images/PL4/L.png"),max);
	}
	public ImageIcon resizeIcon(ImageIcon image ,int w) {
		return new ImageIcon(image.getImage().getScaledInstance(w,w, Image.SCALE_DEFAULT));
	}
	public ImageIcon getPlayerIcon(Directions dir,int num) {
		
		ImageIcon out= null;
		switch (dir) {
			case UP:
				out=icon_pl[num-1][0];
				break;
			case DOWN:
				out=icon_pl[num-1][1];
				break;
			case RIGHT:
				out=icon_pl[num-1][2];
				break;
			case LEFT:
				out=icon_pl[num-1][3];
				break;
		}
		return out;
	}
}
