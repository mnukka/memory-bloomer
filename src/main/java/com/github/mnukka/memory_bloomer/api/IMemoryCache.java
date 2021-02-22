package com.github.mnukka.memory_bloomer.api;

public interface IMemoryCache<T> {
    boolean isKeyPresent(T key);
}
