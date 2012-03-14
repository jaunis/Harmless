package harmless.model;

import java.util.ArrayList;
import java.util.List;

public class BitManager {

	protected List<Bit> listeBits;

	public BitManager(int size) {
		listeBits = new ArrayList<Bit>(size);
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
	
	public String getValeurHexa()
	{
		return "0x" + Integer.toHexString(getValeur());
	}

	public void setValeur(int valeur) {
		int puissance = 1;
		for(int i=0; i<listeBits.size(); i++)
		{
			if((puissance & valeur) > 0)
				listeBits.get(i).setValeur(1);
			else listeBits.get(i).setValeur(0);
			puissance *= 2;
		}
	}

	public void setValeurHexa(String valeurHexa) throws NumberFormatException {
		String copie = valeurHexa;
		if(valeurHexa.startsWith("0x"))
			copie = valeurHexa.substring(2);
		int valeur = Integer.parseInt(copie, 16);
		setValeur(valeur);
	}

}