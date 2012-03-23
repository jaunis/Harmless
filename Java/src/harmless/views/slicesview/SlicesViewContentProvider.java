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
		
		if(this.slicesView == null||registreRacine == null)
			return new Object[0];
		else
			//return new Object[]{this.registreRacine};
			return registreRacine.getListeSlices().toArray();			
	}
	
	
	public void setRegistre(Register registre){
		registreRacine = registre;
		
	}

	
}