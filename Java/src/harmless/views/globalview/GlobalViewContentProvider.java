package harmless.views.globalview;

import harmless.Activator;
import harmless.model.Bit;
import harmless.model.Peripheral;
import harmless.model.Register;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/*
 * The content provider class is responsible for
 * providing objects to the view. It can wrap
 * existing objects in adapters or simply return
 * objects as-is. These objects may be sensitive
 * to the current input of the view, or ignore
 * it and always show the same content 
 * (like Task List, for example).
 */
class GlobalViewContentProvider implements IStructuredContentProvider, 
   										ITreeContentProvider 
   {
		/**
	 * 
	 */
	private final GlobalView globalView;
		public GlobalViewContentProvider(GlobalView globalView)
		{
			this.globalView = globalView;}
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if(parent.equals(this.globalView.getViewSite()))
				return Activator.getDefault().getListePeripheriques().toArray();
			return getChildren(parent);
		}
		@SuppressWarnings("unchecked")
		public Object getParent(Object child) {
			if(child instanceof Peripheral)
				return null;
			else if(child instanceof Register)
				return ((Register)child).getPeripherique();
			else if(child instanceof List<?>)
				return ((List<Bit>)child).get(0).getRegistre();
			else
				return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof Peripheral) 
				return ((Peripheral)parent).getListeRegistres().toArray();
			else if(parent instanceof Register)
				return new Object[] {((Register)parent).getListeBits()};
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof Peripheral)
				return !((Peripheral)parent).getListeRegistres().isEmpty();
			else if (parent instanceof Register)
				return true;
			else if(parent.equals(this.globalView.getViewSite()))
				return true;
			return false;
		}
		
	}