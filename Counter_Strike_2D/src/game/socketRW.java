package game;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class socketRW {
	public static final byte SND_MAP=15;
	
	public static final byte NACK=0;
	public static final byte ACK=1;
	private Socket socket;
	private ObjectInputStream inp;
	private ObjectOutputStream out;
	public socketRW(Socket socket) {
		this.socket=socket;
		try {
			inp=new ObjectInputStream(socket.getInputStream());
			out=new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {}
	}
	public void writeMap(Map map) {
		boolean done=false;
		while(!done) {
			try {
				out.writeByte(SND_MAP);
				if(inp.readByte()==ACK) {
					out.writeObject(map);
					done=true;
				}
			} catch (IOException e) {}
		}
	}
	public Map readMap() {
		boolean done=false;
		Map map=null;
		while(!done) {
			try {
				if(inp.readByte()==SND_MAP) {
					out.writeByte(ACK);
					map=(Map)inp.readObject();
					done=true;
				}
				else out.writeByte(NACK);
			} catch (IOException e) {} catch (ClassNotFoundException e) {}
		}
		return map;
	}
}
