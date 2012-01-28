package harmless;

import harmless.controller.Chargeur;
import harmless.model.Peripheral;
import harmless.model.Range;
import harmless.model.Register;
import harmless.model.Slice;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {


	public static final String PLUGIN_ID = "Harmless";

	// The shared instance
	private static Activator plugin;
	private Chargeur chargeur;
	private List<Peripheral> listePeripheriques;
	
	public Chargeur getChargeur() {
		return chargeur;
	}

	public List<Peripheral> getListePeripheriques() {
		return listePeripheriques;
	}

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		//TODO il faudra sans doute faire un truc plus propre pour récupérer le port
		System.out.println("Veuillez entrer le port d'écoute:");
		Scanner sc = new Scanner(System.in);
		int port = Integer.parseInt(sc.nextLine());
		chargeur = new Chargeur("localhost", port);
		listePeripheriques = chargeur.initialiserPeripheriques();
		afficherEtat();
	}
	
	public void afficherEtat()
	{
		for(Peripheral p: listePeripheriques)
		{
			System.out.println(p.getName() + ":");
			for(Register r: p.getListeRegistres())
			{
				System.out.println(" " + r.getId() + " : " + r.getDescription());
				System.out.println(" valeur: " + r.getValeur());
				for(Slice s: r.getListeSlices())
				{
					System.out.println("  " + s.getId() + " : " + s.getDescription());
					System.out.println("  valeur : " + s.getValeur());
					for(Range range: s.getListeRanges())
					{
						System.out.println("   from " + range.getFrom() + " to " + range.getTo());
					}
					Hashtable<Integer, String> listeItems = s.getItems();
					Enumeration<Integer> listeCles = listeItems.keys();
					while(listeCles.hasMoreElements())
					{
						int local = listeCles.nextElement();
						System.out.println("   " + local + ": " + listeItems.get(local));
					}
				}
			}
		}
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
