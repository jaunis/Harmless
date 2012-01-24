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
public class Peripheral {
	
	private String name;
	private List<Register> listeRegistres;
	
	public Peripheral(String name)
	{
		this.name = name;
		listeRegistres = new ArrayList<Register>();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the listeRegistres
	 */
	public List<Register> getListeRegistres() {
		return listeRegistres;
	}
	/**
	 * @param listeRegistres the listeRegistres to set
	 */
	public void setListeRegistres(List<Register> listeRegistres) {
		this.listeRegistres = listeRegistres;
	}
	
	public void addRegister(int position, Register r)
	{
		listeRegistres.add(position, r);
	}
	
	public String toString()
	{
		return name;
	}

}
