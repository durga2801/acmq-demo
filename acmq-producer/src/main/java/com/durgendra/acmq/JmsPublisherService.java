package com.durgendra.acmq;

import java.util.UUID;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JmsPublisherService {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	@Qualifier("DefaultDestination")
	private Destination destination;
	
	@Autowired
	@Qualifier("DestinationWitType")
	private Destination destinationWithType;

	@Autowired
	@Qualifier("DefaultJmsTemplate")
	private JmsTemplate template;
	
	
	@Autowired
	@Qualifier("JmsTemplateWithType")
	private JmsTemplate templateWithType;

	public String publishMessage(CustomObject detail) throws JsonProcessingException {

		Object message = objectMapper.writeValueAsString(detail).getBytes();

		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty("UUID", UUID.randomUUID().toString());
				message.setStringProperty("MsgType", "CustomObject");
				message.setStringProperty("_type", "_type");
				return message;
			}
		};
		template.convertAndSend(message, messagePostProcessor);

		return null;
	}
	
	public String publishMessageWithType(CustomObject detail) throws JsonProcessingException {

		Object message = objectMapper.writeValueAsString(detail).getBytes();

		MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {

			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty("UUID", UUID.randomUUID().toString());
				message.setStringProperty("MsgType", "CustomObject");
				message.setStringProperty("_type", CustomObject.class.getName());
				return message;
			}
		};
		templateWithType.convertAndSend(message, messagePostProcessor);

		return null;
	}

}
