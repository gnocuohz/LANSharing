package team.wwg.lansharing.main;

import javax.swing.SwingUtilities;

import team.wwg.lansharing.service.ServerControl;
import team.wwg.lansharing.ui.MainWnd;

public class Main {

	public static void main(String[] args) {
		new Thread(new ServerControl()).start();
		SwingUtilities.invokeLater(new Runnable() 
		{  
		      @Override 
		      public void run() 
		      {
		    	  MainWnd mainWnd = new MainWnd();
		      }  
		});  
	}

}
