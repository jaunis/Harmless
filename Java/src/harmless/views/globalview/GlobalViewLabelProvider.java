package harmless.views.globalview;

import harmless.Activator;
import harmless.model.Bit;
import harmless.model.Peripheral;
import harmless.model.Register;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class GlobalViewLabelProvider extends LabelProvider 
								implements ITableLabelProvider {

	
	public String getText(Object obj) {
		return obj.toString();
	}
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		String imageKey = ISharedImages.IMG_OBJ_FOLDER;
		if(columnIndex == 0 && !(element instanceof List))
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
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof Peripheral)
		{
			Peripheral myElement = (Peripheral) element;
			switch(columnIndex)
			{
			case 0:
				return myElement.getName();
			default:
				return null;
			}
		}
		else if(element instanceof Register)
		{
			Register myElement = (Register) element;
			switch(columnIndex)
			{
			case 0:
				return myElement.getId();
			case 1:
				return myElement.getValeurHexa();
			default:
				return null;
			}
		}
		else if(element instanceof List<?>)
		{
			try{
				List<Bit> myElement = (List<Bit>) element;
				switch(columnIndex)
				{
				case 0:
					return "(" + myElement.get(0).getRegistre().getAddress() + ")";
				default:
					return null;
				}
			}
			catch(ClassCastException e)
			{
				System.err.println("Une liste de bits est attendue.");
				e.printStackTrace();
			}
		}
		return null;
	}
}