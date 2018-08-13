package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

import javax.swing.JOptionPane;

public class Simulation
{
	// Magnet
	private Magnet magnet;

	// Additional Simulation parameters
	private int noMagnets;
	private int repetition;

	// Data Arrays
	private double[] ek = new double[3];
	private double[] v = new double[3];

	// Data 2D Array
	private double[][] data = new double[10][8];

	private GlobalVars vars = new GlobalVars();

	// Constructor
	public Simulation()
	{
		this.magnet = new Magnet();

		this.noMagnets = 0;
		this.repetition = 0;
	}

	// Set Simulation + Magnet states
	public void setValues(double s, double d, int n, int r)
	{
		this.magnet = new Magnet(s, d);

		this.noMagnets = n;
		this.repetition = r;

		this.v = new double[3];
		this.ek = new double[3];
	}
	
	// Method: run the experiment 
	public void run()
	{
		// Set alteration based on random uncertainty
		double mAlteration = magnet.getStrength() / 10000.0;
		double cAlteration = magnet.getDistance() / 10000.0;
		// Alter values based on random uncertainty 
		double m = 0.0015 + mAlteration;
		double c = 0.0042 - cAlteration;
		// Blank array created for graph data 
		vars.xData = new double[getNoMag()];
		vars.yData = new double[getNoMag()];
		// Loop through number of magnets in the simulation
		for(int j = 0; j < getNoMag(); j++)
		{	// Initialise array positions and blank data string 
			int posE = 0, posV = 0;
			String s = "";
			// Loop through number of repetitions in the simulation 
			for(int i = 0; i < getRepetition(); i++)
			{	// Use y=mx+c equation to find Kinetic Energy and adjust 
				// Use second equation to find velocity and adjust 
				ek[i] = m * (j+1) + c;
				ek[i] = adjustment(ek[i]);
				v[i] = Math.sqrt((2.0 * ek[i]) / 0.01);
				v[i] = adjustment(v[i]);
				// Change index position based on number of magnets 
				if(i == 0) { posV = 0; posE = 1; }
				if(i == 1) { posV = 2; posE = 3; }
				if(i == 2) { posV = 4; posE = 5; }
				data[j][posV] = v[i];
				data[j][posE] = ek[i];
				// calculate average velocity and kinetic energy 
				double aveV = (v[0] + v[1] + v[2]) / 
						(double ) getRepetition();
				double aveE = (ek[0] + ek[1] + ek[2]) / 
						(double) getRepetition();
				// Enter data in both data array and graph data array 
				data[j][6] = aveV;
				data[j][7] = aveE;
				vars.xData[j] = j+1;
				vars.yData[j] = data[j][6];
			} // End For: i
			// Format strings to 4 decimal places 
			for(int k = 0; k < 7; k++) 
				s += String.format("%.4f", data[j][k]) + ",";
			s += String.format("%.4f", data[j][7]);
			// New Instantiation of RandAccessFile
			// Write no. magnets to file to be used else where
			RandAccessFile r = new RandAccessFile();
			r.randFileWrite(Integer.toString(getNoMag()), 0);
			// Add data to file 
			outputToFile((j+1) + "," + s);
		} // End For: j

	} // End Method: run

	// Method: Adjusting value for repeats 
	private double adjustment(double d)
	{	// Instantiate Random Number and get new random / 1000
		Random r = new Random();
		double rand = r.nextDouble() / 1000;
		// Either add or remove adjustment value based upon second rand int 
		// Return value 
		if(r.nextInt(2) == 0) return d + rand;
		if(r.nextInt(2) == 1) return d - rand;
		return d;
	} 

	// Method: write String to file 
	private void outputToFile(String data)
	{	// Try: connect to file, if it does not exists then create new file
		try
		{
			File file = new File(vars.FILE_NAME);
			if(!file.exists()) file.createNewFile();

			// New FileWriter and BufferedWriter to write to file 
			FileWriter fw = new FileWriter(vars.FILE_NAME, true);
			BufferedWriter bw = new BufferedWriter(fw);
			// Write data passed in as parameters + add a new line 
			bw.write(data);
			bw.newLine();
			// Close both FileWriter and BufferedWriter
			bw.close();
			fw.close();

		}
		catch(Exception e) { 
			// Error handling if this cannot happen 
			JOptionPane.showMessageDialog(null, "File CANNOT be reached,"
					+ " make sure permissions are corect and "
					+ "restart the program", 
					"File Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	// EXTENSIBILITY
	public void outputToMultipleFile()
	{
		/*
		 * This method will alter the file name and allow the data to output to a
		 * second file when the experiment is run again instead of deleting the file
		 * as soon as the results are cleared
		 */
	}

	public void printVals() { System.out.println(magnet.toString() + " No Mag: " + getNoMag()
	+ " Rep: " + getRepetition()); }

	public int getRepetition() { return this.repetition; }
	public int getNoMag() { return this.noMagnets; }

} // End Class: Simulation