package com.smalik.advancedrabbit.transactions;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("transactions")
@EnableBinding(MessagingConfig.Bindings.class)
@EnableJpaRepositories
@EnableTransactionManagement
@Configuration
public class MessagingConfig {

    public interface Bindings {

        String TXIN = "txin";

        @Input(TXIN)
        SubscribableChannel getTxInputChannel();

        String TXOUT = "txout";

        @Output(TXOUT)
        MessageChannel getTxOutputChannel();
    }
}
