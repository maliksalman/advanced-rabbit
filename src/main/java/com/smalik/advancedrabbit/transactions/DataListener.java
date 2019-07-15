package com.smalik.advancedrabbit.transactions;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Profile("transactions")
public class DataListener {

    private DataRepostiory repostiory;
    private MessagingConfig.Bindings bindings;

    public DataListener(DataRepostiory repostiory, MessagingConfig.Bindings bindings) {
        this.repostiory = repostiory;
        this.bindings = bindings;
    }

    @Transactional
    @StreamListener(MessagingConfig.Bindings.TXIN)
    public void handleDataMessage(@Payload String notes) {

        // save to DB
        Data data = new Data(notes);
        repostiory.save(data);

        // repeat message in output channel
        bindings.getTxOutputChannel().send(MessageBuilder.withPayload(data).build());

        if (data.getNotes().contains("fail")) {
            throw new RuntimeException("something failed");
        }
    }
}
