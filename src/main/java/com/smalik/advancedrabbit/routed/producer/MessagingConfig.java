package com.smalik.advancedrabbit.routed.producer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageChannel;

@Profile("routed-producer")
@EnableBinding(MessagingConfig.Bindings.class)
public class MessagingConfig {

    public interface Bindings {

        String PRODUCER = "producer";

        @Output(PRODUCER)
        MessageChannel getProducerChannel();
    }
}
