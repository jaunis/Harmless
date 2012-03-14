package harmless.views.globalview;

import harmless.Activator;
import harmless.model.Register;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class GlobalViewEditionSupport extends EditingSupport{

	private TextCellEditor cellEditor = null;
     
	public GlobalViewEditionSupport(ColumnViewer viewer) {
        super(viewer);
        cellEditor = new TextCellEditor((Composite) getViewer().getControl(), SWT.LEFT);        
    }
     
    @Override
    protected CellEditor getCellEditor(Object element) {
        if(element instanceof Register) return cellEditor;
        else return null;
    }
     
    @Override
    protected boolean canEdit(Object element) {
        if(element instanceof Register) return true;
        else return false;
    }
     
    @Override
    protected Object getValue(Object element) {
        if(element instanceof Register)	return ((Register)element).getValeurHexa();
        else return null;
    }
     
    
    protected void setValue(Object element, Object value){
    	if(element instanceof Register)
		{
    		try
    		{
    			((Register)element).setValeurHexa((String)value);
    			Activator.getDefault().getUpdater().addMaj((Register)element);
        		getViewer().refresh();
    		}
    		catch(NumberFormatException e)
    		{
    			MessageDialog.openInformation(getViewer().getControl().getShell(),
    					"Erreur - Vue globale", "Nombre hexadécimal invalide");
    		}
    		
		}
    }
}
