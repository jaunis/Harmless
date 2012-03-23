package harmless.views.slicesview;

import harmless.model.Register;
import harmless.model.Slice;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;



public class EditionSupportLabelProvider extends LabelProvider 
                                     implements ITableLabelProvider {
		
	
	
public Image getColumnImage(Object element, int columnIndex){
	return null;		
}


public String getColumnText(Object element, int columnIndex){
	if(element instanceof Slice){
		Slice myElement = (Slice) element;
		return myElement.getId();
		
	}
	if(element instanceof Register){
		Register myRegister = (Register) element;
		return myRegister.getId();			
	}
	
	return null;
}



}

