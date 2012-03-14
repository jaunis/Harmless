package harmless.views.slicesview;

import harmless.model.Slice;

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
	@Override
	
	public Image getColumnImage(Object element, int columnIndex){
		return null;		
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
	
	
	
	/*public Image getColumnImage(Object element, int columnIndex) {
		String imageKey = ISharedImages.IMG_OBJ_FOLDER;
		if(columnIndex == 0)
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		else if(element instanceof List<?>)
		{
			List<Bit> myElement = null;
			try
			{
				ImageDescriptor descriptor = null;
				myElement = (List<Bit>) element;
				if(columnIndex >=1 && columnIndex <= 8)
				{
					descriptor = (myElement.get(8 - columnIndex).getValeur()==0?
								Activator.getImageDescriptor("bit_off.png"):
								Activator.getImageDescriptor("bit_on.png"));
					return descriptor.createImage();
				}
				else return null;
			}
			catch(ClassCastException e)
			{
				System.err.println("Une liste de bits est attendue.");
				e.printStackTrace();
			}
		}
		return null;
	}*/
	
	/*
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof Register)
		{
			Register myElement = (Register) element;
			switch(columnIndex)
			{
			case 0:
				return myElement.getId();
			case 1:
				return "(" + myElement.getAddress() + "):";
			case 2:
				return myElement.getValeurHexa();
			default:
				return null;
			}
		}
		else if(element instanceof Slice)
		{
			Slice myElement = (Slice) element;
//			switch
		}
		return null;
	}*/
}