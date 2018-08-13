package logic;

public class Magnet 
{
	// States
	private double distance;
	private double strength;

	// Default Constructor
	public Magnet()
	{
		this.strength = 0;
		this.distance = 0;
	}

	// Constructor
	public Magnet(double s, double d) 
	{
		if(checkDistance(d)) this.distance = d;
		if(checkStrength(s)) this.strength = s;
	}

	// Getters / Setters
	public double getStrength() { return strength; }
//	public void setStrength(double strength) { this.strength = strength; }

	public double getDistance() { return distance; }
//	public void setDistance(double distance) { this.distance = distance; }

	// Other
	private boolean checkStrength(double s) { return s > 0 && s < 6; }
	private boolean checkDistance(double d) { return d > 0 && d < 6; }

	// toString
	@Override public String toString() {
		return "Streng: " + strength + " Dist: " + distance;
	}
	
}
