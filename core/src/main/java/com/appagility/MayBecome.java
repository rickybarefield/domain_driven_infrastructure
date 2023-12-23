package com.appagility;

import java.util.Optional;

/**
 * It will start without a value,
 * then could be set to have a value,
 * once it's set, it can't be set again.
 */
public final class MayBecome<T> {

    private final String name;
    private Optional<T> value = Optional.empty();

    private MayBecome(String name) {

        this.name = name;
    }

    public static <T> MayBecome<T> empty(String nameOfField) {

        return new MayBecome<>(nameOfField);
    }

    public synchronized void set(T value) {

        if(this.value.isPresent()) {

            throw new RuntimeException("Attempt to set " + name + " but it is already set");
        }

        this.value = Optional.of(value);
    }

    public boolean isSet() {

        return value.isPresent();
    }

    public T get() {

        return value.orElseThrow(() -> new RuntimeException("Attempt to get value of " + name + "but it has not been set"));
    }
}
