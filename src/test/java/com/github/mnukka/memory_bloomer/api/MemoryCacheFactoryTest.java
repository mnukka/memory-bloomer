package com.github.mnukka.memory_bloomer.api;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MemoryCacheFactoryTest {

    @Test
    void createImmutableCache_ProvidingStringList_FindsKeyFromList() {
        IMemoryCache cache = MemoryCacheFactory.createImmutableCache(Arrays.asList("one", "two"));
        assertTrue(cache.isKeyPresent("one"));
        assertFalse(cache.isKeyPresent("three"));
    }
}