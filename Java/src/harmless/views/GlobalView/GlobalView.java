package harmless.views.GlobalView;


import harmless.Activator;
import harmless.controller.Updater;
import harmless.model.Bit;
import harmless.views.communs.NameSorter;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class GlobalView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "harmless.views.GlobalView";

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private List<TreeViewerColumn> listeColonnes = new ArrayList<TreeViewerColumn>();
	private Action action1;
	private Action doubleClickAction;
	

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
			listeColonnes.add(new TreeViewerColumn(viewer, SWT.NONE));
		}
		
		for(TreeViewerColumn tvc: listeColonnes)
		{
			TreeColumn localColumn = tvc.getColumn();
			localColumn.pack();
			localColumn.setWidth(100);
			localColumn.setAlignment(SWT.CENTER);
		}
		viewer.getTree().setHeaderVisible(true);
		drillDownAdapter = new DrillDownAdapter(viewer);
		
		viewer.setContentProvider(new GlobalViewContentProvider(this));
		viewer.setLabelProvider(new GlobalViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "Harmless.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		Activator.getDefault().getUpdater().signalerOuverture();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				GlobalView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		//manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				Updater updater = Activator.getDefault().getUpdater();
				updater.demanderReception();
				while(!updater.majRecue());
				viewer.refresh();
			}
		};
		action1.setText("Mettre à jour");
		action1.setToolTipText("Demande au serveur le nouvel état du processeur");
		action1.setImageDescriptor(GlobalViewLabelProvider.getImageDescriptor("refresh.gif"));
		
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
					viewer.refresh();
				}
				else
				{
					showMessage("Double-click detected on " + elem.toString());
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
		Activator.getDefault().getUpdater().signalerFermeture();
	}
}