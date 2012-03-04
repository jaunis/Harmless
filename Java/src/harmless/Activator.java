package harmless;

import harmless.controller.Chargeur;
import harmless.controller.Updater;
import harmless.exceptions.RegistreNonTrouveException;
import harmless.model.Peripheral;
import harmless.model.Range;
import harmless.model.Register;
import harmless.model.Slice;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
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
	private Updater updater;
	
	private List<Peripheral> listePeripheriques;
	
	class DoubleInputDialog extends InputDialog
	{
		private String message2;
		private Text text2;
		private String value2;
		
		DoubleInputDialog(Shell parentShell, String dialogTitle,
	            String dialogMessage1, String initialValue1, IInputValidator validator1,
	            String dialogMessage2, String initialValue2) 
	    {
		
			super(parentShell, dialogTitle, dialogMessage1, initialValue1, validator1);
			
			this.message2 = dialogMessage2;
			if (initialValue2 == null) {
				value2 = "";//$NON-NLS-1$
			} else {
				value2 = initialValue2;
			}
		}
		
		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			if (message2 != null) {
	            Label label = new Label(composite, SWT.WRAP);
	            label.setText(message2);
	            GridData data = new GridData(GridData.GRAB_HORIZONTAL
	                    | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL
	                    | GridData.VERTICAL_ALIGN_CENTER);
	            data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
	            label.setLayoutData(data);
	            label.setFont(parent.getFont());
	        }
	        text2 = new Text(composite, getInputTextStyle());
	        text2.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
	                | GridData.HORIZONTAL_ALIGN_FILL));
	        text2.setText(value2);
			return composite;
		}
		
		public String getValue1()
		{
			return super.getValue();
		}
		
		public String getValue2()
		{
			return value2;
		}
		protected void buttonPressed(int buttonId) {
	        if (buttonId == IDialogConstants.OK_ID) {
	            value2 = text2.getText();
	        } else {
	            value2 = null;
	        }
	        super.buttonPressed(buttonId);
	    }
	}

	
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
		
		plugin = this;
		boolean cont = true;
		String defaultMessage1 = "Veuillez entrer le port d'écoute:";
		String defaultMessage2 = "Veuillez entrer le nom de l'hôte:";
		String dialogMessage1 = defaultMessage1;
		String dialogMessage2 = defaultMessage2;
		int defaultPort = 3239;
		String defaultHost = "localhost";
		while(cont)
		{
			DoubleInputDialog myDialog = new DoubleInputDialog(Display.getCurrent().getActiveShell(), 
												"Informations de connexion", 
												dialogMessage1,
												Integer.toString(defaultPort),
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
			
												}, 
												dialogMessage2, 
												defaultHost);
		
			myDialog.open();
			int port = 0;
			String host = "";
			if(myDialog.getReturnCode() == Window.OK)
			{
				port = Integer.parseInt(myDialog.getValue1());
				host = myDialog.getValue2();
				Socket socket = null;
				boolean echec = false;
				try {
					socket = new Socket(host, port);
				}
				catch(UnknownHostException e)
				{
					dialogMessage2 = "Hôte inconnu. Vérifiez que le nom entré est correct.";
					dialogMessage1 = defaultMessage1;
					defaultHost = host;
					defaultPort = port;
					echec = true;
				}
				catch(IOException e)
				{
					dialogMessage1 = "Impossible d'ouvrir une socket sur le port donné. " + 
								"Vérifiez que le serveur est lancé, et que vous avez entré " + 
								"le bon port, puis réessayez.";
					dialogMessage2 = defaultMessage2;
					defaultPort = port;
					defaultHost = host;
					echec = true;
				}
				
				if(!echec)
				{
					chargeur = new Chargeur(socket);
					listePeripheriques = chargeur.initialiserPeripheriques();
					afficherEtat();
					Thread.sleep(10);
					//updater = new Updater(host, port);
					updater = new Updater(socket);
					updater.start();	
					cont = false;
				}
			}	
			else cont = false;
		}
		super.start(context);
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
