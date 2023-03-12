package com.armhpc.eocsim.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ResourceManager {

	@GetMapping
	public void test() {
		System.out.println("test");
	}


}
