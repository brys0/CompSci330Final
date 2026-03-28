package org.uwgb.compsci330.client_sdk.event;

@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T event);
}
