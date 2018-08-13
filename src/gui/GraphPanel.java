package gui;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import logic.GlobalVars;
import net.miginfocom.swing.MigLayout;

public class GraphPanel extends JPanel 
{	
	private GlobalVars vars = new GlobalVars();

	private JButton btnUpdateGraph;
	private JButton btnClearGraph;

	private JPanel btnPanel;
	private JPanel chartPanel;

	private XYChart chart;

	public GraphPanel()
	{
		init();
	}

	public void init()
	{

		btnPanel = new JPanel();
		btnPanel.setLayout(new MigLayout());

		// New instance of XChart using QuickChart class as simple  
		// Line graph is required
		chart = QuickChart.getChart("Average Velocity vs Magnets",
				"Number of Magnets (x)", "Average Velocity (v)", "y(x)",
				vars.xData, vars.yData);
		chartPanel = new XChartPanel<XYChart>(chart);

		// Instantiate button to update graph
		// Include ActionListener as lambda expression 
		btnUpdateGraph = new JButton("Update Graph");
		btnUpdateGraph.addActionListener(e-> {

			try 
			{
				// Grab data for the table from file 
				getData();
				// Draw new series onto the screen 
				chartPanel.revalidate();
				chartPanel.repaint();
				// Redraw whole JPanel 
				repaint();
				
				JOptionPane.showMessageDialog(null,
						"Graph HAS BEEN updated!",
						"Graph Information",
						JOptionPane.INFORMATION_MESSAGE);
			} 
			catch (Exception e1) 
			{
				JOptionPane.showMessageDialog(null,
						"Graph has NOT been updated!", "Graph Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		btnClearGraph = new JButton("Clear Graph ");
		btnClearGraph.addActionListener(e-> {

			chart.removeSeries("v(x)");

			chartPanel.revalidate();
			chartPanel.repaint();

			repaint();

			JOptionPane.showMessageDialog(null, "Graph HAS BEEN cleared!", "Graph Information", JOptionPane.INFORMATION_MESSAGE);
		});

		btnPanel.add(btnUpdateGraph, "wrap");
		btnPanel.add(btnClearGraph, "wrap");

		add(chartPanel);
		add(btnPanel);

	}

	public void getData()
	{
		chart.addSeries("v(x)", vars.xData, vars.yData);
		chart.removeSeries("y(x)");
	}
	
	//EXTENSIBILITY
	public void addMultipleSeries()
	{
		/*
		 * This method will alter the name of the second 
		 * series of data so it can be added to the graph. 
		 * Another button will be added
		 * from this method as well as a different file 
		 * location to keep data separate
		 */
	}

}