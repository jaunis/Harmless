package harmless.views.GlobalView;

import harmless.model.Peripheral;
import harmless.model.Register;
import harmless.Activator;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
=======
import harmless.views.communs.NameSorter;
import harmless.views.communs.ViewLabelProvider;

import java.util.ArrayList;
import java.util.List;

>>>>>>> 1fdd69d87832cc416a8d80ea9988d8e71c4eb3ea
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
<<<<<<< HEAD
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
=======
import org.eclipse.jface.viewers.TreeViewerColumn;
>>>>>>> 1fdd69d87832cc416a8d80ea9988d8e71c4eb3ea
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;




public class GlobalView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testvue.views.SampleView";
	
	public List<Peripheral> listePeripheriques;

	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private List<TreeViewerColumn> listeColonnes = new ArrayList<TreeViewerColumn>();
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
<<<<<<< HEAD


	 
	class TreeObject implements IAdaptable {
		private String name;
		private String s = "hehejej";
		private TreeParent parent;
		
		public TreeObject(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String getS(){
			return s;
		}
		public void setParent(TreeParent parent) {
			this.parent = parent;
		}
		public TreeParent getParent() {
			return parent;
		}
		public String toString() {
			return getName();
		}
		public Object getAdapter(Class key) {
			return null;
		}
	}
	
	class TreeParent extends TreeObject {
		private ArrayList children;
		public TreeParent(String name) {
			super(name);
			children = new ArrayList();
		}
		public void addChild(TreeObject child) {
			children.add(child);
			child.setParent(this);
		}
		public void removeChild(TreeObject child) {
			children.remove(child);
			child.setParent(null);
		}
		public TreeObject [] getChildren() {
			return (TreeObject [])children.toArray(new TreeObject[children.size()]);
		}
		public boolean hasChildren() {
			return children.size()>0;
		}
	}

	class ViewContentProvider implements IStructuredContentProvider, 
										   ITreeContentProvider {
		private TreeParent invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObject) {
				return ((TreeObject)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof TreeParent) {
				return ((TreeParent)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof TreeParent)
				return ((TreeParent)parent).hasChildren();
			return false;
		}
/*
 * We will set up a dummy model to initialize tree hierarchy.
 * In a real code, you will connect to a real model and
 * expose its hierarchy.
 */
		private void initialize() {
			TreeObject to1 = new TreeObject("bla");
			TreeObject to2 = new TreeObject("Leaf 2");
			TreeObject to3 = new TreeObject("Leaf 3");
			TreeParent p1 = new TreeParent("Periph 1");
			p1.addChild(to1);
			p1.addChild(to2);
			p1.addChild(to3);
			
			TreeObject to4 = new TreeObject("Leaf 4");
			TreeParent p2 = new TreeParent("Periph 2");
			p2.addChild(to4);
			
			TreeParent root = new TreeParent("Peripherals");
			root.addChild(p1);
			root.addChild(p2);
			
			invisibleRoot = new TreeParent("");
			invisibleRoot.addChild(root);
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getText(Object obj) {
			return obj.toString();
		}
		public Image getImage(Object obj) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			if (obj instanceof TreeParent)
			   imageKey = ISharedImages.IMG_OBJ_FOLDER;
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			
			return null;
		}
		@Override
		//objet pour l'objet en question et index pour l'index de column
		public String getColumnText(Object element, int index) {
			if(index == 0) return element.toString();
			else if(index == 1) return ((TreeObject)element).getS();
			else if(index == 2) return ("bla bla");
			return null;
		}
		
	}
	
	class NameSorter extends ViewerSorter {
	}
=======
	
>>>>>>> 1fdd69d87832cc416a8d80ea9988d8e71c4eb3ea

	/**
	 * The constructor.
	 */
	public GlobalView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Tree tree = viewer.getTree();
		TreeColumn col0 = new TreeColumn(tree, SWT.NONE);
		col0.setText("col0");
		TreeColumn col1 = new TreeColumn(tree, SWT.NONE);
		col1.setText("col1");
		TreeColumn col2 = new TreeColumn(tree, SWT.NONE);
		col2.setText("col2");
		TreeColumn col3 = new TreeColumn(tree, SWT.NONE);
		col3.setText("col3");
		TreeColumn col4 = new TreeColumn(tree, SWT.NONE);
		col4.setText("col4");
		TreeColumn col5 = new TreeColumn(tree, SWT.NONE);
		col5.setText("col5");
		TreeColumn col6 = new TreeColumn(tree, SWT.NONE);
		col6.setText("col6");
		TreeColumn col7 = new TreeColumn(tree, SWT.NONE);
		col7.setText("col7");
		TreeColumn col8 = new TreeColumn(tree, SWT.NONE);
		col8.setText("col8");
		
		tree.setHeaderVisible(true);
		drillDownAdapter = new DrillDownAdapter(viewer);
<<<<<<< HEAD
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		col0.pack();
		col1.pack();
		col2.pack();
		col3.pack();
		col4.pack();
		col5.pack();
		col6.pack();
		col7.pack();
		col8.pack();
		
=======
		
		for(int i=0; i<=8; i++)
		{
			listeColonnes.add(new TreeViewerColumn(viewer, SWT.NONE));
		}
		
		for(TreeViewerColumn tvc: listeColonnes)
		{
			tvc.getColumn().pack();
		}
		
		viewer.setContentProvider(new GlobalViewContentProvider(this));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		viewer.getTree().setHeaderVisible(true);

>>>>>>> 1fdd69d87832cc416a8d80ea9988d8e71c4eb3ea
		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "TestVue.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
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
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		
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
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
}