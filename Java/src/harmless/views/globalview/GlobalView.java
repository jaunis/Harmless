package harmless.views.globalview;


import harmless.Activator;
import harmless.controller.Updater;
import harmless.model.Bit;
import harmless.model.Register;
import harmless.views.UpdateSlicesView;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;



public class GlobalView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */

	public static final String ID = "harmless.views.GlobalView";

	private TreeViewer viewer;

	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;



	
	class NameSorter extends ViewerSorter {
	}


	/**
	 * The constructor.
	 */
	public GlobalView() {
	}
	
	public TreeViewer getViewer() {
		return viewer;
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		for(int i=0; i<=8; i++)
		{
			TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
			TreeColumn localColumn = tvc.getColumn();
			if(i==1)
			{
				GlobalViewEditionSupport editonSupport = new GlobalViewEditionSupport(tvc.getViewer());
				tvc.setEditingSupport(editonSupport);
			}
			localColumn.pack();
			if(i==0) localColumn.setWidth(150);
			else localColumn.setWidth(50);
			localColumn.setAlignment(SWT.CENTER);
		}
		viewer.getTree().setHeaderVisible(true);
		drillDownAdapter = new DrillDownAdapter(viewer);
		if(Activator.getDefault().getUpdater() != null)
		{
			viewer.setContentProvider(new GlobalViewContentProvider(this));
			viewer.setLabelProvider(new GlobalViewLabelProvider());
			viewer.setSorter(new NameSorter());
			viewer.setInput(getViewSite());
			// Create the help context id for the viewer's control
			//PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "Harmless.viewer");
			makeActions();
			//hookContextMenu();
			hookDoubleClickAction();
			contributeToActionBars();
			Activator.getDefault().getUpdater().signalerOuverture();
		}
		
	}

//	private void hookContextMenu() {
//		MenuManager menuMgr = new MenuManager("#PopupMenu");
//		menuMgr.setRemoveAllWhenShown(true);
//		menuMgr.addMenuListener(new IMenuListener() {
//			public void menuAboutToShow(IMenuManager manager) {
//				GlobalView.this.fillContextMenu(manager);
//			}
//		});
//		Menu menu = menuMgr.createContextMenu(viewer.getControl());
//		viewer.getControl().setMenu(menu);
//		getSite().registerContextMenu(menuMgr, viewer);
//	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		//fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

//	private void fillLocalPullDown(IMenuManager manager) {
//		manager.add(action1);
//		manager.add(new Separator());
//	}

//	private void fillContextMenu(IMenuManager manager) {
//		manager.add(action1);
//		manager.add(new Separator());
//		drillDownAdapter.addNavigationActions(manager);
//		// Other plug-ins can contribute there actions here
//		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {

		action1 = new Action(){
			public void run() {
				boolean cont = true;
				if(!Activator.getDefault().getUpdater().majEnvoyee())
				{
					cont = MessageDialog.openConfirm(viewer.getControl().getShell(), 
							"Mise à jour", 
							"Ceci écrasera les modifications non envoyées." + 
							"Etes-vous sûr de vouloir continuer?");
				}
				if(cont)
				{
					Updater updater = Activator.getDefault().getUpdater();
					updater.demanderReception();
					while(!updater.majRecue());
					viewer.refresh();
				}
			}
		};
		action1.setText("Mettre à jour");
		action1.setToolTipText("Demande au serveur le nouvel état du processeur");

		action1.setImageDescriptor(Activator.getImageDescriptor("refresh.gif"));
		
		action2 = new Action(){
			public void run()
			{
				Updater updater = Activator.getDefault().getUpdater();
				if(updater.majEnvoyee())
					System.out.println("Aucune mise à jour à envoyer.");
				else
				{
					updater.demanderEnvoi();
					while(!updater.majEnvoyee());
					System.out.println("Maj envoyée.");
				}
			}
		};
		
		action2.setText("Envoyer les modifications.");
		action2.setToolTipText("Envoi au serveur les modifications effectuées localement");
		action2.setImageDescriptor(Activator.getImageDescriptor("send.gif"));
		
		doubleClickAction = new Action() {
			public void run() {
				Point p = Display.getCurrent().getCursorLocation();
				Point pRelatif = Display.getCurrent().map(null, Display.getCurrent().getCursorControl(), p);
				ViewerCell cell = viewer.getCell(pRelatif);
				Object elem = cell.getElement();
				if(elem instanceof List<?>)
				{
					Bit bit = ((List<Bit>)elem).get(8 - cell.getColumnIndex());
					bit.setValeur(bit.getValeur()^1);
					Activator.getDefault().getUpdater().addMaj(bit.getRegistre());
					viewer.refresh();
				}
				else if(elem instanceof Register)
				{
					try 
					{
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(UpdateSlicesView.ID);
					} catch (PartInitException e) {
						showMessage("Erreur: impossible d'ouvrir la vue " + UpdateSlicesView.ID);
						e.printStackTrace();
					}
					//TODO dire à la vue quel registre afficher
				}
			}
		};
	}


	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Vue globale",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void dispose()
	{
		Activator activator = Activator.getDefault();
		if(activator.getUpdater() != null)
		{
			activator.getUpdater().signalerFermeture();
			
		}
		else
		{
			try {
				activator.getBundle().stop(Bundle.STOP_TRANSIENT);
			} catch (BundleException e) {
				System.err.println("Erreur à la fermeture du plugin.");
				e.printStackTrace();
			}
		}
			
		super.dispose();
		
	}
}