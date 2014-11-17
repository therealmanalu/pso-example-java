package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// bean class to represent location

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	private double[] loc;

	public Location(double[] loc) {
		super();
		this.loc = loc;
	}

	public double[] getLoc() {
		return loc;
	}

	public void setLoc(double[] loc) {
		this.loc = loc;
	}
	
}
