package fr.ecoptimize.worker;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class Worker {

    public static void main(String[] args) throws Exception {
        // 1. Connexion a RabbitMQ (le mot de passe vient d'une variable d'environnement)
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.31.249.232");
        factory.setUsername("eco");
        factory.setPassword(System.getenv("RABBIT_PASSWORD"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 2. Ce que le worker fait a chaque demande recue
        DeliverCallback onMessage = (tag, delivery) -> {
            String demande = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String replyTo = delivery.getProperties().getReplyTo();
            String correlationId = delivery.getProperties().getCorrelationId();
            System.out.println("Demande recue : " + demande);

            // Reponse de test : on repond dans la file reply_to avec le meme correlation_id
            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                    .correlationId(correlationId)
                    .build();
            channel.basicPublish("", replyTo, props, "reponse-test".getBytes(StandardCharsets.UTF_8));
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        // 3. Ecoute de la file des demandes
        channel.basicConsume("q.recherche.unitaire", false, onMessage, tag -> { });
        System.out.println("Worker en attente de demandes...");
    }
}