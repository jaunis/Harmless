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
	private String address;

	
	public Register(Peripheral peripherique, String id, String description, String adresse, int size)
	{
		super(size);
		this.peripherique = peripherique;
		this.id = id;
		this.description = description;
		listeSlices = new ArrayList<Slice>();
		this.address = adresse == null? "unspecified": adresse;
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
	
	public void setAddress(String address)
	{
	     this.address = address;	
	}
	
	public String toString()
	{
		return id + ": " + description
				+ "\t" + getValeurHexa();
	}
	
	public void addBit(int position, Bit b)
	{
		listeBits.add(position, b);
	}
	
	public void addSlice(Slice s)
	{
		listeSlices.add(s);
	}
	
//	public boolean equals(Object o)
//	{
//		if(!(o instanceof Register))
//			return false;
//		else
//		{
//			if(this.id.equals(((Register)o).getId()))
//					return true;
//			else
//				return false;
//		}
//	}
}
