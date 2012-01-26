/**
 * 
 */
package harmless.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Jean AUNIS
 *
 */
public class Slice {
	
	private String id;
	private String description;
	private boolean readOnly;
	private List<Bit> listeBits;
	//utile?
	private List<Range> listeRanges;
	private Register registre;
	private Hashtable<Integer, String> items;
	
	public Slice(String id, String description,	Register registre) 
	{
		super();
		this.id = id;
		this.description = description;
		this.registre = registre;
		readOnly = false;
		items = new Hashtable<Integer, String>();
		listeBits = new ArrayList<Bit>();
		listeRanges = new ArrayList<Range>();
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}
	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	/**
	 * @return the listeBits
	 */
	public List<Bit> getListeBits() {
		return listeBits;
	}
	/**
	 * @param listeBits the listeBits to set
	 */
	public void setListeBits(List<Bit> listeBits) {
		this.listeBits = listeBits;
	}
	/**
	 * @return the listeRanges
	 */
	public List<Range> getListeRanges() {
		return listeRanges;
	}
	/**
	 * @param listeRanges the listeRanges to set
	 */
	public void setListeRanges(List<Range> listeRanges) {
		this.listeRanges = listeRanges;
	}
	/**
	 * @return the registre
	 */
	public Register getRegistre() {
		return registre;
	}
	/**
	 * @param registre the registre to set
	 */
	public void setRegistre(Register registre) {
		this.registre = registre;
	}
	/**
	 * @return the valeurs
	 */
	public Hashtable<Integer, String> getItems() {
		return items;
	}
	/**
	 * @param valeurs the valeurs to set
	 */
	public void setItems(Hashtable<Integer, String> items) {
		this.items = items;
	}
	
	public void addItem(Integer valeur, String sens)
	{
		items.put(valeur, sens);
	}

	public void addBit(Bit b)
	{
		if(!listeBits.contains(b))
			listeBits.add(b);
	}
	
	public void addRange(Range r)
	{
		//TODO méthode à tester car un peu complexe
		List<Bit> aInserer = null;
		try
		{
			aInserer = registre.getListeBits().subList(r.getTo(), r.getFrom()+1);
		}
		catch(IndexOutOfBoundsException e)
		{
			System.err.println("liste de bits du registre " + registre.getId() + 
					" mal initialisée.\n");
			e.printStackTrace();
		}
		Bit premier = aInserer.get(0);
		
		Comparator<Bit> comp = new Comparator<Bit>(){
			@Override
			public int compare(Bit o1, Bit o2) {
				List<Bit> listeBits = Slice.this.registre.getListeBits();
				int i1 = listeBits.indexOf(o1);
				int i2 = listeBits.indexOf(o2);
				if(i1 < i2) return -1;
				else if (i1 > i2) return 1;
				else return 0;
			}
		};
		
		
		if(listeBits.size()>0)
		{
			//on cherche à quel indice insérer dans la liste de bits
			int indiceInsertion = 0;
			Bit bitCourant = listeBits.get(indiceInsertion);
			while(comp.compare(premier, bitCourant) < 0)
			{
				indiceInsertion++;
				try
				{
					bitCourant = listeBits.get(indiceInsertion);
				}
				catch(IndexOutOfBoundsException e)
				{
					System.err.println("liste de bits du registre " + registre.getId() + 
							" mal initialisée.\n");
					e.printStackTrace();
				}
			}
			
			/* 
			 * Pour insérer il faut tout décaler.
			 * on recopie listeBits à partir de indiceInsertion jusqu'à la fin
			 * puis on insère les nouveaux bits à indiceInsertion
			 * puis on remet au bout les bits recopiés
			 */
			List<Bit> finListeBits = listeBits.subList(indiceInsertion, listeBits.size()-1);
			listeBits.addAll(indiceInsertion, aInserer);
			listeBits.addAll(indiceInsertion + aInserer.size(), finListeBits);
		}
		else
		{
			listeBits.addAll(aInserer);
		}
	}
	
	public String toString()
	{
		return id + ": " + description
				+ "\t" + Integer.toHexString(getValeur());
	}
	
	public int getValeur()
	{
		int res = 0;
		int puissance = 1;
		for(int i=0; i<listeBits.size(); i++)
		{
			res += listeBits.get(i).getValeur() * puissance;
			puissance *= 2;
		}
		return res;
	}
	
	//public void setValeur(int i)
}
