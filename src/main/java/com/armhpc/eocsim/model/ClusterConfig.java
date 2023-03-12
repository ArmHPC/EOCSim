package com.armhpc.eocsim.model;

public class ClusterConfig {
	public int nodes;
	public int core;
	public int ram;
	public double costPerCore;

	public ClusterConfig(int nodes, int core, int ram, double costPerCore) {
		this.nodes = nodes;
		this.core = core;
		this.ram = ram;
		this.costPerCore = costPerCore;
	}

	public ClusterConfig(int nodes, int core, int ram) {
		this(nodes, core, ram, .0);
	}

}
