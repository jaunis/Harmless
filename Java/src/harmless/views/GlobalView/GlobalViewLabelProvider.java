package harmless.views.GlobalView;

import harmless.model.Bit;
import harmless.model.Peripheral;
import harmless.model.Register;

import java.util.List;

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
		if(columnIndex == 0)
			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		else
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
				return "(" + myElement.getAddress() + "):";
			case 2:
				return myElement.getValeurHexa();
			default:
				return null;
			}
		}
		else if(element instanceof List<?>)
		{
			List<Bit> myElement = null;
			try
			{
				myElement = (List<Bit>) element;
				if(columnIndex >=1 && columnIndex <= 8)
					return myElement.get(columnIndex-1).toString();
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
}