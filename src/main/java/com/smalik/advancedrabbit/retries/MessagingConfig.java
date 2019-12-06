package com.smalik.advancedrabbit.retries;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.SubscribableChannel;

@Profile({"retries", "dlq", "dlq2"})
@EnableBinding(MessagingConfig.Bindings.class)
public class MessagingConfig {

    public interface Bindings {

        String MAIN = "main";

        @Input(MAIN)
        SubscribableChannel getMainChannel();
    }
}
