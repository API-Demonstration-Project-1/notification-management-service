package com.proarchs.notification.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class MQConfig {
	
	@Value("${activemq.broker-url}")
	private String brokerUrl;

	@Value("${activemq.notification.otpverification}")
	private String otpVerificationQueue;
	
	@Value("${activemq.notification.otpverificationconfirmation}")
	private String otpVerificationConfirmationQueue;

	@Bean
	public ActiveMQConnectionFactory receiverMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);

		return activeMQConnectionFactory;
	}

	@Bean
	public DefaultJmsListenerContainerFactory notificationDefaultJmsListenerContainerFactory() {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		factory.setConnectionFactory(receiverMQConnectionFactory());
		factory.setConcurrency("3-10");

		return factory;
	}

	@Bean
	public ActiveMQConnectionFactory senderMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(brokerUrl);

		return activeMQConnectionFactory;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory() {
		return new CachingConnectionFactory(senderMQConnectionFactory());
	}

	@Bean
	public JmsTemplate otpVerificationConfirmationJmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
		jmsTemplate.setDefaultDestinationName(otpVerificationConfirmationQueue);

		return jmsTemplate;
	}

	// @Bean
	/*
	 * public DefaultMessageListenerContainer paymentMessageListenerContainer() {
	 * SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
	 * //endpoint.setMessageListener(new StatusMessageListener("DMLC"));
	 * endpoint.setDestination(stripeEventQName);
	 * 
	 * return
	 * paymentDefaultJmsListenerContainerFactory().createListenerContainer(endpoint)
	 * ; }
	 */
}
