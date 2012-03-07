package harmless.views.slicesview;

import harmless.model.Register;
import harmless.model.Slice;
import harmless.model.Item;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


public final class EditionSupport extends EditingSupport {
    
    private ComboBoxViewerCellEditor cellEditor = null;
    private final SlicesView slicesView;
     
    public EditionSupport(ColumnViewer viewer, final SlicesView slicesView) {
        super(viewer);
        this.slicesView = slicesView;
        cellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
        //cellEditor.setLabelProvider(new LabelProvider());
        //cellEditor.setContenProvider(new ArrayContentProvider());
        cellEditor.setLabelProvider(new EditionSupportLabelProvider());
        cellEditor.setContenProvider(new EditionSupportContentProvider());
        cellEditor.setInput(slicesView.getViewSite());
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
            IStatusLineManager lineManager = slicesView.getViewSite().getActionBars().getStatusLineManager();
              lineManager.setErrorMessage(text);
        			            }
            
            public void applyEditorValue() {
   	                setErrorText(null); 
      			            }
        
        });
        
    }
     
    @Override
    protected CellEditor getCellEditor(Object element) {
        return cellEditor;
    }
     
    @Override
    protected boolean canEdit(Object element) {
        return true;
    }
     
    @Override
    //elements qui apparaitront dans la Jcombobox
    protected Object getValue(Object element) {
        if (element instanceof Item) {
            Item item = (Item)element;
            return item;
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
    /*
    @Override
    protected void setValue(Object element, Object value) {
        if (element instanceof Slice && value instanceof Slice) {
            Slice data = (Slice) element;
           // Value newValue = (Value) value;
            Slice newSlice = (Slice) value;
            /* only set new value if it differs from old one 
            //if(!data.equals(newSlice);
            //   data.setSlice(newSlice);
            //if (!data.getData().equals(newValue)) {
              //  data.setData(newValue);
            //}
        }
    }*/
    
     
}