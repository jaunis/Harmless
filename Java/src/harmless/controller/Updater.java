package harmless.controller;


import harmless.Activator;
import harmless.exceptions.RegistreNonTrouveException;
import harmless.model.Register;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.osgi.framework.Bundle;

public class Updater extends Thread {
	
	private int port, nbVuesAppelantes;
	private String serveur;
	private Set<Register> listeMaj;
	private boolean stop, recevoir, envoyer, majRecue;
	private Socket socket;
	private PrintWriter out;
	private InputStream ips;
		
	public Updater(String serveur, int port) {
		
		this.serveur = serveur;
		this.port = port;
		nbVuesAppelantes = 0;
		initIO();
		listeMaj = new HashSet<Register>();
		stop = false;
		recevoir = false;
		envoyer = false;
		majRecue = false;
	}
	
	public Updater(Socket socket) throws IOException {
		this.socket = socket;
		nbVuesAppelantes = 0;
		listeMaj = new HashSet<Register>();
		stop = false;
		recevoir = false;
		envoyer = false;
		majRecue = false;
		out = new PrintWriter(socket.getOutputStream(), true);
		ips = socket.getInputStream();
	}

	public synchronized void run()
	{
		while(!stop)
		{
			if(recevoir)
				recevoirUpdate();
			else if(envoyer)
				envoyerUpdate();
			try {
				wait();
			} catch (InterruptedException e) {}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void recevoirUpdate() {
		recevoir = false;
		Document document = null;
		synchronized(out)
		{
			synchronized(ips)
			{
				
				SAXBuilder sxb = new SAXBuilder();
				try {
					out.println("send");
					//TODO récupération "propre"
					InputStream newIps = Chargeur.changeInputStream(ips);
					document = sxb.build(newIps);
					newIps.close();
					Element racine = document.getRootElement();
					List<Element> registresXml = racine.getChild("update").getChildren("register");
					for(Element e: registresXml)
					{
						Register r = Activator.getDefault().getRegistre(e.getAttributeValue("id"));
						r.setValeurHexa(e.getAttributeValue("value"));
						System.err.println("registre " + e.getAttributeValue("id") + " = " + e.getAttributeValue("value"));
					}
				} catch (JDOMException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RegistreNonTrouveException e) {
					e.printStackTrace();
				}
				majRecue = true;
//				initIO();				
			}

		}
	}

	private void envoyerUpdate()
	{
		envoyer = false;
		
		synchronized(listeMaj)
		{
			out.print("receive\n");
			for(Register reg: listeMaj)
			{
				String message = reg.getId() + " " + reg.getValeurHexa();
				System.out.println(message);
				out.print(message + "\n");
			}
			out.println("end"); 
			listeMaj.removeAll(listeMaj);
			//initIO();
		}
		
	}
	
	public synchronized void demanderReception()
	{
		recevoir = true;
		notifyAll();
	}
	
	public synchronized void demanderEnvoi()
	{
		envoyer = true;
		notifyAll();
	}
	
	public void arret()
	{
		out.println("stop");
		try {
			Activator.getDefault().getBundle().stop(Bundle.STOP_TRANSIENT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stop = true;
	}
	
	public void addMaj(Register r)
	{
		listeMaj.add(r);
	}
	
	private synchronized void initIO()
	{
		try
		{
			if(out != null)
				out.close();
			if(ips != null)
				ips.close();
			if(socket != null)
				socket.close();
			
			socket = new Socket(serveur, port);
			out = new PrintWriter(socket.getOutputStream(), true);
			ips = socket.getInputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean majRecue() 
	{
		if(majRecue)
		{
			majRecue = false;
			return true;
		}
		else
			return false;
	}
	
	public boolean majEnvoyee()
	{
		return listeMaj.isEmpty();
	}
	
	/**
	 * appeler cette méthode pour signaler à l'Updater qu'une vue va l'utiliser
	 */
	public void signalerOuverture()
	{
		nbVuesAppelantes++;
	}
	
	/**
	 * appeler cette méthode pour signaler à l'Updater qu'une vue utilisant l'Updater
	 * a été fermée. Quand toutes les vues ont été fermées, l'appel de cette méthode 
	 * provoque l'arrêt de l'Updater.
	 */
	public void signalerFermeture()
	{
		nbVuesAppelantes--;
		if(nbVuesAppelantes == 0)
			arret();
		else if(nbVuesAppelantes < 0)
			System.err.println("Erreur: une vue a signalé sa fermeture" 
		+ " sans avoir signalé son ouverture; l'Updater n'est pas arrêté.");
	}
}
