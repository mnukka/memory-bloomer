package com.github.mnukka.memory_bloomer.domain.structure.bloom;

import com.github.mnukka.memory_bloomer.api.IMemoryCache;
import com.github.mnukka.memory_bloomer.domain.hash.IHashFunction;
import com.github.mnukka.memory_bloomer.domain.structure.IDataStructure;
import com.github.mnukka.memory_bloomer.domain.structure.exception.UnexpectedIOException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Bloom filter.
 * <p>
 * A Bloom filter is a space-efficient probabilistic data structure,
 * conceived by Burton Howard Bloom in 1970, that is used to test
 * whether an element is a member of a set.
 * False positive matches are possible, but false negatives are not –
 * in other words, a query returns either "possibly in set" or "definitely not in set".
 * In general, with bloom filters, elements can be added to the set,
 * but not removed. However for current implementation the set is built to be immutable.
 * </p>
 *
 * <p>This implementation of Bloom filter creates bitmap with
 * collision probability of p=0.005. It comes with a <i>naive</i>
 * assumption that underlying hashing algorithms distribute data uniformly.</p>
 *
 * @param <T> the type parameter for elements which are to be hashed within bitmap of bloom filter
 * @apiNote This is 32bit bloom filter, meaning maximum number of bits that can be stored is equal to Integer.MAX_VALUE.
 * In laymen terms this means that in order to have collision probability of 0.005 satisfied,
 * maximum number of items which can be stored in the filter is 787_430_24.
 * This would cost 25,6MiB for bitmap size.
 *
 * @author Miko Nukka
 */
public final class BloomFilter32<T> implements IDataStructure<T>, IMemoryCache<T> {
    private BitSet bitMap;
    private List<IHashFunction> hashFunctionList;
    private int bitmapSize;
    private final BloomProperties properties = new BloomProperties();
    private final double COLLISION_PROBABILITY = 0.005;

    /**
     * Create bloom filter cache
     *
     * @param hashFunctionList list of hash functions to be called for each element in the input list
     * @param input            input of data to be hashed and stored in bitmap
     * @return instance of BloomFilter32
     * @throws NullPointerException if <i>hashFunctionList</i> or <i>input</i> is not defined
     * @throws IllegalArgumentException if <i>hashFunctionList</i> or <i>input</i> list does not contain at least one item
     * @throws IllegalArgumentException if <i>input</i> list contains more items than bitmapSize
     * can hold with predetermined collision probability of 0.005
     */
    @Override
    public IMemoryCache createCache(final List<IHashFunction> hashFunctionList, final List<T> input) {
        if (hashFunctionList == null || input == null) {
            throw new NullPointerException("createCache must be provided with non-null arguments as parameters");
        }

        if (hashFunctionList.size() < 1) {
            throw new IllegalArgumentException("Bloom filter must be provided at least one hash function");
        }

        if (input.size() < 1) {
            throw new IllegalArgumentException("Bloom filter must be provided at least one item to be stored in the cache");
        }

        if (input.size() > 78743024) {
            throw new IllegalArgumentException("Bloom filter's bitmap will exceed 32bit in its size and will no longer satisfy 0.005 collision probability");
        }

        this.hashFunctionList = hashFunctionList;
        createBitmap(input, COLLISION_PROBABILITY);
        return this;
    }

    /**
     * Check if value is present in the bitmap for bloom filter
     *
     * @param input key to check which was previously stored in the
     * bitmap during the createCache call
     * @return boolean value which indicates whether the key is
     * <i>possibly in set</i> (true) or <i>definitely not in set</i> (false)
     * @apiNote The probability for false positives (collisions) with default implementation
     * is p=0.005. In laymen terms it means that on average for every 10000 requests to bitmap
     * with keys that have not been indexed, you will get 50 false positives back.
     */
    @Override
    public boolean isKeyPresent(T input) {
        byte[] key = convertObjToByte(input);
        List<Integer> hashes = hashKey(key).collect(Collectors.toList());
        return hashes.stream().allMatch(bitMap::get);
    }

    /**
     * Gets properties for currently built bloom filter.
     *
     * @return the properties
     */
    public BloomProperties getProperties() {
        return properties;
    }

    private void createBitmap(final List<T> input, final double collisionProbability) {
        bitmapSize = (int) BloomMath.optimalBits(hashFunctionList.size(), input.size(), collisionProbability);
        final List<byte[]> byteArrayList = transformToByteArrayList(input);
        bitMap = new BitSet(bitmapSize);
        populateBitmap(byteArrayList);

        properties.setCollisionProbability(collisionProbability);
        properties.setBitmapSize(bitmapSize);
        properties.setHashFunctionCount(hashFunctionList.size());
        properties.setSeed(IHashFunction.getSeed());
    }

    private void populateBitmap(final List<byte[]> byteArrayList) {
        byteArrayList.stream()
                .flatMap(this::hashKey)
                .forEach(p -> bitMap.set(p));
    }

    private Stream<Integer> hashKey(final byte[] key) {
        return hashFunctionList.stream().map(p -> p.hash32(key, bitmapSize));
    }

    private List<byte[]> transformToByteArrayList(final List<T> input) {
        List<byte[]> byteList = new ArrayList<>();
        for (Object o : input) {
            byteList.add(convertObjToByte(o));
        }
        return byteList;
    }

    private byte[] convertObjToByte(final Object input) {
        try {
            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
            objectOutputStream.writeObject(input);
            return byteOutputStream.toByteArray();
        } catch (IOException exception) {
            throw new UnexpectedIOException(exception.getMessage());
        }
    }
}
