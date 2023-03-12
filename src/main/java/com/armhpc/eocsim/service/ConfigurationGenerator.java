package com.armhpc.eocsim.service;

import com.armhpc.eocsim.model.ClusterConfig;
import com.armhpc.eocsim.model.DataCenter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.armhpc.eocsim.util.Constants.MAX_CORES;
import static com.armhpc.eocsim.util.Constants.MIN_CORES;

@Service
public class ConfigurationGenerator {

	public List<ClusterConfig> generate(DataCenter dataCenter) {
		List<ClusterConfig> configs = new ArrayList<>();
		int totalCores = dataCenter.hosts.stream().mapToInt(host -> host.cores).sum();
		for (int core = MIN_CORES; core <= MAX_CORES; core *= 2) {
			int n = 1;
			while (n * core <= totalCores) {
				ClusterConfig clusterConfig = new ClusterConfig(n, core, core * 2);
				configs.add(clusterConfig);
				n *= 2;
			}
		}
		return configs;
	}

}
