package com.crown.CloudEvents;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.cloudevents.CloudEvent;
import io.cloudevents.v1.AttributesImpl;
import io.cloudevents.v1.CloudEventBuilder;
import io.cloudevents.v1.CloudEventImpl;
import io.cloudevents.v1.ContextAttributes;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Event<T> {
    private static final String HEADER_PREFIX = "ce-";
    private final CloudEvent<AttributesImpl, T> cloudEvent;

    private Event(Builder<T> builder) {
        CloudEventBuilder<T> cloudEventBuilder = CloudEventBuilder.<T>builder()
                .withId(builder.id)
                .withSource(URI.create(builder.source))
                .withType(builder.type)
                .withSubject(builder.subject)
                .withTime(ZonedDateTime.parse(builder.time))
                .withDataContentType(builder.datacontenttype)
                .withData(builder.data)
                .withExtension(new ContentVersionFormat(builder.contentVersion));
        this.cloudEvent = cloudEventBuilder.build();
    }

    private Event(CloudEventImpl<T> cloudEvent) {
        this.cloudEvent = cloudEvent;
    }

    public Map<String, String> getHeaders() {
        AttributesImpl attributes = cloudEvent.getAttributes();
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_PREFIX + ContextAttributes.id.name(), attributes.getId());
        headers.put(HEADER_PREFIX + ContextAttributes.source.name(), attributes.getSource().toString());
        headers.put(HEADER_PREFIX + ContextAttributes.type.name(), attributes.getType());
        headers.put(HEADER_PREFIX + ContextAttributes.subject.name(), attributes.getSubject().orElse(null));
        headers.put(HEADER_PREFIX + ContextAttributes.specversion.name(), attributes.getSpecversion());
        headers.put(HEADER_PREFIX + ContextAttributes.datacontenttype.name(), attributes.getDatacontenttype().orElse(null));
        headers.put(HEADER_PREFIX + ContextAttributes.time.name(), attributes.getTime().map(ZonedDateTime::toString).orElse(null));
        headers.putAll(cloudEvent.getExtensions().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, stringObjectEntry -> stringObjectEntry.getValue().toString())));
        return headers;
    }

    public Optional<T> getPayload() {
        return cloudEvent.getData();
    }

    public CloudEvent<AttributesImpl, T> toCloudEvents() {
        return cloudEvent;
    }

    /**
     * Used by the Jackson Framework to unmarshall.
     */
    @JsonCreator
    public static <T> Event<T> build(
            @JsonProperty("id") String id,
            @JsonProperty("source") URI source,
            @JsonProperty("type") String type,
            @JsonProperty("datacontenttype") String datacontenttype,
            @JsonProperty("subject") String subject,
            @JsonProperty("time") ZonedDateTime time,
            @JsonProperty("specversion") String specversion,
            @JsonProperty("data") T data,
            // Extensions
            @JsonProperty("ce-contentVersion") String contentVersion
            ){

        CloudEventBuilder<T> cloudEventBuilder = CloudEventBuilder.<T>builder()
                .withId(id)
                .withSource(source)
                .withType(type)
                .withTime(time)
                .withDataContentType(datacontenttype)
                .withData(data)
                .withSubject(subject);
        cloudEventBuilder.withExtension(new ContentVersionFormat(contentVersion));
        return new Event<>(cloudEventBuilder.build());
    }

    public static class Builder<T> {
        private String id;
        private String source;
        private String type;
        private String datacontenttype;
        private String subject;
        private String time;
        private String contentVersion;
        private T data;

        private Builder() {

        }

        public static <T> Builder<T> newBuilder() {
            return new Builder<>();
        }

        public Event<T> build() {
            if (id == null) {
                throw new IllegalStateException("Id is required");
            }
            if (source == null) {
                throw new IllegalStateException("Source is required");
            }
            if (type == null) {
                throw new IllegalStateException("Type is required");
            }
            return new Event<>(this);
        }

        public Builder<T> withId(String id) {
            this.id = id;
            return this;
        }

        public Builder<T> withSource(String source) {
            this.source = source;
            return this;
        }

        public Builder<T> withType(String type) {
            this.type = type;
            return this;
        }

        public Builder<T> withSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder<T> withDatacontenttype(String datacontenttype) {
            this.datacontenttype = datacontenttype;
            return this;
        }

        public Builder<T> withTime(String time) {
            this.time = time;
            return this;
        }

        public Builder<T> withContentVersion(String contentVersion) {
            this.contentVersion = contentVersion;
            return this;
        }

        public Builder<T> withData(T data) {
            this.data = data;
            return this;
        }
    }


}
