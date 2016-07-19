package ass03.sol;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Viewer extends JFrame  {	
	private ViewerPanel panel;
	
	public Viewer(ExtPointCloud cloud){
		setTitle("Viewer");
		setSize(400,400);
		setPreferredSize(new Dimension(400,400));
		panel = new ViewerPanel(cloud,400,400,100);
		this.getContentPane().add(panel);
		pack();
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				System.exit(0);		
			}
		});
	}
	
	public void display(){
		setVisible(true);
	}
}
