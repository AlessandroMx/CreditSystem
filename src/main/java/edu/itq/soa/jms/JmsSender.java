package edu.itq.soa.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class JmsSender extends JmsTemplate{
    /**
     * Envia un mensaje a la cola configurada
     * @param message
     */
    public void sendMessage(final String queue ,final String message)
    {
        this.send(queue, new MessageCreator() {
            
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                
                return textMessage;
            }
        });
    }
}
