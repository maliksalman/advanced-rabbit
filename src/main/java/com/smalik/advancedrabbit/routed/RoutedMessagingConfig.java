package com.smalik.advancedrabbit.routed;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding(RoutedMessagingConfig.Bindings.class)
public class RoutedMessagingConfig {

    public interface Bindings {

        String ROUTED = "routed";
        String RED = "redroute";
        String GREEN = "greenroute";

        @Output(ROUTED)
        MessageChannel getRoutedOutputChannel();

        @Input(GREEN)
        SubscribableChannel getGreenChannel();

        @Input(RED)
        SubscribableChannel getRedChannel();
    }
}
