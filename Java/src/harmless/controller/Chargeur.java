package harmless.controller;

import harmless.exceptions.InvalidDescriptionException;
import harmless.model.Bit;
import harmless.model.Item;
import harmless.model.Peripheral;
import harmless.model.Range;
import harmless.model.Register;
import harmless.model.Slice;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * @author Jean AUNIS
 *
 */

public class Chargeur {

	private Socket socket;
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Chargeur(Socket socket)
	{
		this.socket = socket;
	}
	
	public List<Peripheral> initialiserPeripheriques() throws JDOMException, IOException, InvalidDescriptionException 
	{
		PrintWriter out = null;
		InputStream ips = null;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			ips = socket.getInputStream();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		out.println("send");
		InputStream newIps = changeInputStream(ips);
		SAXBuilder sxb = new SAXBuilder();
		List<Peripheral> retour = traiterXML(sxb.build(newIps));
		newIps.close();
		return retour;
	}
	
	
	@SuppressWarnings("unchecked")
	private List<Peripheral> traiterXML(Document document) throws InvalidDescriptionException
	{
		ArrayList<Peripheral> listeObjPeriph = new ArrayList<Peripheral>();
		Element racine = document.getRootElement();
		List<Element> peripheriques = racine.getChildren("peripheral");
		
		Iterator<Element> itPeriph = peripheriques.iterator();
		while(itPeriph.hasNext())
		{
			Peripheral objPeriph = getObjetPeripherique(itPeriph.next());
			listeObjPeriph.add(objPeriph);
		}
		
		return listeObjPeriph;
	}
	
	@SuppressWarnings("unchecked")
	private Peripheral getObjetPeripherique(Element periphXml) throws InvalidDescriptionException
	{
		Peripheral objPeriph = new Peripheral(periphXml.getAttributeValue("name"));
		
		List<Element> registres = periphXml.getChildren("register");
		
		if(registres.isEmpty()) 
		{
			String message = "Dans le périphérique " + objPeriph.getName() + ":";
			message += "aucun Registre trouvé.\n";
			throw new InvalidDescriptionException(message);
		}
		
		Iterator<Element> itRegistres = registres.iterator();
		while(itRegistres.hasNext())
		{
			Register objRegistre = getObjetRegistre(itRegistres.next(), objPeriph);
			objPeriph.addRegister(objRegistre);
		}
		return objPeriph;
	}
	
	@SuppressWarnings("unchecked")
	private Register getObjetRegistre(Element registreXml, Peripheral periph) throws InvalidDescriptionException
	{
		int taille = Integer.parseInt(registreXml.getAttributeValue("size"));
		Register objRegistre = new Register(periph, 
				registreXml.getAttributeValue("id"),
				registreXml.getAttributeValue("description"),
				registreXml.getAttributeValue("address"),
				taille
				);

		
		/*
		 * impossible de faire appel à objRegistre.size() ci-dessous, 
		 * car la liste est vide donc size() renvoie 0
		 */
		for(int i = 0; i < taille; i++)
		{
			objRegistre.addBit(new Bit(0, objRegistre));
		}
		
		String valeur = registreXml.getAttributeValue("value") == null? "0" : registreXml.getAttributeValue("value");
		objRegistre.setValeurHexa(valeur);
		
		List<Element> slices = registreXml.getChildren("slice");
		Iterator<Element> itSlices = slices.iterator();
		while(itSlices.hasNext())
		{
			Element sliceCourant = itSlices.next();
			objRegistre.addSlice(getObjetSlice(sliceCourant, objRegistre));
			
		}
		return objRegistre;
	}
	
	@SuppressWarnings("unchecked")
	private Slice getObjetSlice(Element sliceXml, Register registre) throws InvalidDescriptionException
	{
		String idSlice = sliceXml.getAttributeValue("id");
		String descrSlice = sliceXml.getAttributeValue("description");
		Slice objSlice = new Slice(idSlice, descrSlice, registre);
		
		List<Element> listeRanges = sliceXml.getChildren("range");
		
		if(listeRanges.isEmpty()) 
		{
			String message = "Dans le slice " + idSlice + ":";
			message += "aucun Range trouvé.\n";
			throw new InvalidDescriptionException(message);
		}
			
		Iterator<Element> itRanges = listeRanges.iterator();
		while(itRanges.hasNext())
		{
			objSlice.addRange((getObjetRange(itRanges.next(), objSlice)));
		}
		
		List<Element> items = sliceXml.getChildren("item");
		if(!items.isEmpty())
		{
			//Hashtable<Integer, String> itemsTable = new Hashtable<Integer, String>();
			ArrayList<Item> itemListe = new ArrayList<Item>();
			Iterator<Element> itItems = items.iterator();
			while(itItems.hasNext())
			{
				Element item = itItems.next();
				int valeur = Integer.parseInt(item.getAttributeValue("val"));
				String descrItem = item.getAttributeValue("description");
				//itemsTable.put(valeur, descrItem);
				
				Item bla = new Item(valeur, descrItem);
				itemListe.add(bla);
				
				
			}
			//objSlice.setItems(itemsTable);
			objSlice.setItem(itemListe);
		}
		
		return objSlice;
	}
	
	private Range getObjetRange(Element rangeXml, Slice slice)
	{
		return new Range(
				Integer.parseInt(rangeXml.getAttributeValue("from")),
				Integer.parseInt(rangeXml.getAttributeValue("to")),
				slice);
	}
	
	/**
	 * Transforme un flux en un autre, en faisant transiter le flux par une String. 
	 * La fin du flux est repérée par le caractère "\0"<br>
	 * Intérêt: la fonction SaxBuilder.build(InputStream) attend que le flux soit terminé, 
	 * c'est-à-dire, dans le cas présent, que la socket client soit fermée par le serveur.
	 * Ce qui oblige à réinitialiser la socket après chaque transmission.
	 * Cette méthode permet de contourner le problème.
	 * @param ips le flux à transformer
	 * @return le nouveau flux
	 * @throws IOException
	 */
	public static InputStream changeInputStream(InputStream ips) throws IOException
	{
		BufferedInputStream bufIps= new BufferedInputStream(ips);
		StringWriter writer = new StringWriter();
		//récupérer un caractère à la fois n'est sans doute pas le plus efficace,
		//mais c'est le plus simple pour effectuer le test
		int valeur = bufIps.read();
		while(valeur != (new String("\0")).toCharArray()[0])
		{
			writer.write(valeur);
			valeur = bufIps.read();
		}
		System.out.println(writer.toString());
		return new ByteArrayInputStream(writer.toString().getBytes());
	}
}
