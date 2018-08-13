package gui;

import java.awt.*;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import javax.swing.SpringLayout;

import com.oracle.SpringUtils.SpringUtilities;

import net.miginfocom.swing.MigLayout;
import net.miginfocom.layout.ComponentWrapper;

import graphics.Diagram;
import logic.GlobalVars;
import logic.Simulation;

public class SimulationPanel extends JPanel 
{
	// States
	private JTextField strength;
	private JTextField distance;
	private JTextField noMagnets;

	private JLabel lblStreng;
	private JLabel lblDist;
	private JLabel lblNoMag;
	private JLabel lblSpin;
	private JLabel lblSuccess;
	
	private JSlider repeatSlider;

	private JButton btnHelpButton;
	private JButton btnRun;

	private JPanel mainPanel;
	private JPanel optPanel;

	private Diagram diagram;
	private Simulation sim;
	private GlobalVars vars = new GlobalVars();

	private boolean spinnerChanged;

	//Constructor
	public SimulationPanel() 
	{
		init();
	}

	// Init
	private void init()
	{
		sim = new Simulation();
		spinnerChanged = false;

		mainPanel = new JPanel();
		mainPanel.setLayout(new MigLayout());
		mainPanel.setPreferredSize(vars.frameDimension);

		optPanel = new JPanel();
		optPanel.setLayout(new SpringLayout());

		diagram = new Diagram();

		JLabel lblMain = vars.lblGaussianGunSimulation;
		lblMain.setFont(new Font("Tahoma", Font.PLAIN, 27));

		strength = new JTextField(20);
		lblStreng = new JLabel("Strength of Magnets: ", JLabel.TRAILING);
		lblStreng.setLabelFor(strength);

		distance = new JTextField(20);
		lblDist = new JLabel("Distance between Magnets: ", JLabel.TRAILING);
		lblDist.setLabelFor(distance);

		noMagnets = new JTextField(20);
		lblNoMag = new JLabel("Number of Magnets: ", JLabel.TRAILING);
		lblNoMag.setLabelFor(noMagnets);
		
		repeatSlider = new JSlider(JSlider.HORIZONTAL, 1, 3, 1);
		repeatSlider.addChangeListener(e -> setSliderChanged(true));
		repeatSlider.setMajorTickSpacing(1);
		
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put( new Integer(1), new JLabel("1") );
		labelTable.put( new Integer(2), new JLabel("2") );
		labelTable.put( new Integer(3), new JLabel("3") );
		
		repeatSlider.setLabelTable(labelTable);
		lblSpin = new JLabel("Repetitions: ", JLabel.TRAILING);
		
		repeatSlider.setPaintTicks(true);
		repeatSlider.setPaintLabels(true);
		
		lblSuccess = new JLabel("");

		btnHelpButton = new JButton("Help");
		btnHelpButton.addActionListener(e -> displayHelpDialog());

		btnRun = new JButton("Run");
		btnRun.addActionListener(e -> {

			if(isEmpty() && !spinnerChanged)
			{
				JOptionPane.showMessageDialog(null,
						"Please enter parameters and Run again",
						"Parameter Error", JOptionPane.ERROR_MESSAGE);
				clearParameters();
			}
			else 
			{
				double s = getUserStrength();
				double d = getUserDistance();
				int n = getUserNoMagnets();
				int r = getUserRepetition();
				
				if(vars.cleared)
				{

					if((s < 6 && s > 0) && (d < 6 && d > 0) && (n < 11 && s > 0))
					{
						sim.setValues(s, d, n, r);
						sim.run();
						clearParameters();

						lblSuccess.setText("Simulation RUN Successfully, Click show results");
						
						spinnerChanged = false;
						vars.cleared = false;
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								"Please enter parameters and run again, Help is available if needed",
								"Parameter Error", JOptionPane.ERROR_MESSAGE);
						clearParameters();

						lblSuccess.setText("Simulation NOT RUN successfully, try again. Help is available if needed");
						
						spinnerChanged = false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null,
							"Please CLEAR the table from the Results screen AND Graph screen before running another simulation!",
							"File Error", JOptionPane.ERROR_MESSAGE);
					lblSuccess.setText("Please CLEAR the table from the Results screen AND Graph screen before running another simulation!");
				}

			}
		});

		optPanel.add(lblStreng);
		optPanel.add(strength);
		optPanel.add(lblDist);
		optPanel.add(distance);
		optPanel.add(lblNoMag);
		optPanel.add(noMagnets);
		optPanel.add(lblSpin);
		optPanel.add(repeatSlider);

		SpringUtilities.makeCompactGrid(optPanel, 
				4, 2,
				6, 6,
				6, 6);


		mainPanel.add(lblMain, "dock north, wrap");

		mainPanel.add(diagram, "span 3 1, wrap");

		mainPanel.add(optPanel, "span 2 2");

		mainPanel.add(btnHelpButton, "wrap");
		mainPanel.add(btnRun, "wrap");

		mainPanel.add(lblSuccess, "gapleft 30");

		add(mainPanel);

	}

	// Other
	private void displayHelpDialog()
	{
		String message = "Magnet Strength between 1 and 5 " +
				"\nDistance between 1 and 5 " +
				"\nRepetitions cannot be 0 " +
				"\nNumber of Magnets between 1 and 10";

		JOptionPane.showMessageDialog(null, message, "Help", JOptionPane.INFORMATION_MESSAGE);
	} 

	public void clearParameters()
	{
		noMagnets.setText("");
		distance.setText("");
		strength.setText("");
		repeatSlider.setValue(1);
	}

	public boolean isEmpty()
	{
		if(distance.getText().equals("") || strength.getText().equals("") || noMagnets.getText().equals("") ||
				repeatSlider.getValue() == 0)
			return true;
		return false;
	}

	public double getUserStrength() { return Double.parseDouble(strength.getText()); }

	public double getUserDistance() { return Double.parseDouble(distance.getText()); }

	public int getUserNoMagnets() { return Integer.parseInt(noMagnets.getText()); }

	public int getUserRepetition() { return (int) repeatSlider.getValue(); }

	public void setSliderChanged(boolean b) {  this.spinnerChanged = b; }

}
