package harmless.exceptions;

public class RegistreNonTrouveException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3773365263960735314L;

	public RegistreNonTrouveException(String id)
	{
		super("Impossible de trouver le registre " + id + ".");
	}

}
