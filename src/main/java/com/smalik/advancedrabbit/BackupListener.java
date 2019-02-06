package com.smalik.advancedrabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class BackupListener {

    private static Logger logger = LoggerFactory.getLogger(BackupListener.class);

    @StreamListener(MessagingConfig.Bindings.BACKUP )
    public void processBackupMessages (
            @Payload String payload,
            @Headers Map<String, Object> headers) throws IOException {

        if (payload.startsWith("fail")) {
            logger.info("Rejecting Backup Message: " + payload + ", " + new ObjectMapper().writeValueAsString(headers));
            throw new RuntimeException("rejecting");
        } else {
            logger.info("Got Backup Message: " + payload);
        }
    }
}
