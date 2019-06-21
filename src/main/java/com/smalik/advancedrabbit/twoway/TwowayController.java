package com.smalik.advancedrabbit.twoway;

import com.smalik.advancedrabbit.routed.RoutedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Profile("twoway")
@Controller
public class TwowayController {

    @Autowired
    private MessagingConfig.Bindings bindings;

    @PostMapping("/send/{color}/{word}")
    public void sendColorMessage(@PathVariable("color") String color,
                                   @PathVariable("word") String word) {
        Message<RoutedMessage> message = MessageBuilder
                .withPayload(new RoutedMessage(color, word))
                .build();
        bindings.getProducerChannel().send(message);
    }
}
