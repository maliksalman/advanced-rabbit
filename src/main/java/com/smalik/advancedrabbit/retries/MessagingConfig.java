package com.smalik.advancedrabbit.retries;


import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.SubscribableChannel;

@Profile("retries")
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
