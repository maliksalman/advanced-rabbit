package com.smalik.advancedrabbit.retries;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Profile({"retries", "dlq", "dlq2"})
@Component
public class MainListener {

    private static Logger logger = LoggerFactory.getLogger(MainListener.class);

    @StreamListener(MessagingConfig.Bindings.MAIN)
    public void processMainMessages (
            @Payload String payload,
            @Headers Map<String, Object> headers) throws IOException {

        if (payload.startsWith("fail")) {
            logger.info("Rejecting: " + payload + ", " + new ObjectMapper().writeValueAsString(headers));
            throw new RuntimeException("rejecting");
        } else {
            logger.info("Message: " + payload);
        }
    }
}
