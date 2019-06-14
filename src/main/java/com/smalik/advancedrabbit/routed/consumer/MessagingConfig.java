package com.smalik.advancedrabbit.routed.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.SubscribableChannel;

@Profile("routed-consumer")
@EnableBinding(MessagingConfig.Bindings.class)
public class MessagingConfig {

    public interface Bindings {

        String CONSUMER = "consumer";

        @Input(CONSUMER)
        SubscribableChannel getConsumerChannel();
    }
}
