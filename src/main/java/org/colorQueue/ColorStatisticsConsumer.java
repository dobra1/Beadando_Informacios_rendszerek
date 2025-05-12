package org.colorQueue;

import org.apache.activemq.*;

import javax.jms.*;
import javax.jms.Message;

public class ColorStatisticsConsumer {
    public static void main(String[] args) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://activemq:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue statsQueue = session.createQueue("colorStatistics");
            MessageConsumer consumer = session.createConsumer(statsQueue);

            System.out.println("Statisztikai üzenetfogyasztó elindult...");

            while (true) {
                Message message = consumer.receive();
                if (message instanceof TextMessage) {
                    String text = ((TextMessage) message).getText();
                    System.out.println("STATISZTIKA: " + text);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
