package com.armhpc.eocsim.service;

import com.armhpc.eocsim.model.ClusterConfig;
import com.armhpc.eocsim.model.DataCenter;
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicyFirstFit;
import org.cloudbus.cloudsim.brokers.DatacenterBroker;
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple;
import org.cloudbus.cloudsim.cloudlets.Cloudlet;
import org.cloudbus.cloudsim.cloudlets.CloudletSimple;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.datacenters.Datacenter;
import org.cloudbus.cloudsim.datacenters.DatacenterSimple;
import org.cloudbus.cloudsim.hosts.Host;
import org.cloudbus.cloudsim.hosts.HostSimple;
import org.cloudbus.cloudsim.resources.Pe;
import org.cloudbus.cloudsim.resources.PeSimple;
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic;
import org.cloudbus.cloudsim.vms.Vm;
import org.cloudbus.cloudsim.vms.VmSimple;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
//@Scope("request")
public class EOSimulator {

	private static final long BW = 10_000; //in Megabits/s
	private static final long STORAGE = 1_000_000; //in Megabytes
	private static final double UTILIZATION = .009;

	private CloudSim simulation;
	private List<Vm> vmList;
	private List<Cloudlet> cloudletList;
	private Datacenter datacenter0;
	private DatacenterBroker broker0;


	public double simulate(DataCenter dataCenter, ClusterConfig config, double inputDataSize) {
		simulation = new CloudSim();
		datacenter0 = createDatacenter(dataCenter);
		broker0 = new DatacenterBrokerSimple(simulation);
		vmList = createVms(config, dataCenter.hosts.get(0).getMIPS());
		cloudletList = createCloudlets(config, inputDataSize, UTILIZATION);
		broker0.submitVmList(vmList);
		broker0.submitCloudletList(cloudletList);
		simulation.start();
		List<Cloudlet> finishedCloudlets = broker0.getCloudletFinishedList();
		return finishedCloudlets.get(finishedCloudlets.size() - 1).getFinishTime();
	}

	private Datacenter createDatacenter(DataCenter dataCenter) {
		final var hostList = new ArrayList<Host>();
		dataCenter.hosts.forEach(host -> {
			hostList.add(createHost(host.cores, host.getMIPS(), host.ram));
		});
		return new DatacenterSimple(simulation, hostList, new VmAllocationPolicyFirstFit());
	}

	private Host createHost(int cores, int mips, int ram) {
		final var peList = new ArrayList<Pe>();
		for (int i = 0; i < cores; i++) {
			peList.add(new PeSimple(mips));
		}
		return new HostSimple(ram, BW, STORAGE, peList);
	}

	private List<Vm> createVms(ClusterConfig config, int mips) {
		final var vmList = new ArrayList<Vm>();
		for (int i = 0; i < config.nodes; i++) {
			final var vm = new VmSimple(mips, config.core);
			vm.setRam(config.ram).setBw(BW).setSize(10_000);
			vmList.add(vm);
		}
		return vmList;
	}

	private List<Cloudlet> createCloudlets(ClusterConfig config, double inputDataSize, double utilization) {
		final var cloudlets = new ArrayList<Cloudlet>();
		final var utilizationModel = new UtilizationModelDynamic(utilization);
		long length = (long) (inputDataSize / config.nodes / 8 * 1024 * 1024 / 1000);
		for (int i = 0; i < config.nodes; i++) {
			final var cloudlet = new CloudletSimple(length, config.core, utilizationModel);
			cloudlet.setSizes(1024);
			cloudlets.add(cloudlet);
		}
		return cloudlets;
	}

}
