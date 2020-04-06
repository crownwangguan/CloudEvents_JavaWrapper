package com.crown.CloudEvents;

import io.cloudevents.json.Json;
import org.junit.Before;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

public class EventTest {

    private Event<String> cloudEventMessage;

    @Before
    public void initiateMessage() {
        String message = "Hello World";
        cloudEventMessage = Event.Builder.<String>newBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource("com.crown.CloudEvents")
                .withType("string")
                .withSubject(UUID.randomUUID().toString())
                .withTime(ZonedDateTime.now().toString())
                .withContentVersion("1.0")
                .withData(message)
                .build();
    }

    @Test
    public void serializationPayload() {
        String payload = Json.encode(cloudEventMessage.toCloudEvents());
        System.out.println(payload);
    }

    @Test
    public void deSerializationPayload() {
        String payload = Json.encode(cloudEventMessage.toCloudEvents());
        System.out.println(payload);

        Event<String> payloadDeserialization = Json.decodeValue(payload, Event.class, String.class);
        System.out.println(payloadDeserialization);
    }
}
