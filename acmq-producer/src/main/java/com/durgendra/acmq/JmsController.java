package com.durgendra.acmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/jms")
public class JmsController {
	
	@Autowired
	private JmsPublisherService service;
	
	@PostMapping("/publish")
	public String publish(@RequestBody CustomObject detail) throws JsonProcessingException {
		return service.publishMessage(detail);
	}
	
	@PostMapping("/publishWithType")
	public String publishWithType(@RequestBody CustomObject detail) throws JsonProcessingException {
		return service.publishMessageWithType(detail);
	}

}
