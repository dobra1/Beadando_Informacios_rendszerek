package org.colorQueue;

import org.apache.activemq.*;

import jakarta.jms.*;
import java.util.Random;

public class ColorMessageProducer {
    public static void main(String[] args) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://activemq:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("colorQueue");
            MessageProducer producer = session.createProducer(queue);

            Random random = new Random();
            String[] colors = {"RED", "GREEN", "BLUE"};

            while (true) {
                String color = colors[random.nextInt(colors.length)];

                TextMessage message = session.createTextMessage("Ez egy " + color + " üzenet");
                message.setStringProperty("color", color);

                producer.send(message);
                System.out.println("Elküldött üzenet: " + color);

                Thread.sleep(1000);
            }
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
