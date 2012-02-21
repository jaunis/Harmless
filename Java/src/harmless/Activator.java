package harmless;

import harmless.controller.Chargeur;
import harmless.controller.Updater;
import harmless.model.Peripheral;
import harmless.model.Range;
import harmless.model.Register;
import harmless.model.Slice;

import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

//bla bla

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {


	public static final String PLUGIN_ID = "Harmless";

	// The shared instance
	private static Activator plugin;
	private Chargeur chargeur;
	private Updater updater;
	public List<Peripheral> listePeripheriques;
	
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
		//TODO 
		System.out.println("Veuillez entrer le port d'écoute:");
		Scanner sc = new Scanner(System.in);
		int port = Integer.parseInt(sc.nextLine());
		Socket socket = new Socket("localhost", port);
		chargeur = new Chargeur(socket);
		
		listePeripheriques = chargeur.initialiserPeripheriques();
		afficherEtat();
		
		socket = new Socket("localhost", port);
		updater = new Updater(socket);
		updater.start();
	}
	
	public Updater getUpdater() {
		return updater;
	}

	public void setUpdater(Updater updater) {
		this.updater = updater;
	}

	public void setChargeur(Chargeur chargeur) {
		this.chargeur = chargeur;
	}

	public void setListePeripheriques(List<Peripheral> listePeripheriques) {
		this.listePeripheriques = listePeripheriques;
	}

	public void afficherEtat()
	{
		listePeripheriques.get(1).getListeRegistres().get(1).setValeur(33);
		for(Peripheral p: listePeripheriques)
		{
			System.out.println(p.getName() + ":");
			for(Register r: p.getListeRegistres())
			{
				System.out.println(" " + r.getId() + " : " + r.getDescription() + ", " + r.getAdresse());
				System.out.println(" valeur: " + r.getValeurHexa());
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
					System.out.println("  nb de bits du slice: " + s.getListeBits().size());
					
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
