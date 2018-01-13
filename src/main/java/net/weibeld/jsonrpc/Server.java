package net.weibeld.jsonrpc;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.tools.jsonrpc.JsonRpcServer;

class Server {

    // Queue on which client sends RPC requests to server
    private static final String QUEUE = "json-rpc-queue";

    public static void main(String[] args) throws Exception {

        // Establish connection to RabbitMQ server
        String uri = System.getenv("CLOUDAMQP_URL");
        if (uri == null) uri = "amqp://guest:guest@localhost";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(uri);
        Channel channel = factory.newConnection().createChannel();

        channel.queueDeclare(QUEUE, false, false, false, null);

        // Create JSON-RPC server providing remote methods defined in Service 
        JsonRpcServer server = new JsonRpcServer(channel, QUEUE,
                                                 Service.class,
                                                 new ServiceImpl());

        // Start listening for RPC requests
        server.mainloop();
    }
}
