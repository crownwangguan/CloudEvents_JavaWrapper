# CloudEvents Java Wrapper

Events are everywhere. However, event producers tend to describe events differently.

[CloudEvents](https://cloudevents.io) is a specification for describing event data in common formats to provide interoperability across services, platforms and systems.

This repository is an example to demonstrate how to use CloudEvents.
One cloudevent can be created simply by:
```
Event.Builder.<String>newBuilder()
      .withId(UUID.randomUUID().toString())
      .withSource("com.crown.CloudEvents")
      .withType("string")
      .withSubject(UUID.randomUUID().toString())
      .withTime(ZonedDateTime.now().toString())
      .withContentVersion("1.0")
      .withData(message)
      .build();
```

If different model class is expected, just change the `<String>` to your class `<T>`

When `Extension` is required, take a reference of class `ContentVersionFormat`. 
This class shows how to add extension to the CloudEvents.