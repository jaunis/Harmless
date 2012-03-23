package harmless.views.slicesview;

import harmless.Activator;
import harmless.controller.Updater;
import harmless.model.BitManager;
import harmless.model.Register;
import harmless.model.Slice;
import harmless.views.globalview.GlobalView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;



public class SlicesView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "harmless.views.SlicesView";

	private TableViewer viewer;
	private Action action1;
	private Action doubleClickAction;
	//private Slice slice;
	//pour un slice donne, on associe un slicesView

	private Action action2;
	


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
		EditionSupport editionSupport = new EditionSupport(valueColumn.getViewer());
		valueColumn.setEditingSupport(editionSupport);
			
		
				 
		viewer.setContentProvider(new SlicesViewContentProvider(this, null));
		viewer.setLabelProvider(new SlicesViewLabelProvider());
		viewer.setInput(getViewSite());
        

		makeActions();
		hookDoubleClickAction();
		contributeToActionBars();
		Activator.getDefault().getUpdater().signalerOuverture();
	}


	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}




	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
	}

	private void makeActions() {
		action1 = new Action() {
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
					((GlobalView)PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.findView(GlobalView.ID)).getViewer().refresh();
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
				if(elem instanceof Slice){
					Slice slice = (Slice) elem;
					//je n'autorise une action par double clique que pour un objet slice, pour lequel il n a pas d item
					if(slice.getListeItem().size() == 0){
						BitManager bitmanage = (BitManager) slice;
						//conversion de slice en element de sa classe mere
						bitmanage.getListeBits().get(0).setValeur(1 - bitmanage.getListeBits().get(0).getValeur());
						//ainsi si le bit vaut 0 il vaut 1 et inversement
						//bitmanage.getListeBits().get(0); est un objet Bit
						Activator.getDefault().getUpdater().addMaj(bitmanage.getListeBits().get(0).getRegistre());
						viewer.refresh();
						((GlobalView)PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.findView(GlobalView.ID)).getViewer().refresh();
						
					}
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
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void setRegistre(Register registre){
		SlicesViewContentProvider cp = (SlicesViewContentProvider)viewer.getContentProvider();
		cp.setRegistre(registre);
	}
	
	
	public TableViewer getViewer()
	{
		return viewer;
	}
}