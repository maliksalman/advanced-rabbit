# advanced-rabbit

It is relatively easy to create a [spring-boot](https://spring.io/projects/spring-boot) app which can consume/produce messages through [RabbitMQ](https://www.rabbitmq.com/) using the [spring-cloud-stream](https://spring.io/projects/spring-cloud-stream) abstraction. Usually, that is sufficient for happy path scenarios but in reality, we need to handle error scenarios as well. This repository demonstrates various advanced ways of interfacing with RabbitMQ through spring-cloud-stream in a spring-boot app.

## Scenarios

This spring-boot app takes advantage of the spring profiles capabilities to demonstrate the various advanced scenarios. Most of the scenarios will require us to interact with the RabbitMQ management console. We will do this to act as either a simulated producer or a consumer of messages. The various scenarios are:

1. Configurable retries while consuming messages
1. Moving messages to DLQ after retries are exhausted
1. Expiring messages from DLQ
1. Moving messages to the origial queue from DLQ
1. Shared transactions with DB while consuming messages
1. Routing messages based on JSON message content 

## Setup

All the examples will require a working RabbitMQ instance with access to the management console. We will need to **reset the exchanges/queues** if you switch to different spring profile to demonstrate a different scenario. This is very simple while running RabbitMQ in docker. To start RabbitMQ, run:

```
docker run --rm -d -p 5672:5672 -p 15672:15672 --name rabbit rabbitmq:3-management
``` 

This will start RabbitMQ on `localhost` on port 5672 while the management console will be running on port 15672. The username/password will be `guest`/`guest`. If docker is runnig in a VM (like `docker-machine`), then all ports will not be accessible on `localhost` but instead on the IP address of the VM. In that case, setting `SPRING_RABBITMQ_HOST` to the IP address of your docker VM will need to be done while running the application.

To stop the container, run:

```
docker stop rabbit
``` 

## Scenario: Configurable retries while consuming messages 

While consuming messages, if there is an uncaught error in the method annotated with `@StreamListener`, by default spring-cloud-stream binder will retry by calling that method 3 times before giving up and throwing away the message. Those 3 retries are done without any wait time in between. Sometimes, the underlying cause of the error is just temporary and will go away if we had just put some wait times in between the retries. This example will retry the message 6 times with the initial retry after 0.5 seconds  of sleep. Then will keep doubling the wait times between retrys until the max of 3 second wait is reached (ex: 0.5s, 1s, 2s, 3s, 3s, 3s) 

All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/retries](src/main/java/com/smalik/advancedrabbit/retries) and all the configuration is contained in [src/main/resources/application-retries.yml](src/main/resources/application-retries.yml). To start the application with the `retries` profile, run:

```
SPRING_PROFILES_ACTIVE=retries ./gradlew bootRun
```

Notice that in RabbitMQ management console, there is a `retries.topic` exchange. Any message you publish through it will show up in the log output of our running application. If the message starts with the word `fail`, only then the retry configuration will be applied. On such a message, notice the log output of application.

## Scenario: Moving messages to DLQ after retries are exhausted

Configuring retry logic on failed message processing can only solve some issues, but after the retries are exhausted, the message is just lost. In this scenario, we will have spring-cloud-stream deposit the failed message to an auto-configured DLQ after retries are exhausted.  

All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/retries](src/main/java/com/smalik/advancedrabbit/retries) and all the configuration is contained in [src/main/resources/application-dlq.yml](src/main/resources/application-dlq.yml). To start the application with the `dlq` profile, run:

```
SPRING_PROFILES_ACTIVE=dlq ./gradlew bootRun
```

Notice that in RabbitMQ management console, there is a `retries.topic` exchange and couple of `retries.topic.*` queues. Any message you publish through the exchange will show up in the log output of our running application. If the message starts with the word `fail`, only then the retry with DLQ configuration will be applied. After retries are exchansted, the message should appear in the DLQ queue ... and stay there forever.

## Scenario: Expiring messages from DLQ

Once a message shows up in DLQ because retries were exhausted, it will stay there forever. The DLQ (and even regular queues) can be configured with a TTL after which the message will expire. This scenario does that with expiring the messages in DLQ after 60 seconds. 

All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/retries](src/main/java/com/smalik/advancedrabbit/retries) and all the configuration is contained in [src/main/resources/application-dlq2.yml](src/main/resources/application-dlq2.yml). To start the application with the `dlq2` profile, run:

```
SPRING_PROFILES_ACTIVE=dlq2 ./gradlew bootRun
```

