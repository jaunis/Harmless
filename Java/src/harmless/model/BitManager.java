package harmless.model;

import java.util.List;

public class BitManager {

	protected List<Bit> listeBits;

	public BitManager() {
		super();
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

	public void addBit(Bit b) {
		if(!listeBits.contains(b))
			listeBits.add(b);
	}

	public int getValeur() {
		int res = 0;
		int puissance = 1;
		for(int i=0; i<listeBits.size(); i++)
		{
			res += listeBits.get(i).getValeur() * puissance;
			puissance *= 2;
		}
		return res;
	}

	public void setValeur(int valeur) {
		int puissance = 1;
		for(int i=0; i<=(Math.log(valeur)/Math.log(2.)); i++)
		{
			if((puissance & valeur) > 0)
				listeBits.get(i).setValeur(1);
			else listeBits.get(i).setValeur(0);
			puissance *= 2;
		}
	}

	public void setValeur(String valeurHexa) {
		int valeur = Integer.parseInt(valeurHexa, 16);
		setValeur(valeur);
	}

}