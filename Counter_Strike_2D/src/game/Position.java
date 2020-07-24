package game;

public class Position {
	public int i;
	public int j;
	public Position(int i, int j) {
		this.i=i;
		this.j=j;
	}
	public boolean equals(Object obj) {
		boolean out=false;
		Position pos=(Position) obj;
		if(this.i==pos.i && this.j==pos.j) {
			out=true;
		}
		return out;
	}
}
