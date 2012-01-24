/**
 * 
 */
package harmless.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jean AUNIS
 *
 */
public class Register {
	
	private String id;
	private String description;
	private List<Slice> listeSlices;
	private List<Bit> listeBits;
	private Peripheral peripherique;
	
	
	public Register(Peripheral peripherique, String id, String description, int size)
	{
		this.peripherique = peripherique;
		this.id = id;
		this.description = description;
		listeBits = new ArrayList<Bit>(size);
		listeSlices = new ArrayList<Slice>();
	}
	/**
	 * @return the peripherique
	 */
	public Peripheral getPeripherique() {
		return peripherique;
	}
	/**
	 * @param peripherique the peripherique to set
	 */
	public void setPeripherique(Peripheral peripherique) {
		this.peripherique = peripherique;
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
	 * @return the listeSlices
	 */
	public List<Slice> getListeSlices() {
		return listeSlices;
	}
	/**
	 * @param listeSlices the listeSlices to set
	 */
	public void setListeSlices(List<Slice> listeSlices) {
		this.listeSlices = listeSlices;
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
	
	public int size()
	{
		return listeBits.size();
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
	
}
