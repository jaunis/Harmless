package harmless.views.slicesview;


import harmless.model.Item;
import harmless.model.Slice;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


public final class EditionSupport extends EditingSupport {
    
    private ComboBoxViewerCellEditor cellEditor = null;

    private CheckboxCellEditor check = null;

    private SlicesView slicesView;
     
	public EditionSupport(ColumnViewer viewer, SlicesView slicesView) {
        super(viewer);
        this.slicesView = slicesView;
        
        //check = new CheckboxCellEditor((Composite) getViewer().getControl(), SWT.PUSH);
                
        
        
        cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
        //cellEditor.setLabelProvider(new LabelProvider());
        cellEditor.setContenProvider(new ArrayContentProvider());
        cellEditor.setLabelProvider(new EditionSupportLabelProvider());
        //cellEditor.setContenProvider(new EditionSupportContentProvider());

        //cellEditor.setInput(slicesView.getViewSite());
//        try {
//			cellEditor.setInput(Activator.getDefault().getRegistre("UCSR0A"));
//		} catch (RegistreNonTrouveException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        cellEditor.addListener(new ICellEditorListener(){
        	public void editorValueChanged(boolean oldValidState, boolean newValidState) {
        			                if (newValidState)
        			                    setErrorText(null);
        			                else
        			                    setErrorText("error");
        			                
        			            }

        	 public void cancelEditor() {
                   setErrorText(null);
        			            }

            private void setErrorText(String text) {
            IStatusLineManager lineManager = EditionSupport.this.slicesView.getViewSite().getActionBars().getStatusLineManager();
              lineManager.setErrorMessage(text);
        			            }
            
            public void applyEditorValue() {
   	                setErrorText(null); 
   	                EditionSupport.this.slicesView.getViewer().refresh();
      			            }
        });
        
    }
     
    @Override
    protected CellEditor getCellEditor(Object element) {
    	//cellEditor
    	if (element instanceof Slice){
    		Slice slice = (Slice) element;
    		if(slice.getListeItem().size() != 0){
    			cellEditor.setInput(((Slice)element).getListeItem());
    		    return cellEditor;  	
    		    }
    		     else{
    			      return check;
    		         }
    	}	
    	else return null;
    		    		
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
            	if(((Slice) element).getListeItem().size() == 0) return null;
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
    		
    	}
    	
    	
    }
    
     
}