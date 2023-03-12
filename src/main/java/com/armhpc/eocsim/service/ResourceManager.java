package com.armhpc.eocsim.service;

import com.armhpc.eocsim.model.ClusterConfig;
import com.armhpc.eocsim.model.SimulationResult;
import com.armhpc.eocsim.model.dto.RequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceManager {
	private final ConfigurationGenerator configurationGenerator;
	private final DataSizeEstimator dataSizeEstimator;
	private final EOSimulator eoSimulator;
	private final Optimizer optimizer;

	@Autowired
	public ResourceManager(ConfigurationGenerator configurationGenerator,
	                       DataSizeEstimator dataSizeEstimator,
	                       EOSimulator eoSimulator,
	                       Optimizer optimizer) {
		this.configurationGenerator = configurationGenerator;
		this.dataSizeEstimator = dataSizeEstimator;
		this.eoSimulator = eoSimulator;
		this.optimizer = optimizer;
	}


	public List<SimulationResult> process(RequestDto data) {
		List<ClusterConfig> configs = configurationGenerator.generate(data.dataCenter);
		double inputSize = dataSizeEstimator.estimate(data.bbox, data.period);

		List<SimulationResult> simulationResults = new ArrayList<>();
		for (ClusterConfig config : configs) {
			double execution = eoSimulator.simulate(data.dataCenter, config, inputSize);
			simulationResults.add(new SimulationResult(config, execution));
		}
		return optimizer.optimize(simulationResults, data.costPerCore);

	}

}
