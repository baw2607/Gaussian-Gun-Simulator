package gui;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import logic.GlobalVars;

public class TutorialPanel extends JPanel 
{
	private JTextPane pane;
	
	private GlobalVars vars = new GlobalVars();
	
	public TutorialPanel()
	{
		init();
	}
	
	public void init()
	{
		JLabel lblMain = vars.lblGaussianGunSimulation;
		lblMain.setFont(new Font("Tahoma", Font.PLAIN, 27));
		
		String message = "1) Click the Simulator button"
				+ "\n2) Enter parameters you want to use to simulate the experiment"
				+ "\n3) Press Help if you are not sure. When done, press Run and wait for confirm message"
				+ "\n4) Next, click Show results and update the table"
				+ "\n5) Click show graph and update the graph. When done, remember to "
				+ "\nclear the graph and clear results"
				+ "\n6) If you want to save the file then do not clear results and "
				+ "check the save file \ncheck box on exit";
		
		pane = new JTextPane();
		
		pane.setFont(new Font("Dialog", Font.PLAIN, 20));
		pane.setText(message);
		pane.setEditable(false);
		
		add(lblMain);
		add(pane);
	}
}
