package org.gandhim.pso;

/* author: gandhi - gandhi.mtm [at] gmail [dot] com - Depok, Indonesia */

// this is the heart of the PSO program
// the code is for 2-dimensional space problem
// but you can easily modify it to solve higher dimensional space problem

import java.util.Random;
import java.util.Vector;

public class PSOProcess implements PSOConstants {
	private Vector<Particle> swarm = new Vector<Particle>();
	private double[] pBest = new double[SWARM_SIZE];
	private Vector<Location> pBestLocation = new Vector<Location>();
	private double gBest;
	private Location gBestLocation;
	private double[] fitnessValueList = new double[SWARM_SIZE];
	
	Random generator = new Random();
	
	public void execute() {
		initializeSwarm();
		updateFitnessList();
		
		for(int i=0; i<SWARM_SIZE; i++) {
			pBest[i] = fitnessValueList[i];
			pBestLocation.add(swarm.get(i).getLocation());
		}
		
		int t = 0;
		double w;
		double err = 9999;
		
		while(t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
			// step 1 - update pBest
			for(int i=0; i<SWARM_SIZE; i++) {
				if(fitnessValueList[i] < pBest[i]) {
					pBest[i] = fitnessValueList[i];
					pBestLocation.set(i, swarm.get(i).getLocation());
				}
			}
				
			// step 2 - update gBest
			int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
			if(t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
				gBest = fitnessValueList[bestParticleIndex];
				gBestLocation = swarm.get(bestParticleIndex).getLocation();
			}
			
			w = W_UPPERBOUND - (((double) t) / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
			
			for(int i=0; i<SWARM_SIZE; i++) {
				double r1 = generator.nextDouble();
				double r2 = generator.nextDouble();
				
				Particle p = swarm.get(i);
				
				// step 3 - update velocity
				double[] newVel = new double[PROBLEM_DIMENSION];
				newVel[0] = (w * p.getVelocity().getPos()[0]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[0] - p.getLocation().getLoc()[0]) +
							(r2 * C2) * (gBestLocation.getLoc()[0] - p.getLocation().getLoc()[0]);
				newVel[1] = (w * p.getVelocity().getPos()[1]) + 
							(r1 * C1) * (pBestLocation.get(i).getLoc()[1] - p.getLocation().getLoc()[1]) +
							(r2 * C2) * (gBestLocation.getLoc()[1] - p.getLocation().getLoc()[1]);
				Velocity vel = new Velocity(newVel);
				p.setVelocity(vel);
				
				// step 4 - update location
				double[] newLoc = new double[PROBLEM_DIMENSION];
				newLoc[0] = p.getLocation().getLoc()[0] + newVel[0];
				newLoc[1] = p.getLocation().getLoc()[1] + newVel[1];
				Location loc = new Location(newLoc);
				p.setLocation(loc);
			}
			
			err = ProblemSet.evaluate(gBestLocation) - 0; // minimizing the functions means it's getting closer to 0
			
			
			System.out.println("ITERATION " + t + ": ");
			System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
			System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
			System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));
			
			t++;
			updateFitnessList();
		}
		
		System.out.println("\nSolution found at iteration " + (t - 1) + ", the solutions is:");
		System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
		System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
	}
	
	public void initializeSwarm() {
		Particle p;
		for(int i=0; i<SWARM_SIZE; i++) {
			p = new Particle();
			
			// randomize location inside a space defined in Problem Set
			double[] loc = new double[PROBLEM_DIMENSION];
			loc[0] = ProblemSet.LOC_X_LOW + generator.nextDouble() * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
			loc[1] = ProblemSet.LOC_Y_LOW + generator.nextDouble() * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
			Location location = new Location(loc);
			
			// randomize velocity in the range defined in Problem Set
			double[] vel = new double[PROBLEM_DIMENSION];
			vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
			Velocity velocity = new Velocity(vel);
			
			p.setLocation(location);
			p.setVelocity(velocity);
			swarm.add(p);
		}
	}
	
	public void updateFitnessList() {
		for(int i=0; i<SWARM_SIZE; i++) {
			fitnessValueList[i] = swarm.get(i).getFitnessValue();
		}
	}
}
