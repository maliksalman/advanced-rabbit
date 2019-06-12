package com.smalik.advancedrabbit.routed;

import com.smalik.advancedrabbit.MessagingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RoutedMessageListener {

    private Logger logger = LoggerFactory.getLogger(RoutedMessageListener.class);

    @StreamListener(MessagingConfig.Bindings.RED)
    public void receiveRedMessage(@Payload RoutedMessage message) {
        logReceiveMessage("red", message);
    }

    @StreamListener(MessagingConfig.Bindings.GREEN)
    public void receiveGreenMessage(@Payload RoutedMessage message) {
        logReceiveMessage("green", message);
    }

    private void logReceiveMessage(String color, RoutedMessage message) {
        logger.info(String.format("Got message: Id=[%s] Greeting=[%s] ColorMatch=[%s]",
            message.getId(),
            message.getGreeting(),
            Boolean.valueOf(color.equals(message.getColor())).toString()));
    }
}
