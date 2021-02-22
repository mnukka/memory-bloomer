package com.github.mnukka.memory_bloomer.api;

import com.github.mnukka.memory_bloomer.domain.CacheBuilder;
import com.github.mnukka.memory_bloomer.domain.hash.MurmurHash;
import com.github.mnukka.memory_bloomer.domain.hash.SpookyHash;
import com.github.mnukka.memory_bloomer.domain.structure.bloom.BloomFilter32;

import java.util.List;

/**
 * Memory cache factory.
 *
 * A wrapper class that puts together necessary hashing functions and data structure for end user.
 *
 */
public final class MemoryCacheFactory {
    /**
     * Create memory cache.
     *
     * @param list the input which is to be cached
     * @return the memory cache IMemoryCache interface implementation
     */
    public static IMemoryCache createImmutableCache(List<String> list) {
        return new CacheBuilder()
                .addHashFunction(new MurmurHash())
                .addHashFunction(new SpookyHash())
                .addDataStructure(new BloomFilter32<String>())
                .buildCache(list);
    }
}
