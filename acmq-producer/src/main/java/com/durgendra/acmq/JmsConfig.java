package com.durgendra.acmq;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JmsConfig {

	String BROKER_URL = "tcp://localhost:61616"; 
	String BROKER_USERNAME = "admin"; 
	String BROKER_PASSWORD = "admin";

	@Value("${acmq.destination.name}")
	private String destinationName;
	
	@Value("${acmq.destination.name-with-type}")
	private String destinationWithType;

	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(BROKER_URL);
		connectionFactory.setPassword(BROKER_USERNAME);
		connectionFactory.setUserName(BROKER_PASSWORD);
		return connectionFactory;
	}

	@Bean("DefaultJmsTemplate")
	public JmsTemplate jmsTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDefaultDestination(getDestination());
		//template.setDefaultDestinationName(destinationName);
		//template.setMessageConverter(messageConverter());
		return template;
	}
	
	@Bean("JmsTemplateWithType")
	public JmsTemplate jmsTemplateWithType(){
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDefaultDestination(getDestinationWitType());
		//template.setDefaultDestinationName(destinationName);
		//template.setMessageConverter(messageConverter());
		return template;
	}
	
	@Bean("MessageConverterCustomObject")
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.BYTES);
        converter.setTypeIdPropertyName("_type");
        Map<String, Class<?>> idMapping = new HashMap<>();
        idMapping.put("_type", CustomObject.class);
       // converter.setTypeIdMappings(idMapping);
        return converter;
    }
	

	@Bean("DefaultDestination")
	public Destination getDestination() {
		return new ActiveMQQueue(destinationName);
	}
	
	@Bean("DestinationWitType")
	public Destination getDestinationWitType() {
		return new ActiveMQQueue(destinationWithType);
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory());
		factory.setMessageConverter(null);
		factory.setConcurrency("1-1");
		return factory;
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

}