package com.armhpc.eocsim.service;

import com.armhpc.eocsim.model.SimulationResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Optimizer {
	public List<SimulationResult> optimize(List<SimulationResult> simulationResults, double costPerCore) {
		List<SimulationResult> optimalSolutions = new ArrayList<>();

		if (costPerCore == 0.0) {
			return simulationResults.stream().sorted(Comparator.reverseOrder())
					.limit(5)
					.collect(Collectors.toList());
		}
		for (SimulationResult simulationResult : simulationResults) {
			double currentCost = calculateCost(simulationResult, costPerCore);
			boolean isOptimal = simulationResults.stream().filter(r -> {
				double cost = calculateCost(r, costPerCore);
				return currentCost > cost && simulationResult.execution() > r.execution();
			}).toList().isEmpty();
			if (isOptimal) {
				optimalSolutions.add(simulationResult);
			}
		}
		return optimalSolutions;
	}

	private double calculateCost(SimulationResult simulationResult, double costPerCore) {
		return simulationResult.config().core * simulationResult.config().nodes * simulationResult.execution() * costPerCore;
	}

}
