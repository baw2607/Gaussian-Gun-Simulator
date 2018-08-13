package logic;

import java.io.*;

public class RandAccessFile
{
	// New RandomAccessFile and File Instance
	private static RandomAccessFile r;
	private File file;
	// Constructor 
	public RandAccessFile() { init(); }
	// Initialisation method
	private void init()
	{	// Try: Connect to file 
		try
		{
			file = new File("rand.csv");
			if(!file.exists()) file.createNewFile();
			r = new RandomAccessFile(file, "rw");
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	// Method: reading RandomAccessFile based on a position index
	public String randFileRead(int pos)
	{	// Try: read from RandomAccessFile 
		String line = "";
		try
		{	// Seek pointer to position and capture current data element 
			r.seek(pos);
			line = r.readUTF();
			
		} catch(Exception e) { e.printStackTrace(); }
		// Return data 
		return line;
	}
	// Method: write line to RandomAccessFile based on position index
	public void randFileWrite(String line, int pos)
	{
		try
		{	// Seek pointer to position index and write line 
			r.seek(pos);
			r.writeUTF(line);
		} catch(Exception e) { e.printStackTrace(); }
	}
	// Methods: check if file already exists and delete if file exists
	public boolean exists() { return file.exists(); }
	public void delete() { if(exists()) file.delete(); }
}
