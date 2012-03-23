package harmless.views.slicesview;

import harmless.Activator;
import harmless.model.BitManager;
import harmless.model.Slice;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class SlicesViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	
	public String getText(Object obj) {
		return obj.toString();
	}
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	
	
	public String getColumnText(Object element, int columnIndex){
		if(element instanceof Slice){
			Slice bla = (Slice) element;
		
	    switch (columnIndex) {
	      case 0:
	        return bla.getId();
	      case 1:
	    	  if(bla.getListeItem().isEmpty())
	    		  return "";
			else
			{
				try {
					return bla.getItem(bla.getValeur()).toString();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

	      default:
	        return "";
	      }
	    
		}
		
		return null;
	}
	
	
	
	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex == 1){
			if(element instanceof Slice){
			   Slice bla = (Slice) element;
			   //presence d'une image carre noir si et seulement si il n y a pas d item pour ce slice
			   if(((Slice) element).getListeItem().size() ==0){
				   //Un Slice est un BitManager par polymorphisme et heritage
				   //Si la liste de slice est vide, le slice represente un bit, par consequent on a acces à cette valeur en regardant
				   //la valeur de l'objet BitManager
				   BitManager bloup = (BitManager) bla;
				   
				   ImageDescriptor descriptor = null;
				   descriptor =  bloup.getValeur() == 0 ?
						        Activator.getImageDescriptor("bit_off.png"):
								Activator.getImageDescriptor("bit_on.png");
					return descriptor.createImage();
				                                              }	
			                             }
		                       }
		return null;
	}	
}
