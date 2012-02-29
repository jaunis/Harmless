package harmless;

import harmless.controller.Chargeur;
import harmless.controller.Updater;
import harmless.exceptions.RegistreNonTrouveException;
import harmless.model.Peripheral;
import harmless.model.Range;
import harmless.model.Register;
import harmless.model.Slice;

import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {


	public static final String PLUGIN_ID = "Harmless";

	// The shared instance
	private static Activator plugin;
	private Chargeur chargeur;
	private Updater updater;

	
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
		//TODO demander le port de façon plus "propre"
		InputDialog myDialog = new InputDialog(Display.getCurrent().getActiveShell(), 
												"Port", 
												"Veuillez entrer le port d'écoute:",
												"3239",
												new IInputValidator(){

													@Override
													public String isValid(
															String newText) {
														try
														{
															Integer.parseInt(newText);
														}
														catch(NumberFormatException e)
														{
															return "Entrez un nombre décimal.";
														}
														return null;
													}
			
												});
		myDialog.open();
		int port = 0;
		if(myDialog.getReturnCode() == Window.OK)
			port = Integer.parseInt(myDialog.getValue());
		else
		{
			try {
				getBundle().stop(Bundle.STOP_TRANSIENT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Socket socket = new Socket("localhost", port);
		chargeur = new Chargeur(socket);
		
		listePeripheriques = chargeur.initialiserPeripheriques();
		afficherEtat();
		Thread.sleep(10);
		updater = new Updater("localhost", port);
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
		listePeripheriques.get(1).getListeRegistres().get(1).setValeurHexa("21");
		for(Peripheral p: listePeripheriques)
		{
			System.out.println(p.getName() + ":");
			for(Register r: p.getListeRegistres())
			{
				System.out.println(" " + r.getId() + " : " + r.getDescription() + ", " + r.getAddress());
				System.out.println(" valeur: " + r.getValeurHexa());
				for(Slice s: r.getListeSlices())
				{
					System.out.println("  " + s.getId() + " : " + s.getDescription());
					System.out.println("  valeur : " + s.getValeur());
					for(Range range: s.getListeRanges())
					{
						System.out.println("   from " + range.getFrom() + " to " + range.getTo());
					}
					System.out.println("   nom de l'item: " + s.getItems().get(s.getValeur()));
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
	
	public Register getRegistre(String id) throws RegistreNonTrouveException
	{
		for(Peripheral p: listePeripheriques)
		{
			for(Register r: p.getListeRegistres())
			{
				if(r.getId().equals(id))
					return r;
			}
		}
		throw new RegistreNonTrouveException(id);
	}
	
	public static ImageDescriptor getImageDescriptor(String name) 
	{
	   String iconPath = "icons/";
	   try {
		   URL installURL = FileLocator.find(Platform.getBundle(PLUGIN_ID), new Path("/"), null);
		   URL url = new URL(installURL, iconPath + name);
	       return ImageDescriptor.createFromURL(url);
	   } catch (MalformedURLException e) {
	       // should not happen
	       return ImageDescriptor.getMissingImageDescriptor();
	   }
	}
}
