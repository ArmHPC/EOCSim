package com.armhpc.eocsim.model;

public record SimulationResult(ClusterConfig config, double execution) implements Comparable<SimulationResult> {

	@Override
	public int compareTo(SimulationResult res) {
		return (int) (res.execution - execution);
	}
}
