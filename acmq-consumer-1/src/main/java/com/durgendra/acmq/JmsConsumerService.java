package com.durgendra.acmq;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JmsConsumerService {
	
	@Autowired
	@Qualifier("messageConverterIdMapping")
	private MessageConverter converter;
	
	
	@Autowired
	@Qualifier("messageConverterText")
	private MessageConverter converterText;

	@JmsListener(destination = "${acmq.destination.name}")
	public void receiveMessage(Message message, @Headers MessageHeaders messageHeaders) throws JMSException {
		
		log.info("message headers {}",messageHeaders);
		
		log.info("consumed message {}", message);
		CustomObject value = (CustomObject) converter.fromMessage(message);
		System.out.println(String.valueOf(value));

	}
	
	
	@JmsListener(destination = "${acmq.destination.name-with-type}")
	public void receiveOrder(Message message, @Headers MessageHeaders messageHeaders) throws JMSException {
		
		log.info("message headers {}",messageHeaders);
		
		log.info("consumed message {}", message);
		CustomObject value = (CustomObject) converterText.fromMessage(message);
		System.out.println(String.valueOf(value));

	}

}
