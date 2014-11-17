package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// just a simple utility class to find a minimum position on a list

public class PSOUtility {
	public static int getMinPos(double[] list) {
		int pos = 0;
		double minValue = list[0];
		
		for(int i=0; i<list.length; i++) {
			if(list[i] < minValue) {
				pos = i;
				minValue = list[i];
			}
		}
		
		return pos;
	}
}
