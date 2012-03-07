package harmless.model;

public class Item {
	
	private int valeur;
	private String description;
	
	
	public Item(int valeur, String description){
		this.valeur = valeur;
		this.description = description;	
	}
	
	//voir si utile
	public boolean equals(Item item){
		return this.valeur==item.getValeur();		
	}
	
	public int getValeur(){
		return this.valeur;
	}
	
	public void setValeur(int value){
		this.valeur = value;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	

}
