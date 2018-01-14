# JSON-RPC Server

This is a JSON-RPC server using the [JSON-RPC](http://www.jsonrpc.org/) implementation of [RabbitMQ](http://www.rabbitmq.com/).

The purpose is to show how to implement a JSON-RPC server/server pair with RabbitMQ in Java.

The corresponding JSON-RPC client can be found [here](https://github.com/weibeld/JSON-RPC-Client-Heroku).

## What is JSON-RPC?

JSON-RPC is a protocol that allows a client to make remote procedure calls (RPC) to a server. It allows applications running on different machines to communicate with each other.

JSON-RPC can run over different network protocols like HTTP or TCP. In the case of the RabbitMQ, the underlying network protocol is [AMQP](https://www.amqp.org/).

In JSON-RPC, the data for the RPC requests and replies is encoded in JSON.

Note: RPC is a *synchronous* communication paradigm. That is, after making an RPC request, the client blocks until it gets a response from the server.

## Implementation

This implementation uses:

- [JSON-RPC](http://www.jsonrpc.org/): a lightweight RPC protocol
- [RabbitMQ](http://www.rabbitmq.com/): message passing service (message broker) implementing the [AMQP](https://www.amqp.org/) protocol
- [RabbitMQ Java Client Library](http://www.rabbitmq.com/java-client.html): Java APIs for RabbitMQ
- [`JsonRpcServer`](http://www.rabbitmq.com/releases/rabbitmq-java-client/current-javadoc/com/rabbitmq/tools/jsonrpc/JsonRpcServer.html): class of the RabbitMQ Java Client Library that implements a JSON-RPC server
- [Heroku](http://heroku.com): Platform as a Service (PaaS) provider for running any apps in the cloud
- [CloudAMQP](https://elements.heroku.com/addons/cloudamqp): Heroku add-on providing "RabbitMQ as a Service" for Heroku apps

## Run on Heroku

### Create Heroku App

Create an app on Heroku for your JSON-RPC server:

~~~bash
heroku create YOUR-APP-NAME
~~~

### Set Up RabbitMQ

Install the CloudAMQP add-on for this Heroku application:

~~~bash
heroku addons:create cloudamqp
~~~

This creates an additional Heroku dyno running a **RabbitMQ server** for your application on Heroku.

In addition, it adds the following config vars to your Heroku application:

- `CLOUDAMQP_APIKEY`
- `CLOUDAMQP_URL`

You can confirm this with `heroku config`.

The value of the `CLOUDAMQP_URL` variable is the URI of the RabbitMQ server that has just been created on Heroku. Your application needs this URI in order to connect to the RabbitMQ server.

**Important:** you have to execute the above command **only once** for the client/server pair. If you already ran this for the client, then **do not** run it again for the server. Instead, just add the above config vars to the server application:

~~~bash
heroku config:set CLOUDAMQP_APIKEY="..."
heroku config:set CLOUDAMQP_URL="..."
~~~

### Run

Deploy and launch the JSON-RPC server application with:

~~~bash
git push heroku master
~~~

### Monitor

To see all the queues and their content on the RabbitMQ server, use the **CloudAMQP Dashboard**:

~~~bash
heroku addons:open cloudamqp
~~~

Note that this command only works from the application (server or client) on which you *installed* the CloudAMQP add-on (i.e. the one in which you executed `heroku addons:create cloudamqp`).

### Order of Execution

The JSON-RPC server is a long-running application. That is, once started, it is supposed to just run and never stop. The [JSON-RPC client](https://github.com/weibeld/JSON-RPC-Client-Heroku), on the other hand, is launched on demand, makes one request to the JSON-RPC server, and then terminates.

Thus, the normal order of execution is to first start the JSON-RPC server, and then the JSON-RPC client In this case, the request sent by the JSON-RPC client is handled immediately by the JSON-RPC server.

However, starting the JSON-RPC client before the JSON-RPC server is running is also possible. In this case, there are two possibilities of what can happen:

- If the RPC request queue already exists (if the JSON-RPC server has been running before at some time), the message sent by the JSON-RPC client is stored in the queue until the JSON-RPC server starts up, at which point the JSON-RPC server handles the message instantly.
- If the RPC request queue does not exist, then the message sent by the JSON-RPC client is simply discarded. When the JSON-RPC server starts up, it doesn't receive this message, because it has not been saved in the RPC request queue. Consequently, the JSON-RPC client will never receive a response for this message.


### Tip

If no messages seem to be sent at all, make sure that there's actually a dyno scaled for the JSON-RPC client and server applications:

~~~bash
heroku ps
~~~~

Scale one dyno for the JSON-RPC server:

~~~bash
heroku ps:scale client=1
~~~

## Run Locally

The Heroku application can be run on the local machine, which is handy during development.

However, for this to work, you need to install and run a RabbitMQ server on your local machine.

### Install RabbitMQ Server

Install the RabbitMQ server on your local machine according to the instructions [here](http://www.rabbitmq.com/download.html).

This provides the command `rabbitmq-server`, which starts a RabbitMQ server on the default port 5672.

Note that if you install with Homebrew, you might need to add the folder containing the RabbitMQ executables to the `PATH`.

### Run

First, make sure that the RabbitMQ server is running on the local machine with `rabbitmq-server`.

Then, start the JSON-RPC server:

~~~bash
heroku local
~~~~

### Monitor

See all queues and their content of the local RabbitMQ server in the [Management Web UI](http://www.rabbitmq.com/management.html) here (username: **guest**, password: **guest**): <http://localhost:15672> .

You can also list all the queues from the command line:

~~~bash
sudo rabbitmqctl list_queues
~~~
