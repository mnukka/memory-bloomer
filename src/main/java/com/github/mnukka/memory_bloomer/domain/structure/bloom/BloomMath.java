package com.github.mnukka.memory_bloomer.domain.structure.bloom;

import java.math.BigDecimal;
import java.math.RoundingMode;


/**
 * Bloom math.
 * Helper functions to support blooming :)
 *
 * @see <a href="https://en.wikipedia.org/wiki/Bloom_filter">Wikipedia Bloomfilter</a> for more background.
 *
 * @author Miko Nukka
 */
final class BloomMath {

    /**
     * Collision probability in bitmap.
     *
     * This method is provided for custom BloomFilter implementations.
     * Where user may want to create a dynamically scaling bloom filter by a given probability for collision.
     *
     * @param k number of hash functions
     * @param n number of expected elements to be inserted into bloom filter's bitmap
     * @param m number of bits in bitmap
     * @return the probability of collision
     */
    public static double findCollisionProbability(int k, int n, int m) {
        BigDecimal bk = BigDecimal.valueOf(k);
        BigDecimal minusOne = BigDecimal.valueOf(-1);
        BigDecimal bn = BigDecimal.valueOf(n);
        BigDecimal bm = BigDecimal.valueOf(m);

        BigDecimal knDivBym = minusOne
                .multiply(
                        bk.multiply(bn)
                                .divide(bm, 3, RoundingMode.HALF_UP)
                );
        return Math.pow((1 - Math.exp(knDivBym.doubleValue())), k);
    }

    /**
     * Optimal bits count for bitmap when given k, n, p
     *
     * @param k number of hash functions
     * @param n number of expected elements to be inserted into bloom filter's bitmap
     * @param p probability
     * @return number of recommended bits in bitmap to satisfy p with n & k
     * @apiNote Keep in mind that double will introduce rounding precision errors.
     * Around 1 bit worth of extra complexity.
     */
    public static double optimalBits(int k, int n, double p) {
        return n * (-k / Math.log(1 - Math.exp(Math.log(p) / k)));
    }

}

