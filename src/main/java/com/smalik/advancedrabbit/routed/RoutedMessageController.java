package com.smalik.advancedrabbit.routed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoutedMessageController {

    @Autowired
    private RoutedMessagingConfig.Bindings bindings;

    @PostMapping("/send/red/{greeting}")
    public String sendRedColorMessage(@PathVariable("greeting") String greeting) {
        return sendColorMessage("red", greeting);
    }

    @PostMapping("/send/green/{greeting}")
    public String sendGreenColorMessage(@PathVariable("greeting") String greeting) {
        return sendColorMessage("green", greeting);
    }

    private String sendColorMessage(String color, String greeting) {
        RoutedMessage payload = new RoutedMessage(color, greeting);
        Message<RoutedMessage> message = MessageBuilder
                .withPayload(payload)
                .build();
        bindings.getRoutedOutputChannel().send(message);
        return payload.getId();
    }
}
