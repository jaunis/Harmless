package harmless.views.slicesview;


import harmless.Activator;
import harmless.model.Item;
import harmless.model.Slice;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


public final class EditionSupport extends EditingSupport {
    
    private ComboBoxViewerCellEditor cellEditor = null;

    private SlicesView slicesView;
     
	@SuppressWarnings("deprecation")
	public EditionSupport(ColumnViewer viewer, SlicesView slicesView) {
        super(viewer);
        this.slicesView = slicesView;

        
        cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
        cellEditor.setContenProvider(new ArrayContentProvider());
        
    }
     
    @Override
    protected CellEditor getCellEditor(Object element) {
    	if (element instanceof Slice){
    		Slice slice = (Slice) element;
    		if(slice.getListeItem().size() != 0){
    			cellEditor.setInput(((Slice)element).getListeItem());
    		    return cellEditor;  	
    		    }
    	     }
    	return null;    		
    }
     
    @Override
    protected boolean canEdit(Object element) {
        return true;
    }
     
    @Override
    //elements qui apparaitront dans la Jcombobox
    protected Object getValue(Object element) {
        if (element instanceof Slice) {
            Slice slice = (Slice)element;
            try {
            	if(slice.getListeItem().size() == 0) return (slice.getValeur() > 0);
            	else return slice.getItem(slice.getValeur());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
        }
        return null;
    }
     
    
    protected void setValue(Object element, Object value){
    	if (element instanceof Slice && value instanceof Item){
    		Slice data = (Slice) element;
    		Item newValue = (Item) value;
    	    data.setValeur(newValue.getValeur());
    	     
    	    Activator.getDefault().getUpdater().addMaj(((Slice)element).getRegistre());
    		getViewer().refresh();    		
    	}
    }
    
     
}