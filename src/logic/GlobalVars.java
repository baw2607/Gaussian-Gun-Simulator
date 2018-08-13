package logic;

import java.awt.Dimension;


import javax.swing.JLabel;

public class GlobalVars {
	
	final public Dimension frameDimension = new Dimension(800, 500);
	
	final public JLabel lblGaussianGunSimulation = new JLabel("Gaussian Gun Simulator", JLabel.CENTER);
	
	public static int f = 1;
	final public String FILE_NAME = "data" + f + ".csv";
	
	public static double[] xData = new double[10];
	public static double[] yData = new double[10];
	
	public static boolean cleared = true;
	
}