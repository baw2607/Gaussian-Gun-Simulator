package gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import graphics.Diagram;
import logic.GlobalVars;

public class MainScreenPanel extends JPanel 
{
	GlobalVars vars = new GlobalVars();
	
	public MainScreenPanel() 
	{
		init();

	} // End Constructor

	private void init()
	{
		Diagram shape = new Diagram();
		setPreferredSize(vars.frameDimension);

		JLabel lbl = vars.lblGaussianGunSimulation;
		lbl.setFont(new Font("Tahoma", Font.PLAIN, 27));

		add(lbl);
		add(shape);
	}

} // End Class
