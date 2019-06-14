package com.smalik.advancedrabbit.routed.producer;

import com.smalik.advancedrabbit.routed.RoutedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("routed-producer")
@RestController
public class RoutedMessageController {

    @Autowired
    private MessagingConfig.Bindings bindings;

    @PostMapping("/send/{color}/{word}")
    public String sendColorMessage(@PathVariable("color") String color,
                                   @PathVariable("word") String word) {

        RoutedMessage payload = new RoutedMessage(color, word);
        Message<RoutedMessage> message = MessageBuilder
                .withPayload(payload)
                .build();
        bindings.getProducerChannel().send(message);

        return payload.getId();
    }
}
