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
public class Register extends BitManager{
	
	private String id;
	private String description;
	private List<Slice> listeSlices;
	private Peripheral peripherique;
<<<<<<< HEAD
	private String address;
=======
	private String adresse;
>>>>>>> c8aec1978cf6f431f8246d8e5d2d630893bca7ea
	
	public Register(Peripheral peripherique, String id, String description, String adresse, int size)
	{
		super(size);
		this.peripherique = peripherique;
		this.id = id;
		this.description = description;
		this.adresse = adresse;
		listeSlices = new ArrayList<Slice>();
		this.address = "unspecified";
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
	
	public int size()
	{
		return listeBits.size();
	}
	
	public String getAddress()
	{
		return address;
	}
	
	public void setAddress(String bla)
	{
	     this.address = bla;	
	}
	
	public String toString()
	{
		return id + ": " + description
				+ "\t" + Integer.toHexString(getValeur());
	}
	
	public void addBit(int position, Bit b)
	{
		listeBits.add(position, b);
	}
	
	public void addSlice(Slice s)
	{
		listeSlices.add(s);
	}
	/**
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}
	/**
	 * @param adresse the adresse to set
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
}
