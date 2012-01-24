/**
 * 
 */
package harmless.model;

/**
 * @author Jean AUNIS
 *
 */
public class Bit {
	
	private int valeur;
	//utile?
	private Register registre;
	//faut-il rajouter une position?

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
	 * @return the valeur
	 */
	public int getValeur() {
		return valeur;
	}

	/**
	 * @param valeur the valeur to set
	 */
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public Bit(int valeur) {
		this.valeur = valeur;
	}
	
	public String toString()
	{
		return Integer.toString(valeur);
	}
}
