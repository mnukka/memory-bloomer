package com.github.mnukka.memory_bloomer.domain;

import com.github.mnukka.memory_bloomer.api.IMemoryCache;
import com.github.mnukka.memory_bloomer.domain.hash.IHashFunction;
import com.github.mnukka.memory_bloomer.domain.structure.IDataStructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Cache builder.
 *
 * @author Miko Nukka
 */
public class CacheBuilder {
    private List<IHashFunction> hashFunctionList = new ArrayList<>();
    private IDataStructure dataStructure;

    /**
     * Add hash function to cache builder.
     *
     * @param hashFunction the hash function
     * @return the cache builder
     */
    public CacheBuilder addHashFunction(final IHashFunction hashFunction) {
        hashFunctionList.add(hashFunction);
        return this;
    }

    /**
     * Add data structure cache builder.
     *
     * For example
     *
     * @param dataStructure the data structure
     * @return the cache builder
     */
    public CacheBuilder addDataStructure(IDataStructure dataStructure) {
        this.dataStructure = dataStructure;
        return this;
    }

    /**
     * Build cache memory cache.
     *
     * @param myList the my list
     * @return the memory cache
     */
    public IMemoryCache buildCache(List<?> myList) {
        return dataStructure.createCache(hashFunctionList, myList);
    }
}
