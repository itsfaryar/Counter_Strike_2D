package game;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class socketRW {
	public int port;
	public String ip;
	public socketRW(String ip,int port) {
		this.ip=ip;
		this.port=port;
	}
}
