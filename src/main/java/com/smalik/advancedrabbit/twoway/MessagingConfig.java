package com.smalik.advancedrabbit.twoway;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.MessageChannel;

@Profile("twoway")
@EnableBinding(MessagingConfig.Bindings.class)
public class MessagingConfig {

    public interface Bindings {

        String CONSUMER = "consumer";

        @Input(CONSUMER)
        MessageChannel getConsumerChannel();

        String PRODUCER = "producer";

        @Output(PRODUCER)
        MessageChannel getProducerChannel();

    }
}
