package harmless.controller;

import harmless.model.Register;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Updater extends Thread {
	
	private Socket socket;
	private List<Register> listeMaj;
	private boolean stop;
	private PrintWriter out;
	private InputStream ips;
	
	public Updater(Socket socket)
	{
		this.socket = socket;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			ips = socket.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		listeMaj = new ArrayList<Register>();
	}
	
	public void start()
	{	}
	
	public void run()
	{
		while(!stop)
		{
			synchronized(socket)
			{
				out.print("send");
				recevoirUpdate();
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void recevoirUpdate() {
		// TODO recevoir et effectuer les MAJ
		
	}

	public void envoyerUpdate()
	{
		synchronized(socket)
		{
			//TODO envoyer les MAJ
		}
	}
}
