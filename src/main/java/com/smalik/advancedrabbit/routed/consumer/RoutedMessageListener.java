package com.smalik.advancedrabbit.routed.consumer;

import com.smalik.advancedrabbit.routed.RoutedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Profile("routed-consumer")
@Component
public class RoutedMessageListener {

    private Logger logger = LoggerFactory.getLogger(RoutedMessageListener.class);

    @StreamListener(MessagingConfig.Bindings.CONSUMER)
    public void receiveMessage(@Payload RoutedMessage message) {

        logger.info(String.format("Id=[%s] Color=[%s] Word=[%s]",
                message.getId(),
                message.getColor(),
                message.getWord()));
    }
}
