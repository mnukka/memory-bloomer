package com.github.mnukka.memory_bloomer.domain.structure.bloom;

/**
 * Bloom properties.
 *
 * This class holds information about active bloom filter.
 * For the purposes of logging or debugging.
 * Modifying any of the values in this class will not affect
 * associated bloom filter's behaviour.
 *
 * @author Miko Nukka
 */
public final class BloomProperties {
    public int bitmapSize;
    public int hashFunctionCount;
    public double collisionProbability;
    public int seed;

    public int getBitmapSize() {
        return bitmapSize;
    }

    public void setBitmapSize(int bitmapSize) {
        this.bitmapSize = bitmapSize;
    }

    public int getHashFunctionCount() {
        return hashFunctionCount;
    }

    public void setHashFunctionCount(int hashFunctionCount) {
        this.hashFunctionCount = hashFunctionCount;
    }

    public double getCollisionProbability() {
        return collisionProbability;
    }

    public void setCollisionProbability(double collisionProbability) {
        this.collisionProbability = collisionProbability;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
