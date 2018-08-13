package graphics;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Diagram extends JPanel
{
	
	// Static Diagram
    public Diagram()
    {
    	setPreferredSize(new Dimension(1000, 150));
    	
    	ClassLoader loader = this.getClass().getClassLoader();
    	ImageIcon diag = new ImageIcon(loader.getResource("graphics/diagram_new.png"));
    	
    	JLabel lbl = new JLabel(diag);
    	
    	add(lbl);
    }

}
