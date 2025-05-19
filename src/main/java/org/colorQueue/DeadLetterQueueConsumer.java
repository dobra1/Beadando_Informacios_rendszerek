package org.colorQueue;

import org.apache.activemq.*;

import jakarta.jms.*;

public class DeadLetterQueueConsumer {
    public static void main(String[] args) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://activemq:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            jakarta.jms.Queue dlq = session.createQueue("DLQ.colorQueue");
            MessageConsumer consumer = session.createConsumer(dlq);

            System.out.println("DLQ fogyasztó fut...");

            while (true) {
                jakarta.jms.Message message = consumer.receive();

                if (message instanceof TextMessage) {
                    String text = ((TextMessage) message).getText();
                    System.out.println("Dead Letter (hibás üzenet) érkezett: " + text);
                } else {
                    System.out.println("Nem szöveges üzenet érkezett a DLQ-ba");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
