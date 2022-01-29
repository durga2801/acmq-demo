package com.durgendra.acmq;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.jms.support.converter.MessageConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JmsListener implements MessageListener{
	
	private MessageConverter messageConverter;
	
	public JmsListener(MessageConverter messageConverter) {
		super();
		this.messageConverter = messageConverter;
	}


	@Override
	public void onMessage(Message message) {
		log.info("-----------"+message);
		if(message instanceof TextMessage) {
			log.info("-----TextMessage-----");
			try {
				String msg = message.getBody(String.class);
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(message instanceof BytesMessage) {
			log.info("-----ByteMessage-----");
			
			try {
				byte[] byteData = null;
				 byteData = new byte[(int) ((BytesMessage) message).getBodyLength()];
				 ((BytesMessage) message).readBytes(byteData);
				String stringMessage =  new String(byteData);
				log.info(stringMessage);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
			
	}

}
