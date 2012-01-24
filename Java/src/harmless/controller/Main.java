package harmless.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 
 */

/**
 * @author Jean AUNIS
 *
 */

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws JDOMException, IOException 
	{
		//String adresse = "D:\\Mes Documents\\Centrale - 3e année\\projet harmless\\example.xml";
		
		Socket socket = null;
		
		try {
			socket = new Socket("localhost", 3239);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = null;
		InputStream ips = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			ips = socket.getInputStream();
			//in = new BufferedReader(new InputStreamReader(ips));
			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		out.println("C'est parti.");
		SAXBuilder sxb = new SAXBuilder();
		
		//Document document = sxb.build(new File(adresse));
		//Main.traiterXML(sxb.build(new File("/home/jean/Téléchargements/example.xml")));
		Main.traiterXML(sxb.build(ips));
		//Main.traiterXML(sxb.build(in));		
	}
	public static void testSocket(String serveur, int port)
	{
		Socket socket = null;
	
		try {
			socket = new Socket(serveur, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Scanner sc = new Scanner(System.in);
		String entree = "";
		while(!entree.equals("stop") && !entree.equals("stop\n"))
		{
			entree = sc.nextLine();
			out.println(entree);
			try 
			{
				String retour = in.readLine();
				System.out.println(retour);
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			
		}
		
		out.close();
		try {
			in.close();
			socket.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void traiterXML(Document document)
	{
		Element racine = document.getRootElement();
		List<Element> peripheriques = racine.getChild("peripherals").getChildren("peripheral");
		Iterator<Element> itPeriph = peripheriques.iterator();
		while(itPeriph.hasNext())
		{
			Element periphCourant = itPeriph.next();
			System.out.println("Périphérique " + periphCourant.getAttributeValue("name"));
			List<Element> registres = periphCourant.getChildren("register");
			Iterator<Element> itRegistres = registres.iterator();
			while(itRegistres.hasNext())
			{
				Element registreCourant = itRegistres.next();
				String id = registreCourant.getAttributeValue("id");
				String description = registreCourant.getAttributeValue("description");
				int taille = Integer.parseInt(registreCourant.getAttributeValue("size"));
				System.out.println("  registre " + id + " : " + description 
						+ ", taille: " + taille);
				List<Element> slices = registreCourant.getChildren("slice");
				Iterator<Element> itSlices = slices.iterator();
				while(itSlices.hasNext())
				{
					Element sliceCourant = itSlices.next();
					String idSlice = sliceCourant.getAttributeValue("id");
					String descrSlice = sliceCourant.getAttributeValue("description");
					Element range = sliceCourant.getChild("range");
					String borne1 = range.getAttributeValue("from");
					String borne2 = range.getAttributeValue("to");
					System.out.println("    slice " + idSlice + 
							" : " + descrSlice + ", from " +
							borne1 + " to " + borne2);
					List<Element> items = sliceCourant.getChildren("item");
					if(!items.isEmpty())
					{
						System.out.println("    Valeurs possibles: ");
						Iterator<Element> itItems = items.iterator();
						while(itItems.hasNext())
						{
							Element item = itItems.next();
							int valeur = Integer.parseInt(item.getAttributeValue("val"));
							String descrItem = item.getAttributeValue("description");
							System.out.println("      valeur " + valeur +
									" : " + descrItem);
						}
					}
					
				}
			}
		}
	}
}