Just like previous example, notice that in RabbitMQ management console, there is a `retries.topic` exchange and couple of `retries.topic.*` queues. Any message you publish through the exchange will show up in the log output of our running application. If the message starts with the word `fail`, only then the retry with DLQ configuration will be applied. After retries are exchansted, the message should appear in the DLQ queue. Instead of staying in the DLQ forever, it will disppear after 60 unless it was consumed/removed during that time.

## Scenario: Moving messages to the origial queue from DLQ

This scenario build on the previous one where a failed message retries are exhausted and the message is put in the DLQ. But instead of message disappearing from the DLQ, it will be put back in the original queue.

All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/retries](src/main/java/com/smalik/advancedrabbit/retries) and all the configuration is contained in [src/main/resources/application-dlq3.yml](src/main/resources/application-dlq3.yml). To start the application with the `dlq3` profile, run:

```
SPRING_PROFILES_ACTIVE=dlq3 ./gradlew bootRun
```

Just like previous example, notice that in RabbitMQ management console, there is a `retries.topic` exchange and couple of `retries.topic.*` queues. Any message you publish through the exchange will show up in the log output of our running application. If the message starts with the word `fail`, this DLQ configuration will be applied. After retries are exchausted, the message should appear in the DLQ queue. Instead of staying in the DLQ forever, it will be put back in the original queue after 60 seconds unless it is consumed/removed during that time from DLQ.

## Scenario: Shared transactions with RabbitMQ and DB while consuming messages

A very common scenario is to do some processing, make a change in a DB, and send out a different message to another topic/exchange in your `@SteamListener` method. If sequence of events is:
 
1. do some processing
1. record a change in the DB
1. send an output message to another topic/exchange
1. do some further processing

If some exception occurs while doing that "further processing" (last step), by default spring-cloud-stream will retry the message 3 times before giving up. You will get a DB change and a message in the output queue for every try - which is not desirable. The whole `@StreamListener` method can run in a transaction which will prevent the above from happening.
 
All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/transactions](src/main/java/com/smalik/advancedrabbit/transactions) and all the configuration is contained in [src/main/resources/application-transactions.yml](src/main/resources/application-transactions.yml). To start the application with the `transactions` profile, run:

```
SPRING_PROFILES_ACTIVE=transactions ./gradlew bootRun
```

Notice that in RabbitMQ management console, there are `txin.topic` and `txout.topic` exchanges. Also, there is a `DATA` table in the h2 DB which you can access through [http://localhost:8080/h2](http://localhost:8080/h2) - remember to use `jdbc:h2:mem:testdb` for the JDBC url, `sa` as the username, and no password. Then, any message you publish through the `txin.topic` exchange will show up in the `txout.topic` exchange and a new row in `DATA` table unless the message starts with the word `fail`. If it is a fail message, the default behavior of spring-cloud-stream is to retry 3 times, so you will see the exception stacktraces repeated 3 times ... but no message in the `txout.topic` and no new rows in the `DATA` table should appear due to our transactions configuration.

## Scenario: Routing messages based on JSON message content

Sometimes, it is desirable to route message to different queues that originate from the same exchange. RabbitMQ can do that based on a "routing key". A spring-cloud-stream producer can set the routing key for each message based on a header or content value of the message. A spring-cloud-stream consumer can indicate that it is only interested in messages with a certain binding key. This example uses these capabilities.

All relevant code for this scenario is contained in [src/main/java/com/smalik/advancedrabbit/routed](src/main/java/com/smalik/advancedrabbit/routed) and all the configuration is contained in [src/main/resources/application-routed-producer.yml](src/main/resources/application-routed-producer.yml) and [src/main/resources/application-routed-consumer.yml](src/main/resources/application-routed-consumer.yml). We will start 3 instances of the same application with different profiles to simulate one producer and 2 consumers. Run the following the appication instances:
 
- `SPRING_PROFILES_ACTIVE=routed-producer ./gradlew bootRun`
- `SPRING_PROFILES_ACTIVE=routed-consumer ROUTING_KEY=red ./gradlew bootRun`
- `SPRING_PROFILES_ACTIVE=routed-consumer ROUTING_KEY=green ./gradlew bootRun`
 
Notice that in RabbitMQ management console, there is a `routed.topic` exchange with `routed.topic.red` and `routed.topic.green` queues bound to it. For this example, we will use a REST endpoint on the producer app to add messages to `routed.topic` exchange.

Use the following command and see the message is only received by the **green** consumer:
 
```
curl -X POST http://localhost:8080/send/green/cough-in-your-elbow
```
 
Use the following command and see the message is only received by the **red** consumer:
 
```
curl -X POST http://localhost:8080/send/red/wash-your-hands-often
```

If you were to send a message with a different color, none of our consumers will get it - it will just disappear. On the other hand, if we create a queue and bind to the same exchange without any routing key specified, it will receive all messages sent to that exchange no matter the color.