package com.armhpc.eocsim.controller;

import com.armhpc.eocsim.model.SimulationResult;
import com.armhpc.eocsim.model.dto.RequestDto;
import com.armhpc.eocsim.service.ResourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/optimize")
public class Controller {

	@Autowired
	private ResourceManager resourceManager;

	@PostMapping
	public ResponseEntity<List<SimulationResult>> optimize(@RequestBody RequestDto data) {
		return ResponseEntity.ok(resourceManager.process(data));
	}


}
