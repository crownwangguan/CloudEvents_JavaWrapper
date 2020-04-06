package com.crown.CloudEvents;

import io.cloudevents.extensions.ExtensionFormat;
import io.cloudevents.extensions.InMemoryFormat;

import java.util.HashMap;
import java.util.Map;

public class ContentVersionFormat implements ExtensionFormat {

    private static final String IN_MEMORY_KEY = "ce-contentVersion";
    private final InMemoryFormat memory;
    private final Map<String, String> transport = new HashMap<>();

    public ContentVersionFormat(String version) {
        String contentVersion = version == null? "latest": version;
        this.memory = InMemoryFormat.of(IN_MEMORY_KEY, contentVersion, String.class);
        this.transport.put(IN_MEMORY_KEY, contentVersion);
    }

    @Override
    public InMemoryFormat memory() {
        return memory;
    }

    @Override
    public Map<String, String> transport() {
        return transport;
    }
}
