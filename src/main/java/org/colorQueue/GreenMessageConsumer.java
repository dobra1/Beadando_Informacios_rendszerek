package org.colorQueue;

import org.apache.activemq.*;

import javax.jms.*;
import java.util.*;

public class GreenMessageConsumer {
    private static int messageCount = 0;

    public static void main(String[] args) {
        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://activemq:61616");
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            Destination queue = session.createQueue("colorQueue");

            String selector = "color = 'GREEN'";
            MessageConsumer consumer = session.createConsumer(queue, selector);

            consumer.setMessageListener(message -> {
                try {
                    if (message instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message;
                        String text = textMessage.getText();
                        System.out.println("ZÖLD üzenet fogadva: " + text);

                        if (new Random().nextInt(10) < 3) {
                            System.out.println("Hibaszimuláció - visszagörgetés");
                            session.rollback();
                            return;
                        }

                        messageCount++;
                        if (messageCount % 10 == 0) {
                            sendStatistics(textMessage.getJMSDestination(), session);
                        }

                        session.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        session.rollback();
                    } catch (JMSException jmsEx) {
                        jmsEx.printStackTrace();
                    }
                }
            });

            System.out.println("PIROS fogyasztó fut...");
            System.out.println("Nyomj CTRL+C-t a leállításhoz.");
            Thread.currentThread().join();

            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendStatistics(Destination statisticsQueue, Session session) {
        try {
            MessageProducer producer = session.createProducer(session.createQueue("colorStatistics"));
            TextMessage statsMsg = session.createTextMessage("10 ZÖLD üzenet feldolgozva");
            producer.send(statsMsg);
            System.out.println("Statisztika elküldve: 10 ZÖLD üzenet feldolgozva.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*@MessageDriven(
    name = "GreenMessageConsumerMDB",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/colorQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    }
)
public class GreenMessageConsumerMDB implements MessageListener {
    private static int messageCount = 0;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("ZÖLD üzenet fogadva: " + text);


                if (new Random().nextInt(10) < 3) {
                    System.out.println("Hibaszimuláció - visszagörgetés");

                    return;
                }

                messageCount++;
                if (messageCount % 10 == 0) {
                    sendStatistics();
                }

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }*/