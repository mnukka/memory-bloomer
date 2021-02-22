package com.github.mnukka.memory_bloomer.domain.hash;

import com.github.hasher.mnukka.api.SpookyHash32;

public final class SpookyHash implements IHashFunction {

    @Override
    public int hash32(byte[] input, int maxBitPos) {
        long[] seed = new long[]{IHashFunction.getSeed(), IHashFunction.getSeed()};
        long bit = SpookyHash32.hash(input, seed) % maxBitPos;
        return (int) Math.abs(bit);
    }
}
