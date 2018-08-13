package gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Menu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.security.auth.callback.ChoiceCallback;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.xml.crypto.Data;

import logic.GlobalVars;
import logic.RandAccessFile;

public class MenuScreenFrame extends JFrame
{
	
	// New instance of all Panels
	private MainScreenPanel mainScreenPanel;
	private SimulationPanel simulationPanel;
	private ResultsPanel resultsPanel;
	private GraphPanel graphPanel;
	private TutorialPanel tutorialPanel;
	
	// Layout and Panels for Main Frame 
	private CardLayout cardLayout;
	private JPanel mainPanel;
	private JPanel topPanel;
	private JPanel menuPanel;
	
	private GlobalVars vars = new GlobalVars();
	
	private boolean saveFile = false;
	private int count = 1;

	// Inner Class: for GUI switching in the same JFrame 
	private class CustomListener implements ActionListener	
	{
		// State 
		String name;
		// Constructor to set the state 
		public CustomListener(String n)
		{
			this.name = n;
		}
		// Override ActionListeners actionPerformed method with own function
		@Override
		public void actionPerformed(ActionEvent e)
		{
			// Show the panel appropriate to the name given 
			cardLayout.show(topPanel, name);
		}
	}
	
	// Constructor
	public MenuScreenFrame() 
	{
		super("Gaussian Gun Simulation");
		init();
	}

	private void init()
	{
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(vars.frameDimension);
		setLocationRelativeTo(null);
		
		cardLayout = new CardLayout();
		
		topPanel = new JPanel();
		topPanel.setLayout(cardLayout);
		topPanel.setMinimumSize(new Dimension(800, 440));
		
		menuPanel = new JPanel();
		menuPanel.setMinimumSize(new Dimension(800, 60));
		
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(vars.frameDimension);
		mainPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		mainScreenPanel = new MainScreenPanel();
		simulationPanel = new SimulationPanel();
		resultsPanel = new ResultsPanel();
		graphPanel = new GraphPanel();
		tutorialPanel = new TutorialPanel();
		
		
		// Add all JPanels to MainScreenFrame top JPanel
		// So as to maintain a consistent navigation bar on 
		// The button panel, names A, B, C, D & E are constraints
		// So the CustomListener can switch on button press
		topPanel.add(mainScreenPanel, "A");
		topPanel.add(simulationPanel, "B");
		topPanel.add(resultsPanel, "C");
		topPanel.add(graphPanel, "D");
		topPanel.add(tutorialPanel, "E");
		
		// Show mainScreenPanel on start up
		cardLayout.show(topPanel, "A");
		
		// Instantiate JButtons and add CustomListener as 
		// ActionListener since CustomListener implements 
		// ActionListener and overrides the actionPerformed
		// Method 
		JButton btnHome = new JButton("Home");
		btnHome.addActionListener(new CustomListener("A"));
		
		JButton btnSimulation = new JButton("Simulation");
		btnSimulation.addActionListener(new CustomListener("B"));

		JButton btnShowResults = new JButton("Show Results");
		btnShowResults.addActionListener(new CustomListener("C"));
		
		JButton btnShowGraph = new JButton("Show Graph");
		btnShowGraph.addActionListener(new CustomListener("D"));
		
		JButton btnTutorial = new JButton("Tutorial");
		btnTutorial.addActionListener(new CustomListener("E"));
		
		JCheckBox check = new JCheckBox("Save file on exit");
		check.setSelected(false);
		check.addItemListener(e -> 
		{
			if(e.getStateChange() == ItemEvent.SELECTED)
				saveFile = true;
			if(e.getStateChange() == ItemEvent.DESELECTED)
				saveFile = false;
		});
		
		menuPanel.add(btnHome);
		menuPanel.add(btnSimulation);
		menuPanel.add(btnShowResults);
		menuPanel.add(btnShowGraph);
		menuPanel.add(btnTutorial);
		menuPanel.add(check);
		
		c.gridy = 0;
		c.anchor = GridBagConstraints.PAGE_START;
		mainPanel.add(topPanel, c);
		
		c.gridy = 20;
		c.anchor = GridBagConstraints.PAGE_END;
		mainPanel.add(menuPanel, c);
		
		add(mainPanel);
		// Override WindowListener with custom method 
		addWindowListener(new WindowAdapter() 
		{	// Call custom method on close button click
			public void windowClosing(WindowEvent e)
			{
				exitOnClose();
			}
		});
	}
	// Method: window button click behaviour
	public void exitOnClose()
	{	// Connect to file and RandomAccessFile
		File file = new File(vars.FILE_NAME);
		RandAccessFile r = new RandAccessFile();
		// Delete RandAccessFile and check save flag 
		if(r.exists()) r.delete();
		if(file.exists() && !saveFile)
		{	// If save flag not checked confirm file delete the delete file
			JOptionPane.showConfirmDialog(null,
					"Files will be deleted", "Exit?", JOptionPane.OK_OPTION);
			file.delete();
			//Exit
			System.exit(0);
		} 
		else if(file.exists() && saveFile)
		{	// Confirm file save and save new file 
			JOptionPane.showConfirmDialog(null,
					"Files will be saved to simulation.txt", "Exit?",
					JOptionPane.OK_OPTION);
			try {
				// Connect to new file, if exists alter name then save data 
				File newFile = new File("Simulation" + count + ".txt");
				if(newFile.exists()) count++;
				Files.copy(file.toPath(), newFile.toPath());
				// Delete existing files 
				file.delete();
				r.delete();
				
			} catch (IOException e) {
				// Error handling if not completed
				JOptionPane.showMessageDialog(null, "File cannot be copied!",
						"File Error", JOptionPane.ERROR_MESSAGE);
			}
			// Delete RandomAccessFile using custom methods and exit
			if(r.exists()) r.delete();
			System.exit(0);
		} else {
			// Exit
			System.exit(0);
		}
	}
	
	
	// Main Method
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuScreenFrame window = new MenuScreenFrame();
					window.setVisible(true);
					window.setResizable(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
} // End Class