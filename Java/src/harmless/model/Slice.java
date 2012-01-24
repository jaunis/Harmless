/**
 * 
 */
package harmless.model;

import java.util.ArrayList;
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
	
	public Slice(String id, String description, List<Range> listeRanges,
			Register registre) {
		super();
		this.id = id;
		this.description = description;
		this.listeRanges = listeRanges;
		this.registre = registre;
		readOnly = false;
		items = new Hashtable<Integer, String>();
		listeBits = new ArrayList<Bit>();
		listeRanges = new ArrayList<Range>();
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
		//TODO méthode à coder: ajouter aussi les bits correspondant
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
