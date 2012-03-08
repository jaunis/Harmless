package harmless.views.slicesview;

import harmless.model.Register;

import org.eclipse.jface.viewers.IStructuredContentProvider;
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


class SlicesViewContentProvider implements IStructuredContentProvider
//,ITreeContentProvider
   {
		/**
		 * 
		 */
		private final SlicesView slicesView;
		private Register registreRacine;
		
		public SlicesViewContentProvider(SlicesView slicesView, Register registre)
		{
			this.slicesView = slicesView;
			this.registreRacine = registre;
		}
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		
		
		public Object[] getElements(Object parent) {
			
			if(this.slicesView == null)
				return new Object[0];
			else
				//return new Object[]{this.registreRacine};
				return registreRacine.getListeSlices().toArray();
			
		}
		
		/*
		public Object[] getElements(Object parent) {
			if(parent.equals(this.slicesView.getViewSite()))
			{
				if(this.slicesView == null)
					return new Object[0];
				else
					return new Object[]{this.registreRacine};
			}
			return getChildren(parent);
		}*/
		
		
		/*
		public Object getParent(Object child) {
			if(child instanceof Register)
				return null;
			else if(child instanceof Slice)
				return ((Slice)child).getRegistre();
			else
				return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof Register) 
				return ((Register)parent).getListeSlices().toArray();
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof Register)
				return !((Register)parent).getListeSlices().isEmpty();
			else if(parent.equals(this.slicesView.getViewSite()))
					return true;
			return false;
		}*/
		
	}