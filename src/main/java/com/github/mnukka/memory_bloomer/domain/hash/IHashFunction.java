package com.github.mnukka.memory_bloomer.domain.hash;

public interface IHashFunction {
    int seed = (int)System.nanoTime();

    static int getSeed() {
        return seed;
    }

    int hash32(byte[] input, int maxBitPos);
}
