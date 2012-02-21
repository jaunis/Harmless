package harmless.controller;

import harmless.Activator;

import java.util.Scanner;

public class EntreeStandard extends Thread 
{
	private Scanner sc;
	private Updater updater;
	
	public EntreeStandard()
	{
		sc = new Scanner(System.in);
		updater = Activator.getDefault().getUpdater();
	}
	public void run()
	{
		while(true)
		{
			String ordre = sc.nextLine();
			if(ordre.equals("update"))
				updater.demanderReception();
			while(!updater.majRecue());
			System.err.println("Une mise à jour effectuée:");
			Activator.getDefault().afficherEtat();
		}
	}
}
