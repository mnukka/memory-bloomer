package com.github.mnukka.memory_bloomer.domain.hash;


import com.github.hasher.mnukka.api.MurmurHash3;

public final class MurmurHash implements IHashFunction {
    @Override
    public int hash32(byte[] input, int maxBitPos) {
        return Math.abs(MurmurHash3.hash32x86(input, 0, input.length, IHashFunction.getSeed()) % maxBitPos);
    }
}
