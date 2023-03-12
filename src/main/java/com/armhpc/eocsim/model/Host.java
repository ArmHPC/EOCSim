package com.armhpc.eocsim.model;

public class Host {

	public int id;
	public double frequency;
	public int cores;
	public int ram;


	public int getMIPS() {
		return (int) (frequency * 1000);
	}

}
