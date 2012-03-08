package harmless.views.slicesview;

import harmless.Activator;
import harmless.controller.Updater;
import harmless.exceptions.RegistreNonTrouveException;
import harmless.views.communs.NameSorter;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;



public class SlicesView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "harmless.views.SlicesView";

	private TableViewer viewer;
	//private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action doubleClickAction;


	/**
	 * The constructor.
	 */
	public SlicesView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		 
		Table exampleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		exampleTable.setLinesVisible(true);
		exampleTable.setHeaderVisible(true);
		exampleTable.setLayout(tableLayout);
		 
		viewer = new TableViewer(exampleTable);
		viewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		 
		TableViewerColumn labelColumn = new TableViewerColumn(viewer, SWT.NONE);
		labelColumn.getColumn().setText("Label");
		
		TableViewerColumn valueColumn = new TableViewerColumn(viewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		EditionSupport editionSupport = new EditionSupport(valueColumn.getViewer(), this);
		valueColumn.setEditingSupport(editionSupport);
		
		TableViewerColumn checkColumn = new TableViewerColumn(viewer, SWT.NONE);
		labelColumn.getColumn().setText(" ");

		try {
			viewer.setContentProvider(new SlicesViewContentProvider(this, Activator.getDefault().getRegistre("UCSR0C")));
		} catch (RegistreNonTrouveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewer.setLabelProvider(new SlicesViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
        

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
				SlicesView.this.fillContextMenu(manager);
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
		//drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		//drillDownAdapter.addNavigationActions(manager);
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
		action1.setImageDescriptor(Activator.getImageDescriptor("refresh.gif"));
		
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
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
	
	public TableViewer getViewer()
	{
		return viewer;
	}
}