package com.durgendra.acmq;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class JmsConfigure implements JmsListenerConfigurer{

	@Value("${acmq.context-factory}")
	private String contextFactory;
	@Value("${acmq.destination}")
	private String destination;
	@Value("${acmq.provider-url}")
	private String providerUrl;
	@Value("${acmq.connection-factory-name}")
	private String connectionFactoryName;

	private Properties getJndiProperties() {
		Properties properties = new  Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
		properties.put(Context.PROVIDER_URL, providerUrl);
		return properties;

	}

	@Bean
	public ConnectionFactory connectionFactory() throws NamingException {
		JndiTemplate jndiTemplate = new JndiTemplate(getJndiProperties());
		ConnectionFactory connectionFactory = (ConnectionFactory) jndiTemplate.lookup(connectionFactoryName);
		return connectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() throws NamingException {
		UserCredentialsConnectionFactoryAdapter adapter = new UserCredentialsConnectionFactoryAdapter();
		adapter.setTargetConnectionFactory(connectionFactory());
		adapter.afterPropertiesSet();
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(adapter);
		connectionFactory.setReconnectOnException(true);
		connectionFactory.setSessionCacheSize(1);
		connectionFactory.afterPropertiesSet();
		return connectionFactory;
	}


	@Bean
	public JndiDestinationResolver jndiDestinationResolver() {
		JndiDestinationResolver destinationResolver = new JndiDestinationResolver();
		destinationResolver.setDynamicDestinationResolver(new DynamicDestinationResolver());
		destinationResolver.setFallbackToDynamicDestination(true);
		destinationResolver.setJndiTemplate(new JndiTemplate(getJndiProperties()));
		return destinationResolver;
	}

	@Bean
	public MessageConverter messageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.BYTES);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}

	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerFactory() throws NamingException{
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(cachingConnectionFactory());
		factory.setSessionTransacted(true);
		factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
		factory.setConcurrency("1-4");
		factory.setRecoveryInterval(30000L);
		factory.setDestinationResolver(jndiDestinationResolver());
		//factory.setErrorHandler(new DefaultErrorHandler());
		factory.setMessageConverter(messageConverter());
		return factory;
	}

	@Override
	public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
		try {
			SimpleJmsListenerEndpoint endPoint = new SimpleJmsListenerEndpoint();
			endPoint.setDestination(destination);
			endPoint.setId("JmsListener1");
			JmsListener listener = new JmsListener(messageConverter());
			endPoint.setMessageListener(listener);
			registrar.registerEndpoint(endPoint, jmsListenerContainerFactory());
			//registrar.setContainerFactory(jmsListenerContainerFactory());
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
