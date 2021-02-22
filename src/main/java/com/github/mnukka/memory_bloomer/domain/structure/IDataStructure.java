package com.github.mnukka.memory_bloomer.domain.structure;

import com.github.mnukka.memory_bloomer.api.IMemoryCache;
import com.github.mnukka.memory_bloomer.domain.hash.IHashFunction;

import java.util.List;

/**
 * The interface Data structure.
 *
 * @param <T> the type parameter for data which should be stored in the cache
 *
 * @author Miko Nukka
 */
public interface IDataStructure<T> {

    /**
     * Create memory cache.
     *
     * @param hashingMethods the hashing methods
     * @param input          the input
     * @return the memory cache
     */
    IMemoryCache createCache(List<IHashFunction> hashingMethods, List<T> input);
}
