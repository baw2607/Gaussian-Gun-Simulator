package gui;

import java.awt.Font;
import java.io.*;

import javax.swing.*;

import org.quinto.swing.table.model.*;
import org.quinto.swing.table.view.JBroTable;

import logic.GlobalVars;
import logic.RandAccessFile;
import net.miginfocom.swing.MigLayout;

public class ResultsPanel extends JPanel
{
	private JBroTable table;

	private JButton btnUpdateTable;
	private JButton btnClearTable;
	private JButton btnSortBy;

	private JPanel btnPanel;

	private ModelField[] fields;
	private ModelRow[] rows;
	private ModelData data;

	private int simMag;

	private GlobalVars vars = new GlobalVars();

	public ResultsPanel()
	{
		init();
	}

	private void init()
	{
		// Instantiate a new JPanel for the buttons to the side of the table 
		// Set layout to MigLayout Library 
		btnPanel = new JPanel();
		btnPanel.setLayout(new MigLayout());

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { e.printStackTrace(); }
		
		// Initialise table model with header groups and rowspan settings 
		IModelFieldGroup[] groups = new IModelFieldGroup[]
				{
						new ModelField("N", "N"),
						new ModelFieldGroup("Repeats", "Repeats")
						.withChild(new ModelFieldGroup("1", "1")
								.withChild(new ModelField("V1", "V"))
								.withChild(new ModelField("Ek1", "Ek")))
						.withChild(new ModelFieldGroup("2", "2")
								.withChild(new ModelField("V2", "V"))
								.withChild(new ModelField("Ek2", "Ek")))
						.withChild(new ModelFieldGroup("3", "3")
								.withChild(new ModelField("V3", "V"))
								.withChild(new ModelField("Ek3", "Ek"))),
						new ModelFieldGroup("Ave", "Ave")
						.withChild(new ModelField("Va", "V").withRowspan(2))
						.withChild(new ModelField("Eka", "Ek").withRowspan(2))
				};

		// New blank row data array of maximum size 
		rows = new ModelRow[10];

		data = new ModelData(groups);
		data.setRows(rows);

		fields = ModelFieldGroup.getBottomFields(groups);

		table = new JBroTable(data);

		btnUpdateTable = new JButton("Update Table");
		btnUpdateTable.addActionListener(e ->
		{
			try 
			{
				setSimMagnetsFromFile();
				readFromFile(fields, getSimMag());
				data.setRows(rows);

				table.revalidate();
				table.repaint();

				repaint();
				
				vars.cleared = false;
			}
			catch(Exception ex)
			{
				JOptionPane.showMessageDialog(null, "Error, NO file exists!");
			}

		});

		btnClearTable = new JButton("Clear Results");
		btnClearTable.addActionListener(e ->
		{
			rows = new ModelRow[10];
			data.setRows(rows);

			table.revalidate();
			table.repaint();

			repaint();

			deleteFile();
			vars.cleared = true;
		});

		btnSortBy = new JButton("Sort By...");
		btnSortBy.addActionListener(e-> {

			table.setAutoCreateRowSorter(true);
			JOptionPane.showMessageDialog(
					null,
					"Click on column headers to sort by column!",
					"Sorting",
					JOptionPane.INFORMATION_MESSAGE);
		});

		btnPanel.add(btnUpdateTable, "wrap");
		btnPanel.add(btnClearTable, "wrap");
		btnPanel.add(btnSortBy, "wrap");
		btnPanel.add(new JLabel("v = Average Velocity"), "wrap");
		btnPanel.add(new JLabel("Ek =  Kinetic Energy"), "wrap");

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		add(scrollPane);
		add(btnPanel);
	}

	// Method: read data and add to table 
	private void readFromFile(ModelField[] field, int no)
	{
		// 
		rows = new ModelRow[no];
		File file = new File(vars.FILE_NAME);
		BufferedReader br = null;
		FileReader fr = null;

		try
		{	// Only run if file exists 			
			if(file.exists())
			{	// Declare new FileReader and BufferedReader for file
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				
				String line;
				// Loop through number of lines in the array
				for (int i = 0; i < no; i++)
				{	// Read and split each line and create row data array
					line = br.readLine();
					String[] arr = line.split(",");
					rows[i] = new ModelRow(9);
					// Set data to row data array based on column
					for (int j = 0; j < fields.length; j++)
					{
						if(j == 0) rows[i].setValue(j, (j+1));
						rows[i].setValue(j, arr[j]);
					} // End For: j
				} // End For: i
			}
			else
			{	// Error handling of not possible 
				JOptionPane.showMessageDialog(null,
						"No File Exists", "File Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} // End Try 
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{	// Close all readers if not null 
			try {
				if (fr != null) fr.close();
				if (br != null) br.close();
			} 
			catch(Exception ex) {}
		}
	}

	private void deleteFile()
	{
		File file = new File(vars.FILE_NAME);

		if(file.exists()) {
			file.delete();
			JOptionPane.showMessageDialog(null,
					"File WAS deleted!",
					"File",
					JOptionPane.WARNING_MESSAGE);
			
		}
	}
	// Method: use RandomAccessFile to store property 
	private void setSimMagnetsFromFile()
	{
		// Instantiate RandAccessFile and read position index 0
		// to String s. Set string to global state simMag
		RandAccessFile r = new RandAccessFile();
		String s = r.randFileRead(0);
		this.simMag = Integer.parseInt(s);
	}

	public int getSimMag() { return this.simMag; }

}
