package com.smalik.advancedrabbit;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding(MessagingConfig.Bindings.class)
public class MessagingConfig {


    public interface Bindings {

        String MAIN = "main";
        String BACKUP = "backup";

        @Input(MAIN)
        SubscribableChannel getMainChannel();

        @Input(BACKUP)
        SubscribableChannel getBackupChannel();
    }
}
