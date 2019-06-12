package com.smalik.advancedrabbit.routed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RoutedMessageListener {

    private Logger logger = LoggerFactory.getLogger(RoutedMessageListener.class);

    @StreamListener(RoutedMessagingConfig.Bindings.RED)
    public void receiveRedMessage(@Payload RoutedMessage message) {
        logReceiveMessage(message);
    }

    @StreamListener(RoutedMessagingConfig.Bindings.GREEN)
    public void receiveGreenMessage(@Payload RoutedMessage message) {
        logReceiveMessage(message);
    }

    private void logReceiveMessage(RoutedMessage message) {
        logger.info(String.format("Color=[%s] Id=[%s] Greeting=[%s]",
            message.getColor(),
            message.getId(),
            message.getGreeting()));
    }
}
