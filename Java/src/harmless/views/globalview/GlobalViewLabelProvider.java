package harmless.views.globalview;

import harmless.Activator;
import harmless.model.Bit;
import harmless.model.Register;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class GlobalViewLabelProvider extends LabelProvider 
								implements ITableLabelProvider {

	/*
	 * Dans les deux méthodes ci-dessous, l'indice de colonne 0 n'est pas pris en compte.
	 * En effet cette colonne est gérée par le CellLabelProvider créé dans GlobalView
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
				
		if(element instanceof List<?>)
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
		if(element instanceof Register && columnIndex == 1)
		{
			Register myElement = (Register) element;
			return myElement.getValeurHexa();
			
		}
		return null;
	}
	

}