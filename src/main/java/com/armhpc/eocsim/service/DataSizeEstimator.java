package com.armhpc.eocsim.service;

import org.springframework.stereotype.Service;

import static com.armhpc.eocsim.util.Constants.WEEKLY_DATA_SIZE;

@Service
public class DataSizeEstimator {

	public double estimate(String bbox, String period) {
		return WEEKLY_DATA_SIZE;
	}

}
